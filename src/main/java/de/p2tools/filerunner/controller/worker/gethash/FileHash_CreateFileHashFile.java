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


package de.p2tools.filerunner.controller.worker.gethash;

import de.p2tools.filerunner.controller.config.Events;
import de.p2tools.filerunner.controller.config.ProgConfig;
import de.p2tools.filerunner.controller.config.ProgData;
import de.p2tools.filerunner.controller.config.RunPEvent;
import de.p2tools.p2lib.tools.log.PLog;
import de.p2tools.p2lib.tools.net.PUrlTools;
import javafx.application.Platform;
import javafx.beans.property.StringProperty;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.DigestInputStream;
import java.security.MessageDigest;

public class FileHash_CreateFileHashFile {

    private boolean stop = false;
    private int max = 0;
    private int progress = 0;
    private int threads = 0;

    public void setStop() {
        stop = true;
    }

    private void notifyEvent() {
        ProgData.getInstance().pEventHandler.notifyListener(new RunPEvent(Events.GENERATE_COMPARE_FILE_LIST,
                progress, max, ""));
    }

    public void genHash(String file, StringProperty stringProperty) {
        stop = false;

        max += 100;
        threads += 1;
        notifyEvent();

        CreateHash createHash = new CreateHash(file, stringProperty);
        Thread genHashThread = new Thread(createHash);
        genHashThread.setName("CreateHash");
        genHashThread.setDaemon(true);
        genHashThread.start();
    }

    private class CreateHash implements Runnable {
        private final String fileStr;
        private final StringProperty stringProperty;
        private String fileHash = "";

        public CreateHash(String fileStr, StringProperty stringProperty) {
            this.fileStr = fileStr;
            this.stringProperty = stringProperty;
        }

        public synchronized void run() {
            InputStream srcStream = null;

            try {
                byte[] hash;
                final int BUFFER_MAX = 1024;
                byte[] buffer = new byte[BUFFER_MAX];

                long countBuffer;
                long msgBuff;
                long run = 0;
                MessageDigest md;

                if (PUrlTools.isUrl(fileStr)) {
                    // eine URL verarbeiten
                    int timeout = PUrlTools.TIME_OUT; //ms
                    HttpURLConnection conn;
                    conn = (HttpURLConnection) new URL(fileStr).openConnection();
                    conn.setReadTimeout(timeout);
                    conn.setConnectTimeout(timeout);

                    countBuffer = conn.getContentLengthLong() / BUFFER_MAX;
                    msgBuff = countBuffer / 100;

                    md = MessageDigest.getInstance(ProgConfig.GUI_FILE_HASH.get());
                    srcStream = new DigestInputStream(conn.getInputStream(), md);

                } else {
                    File f = new File(fileStr);
                    countBuffer = f.length() / BUFFER_MAX;
                    msgBuff = countBuffer / 100;

                    md = MessageDigest.getInstance(ProgConfig.GUI_FILE_HASH.get());
                    srcStream = new DigestInputStream(new FileInputStream(fileStr), md);
                }


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
                PLog.errorLog(764512032, ex, "Hash konnte nicht erstellt werden.");
                fileHash = "";
            } finally {
                try {
                    if (srcStream != null) {
                        srcStream.close();
                    }
                } catch (IOException ex) {
                }
            }

            Platform.runLater(() -> stringProperty.setValue(fileHash));

            --threads;
            if (threads <= 0) {
                max = 0;
                progress = 0;
                notifyEvent();
            }
        }
    }

    private String toHexString(byte b) {
        int value = (b & 0x7F) + (b < 0 ? 128 : 0);
        String ret = (value < 16 ? "0" : "");
        ret += Integer.toHexString(value).toUpperCase();
        return ret;
    }

}

