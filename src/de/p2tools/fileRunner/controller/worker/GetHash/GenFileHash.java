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
import de.p2tools.fileRunner.controller.config.ProgConfig;
import de.p2tools.p2Lib.tools.Log;
import javafx.application.Platform;
import javafx.beans.property.StringProperty;

import javax.swing.event.EventListenerList;
import java.io.*;
import java.security.DigestInputStream;
import java.security.MessageDigest;

public class GenFileHash {
    private boolean stop = false;
    private EventListenerList listeners = new EventListenerList();
    private int max = 0;
    private int progress = 0;
    private String fileHash = "";

    public void addAdListener(RunListener listener) {
        listeners.add(RunListener.class, listener);
    }

    public void setStop() {
        stop = true;
    }

    private void notifyEvent() {
        RunEvent event;
        event = new RunEvent(this, progress, max, "");
        for (RunListener l : listeners.getListeners(RunListener.class)) {
            l.notify(event);
        }
    }

    public String getFileHash() {
        return fileHash;
    }

    public void genHash(File file, StringProperty stringProperty) {
        stop = false;
        fileHash = "";

        HashErstellen hashErstellen = new HashErstellen(file, stringProperty);
        Thread startenThread = new Thread(hashErstellen);
        startenThread.setName("HashErstellen");
        startenThread.setDaemon(true);
        startenThread.start();
    }

    private class HashErstellen implements Runnable {
        private final File file;
        private final StringProperty stringProperty;

        public HashErstellen(File file, StringProperty stringProperty) {
            this.file = file;
            this.stringProperty = stringProperty;
        }

        public synchronized void run() {
            InputStream srcStream = null;

            try {
                byte[] hash;
                final int BUFFER_MAX = 1024;
                byte[] buffer = new byte[BUFFER_MAX];

                long countBuffer = file.length() / BUFFER_MAX;
                long msgBuff = countBuffer / 100;
                long run = 0;
                max = 100;
                progress = 0;
                notifyEvent();

                MessageDigest md = MessageDigest.getInstance(ProgConfig.GUI_FILE_HASH.get());
                srcStream = new DigestInputStream(new FileInputStream(file), md);

                while (!stop && srcStream.read(buffer) != -1) {
                    ++run;
                    if (run > msgBuff) {
                        run = 0;
                        ++progress;
                        notifyEvent();
                    }
                }

                if (!stop) {
                    hash = md.digest();
                    StringBuffer h = new StringBuffer();
                    for (int i = 0; i < hash.length; ++i) {
                        h.append(toHexString(hash[i]));
                    }
                    fileHash = h.toString();
                }
            } catch (Exception ex) {
                Log.errorLog(764512032, ex, "Hash konnte nicht erstellt werden.");
                fileHash = "";
            } finally {
                try {
                    if (srcStream != null) {
                        srcStream.close();
                    }
                } catch (IOException ex) {
                }
            }

            max = 0;
            progress = 0;
            notifyEvent();
            Platform.runLater(() -> stringProperty.setValue(fileHash));
        }
    }

    private String toHexString(byte b) {
        int value = (b & 0x7F) + (b < 0 ? 128 : 0);
        String ret = (value < 16 ? "0" : "");
        ret += Integer.toHexString(value).toUpperCase();
        return ret;
    }

    public void save(File file, String fileHash, String hash) {
        OutputStreamWriter out = null;
        try {
            if (file.exists()) {
                file.delete();
            }
            file.createNewFile();
            out = new OutputStreamWriter(new FileOutputStream(hash, true));
            out.write(hash + " " + "*" + fileHash + "\n");
            out.flush();
        } catch (Exception ex) {
            Log.errorLog(620120124, ex, "Fehler beim Schreiben der Hashdatei!");
        } finally {
            try {
                out.close();
            } catch (IOException ex) {
                Log.errorLog(987410235, ex, "Fehler beim Schießen der Hashdatei!");
            }
        }
    }
}
