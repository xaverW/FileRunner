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

import de.p2tools.p2Lib.configFile.pConfData.PConfData;
import de.p2tools.p2Lib.configFile.pConfData.PConfList;
import de.p2tools.p2Lib.configFile.pData.PData;
import de.p2tools.p2Lib.hash.HashConst;

public class ProgConfList extends PConfList {
    // wegen des Problems mit ext. Programmaufrufen und Leerzeichen
    public static PConfData SYSTEM_PROG_OPEN_DIR = addNewKey("Programm-Ordner-oeffnen");
    public static PConfData SYSTEM_PROG_OPEN_URL = addNewKey("Programm-Url-oeffnen");
    public static PConfData SYSTEM_PROG_PLAY_FILE = addNewKey("Programm-zum-Abspielen");

    // Fenstereinstellungen
    public static PConfData SYSTEM_GROESSE_GUI = addNewKey("Groesse-Gui", "1000:900");
    public static PConfData SYSTEM_CONFIG_DIALOG_SIZE = addNewKey("system-config-dialog-size", "500:500");
    public static PConfData SYSTEM_STORE_CONFIG = addNewKey("system-store-config", Boolean.TRUE.toString());

    // GuiDirRunner
    public static PConfData GUI_FILERUNNER_TABLE1_WIDTH = addNewKey("gui-filerunner-table1-width");
    public static PConfData GUI_FILERUNNER_TABLE1_SORT = addNewKey("gui-filerunner-table1-sort");
    public static PConfData GUI_FILERUNNER_TABLE1_UPDOWN = addNewKey("gui-filerunner-table1-upDown");
    public static PConfData GUI_FILERUNNER_TABLE1_VIS = addNewKey("gui-filerunner-table1-vis");
    public static PConfData GUI_FILERUNNER_TABLE1_ORDER = addNewKey("gui-filerunner-table1-order");

    public static PConfData GUI_FILERUNNER_TABLE2_WIDTH = addNewKey("gui-filerunner-table2-width");
    public static PConfData GUI_FILERUNNER_TABLE2_SORT = addNewKey("gui-filerunner-table2-sort");
    public static PConfData GUI_FILERUNNER_TABLE2_UPDOWN = addNewKey("gui-filerunner-table2-upDown");
    public static PConfData GUI_FILERUNNER_TABLE2_VIS = addNewKey("gui-filerunner-table2-vis");
    public static PConfData GUI_FILERUNNER_TABLE2_ORDER = addNewKey("gui-filerunner-table2-order");

    public static PConfData GUI_FILE_FILE1 = addNewKey("gui-file-file1");
    public static PConfData GUI_FILE_HASH1 = addNewKey("gui-file-hash1");
    public static PConfData GUI_FILE_FILE2 = addNewKey("gui-file-file2");
    public static PConfData GUI_FILE_HASH2 = addNewKey("gui-file-hash2");

    public static PConfData GUI_FILE_HASH = addNewKey("gui-file-hash", HashConst.HASH_MD5);
    public static PConfData GUI_FILE_HASH_SUFF = addNewKey("gui-file-hash-suff", HashConst.HASH_MD5_SUFFIX);

    public static PData getConfigsData() {
        return PConfList.getPData();
    }

}
