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

package de.p2tools.fileRunner.controller;

import de.p2tools.fileRunner.controller.config.ProgConfig;
import de.p2tools.fileRunner.controller.config.ProgConst;
import de.p2tools.fileRunner.controller.config.ProgData;
import de.p2tools.fileRunner.controller.config.ProgInfos;
import de.p2tools.p2Lib.PInit;
import de.p2tools.p2Lib.configFile.ConfigFile;
import de.p2tools.p2Lib.tools.Log;
import de.p2tools.p2Lib.tools.SysMsg;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;

import static de.p2tools.p2Lib.tools.Log.LILNE;

public class ProgStart {
    ProgData progData;

    public ProgStart(ProgData progData) {
        this.progData = progData;
        startMsg();
    }

    public static void startMsg() {
        Log.versionMsg(ProgConst.PROGRAMMNAME);
        SysMsg.sysMsg("Programmpfad: " + ProgInfos.getPathJar());
        SysMsg.sysMsg("Verzeichnis Einstellungen: " + ProgInfos.getSettingsDirectory_String());
        SysMsg.sysMsg("");
        SysMsg.sysMsg(LILNE);
        SysMsg.sysMsg("");
        SysMsg.sysMsg("");
    }

    public boolean loadConfigData() {
        if (!loadConnfig(new ProgInfos().getXmlFilePath())) {
            // teils geladene Reste entfernen
            initKonfig();
        }
        SysMsg.sysMsg("Progstart: Konfig");

        PInit.initLib(ProgData.debug, ProgConst.PROGRAMMNAME, ProgInfos.getUserAgent());
        return true;
    }

    private void initKonfig() {
        ProgData progData = ProgData.getInstance();
        // ....
    }

    private boolean loadConnfig(Path xmlFilePath) {
        ConfigFile configFile = new ConfigFile(xmlFilePath);
        return configFile.readConfigFile(
                null, //todo
                new ArrayList<>(Arrays.asList(ProgConfig.getConfigsData())));
    }

}
