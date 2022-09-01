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
import de.p2tools.fileRunner.controller.config.ProgConst;
import de.p2tools.fileRunner.controller.config.ProgData;
import de.p2tools.fileRunner.controller.data.fileData.FileData;
import de.p2tools.fileRunner.controller.data.fileData.FileDataList;
import de.p2tools.p2Lib.P2LibConst;
import de.p2tools.p2Lib.tools.events.RunPEvent;
import de.p2tools.p2Lib.tools.log.PLog;

import java.util.Optional;

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
            fd.setFileId(0);
        });
        fileDataList2.stream().forEach(fd -> {
            fd.setOnly(true);
            fd.setDiff(false);
            fd.setFileId(0);
        });

        //jetzt die Dateien vergleichen
        switch (ProgConfig.CONFIG_COMPARE_FILE.getValue()) {
            case ProgConst.COMPARE_PATH_NAME:
                compareWithPathAndSetId(fileDataList1, fileDataList2);
                break;
            case ProgConst.COMPARE_NAME:
                compareWithNameAndSetId(fileDataList1, fileDataList2);
                break;
            case ProgConst.COMPARE_ALL:
                compareWithHashAndSetId(fileDataList1, fileDataList2);
                break;
        }

        if (ProgConfig.CONFIG_COMPARE_FILE.getValue() == ProgConst.COMPARE_ALL) {
            fileDataList1.stream().forEach(fileData -> fileData.setId(fileData.getHashId()));
            fileDataList2.stream().forEach(fileData -> fileData.setId(fileData.getHashId()));
        } else {
            fileDataList1.stream().forEach(fileData -> fileData.setId(fileData.getFileId()));
            fileDataList2.stream().forEach(fileData -> fileData.setId(fileData.getFileId()));
        }

        progData.pEventHandler.notifyListener(new RunPEvent(Events.COMPARE_OF_FILE_LISTS_FINISHED));
    }

    private static synchronized int getNextFileId() {
        return ++fileId;
    }

    private static void compareWithPathAndSetId(FileDataList fileDataList1, FileDataList fileDataList2) {
        //und hier Dateien mit Pfad vergleichen und ID setzen
        PLog.sysLog("Vergleichen von Dateien mit gleichem Verzeichnis/Namen:" + P2LibConst.LINE_SEPARATOR
                + fileDataList1.getSourceDir() + P2LibConst.LINE_SEPARATOR + fileDataList2.getSourceDir());

        fileDataList1.stream().forEach(fd1 -> {
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
                    getNextFileId();
                    fd1.setFileId(fileId);
                    fd2.setFileId(fileId);
                } else {
                    fd1.setDiff(true);
                    fd2.setDiff(true);
                }
            }
        });
    }

    private static void compareWithNameAndSetId(FileDataList fileDataList1, FileDataList fileDataList2) {
        //und hier Dateien mit Pfad vergleichen und ID setzen
        PLog.sysLog("Vergleichen von Dateien mit gleichem Namen:" + P2LibConst.LINE_SEPARATOR
                + fileDataList1.getSourceDir() + P2LibConst.LINE_SEPARATOR + fileDataList2.getSourceDir());

        //diff setzen
        setDiff(fileDataList1, fileDataList2);

        fileDataList1.stream().forEach(fd1 -> {
            if (stop) {
                return;
            }

            fileDataList2.stream().filter(fdFilter -> fdFilter.getFileName().equals(fd1.getFileName()))
                    .forEach(fd2 -> {
                        //dann gibts schon mal eine mit gleichem Namen
//                        fd1.setOnly(false);
//                        fd2.setOnly(false);

                        if (fd1.getHash().equals(fd2.getHash())) {
                            //dann sind sie auch noch gleich
//                            fd1.setDiff(false);
                            if (fd1.getFileId() == 0 && fd2.getFileId() == 0) {
                                getNextFileId();
                                fd1.setFileId(fileId);
                                fd2.setFileId(fileId);
                            } else if (fd1.getFileId() > 0) {
                                //dann wurde sie schon gesetzt
                                fd2.setFileId(fd1.getFileId());
                            } else {
                                fd1.setFileId(fd2.getFileId());
                            }

                        } else {
//                            fd1.setDiff(true);
//                            fd2.setDiff(true);
                        }
                    });
        });
    }

    private static void setDiff(FileDataList fileDataList1, FileDataList fileDataList2) {
        //diff setzen
        fileDataList1.stream().forEach(fd1 -> {
            if (stop) {
                return;
            }

            Optional<FileData> opt = fileDataList2.stream()
                    .filter(data -> data.getPathFileName().equals(fd1.getPathFileName()))
                    .findAny();
            if (opt.isPresent()) {
                FileData fd2 = opt.get();
                //dann gibts schon mal eine mit gleichem Pfad/Namen
                fd1.setOnly(false);
                fd2.setOnly(false);
                if (!fd1.getHash().equals(fd2.getHash())) {
                    //dann sind sie nicht gleich
                    fd1.setDiff(true);
                    fd2.setDiff(true);
                }
            }
        });
    }

    private static void compareWithHashAndSetId(FileDataList fileDataList1, FileDataList fileDataList2) {
        //und hier Dateien nur mit dem Hash vergleichen und ID setzen
        PLog.sysLog("Vergleichen mit Verzeichnis:" + P2LibConst.LINE_SEPARATOR
                + fileDataList1.getSourceDir() + P2LibConst.LINE_SEPARATOR + fileDataList2.getSourceDir());

        //diff setzen
        setDiff(fileDataList1, fileDataList2);

        //erst mal in der eigenen Liste doppelte suchen
        searchInList(fileDataList1);

        //und dann mit der anderen Liste vergleichen
        fileDataList1.stream().forEach(fd1 -> {
            fileDataList2.stream().filter(fd2 -> fd2.getHash().equals(fd1.getHash()))
                    .forEach(fd2 -> {
                        if (stop) {
                            return;
                        }
                        //dann gibts eine mit gleichem Hash
//                        if (fd1.getPathFileName().equals(fd2.getPathFileName())) {
//                            fd1.setOnly(false);
//                            fd2.setOnly(false);
//                        }
                        fd1.setOnly(false);
                        fd2.setOnly(false);
                        //und dann sind sie auch noch gleich
                        if (fd1.getFileId() == 0) {
                            getNextFileId();
                            fd1.setFileId(fileId);
                            fd2.setFileId(fileId);
                        } else {
                            fd2.setFileId(fd1.getFileId());
                        }
                    });
        });

        //und jetzt noch doppelte in der anderen Liste suchen
        searchInList(fileDataList2);
    }

    private static void searchInList(FileDataList fileDataList) {
        //und jetzt noch doppelte in der anderen Liste suchen
        fileDataList.stream()
                .filter(fileData -> {
                    if (fileData.getFileId() == 0) {
                        return true;
                    } else {
                        return false;
                    }
                })
                .forEach(fd1 -> {
                    if (stop) {
                        return;
                    }
                    fileDataList.stream()
                            .filter(fd2 -> !fd2.equals(fd1) && fd2.getFileId() == 0 &&
                                    fd2.getHash().equals(fd1.getHash()))
                            .forEach(fd3 -> {
                                getNextFileId();
//                                if (fd1.getPathFileName().equals(fd3.getPathFileName())) {
//                                    fd1.setOnly(false);
//                                    fd3.setOnly(false);
//                                }
                                fd1.setOnly(false);
                                fd3.setOnly(false);
                                fd1.setFileId(fileId);
                                fd3.setFileId(fileId);
                            });
                });
    }
}
