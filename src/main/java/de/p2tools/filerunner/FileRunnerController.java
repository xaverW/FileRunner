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
import de.p2tools.filerunner.controller.config.ProgData;
import de.p2tools.filerunner.controller.config.RunPEvent;
import de.p2tools.filerunner.controller.worker.CompareFileListFactory;
import de.p2tools.filerunner.gui.GuiDirRunner;
import de.p2tools.filerunner.gui.GuiFileRunner;
import de.p2tools.filerunner.gui.HelpDialogController;
import de.p2tools.filerunner.gui.StatusBarController;
import de.p2tools.filerunner.gui.configdialog.ConfigDialogController;
import de.p2tools.filerunner.gui.dialog.AboutDialogController;
import de.p2tools.filerunner.icon.ProgIcons;
import de.p2tools.p2lib.guitools.pmask.PMaskerPane;
import de.p2tools.p2lib.tools.events.PEvent;
import de.p2tools.p2lib.tools.events.PListener;
import de.p2tools.p2lib.tools.log.PLog;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.layout.*;

public class FileRunnerController extends StackPane {

    private final PMaskerPane maskerPane = new PMaskerPane();
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
            menuButton.setGraphic(ProgIcons.Icons.FX_ICON_TOOLBAR_MENUE_TOP.getImageView());
            menuButton.getItems().addAll(miConfig, miHelp, miSearchUpdate, miAbout,
                    new SeparatorMenuItem(), miQuitt);

            menuButton2.getStyleClass().addAll("btnFunction", "btnFunc-2");//damit das tilePane mittig ist!!
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
            borderPane.setTop(hBoxMenueButton);
            borderPane.setCenter(stackPaneCont);
            borderPane.setBottom(statusBarController);
            borderPane.setPadding(new Insets(0));
            selPanelDirRunner();
        } catch (Exception ex) {
            PLog.errorLog(645120321, ex);
        }
    }

    private void initMaskerPane() {
        StackPane.setAlignment(maskerPane, Pos.CENTER);
        maskerPane.setPadding(new Insets(4, 1, 1, 1));
        maskerPane.toFront();

        Button btnStop = maskerPane.getButton();
        btnStop.setGraphic(ProgIcons.Icons.ICON_BUTTON_STOP.getImageView());
        btnStop.setOnAction(e -> progData.worker.setStop());
        maskerPane.setButtonText("");
        maskerPane.setButtonVisible(true);

        ProgData.getInstance().pEventHandler.addListener(new PListener(Events.GENERATE_COMPARE_FILE_LIST) {
            public <T extends PEvent> void pingGui(T runEvent) {
                if (runEvent.getClass().equals(RunPEvent.class)) {
                    RunPEvent runE = (RunPEvent) runEvent;
                    if (runE.nixLos()) {
                        if (!progData.worker.createHashIsRunning() ||
                                CompareFileListFactory.isRunningProperty().get()) {
                            //sonst läuft nochmal
                            maskerPane.setMaskerVisible(false);
                        }
                    } else {
                        maskerPane.setMaskerVisible(true, true);
                    }

                    int max = runE.getMax();
                    int progress = runE.getProgress();
                    double prog = 1.0;
                    if (max > 0) {
                        prog = 1.0 * progress / max;
                    }
                    maskerPane.setMaskerProgress(prog, runE.getText());
                }
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
