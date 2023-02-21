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

package de.p2tools.fileRunner.gui.configDialog;

import de.p2tools.fileRunner.controller.config.ProgConfig;
import de.p2tools.fileRunner.controller.config.ProgData;
import de.p2tools.fileRunner.gui.HelpText;
import de.p2tools.p2Lib.P2LibConst;
import de.p2tools.p2Lib.guiTools.PButton;
import de.p2tools.p2Lib.guiTools.PColumnConstraints;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.util.Collection;

public class PaneConfig {

    private final CheckBox chkFollowLink1 = new CheckBox("In Tabelle 1");
    private final CheckBox chkFollowLink2 = new CheckBox("In Tabelle 2");
    private final ProgData progData;
    private final Stage stage;

    public PaneConfig(Stage stage) {
        this.stage = stage;
        progData = ProgData.getInstance();
    }

    public void close() {
        chkFollowLink1.selectedProperty().unbindBidirectional(ProgConfig.followLink1);
        chkFollowLink2.selectedProperty().unbindBidirectional(ProgConfig.followLink2);
    }

    public void makeConfig(Collection<TitledPane> result) {
        final GridPane gridPane = new GridPane();
        gridPane.setHgap(P2LibConst.DIST_GRIDPANE_HGAP);
        gridPane.setVgap(P2LibConst.DIST_GRIDPANE_VGAP);
        gridPane.setPadding(new Insets(P2LibConst.DIST_EDGE));

        TitledPane tpConfig = new TitledPane("Allgemein", gridPane);
        result.add(tpConfig);

        final Label lblFollow = new Label("Symbolische Verknüpfungen auflösen");

        final Button btnHelpFollowLink = PButton.helpButton(stage, "Verknüpfung folgen", HelpText.FOLLOW_SYMLINK);
        GridPane.setHalignment(btnHelpFollowLink, HPos.RIGHT);

        chkFollowLink1.selectedProperty().bindBidirectional(ProgConfig.followLink1);
        chkFollowLink2.selectedProperty().bindBidirectional(ProgConfig.followLink2);

        int row = 0;
        gridPane.add(lblFollow, 0, row, 2, 1);
        gridPane.add(btnHelpFollowLink, 2, row);
        gridPane.add(chkFollowLink1, 0, ++row);
        gridPane.add(chkFollowLink2, 0, ++row);

        gridPane.getColumnConstraints().addAll(PColumnConstraints.getCcPrefSize(),
                PColumnConstraints.getCcComputedSizeAndHgrow());
    }
}
