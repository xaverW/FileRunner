/*
 * P2tools Copyright (C) 2022 W. Xaver W.Xaver[at]googlemail.com
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


package de.p2tools.filerunner.gui;

import de.p2tools.filerunner.controller.config.ProgConfig;

import java.io.File;

public class GuiFactory {

    private GuiFactory() {
    }

    public static void setLastUsedDir(String dir, boolean panel1) {
        if (dir.isEmpty()) {
            return;
        }

        File f = new File(dir);
        if (!f.exists()) {
            return;
        }

        if (f.isFile() && f.getParentFile() != null) {
            //ist eine Datei -> Verzeichnis der Datei
            f = f.getParentFile();
            if (panel1) {
                ProgConfig.lastUsedDir1.setValue(f.getPath());
            } else {
                ProgConfig.lastUsedDir2.set(f.getPath());
            }

        } else if (f.isDirectory() && f.getParentFile() != null) {
            //ist Verzeichnis und nicht root -> Parent
            if (panel1) {
                ProgConfig.lastUsedDir1.set(f.getParent());
            } else {
                ProgConfig.lastUsedDir2.set(f.getParent());
            }
        } else if (f.isDirectory()) {
            //ist Verzeichnis und ROOT -> dann bleibt ROOT
            if (panel1) {
                ProgConfig.lastUsedDir1.set(f.getPath());
            } else {
                ProgConfig.lastUsedDir2.set(f.getPath());
            }
        }
    }
}
