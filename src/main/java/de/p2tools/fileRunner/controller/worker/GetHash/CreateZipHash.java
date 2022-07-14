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
import de.p2tools.fileRunner.controller.data.fileData.FileDataList;
import de.p2tools.fileRunner.controller.listener.PEventHandler;
import de.p2tools.fileRunner.controller.listener.PRunEvent;
import de.p2tools.fileRunner.controller.worker.compare.CompareFileList;
import de.p2tools.p2Lib.alert.PAlert;
import de.p2tools.p2Lib.hash.HashConst;
import de.p2tools.p2Lib.tools.date.PDate;
import de.p2tools.p2Lib.tools.file.PFileSize;
import de.p2tools.p2Lib.tools.log.PLog;
import javafx.application.Platform;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;

public class CreateZipHash {


    private ProgData progData;
    private boolean stop = false;
    //    private EventListenerList listeners = new EventListenerList();
    private int max = 0; //anzahl dateien
    private int progress = 0;

    public CreateZipHash(ProgData progData) {
        this.progData = progData;
    }

//    public void addAdListener(RunListener listener) {
//        listeners.add(RunListener.class, listener);
//    }

    public void setStop() {
        stop = true;
    }

    public void createHash(File file, FileDataList fileDataList) {
        max = 0;
        progress = 0;
        stop = false;

//        fileDataList.removeAll(fileDataList);
        fileDataList.clear();

        CreateHash createHash = new CreateHash(file, fileDataList);
        Thread thread = new Thread(createHash);
        thread.setName("CreateZipHash");
        thread.setDaemon(true);
        thread.start();
    }

    private void notifyEvent() {
        progData.pEventHandler.notifyEvent(new PRunEvent(PEventHandler.EVENT.GENERATE_COMPARE_FILE_LIST,
                this, progress, max, ""));

//        RunEvent event;
//        event = new RunEvent(this, progress, max, "");
//        for (RunListener l : listeners.getListeners(RunListener.class)) {
//            l.notify(event);
//        }
    }

    private class CreateHash implements Runnable {

        private File zipFile;
        private FileDataList fileDataList;

        public CreateHash(File zipFile, FileDataList fileDataList) {
            this.zipFile = zipFile;
            this.fileDataList = fileDataList;
        }

        public synchronized void run() {
            notifyEvent();

            try {
                ZipFile zFile = new ZipFile(zipFile);

                // Anzahl Dateien ermitteln und melden
                zFile.stream().filter(ze -> !ze.isDirectory()).forEach(e -> ++max);
                notifyEvent();

                ZipInputStream zipInputStream = new ZipInputStream(new FileInputStream(zipFile));
                ZipEntry zipEntry = zipInputStream.getNextEntry();
                while (!stop && zipEntry != null) {

                    if (zipEntry.isDirectory()) {
                        zipEntry = zipInputStream.getNextEntry();
                        continue;
                    }

                    InputStream inputStream = zFile.getInputStream(zipEntry);
                    hash(inputStream, zipEntry);

                    ++progress;
                    notifyEvent();
                    zipEntry = zipInputStream.getNextEntry();

                }

                zipInputStream.closeEntry();
                zipInputStream.close();

            } catch (Exception ex) {
                Platform.runLater(() -> PAlert.showErrorAlert("Zipdatei lesen",
                        "Die Zipdatei konnte nicht gelesen werden."));
            }


            if (stop) {
                fileDataList.clear();
            } else {
                new CompareFileList().compareList();
            }

            max = 0;
            progress = 0;
            notifyEvent();
        }


        private String hash(InputStream inputStream, ZipEntry zipEntry) {
            InputStream srcStream = null;
            byte[] buffer = new byte[1024];
            String ret = "";

            try {
                MessageDigest messageDigest = MessageDigest.getInstance(HashConst.HASH_MD5);
                srcStream = new DigestInputStream(inputStream, messageDigest);

                while (!stop && srcStream.read(buffer) > -1) {
                }
                ret = HashTools.getHashString(messageDigest.digest());

                String strFileZipEntry = zipEntry.getName();

                // damit / bei Win zu \ wird oder wenn fehlerhaft im zip: \ dann unter Linux zu /
                String strFile = strFileZipEntry.replace("/", File.separator);
                strFile = strFile.replace("\\", File.separator);

                if (strFile.startsWith(File.separator)) {
                    strFile = strFile.substring(1);
                }

                PDate fileDate = new PDate(zipEntry.getLastModifiedTime().toMillis());
                PFileSize fileSize = new PFileSize(zipEntry.getSize());
                fileDataList.addHashString(strFile, fileDate, fileSize, ret);

            } catch (Exception ex) {
                PLog.errorLog(978450202, ex, "Fehler! " + zipEntry.getName());
            } finally {
                try {
                    if (srcStream != null) {
                        srcStream.close();
                    }
                } catch (IOException ex) {
                }
            }
            return ret;
        }
    }
}
