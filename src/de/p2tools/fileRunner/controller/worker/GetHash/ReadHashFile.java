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


package de.p2tools.fileRunner.controller.worker.GetHash;

import de.p2tools.fileRunner.controller.RunEvent;
import de.p2tools.fileRunner.controller.RunListener;
import de.p2tools.fileRunner.controller.data.fileData.FileData;
import de.p2tools.fileRunner.controller.data.fileData.FileDataList;
import de.p2tools.p2Lib.tools.Log;

import javax.swing.event.EventListenerList;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.util.ArrayList;

public class ReadHashFile {


    private boolean stop = false;
    private EventListenerList listeners = new EventListenerList();

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
        private FileDataList fileDataList;

        public HashFileRead(File hashFile, FileDataList fileDataList) {
            this.hashFile = hashFile;
            this.fileDataList = fileDataList;
        }

        public synchronized void run() {
            //Liste aus Hashdatei laden
            notifyEvent(1, 0, "");
            laden();
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
                        tmp.add(HashTools.getFileData(zeile));
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
