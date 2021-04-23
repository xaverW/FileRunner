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
import de.p2tools.p2Lib.checkForUpdates.SearchProgUpdate;
import de.p2tools.p2Lib.checkForUpdates.UpdateSearchData;
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
    public boolean searchUpdate() {
        return checkVersion(true);
    }

    /**
     * check if program update is available and show the
     * the result in the program title
     */
    public void searchUpdateProgStart() {
        PDuration.onlyPing("checkProgUpdate");
        if (!ProgConfig.SYSTEM_UPDATE_SEARCH_PROG_START.get()) {
            // will der User nicht --oder-- keine neue Version und heute schon gemacht
            PLog.sysLog("Kein Update-Check");
            return;
        }

        try {
            if (checkVersion(false)) {
                // gab eine neue Version
                Platform.runLater(() -> ProgStart.setUpdateTitle(progData));
            } else {
                Platform.runLater(() -> ProgStart.setNoUpdateTitle(progData));
            }
            sleep(10_000);
        } catch (final Exception ex) {
            PLog.errorLog(794612801, ex);
        }
        Platform.runLater(() -> ProgStart.setOrgTitle(progData));
    }

    private boolean checkVersion(boolean showProgramInformation) {
        // prüft auf neue Version,
//        ProgConfig.SYSTEM_UPDATE_VERSION_SHOWN.setValue(ProgramTools.getProgVersionInt());
        ProgConfig.SYSTEM_UPDATE_SEARCH_DATE.setValue(FastDateFormat.getInstance("dd.MM.yyyy").format(new Date()));

        UpdateSearchData updateSearchData = new UpdateSearchData(ProgConst.WEBSITE_PROG_UPDATE,
                ProgramTools.getProgVersionInt(),
                ProgramTools.getBuildInt(),
                ProgConfig.SYSTEM_UPDATE_VERSION_SHOWN,
                null,
                ProgConfig.SYSTEM_UPDATE_INFOS_NR_SHOWN,
                ProgConfig.SYSTEM_UPDATE_SEARCH_PROG_START);

        boolean ret = new SearchProgUpdate(stage).checkAllUpdates(updateSearchData, null, showProgramInformation);
        return ret;
    }

}
