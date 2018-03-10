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

import de.p2tools.p2Lib.configFile.config.*;
import de.p2tools.p2Lib.configFile.pData.PDataVault;
import de.p2tools.p2Lib.hash.HashConst;
import javafx.beans.property.*;

import java.util.ArrayList;

public class ProgConfig extends PDataVault<ProgConfig> {

    public static final String TAG = "ProgConfig";

    public static ConfigStringProp SYSTEM_PROG_OPEN_DIR;
    public static ConfigStringProp SYSTEM_PROG_OPEN_URL;
    public static ConfigStringProp SYSTEM_PROG_PLAY_FILE;

    // Fenstereinstellungen
    public static ConfigStringProp SYSTEM_GROESSE_GUI;
    public static ConfigStringProp SYSTEM_CONFIG_DIALOG_SIZE;
    public static ConfigBoolProp SYSTEM_STORE_CONFIG;

    // GuiDirRunner
    public static ConfigStringProp GUI_FILERUNNER_TABLE1_WIDTH;
    public static ConfigStringProp GUI_FILERUNNER_TABLE1_SORT;
    public static ConfigStringProp GUI_FILERUNNER_TABLE1_UPDOWN;
    public static ConfigStringProp GUI_FILERUNNER_TABLE1_VIS;
    public static ConfigStringProp GUI_FILERUNNER_TABLE1_ORDER;

    public static ConfigStringProp GUI_FILERUNNER_TABLE2_WIDTH;
    public static ConfigStringProp GUI_FILERUNNER_TABLE2_SORT;
    public static ConfigStringProp GUI_FILERUNNER_TABLE2_UPDOWN;
    public static ConfigStringProp GUI_FILERUNNER_TABLE2_VIS;
    public static ConfigStringProp GUI_FILERUNNER_TABLE2_ORDER;

    public static ConfigStringProp GUI_FILE_FILE1;
    public static ConfigStringProp GUI_FILE_HASH1;
    public static ConfigStringProp GUI_FILE_FILE2;
    public static ConfigStringProp GUI_FILE_HASH2;

    public static ConfigStringProp GUI_FILE_HASH;
    public static ConfigStringProp GUI_FILE_HASH_SUFF;


    private static final ArrayList<Config> arrayList = new ArrayList<>();

    public ProgConfig() {
        SYSTEM_PROG_OPEN_DIR = addStrProp("system-prog-open-dir", "");
        SYSTEM_PROG_OPEN_URL = addStrProp("system-prog-open-uri", "");
        SYSTEM_PROG_PLAY_FILE = addStrProp("system-prog-open-media", "");

        SYSTEM_GROESSE_GUI = addStrProp("system-gui-size", "1000:900");
        SYSTEM_CONFIG_DIALOG_SIZE = addStrProp("system-config-dialog-size", "500:500");
        SYSTEM_STORE_CONFIG = addBoolProp("system-store-config", Boolean.TRUE);


        GUI_FILERUNNER_TABLE1_WIDTH = addStrProp("gui-filerunner-table1-width");
        GUI_FILERUNNER_TABLE1_SORT = addStrProp("gui-filerunner-table1-sort");
        GUI_FILERUNNER_TABLE1_UPDOWN = addStrProp("gui-filerunner-table1-upDown");
        GUI_FILERUNNER_TABLE1_VIS = addStrProp("gui-filerunner-table1-vis");
        GUI_FILERUNNER_TABLE1_ORDER = addStrProp("gui-filerunner-table1-order");

        GUI_FILERUNNER_TABLE2_WIDTH = addStrProp("gui-filerunner-table2-width");
        GUI_FILERUNNER_TABLE2_SORT = addStrProp("gui-filerunner-table2-sort");
        GUI_FILERUNNER_TABLE2_UPDOWN = addStrProp("gui-filerunner-table2-upDown");
        GUI_FILERUNNER_TABLE2_VIS = addStrProp("gui-filerunner-table2-vis");
        GUI_FILERUNNER_TABLE2_ORDER = addStrProp("gui-filerunner-table2-order");

        GUI_FILE_FILE1 = addStrProp("gui-file-file1");
        GUI_FILE_HASH1 = addStrProp("gui-file-hash1");
        GUI_FILE_FILE2 = addStrProp("gui-file-file2");
        GUI_FILE_HASH2 = addStrProp("gui-file-hash2");

        GUI_FILE_HASH = addStrProp("gui-file-hash", HashConst.HASH_MD5);
        GUI_FILE_HASH_SUFF = addStrProp("gui-file-hash-suff", HashConst.HASH_MD5_SUFFIX);
    }

    public String getTag() {
        return TAG;
    }

    public String getComment() {
        return "Programmeinstellungen";
    }

    public ArrayList<Config> getConfigsArr() {
        return arrayList;
    }

    public static synchronized ConfigStringProp addStrProp(String key) {
        StringProperty property = new SimpleStringProperty("");
        ConfigStringProp c = new ConfigStringProp(key, "", property);
        arrayList.add(c);
        return c;
    }

    public static synchronized ConfigStringProp addStrProp(String key, String init) {
        StringProperty property = new SimpleStringProperty(init);
        ConfigStringProp c = new ConfigStringProp(key, init, property);
        arrayList.add(c);
        return c;
    }

    public static synchronized ConfigIntProp addIntProp(String key, int init) {
        IntegerProperty property = new SimpleIntegerProperty(init);
        ConfigIntProp c = new ConfigIntProp(key, init, property);
        arrayList.add(c);
        return c;
    }

    public static synchronized ConfigDoubleProp addDoubleProp(String key, double init) {
        DoubleProperty property = new SimpleDoubleProperty(init);
        ConfigDoubleProp c = new ConfigDoubleProp(key, init, property);
        arrayList.add(c);
        return c;
    }

    public static synchronized ConfigBoolProp addBoolProp(String key, boolean init) {
        BooleanProperty property = new SimpleBooleanProperty(init);
        ConfigBoolProp c = new ConfigBoolProp(key, init, property);
        arrayList.add(c);
        return c;
    }

}
