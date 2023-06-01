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

package de.p2tools.filerunner.gui.configdialog;

import de.p2tools.filerunner.controller.config.ProgConfig;
import de.p2tools.filerunner.controller.config.ProgData;
import de.p2tools.p2lib.dialogs.accordion.PAccordionPane;
import javafx.scene.control.TitledPane;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.Collection;

public class ControllerConfig extends PAccordionPane {

    private final ProgData progData;
    private final Stage stage;

    private PaneConfig paneConfig;
    private PaneUpdate paneUpdate;

    public ControllerConfig(Stage stage) {
        super(ProgConfig.CONFIG_DIALOG_ACCORDION, ProgConfig.SYSTEM_CONFIG_DIALOG_GENERAL);
        this.stage = stage;
        progData = ProgData.getInstance();

        init();
    }

    public void close() {
        paneConfig.close();
        paneUpdate.close();
        super.close();
    }

    public Collection<TitledPane> createPanes() {
        Collection<TitledPane> result = new ArrayList<>();

        paneConfig = new PaneConfig(stage);
        paneConfig.makeConfig(result);

        paneUpdate = new PaneUpdate(stage);
        paneUpdate.makePane(result);
        return result;
    }
}
