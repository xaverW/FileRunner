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

package de.p2tools.fileRunner;

import de.p2tools.fileRunner.controller.ProgQuittFactory;
import de.p2tools.fileRunner.controller.SearchProgramUpdate;
import de.p2tools.fileRunner.controller.config.ProgData;
import de.p2tools.fileRunner.gui.GuiDirRunner;
import de.p2tools.fileRunner.gui.GuiFileRunner;
import de.p2tools.fileRunner.gui.StatusBarController;
import de.p2tools.fileRunner.gui.configDialog.ConfigDialogController;
import de.p2tools.fileRunner.gui.dialog.AboutDialogController;
import de.p2tools.fileRunner.icon.ProgIcons;
import de.p2tools.p2Lib.tools.log.PLog;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.layout.*;

public class FileRunnerController extends BorderPane {

    Button btnDirRunner = new Button("Ordner " + "vergleichen");
    Button btnFileRunner = new Button("Dateien " + "vergleichen");

    MenuButton menuButton = new MenuButton("");
    MenuButton menuButton2 = new MenuButton("");

    StackPane stackPaneCont = new StackPane();
    private StatusBarController statusBarController;

    private final ProgData progData;

    public FileRunnerController() {
        progData = ProgData.getInstance();
        init();
    }

    private void init() {
        try {
            // Top-Menü
            HBox hBoxMenueButton = new HBox();
            hBoxMenueButton.setPadding(new Insets(10));
            hBoxMenueButton.setSpacing(20);
            hBoxMenueButton.setAlignment(Pos.CENTER);
            HBox.setHgrow(hBoxMenueButton, Priority.ALWAYS);

            TilePane tilePane = new TilePane();
            tilePane.setHgap(20);
            tilePane.setAlignment(Pos.CENTER);
            HBox.setHgrow(tilePane, Priority.ALWAYS);

            tilePane.getChildren().addAll(btnDirRunner, btnFileRunner);
            hBoxMenueButton.getChildren().addAll(menuButton2, tilePane, menuButton);

            btnDirRunner.setOnAction(e -> selPanelDirRunner());
            btnDirRunner.setMaxWidth(Double.MAX_VALUE);

            btnFileRunner.setOnAction(e -> selPanelFileRunner());
            btnFileRunner.setMaxWidth(Double.MAX_VALUE);


            // Menü
            final MenuItem miConfig = new MenuItem("Einstellungen");
            miConfig.setOnAction(e -> new ConfigDialogController());

            final MenuItem miQuitt = new MenuItem("Beenden");
            miQuitt.setOnAction(e -> ProgQuittFactory.quit());

            final MenuItem miAbout = new MenuItem("Über dieses Programm");
            miAbout.setOnAction(event -> new AboutDialogController(progData).showDialog());

            final MenuItem miSearchUpdate = new MenuItem("Gibts ein Update?");
            miSearchUpdate.setOnAction(a -> new SearchProgramUpdate(progData, progData.primaryStage).searchNewProgramVersion(true));

            menuButton.getStyleClass().add("btnFunctionWide");
            menuButton.setText("");
            menuButton.setGraphic(ProgIcons.Icons.FX_ICON_TOOLBAR_MENUE_TOP.getImageView());
            menuButton.getItems().addAll(miConfig, miSearchUpdate, miAbout,
                    new SeparatorMenuItem(), miQuitt);

            menuButton2.getStyleClass().add("btnFunctionWide");
            menuButton2.setText("");
            menuButton2.setGraphic(ProgIcons.Icons.FX_ICON_TOOLBAR_MENUE_TOP.getImageView());
            menuButton2.setVisible(false);

            // Panes
            progData.guiDirRunner = new GuiDirRunner();
            progData.guiFileRunner = new GuiFileRunner();
            stackPaneCont.getChildren().addAll(progData.guiDirRunner, progData.guiFileRunner);

            // Statusbar
            statusBarController = new StatusBarController(progData);

            // ProgGUI
            this.setTop(hBoxMenueButton);
            this.setCenter(stackPaneCont);
            this.setBottom(statusBarController);
            this.setPadding(new Insets(0));
            selPanelDirRunner();
        } catch (Exception ex) {
            PLog.errorLog(645120321, ex);
        }
    }

    private void selPanelDirRunner() {
        btnDirRunner.getStyleClass().clear();
        btnFileRunner.getStyleClass().clear();

        btnDirRunner.getStyleClass().add("btnTab-sel");
        btnFileRunner.getStyleClass().add("btnTab");

        progData.guiDirRunner.toFront();
        progData.guiDirRunner.isShown();
        statusBarController.setStatusbarIndex(StatusBarController.StatusbarIndex.DIR_RUNNER);
    }

    private void selPanelFileRunner() {
        btnDirRunner.getStyleClass().clear();
        btnFileRunner.getStyleClass().clear();

        btnDirRunner.getStyleClass().add("btnTab");
        btnFileRunner.getStyleClass().add("btnTab-sel");

        progData.guiFileRunner.toFront();
        progData.guiFileRunner.isShown();
        statusBarController.setStatusbarIndex(StatusBarController.StatusbarIndex.NONE);
    }
}


