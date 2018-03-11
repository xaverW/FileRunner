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


package de.p2tools.fileRunner.controller.config;

import de.p2tools.p2Lib.configFile.pData.PDataProgConfig;
import de.p2tools.p2Lib.hash.HashConst;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.StringProperty;

public class ProgConfig extends PDataProgConfig {

    public static StringProperty SYSTEM_PROG_OPEN_DIR = addStrProp("system-prog-open-dir", "");
    public static StringProperty SYSTEM_PROG_OPEN_URL = addStrProp("system-prog-open-uri", "");
    public static StringProperty SYSTEM_PROG_PLAY_FILE = addStrProp("system-prog-open-media", "");

    // Fenstereinstellungen
    public static StringProperty SYSTEM_GUI_SIZE = addStrProp("system-gui-size", "1000:900");
    public static StringProperty SYSTEM_CONFIG_DIALOG_SIZE = addStrProp("system-config-dialog-size", "500:500");
    public static BooleanProperty SYSTEM_STORE_CONFIG = addBoolProp("system-store-config", Boolean.TRUE);

    // GuiDirRunner
    public static StringProperty GUI_FILERUNNER_TABLE1_WIDTH = addStrProp("gui-filerunner-table1-width");
    public static StringProperty GUI_FILERUNNER_TABLE1_SORT = addStrProp("gui-filerunner-table1-sort");
    public static StringProperty GUI_FILERUNNER_TABLE1_UPDOWN = addStrProp("gui-filerunner-table1-upDown");
    public static StringProperty GUI_FILERUNNER_TABLE1_VIS = addStrProp("gui-filerunner-table1-vis");
    public static StringProperty GUI_FILERUNNER_TABLE1_ORDER = addStrProp("gui-filerunner-table1-order");

    public static StringProperty GUI_FILERUNNER_TABLE2_WIDTH = addStrProp("gui-filerunner-table2-width");
    public static StringProperty GUI_FILERUNNER_TABLE2_SORT = addStrProp("gui-filerunner-table2-sort");
    public static StringProperty GUI_FILERUNNER_TABLE2_UPDOWN = addStrProp("gui-filerunner-table2-upDown");
    public static StringProperty GUI_FILERUNNER_TABLE2_VIS = addStrProp("gui-filerunner-table2-vis");
    public static StringProperty GUI_FILERUNNER_TABLE2_ORDER = addStrProp("gui-filerunner-table2-order");

    public static StringProperty GUI_FILE_FILE1 = addStrProp("gui-file-file1");
    public static StringProperty GUI_FILE_HASH1 = addStrProp("gui-file-hash1");
    public static StringProperty GUI_FILE_FILE2 = addStrProp("gui-file-file2");
    public static StringProperty GUI_FILE_HASH2 = addStrProp("gui-file-hash2");

    public static StringProperty GUI_FILE_HASH = addStrProp("gui-file-hash", HashConst.HASH_MD5);
    public static StringProperty GUI_FILE_HASH_SUFF = addStrProp("gui-file-hash-suff", HashConst.HASH_MD5_SUFFIX);
    private static ProgConfig instance;

    public static final ProgConfig getInstance() {
        return instance == null ? instance = new ProgConfig() : instance;
    }

}
