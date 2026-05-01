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

import de.p2tools.filerunner.controller.config.Events;
import de.p2tools.filerunner.controller.config.ProgData;
import de.p2tools.filerunner.controller.worker.CompareFileListFactory;
import de.p2tools.filerunner.gui.GuiDirRunner;
import de.p2tools.filerunner.gui.GuiFileRunner;
import de.p2tools.filerunner.gui.StatusBarController;
import de.p2tools.filerunner.gui.picon.PIconFactory;
import de.p2tools.p2lib.guitools.pmask.P2MaskerPane;
import de.p2tools.p2lib.p2event.P2Event;
import de.p2tools.p2lib.p2event.P2Listener;
import de.p2tools.p2lib.tools.log.P2Log;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;

public class FileRunnerController extends StackPane {

    private final P2MaskerPane maskerPane = new P2MaskerPane();
    BorderPane borderPane = new BorderPane();

    Button btnDirRunner = new Button("Ordner vergleichen");
    Button btnFileRunner = new Button("Dateien vergleichen");

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


            HBox hBox = new HBox(20);
            hBox.setPadding(new Insets(10, 10, 0, 10));
            hBox.setAlignment(Pos.BOTTOM_CENTER);
            HBox.setHgrow(hBox, Priority.ALWAYS);
            hBox.getChildren().addAll(btnDirRunner, btnFileRunner);


            HBox hBoxMenuButton = new HBox();
            hBoxMenuButton.setAlignment(Pos.CENTER);
            FileRunnerMenu m = new FileRunnerMenu();
            m.setVisible(false);
            hBoxMenuButton.getChildren().addAll(m, hBox, new FileRunnerMenu());


            btnDirRunner.setOnAction(e -> selPanelDirRunner());
            btnDirRunner.setMaxWidth(Double.MAX_VALUE);

            btnFileRunner.setOnAction(e -> selPanelFileRunner());
            btnFileRunner.setMaxWidth(Double.MAX_VALUE);

            // Panes
            progData.guiDirRunner = new GuiDirRunner();
            progData.guiFileRunner = new GuiFileRunner();
            stackPaneCont.getChildren().addAll(progData.guiDirRunner, progData.guiFileRunner);

            // Statusbar
            statusBarController = new StatusBarController(progData);

            // ProgGUI
            borderPane.setTop(hBoxMenuButton);
            borderPane.setCenter(stackPaneCont);
            stackPaneCont.getStyleClass().add("splitPaneTab");

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
        btnStop.setGraphic(PIconFactory.PICON.BTN_CLEAR.getFontIcon());
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

        btnDirRunner.getStyleClass().setAll("pFuncBtn", "pFuncBtnTitleBar", "pFuncBtnTitleBarSel");
        btnFileRunner.getStyleClass().setAll("pFuncBtn", "pFuncBtnTitleBar");

        progData.guiDirRunner.toFront();
        progData.guiDirRunner.isShown();
        statusBarController.setStatusbarIndex(StatusBarController.StatusbarIndex.DIR_RUNNER);
    }

    private void selPanelFileRunner() {
        btnDirRunner.getStyleClass().clear();
        btnFileRunner.getStyleClass().clear();

        btnDirRunner.getStyleClass().setAll("pFuncBtn", "pFuncBtnTitleBar");
        btnFileRunner.getStyleClass().setAll("pFuncBtn", "pFuncBtnTitleBar", "pFuncBtnTitleBarSel");

        progData.guiFileRunner.toFront();
        progData.guiFileRunner.isShown();
        statusBarController.setStatusbarIndex(StatusBarController.StatusbarIndex.NONE);
    }
}
