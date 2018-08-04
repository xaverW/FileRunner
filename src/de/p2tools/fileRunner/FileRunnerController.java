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

import de.p2tools.fileRunner.controller.ProgQuitt;
import de.p2tools.fileRunner.controller.config.ProgConfig;
import de.p2tools.fileRunner.controller.config.ProgConst;
import de.p2tools.fileRunner.controller.config.ProgData;
import de.p2tools.fileRunner.controller.data.Icons;
import de.p2tools.fileRunner.gui.GuiDirRunner;
import de.p2tools.fileRunner.gui.GuiFileRunner;
import de.p2tools.fileRunner.gui.StatusBarController;
import de.p2tools.fileRunner.gui.configDialog.ConfigDialogController;
import de.p2tools.fileRunner.gui.dialog.AboutDialogController;
import de.p2tools.p2Lib.PConst;
import de.p2tools.p2Lib.checkForUpdates.SearchProgInfo;
import de.p2tools.p2Lib.tools.Functions;
import de.p2tools.p2Lib.tools.log.PLog;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.layout.*;

public class FileRunnerController extends StackPane {

    Button btnDirRunner = new Button("Ordner" + PConst.LINE_SEPARATOR + "vergleichen");
    Button btnFileRunner = new Button("Dateien" + PConst.LINE_SEPARATOR + "vergleichen");

    MenuButton menuButton = new MenuButton("");

    BorderPane borderPane = new BorderPane();
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
            hBoxMenueButton.getChildren().addAll(tilePane, menuButton);

            btnDirRunner.setOnAction(e -> selPanelDirRunner());
            btnDirRunner.setMaxWidth(Double.MAX_VALUE);

            btnFileRunner.setOnAction(e -> selPanelFileRunner());
            btnFileRunner.setMaxWidth(Double.MAX_VALUE);


            // Menü
            final MenuItem miConfig = new MenuItem("Einstellungen");
            miConfig.setOnAction(e -> new ConfigDialogController());

            final MenuItem miQuitt = new MenuItem("Beenden");
            miQuitt.setOnAction(e -> new ProgQuitt().beenden(true));

            final MenuItem miUpdate = new MenuItem("Gibt es ein Update?");
            miUpdate.setOnAction(event -> new SearchProgInfo().checkUpdate(ProgConst.WEBSITE_PROG_UPDATE,
                    Functions.getProgVersionInt(),
                    ProgConfig.SYSTEM_INFOS_NR, true, true));

            final MenuItem miAbout = new MenuItem("Über dieses Programm");
            miAbout.setOnAction(event -> new AboutDialogController(progData));

            menuButton.getStyleClass().add("btnFunction");
            menuButton.setText("");
            menuButton.setGraphic(new Icons().FX_ICON_TOOLBAR_MENUE_TOP);
            menuButton.getItems().addAll(miConfig,
                    new SeparatorMenuItem(), miAbout, miUpdate,
                    new SeparatorMenuItem(), miQuitt);


            // Panes
            progData.guiDirRunner = new GuiDirRunner();
            progData.guiFileRunner = new GuiFileRunner();
            stackPaneCont.getChildren().addAll(progData.guiDirRunner, progData.guiFileRunner);

            // Statusbar
            statusBarController = new StatusBarController(progData);

            // ProgGUI
            borderPane.setTop(hBoxMenueButton);
            borderPane.setCenter(stackPaneCont);
            borderPane.setBottom(statusBarController);

            this.setPadding(new Insets(0));
            this.getChildren().addAll(borderPane);

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


