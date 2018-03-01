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

import de.p2tools.fileRunner.controller.RunEvent;
import de.p2tools.fileRunner.controller.RunListener;
import de.p2tools.fileRunner.controller.config.ProgData;
import de.p2tools.fileRunner.controller.data.fileData.FileDataList;
import de.p2tools.fileRunner.controller.worker.GetHash.CreateDirHash;
import de.p2tools.fileRunner.controller.worker.GetHash.CreateFileHash;
import de.p2tools.fileRunner.controller.worker.GetHash.HashTools;
import de.p2tools.fileRunner.controller.worker.GetHash.ReadDirHashFile;
import de.p2tools.p2Lib.hash.WriteHashFile;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.StringProperty;

import javax.swing.event.EventListenerList;
import java.io.File;

public class Worker {
    private final ProgData progData;

    private final CreateDirHash createDirHash;
    private final ReadDirHashFile readDirHashFile;
    private final CreateFileHash createFileHash;
    private EventListenerList listeners = new EventListenerList();
    private BooleanProperty working = new SimpleBooleanProperty(false);

    public Worker(ProgData progData) {
        this.progData = progData;
        createDirHash = new CreateDirHash(progData);
        readDirHashFile = new ReadDirHashFile(progData);
        createFileHash = new CreateFileHash();

        createDirHash.addAdListener(new RunListener() {
            @Override
            public void ping(RunEvent runEvent) {
                notifyEvent(runEvent);
            }
        });
        readDirHashFile.addAdListener(new RunListener() {
            @Override
            public void ping(RunEvent runEvent) {
                notifyEvent(runEvent);
            }
        });
        createFileHash.addAdListener(new RunListener() {
            @Override
            public void ping(RunEvent runEvent) {
                notifyEvent(runEvent);
            }
        });
    }

    public boolean isWorking() {
        return working.get();
    }

    public BooleanProperty workingProperty() {
        return working;
    }

    /**
     * @param listener
     */
    public void addAdListener(RunListener listener) {
        listeners.add(RunListener.class, listener);
    }

    public void setStop() {
        createDirHash.setStop();
        readDirHashFile.setStop();
        createFileHash.setStop();
    }

    public void createDirHash(File dir, FileDataList fileDataList, int sumThreads, boolean recursiv, boolean followLink) {
        createDirHash.createHash(dir, fileDataList, sumThreads, recursiv, followLink);
    }

    public void readDirHashFile(File file, FileDataList fileDataList) {
        readDirHashFile.readFile(file, fileDataList);
    }

    public void writeDirHashFile(File destFile, FileDataList fileDataList) {
        HashTools.writeDirHashFile(destFile, fileDataList);
    }

    public void createFileHash(File file, StringProperty stringProperty) {
        createFileHash.genHash(file, stringProperty);
    }

    public void createFileHash(File file1, StringProperty stringProperty1, File file2, StringProperty stringProperty2) {
        createFileHash.genHash(file1, stringProperty1, file2, stringProperty2);
    }

    public void writeFileHash(File file, String fileHash, String hash) {
        WriteHashFile.writeFileHash(file, fileHash, hash);
    }

    private void notifyEvent(RunEvent runEvent) {
        working.setValue(!runEvent.nixLos());

        for (RunListener l : listeners.getListeners(RunListener.class)) {
            l.notify(runEvent);
        }
    }

}
