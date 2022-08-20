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
import de.p2tools.fileRunner.controller.config.ProgConfig;
import de.p2tools.fileRunner.controller.config.ProgData;
import de.p2tools.fileRunner.controller.data.fileData.FileData;
import de.p2tools.fileRunner.controller.data.fileData.FileDataList;
import de.p2tools.p2Lib.P2LibConst;
import de.p2tools.p2Lib.tools.events.RunPEvent;
import de.p2tools.p2Lib.tools.log.PLog;

public class CompareFileListFactory {
    private static boolean stop = false;
    private static int fileId = 0;

    private CompareFileListFactory() {
    }

    public static void setStop() {
        stop = true;
    }

    public static void compareList() {
        ProgData progData = ProgData.getInstance();
        FileDataList fileDataList1 = progData.fileDataList_1;
        FileDataList fileDataList2 = progData.fileDataList_2;
        stop = false;
        fileId = 0;

        // erst mal alle auf diff/only setzen
        fileDataList1.stream().forEach(fd -> {
            fd.setOnly(true);
            fd.setDiff(false);
            fd.setId(0);
        });
        fileDataList2.stream().forEach(fd -> {
            fd.setOnly(true);
            fd.setDiff(false);
            fd.setId(0);
        });

        //jetzt die Dateien vergleichen
        if (ProgConfig.CONFIG_COMPARE_ONLY_WITH_HASH.getValue()) {
            compareWithHashAndSetId(fileDataList1, fileDataList2);
        } else {
            compareWithPathAndSetId(fileDataList1, fileDataList2);
        }

        progData.pEventHandler.notifyListener(new RunPEvent(Events.COMPARE_OF_FILE_LISTS_FINISHED));
    }

    private static void compareWithHashAndSetId(FileDataList fileDataList1, FileDataList fileDataList2) {
        //und hier Dateien nur mit dem Hash vergleichen und ID setzen
        PLog.sysLog("Vergleichen mit Verzeichnis:" + P2LibConst.LINE_SEPARATOR
                + fileDataList1.getSourceDir() + P2LibConst.LINE_SEPARATOR + fileDataList2.getSourceDir());

        //erst mal in der eigenen Liste suchen
        fileDataList1.stream()
                .filter(fileData -> {
                    if (fileData.getId() == 0) {
                        return true;
                    } else {
                        return false;
                    }
                })
                .forEach(fd1 -> {
                    if (stop) {
                        return;
                    }
                    fileDataList1.stream()
                            .filter(fd2 -> !fd2.equals(fd1) && fd2.getId() == 0 &&
                                    fd2.getHash().equals(fd1.getHash()))
                            .forEach(fd3 -> {
                                getNextFileId();
                                fd1.setOnly(false);
                                fd3.setOnly(false);
                                fd1.setId(fileId);
                                fd3.setId(fileId);
                            });
                });

        //und dann mit der anderen Liste vergleichen
        fileDataList1.stream().forEach(fd1 -> {
            if (stop) {
                return;
            }
            fileDataList2.stream().filter(fd2 -> fd2.getHash().equals(fd1.getHash()))
                    .forEach(fd2 -> {
                        //dann gibts eine mit gleichem Hash
                        fd1.setOnly(false);
                        fd2.setOnly(false);

                        //und dann sind sie auch noch gleich
                        fd1.setDiff(false);
                        fd2.setDiff(false);
                        if (fd1.getId() == 0) {
                            getNextFileId();
                            fd1.setId(fileId);
                            fd2.setId(fileId);
                        } else {
                            fd2.setId(fd1.getId());
                        }
                    });
        });

        //und jetzt noch doppelte in der anderen Liste suchen
        fileDataList2.stream()
                .filter(fileData -> {
                    if (fileData.getId() == 0) {
                        return true;
                    } else {
                        return false;
                    }
                })
                .forEach(fd1 -> {
                    if (stop) {
                        return;
                    }
                    fileDataList2.stream()
                            .filter(fd2 -> !fd2.equals(fd1) && fd2.getId() == 0 &&
                                    fd2.getHash().equals(fd1.getHash()))
                            .forEach(fd3 -> {
                                getNextFileId();
                                fd1.setOnly(false);
                                fd3.setOnly(false);
                                fd1.setId(fileId);
                                fd3.setId(fileId);
                            });
                });
    }

    private static void compareWithPathAndSetId(FileDataList fileDataList1, FileDataList fileDataList2) {
        //und hier Dateien mit Pfad vergleichen und ID setzen
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

                if (fd1.getHash().equals(fd2.getHash())) {
                    //dann sind sie auch noch gleich
                    fd1.setDiff(false);
                    fd2.setDiff(false);
                    getNextFileId();
                    fd1.setId(fileId);
                    fd2.setId(fileId);
                } else {
                    fd1.setDiff(true);
                    fd2.setDiff(true);
                }
            }
        });
    }

    private static synchronized int getNextFileId() {
        return ++fileId;
    }
}
