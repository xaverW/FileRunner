/*
 * P2tools Copyright (C) 2018 W. Xaver W.Xaver[at]googlemail.com
 * https://www.p2tools.de/
 *
 * This program is free software: you can redistribute it and/or modify it under the terms of the
 * GNU General Public License as published by the Free Software Foundation, either version 3 of the
 * License, or any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with this program. If
 * not, see <http://www.gnu.org/licenses/>.
 */


package de.p2tools.fileRunner.controller.worker.compare;

import de.p2tools.fileRunner.controller.config.Events;
import de.p2tools.fileRunner.controller.config.ProgData;
import de.p2tools.fileRunner.controller.data.fileData.FileData;
import de.p2tools.fileRunner.controller.data.fileData.FileDataList;
import de.p2tools.p2Lib.P2LibConst;
import de.p2tools.p2Lib.tools.events.RunPEvent;
import de.p2tools.p2Lib.tools.log.PLog;

public class CompareFileList {
    private boolean stop = false;
    private int id = 0;

    public void setStop() {
        stop = true;
    }

    public void compareList() {
        ProgData progData = ProgData.getInstance();
        FileDataList fileDataList1 = progData.fileDataList_1;
        FileDataList fileDataList2 = progData.fileDataList_2;
        stop = false;

        // erst mal alle auf diff/only setzen
        fileDataList1.stream().forEach(fd -> {
            fd.setSameHash(false);
            fd.setOnly(true);
            fd.setDiff(false);
            fd.setId(0);
        });
        fileDataList2.stream().forEach(fd -> {
            fd.setSameHash(false);
            fd.setOnly(true);
            fd.setDiff(false);
            fd.setId(0);
        });

        //jetzt die Dateien vergleichen
        compareWithPath(fileDataList1, fileDataList2);

        //und dann die ID setzen
        setId(fileDataList1, fileDataList2);
//        if (ProgConfig.CONFIG_COMPARE_ONLY_WITH_HASH.getValue()) {
//            setIdWithHash(fileDataList1, fileDataList2);
//        } else {
//            setIdWithPath(fileDataList1, fileDataList2);
//        }

        progData.pEventHandler.notifyListener(new RunPEvent(Events.COMPARE_OF_FILE_LISTS_FINISHED));
    }

    public void setFileId() {
        ProgData progData = ProgData.getInstance();
        FileDataList fileDataList1 = progData.fileDataList_1;
        FileDataList fileDataList2 = progData.fileDataList_2;
        stop = false;

        // erst mal bei allen die ID löschen
        fileDataList1.stream().forEach(fd -> {
            fd.setSameHash(false);
            fd.setId(0);
        });
        fileDataList2.stream().forEach(fd -> {
            fd.setSameHash(false);
            fd.setId(0);
        });

        setId(fileDataList1, fileDataList2);
//        if (ProgConfig.CONFIG_COMPARE_ONLY_WITH_HASH.getValue()) {
//            setIdWithHash(fileDataList1, fileDataList2);
//        } else {
//            setIdWithPath(fileDataList1, fileDataList2);
//        }

        progData.pEventHandler.notifyListener(new RunPEvent(Events.COMPARE_OF_FILE_LISTS_FINISHED));
    }

    private void compareWithPath(FileDataList fileDataList1, FileDataList fileDataList2) {
        //und hier Dateien mit Pfad vergleichen
        PLog.sysLog("Vergleichen mit Verzeichnis:" + P2LibConst.LINE_SEPARATOR
                + fileDataList1.getSourceDir() + P2LibConst.LINE_SEPARATOR + fileDataList2.getSourceDir());

        fileDataList1.parallelStream().forEach(fd1 -> {
            if (stop) {
                return;
            }

            FileData fd2;
            fd2 = fileDataList2.stream().filter(data -> data.getPathFileName().equals(fd1.getPathFileName()))
                    .findFirst().orElse(null);

            if (fd2 != null) {
                //dann gibts schon mal eine mit gleichem Namen
                fd1.setOnly(false);
                fd2.setOnly(false);

                if (!fd1.getHash().equals(fd2.getHash())) {
                    //nicht gleich
                    fd1.setDiff(true);
                    fd2.setDiff(true);
                }
            }
        });
    }

    private void setIdWithPath(FileDataList fileDataList1, FileDataList fileDataList2) {
        //und hier FileID für Dateien mit Pfad setzen
        PLog.sysLog("ID für Vergleich mit Verzeichnis:" + P2LibConst.LINE_SEPARATOR
                + fileDataList1.getSourceDir() + P2LibConst.LINE_SEPARATOR + fileDataList2.getSourceDir());

        fileDataList1.parallelStream().forEach(fd1 -> {
            if (stop) {
                return;
            }

            FileData fd2;
            fd2 = fileDataList2.stream().filter(data -> data.getPathFileName().equals(fd1.getPathFileName()))
                    .findFirst().orElse(null);

            if (fd2 != null) {
                //dann gibts schon mal eine mit gleichem Namen
                if (fd1.getHash().equals(fd2.getHash())) {
                    //sind gleich
                    if (fd1.getId() == 0) {
                        int id = getNextId();
                        fd1.setId(id);
                    }
                    fd2.setId(id);
                }
            }
        });
    }

    private void setIdWithHash(FileDataList fileDataList1, FileDataList fileDataList2) {
        //FileID für Dateien nur über den Hash setzen
        //hier müssen beide Listen immer ganz durchgesucht werden, denn
        //es kann gleiche Dateien in unterschiedlichen Verzeichnissen geben
        //und dummerweise können die auch mehrfach vorkommen :(

        PLog.sysLog("ID für Vergleich ohne Verzeichnis:" + P2LibConst.LINE_SEPARATOR
                + fileDataList1.getSourceDir() + P2LibConst.LINE_SEPARATOR + fileDataList2.getSourceDir());


        fileDataList1.stream().forEach(fd1 -> {
            //erst mal Doppelte in List1 suchen
            if (stop) {
                return;
            }

            if (fd1.getId() == 0) {
                //sonst ist er schon markiert

                fileDataList1.stream().forEach(f1 -> {
                    if (!f1.equals(fd1) && f1.getHash().equals(fd1.getHash())) {
                        //die "gleichen" sind ja immer gleich :)
                        //also nur andere gleiche suchen
                        if (fd1.getId() == 0) {
                            //dann ists der erste Treffer
                            int id = getNextId();
                            fd1.setId(id);
                        }
                        f1.setId(fd1.getId());
                    }
                });
            }
        });

        //und jetzt gleiche in der Liste2 suchen
        fileDataList1.parallelStream().forEach(fd1 -> {
            if (stop) {
                return;
            }

            fileDataList2.stream()
                    .filter(fd2 -> fd1.getHash().equals(fd2.getHash()))
                    //dann ist der Hash gleich
                    .forEach(fd2 -> {
                        if (fd1.getId() == 0) {
                            int id = getNextId();
                            fd1.setId(id);
                        }
                        fd2.setId(fd1.getId());
                    });
        });
    }

    private void setId(FileDataList fileDataList1, FileDataList fileDataList2) {
        //FileID für Dateien nur über den Hash setzen
        //hier müssen beide Listen immer ganz durchgesucht werden, denn
        //es kann gleiche Dateien in unterschiedlichen Verzeichnissen geben
        //und dummerweise können die auch mehrfach vorkommen :(

        PLog.sysLog("ID für Vergleich ohne Verzeichnis:" + P2LibConst.LINE_SEPARATOR
                + fileDataList1.getSourceDir() + P2LibConst.LINE_SEPARATOR + fileDataList2.getSourceDir());

        //Liste1 aufbereiten
        fileDataList1.stream().forEach(fd1 -> {
            //erst mal Doppelte in List1 suchen
            if (stop) {
                return;
            }

            if (fd1.getId() == 0) {
                //sonst ist er schon markiert
                fileDataList1.stream().forEach(f1 -> {
                    if (!f1.equals(fd1) && f1.getHash().equals(fd1.getHash())) {
                        //die "gleichen" sind ja immer gleich :)
                        //also nur andere gleiche suchen
                        if (fd1.getId() == 0) {
                            //dann ists der erste Treffer
                            int id = getNextId();
                            fd1.setId(id);
                        }
                        f1.setId(fd1.getId());
                        fd1.setSameHash(true);
                        f1.setSameHash(true);
                    }
                });
            }
            if (fd1.getId() == 0) {
                int id = getNextId();
                fd1.setId(id);
            }
        });


        //und jetzt das Gleiche in der Liste2 suchen
        fileDataList2.stream().forEach(fd2 -> {
            fileDataList1.stream().forEach(fd1 -> {
                if (fd2.getHash().equals(fd1.getHash())) {
                    fd2.setId(fd1.getId());
                    fd1.setSameHash(true);
                    fd2.setSameHash(true);
                }
            });

            //und die Nicht-Treffer
            if (fd2.getId() == 0) {
                int id = getNextId();
                fd2.setId(id);
                fileDataList2.stream().forEach(f2 -> {
                    if (!fd2.equals(fd2) && fd2.getHash().equals(f2.getHash())) {
                        //dann sind es gleiche in Liste2
                        f2.setId(fd2.getId());
                        fd2.setSameHash(true);
                        f2.setSameHash(true);
                    }
                });
            }
        });
    }

    private synchronized int getNextId() {
        return ++id;
    }
}
