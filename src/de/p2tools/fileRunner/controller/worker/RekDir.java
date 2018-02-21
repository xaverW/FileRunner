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

import de.p2tools.fileRunner.controller.config.ProgData;
import de.p2tools.fileRunner.gui.dialog.MTAlert;
import de.p2tools.p2Lib.tools.Log;
import javafx.application.Platform;

import java.io.File;

public class RekDir {
    private File dir;
    private boolean stop = false;
    private boolean rekur = false;
    private ProgData progData;

    public RekDir(ProgData progData) {
        this.progData = progData;
    }

    public int rekDir(File ddir, boolean rrekur) {
        dir = ddir;
        rekur = rrekur;
        return runDir(dir);
    }

    public void setStop() {
        stop = true;
    }

    void tuwas(File file) {
    }

    private int runDir(File dir) {
        int r = 0;
        try {
            if (stop) {
                return 0;
            }
            File[] list = null;
            if (dir.isDirectory()) {
                list = dir.listFiles();
                if (list == null) {
                    final String path = dir.getCanonicalPath();
                    Platform.runLater(() ->
                            MTAlert.showErrorAlert("Dateien lesen", "Kann Dateien des Ordners \"" + path + "\" nicht lesen", "Fehler!"));
                } else {
                    for (int i = 0; i < list.length; i++) {
                        if (list[i].isFile()) {
                            ++r;
                            tuwas(list[i]);
                        } else if (list[i].isDirectory() && rekur) {
                            r += runDir(list[i]);
                        }
                    }
                }
            }
            if (dir.isFile()) {
                ++r;
                tuwas(dir);
            }
        } catch (Exception ex) {
            Log.errorLog(945123697, ex, "Fehler beim Schreiben der Hashdatei!");
        }
        return r;
    }

}
