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

import de.p2tools.filerunner.controller.config.Events;
import de.p2tools.filerunner.controller.config.ProgConfig;
import de.p2tools.filerunner.controller.config.ProgConst;
import de.p2tools.filerunner.controller.config.ProgData;
import de.p2tools.filerunner.controller.data.filedata.FileData;
import de.p2tools.filerunner.controller.data.filedata.FileDataList;
import de.p2tools.p2lib.P2LibConst;
import de.p2tools.p2lib.p2event.P2Event;
import de.p2tools.p2lib.tools.log.P2Log;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import org.apache.commons.collections4.MultiValuedMap;
import org.apache.commons.collections4.multimap.ArrayListValuedHashMap;

import java.util.Collection;
import java.util.HashMap;

public class CompareFileListFactory {
    private static boolean stop = false;
    private static int id = 0;
    private static int count = 0;
    private static final BooleanProperty running = new SimpleBooleanProperty(false);
    private static final Object obj = new Object();

    private CompareFileListFactory() {
    }

    public static void setStop() {
        stop = true;
    }

    public static BooleanProperty isRunningProperty() {
        return running;
    }

    public static void compareList() {
        if (running.get()) {
            //dann läuft er schon
            P2Log.sysLog("===============> CompareFileListFactory isRunning");
            return;
        }

        synchronized (obj) {
            compare();
        }
    }

    private static void compare() {
        running.set(true);
        P2Log.sysLog("CompareFileListFactory -> compareList");
        ProgData progData = ProgData.getInstance();
        FileDataList fileDataList1 = progData.fileDataList_1;
        FileDataList fileDataList2 = progData.fileDataList_2;
        stop = false;
        id = 0;

        // erst mal alle auf diff/only setzen
        ProgData.getInstance().pEventHandler.notifyListener(new P2Event(Events.GENERATE_COMPARE_FILE_LIST,
                "Dateien vergleichen", 1, 5));
        fileDataList1.forEach(fd -> {
            fd.setOnly(true);
            fd.setDiff(false);
            fd.setFilePathId(0);
            fd.setFileNameId(0);
            fd.setHashId(0);
        });
        fileDataList2.forEach(fd -> {
            fd.setOnly(true);
            fd.setDiff(false);
            fd.setFilePathId(0);
            fd.setFileNameId(0);
            fd.setHashId(0);
        });

        //jetzt die Dateien vergleichen
        switch (ProgConfig.CONFIG_COMPARE_FILE.getValue()) {
            case ProgConst.COMPARE_PATH_NAME:
                P2Log.sysLog("ProgConst.COMPARE_PATH_NAME");
                compareWithPathName(fileDataList1, fileDataList2);
                fileDataList1.forEach(fileData -> fileData.setId(fileData.getFilePathId()));
                fileDataList2.forEach(fileData -> fileData.setId(fileData.getFilePathId()));
                break;
            case ProgConst.COMPARE_NAME:
                P2Log.sysLog("ProgConst.COMPARE_NAME");
                compareWithName(fileDataList1, fileDataList2);
                fileDataList1.forEach(fileData -> fileData.setId(fileData.getFileNameId()));
                fileDataList2.forEach(fileData -> fileData.setId(fileData.getFileNameId()));
                break;

            case ProgConst.COMPARE_HASH:
                P2Log.sysLog("ProgConst.COMPARE_PATH_HASH");
                compareWithHash(fileDataList1, fileDataList2);
                fileDataList1.forEach(fileData -> fileData.setId(fileData.getHashId()));
                fileDataList2.forEach(fileData -> fileData.setId(fileData.getHashId()));
                break;
        }

        running.set(false);
        ProgData.getInstance().pEventHandler.notifyListener(new P2Event(Events.GENERATE_COMPARE_FILE_LIST,
                "Dateien vergleichen", 0, 0));
        progData.pEventHandler.notifyListener(new P2Event(Events.COMPARE_OF_FILE_LISTS_FINISHED));
    }

    private static synchronized int getNextId() {
        return ++id;
    }

    private static void compareWithPathName(FileDataList fileDataList1, FileDataList fileDataList2) {
        // Dateien mit Pfad/Name vergleichen und ID setzen
        P2Log.sysLog("Vergleichen von Dateien mit gleichem Verzeichnis/Namen:" + P2LibConst.LINE_SEPARATOR
                + fileDataList1.getSourceDir() + P2LibConst.LINE_SEPARATOR + fileDataList2.getSourceDir());
        ProgData.getInstance().pEventHandler.notifyListener(new P2Event(Events.GENERATE_COMPARE_FILE_LIST,
                "Dateien vergleichen", 0, fileDataList1.size()));

        HashMap<String, FileData> map2 = new HashMap<>();
        fileDataList2.forEach(f -> map2.put(f.getPathFileName(), f));

        fileDataList1.forEach(fd1 -> {
            if (stop) {
                return;
            }

            ++count;
            if (count % 1000 == 0) {
                ProgData.getInstance().pEventHandler.notifyListener(new P2Event(Events.GENERATE_COMPARE_FILE_LIST,
                        "Dateien vergleichen", count, fileDataList1.size()));
            }

            FileData fd2 = map2.get(fd1.getPathFileName());
            if (fd2 != null) {
                //dann gibts schon mal eine mit gleichem Pfad/Namen
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

    private static void compareWithName(FileDataList fileDataList1, FileDataList fileDataList2) {
        // Dateien mit dem gleich Namen vergleichen und ID setzen
        // gleich sind sie, wenn ALLE Dateien mit dem gleichen Namen auch gleich sind!!!
        P2Log.sysLog("Vergleichen von Dateien mit gleichem Namen:" + P2LibConst.LINE_SEPARATOR
                + fileDataList1.getSourceDir() + P2LibConst.LINE_SEPARATOR + fileDataList2.getSourceDir());
        ProgData.getInstance().pEventHandler.notifyListener(new P2Event(Events.GENERATE_COMPARE_FILE_LIST,
                "Dateien vergleichen", 0, fileDataList1.size()));

        MultiValuedMap<String, FileData> multiMap1 = new ArrayListValuedHashMap<>();
        fileDataList1.forEach(f -> multiMap1.put(f.getFileName(), f));

        MultiValuedMap<String, FileData> multiMap2 = new ArrayListValuedHashMap<>();
        fileDataList2.forEach(f -> multiMap2.put(f.getFileName(), f));

        fileDataList1.forEach(fd1 -> {
            if (stop) {
                return;
            }

            ++count;
            if (count % 1000 == 0) {
                ProgData.getInstance().pEventHandler.notifyListener(new P2Event(Events.GENERATE_COMPARE_FILE_LIST,
                        "Dateien vergleichen", count, fileDataList1.size()));
            }

            boolean found = false;
            Collection<FileData> coll1 = multiMap1.get(fd1.getFileName());
            Collection<FileData> coll2 = multiMap2.get(fd1.getFileName());

            if (!coll2.isEmpty()) {
                // nur wenn auch in coll2 was ist, sonst gibts keine
                for (FileData fd2 : coll2) {
                    fd2.setOnly(false);
                    if (!fd2.getHash().equals(fd1.getHash())) {
                        // und dann sind sie aber nicht gleich
                        found = true;
                    }
                }

                // dann auch noch im 1er schauen, muss auf jeden Fall um die doppelten zu erwischen
                for (FileData fd11 : coll1) {
                    fd11.setOnly(false);
                    if (!fd11.getHash().equals(fd1.getHash())) {
                        found = true;
                    }
                }
            }

            if (!found) {
                //dann sind alle gleich :)
                final int i = fd1.getFileNameId() > 0 ? fd1.getFileNameId() : getNextId();
                coll1.forEach(f -> f.setFileNameId(i));
                coll2.forEach(f -> f.setFileNameId(i));

            } else {
                //dann gibts unterschiedliche Hashes bei diesem
                coll1.forEach(f -> f.setDiff(true));
                coll2.forEach(f -> f.setDiff(true));
            }
        });
    }

    private static void compareWithHash(FileDataList fileDataList1, FileDataList fileDataList2) {
        // Dateien nur mit dem Hash vergleichen und ID setzen
        int max = 4;
        P2Log.sysLog("Vergleichen mit Hash:" + P2LibConst.LINE_SEPARATOR
                + fileDataList1.getSourceDir() + P2LibConst.LINE_SEPARATOR + fileDataList2.getSourceDir());
        ProgData.getInstance().pEventHandler.notifyListener(new P2Event(Events.GENERATE_COMPARE_FILE_LIST,
                "Dateien vergleichen", 0, max));

        MultiValuedMap<String, FileData> multiMap1 = new ArrayListValuedHashMap<>();
        fileDataList1.forEach(f -> multiMap1.put(f.getHash(), f));

        MultiValuedMap<String, FileData> multiMap2 = new ArrayListValuedHashMap<>();
        fileDataList2.forEach(f -> multiMap2.put(f.getHash(), f));


        // ======================================
        // die ID setzen
        P2Log.sysLog("Vergleichen mit Hash, ==== 2 ====:");
        fileDataList1.forEach(fd1 -> {
            ++count;
            if (count % 1000 == 0) {
                ProgData.getInstance().pEventHandler.notifyListener(new P2Event(Events.GENERATE_COMPARE_FILE_LIST,
                        "Dateien vergleichen", count, fileDataList1.size()));
            }

            Collection<FileData> col2 = multiMap2.get(fd1.getHash());
            if (!col2.isEmpty()) {
                // den 1er setzen
                fd1.setOnly(false);
                if (fd1.getHashId() == 0) {
                    fd1.setHashId(getNextId());
                }

                // alle 2er setzen
                col2.forEach(fd2 -> {
                    fd2.setOnly(false);
                    fd2.setHashId(fd1.getHashId());
                });

                // und alle gleichen 1er auch setzen
                Collection<FileData> col1 = multiMap1.get(fd1.getHash());
                col1.forEach(f -> {
                    f.setOnly(false);
                    f.setHashId(fd1.getHashId());
                });
            }
        });
    }
}
