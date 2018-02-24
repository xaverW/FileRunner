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
import javafx.beans.property.StringProperty;

import javax.swing.event.EventListenerList;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.DigestInputStream;
import java.security.MessageDigest;

public class GetHash {
    private boolean stop = false;
    private EventListenerList listeners = new EventListenerList();
    private int max = 0;
    private int progress = 0;

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

    public void getHash(File file, StringProperty stringProperty) {
        HashErstellen hashErstellen = new HashErstellen(file, stringProperty);
        new Thread(hashErstellen).start();
    }

    private class HashErstellen implements Runnable {
        private final File file;
        private final StringProperty stringProperty;

        public HashErstellen(File file, StringProperty stringProperty) {
            this.file = file;
            this.stringProperty = stringProperty;
        }

        public synchronized void run() {
            String ret = "";
            InputStream srcStream = null;

            try {
                byte[] hash;
                MessageDigest md;
                md = MessageDigest.getInstance(ProgConfig.GUI_FILE_HASH.get());
                final int BUFFER_MAX = 4096;
                byte[] buffer = new byte[BUFFER_MAX];
                long countBuffer = file.length() / BUFFER_MAX;
                max = (int) (countBuffer);
                progress = 0;
                notifyEvent();

                srcStream = new DigestInputStream(new FileInputStream(file), md);
                while (!stop && srcStream.read(buffer) != -1) {
                    ++progress;
                    notifyEvent();
                }
                if (!stop) {
                    hash = md.digest();
                    StringBuffer h = new StringBuffer();
                    for (int i = 0; i < hash.length; ++i) {
                        h.append(toHexString(hash[i]));
                    }
                    ret = h.toString();
                }
            } catch (Exception ex) {
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
            stringProperty.set(ret);
        }
    }

    private String toHexString(byte b) {
        int value = (b & 0x7F) + (b < 0 ? 128 : 0);
        String ret = (value < 16 ? "0" : "");
        ret += Integer.toHexString(value).toUpperCase();
        return ret;
    }

}

