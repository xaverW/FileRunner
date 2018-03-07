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

package de.p2tools.fileRunner.gui.configDialog;

import de.p2tools.fileRunner.controller.config.ProgData;
import de.p2tools.fileRunner.controller.data.Icons;
import de.p2tools.fileRunner.gui.HelpText;
import de.p2tools.p2Lib.dialog.PAlert;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.layout.*;

public class ConfigPane extends AnchorPane {

    private final ProgData daten;
    private final VBox vBox = new VBox(10);

    public ConfigPane() {
        daten = ProgData.getInstance();

        AnchorPane.setTopAnchor(vBox, 10.0);
        AnchorPane.setRightAnchor(vBox, 10.0);
        AnchorPane.setBottomAnchor(vBox, 10.0);
        AnchorPane.setLeftAnchor(vBox, 10.0);

        getChildren().addAll(vBox);
        makeConfig();
    }

    private void makeConfig() {
        final GridPane gridPane = new GridPane();
        gridPane.setHgap(15);
        gridPane.setVgap(15);
        gridPane.setPadding(new Insets(20, 20, 20, 20));

        final CheckBox tglSearchAbo = new CheckBox("Abos automatisch suchen");
        tglSearchAbo.setMaxWidth(Double.MAX_VALUE);
//        tglSearchAbo.selectedProperty().bindBidirectional(propAbo);
        gridPane.add(tglSearchAbo, 0, 1);
        final Button btnHelpAbo = new Button("");
        btnHelpAbo.setGraphic(new Icons().ICON_BUTTON_HELP);
        btnHelpAbo.setOnAction(a -> new PAlert().showHelpAlert("Filtern", HelpText.FOLLOW_SYMLINK));
        GridPane.setHalignment(btnHelpAbo, HPos.RIGHT);
        gridPane.add(btnHelpAbo, 1, 1);

        final ColumnConstraints ccTxt = new ColumnConstraints();
        ccTxt.setFillWidth(true);
        ccTxt.setMinWidth(Region.USE_COMPUTED_SIZE);
        ccTxt.setHgrow(Priority.ALWAYS);
        gridPane.getColumnConstraints().addAll(new ColumnConstraints(), ccTxt);

        vBox.getChildren().add(gridPane);
    }


}
