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
import de.p2tools.fileRunner.controller.config.ProgData;
import de.p2tools.fileRunner.controller.data.Icons;
import de.p2tools.fileRunner.gui.GuiDirRunner;
import de.p2tools.fileRunner.gui.GuiFileRunner;
import de.p2tools.fileRunner.gui.StatusBarController;
import de.p2tools.fileRunner.gui.dialog.AboutDialogController;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.*;

public class FileRunnerController extends StackPane {

    Button btnDirRunner = new Button("Ordner");
    Button btnFileRunner = new Button("Dateien");
    Button btnPrev = new Button("");
    Button btnNext = new Button("");

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
            final MenuItem miQuitt = new MenuItem("Beenden");
            miQuitt.setOnAction(e -> new ProgQuitt().beenden(true));

            final MenuItem miAbout = new MenuItem("Über dieses Programm");
            miAbout.setOnAction(event -> new AboutDialogController(progData));

            menuButton.getStyleClass().add("btnFunction");
            menuButton.setText("");
            menuButton.setGraphic(new Icons().FX_ICON_TOOLBAR_MENUE_TOP);
            menuButton.getItems().addAll(miAbout, miQuitt);


            // Panes
            progData.guiDirRunner = new GuiDirRunner();
            progData.guiFileRunner = new GuiFileRunner();

            stackPaneCont.getChildren().addAll(progData.guiDirRunner, progData.guiFileRunner);

            // Statusbar
            statusBarController = new StatusBarController(progData);


            // Button NEXT-PREV
            HBox hBoxPrev = new HBox();
            hBoxPrev.getChildren().addAll(btnPrev);
            hBoxPrev.setAlignment(Pos.CENTER);
            btnPrev.setMaxHeight(Double.MAX_VALUE);
            btnPrev.setGraphic(new Icons().ICON_BUTTON_GUI_PREV);
            btnPrev.setOnAction(a -> setPrev());
            HBox.setHgrow(btnPrev, Priority.ALWAYS);

            HBox hBoxNext = new HBox();
            hBoxNext.getChildren().addAll(btnNext);
            hBoxNext.setAlignment(Pos.CENTER);
            btnNext.setMaxHeight(Double.MAX_VALUE);
            btnNext.setGraphic(new Icons().ICON_BUTTON_GUI_NEXT);
            btnNext.setOnAction(a -> setNext());
            HBox.setHgrow(btnNext, Priority.ALWAYS);

            // ProgGUI
            borderPane.setTop(hBoxMenueButton);
            borderPane.setCenter(stackPaneCont);
            borderPane.setBottom(statusBarController);
//            borderPane.setLeft(hBoxPrev);
//            borderPane.setRight(hBoxNext);

            this.setPadding(new Insets(0));
            this.getChildren().addAll(borderPane);

            selPanelDirRunner();

        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

    private void setPrev() {
        Node front = stackPaneCont.getChildren().get(stackPaneCont.getChildren().size() - 1);
        if (front.equals(progData.guiDirRunner)) {

        } else if (front.equals(progData.guiFileRunner)) {
            selPanelDirRunner();
        }
    }

    private void setNext() {
        Node front = stackPaneCont.getChildren().get(stackPaneCont.getChildren().size() - 1);
        if (front.equals(progData.guiDirRunner)) {
            selPanelFileRunner();
        }
    }

    private void selPanelDirRunner() {
        btnPrev.setDisable(true);
        btnNext.setDisable(false);

        btnDirRunner.getStyleClass().clear();
        btnFileRunner.getStyleClass().clear();

        btnDirRunner.getStyleClass().add("btnTab-sel");
        btnFileRunner.getStyleClass().add("btnTab");

        progData.guiDirRunner.toFront();
        progData.guiDirRunner.isShown();
        statusBarController.setStatusbarIndex(StatusBarController.StatusbarIndex.DIR_RUNNER);
    }

    private void selPanelFileRunner() {
        btnPrev.setDisable(false);
        btnNext.setDisable(true);

        btnDirRunner.getStyleClass().clear();
        btnFileRunner.getStyleClass().clear();

        btnDirRunner.getStyleClass().add("btnTab");
        btnFileRunner.getStyleClass().add("btnTab-sel");

        progData.guiFileRunner.toFront();
        progData.guiFileRunner.isShown();
        statusBarController.setStatusbarIndex(StatusBarController.StatusbarIndex.NONE);
    }
}


