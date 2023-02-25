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


package de.p2tools.filerunner.controller.worker;

import de.p2tools.filerunner.controller.config.*;
import de.p2tools.filerunner.controller.data.filedata.FileData;
import de.p2tools.filerunner.controller.data.filedata.FileDataList;
import de.p2tools.p2lib.P2LibConst;
import de.p2tools.p2lib.tools.log.PLog;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Optional;

public class CompareFileListFactory {
    private static boolean stop = false;
    private static int id = 0;
    private static BooleanProperty running = new SimpleBooleanProperty(false);
    private static Object obj1 = new Object();
    private static Object obj2 = new Object();

    private CompareFileListFactory() {
    }

    public static void setStop() {
        stop = true;
    }

    public static boolean getRunning() {
        return running.get();
    }

    public static BooleanProperty isRunningProperty() {
        return running;
    }

    public static void compareList() {
        if (running.get()) {
            //dann läuft er schon
            PLog.sysLog("===============> CompareFileListFactory isRunning");
            return;
        }

        synchronized (obj2) {
            compare();
        }
    }

    private static void compare() {
        running.set(true);
        PLog.sysLog("CompareFileListFactory -> compareList");
        ProgData progData = ProgData.getInstance();
        FileDataList fileDataList1 = progData.fileDataList_1;
        FileDataList fileDataList2 = progData.fileDataList_2;
        stop = false;
        id = 0;

        // erst mal alle auf diff/only setzen
        fileDataList1.stream().forEach(fd -> {
            fd.setOnly(true);
            fd.setDiff(false);
            fd.setFilePathId(0);
            fd.setFileNameId(0);
            fd.setHashId(0);
        });
        fileDataList2.stream().forEach(fd -> {
            fd.setOnly(true);
            fd.setDiff(false);
            fd.setFilePathId(0);
            fd.setFileNameId(0);
            fd.setHashId(0);
        });

        //jetzt die Dateien vergleichen
        switch (ProgConfig.CONFIG_COMPARE_FILE.getValue()) {
            case ProgConst.COMPARE_PATH_NAME:
                PLog.sysLog("ProgConst.COMPARE_PATH_NAME");
                compareWithPathAndSetId(fileDataList1, fileDataList2);
                fileDataList1.stream().forEach(fileData -> fileData.setId(fileData.getFilePathId()));
                fileDataList2.stream().forEach(fileData -> fileData.setId(fileData.getFilePathId()));
                break;
            case ProgConst.COMPARE_NAME:
                compareWithNameAndSetId(fileDataList1, fileDataList2);
                PLog.sysLog("CompareFileListFactory: fileDataList1.stream()");
                fileDataList1.stream().forEach(fileData -> fileData.setId(fileData.getFileNameId()));
                PLog.sysLog("CompareFileListFactory: fileDataList2.stream()");
                fileDataList2.stream().forEach(fileData -> fileData.setId(fileData.getFileNameId()));
                break;
            case ProgConst.COMPARE_ALL:
                compareWithHashAndSetId(fileDataList1, fileDataList2);
                fileDataList1.stream().forEach(fileData -> fileData.setId(fileData.getHashId()));
                fileDataList2.stream().forEach(fileData -> fileData.setId(fileData.getHashId()));
                break;
        }

        running.set(false);
        progData.pEventHandler.notifyListener(new RunPEvent(Events.COMPARE_OF_FILE_LISTS_FINISHED));
    }

    private static synchronized int getNextId() {
        return ++id;
    }

    private static void setOnlyDiff(FileDataList fileDataList1, FileDataList fileDataList2) {
        //only und diff setzen
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
                    getNextId();
                    fd1.setFilePathId(id);
                    fd2.setFilePathId(id);
                } else {
                    fd1.setDiff(true);
                    fd2.setDiff(true);
                }
            }
        });
    }

    private static void compareWithNameAndSetId(FileDataList fileDataList1, FileDataList fileDataList2) {
        //hier werden Dateien mit dem gleich Namen vergleichen und ID setzen
        //gleich sind sie, wenn ALLE Dateien mit dem gleichen Namen auch gleich sind!!!
        PLog.sysLog("Vergleichen von Dateien mit gleichem Namen:" + P2LibConst.LINE_SEPARATOR
                + fileDataList1.getSourceDir() + P2LibConst.LINE_SEPARATOR + fileDataList2.getSourceDir());

        //only
        fileDataList1.stream().filter(fileData -> fileData.isOnly())
                //sonst hammers schon
                .forEach(fd1 -> {
                    if (stop) {
                        return;
                    }

                    Optional<FileData> optional;
                    optional = fileDataList2.stream()
                            //nur noch die noch nicht erwischten prüfen
                            .filter(fd2 -> fd2.getFileName().equals(fd1.getFileName())).findAny();
                    if (optional.isPresent()) {
                        fileDataList1.stream()
                                //dann wird auch fd1 mit erwischt
                                .filter(fileData -> fileData.getFileName().equals(fd1.getFileName()))
                                .forEach(fileData -> fileData.setOnly(false));
                        fileDataList2.stream()
                                //dann wird auch fd1 mit erwischt
                                .filter(fileData -> fileData.getFileName().equals(fd1.getFileName()))
                                .forEach(fileData -> fileData.setOnly(false));
                    }
                });

        //und jetzt werden nur Dateien mit "not ONLY" verglichen
        ArrayList<FileData> listFound = new ArrayList<>();
        ArrayList<FileData> listNotFound = new ArrayList<>();

        fileDataList1.stream().filter(fileData -> !fileData.isOnly())
                .forEach(fd1 -> {

                    Optional<FileData> optional;
                    boolean found = false;
                    optional = fileDataList1.stream()
                            .filter(fileData -> !fileData.equals(fd1) &&
                                    fileData.getFileName().equals(fd1.getFileName()) &&
                                    !fileData.getHash().equals(fd1.getHash())).findAny();

                    if (optional.isPresent()) {
                        found = true;
                    }

                    if (!found) {
                        //dann versuchen wirs noch in der 2. Liste
                        optional = fileDataList2.stream()
                                .filter(fd -> fd.getFileName().equals(fd1.getFileName()) &&
                                        !fd.getHash().equals(fd1.getHash())).findAny();
                        if (optional.isPresent()) {
                            found = true;
                        }
                    }

                    if (!found) {
                        //dann sind alle gleich :)
                        final int i;
                        if (fd1.getFileNameId() > 0) {
                            i = fd1.getFileNameId();
                        } else {
                            getNextId();
                            i = id;
                        }
                        fileDataList1.stream().filter(fd -> fd.getFileName().equals(fd1.getFileName()))
                                .forEach(fd -> fd.setFileNameId(i));
                        fileDataList2.stream().filter(fd -> fd.getFileName().equals(fd1.getFileName()))
                                .forEach(fd -> fd.setFileNameId(i));

                    } else {
                        //dann gibts unterschiedliche Hashes bei diesem
                        fileDataList1.stream().filter(fd -> fd.getFileName().equals(fd1.getFileName()))
                                .forEach(fd -> fd.setDiff(true));
                        fileDataList2.stream().filter(fd -> fd.getFileName().equals(fd1.getFileName()))
                                .forEach(fd -> fd.setDiff(true));
                    }
                });
    }

    private static void compareWithHashAndSetId(FileDataList fileDataList1, FileDataList fileDataList2) {
        //und hier Dateien nur mit dem Hash vergleichen und ID setzen
        PLog.sysLog("Vergleichen mit Hash:" + P2LibConst.LINE_SEPARATOR
                + fileDataList1.getSourceDir() + P2LibConst.LINE_SEPARATOR + fileDataList2.getSourceDir());
        HashSet<String> hashset = new HashSet<>(fileDataList1.getSize());

        //mit der anderen Liste vergleichen
        //erst mal das HashSet füllen
        fileDataList1.stream().forEach(fd1 -> {
            fileDataList2.stream().filter(fd2 -> fd2.getHash().equals(fd1.getHash()))
                    .forEach(fd2 -> {
                        if (stop) {
                            return;
                        }
                        hashset.add(fd1.getHash());
                    });
        });

        //und jetzt die ID setzen
        fileDataList1.stream().forEach(fd1 -> {
            fileDataList2.stream().filter(fd2 -> fd2.getHash().equals(fd1.getHash()))
                    .forEach(fd2 -> {
                        if (stop) {
                            return;
                        }

                        fd1.setOnly(false);
                        fd2.setOnly(false);
                        //und dann sind sie auch noch gleich
                        if (fd1.getHashId() == 0) {
                            int no = getFromHashSet(hashset, fd1.getHash());
                            fd1.setHashId(no);
                            fd2.setHashId(no);

                        } else {
                            fd2.setHashId(fd1.getHashId());
                        }
                    });
        });

        //in den eigenen Listen doppelte suchen
        searchInList(fileDataList1);
        searchInList(fileDataList2);
    }

    private static int getFromHashSet(HashSet<String> hashSet, String search) {
        int ret = 0;
        for (String h : hashSet) {
            ++ret;
            if (h.equals(search)) {
                break;
            }
        }
        return ret;
    }

    private static void searchInList(FileDataList fileDataList) {
        //und jetzt noch doppelte in der eigenen Liste suchen
        fileDataList.stream()
                .filter(fileData -> {
                    //und nur markieren, wenn es auch welche in der anderen Liste gibt und die
                    //HashId damit >0 ist!!
                    if (fileData.getHashId() > 0) {
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
                            .filter(fd2 -> !fd2.equals(fd1) && fd2.getHashId() == 0 &&
                                    fd2.getHash().equals(fd1.getHash()))
                            .forEach(fd3 -> {
                                fd1.setOnly(false);
                                fd3.setOnly(false);
                                fd3.setHashId(fd1.getHashId());
                            });
                });
    }
}
