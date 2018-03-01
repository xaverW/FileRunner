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
import de.p2tools.fileRunner.controller.data.fileData.FileData;
import de.p2tools.fileRunner.controller.data.fileData.FileDataList;
import de.p2tools.fileRunner.controller.worker.compare.CompareFileList;
import de.p2tools.p2Lib.tools.Log;

import javax.swing.event.EventListenerList;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.util.ArrayList;

public class ReadDirHashFile {


    private boolean stop = false;
    private EventListenerList listeners = new EventListenerList();

    private final ProgData progData;

    public ReadDirHashFile(ProgData progData) {
        this.progData = progData;
    }

    public void addAdListener(RunListener listener) {
        listeners.add(RunListener.class, listener);
    }

    public void setStop() {
        stop = true;
    }

    public void readFile(File fileHash, FileDataList fileDataList) {
        stop = false;
        fileDataList.clear();
        HashFileRead hashFileRead = new HashFileRead(fileHash, fileDataList);
        new Thread(hashFileRead).start();
    }

    private void notifyEvent(int max, int progress, String text) {
        RunEvent event;
        event = new RunEvent(this, progress, max, text);
        for (RunListener l : listeners.getListeners(RunListener.class)) {
            l.notify(event);
        }
    }

    private class HashFileRead implements Runnable {

        private File hashFile;
        private String search;
        private FileDataList fileDataList;

        public HashFileRead(File hashFile, FileDataList fileDataList) {
            this.hashFile = hashFile;
            this.search = search;
            this.fileDataList = fileDataList;
        }

        public synchronized void run() {
            //Liste aus Hashdatei laden
            notifyEvent(1, 0, "");
            laden();
            if (stop) {
                fileDataList.clear();
            } else {
                new CompareFileList().compareList(progData.fileDataList1, progData.fileDataList2);
            }
            notifyEvent(0, 0, "");
        }

        private void laden() {
            LineNumberReader in = null;
            ArrayList<FileData> tmp = new ArrayList<>();
            try {
                in = new LineNumberReader(new InputStreamReader(new FileInputStream(hashFile)));
                String zeile;
                while (!stop && (zeile = in.readLine()) != null) {
                    try {
                        final FileData fileData = HashTools.getFileData(zeile);
                        tmp.add(fileData);
                    } catch (Exception ex) {
                        Log.errorLog(704125890, ex, new String[]{"Kann die Zeile in der Hashdatei nicht verarbeiten:", zeile});
                    }
                }
                in.close();
            } catch (Exception ex) {
                Log.errorLog(954102023, ex);
                tmp.clear();
            } finally {
                try {
                    in.close();
                } catch (Exception ex) {
                }
            }
            fileDataList.addAll(tmp);
        }

    }
}
