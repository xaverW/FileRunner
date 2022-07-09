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

package de.p2tools.fileRunner.gui.dialog;

import de.p2tools.fileRunner.controller.SearchProgramUpdate;
import de.p2tools.fileRunner.controller.config.ProgConfig;
import de.p2tools.fileRunner.controller.config.ProgConst;
import de.p2tools.fileRunner.controller.config.ProgData;
import de.p2tools.fileRunner.controller.config.ProgInfos;
import de.p2tools.p2Lib.dialogs.AboutDialog;
import de.p2tools.p2Lib.tools.file.PFileUtils;

public class AboutDialogController extends AboutDialog {

    public AboutDialogController(ProgData progData) {
        super(progData.primaryStage, ProgConst.PROGRAM_NAME, ProgConst.URL_WEBSITE, ProgConst.URL_WEBSITE_HELP,
                PFileUtils.addsPath(ProgConst.P2_ICON_PATH, ProgConst.P2_ICON), ProgConfig.SYSTEM_PROG_OPEN_URL,
                ProgConfig.SYSTEM_DARK_THEME.getValue(),
                new String[]{"Einstellungen:"},
                new String[]{ProgInfos.getSettingsFile().toAbsolutePath().toString()},
                false);
    }

    @Override
    public void runCheckButton() {
        new SearchProgramUpdate(ProgData.getInstance(), this.getStage()).searchNewProgramVersion(true);
    }
}
