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
import de.p2tools.fileRunner.controller.config.ProgConfig;
import de.p2tools.fileRunner.controller.config.ProgData;
import de.p2tools.fileRunner.controller.config.RunPEvent;
import de.p2tools.fileRunner.controller.data.fileData.FileData;
import de.p2tools.fileRunner.controller.data.fileData.FileDataList;
import de.p2tools.fileRunner.controller.worker.CompareFileListFactory;
import de.p2tools.p2Lib.P2LibConst;
import de.p2tools.p2Lib.tools.log.PLog;
import javafx.beans.property.StringProperty;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.util.ArrayList;

public class DirHash_ReadHashFile {


    private boolean stop = false;
    private final ProgData progData;
    private final boolean list1;
    private int max = 0;

    private final FileDataList fileDataList;
    private final StringProperty searchHashFile;

    public DirHash_ReadHashFile(ProgData progData, boolean list1) {
        this.progData = progData;
        this.list1 = list1;
        fileDataList = list1 ? progData.fileDataList_1 : progData.fileDataList_2;
        searchHashFile = list1 ? ProgConfig.searchHashFile1 : ProgConfig.searchHashFile2;
    }

    public void setStop() {
        stop = true;
    }

    public boolean isRunning() {
        return max > 0;
    }

    public void readFile() {
        stop = false;
        max = 1;
        notifyEvent(0, "");
        HashFileRead hashFileRead = new HashFileRead();
        new Thread(hashFileRead).start();
    }

    private void notifyEvent(int progress, String text) {
        progData.pEventHandler.notifyListener(new RunPEvent(Events.GENERATE_COMPARE_FILE_LIST,
                progress, max, text));
    }

    private class HashFileRead implements Runnable {

        private final File hashFile;

        public HashFileRead() {
            File hashFile = new File(searchHashFile.getValueSafe());
            this.hashFile = hashFile;

            fileDataList.clear();
            fileDataList.setSourceDir(hashFile.getAbsolutePath());
        }

        public synchronized void run() {
            //Liste aus Hashdatei laden
            max = 1;
            notifyEvent(0, hashFile.getAbsolutePath());

            laden();
            if (stop) {
                fileDataList.clear();
            }

            PLog.sysLog("DirHash_ReadHashFile, max: " + list1 + " - " + max);
            CompareFileListFactory.compareList();
            max = 0;
            notifyEvent(0, hashFile.getAbsolutePath());
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
