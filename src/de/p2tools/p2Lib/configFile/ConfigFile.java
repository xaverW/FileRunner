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


package de.p2tools.p2Lib.configFile;

import de.p2tools.fileRunner.controller.config.ProgConst;
import de.p2tools.p2Lib.tools.SysMsg;

import java.nio.file.Path;
import java.util.ArrayList;

public class ConfigFile {
    public static final int MAX_COPY_BACKUPFILE = 5; // Maximum number of backup files to be stored.

    private final Path configFile;
    private final ArrayList<ConfigsData> configsData;
    private final ArrayList<ConfigsDataList> configsDataList;

    private int maxCopyBackupfile = MAX_COPY_BACKUPFILE;


    public ConfigFile(Path configFile) {
        this.configFile = configFile;
        this.configsDataList = new ArrayList<>();
        this.configsData = new ArrayList<>();
    }

//    public void addConfigs(String tag, ArrayList<Config> configs) {
//        ConfigsData configsData = new ConfigsData() {
//            @Override
//            public String getTag() {
//                return tag;
//            }
//
//            @Override
//            public ArrayList<Config> getConfigsArr() {
//                return configs;
//            }
//        };
//        configsDataList.add(configsData);
//    }

    public int getMaxCopyBackupfile() {
        return maxCopyBackupfile;
    }

    public void setMaxCopyBackupfile(int maxCopyBackupfile) {
        this.maxCopyBackupfile = maxCopyBackupfile;
    }

    public void addConfigs(ConfigsDataList configsData) {
        configsDataList.add(configsData);
    }

    public void addConfigs(ConfigsData configsData) {
        this.configsData.add(configsData);
    }

    public boolean writeConfigFile() {
        boolean ret = false;
        new BackupConfigFile(maxCopyBackupfile, configFile).konfigCopy();

        SaveConfigFile saveConfigFile = new SaveConfigFile(ProgConst.XML_START, configFile, configsDataList, configsData);
        saveConfigFile.write();
        return ret;
    }

    public boolean readConfigFile(ArrayList<ConfigsDataList> configsDataList,
                                  ArrayList<ConfigsData> configsData) {
        //todo wenn eins null
        if (new LoadConfigFile(configFile, configsDataList, configsData).readConfiguration()) {
            SysMsg.sysMsg("Config geladen");
            return true;

        } else if (new BackupConfigFile(maxCopyBackupfile, configFile).loadBackup(configsDataList, configsData)) {
            SysMsg.sysMsg("Config-Backup geladen");
            return true;
        }

        return false;
    }
}
