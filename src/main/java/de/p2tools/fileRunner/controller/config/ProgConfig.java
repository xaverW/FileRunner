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

import de.p2tools.p2Lib.configFile.ConfigFile;
import de.p2tools.p2Lib.configFile.config.Config;
import de.p2tools.p2Lib.configFile.pData.PDataProgConfig;
import de.p2tools.p2Lib.hash.HashConst;
import de.p2tools.p2Lib.tools.ProgramTools;
import javafx.beans.property.*;

import java.util.ArrayList;

public class ProgConfig extends PDataProgConfig {

    private static final ArrayList<Config> arrayList = new ArrayList<>();

    //Configs der Programmversion, nur damit sie (zur Update-Suche) im Config-File stehen
    public static StringProperty SYSTEM_PROG_VERSION = addStr("system-prog-version");
    public static StringProperty SYSTEM_PROG_BUILD_NO = addStr("system-prog-build-no");
    public static StringProperty SYSTEM_PROG_BUILD_DATE = addStr("system-prog-build-date");
    public static StringProperty SYSTEM_DOWNLOAD_DIR_NEW_VERSION = addStr("system-download-dir-new-version", "");


    //Programmupdate
    public static StringProperty SYSTEM_UPDATE_DATE = addStr("system-update-date"); // Datum der letzten Prüfung
    public static BooleanProperty SYSTEM_UPDATE_SEARCH_ACT = addBool("system-update-search-act", true); //Infos und Programm
    public static BooleanProperty SYSTEM_UPDATE_SEARCH_BETA = addBool("system-update-search-beta", false); //beta suchen
    public static BooleanProperty SYSTEM_UPDATE_SEARCH_DAILY = addBool("system-update-search-daily", false); //daily suchen

    public static StringProperty SYSTEM_UPDATE_LAST_INFO = addStr("system-update-last-info");
    public static StringProperty SYSTEM_UPDATE_LAST_ACT = addStr("system-update-last-act");
    public static StringProperty SYSTEM_UPDATE_LAST_BETA = addStr("system-update-last-beta");
    public static StringProperty SYSTEM_UPDATE_LAST_DAILY = addStr("system-update-last-daily");


    public static StringProperty SYSTEM_PROG_OPEN_URL = addStr("system-prog-open-uri", "");
    public static StringProperty SYSTEM_LOG_DIR = addStr("system-log-dir", "");
    public static BooleanProperty SYSTEM_DARK_THEME = addBool("system-dark-theme", false);

    public static IntegerProperty SYSTEM_CONFIG_DIALOG_TAB = new SimpleIntegerProperty(0);
    public static BooleanProperty CONFIG_DIALOG_ACCORDION = addBool("config_dialog-accordion", Boolean.TRUE);
    public static IntegerProperty SYSTEM_CONFIG_DIALOG_CONFIG = new SimpleIntegerProperty(-1);
    public static IntegerProperty SYSTEM_CONFIG_DIALOG_COLOR = new SimpleIntegerProperty(-1);


    // Fenstereinstellungen
    public static StringProperty SYSTEM_GUI_SIZE = addStr("system-gui-size", "1000:900");
    public static StringProperty SYSTEM_CONFIG_DIALOG_SIZE = addStr("system-config-dialog-size", "750:500");
    public static StringProperty SYSTEM_SELECT_HASH_DIALOG_SIZE = addStr("system-select-hash-dialog-size", "500:500");

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

    public static StringProperty GUI_FILE_HASH = addStr("gui-file-hash", HashConst.HASH_MD5);
    public static StringProperty GUI_FILE_HASH_SUFF = addStr("gui-file-hash-suff", HashConst.HASH_MD5_SUFFIX);
    public static BooleanProperty CONFIG_COMPARE_WITH_PATH = addBool("config-compare-with-path", Boolean.TRUE);

    private static ProgConfig instance;

    private ProgConfig() {
        super(arrayList, "ProgConfig");
    }

    public static final ProgConfig getInstance() {
        return instance == null ? instance = new ProgConfig() : instance;
    }

    public static void addConfigData(ConfigFile configFile) {
        // Configs der Programmversion, nur damit sie (zur Update-Suche) im Config-File stehen
        ProgConfig.SYSTEM_PROG_VERSION.set(ProgramTools.getProgVersion());
        ProgConfig.SYSTEM_PROG_BUILD_NO.set(ProgramTools.getBuild());
        ProgConfig.SYSTEM_PROG_BUILD_DATE.set(ProgramTools.getCompileDate());

        configFile.addConfigs(ProgConfig.getInstance());
        configFile.addConfigs(ProgColorList.getConfigsData());
        configFile.addConfigs(ProgData.getInstance().projectData);
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

    private static DoubleProperty addDouble(String key, double init) {
        return addDoubleProp(arrayList, key, init);
    }

    private static BooleanProperty addBool(String key) {
        return addBoolProp(arrayList, key, true);
    }
}
