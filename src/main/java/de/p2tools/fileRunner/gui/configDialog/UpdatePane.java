/*
 * P2tools Copyright (C) 2018 W. Xaver W.Xaver[at]googlemail.com
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

package de.p2tools.fileRunner.gui.configDialog;


import de.p2tools.fileRunner.controller.SearchProgramUpdate;
import de.p2tools.fileRunner.controller.config.ProgConfig;
import de.p2tools.fileRunner.controller.config.ProgConst;
import de.p2tools.fileRunner.controller.config.ProgData;
import de.p2tools.p2Lib.P2LibConst;
import de.p2tools.p2Lib.guiTools.PButton;
import de.p2tools.p2Lib.guiTools.PColumnConstraints;
import de.p2tools.p2Lib.guiTools.POpen;
import de.p2tools.p2Lib.tools.log.PLog;
import javafx.beans.property.BooleanProperty;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class UpdatePane extends Tab {

    private final Stage stage;
    final GridPane gridPane = new GridPane();
    BooleanProperty searchProgStart = ProgConfig.SYSTEM_UPDATE_SEARCH_PROG_START;

    public UpdatePane(Stage stage) {
        this.stage = stage;

        setText("Update");
        setClosable(false);
        setContent(gridPane);
        makeConfig();
    }

    private void makeConfig() {
        gridPane.setPadding(new Insets(20));
        gridPane.setHgap(15);
        gridPane.setVgap(15);

        //einmal am Tag Update suchen
        final CheckBox tglSearch = new CheckBox("einmal am Tag nach einer neuen Programmversion suchen");
        tglSearch.selectedProperty().bindBidirectional(searchProgStart);

        final Button btnHelp = PButton.helpButton(stage, "Programmupdate suchen",
                "Beim Programmstart wird geprüft, ob es eine neue Version des Programms gibt. " +
                        "Ist eine aktualisierte Version vorhanden, dann wird es gemeldet." + P2LibConst.LINE_SEPARATOR +
                        "Das Programm wird aber nicht ungefragt ersetzt.");
        GridPane.setHalignment(btnHelp, HPos.RIGHT);

        //jetzt suchen
        Button btnNow = new Button("Jetzt suchen");
        btnNow.setMaxWidth(Double.MAX_VALUE);
        btnNow.setOnAction(event -> new SearchProgramUpdate(stage, ProgData.getInstance()).searchUpdate());

        // Website
        Hyperlink hyperlink = new Hyperlink(ProgConst.WEBSITE_FILE_RUNNER);
        hyperlink.setOnAction(a -> {
            try {
                POpen.openURL(ProgConst.WEBSITE_FILE_RUNNER);
            } catch (Exception e) {
                PLog.errorLog(932012478, e);
            }
        });

        gridPane.add(tglSearch, 0, 0, 2, 1);
        gridPane.add(btnHelp, 3, 0);
        gridPane.add(btnNow, 0, 1);
        gridPane.add(new Label("Infos auch auf der Website:"), 0, 2);
        gridPane.add(hyperlink, 1, 2);

        gridPane.getColumnConstraints().addAll(PColumnConstraints.getCcPrefSize(),
                PColumnConstraints.getCcComputedSizeAndHgrow());
    }
}
