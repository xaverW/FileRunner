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

package de.p2tools.fileRunner.gui;

import de.p2tools.fileRunner.controller.config.ProgData;
import de.p2tools.fileRunner.controller.data.Icons;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

public class GuiFileRunner extends AnchorPane {

    private final SplitPane splitPane = new SplitPane();

    private final VBox vBox1 = new VBox(10);
    private final VBox vBox2 = new VBox(10);

    private final ScrollPane scrollPane1 = new ScrollPane();
    private final ScrollPane scrollPane2 = new ScrollPane();
    private final TableView table1 = new TableView();
    private final TableView table2 = new TableView();

    private final TextField txtDir1 = new TextField("");
    private final TextField txtDir2 = new TextField("");
    private final TextField txtHash1 = new TextField("");
    private final TextField txtHash2 = new TextField("");
    private final TextField txtWriteHash1 = new TextField("");
    private final TextField txtWriteHash2 = new TextField("");

    private final Button btnDir1 = new Button("");
    private final Button btnDir2 = new Button("");
    private final Button btnhash1 = new Button("");
    private final Button btnhash2 = new Button("");
    private final Button btnWriteHash1 = new Button();
    private final Button btnWriteHash2 = new Button();

    private final ProgData progData;

    public GuiFileRunner() {
        progData = ProgData.getInstance();


        AnchorPane.setLeftAnchor(splitPane, 0.0);
        AnchorPane.setBottomAnchor(splitPane, 0.0);
        AnchorPane.setRightAnchor(splitPane, 0.0);
        AnchorPane.setTopAnchor(splitPane, 0.0);
        getChildren().addAll(splitPane);

        scrollPane1.setFitToHeight(true);
        scrollPane1.setFitToWidth(true);
        scrollPane2.setFitToHeight(true);
        scrollPane2.setFitToWidth(true);

        scrollPane1.setContent(table1);
        scrollPane2.setContent(table2);

        initCont();

    }

    public void isShown() {
    }

    private void initCont() {
        btnDir1.setGraphic(new Icons().ICON_BUTTON_FILE_OPEN);
        btnDir2.setGraphic(new Icons().ICON_BUTTON_FILE_OPEN);
        btnhash1.setGraphic(new Icons().ICON_BUTTON_FILE_OPEN);
        btnhash2.setGraphic(new Icons().ICON_BUTTON_FILE_OPEN);
        btnWriteHash1.setGraphic(new Icons().ICON_BUTTON_FILE_OPEN);
        btnWriteHash2.setGraphic(new Icons().ICON_BUTTON_FILE_OPEN);

        HBox hBoxDir1 = new HBox(10);
        hBoxDir1.getChildren().addAll(txtDir1, btnDir1);
        HBox hBoxDir2 = new HBox(10);
        hBoxDir2.getChildren().addAll(txtDir2, btnDir2);
        HBox hBoxDir1Hash = new HBox(10);
        hBoxDir1Hash.getChildren().addAll(txtHash1, btnhash1);
        HBox hBoxDir2Hash = new HBox(10);
        hBoxDir2Hash.getChildren().addAll(txtHash2, btnhash2);

        HBox hBoxWriteHash1 = new HBox(10);
        hBoxWriteHash1.getChildren().addAll(txtWriteHash1, btnWriteHash1);
        HBox hBoxWriteHash2 = new HBox(10);
        hBoxWriteHash2.getChildren().addAll(txtWriteHash2, btnWriteHash2);

        txtDir1.setMaxWidth(Double.MAX_VALUE);
        txtDir2.setMaxWidth(Double.MAX_VALUE);
        txtHash1.setMaxWidth(Double.MAX_VALUE);
        txtHash2.setMaxWidth(Double.MAX_VALUE);
        txtWriteHash1.setMaxWidth(Double.MAX_VALUE);
        txtWriteHash2.setMaxWidth(Double.MAX_VALUE);

        HBox.setHgrow(txtDir1, Priority.ALWAYS);
        HBox.setHgrow(txtDir2, Priority.ALWAYS);
        HBox.setHgrow(txtHash1, Priority.ALWAYS);
        HBox.setHgrow(txtHash2, Priority.ALWAYS);
        HBox.setHgrow(txtWriteHash1, Priority.ALWAYS);
        HBox.setHgrow(txtWriteHash2, Priority.ALWAYS);

        vBox1.setPadding(new Insets(10));
        vBox2.setPadding(new Insets(10));

        VBox.setVgrow(scrollPane1, Priority.ALWAYS);
        VBox.setVgrow(scrollPane2, Priority.ALWAYS);

        vBox1.getChildren().addAll(new Label("Verzeichnis 1"), hBoxDir1,
                new Label("Hashdatei"), hBoxDir1Hash, scrollPane1,
                new Label("Hashdatei schreiben"), hBoxWriteHash1);

        vBox2.getChildren().addAll(new Label("Verzeichnis 2"), hBoxDir2,
                new Label("Hashdatei"), hBoxDir2Hash, scrollPane2,
                new Label("Hashdatei schreiben"), hBoxWriteHash2);

        splitPane.setOrientation(Orientation.HORIZONTAL);
        splitPane.getItems().addAll(vBox1, vBox2);
    }

}
