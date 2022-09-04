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
import de.p2tools.fileRunner.controller.config.ProgData;
import de.p2tools.fileRunner.controller.data.fileData.FileDataList;
import de.p2tools.fileRunner.controller.worker.compare.CompareFileListFactory;
import de.p2tools.p2Lib.hash.HashConst;
import de.p2tools.p2Lib.tools.date.PDate;
import de.p2tools.p2Lib.tools.duration.PDuration;
import de.p2tools.p2Lib.tools.events.RunPEvent;
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

    private static int i = 0;
    private int ii = 0;

    private ProgData progData;
    private boolean stop = false;
    private int max = 0; //Anzahl Dateien
    private int progress = 0;
    private int threads = 0;

    public DirHash_CreateDirHash(ProgData progData) {
        this.progData = progData;
    }

    public void setStop() {
        stop = true;
    }

    public boolean isRunning() {
        return max != 0;
    }

    public void createHash(String src, FileDataList fileDataList, boolean recursive, File file, int anzThread, boolean followLink) {
        ii = i++;
        System.out.println("Start createHash: " + ii);

        max = 1;
        progress = 0;
        stop = false;

        fileDataList.clear();
        fileDataList.setSourceDir(src);

        CreateHash createHash = new CreateHash(fileDataList, file, anzThread, recursive, followLink);
        Thread startenThread = new Thread(createHash);
        startenThread.setName("CreateHash");
        startenThread.setDaemon(true);
        startenThread.start();
    }

    private void notifyEvent(String text) {
        progData.pEventHandler.notifyListener(new RunPEvent(Events.GENERATE_COMPARE_FILE_LIST,
                progress, max, text));
    }

    private class CreateHash implements Runnable {

        private FileDataList fileDataList;
        private File searchDir;
        private LinkedList<File> listeFile = new LinkedList<>();
        private int anzThread;
        private boolean recursive;
        private boolean followLink;

        public CreateHash(FileDataList fileDataList, File searchDir, int anzThread,
                          boolean recursive, boolean followLink) {
            this.fileDataList = fileDataList;
            this.searchDir = searchDir;
            this.anzThread = anzThread;
            this.recursive = recursive;
            this.followLink = followLink;
        }

        public synchronized void run() {
            // Dateien auflisten
            try {
                runDirFindFiles();
                notifyEvent(searchDir.toString());

                // Hash berechnen
                anzThread = 1; //todo
                for (int i = 0; i < anzThread; ++i) {
                    ++threads;
                    new Thread(new Hash(followLink)).start();
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
                }
            } catch (Exception ex) {
                System.out.println(ex.getStackTrace());
            }
            max = 0;
            progress = 0;
            System.out.println("Stop createHash: " + ii);
            CompareFileListFactory.addRunner(-1);
            CompareFileListFactory.compareList();
            notifyEvent(searchDir.toString());

            PDuration.counterStop("runThread");
            PDuration.getCounter();
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

                }.recDir(searchDir, recursive);
            } catch (Exception ex) {
                PLog.errorLog(975102364, ex, "CreateHash.run - " + searchDir.getAbsolutePath());
            }
        }

        private class Hash implements Runnable {

            private InputStream srcStream = null;
            private byte[] buffer = new byte[1024];
            private boolean followLink;

            public Hash(boolean followLink) {
                this.followLink = followLink;
            }

            public synchronized void run() {
                File file;
                while (!stop && (file = addGetFile(null)) != null) {
                    hash(file, searchDir, followLink);
                    ++progress;
                    notifyEvent(searchDir.toString());
                }
                --threads;
            }

            private String hash(File file, File searchDir, boolean followLink) {
                String hashString = "";
                try {
                    MessageDigest messageDigest = MessageDigest.getInstance(HashConst.HASH_MD5);
                    srcStream = new DigestInputStream(new FileInputStream(file), messageDigest);

                    while (!stop && srcStream.read(buffer) > -1) {
                    }
                    hashString = DirHashFileFactory.getHashString(messageDigest.digest());

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
                        fileDataList.addHashString(strFile, fileDate, fileSize, hashString, link);
                    }
                } catch (Exception ex) {
                    PLog.errorLog(963210472, ex, "Fehler! " + file.getAbsolutePath());
                } finally {
                    try {
                        srcStream.close();
                    } catch (IOException ex) {
                    }
                }
                return hashString;
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
