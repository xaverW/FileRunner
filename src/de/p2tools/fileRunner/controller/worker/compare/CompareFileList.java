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


package de.p2tools.fileRunner.controller.worker.compare;

import de.p2tools.fileRunner.controller.RunEvent;
import de.p2tools.fileRunner.controller.RunListener;
import de.p2tools.fileRunner.controller.data.fileData.FileData;
import de.p2tools.fileRunner.controller.data.fileData.FileDataList;

import javax.swing.event.EventListenerList;

public class CompareFileList {
    private boolean stop = false;
    private EventListenerList listeners = new EventListenerList();

    public void addAdListener(RunListener listener) {
        listeners.add(RunListener.class, listener);
    }

    public void setStop() {
        stop = true;
    }


    private void notifyEvent(int max, int progress, String text) {
        RunEvent event;
        event = new RunEvent(this, progress, max, text);
        for (RunListener l : listeners.getListeners(RunListener.class)) {
            l.notify(event);
        }
    }

    public void compareList(FileDataList fileDataList1, FileDataList fileDataList2) {
        stop = false;
        notifyEvent(1, 0, "");
        // erst mal alle auf diff/only setzen
        fileDataList1.stream().forEach(fd -> {
            fd.setOnly(true);
            fd.setDiff(true);
        });
        fileDataList2.stream().forEach(fd -> {
            fd.setOnly(true);
            fd.setDiff(true);
        });

        // und jetzt gleiche suchen
        fileDataList1.parallelStream().forEach(fd1 -> {
            if (stop) {
                return;
            }

            FileData fd2 = fileDataList2.stream().filter(data -> data.getFileName().equals(fd1.getFileName()))
                    .findFirst().orElse(null);

            if (fd2 != null) {
                fd1.setOnly(false);
                fd2.setOnly(false);
                if (fd1.getHash().equals(fd2.getHash())) {
                    fd1.setDiff(false);
                    fd2.setDiff(false);
                }
            }

        });
        notifyEvent(1, 0, "");
    }

}
