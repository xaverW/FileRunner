/*
 * MTPlayer Copyright (C) 2017 W. Xaver W.Xaver[at]googlemail.com
 * https://www.p2tools.de
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
import de.p2tools.p2Lib.checkForUpdates.SearchProgInfo;
import de.p2tools.p2Lib.tools.ProgramTools;
import de.p2tools.p2Lib.tools.duration.PDuration;
import de.p2tools.p2Lib.tools.log.PLog;
import javafx.application.Platform;
import javafx.stage.Stage;
import org.apache.commons.lang3.time.FastDateFormat;

import java.util.Date;

import static java.lang.Thread.sleep;

public class SearchProgramUpdate {

    private final ProgData progData;
    private final Stage stage;

    public SearchProgramUpdate(Stage stage, ProgData progData) {
        this.stage = stage;
        this.progData = progData;
    }

    /**
     * check if there is a newer program version available
     * and show always the result or error
     *
     * @return
     */
    public boolean checkVersion() {
        return checkVersion(true, true, true);
    }

    public boolean checkVersion(boolean showDailyUpdate) {
        return checkVersion(showDailyUpdate, true, true);
    }

    private boolean checkVersion(boolean showDailyUpdate, boolean showError, boolean showProgramInformation) {
        // prüft auf neue Version,
        ProgConfig.SYSTEM_UPDATE_PROGRAM_VERSION.setValue(ProgramTools.getProgVersionInt());
        ProgConfig.SYSTEM_UPDATE_DATE.setValue(FastDateFormat.getInstance("dd.MM.yyyy").format(new Date()));

        if (showDailyUpdate) {
            return new SearchProgInfo(stage).checkUpdate(ProgConst.WEBSITE_PROG_UPDATE,
                    ProgConfig.SYSTEM_UPDATE_PROGRAM_VERSION.get(),
                    ProgConfig.SYSTEM_UPDATE_INFOS_NR,
                    ProgConfig.SYSTEM_UPDATE_SEARCH_PROG_START,
                    showProgramInformation, showError);
        } else {
            return new SearchProgInfo(stage).checkUpdate(ProgConst.WEBSITE_PROG_UPDATE,
                    ProgConfig.SYSTEM_UPDATE_PROGRAM_VERSION.get(),
                    ProgConfig.SYSTEM_UPDATE_INFOS_NR,
                    //ProgConfig.SYSTEM_UPDATE_SEARCH_PROG_START,
                    showProgramInformation, showError);
        }
    }


    /**
     * check if program update is available and show the
     * the result in the program title
     */
    public void checkProgramUpdate() {
        PDuration.onlyPing("checkProgUpdate");

        if (!ProgConfig.SYSTEM_UPDATE_SEARCH_PROG_START.get() ||
                ProgConfig.SYSTEM_UPDATE_PROGRAM_VERSION.get() == ProgramTools.getProgVersionInt() /*Start mit neuer Version*/
                        && ProgConfig.SYSTEM_UPDATE_DATE.get().equals(FastDateFormat.getInstance("dd.MM.yyyy").format(new Date()))) {

            // will der User nicht --oder-- keine neue Version und heute schon gemacht
            PLog.sysLog("Kein Update-Check");
            return;
        }

        Thread th = new Thread(() -> {
            try {
                if (checkVersion(true, false, false)) {
                    // gab eine neue Version
                    Platform.runLater(() -> ProgStart.setUpdateTitle(progData));
                } else {
                    Platform.runLater(() -> ProgStart.setNoUpdateTitle(progData));
                }

                sleep(10_000);
                Platform.runLater(() -> ProgStart.setOrgTitle(progData));

            } catch (final Exception ex) {
                PLog.errorLog(794612801, ex);
                Platform.runLater(() -> ProgStart.setOrgTitle(progData));
            }
        });
        th.setName("checkProgUpdate");
        th.start();
    }
}
