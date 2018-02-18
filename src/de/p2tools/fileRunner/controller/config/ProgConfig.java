/*
 * MTPlayer Copyright (C) 2017 W. Xaver W.Xaver[at]googlemail.com
 * https://sourceforge.net/projects/mtplayer/
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

import de.p2tools.p2Lib.configFile.ConfigsData;
import de.p2tools.p2Lib.tools.PConfig;
import de.p2tools.p2Lib.tools.PConfigs;

public class ProgConfig extends PConfig {

    // wegen des Problems mit ext. Programmaufrufen und Leerzeichen
    public static PConfigs SYSTEM_PROG_OPEN_DIR = addNewKey("Programm-Ordner-oeffnen");
    public static PConfigs SYSTEM_PROG_OPEN_URL = addNewKey("Programm-Url-oeffnen");
    public static PConfigs SYSTEM_PROG_PLAY_FILE = addNewKey("Programm-zum-Abspielen");

    // Fenstereinstellungen
    public static PConfigs SYSTEM_GROESSE_GUI = addNewKey("Groesse-Gui", "1000:900");

    // GuiFileRunner
    public static PConfigs GUI_FILERUNNER_TABLE1_WIDTH = addNewKey("gui-filerunner-table1-width");
    public static PConfigs GUI_FILERUNNER_TABLE1_SORT = addNewKey("gui-filerunner-table1-sort");
    public static PConfigs GUI_FILERUNNER_TABLE1_UPDOWN = addNewKey("gui-filerunner-table1-upDown");
    public static PConfigs GUI_FILERUNNER_TABLE1_VIS = addNewKey("gui-filerunner-table1-vis");
    public static PConfigs GUI_FILERUNNER_TABLE1_ORDER = addNewKey("gui-filerunner-table1-order");

    public static PConfigs GUI_FILERUNNER_TABLE2_WIDTH = addNewKey("gui-filerunner-table2-width");
    public static PConfigs GUI_FILERUNNER_TABLE2_SORT = addNewKey("gui-filerunner-table2-sort");
    public static PConfigs GUI_FILERUNNER_TABLE2_UPDOWN = addNewKey("gui-filerunner-table2-upDown");
    public static PConfigs GUI_FILERUNNER_TABLE2_VIS = addNewKey("gui-filerunner-table2-vis");
    public static PConfigs GUI_FILERUNNER_TABLE2_ORDER = addNewKey("gui-filerunner-table2-order");

    public static PConfigs GUI_FILERUNNER_DIR1 = addNewKey("gui-filerunner-dir1");
    public static PConfigs GUI_FILERUNNER_DIR2 = addNewKey("gui-filerunner-dir2");
    public static PConfigs GUI_FILERUNNER_HASH1 = addNewKey("gui-filerunner-hash1");
    public static PConfigs GUI_FILERUNNER_HASH2 = addNewKey("gui-filerunner-hash2");
    public static PConfigs GUI_FILERUNNER_WRITE_HASH1 = addNewKey("gui-filerunner-write-hash1");
    public static PConfigs GUI_FILERUNNER_WRITE_HASH2 = addNewKey("gui-filerunner-write-hash2");


    public static ConfigsData getConfigsData() {
        // sonst werden die Keys nich vorher angelegt :)
        return PConfig.getConfigsData();
    }

}
