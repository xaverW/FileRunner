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

import de.p2tools.fileRunner.controller.config.ProgData;
import de.p2tools.fileRunner.controller.data.fileData.FileDataList;
import de.p2tools.p2Lib.P2LibConst;
import de.p2tools.p2Lib.tools.log.PLog;

public class HashIdFactory {
    private static boolean stop = false;
    private static int hashId = 0;

    public static void setStop() {
        stop = true;
    }

    public static void setHashId() {
        ProgData progData = ProgData.getInstance();
        FileDataList fileDataList1 = progData.fileDataList_1;
        FileDataList fileDataList2 = progData.fileDataList_2;
        stop = false;
        hashId = 0;

        // erst mal alle auf 0 setzen
        fileDataList1.stream().forEach(fd -> {
            fd.setHashId(0);
        });
        fileDataList2.stream().forEach(fd -> {
            fd.setHashId(0);
        });

        setHashId(fileDataList1, fileDataList2);
    }

    private static void setHashId(FileDataList fileDataList1, FileDataList fileDataList2) {
        //HashID für Dateien über den Hash setzen
        //hier müssen beide Listen immer ganz durchgesucht werden, denn
        //es kann gleiche Dateien in unterschiedlichen Verzeichnissen geben
        //und dummerweise können die auch mehrfach vorkommen :(

        PLog.sysLog("HashID setzen für das Verzeichnis:" + P2LibConst.LINE_SEPARATOR
                + fileDataList1.getSourceDir() + P2LibConst.LINE_SEPARATOR + fileDataList2.getSourceDir());

        //erst mal in der eigenen Liste suchen
        fileDataList1.stream()
                .filter(fileData -> {
                    if (fileData.getHashId() == 0) {
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
                            .filter(fd2 -> !fd2.equals(fd1) && fd2.getHashId() == 0 &&
                                    fd2.getHash().equals(fd1.getHash()))
                            .forEach(fd3 -> {
                                if (fd1.getHashId() == 0) {
                                    getNextHashId();
                                    fd1.setHashId(hashId);
                                    fd3.setHashId(hashId);
                                } else {
                                    fd3.setHashId(fd1.getHashId());
                                }
                            });
                });

        //und dann mit der anderen Liste vergleichen
        fileDataList1.stream().forEach(fd1 -> {
            if (stop) {
                return;
            }
            fileDataList2.stream().filter(fd2 -> fd2.getHash().equals(fd1.getHash()))
                    .forEach(fd2 -> {
                        //und dann sind sie auch noch gleich
                        if (fd1.getHashId() == 0) {
                            getNextHashId();
                            fd1.setHashId(hashId);
                            fd2.setHashId(hashId);
                        } else {
                            fd2.setHashId(fd1.getHashId());
                        }
                    });
        });

        //und jetzt noch doppelte in der anderen Liste suchen
        fileDataList2.stream()
                .filter(fileData -> {
                    if (fileData.getHashId() == 0) {
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
                            .filter(fd2 -> !fd2.equals(fd1) && fd2.getHashId() == 0 &&
                                    fd2.getHash().equals(fd1.getHash()))
                            .forEach(fd3 -> {
                                if (fd1.getHashId() == 0) {
                                    getNextHashId();
                                    fd1.setHashId(hashId);
                                    fd3.setHashId(hashId);
                                } else {
                                    fd3.setHashId(fd1.getHashId());
                                }
                            });
                });
    }

    private static synchronized int getNextHashId() {
        return ++hashId;
    }
}
