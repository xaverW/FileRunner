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
import de.p2tools.fileRunner.controller.data.fileData.FileData;
import de.p2tools.fileRunner.controller.data.fileData.FileDataList;
import de.p2tools.fileRunner.controller.worker.compare.CompareFileListFactory;
import de.p2tools.p2Lib.P2LibConst;
import de.p2tools.p2Lib.tools.events.RunPEvent;
import de.p2tools.p2Lib.tools.log.PLog;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.util.ArrayList;

public class DirHash_ReadHashFile {


    private boolean stop = false;
    private final ProgData progData;

    public DirHash_ReadHashFile(ProgData progData) {
        this.progData = progData;
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
        progData.pEventHandler.notifyListener(new RunPEvent(Events.GENERATE_COMPARE_FILE_LIST,
                progress, max, text));
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
            notifyEvent(1, 0, hashFile.getAbsolutePath());
            laden();
            if (stop) {
                fileDataList.clear();
            } else {
                CompareFileListFactory.compareList();
            }
            notifyEvent(0, 0, hashFile.getAbsolutePath());
        }

        private void laden() {
            FileData fileData;
            String line;
            String firstLine = "";
            LineNumberReader in = null;
            ArrayList<FileData> tmp = new ArrayList<>();

            try {
                in = new LineNumberReader(new InputStreamReader(new FileInputStream(hashFile)));
                while (!stop && (line = in.readLine()) != null) {
                    try {
                        if (line.isEmpty()) {
                            continue;
                        }
                        if (firstLine.isEmpty() && DirHashFileFactory.hasLineBreak(line)) {
                            //dann ist schon die zweite Zeile oder
                            //es gibt eine zweite Zeile
                            firstLine = line;
                            continue;
                        }

                        if (firstLine.isEmpty()) {
                            fileData = DirHashFileFactory.getFileData(line);
                        } else {
                            fileData = DirHashFileFactory.getFileData(firstLine + P2LibConst.LINE_SEPARATOR + line);
                            firstLine = "";
                        }
                        tmp.add(fileData);
                    } catch (Exception ex) {
                        PLog.errorLog(704125890, ex, new String[]{"Kann die Zeile in der Hashdatei nicht verarbeiten:", line});
                    }
                }
                in.close();
            } catch (Exception ex) {
                PLog.errorLog(954102023, ex);
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
