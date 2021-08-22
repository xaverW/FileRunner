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
import de.p2tools.p2Lib.dialogs.accordion.PAccordionPane;
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

import java.util.ArrayList;
import java.util.Collection;

public class GeneralPane extends PAccordionPane {

    private final ProgData progData;
    private final Stage stage;
    private UpdatePane updatePane;

    public GeneralPane(Stage stage) {
        super(stage, ProgConfig.CONFIG_DIALOG_ACCORDION, ProgConfig.SYSTEM_CONFIG_DIALOG_CONFIG);
        this.stage = stage;
        progData = ProgData.getInstance();

        init();
    }

    public void close() {
        super.close();
        updatePane.close();
    }

    public Collection<TitledPane> createPanes() {
        Collection<TitledPane> result = new ArrayList<>();
        makeConfig(result);
        updatePane = new UpdatePane(stage);
        updatePane.makePane(result);
        return result;
    }

    private void makeConfig(Collection<TitledPane> result) {
        final GridPane gridPane = new GridPane();
        gridPane.setPadding(new Insets(20));
        gridPane.setHgap(15);
        gridPane.setVgap(15);

        TitledPane tpConfig = new TitledPane("Allgemein", gridPane);
        result.add(tpConfig);

        final CheckBox chkFollowLink1 = new CheckBox("In Tabelle 1");
        final CheckBox chkFollowLink2 = new CheckBox("In Tabelle 2");
        final Label lblFollow = new Label("Symbolische Verknüpfungen auflösen");

        final Button btnHelpFollowLink = PButton.helpButton(stage, "Verknüpfung folgen", HelpText.FOLLOW_SYMLINK);
        GridPane.setHalignment(btnHelpFollowLink, HPos.RIGHT);

        chkFollowLink1.selectedProperty().bindBidirectional(progData.projectData.followLink1Property());
        chkFollowLink2.selectedProperty().bindBidirectional(progData.projectData.followLink2Property());

        int row = 0;
        gridPane.add(lblFollow, 0, row, 2, 1);
        gridPane.add(btnHelpFollowLink, 2, row);
        gridPane.add(chkFollowLink1, 0, ++row);
        gridPane.add(chkFollowLink2, 0, ++row);

        gridPane.getColumnConstraints().addAll(PColumnConstraints.getCcPrefSize(),
                PColumnConstraints.getCcComputedSizeAndHgrow());
    }
}
