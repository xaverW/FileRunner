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

package de.p2tools.fileRunner.controller;

import de.p2tools.fileRunner.controller.config.ProgConfig;
import de.p2tools.fileRunner.controller.config.ProgConst;
import de.p2tools.fileRunner.controller.config.ProgData;
import de.p2tools.fileRunner.controller.config.ProgInfos;
import de.p2tools.p2Lib.configFile.ConfigFile;
import de.p2tools.p2Lib.configFile.WriteConfigFile;

import java.nio.file.Path;

public class ProgSaveFactory {
    final ProgData progData;

    public ProgSaveFactory() {
        progData = ProgData.getInstance();
    }


    public void save() {
        final Path xmlFilePath = new ProgInfos().getSettingsFile();
        ConfigFile configFile = new ConfigFile(ProgConst.XML_START, xmlFilePath);
        ProgConfig.addConfigData(configFile);

        WriteConfigFile readWriteConfigFile = new WriteConfigFile();
        readWriteConfigFile.addConfigFile(configFile);
        readWriteConfigFile.writeConfigFile();
    }
}
