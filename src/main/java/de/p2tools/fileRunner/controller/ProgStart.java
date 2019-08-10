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

import de.p2tools.fileRunner.controller.config.*;
import de.p2tools.p2Lib.PConst;
import de.p2tools.p2Lib.PInit;
import de.p2tools.p2Lib.configFile.ConfigFile;
import de.p2tools.p2Lib.configFile.ReadWriteConfigFile;
import de.p2tools.p2Lib.tools.log.LogMessage;
import de.p2tools.p2Lib.tools.log.PLog;
import de.p2tools.p2Lib.tools.log.PLogger;

import java.nio.file.Path;
import java.util.ArrayList;

public class ProgStart {
    private static final String backupHeader = "Die Einstellungen des Programms sind beschädigt" + PConst.LINE_SEPARATOR +
            "und können nicht geladen werden.";
    private static final String backupText = "Soll versucht werden, mit gesicherten Einstellungen" + PConst.LINE_SEPARATOR
            + "des Programms zu starten?" + PConst.LINE_SEPARATORx2
            + "(ansonsten startet das Programm mit Standardeinstellungen)";

    ProgData progData;

    public ProgStart(ProgData progData) {
        this.progData = progData;
        startMsg();
    }

    public static void startMsg() {
        if (ProgData.debug) {
            PLogger.setFileHandler(ProgInfos.getLogDirectory_String());
        }

        ArrayList<String> list = new ArrayList<>();
        list.add("Verzeichnisse:");
        list.add("Programmpfad: " + ProgInfos.getPathJar());
        list.add("Verzeichnis Einstellungen: " + ProgInfos.getSettingsDirectory_String());

        LogMessage.startMsg(ProgConst.PROGRAMMNAME, list);
    }

    public boolean loadConfigData() {
        boolean load = loadConnfig(new ProgInfos().getSettingsFile());

        if (!load) {
            // teils geladene Reste entfernen
            initKonfig();
        }

        PLog.sysLog("Progstart: Konfig");

        PInit.initLib(ProgData.getInstance().primaryStage, ProgConst.PROGRAMMNAME,
                ProgConst.CSS_FILE, ProgInfos.getUserAgent(),
                ProgData.debug, ProgData.duration);


        if (!ProgConfig.SYSTEM_STORE_CONFIG.get()) {
            progData.projectData.clearProjectData();
        }

        return true;
    }

    private void initKonfig() {
        ProgData progData = ProgData.getInstance();
        // ....
    }

    private boolean loadConnfig(Path xmlFilePath) {
//        ConfigFile configFile = new ConfigFile(ProgConst.XML_START, xmlFilePath);
//        return configFile.readConfigFile(
//                null,
//                new ArrayList<>(Arrays.asList(ProgConfig.getInstance(),
//                        ProgColorList.getConfigsData(),
//                        progData.projectData)));


        ConfigFile configFile = new ConfigFile(ProgConst.XML_START, xmlFilePath);
        configFile.addConfigs(ProgConfig.getInstance());
        configFile.addConfigs(ProgColorList.getConfigsData());
        configFile.addConfigs(progData.projectData);

        PLog.sysLog("Konfig lesen: " + xmlFilePath.toString());

        ReadWriteConfigFile readWriteConfigFile = new ReadWriteConfigFile();
        readWriteConfigFile.addConfigFile(configFile);

        boolean ret = readWriteConfigFile.readConfigFile(backupHeader, backupText);

        return ret;

    }

}
