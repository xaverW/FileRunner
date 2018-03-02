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

import de.p2tools.fileRunner.controller.RunEvent;
import de.p2tools.fileRunner.controller.RunListener;
import de.p2tools.fileRunner.controller.config.ProgData;
import de.p2tools.fileRunner.controller.data.fileData.FileDataList;
import de.p2tools.fileRunner.controller.worker.compare.CompareFileList;
import de.p2tools.p2Lib.hash.HashConst;
import de.p2tools.p2Lib.tools.FileSize;
import de.p2tools.p2Lib.tools.Log;
import de.p2tools.p2Lib.tools.PDate;

import javax.swing.event.EventListenerList;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.util.LinkedList;

public class CreateDirHash {


    private ProgData progData;
    private boolean stop = false;
    private EventListenerList listeners = new EventListenerList();
    private int max = 0; //anzahl dateien
    private int progress = 0;
    private int threads = 0;
    private int anzThread = 1;
    private boolean rekursiv = true;
    private boolean followLink = false;
    private int runThreads = 0;

    public CreateDirHash(ProgData progData) {
        this.progData = progData;
    }

    public void addAdListener(RunListener listener) {
        listeners.add(RunListener.class, listener);
    }

    public void setStop() {
        stop = true;
    }

    public void createHash(File file, FileDataList fileDataList, int anzThread, boolean rekursiv, boolean followLink) {
        this.anzThread = anzThread;
        this.rekursiv = rekursiv;
        this.followLink = followLink;

        max = 0;
        progress = 0;
        stop = false;

        fileDataList.removeAll(fileDataList);
        CreateHash createHash = new CreateHash(file, fileDataList);
        runThreads = 1;
        Thread startenThread = new Thread(createHash);
        startenThread.setName("HashErstellen");
        startenThread.setDaemon(true);
        startenThread.start();
    }

    private void notifyEvent() {
        RunEvent event;
        event = new RunEvent(this, progress, max, "");
        for (RunListener l : listeners.getListeners(RunListener.class)) {
            l.notify(event);
        }

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
                new CompareFileList().compareList(progData.fileDataList1, progData.fileDataList2);
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
                new RunRekDir() {

                    @Override
                    void work(File file) {
                        if (stop) {
                            this.setStop();
                        }
                        addGetFile(file);
                    }

                }.rekDir(searchDir, rekursiv);
            } catch (Exception ex) {
                Log.errorLog(975102364, ex, "CreateHash.run - " + searchDir.getAbsolutePath());
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

                    ret = HashTools.getHashString(messageDigest.digest());

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
                        FileSize fileSize = new FileSize(file.length());
                        fileDataList.addHashString(strFile, fileDate, fileSize, ret, link);
                    }
                } catch (Exception ex) {
                    Log.errorLog(963210472, ex, "Fehler! " + file.getAbsolutePath());
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
                //Log.sysLog(file.getAbsolutePath());
                ++max;
            } else {
                ret = listeFile.poll();
            }
            return ret;
        }


    }
}
