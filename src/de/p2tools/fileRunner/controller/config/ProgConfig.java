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

import de.p2tools.p2Lib.configFile.config.Config;
import de.p2tools.p2Lib.configFile.pData.PDataProgConfig;
import de.p2tools.p2Lib.hash.HashConst;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.StringProperty;

import java.util.ArrayList;

public class ProgConfig extends PDataProgConfig {

    private static final ArrayList<Config> arrayList = new ArrayList<>();

    public static StringProperty SYSTEM_PROG_OPEN_DIR = addStr("system-prog-open-dir", "");
    public static StringProperty SYSTEM_PROG_OPEN_URL = addStr("system-prog-open-uri", "");
    public static StringProperty SYSTEM_PROG_PLAY_FILE = addStr("system-prog-open-media", "");
    public static StringProperty SYSTEM_LOG_DIR = addStr("system-log-dir", "");

    // Fenstereinstellungen
    public static StringProperty SYSTEM_GUI_SIZE = addStr("system-gui-size", "1000:900");
    public static StringProperty SYSTEM_CONFIG_DIALOG_SIZE = addStr("system-config-dialog-size", "500:500");
    public static BooleanProperty SYSTEM_STORE_CONFIG = addBool("system-store-config", Boolean.TRUE);
    public static IntegerProperty SYSTEM_INFOS_NR = addInt("system-info-nr", 0);

    // GuiDirRunner
    public static StringProperty GUI_FILERUNNER_TABLE1_WIDTH = addStr("gui-filerunner-table1-width");
    public static StringProperty GUI_FILERUNNER_TABLE1_SORT = addStr("gui-filerunner-table1-sort");
    public static StringProperty GUI_FILERUNNER_TABLE1_UPDOWN = addStr("gui-filerunner-table1-upDown");
    public static StringProperty GUI_FILERUNNER_TABLE1_VIS = addStr("gui-filerunner-table1-vis");
    public static StringProperty GUI_FILERUNNER_TABLE1_ORDER = addStr("gui-filerunner-table1-order");

    public static StringProperty GUI_FILERUNNER_TABLE2_WIDTH = addStr("gui-filerunner-table2-width");
    public static StringProperty GUI_FILERUNNER_TABLE2_SORT = addStr("gui-filerunner-table2-sort");
    public static StringProperty GUI_FILERUNNER_TABLE2_UPDOWN = addStr("gui-filerunner-table2-upDown");
    public static StringProperty GUI_FILERUNNER_TABLE2_VIS = addStr("gui-filerunner-table2-vis");
    public static StringProperty GUI_FILERUNNER_TABLE2_ORDER = addStr("gui-filerunner-table2-order");

    public static StringProperty GUI_FILE_FILE1 = addStr("gui-file-file1");
    public static StringProperty GUI_FILE_HASH1 = addStr("gui-file-hash1");
    public static StringProperty GUI_FILE_FILE2 = addStr("gui-file-file2");
    public static StringProperty GUI_FILE_HASH2 = addStr("gui-file-hash2");

    public static StringProperty GUI_FILE_HASH = addStr("gui-file-hash", HashConst.HASH_MD5);
    public static StringProperty GUI_FILE_HASH_SUFF = addStr("gui-file-hash-suff", HashConst.HASH_MD5_SUFFIX);
    private static ProgConfig instance;

    public static final ProgConfig getInstance() {
        return instance == null ? instance = new ProgConfig() : instance;
    }

    private static StringProperty addStr(String key) {
        return addStrProp(arrayList, key);
    }

    private static StringProperty addStr(String key, String init) {
        return addStrProp(arrayList, key, init);
    }

    private static IntegerProperty addInt(String key, int init) {
        return addIntProp(arrayList, key, init);
    }

    private static BooleanProperty addBool(String key, boolean init) {
        return addBoolProp(arrayList, key, init);
    }
}
