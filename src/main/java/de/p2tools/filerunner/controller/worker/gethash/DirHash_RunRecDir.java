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


package de.p2tools.filerunner.controller.worker.gethash;

import de.p2tools.p2lib.dialogs.PDialogFileChosser;
import de.p2tools.p2lib.tools.log.PLog;
import javafx.application.Platform;

import java.io.File;

public class DirHash_RunRecDir {
    private File dir;
    private boolean stop = false;
    private boolean recur = false;
    private boolean altert = false;

    public int recDir(File dir, boolean recur) {
        this.dir = dir;
        this.recur = recur;
        return runDir(this.dir);
    }

    public void setStop() {
        stop = true;
    }

    void work(File file) {
    }

    private int runDir(File dir) {
        int r = 0;
        try {
            if (stop) {
                return 0;
            }
            File[] list;
            if (dir.isDirectory()) {
                list = dir.listFiles();
                if (list == null) {
                    final String path = dir.getCanonicalPath();
                    if (!altert) {
                        altert = true;
                        Platform.runLater(() ->
                                PDialogFileChosser.showErrorAlert("Dateien lesen", "Kann Dateien des Ordners \"" + path + "\" nicht lesen", "Fehler!"));
                    }
                } else {
                    for (int i = 0; i < list.length; i++) {
                        if (list[i].isFile()) {
                            ++r;
                            work(list[i]);
                        } else if (list[i].isDirectory() && recur) {
                            r += runDir(list[i]);
                        }
                    }
                }
            }
            if (dir.isFile()) {
                ++r;
                work(dir);
            }
        } catch (Exception ex) {
            PLog.errorLog(945123697, ex, "Fehler beim Schreiben der Hashdatei!");
        }
        return r;
    }
}
