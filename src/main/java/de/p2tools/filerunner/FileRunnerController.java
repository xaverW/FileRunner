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

package de.p2tools.filerunner;

import de.p2tools.filerunner.controller.ProgQuittFactory;
import de.p2tools.filerunner.controller.SearchProgramUpdate;
import de.p2tools.filerunner.controller.config.Events;
import de.p2tools.filerunner.controller.config.ProgConfig;
import de.p2tools.filerunner.controller.config.ProgData;
import de.p2tools.filerunner.controller.worker.CompareFileListFactory;
import de.p2tools.filerunner.gui.GuiDirRunner;
import de.p2tools.filerunner.gui.GuiFileRunner;
import de.p2tools.filerunner.gui.HelpDialogController;
import de.p2tools.filerunner.gui.StatusBarController;
import de.p2tools.filerunner.gui.configdialog.ConfigDialogController;
import de.p2tools.filerunner.gui.dialog.AboutDialogController;
import de.p2tools.filerunner.icon.ProgIconsFileRunner;
import de.p2tools.p2lib.guitools.pmask.P2MaskerPane;
import de.p2tools.p2lib.p2event.P2Event;
import de.p2tools.p2lib.p2event.P2Listener;
import de.p2tools.p2lib.tools.log.P2Log;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.*;

public class FileRunnerController extends StackPane {

    private final P2MaskerPane maskerPane = new P2MaskerPane();
    BorderPane borderPane = new BorderPane();

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
            // Gui zusammenbauen
            this.setPadding(new Insets(0));
            this.getChildren().addAll(borderPane, maskerPane);
            initMaskerPane();

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

            final MenuItem miHelp = new MenuItem("Hilfe");
            miHelp.setOnAction(e -> new HelpDialogController());

            final MenuItem miSearchUpdate = new MenuItem("Gibt's ein Update?");
            miSearchUpdate.setOnAction(a -> new SearchProgramUpdate(progData, progData.primaryStage).searchNewProgramVersion(true));

            final MenuItem miAbout = new MenuItem("Über dieses Programm");
            miAbout.setOnAction(event -> new AboutDialogController(progData).showDialog());

            final MenuItem miQuitt = new MenuItem("Beenden");
            miQuitt.setOnAction(e -> ProgQuittFactory.quit());

            menuButton.getStyleClass().addAll("btnFunction", "btnFunc-2");
            menuButton.setText("");
            menuButton.setGraphic(ProgIconsFileRunner.FX_ICON_TOOLBAR_MENUE_TOP.getImageView());
            menuButton.getItems().addAll(miConfig, miHelp, miSearchUpdate, miAbout,
                    new SeparatorMenuItem(), miQuitt);
            menuButton.setOnMouseClicked(mouseEvent -> {
                if (mouseEvent.getButton().equals(MouseButton.SECONDARY)) {
                    ProgConfig.SYSTEM_DARK_THEME.setValue(!ProgConfig.SYSTEM_DARK_THEME.getValue());
                }
            });

            menuButton2.getStyleClass().addAll("btnFunction", "btnFunc-2");//damit das tilePane mittig ist!!
            menuButton2.setText("");
            menuButton2.setGraphic(ProgIconsFileRunner.FX_ICON_TOOLBAR_MENUE_TOP.getImageView());
            menuButton2.setVisible(false);

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
            borderPane.setPadding(new Insets(0));
            selPanelDirRunner();
        } catch (Exception ex) {
            P2Log.errorLog(645120321, ex);
        }
    }

    private void initMaskerPane() {
        StackPane.setAlignment(maskerPane, Pos.CENTER);
        maskerPane.setPadding(new Insets(4, 1, 1, 1));
        maskerPane.toFront();

        Button btnStop = maskerPane.getButton();
        btnStop.setGraphic(ProgIconsFileRunner.ICON_BUTTON_STOP.getImageView());
        btnStop.setOnAction(e -> progData.worker.setStop());
        maskerPane.setButtonText("");
        maskerPane.setButtonVisible(true);

        ProgData.getInstance().pEventHandler.addListener(new P2Listener(Events.GENERATE_COMPARE_FILE_LIST) {
            public void pingGui(P2Event event) {
                if (event.getMax() == 0) {
                    if (!progData.worker.createHashIsRunning() ||
                            CompareFileListFactory.isRunningProperty().get()) {
                        //sonst läuft nochmal
                        maskerPane.switchOffMasker();
                    }
                } else {
                    maskerPane.setMaskerVisible();
                }

                int max = (int) event.getMax();
                int progress = (int) event.getAct();
                double prog = 1.0;
                if (max > 0) {
                    prog = 1.0 * progress / max;
                }
                maskerPane.setMaskerProgress(prog, event.getText());
            }
        });
    }

    private void selPanelDirRunner() {
        btnDirRunner.getStyleClass().clear();
        btnFileRunner.getStyleClass().clear();

        btnDirRunner.getStyleClass().add("btnTabTop-sel");
        btnFileRunner.getStyleClass().add("btnTabTop");

        progData.guiDirRunner.toFront();
        progData.guiDirRunner.isShown();
        statusBarController.setStatusbarIndex(StatusBarController.StatusbarIndex.DIR_RUNNER);
    }

    private void selPanelFileRunner() {
        btnDirRunner.getStyleClass().clear();
        btnFileRunner.getStyleClass().clear();

        btnDirRunner.getStyleClass().add("btnTabTop");
        btnFileRunner.getStyleClass().add("btnTabTop-sel");

        progData.guiFileRunner.toFront();
        progData.guiFileRunner.isShown();
        statusBarController.setStatusbarIndex(StatusBarController.StatusbarIndex.NONE);
    }
}
