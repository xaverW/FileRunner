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
import de.p2tools.p2Lib.dialog.PDialog;
import de.p2tools.p2Lib.tools.log.PLog;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;


public class ConfigDialogController extends PDialog {

    private TabPane tabPane = new TabPane();
    private Button btnOk = new Button("Ok");

    private final ProgData daten;
    private Stage stage;

    public ConfigDialogController() {
        super(ProgData.getInstance().primaryStage, ProgConfig.SYSTEM_CONFIG_DIALOG_SIZE, "Einstellungen",
                true, true);
        this.daten = ProgData.getInstance();

        VBox vBox = new VBox();
        vBox.setPadding(new Insets(10));
        vBox.setSpacing(10);

        vBox.getChildren().add(tabPane);
        VBox.setVgrow(tabPane, Priority.ALWAYS);

        HBox hBox = new HBox();
        hBox.setAlignment(Pos.CENTER_RIGHT);
        hBox.getChildren().add(btnOk);
        vBox.getChildren().add(hBox);

        init(vBox, true);
    }

    @Override
    public void make() {
        this.stage = getStage();
        btnOk.setOnAction(a -> close());
        initPanel();
    }

    public void close() {
        super.close();
    }


    private void initPanel() {
        try {
            AnchorPane configPane = new GeneralPane(stage);
            Tab tab = new Tab("Allgemein");
            tab.setClosable(false);
            tab.setContent(configPane);
            tabPane.getTabs().add(tab);

            AnchorPane updatePane = new UpdatePane(getStage());
            tab = new Tab("Update");
            tab.setClosable(false);
            tab.setContent(updatePane);
            tabPane.getTabs().add(tab);

            AnchorPane colorPane = new ColorPane(stage);
            tab = new Tab("Farben");
            tab.setClosable(false);
            tab.setContent(colorPane);
            tabPane.getTabs().add(tab);

        } catch (final Exception ex) {
            PLog.errorLog(874102058, ex);
        }
    }

}
