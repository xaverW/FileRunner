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

package de.p2tools.filerunner.controller;

import de.p2tools.filerunner.controller.config.ProgConfig;
import de.p2tools.filerunner.controller.config.ProgConst;
import de.p2tools.filerunner.controller.config.ProgData;
import de.p2tools.filerunner.controller.config.ProgInfos;
import de.p2tools.p2lib.P2LibConst;
import de.p2tools.p2lib.P2LibInit;
import de.p2tools.p2lib.configfile.ConfigFile;
import de.p2tools.p2lib.configfile.ConfigReadFile;
import de.p2tools.p2lib.css.P2CssFactory;
import de.p2tools.p2lib.tools.P2ToolsRaspberry;
import de.p2tools.p2lib.tools.date.P2DateConst;
import de.p2tools.p2lib.tools.duration.P2Duration;
import de.p2tools.p2lib.tools.log.P2Log;
import de.p2tools.p2lib.tools.log.P2Logger;
import javafx.beans.property.SimpleObjectProperty;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ProgStartBeforeGui {

    private static final String backupHeader = "Die Einstellungen des Programms sind beschädigt" + P2LibConst.LINE_SEPARATOR +
            "und können nicht geladen werden.";
    private static final String backupText = "Soll versucht werden, mit gesicherten Einstellungen" + P2LibConst.LINE_SEPARATOR
            + "des Programms zu starten?" + P2LibConst.LINE_SEPARATORx2
            + "(ansonsten startet das Programm mit Standardeinstellungen)";

    private ProgStartBeforeGui() {
    }

    public static void workBeforeGui() {
        ProgData.raspberry = P2ToolsRaspberry.isRaspberry();
        boolean load = loadAll();
        initLib();

        if (!load) {
            // dann ist der erste Start
            P2Duration.onlyPing("Erster Start");
        }
    }

    private static void initLib() {
        P2LibInit.initLib(ProgData.getInstance().primaryStage, ProgConst.PROGRAM_NAME, "",
                ProgConfig.SYSTEM_THEME_CHANGED,
                ProgConfig.SYSTEM_DARK_THEME,
                ProgConfig.SYSTEM_GUI_THEME_1,
                ProgConfig.SYSTEM_ICON_COLOR,
                ProgConfig.SYSTEM_CSS_ADDER,

                new String[]{
                        "de/p2tools/filerunner/css/mtfx.css",
                        "de/p2tools/filerunner/css/pFuncBtn.css",
                        "de/p2tools/filerunner/css/pFuncMenu.css",
                        "de/p2tools/filerunner/css/pFuncTitleBar.css",
                        "de/p2tools/filerunner/css/pFuncTable.css",
                        "de/p2tools/filerunner/css/pFuncToolBar.css",
                        "de/p2tools/filerunner/css/pFuncTips.css",
                        "de/p2tools/filerunner/css/pFuncStartDialog.css"
                },

                new String[]{
                        "de/p2tools/filerunner/css/mtfx-dark.css"
                },

                new SimpleObjectProperty<>(P2CssFactory.CSS.CSS_1),
                ProgConfig.SYSTEM_FONT_SIZE,

                null,
                "", "",
                ProgData.debug, ProgData.duration);
    }

    /**
     * Config beim  Programmstart laden
     */
    private static boolean loadAll() {
        ArrayList<String> logList = new ArrayList<>();
        boolean ret = load(logList);

        if (ProgConfig.SYSTEM_LOG_ON.getValue()) {
            // dann für den evtl. geänderten LogPfad
            P2Logger.setFileHandler(ProgInfos.getLogDirectory_String());
        }
        P2Log.sysLog(logList);

        if (!ret) {
            P2Log.sysLog("Weder Konfig noch Backup konnte geladen werden!");
            // teils geladene Reste entfernen
            clearTheConfigs();
        }

        return ret;
    }

    private static boolean load(ArrayList<String> logList) {
        final Path xmlFilePath = ProgInfos.getSettingsFile();
        P2Duration.onlyPing("ProgStartFactory.loadProgConfigData");
        try {
            if (!Files.exists(xmlFilePath)) {
                //dann gibts das Konfig-File gar nicht
                logList.add("Konfig existiert nicht!");
                return false;
            }

            logList.add("Programmstart und ProgConfig laden von: " + xmlFilePath);
            ConfigFile configFile = new ConfigFile(xmlFilePath.toString(), true) {
                @Override
                public void clearConfigFile() {
                    clearTheConfigs();
                }
            };

            // Config laden
            ProgConfig.addConfigData(configFile);
            boolean ok = ConfigReadFile.readConfig(configFile);

            if (ok) {
                checkProgUpdate(); // falls es ein Programmupdate gab, Configs anpassen
                logList.add("Konfig wurde geladen!");
                return true;

            } else {
                // dann hat das Laden nicht geklappt
                logList.add("Konfig konnte nicht geladen werden!");
                return false;
            }
        } catch (final Exception ex) {
            logList.add(ex.getLocalizedMessage());
        }
        return false;
    }

    private static void clearTheConfigs() {
        ProgConfig.filter1.set("");
        ProgConfig.filter2.set("");
        ProgConfig.writeHash1.set("");
        ProgConfig.writeHash2.set("");
        ProgConfig.selTab1.set(0);
        ProgConfig.selTab2.set(0);
    }

    private static void checkProgUpdate() {
        // Prüfen obs ein Programmupdate gibt
        P2Duration.onlyPing("checkProgUpdate");

        if (ProgConfig.SYSTEM_UPDATE_SEARCH_ACT.get() &&
                !updateCheckTodayDone()) {
            // nach Updates suchen
            runUpdateCheck(ProgData.getInstance(), false);

        } else {
            // will der User nicht --oder-- wurde heute schon gemacht
            List<String> list = new ArrayList<>(5);
            list.add("Kein Update-Check:");
            if (!ProgConfig.SYSTEM_UPDATE_SEARCH_ACT.get()) {
                list.add("  der User will nicht");
            }
            if (updateCheckTodayDone()) {
                list.add("  heute schon gemacht");
            }
            P2Log.sysLog(list);
        }
    }

    private static boolean updateCheckTodayDone() {
        return ProgConfig.SYSTEM_UPDATE_DATE.get().equals(P2DateConst.F_FORMAT_yyyy_MM_dd.format(new Date()));
    }

    private static void runUpdateCheck(ProgData progData, boolean showAlways) {
        //prüft auf neue Version, ProgVersion und auch (wenn gewünscht) BETA-Version, ..
        ProgConfig.SYSTEM_UPDATE_DATE.setValue(P2DateConst.F_FORMAT_yyyy_MM_dd.format(new Date()));
        new SearchProgramUpdate(progData).searchNewProgramVersion(showAlways);
    }
}