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


package de.p2tools.fileRunner.controller.worker.GetHash;

import de.p2tools.fileRunner.controller.config.Events;
import de.p2tools.fileRunner.controller.config.ProgConfig;
import de.p2tools.fileRunner.controller.config.ProgData;
import de.p2tools.fileRunner.controller.data.fileData.FileDataList;
import de.p2tools.fileRunner.controller.worker.compare.CompareFileList;
import de.p2tools.p2Lib.hash.HashConst;
import de.p2tools.p2Lib.tools.date.PDate;
import de.p2tools.p2Lib.tools.events.RunEvent;
import de.p2tools.p2Lib.tools.file.PFileSize;
import de.p2tools.p2Lib.tools.log.PLog;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.util.LinkedList;

public class DirHash_CreateDirHash {

    private ProgData progData;
    private boolean stop = false;
    private int max = 0; //anzahl dateien
    private int progress = 0;
    private int threads = 0;
    private int anzThread = 1;
    private boolean recursiv = true;
    private boolean followLink = false;
    private int runThreads = 0;

    public DirHash_CreateDirHash(ProgData progData) {
        this.progData = progData;
    }

    public void setStop() {
        stop = true;
    }

    public void createHash(File file, FileDataList fileDataList, int anzThread, boolean followLink) {
        this.anzThread = anzThread;
        this.recursiv = ProgConfig.CONFIG_COMPARE_WITH_PATH.getValue();
        this.followLink = followLink;

        max = 0;
        progress = 0;
        stop = false;

        fileDataList.clear();
        CreateHash createHash = new CreateHash(file, fileDataList);
        runThreads = 1;
        Thread startenThread = new Thread(createHash);
        startenThread.setName("CreateHash");
        startenThread.setDaemon(true);
        startenThread.start();
    }

    private void notifyEvent() {
        progData.pEventHandler.notifyListener(new RunEvent(Events.GENERATE_COMPARE_FILE_LIST,
                progress, max, ""));
    }

    private class CreateHash implements Runnable {

        private File searchDir;
        private FileDataList fileDataList;
        private LinkedList<File> listeFile = new LinkedList<>();

        public CreateHash(File searchDir, FileDataList fileDataList) {
            this.searchDir = searchDir;
            this.fileDataList = fileDataList;
        }

        public synchronized void run() {
            // Dateien auflisten
            runDirFindFiles();
            notifyEvent();

            // Hash berechnen
            anzThread = 1; //todo
            for (int i = 0; i < anzThread; ++i) {
                ++threads;
                new Thread(new Hash()).start();
            }

            // warten bis alle Threads fertig sind
            while (threads > 0) {
                try {
                    this.wait(1000);
                } catch (Exception e) {
                }
            }


            if (stop) {
                fileDataList.clear();
            } else {
                new CompareFileList().compareList();
            }
            --runThreads;
            if (runThreads == 0) {
                max = 0;
                progress = 0;
                notifyEvent();
            }
        }

        private void runDirFindFiles() {
            //Verzeichnis ablaufen und Dateien suchen
            try {
                new DirHash_RunRecDir() {
                    @Override
                    void work(File file) {
                        if (stop) {
                            this.setStop();
                        }
                        addGetFile(file);
                    }

                }.recDir(searchDir, recursiv);
            } catch (Exception ex) {
                PLog.errorLog(975102364, ex, "CreateHash.run - " + searchDir.getAbsolutePath());
            }
        }

        private class Hash implements Runnable {

            private InputStream srcStream = null;
            private byte[] buffer = new byte[1024];

            public synchronized void run() {
                File file;
                while (!stop && (file = addGetFile(null)) != null) {
                    hash(file, searchDir);
                    ++progress;
                    notifyEvent();
                }
                --threads;
            }

            private String hash(File file, File searchDir) {
                String ret = "";
                try {
                    MessageDigest messageDigest = MessageDigest.getInstance(HashConst.HASH_MD5);
                    srcStream = new DigestInputStream(new FileInputStream(file), messageDigest);

                    while (!stop && srcStream.read(buffer) > -1) {
                    }

                    ret = DirHashFileFactory.getHashString(messageDigest.digest());

                    if (searchDir != null) {
                        String strFile = file.getAbsolutePath();

                        boolean link = Files.isSymbolicLink(file.toPath());
                        if (link && followLink) {
                            strFile = file.getCanonicalPath(); // ist der Pfad des verlinkten Files
                        } else {
                            strFile = strFile.substring(searchDir.getAbsolutePath().length());
                            if (strFile.startsWith(File.separator)) {
                                strFile = strFile.substring(1);
                            }
                        }

                        PDate fileDate = new PDate(file.lastModified());
                        PFileSize fileSize = new PFileSize(file.length());
                        fileDataList.addHashString(strFile, fileDate, fileSize, ret, link);
                    }
                } catch (Exception ex) {
                    PLog.errorLog(963210472, ex, "Fehler! " + file.getAbsolutePath());
                } finally {
                    try {
                        srcStream.close();
                    } catch (IOException ex) {
                    }
                }
                return ret;
            }

        }

        private synchronized File addGetFile(File file) {
            File ret = null;
            if (file != null) {
                listeFile.add(file);
                ++max;
            } else {
                ret = listeFile.poll();
            }
            return ret;
        }
    }
}