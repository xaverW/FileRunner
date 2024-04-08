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


package de.p2tools.filerunner.controller.config;

import de.p2tools.p2lib.configfile.ConfigFile;
import de.p2tools.p2lib.configfile.config.Config;
import de.p2tools.p2lib.data.P2ColorData;
import de.p2tools.p2lib.data.P2DataProgConfig;
import de.p2tools.p2lib.hash.HashConst;
import de.p2tools.p2lib.tools.P2ToolsFactory;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.ObservableList;

import java.util.ArrayList;

public class ProgConfig extends P2DataProgConfig {

    private static final ArrayList<Config> arrayList = new ArrayList<>();

    //Configs der Programmversion, nur damit sie (zur Update-Suche) im Config-File stehen
    public static StringProperty SYSTEM_PROG_VERSION = addStrProp("system-prog-version");
    public static StringProperty SYSTEM_PROG_BUILD_NO = addStrProp("system-prog-build-no");
    public static StringProperty SYSTEM_PROG_BUILD_DATE = addStrProp("system-prog-build-date");
    public static StringProperty SYSTEM_DOWNLOAD_DIR_NEW_VERSION = addStrProp("system-download-dir-new-version", "");
    public static StringProperty SYSTEM_PROG_OPEN_DIR = addStrProp("system-prog-open-dir");

    //Programmupdate
    public static StringProperty SYSTEM_UPDATE_DATE = addStrProp("system-update-date"); // Datum der letzten Prüfung
    public static BooleanProperty SYSTEM_UPDATE_SEARCH_ACT = addBoolProp("system-update-search-act", true); //Infos und Programm
    public static BooleanProperty SYSTEM_UPDATE_SEARCH_BETA = addBoolProp("system-update-search-beta", false); //beta suchen
    public static BooleanProperty SYSTEM_UPDATE_SEARCH_DAILY = addBoolProp("system-update-search-daily", false); //daily suchen

    public static StringProperty SYSTEM_UPDATE_LAST_INFO = addStrProp("system-update-last-info");
    public static StringProperty SYSTEM_UPDATE_LAST_ACT = addStrProp("system-update-last-act");
    public static StringProperty SYSTEM_UPDATE_LAST_BETA = addStrProp("system-update-last-beta");
    public static StringProperty SYSTEM_UPDATE_LAST_DAILY = addStrProp("system-update-last-daily");

    public static StringProperty SYSTEM_PROG_OPEN_URL = addStrProp("system-prog-open-uri", "");
    public static StringProperty SYSTEM_LOG_DIR = addStrProp("system-log-dir", "");
    public static BooleanProperty SYSTEM_DARK_THEME = addBoolProp("system-dark-theme", false);
    public static BooleanProperty SYSTEM_EVEN_ODD = addBoolProp("system-even-odd", true);
    public static IntegerProperty SYSTEM_EVEN_ODD_VALUE = addIntProp("system-even-odd-value", P2ColorData.ODD_DIV);

    public static IntegerProperty SYSTEM_CONFIG_DIALOG_TAB = new SimpleIntegerProperty(0);
    public static BooleanProperty CONFIG_DIALOG_ACCORDION = addBoolProp("config_dialog-accordion", Boolean.TRUE);
    public static IntegerProperty SYSTEM_CONFIG_DIALOG_GENERAL = new SimpleIntegerProperty(-1);
    public static IntegerProperty SYSTEM_CONFIG_DIALOG_CONFIG = new SimpleIntegerProperty(-1);
    public static IntegerProperty SYSTEM_CONFIG_DIALOG_COLOR = new SimpleIntegerProperty(-1);

    // Fenstereinstellungen
    public static StringProperty SYSTEM_GUI_SIZE = addStrProp("system-gui-size", "1000:800");
    public static StringProperty SYSTEM_CONFIG_DIALOG_SIZE = addStrProp("system-config-dialog-size", "900:650");
    public static StringProperty SYSTEM_SELECT_HASH_DIALOG_SIZE = addStrProp("system-select-hash-dialog-size", "500:500");
    public static StringProperty SYSTEM_SUBDIR_SHOW_AGAIN_DIALOG_SIZE = addStrProp("system-subdir-show-again-dialog-size", "400:400");
    public static BooleanProperty SYSTEM_SUBDIR_SHOW_AGAIN_DIALOG_SHOW = addBoolProp("system-subdir-show-again-dialog-show", true);

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

    public static StringProperty GUI_FILE_HASH = addStrProp("gui-file-hash", HashConst.HASH_MD5);
    public static StringProperty GUI_FILE_HASH_SUFF = addStrProp("gui-file-hash-suff", HashConst.HASH_MD5_SUFFIX);
    public static BooleanProperty CONFIG_COMPARE_WITH_PATH_1 = addBoolProp("config-compare-with-path-1", Boolean.TRUE);
    public static BooleanProperty CONFIG_COMPARE_WITH_PATH_2 = addBoolProp("config-compare-with-path-2", Boolean.TRUE);
    public static IntegerProperty CONFIG_COMPARE_FILE = addIntProp("config-compare-file", ProgConst.COMPARE_PATH_NAME);

    //ProjectData
    public static IntegerProperty compFileSel1 = addIntProp("comp-file-sel-1", 0);
    public static IntegerProperty compFileSel2 = addIntProp("comp-file-sel-2", 0);

    public static StringProperty compFileSrcFile1 = addStrProp("compFileSrcFile1");
    public static StringProperty compFileSrcFile2 = addStrProp("compFileSrcFile2");
    public static ObservableList<String> compFileSrcFileList = addListProp("compFileSrcFileList");


    public static StringProperty compFileHashFile1 = addStrProp("compFileHashFile1");
    public static StringProperty compFileHashFile2 = addStrProp("compFileHashFile2");
    public static ObservableList<String> compFileHashFileList = addListProp("compFileHashFileList");

    // compare dir
    public static StringProperty lastUsedDir1 = addStrProp("lastUsedDir1");
    public static StringProperty lastUsedDir2 = addStrProp("lastUsedDir2");

    public static StringProperty searchDir1 = addStrProp("searchDir1");
    public static StringProperty searchDir2 = addStrProp("searchDir2");
    public static ObservableList<String> searchDirList = addListProp("searchDirList");

    public static StringProperty searchZip1 = addStrProp("searchZip1");
    public static StringProperty searchZip2 = addStrProp("searchZip2");
    public static ObservableList<String> searchZipList = addListProp("searchZipList");

    public static StringProperty searchHashFile1 = addStrProp("searchHashFile1");
    public static StringProperty searchHashFile2 = addStrProp("searchHashFile2");
    public static ObservableList<String> searchHashFileList = addListProp("searchHashFileList");

    public static StringProperty filter1 = addStrProp("filter1");
    public static StringProperty filter2 = addStrProp("filter2");
    public static ObservableList<String> filterList = addListProp("filterList");

    public static StringProperty writeHash1 = addStrProp("writeHash1");
    public static StringProperty writeHash2 = addStrProp("writeHash2");
    public static StringProperty writeFileHash1 = addStrProp("writeFileHash1");
    public static StringProperty writeFileHash2 = addStrProp("writeFileHash2");
    public static ObservableList<String> writeHashList1 = addListProp("writeHashList1");
    public static ObservableList<String> writeHashList2 = addListProp("writeHashList2");
    public static ObservableList<String> writeFileHashList = addListProp("writeFileHashList");

    public static IntegerProperty selTab1 = addIntProp("selTab1", 0);
    public static IntegerProperty selTab2 = addIntProp("selTab2", 0);
    public static BooleanProperty followLink1 = addBoolProp("followLink1", false);
    public static BooleanProperty followLink2 = addBoolProp("followLink2", false);

    private static ProgConfig instance;

    private ProgConfig() {
        super("ProgConfig");
    }

    public static final ProgConfig getInstance() {
        return instance == null ? instance = new ProgConfig() : instance;
    }

    public static void addConfigData(ConfigFile configFile) {
        // Configs der Programmversion, nur damit sie (zur Update-Suche) im Config-File stehen
        ProgConfig.SYSTEM_PROG_VERSION.set(P2ToolsFactory.getProgVersion());
        ProgConfig.SYSTEM_PROG_BUILD_NO.set(P2ToolsFactory.getBuild());
        ProgConfig.SYSTEM_PROG_BUILD_DATE.set(P2ToolsFactory.getCompileDate());

        configFile.addConfigs(ProgConfig.getInstance());
        configFile.addConfigs(ProgColorList.getInstance());
    }
}
