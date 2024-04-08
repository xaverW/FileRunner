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

import de.p2tools.p2lib.alert.P2Alert;
import de.p2tools.p2lib.tools.net.PUrlTools;

import java.io.File;

public class HashFactory {
    public static final String hashStart = "*hash[";
    public static final String hashEnd = "]";
    public static final String fileStart = "*file[";
    public static final String fileEnd = "]";
    public static final String sizeStart = "*size[";
    public static final String sizeEnd = "]";
    public static final String dateStart = "*date[";
    public static final String dateEnd = "]";
    public static final String linkStart = "*link[";
    public static final String linkEnd = "]";
    public static final String pause = "   ";

    private HashFactory() {
    }

    public static boolean checkFile(String file) {
        if (PUrlTools.isUrl(file) && PUrlTools.urlExists(file) || new File(file).exists()) {
            return true;
        }
        if (PUrlTools.isUrl(file)) {
            P2Alert.showErrorAlert("Hash erstellen", "Die angegebene URL existiert nicht!");
        } else {
            P2Alert.showErrorAlert("Hash erstellen", "Die angegebene Datei existiert nicht!");
        }
        return false;
    }
}
