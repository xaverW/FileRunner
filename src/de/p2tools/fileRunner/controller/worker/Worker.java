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


package de.p2tools.fileRunner.controller.worker;

import de.p2tools.fileRunner.controller.RunEvent;
import de.p2tools.fileRunner.controller.RunListener;
import de.p2tools.fileRunner.controller.config.ProgData;
import de.p2tools.fileRunner.controller.data.fileData.FileDataList;
import de.p2tools.fileRunner.controller.worker.GetHash.GetHash;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;

import javax.swing.event.EventListenerList;
import java.io.File;

public class Worker {
    private final ProgData progData;

    private final GetHash getHash;

    private EventListenerList listeners = new EventListenerList();
    private BooleanProperty working = new SimpleBooleanProperty(false);

    public Worker(ProgData progData) {
        this.progData = progData;
        getHash = new GetHash(progData);

        getHash.addAdListener(new RunListener() {
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
        getHash.setStop();
    }

    public void readDir(File dir, FileDataList fileDataList, int sumThreads, boolean recursiv) {
        getHash.hashLesen(dir, fileDataList, sumThreads, recursiv);
    }

    public void writeHash(FileDataList fileDataList, File destFile) {
        getHash.hashSchreiben(fileDataList, destFile);
    }

    private void notifyEvent(RunEvent runEvent) {
        working.setValue(!runEvent.nixLos());

        for (RunListener l : listeners.getListeners(RunListener.class)) {
            l.notify(runEvent);
        }
    }

}
