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
import de.p2tools.p2Lib.dialogs.dialog.PDialogExtra;
import de.p2tools.p2Lib.tools.log.PLog;
import javafx.beans.property.IntegerProperty;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;


public class ConfigDialogController extends PDialogExtra {

    private TabPane tabPane = new TabPane();
    private Button btnOk = new Button("Ok");

    private final ProgData progData;
    IntegerProperty propSelectedTab = ProgConfig.SYSTEM_CONFIG_DIALOG_TAB;

    private ControllerConfig controllerConfig;
    private ControllerProg controllerProg;
    private ControllerColor controllerColor;

    public ConfigDialogController() {
        super(ProgData.getInstance().primaryStage, ProgConfig.SYSTEM_CONFIG_DIALOG_SIZE, "Einstellungen",
                true, false, DECO.NO_BORDER);
        this.progData = ProgData.getInstance();

        init(true);
    }

    @Override
    public void make() {
        VBox.setVgrow(tabPane, Priority.ALWAYS);
        getVBoxCont().getChildren().add(tabPane);
        getVBoxCont().setPadding(new Insets(0));
        addOkButton(btnOk);
        btnOk.setOnAction(a -> close());
        ProgConfig.SYSTEM_DARK_THEME.addListener((u, o, n) -> {
            updateCss();
        });
        initPanel();
    }

    public void close() {
        controllerConfig.close();
        controllerProg.close();
        controllerColor.close();
        super.close();
    }


    private void initPanel() {
        try {
            controllerConfig = new ControllerConfig(getStage());
            Tab tab = new Tab("Allgemein");
            tab.setClosable(false);
            tab.setContent(controllerConfig);
            tabPane.getTabs().add(tab);

            controllerProg = new ControllerProg(getStage());
            tab = new Tab("Programme");
            tab.setClosable(false);
            tab.setContent(controllerProg);
            tabPane.getTabs().add(tab);

            controllerColor = new ControllerColor(getStage());
            tab = new Tab("Farben");
            tab.setClosable(false);
            tab.setContent(controllerColor);
            tabPane.getTabs().add(tab);

            tabPane.getSelectionModel().select(propSelectedTab.get());
            tabPane.getSelectionModel().selectedIndexProperty().addListener((observable, oldValue, newValue) -> {
                // readOnlyBinding!!
                propSelectedTab.setValue(newValue);
            });
        } catch (final Exception ex) {
            PLog.errorLog(874102058, ex);
        }
    }
}
