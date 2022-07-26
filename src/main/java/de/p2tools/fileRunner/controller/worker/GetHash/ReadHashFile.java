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

import de.p2tools.fileRunner.controller.config.ProgData;
import de.p2tools.fileRunner.controller.listener.Events;
import de.p2tools.fileRunner.gui.dialog.SelectHashDialogController;
import de.p2tools.p2Lib.tools.events.RunEvent;
import de.p2tools.p2Lib.tools.log.PLog;
import javafx.application.Platform;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;

public class ReadHashFile {

    private boolean stop = false;

    public void setStop() {
        stop = true;
    }

    private void notifyEvent(int max, int progress, String text) {
        ProgData.getInstance().pEventHandler.notifyListenerGui(new RunEvent(Events.GENERATE_COMPARE_FILE_LIST,
                progress, max, ""));
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
        private ObservableList<HashFileEntry> hashList = FXCollections.observableArrayList();

        public ReadHash(String fileStr, StringProperty stringProperty) {
            this.fileStr = fileStr;
            this.stringProperty = stringProperty;
        }

        public synchronized void run() {
            notifyEvent(1, 0, "");

            try (Stream<String> lines = Files.lines(Paths.get(fileStr), Charset.defaultCharset())) {
                lines.forEachOrdered(line -> {

                    final HashFileEntry entry = getHashFromLine(line);
                    if (entry != null) {
                        hashList.add(entry);
                    }

                });
            } catch (IOException ex) {
                PLog.errorLog(620301973, ex, "load hashfile: " + fileStr);
            }

            if (hashList.size() == 1) {
                //dann gibts eh nur einen
                HashFileEntry entry = hashList.get(0);
                stringProperty.setValue(entry.getHash());
            } else if (hashList.size() > 1) {
                //dann den Hash auswählen
                Platform.runLater(() -> {
                    SelectHashDialogController hashDialog = new SelectHashDialogController(ProgData.getInstance(), hashList);
                    HashFileEntry entry = hashDialog.getHashFileEntry();
                    if (entry != null) {
                        stringProperty.setValue(entry.getHash());
                    }
                });
            }

            notifyEvent(0, 0, "");
        }
    }

    private HashFileEntry getHashFromLine(String line) {
        if (line.isEmpty()) {
            return null;
        }

        // F40736FE37D60E1CFE29E0C4034C353C3E45D547 *Film 3.mp4.sha1
        String h = "", f = "";

        int l = line.indexOf(" ");
        if (l > 0) {
            h = line.substring(0, l).trim();
            f = line.substring(l).trim();
            if (f.startsWith("*")) {
                f = f.replaceFirst("\\*", "");
            }
            return new HashFileEntry(h, f);
        }

        //93d01e9d3b24befdf8ef2c6a04e99e00640a5799d502ad5275c
        l = line.indexOf(" ");
        if (l < 0) {
            h = line.trim();
            return new HashFileEntry(h, f);
        }

        return null;
    }
}

