/*
 * MTPlayer Copyright (C) 2018 W. Xaver W.Xaver[at]googlemail.com
 * http://zdfmediathk.sourceforge.net/
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


package de.p2tools.fileRunner.controller.worker;

import de.p2tools.fileRunner.controller.RunEvent;
import de.p2tools.fileRunner.controller.RunListener;
import de.p2tools.fileRunner.controller.config.ProgData;
import de.p2tools.fileRunner.controller.data.fileData.FileData;
import de.p2tools.fileRunner.controller.data.fileData.FileDataList;
import de.p2tools.p2Lib.tools.Log;

import javax.swing.event.EventListenerList;
import java.io.*;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.util.Iterator;
import java.util.LinkedList;

public class GetHash {


    private ProgData progData;
    private boolean stop = false;
    private final static String MD5 = new String("MD5");
    private EventListenerList listeners = new EventListenerList();
    private int max = 0; //anzahl dateien
    private int progress = 0;
    private int threads = 0;
    private int anzThread = 1;
    private boolean rekursiv = true;
    private int laufer = 0;

    public GetHash(ProgData progData) {
        this.progData = progData;
    }
    ///////////////////////
    // public
    ////////////////////////

    /**
     * @param listener
     */
    public void addAdListener(RunListener listener) {
        listeners.add(RunListener.class, listener);
    }

    public void setStop() {
        stop = true;
    }

    public void hashLesen(File dir1, FileDataList listeDiff1,
                          File dir2, File hash2, FileDataList listeDiff2,
                          int aanzThread, boolean rrekursiv) {
        anzThread = aanzThread;
        rekursiv = rrekursiv;
        max = 0;
        progress = 0;
        stop = false;
        HashErstellen hashErstellen1, hashErstellen2;
        hashErstellen1 = new HashErstellen(dir1, null, listeDiff1);
        hashErstellen2 = new HashErstellen(dir2, hash2, listeDiff2);
        laufer = 2;
        new Thread(hashErstellen1).start();
        new Thread(hashErstellen2).start();
    }

    public void hashLesen(File dir1, FileDataList fileDataList,
                          int anzThread, boolean rekursiv) {
        this.anzThread = anzThread;
        this.rekursiv = rekursiv;
        max = 0;
        progress = 0;
        stop = false;
        HashErstellen hashErstellen1 = new HashErstellen(dir1, null, fileDataList);
        laufer = 1;
        new Thread(hashErstellen1).start();
    }

    public void hashSchreiben(FileDataList fileDataList, File hash) {
        max = 0;
        progress = 0;
        stop = false;
        HashErstellen hashErstellen;
        hashErstellen = new HashErstellen(null, hash, fileDataList);
        laufer = 1;
        hashErstellen.schreiben();
    }

    //////////////////////////
    // private
    //////////////////////////
    private class HashErstellen implements Runnable {

        private File dir = null, hash = null;
        private FileDataList fileDataList = null;
        private LinkedList<File> listeFile = new LinkedList<File>();

        public HashErstellen(File ddir, File hhash, FileDataList fileDataList) {
            dir = ddir;
            hash = hhash;
            this.fileDataList = fileDataList;

        }

        public synchronized void run() {
            listeFile.clear();
            if (dir != null) {
                //Verzeichnis absuchen
                try {
                    new RekDir(progData) {

                        @Override
                        void tuwas(File file) {
                            if (stop) {
                                this.setStop();
                            }
                            addGetFile(file);
                        }

                    }.rekDir(dir, rekursiv);
                } catch (Exception ex) {
                    Log.errorLog(975102364, ex, "HashErstellen.run - " + dir.getAbsolutePath());
                }
                notifyEvent();
                if (anzThread < 1) {
                    anzThread = 1;
                }
                for (int i = 0; i < anzThread; ++i) {
                    ++threads;
                    new Thread(new Hash()).start();
                }
                while (threads > 0) {
                    try {
                        this.wait(1000);
                    } catch (Exception e) {
                    }
                }
            } else if (dir == null) {
                //Liste aus Hashdatei laden
                laden();
            }
            if (stop) {
                fileDataList.clear();
            } else {
//                JOptionPane.showMessageDialog(null, "Ok (" + progress + " Dateien)", "Info", JOptionPane.INFORMATION_MESSAGE);
            }
            --laufer;
            if (laufer == 0) {
                max = 0;
                progress = 0;
                notifyEvent();
            }
        }

        public void schreiben() {
            OutputStreamWriter out = null;
            try {
                if (hash.exists()) {
                    hash.delete();
                }
                hash.createNewFile();
                out = new OutputStreamWriter(new FileOutputStream(hash, true));
                Iterator<FileData> it = fileDataList.iterator();
                FileData fileData;
                while (it.hasNext()) {
                    fileData = it.next();
                    out.write(fileData.getHash() + " " +
                            "*" + fileData.getFileName() + "\n");
                }
                out.flush();
            } catch (Exception ex) {
                Log.errorLog(986532014, ex, "Fehler beim Schreiben der Hashdatei!");
            } finally {
                try {
                    out.close();
                } catch (IOException ex) {
                    Log.errorLog(203064547, ex, "Fehler beim Schießen der Hashdatei!");
                }
            }
        }

        private void laden() {
//            int zeilen = 0;
            LineNumberReader in = null;
            try {
                in = new LineNumberReader(new InputStreamReader(new FileInputStream(hash)));
                String zeile;
                while (!stop && (zeile = in.readLine()) != null) {
                    try {
                        //---------
//                        ++zeilen;
                        System.out.println(zeile);
                        hash(changeZeile(zeile));
                    } catch (Exception ex) {
                        Log.errorLog(704125890, ex, new String[]{"Kann die Zeile in der Hashdatei nicht verarbeiten:", zeile});
                    }
                }
                in.close();
            } catch (Exception ex) {
            } finally {
                try {
                    in.close();
                } catch (Exception ex) {
                }
            }
        }

        private void hash(String zeile) {
            addHashString(Helper.getFile(zeile), Helper.getHash(zeile));
        }

        private String changeZeile(String zeile) {
            char search;
            char c = File.separatorChar;
            if (c == '\\') {
                search = '/';
            } else {
                search = '\\';
            }
            zeile = zeile.replace(search, File.separatorChar);
            return zeile;
        }

        private class Hash implements Runnable {

            private InputStream srcStream = null;
            private StringBuffer h;
            private byte[] hash = null;
            private MessageDigest md;
            private byte[] buffer = new byte[1024];

            public synchronized void run() {
                File file;
                while (!stop && (file = addGetFile(null)) != null) {
                    hash(file, dir);
                    ++progress;
                    notifyEvent();
                }
                --threads;
            }

            private String hash(File fil, File di) {
                String ret = "";
                try {
                    md = MessageDigest.getInstance(MD5);
                    srcStream = new DigestInputStream(new FileInputStream(fil), md);
                    while (!stop && srcStream.read(buffer) != -1) {
                    }
                    hash = md.digest();
                    h = new StringBuffer();
                    for (int k = 0; k < hash.length; ++k) {
                        h.append(Helper.toHexString(hash[k]));
                    }
                    ret = h.toString();
                    if (di != null) {
                        //dir == null -> nur hash prüfen
                        String strFile = fil.getAbsolutePath();
                        if (di.isDirectory()) {
                            strFile = strFile.substring(di.getAbsolutePath().length());
                        } else if (di.isFile()) {
                            strFile = strFile.substring(di.getParent().length());
                        }
                        if (strFile.startsWith(File.separator)) {
                            strFile = strFile.substring(1);
                        }
                        addHashString(strFile, h.toString());
                    }
                } catch (Exception ex) {
                    Log.errorLog(963210472, ex, "Fehler! " + fil.getAbsolutePath());
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
                System.out.println(file.getAbsolutePath());
                ++max;
            } else {
                ret = listeFile.poll();
            }
            return ret;
        }

        private synchronized void addHashString(String file, String hash) {
            fileDataList.add(new FileData(file, hash));
        }

    }

    private void notifyEvent(int max, int progress, String text) {
        RunEvent event;
        event = new RunEvent(this, progress, max, text);
        for (RunListener l : listeners.getListeners(RunListener.class)) {
            l.notify(event);
        }
    }

    private void notifyEvent() {
        RunEvent event;
        event = new RunEvent(this, progress, max, "");
        for (RunListener l : listeners.getListeners(RunListener.class)) {
            l.notify(event);
        }

    }

}
