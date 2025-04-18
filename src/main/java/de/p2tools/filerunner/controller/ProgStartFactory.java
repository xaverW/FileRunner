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
import de.p2tools.p2lib.tools.P2InfoFactory;
import de.p2tools.p2lib.tools.date.P2DateConst;
import de.p2tools.p2lib.tools.duration.P2Duration;
import de.p2tools.p2lib.tools.log.P2Log;
import de.p2tools.p2lib.tools.log.P2LogMessage;
import de.p2tools.p2lib.tools.log.P2Logger;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ProgStartFactory {

    private static final String backupHeader = "Die Einstellungen des Programms sind beschädigt" + P2LibConst.LINE_SEPARATOR +
            "und können nicht geladen werden.";
    private static final String backupText = "Soll versucht werden, mit gesicherten Einstellungen" + P2LibConst.LINE_SEPARATOR
            + "des Programms zu starten?" + P2LibConst.LINE_SEPARATORx2
            + "(ansonsten startet das Programm mit Standardeinstellungen)";

    private ProgStartFactory(ProgData progData) {
    }

    public static void startMsg() {
        if (ProgData.debug) {
            P2Logger.setFileHandler(ProgInfos.getLogDirectory_String());
        }

        ArrayList<String> list = new ArrayList<>();
        list.add("Verzeichnisse:");
        list.add("Programmpfad: " + ProgInfos.getPathJar());
        list.add("Verzeichnis Einstellungen: " + ProgInfos.getSettingsDirectory_String());

        P2LogMessage.startMsg(ProgConst.PROGRAM_NAME, list);
    }

    public static boolean workBeforeGui(ProgData progData) {
        boolean load = ProgLoadFactory.loadProgConfigData();
        clearProjectData();
        return load;
    }

    /**
     * alles was nach der GUI gemacht werden soll z.B.
     * Senderliste beim Programmstart!! laden
     *
     * @param firstProgramStart
     */
    public static void workAfterGui(ProgData progData, boolean firstProgramStart) {
        startMsg();
        setTitle();
        checkProgUpdate(progData);
    }

    public static void setTitle() {
        if (ProgData.debug) {
            ProgData.getInstance().primaryStage.setTitle(ProgConst.PROGRAM_NAME + " " + P2InfoFactory.getProgVersion() + " / DEBUG");
        } else {
            ProgData.getInstance().primaryStage.setTitle(ProgConst.PROGRAM_NAME + " " + P2InfoFactory.getProgVersion());
        }
    }

    private static void clearProjectData() {
        ProgConfig.filter1.set("");
        ProgConfig.filter2.set("");
        ProgConfig.writeHash1.set("");
        ProgConfig.writeHash2.set("");
        ProgConfig.selTab1.set(0);
        ProgConfig.selTab2.set(0);
    }

    private static void checkProgUpdate(ProgData progData) {
        // Prüfen obs ein Programmupdate gibt
        P2Duration.onlyPing("checkProgUpdate");

        /* if (ProgData.debug) {
            // damits bei jedem Start gemacht wird
            P2Log.sysLog("DEBUG: Update-Check");
            runUpdateCheck(progData, true);

        } else */
        if (ProgConfig.SYSTEM_UPDATE_SEARCH_ACT.get() &&
                !updateCheckTodayDone()) {
            // nach Updates suchen
            runUpdateCheck(progData, false);

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
