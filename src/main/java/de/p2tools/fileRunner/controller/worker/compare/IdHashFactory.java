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

public class IdHashFactory {
    private static boolean stop = false;
    private static int idHash = 0;

    public static void setStop() {
        stop = true;
    }

    public static void setIdHash() {
        ProgData progData = ProgData.getInstance();
        FileDataList fileDataList1 = progData.fileDataList_1;
        FileDataList fileDataList2 = progData.fileDataList_2;
        stop = false;
        idHash = 0;

        // erst mal alle auf 0 setzen
        fileDataList1.stream().forEach(fd -> {
            fd.setIdHash(0);
        });
        fileDataList2.stream().forEach(fd -> {
            fd.setIdHash(0);
        });

        setIdHash(fileDataList1, fileDataList2);
    }

    private static void setIdHash(FileDataList fileDataList1, FileDataList fileDataList2) {
        //HashID für Dateien über den Hash setzen
        //hier müssen beide Listen immer ganz durchsucht werden, denn
        //es kann gleiche Dateien in unterschiedlichen Verzeichnissen geben
        //und dummerweise können die auch mehrfach vorkommen :(

        PLog.sysLog("HashID setzen für das Verzeichnis:" + P2LibConst.LINE_SEPARATOR
                + fileDataList1.getSourceDir() + P2LibConst.LINE_SEPARATOR + fileDataList2.getSourceDir());

        //erst mal in der eigenen Liste suchen
        fileDataList1.stream()
                .filter(fileData -> {
                    if (fileData.getIdHash() == 0) {
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
                            .filter(fd2 -> !fd2.equals(fd1) && fd2.getIdHash() == 0 &&
                                    fd2.getHash().equals(fd1.getHash()))
                            .forEach(fd3 -> {
                                if (fd1.getIdHash() == 0) {
                                    getNextHashId();
                                    fd1.setIdHash(idHash);
                                    fd3.setIdHash(idHash);
                                } else {
                                    fd3.setIdHash(fd1.getIdHash());
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
                        if (fd1.getIdHash() == 0) {
                            getNextHashId();
                            fd1.setIdHash(idHash);
                            fd2.setIdHash(idHash);
                        } else {
                            fd2.setIdHash(fd1.getIdHash());
                        }
                    });
        });

        //und jetzt noch doppelte in der anderen Liste suchen
        fileDataList2.stream()
                .filter(fileData -> {
                    if (fileData.getIdHash() == 0) {
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
                            .filter(fd2 -> !fd2.equals(fd1) && fd2.getIdHash() == 0 &&
                                    fd2.getHash().equals(fd1.getHash()))
                            .forEach(fd3 -> {
                                if (fd1.getIdHash() == 0) {
                                    getNextHashId();
                                    fd1.setIdHash(idHash);
                                    fd3.setIdHash(idHash);
                                } else {
                                    fd3.setIdHash(fd1.getIdHash());
                                }
                            });
                });
    }

    private static synchronized int getNextHashId() {
        return ++idHash;
    }
}
