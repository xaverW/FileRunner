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


package de.p2tools.fileRunner.controller.worker;

import de.p2tools.fileRunner.controller.config.ProgData;
import de.p2tools.fileRunner.controller.data.fileData.FileDataList;
import de.p2tools.fileRunner.controller.worker.GetHash.*;
import de.p2tools.p2Lib.hash.WriteHashFile;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.StringProperty;

import javax.swing.event.EventListenerList;
import java.io.File;

public class Worker {
    private final ProgData progData;

    private final CreateDirHash createDirHash;
    private final CreateZipHash createZipHash;
    private final ReadDirHashFile readDirHashFile;
    private final CreateFileHash createFileHash;
    private final ReadHashFile readHashFile;
    private EventListenerList listeners = new EventListenerList();
    private BooleanProperty working = new SimpleBooleanProperty(false);

    public Worker(ProgData progData) {
        this.progData = progData;
        createDirHash = new CreateDirHash(progData);
        createZipHash = new CreateZipHash(progData);
        readDirHashFile = new ReadDirHashFile(progData);
        createFileHash = new CreateFileHash();
        readHashFile = new ReadHashFile();
    }

    public boolean isWorking() {
        return working.get();
    }

    public BooleanProperty workingProperty() {
        return working;
    }

    public void setStop() {
        createDirHash.setStop();
        createZipHash.setStop();
        readDirHashFile.setStop();
        createFileHash.setStop();
        readHashFile.setStop();
    }

    public void createDirHash(File dir, FileDataList fileDataList, int sumThreads, boolean recursiv, boolean followLink) {
        createDirHash.createHash(dir, fileDataList, sumThreads, recursiv, followLink);
    }

    public void createZipHash(File zipFile, FileDataList fileDataList) {
        createZipHash.createHash(zipFile, fileDataList);
    }

    public void readDirHashFile(File file, FileDataList fileDataList) {
        readDirHashFile.readFile(file, fileDataList);
    }

    public boolean writeDirHashFile(File destFile, FileDataList fileDataList) {
        return HashTools.writeDirHashFile(destFile, fileDataList);
    }

    public void readHashFile(String file, StringProperty stringProperty) {
        readHashFile.readHash(file, stringProperty);
    }

    public void createFileHash(String file, StringProperty stringProperty) {
        createFileHash.genHash(file, stringProperty);
    }

    public void writeFileHash(File file, String fileHash, String hash) {
        WriteHashFile.writeFileHash(file, fileHash, hash);
    }
}
