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
import de.p2tools.p2Lib.tools.log.PLog;
import javafx.application.Platform;
import javafx.beans.property.StringProperty;

import javax.swing.event.EventListenerList;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.stream.Stream;

public class ReadHashFile {

    private boolean stop = false;
    private EventListenerList listeners = new EventListenerList();

    public void addAdListener(RunListener listener) {
        listeners.add(RunListener.class, listener);
    }

    public void setStop() {
        stop = true;
    }

    private void notifyEvent(int max, int progress, String text) {
        RunEvent event;
        event = new RunEvent(this, progress, max, "");
        for (RunListener l : listeners.getListeners(RunListener.class)) {
            l.notify(event);
        }
    }

    public void readHash(String file, StringProperty stringProperty) {
        stop = false;

        ReadHash readHash = new ReadHash(file, stringProperty);
        Thread genHashThread = new Thread(readHash);
        genHashThread.setName("ReadHashFile");
        genHashThread.setDaemon(true);
        genHashThread.start();
    }

    private class ReadHash implements Runnable {
        private final String fileStr;
        private final StringProperty stringProperty;
        private String fileHash = "";
        private ArrayList<String[]> hashList = new ArrayList<>();

        public ReadHash(String fileStr, StringProperty stringProperty) {
            this.fileStr = fileStr;
            this.stringProperty = stringProperty;
        }

        public synchronized void run() {
            notifyEvent(1, 0, "");

            try (Stream<String> lines = Files.lines(Paths.get(fileStr), Charset.defaultCharset())) {
                lines.forEachOrdered(line -> {
                    if (!line.isEmpty()) {
                        hashList.add(getHashFromLine(line));
                    }
                });
            } catch (IOException ex) {
                PLog.errorLog(620301973, ex, "load hashfile: " + fileStr);
            }

            if (!hashList.isEmpty()) {
                // todo dialog if more then one line
                String[] str = hashList.get(0);
                fileHash = str[0];
                Platform.runLater(() -> stringProperty.setValue(fileHash));
            }

            notifyEvent(0, 0, "");
        }
    }

    private String[] getHashFromLine(String line) {
        // F40736FE37D60E1CFE29E0C4034C353C3E45D547 *Film 3.mp4.sha1
        String[] ret = {"", ""};
        String h = "", f = "";

        int l = line.indexOf(" ");
        if (l > 0) {
            h = line.substring(0, l).trim();
            f = line.substring(l).trim();
            if (f.startsWith("*")) {
                f = f.replaceFirst("\\*", "");
            }
            ret[0] = h;
            ret[1] = f;
        }
        return ret;
    }

}

