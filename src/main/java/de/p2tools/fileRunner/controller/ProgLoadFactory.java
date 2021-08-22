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
import de.p2tools.fileRunner.controller.config.ProgInfos;
import de.p2tools.p2Lib.P2LibConst;
import de.p2tools.p2Lib.configFile.ConfigFile;
import de.p2tools.p2Lib.configFile.ReadConfigFile;
import de.p2tools.p2Lib.tools.duration.PDuration;
import de.p2tools.p2Lib.tools.log.PLog;

import java.nio.file.Path;

public class ProgLoadFactory {
    private static final String backupHeader = "Die Einstellungen des Programms sind beschädigt" + P2LibConst.LINE_SEPARATOR +
            "und können nicht geladen werden.";
    private static final String backupText = "Soll versucht werden, mit gesicherten Einstellungen" + P2LibConst.LINE_SEPARATOR
            + "des Programms zu starten?" + P2LibConst.LINE_SEPARATORx2
            + "(ansonsten startet das Programm mit Standardeinstellungen)";

    private ProgLoadFactory() {
    }

    public static boolean loadProgConfigData() {
        PDuration.onlyPing("ProgStartFactory.loadProgConfigData");
        Path path = new ProgInfos().getSettingsFile();
        PLog.sysLog("Konfig lesen: " + path.toString());

        ConfigFile configFile = new ConfigFile(ProgConst.XML_START, path);
        ProgConfig.addConfigData(configFile);
        ReadConfigFile readWriteConfigFile = new ReadConfigFile();
        readWriteConfigFile.addConfigFile(configFile);

        return readWriteConfigFile.readConfigFile(backupHeader, backupText);
    }
}


