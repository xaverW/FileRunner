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
import de.p2tools.fileRunner.controller.data.fileData.FileDataList;
import de.p2tools.fileRunner.controller.worker.CompareFileListFactory;
import de.p2tools.p2Lib.alert.PAlert;
import de.p2tools.p2Lib.hash.HashConst;
import de.p2tools.p2Lib.tools.date.PDate;
import de.p2tools.p2Lib.tools.file.PFileSize;
import de.p2tools.p2Lib.tools.log.PLog;
import javafx.application.Platform;
import javafx.beans.property.StringProperty;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;

public class DirHash_CreateZipHash {

    private ProgData progData;
    private final boolean list1;
    private boolean stop = false;
    private int max = 0; //Anzahl Dateien
    private int progress = 0;

    private final FileDataList fileDataList;
    private final StringProperty searchDir;

    public DirHash_CreateZipHash(ProgData progData, boolean list1) {
        this.progData = progData;
        this.list1 = list1;

        fileDataList = list1 ? progData.fileDataList_1 : progData.fileDataList_2;
        searchDir = list1 ? ProgConfig.searchZip1 : ProgConfig.searchZip2;
    }

    public void setStop() {
        stop = true;
    }

    public boolean isRunning() {
//        System.out.println("DirHash_CreateZipHash, isRunning: " + list1 + " - " + running);
        return max > 0;
    }

    public void createHash(File file) {
        System.out.println("createHash: " + file.getAbsolutePath());
        max = 1;
        progress = 0;
        stop = false;
        fileDataList.clear();
        fileDataList.setSourceDir(file.getAbsolutePath());

        CreateHash createHash = new CreateHash(file);
        Thread thread = new Thread(createHash);
        thread.setName("CreateZipHash");
        thread.setDaemon(true);
        thread.start();
    }

    private void notifyEvent(String text) {
        progData.pEventHandler.notifyListener(new RunPEvent(Events.GENERATE_COMPARE_FILE_LIST,
                progress, max, text));
    }

    private class CreateHash implements Runnable {

        private File zipFile;

        public CreateHash(File zipFile) {
            this.zipFile = zipFile;
        }

        public synchronized void run() {
            notifyEvent(zipFile.toString());

            try {
                ZipFile zFile = new ZipFile(zipFile);

                // Anzahl Dateien ermitteln und melden
                zFile.stream().filter(ze -> !ze.isDirectory()).forEach(e -> ++max);
                notifyEvent(zipFile.getAbsolutePath());

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
                    notifyEvent(zipFile.getAbsolutePath());
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
            }

            CompareFileListFactory.compareList();
            max = 0;
            progress = 0;
            notifyEvent(zipFile.getAbsolutePath());
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
                ret = DirHashFileFactory.getHashString(messageDigest.digest());

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
