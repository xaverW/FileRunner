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

import de.p2tools.fileRunner.controller.config.Events;
import de.p2tools.fileRunner.controller.config.ProgConfig;
import de.p2tools.fileRunner.controller.config.ProgData;
import de.p2tools.fileRunner.controller.data.fileData.FileData;
import de.p2tools.fileRunner.controller.data.fileData.FileDataList;
import de.p2tools.p2Lib.P2LibConst;
import de.p2tools.p2Lib.tools.events.RunEvent;
import de.p2tools.p2Lib.tools.log.PLog;

public class CompareFileList {
    private boolean stop = false;

    public void setStop() {
        stop = true;
    }

    public void compareList() {
        ProgData progData = ProgData.getInstance();
        FileDataList fileDataList1 = progData.fileDataList_1;
        FileDataList fileDataList2 = progData.fileDataList_2;

        stop = false;
        // erst mal alle auf diff/only setzen
        fileDataList1.stream().forEach(fd -> {
            fd.setOnly(true);
            fd.setDiff(false);
        });
        fileDataList2.stream().forEach(fd -> {
            fd.setOnly(true);
            fd.setDiff(false);
        });

        // und jetzt vergleichen
        if (ProgConfig.CONFIG_COMPARE_WITH_PATH.getValue()) {
            compareListPath(fileDataList1, fileDataList2);
        } else {
            compareListFile(fileDataList1, fileDataList2);
        }

        progData.pEventHandler.notifyListener(new RunEvent(Events.COMPARE_OF_FILE_LISTS_FINISHED));
    }

    private void compareListFile(FileDataList fileDataList1, FileDataList fileDataList2) {
        //Dateien ohne Pfad vergleichen
        //hier müssen beide Listen immer ganz durchgesucht werden, denn
        //es kann gleiche Dateien in unterschiedlichen Verzeichnissen geben
        //und dummerweise können die auch mehrfach vorkommen :(

        PLog.sysLog("Vergleichen ohne Verzeichnis:" + P2LibConst.LINE_SEPARATOR
                + fileDataList1.getSourceDir() + P2LibConst.LINE_SEPARATOR + fileDataList2.getSourceDir());
        fileDataList1.parallelStream().forEach(fd1 -> {
            if (stop) {
                return;
            }

            fileDataList2.stream()
                    .filter(fd2 -> fd1.getFileName().equals(fd2.getFileName()))
                    .forEach(fd2 -> {
                        //dann ist schon mal der Name gleich
                        fd1.setOnly(false);
                        fd2.setOnly(false);

                        if (!fd1.getHash().equals(fd2.getHash())) {
                            fd1.setDiff(true);
                            fd2.setDiff(true);
                        }
                    });
        });
    }

    private void compareListPath(FileDataList fileDataList1, FileDataList fileDataList2) {
        //und hier Dateien mit Pfad vergleichen
        PLog.sysLog("Vergleichen mit Verzeichnis:" + P2LibConst.LINE_SEPARATOR
                + fileDataList1.getSourceDir() + P2LibConst.LINE_SEPARATOR + fileDataList2.getSourceDir());
        fileDataList1.parallelStream().forEach(fd1 -> {
            if (stop) {
                return;
            }

            FileData fd2 = null;
            fd2 = fileDataList2.stream().filter(data -> data.getPathFileName().equals(fd1.getPathFileName()))
                    .findFirst().orElse(null);

            if (fd2 != null) {
                //dann gibts schon mal eine mit gleichem Namen
                fd1.setOnly(false);
                fd2.setOnly(false);
                if (!fd1.getHash().equals(fd2.getHash())) {
                    //und dann sind sie auch noch gleich
                    fd1.setDiff(true);
                    fd2.setDiff(true);
                }
            }
        });
    }
}
