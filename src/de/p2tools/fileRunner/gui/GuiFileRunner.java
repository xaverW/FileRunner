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

package de.p2tools.fileRunner.gui;

import de.p2tools.fileRunner.controller.config.ProgConfig;
import de.p2tools.fileRunner.controller.config.ProgConst;
import de.p2tools.fileRunner.controller.config.ProgData;
import de.p2tools.fileRunner.controller.data.Icons;
import de.p2tools.p2Lib.dialog.DirFileChooser;
import de.p2tools.p2Lib.dialog.PAlert;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.VPos;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

public class GuiFileRunner extends AnchorPane {


    private final ScrollPane scrollPane = new ScrollPane();
    private final VBox vBoxCont = new VBox(10);

    private final TextField txtFile1 = new TextField("");
    private final TextField txtFile2 = new TextField("");
    private final TextField txtHash1 = new TextField("");
    private final TextField txtHash2 = new TextField("");

    private final RadioButton cbxMd5 = new RadioButton("MD5");
    private final RadioButton cbxSha1 = new RadioButton("Sha-1");
    private final RadioButton cbxSha256 = new RadioButton("Sha-256");

    private final Button btnGenHash1 = new Button("");
    private final Button btnGenHash2 = new Button("");

    private final Button btnGetFile1 = new Button("");
    private final Button btnGetFile2 = new Button("");

    private final Button btnSaveHash1 = new Button("Hash speichern");
    private final Button btnSaveHash2 = new Button("Hash speichern");

    private final ProgData progData;

    public GuiFileRunner() {
        progData = ProgData.getInstance();

        AnchorPane.setLeftAnchor(scrollPane, 0.0);
        AnchorPane.setBottomAnchor(scrollPane, 0.0);
        AnchorPane.setRightAnchor(scrollPane, 0.0);
        AnchorPane.setTopAnchor(scrollPane, 0.0);
        getChildren().addAll(scrollPane);
        scrollPane.setFitToHeight(true);
        scrollPane.setFitToWidth(true);
        scrollPane.setContent(vBoxCont);

        ToggleGroup tg = new ToggleGroup();
        cbxMd5.setToggleGroup(tg);
        cbxSha1.setToggleGroup(tg);
        cbxSha256.setToggleGroup(tg);
        cbxMd5.setSelected(true);
        initCont();
        initData();
        addListener();
    }

    public void isShown() {
    }

    private void initCont() {
        btnGenHash1.setGraphic(new Icons().ICON_BUTTON_GEN_HASH);
        btnGenHash2.setGraphic(new Icons().ICON_BUTTON_GEN_HASH);
        btnGetFile1.setGraphic(new Icons().ICON_BUTTON_FILE_OPEN);
        btnGetFile2.setGraphic(new Icons().ICON_BUTTON_FILE_OPEN);

        Label lblDir1 = new Label("Datei 1");
        lblDir1.getStyleClass().add("headerLabelVertikal");
        lblDir1.setMaxWidth(Double.MAX_VALUE);

        Label lblDir2 = new Label("Datei 2");
        lblDir2.getStyleClass().add("headerLabelVertikal");
        lblDir2.setMaxWidth(Double.MAX_VALUE);

        Label lblHash = new Label("Hash");
        lblHash.getStyleClass().add("headerLabelVertikal");
        lblHash.setMaxWidth(Double.MAX_VALUE);

        GridPane.setHgrow(txtFile1, Priority.ALWAYS);
        GridPane.setHgrow(txtHash1, Priority.ALWAYS);
        GridPane.setHgrow(txtFile2, Priority.ALWAYS);
        GridPane.setHgrow(txtHash2, Priority.ALWAYS);
        GridPane.setVgrow(lblDir1, Priority.ALWAYS);
        GridPane.setValignment(lblDir1, VPos.CENTER);
        GridPane.setVgrow(lblDir2, Priority.ALWAYS);
        GridPane.setValignment(lblDir2, VPos.CENTER);
        GridPane.setMargin(btnGetFile1, new Insets(10));
        GridPane.setMargin(btnGetFile2, new Insets(10));
        GridPane.setMargin(btnGenHash1, new Insets(10));

        GridPane.setMargin(btnSaveHash1, new Insets(10));
        GridPane.setMargin(btnSaveHash2, new Insets(10));
        GridPane.setMargin(btnGenHash2, new Insets(10));
        GridPane.setHalignment(btnSaveHash1, HPos.RIGHT);
        GridPane.setHalignment(btnSaveHash2, HPos.RIGHT);

        int r = 0;
        GridPane gridPane1 = new GridPane();
        gridPane1.getStyleClass().add("pane-border");
        gridPane1.setVgap(10);
        gridPane1.setHgap(10);

        gridPane1.add(lblDir1, 0, r, 1, 4);
        gridPane1.add(new Label(" "), 1, r);
        gridPane1.add(new Label("Datei"), 1, ++r);
        gridPane1.add(txtFile1, 2, r);
        gridPane1.add(btnGetFile1, 3, r);

        gridPane1.add(new Label("Hash"), 1, ++r);
        gridPane1.add(txtHash1, 2, r);
        gridPane1.add(btnGenHash1, 3, r);
        gridPane1.add(btnSaveHash1, 2, ++r, 2, 1);
        gridPane1.add(new Label(" "), 1, ++r);

        r = 0;
        GridPane gridPane2 = new GridPane();
        gridPane2.getStyleClass().add("pane-border");
        gridPane2.setVgap(10);
        gridPane2.setHgap(10);

        gridPane2.add(lblDir2, 0, ++r, 1, 4);
        gridPane2.add(new Label(" "), 1, r);

        gridPane2.add(new Label("Datei"), 1, ++r);
        gridPane2.add(txtFile2, 2, r);
        gridPane2.add(btnGetFile2, 3, r);

        gridPane2.add(new Label("Hash"), 1, ++r);
        gridPane2.add(txtHash2, 2, r);
        gridPane2.add(btnGenHash2, 3, r);
        gridPane2.add(btnSaveHash2, 2, ++r, 2, 1);
        gridPane2.add(new Label(" "), 1, ++r);

        r = 0;
        GridPane gridPaneHash = new GridPane();
        gridPaneHash.getStyleClass().add("pane-border");
        gridPaneHash.setVgap(10);
        gridPaneHash.setHgap(10);

        gridPaneHash.add(lblHash, 0, r, 1, 5);

        gridPaneHash.add(new Label(" "), 1, r);
        gridPaneHash.add(cbxMd5, 1, ++r);
        gridPaneHash.add(cbxSha1, 1, ++r);
        gridPaneHash.add(cbxSha256, 1, ++r);
        gridPaneHash.add(new Label(" "), 1, ++r);

        vBoxCont.setPadding(new Insets(25));
        vBoxCont.getChildren().addAll(gridPane1, gridPane2, gridPaneHash);

    }

    private void initData() {
        txtFile1.textProperty().bindBidirectional(ProgConfig.GUI_FILE_FILE1.getStringProperty());
        txtHash1.textProperty().bindBidirectional(ProgConfig.GUI_FILE_HASH1.getStringProperty());
        txtFile2.textProperty().bindBidirectional(ProgConfig.GUI_FILE_FILE2.getStringProperty());
        txtHash2.textProperty().bindBidirectional(ProgConfig.GUI_FILE_HASH2.getStringProperty());
        switch (ProgConfig.GUI_FILE_HASH.get()) {
            case ProgConst.HASH_MD5:
                cbxMd5.setSelected(true);
                break;
            case ProgConst.HASH_SHA1:
                cbxSha1.setSelected(true);
                break;
            case ProgConst.HASH_SHA256:
                cbxSha256.setSelected(true);
                break;
        }
        cbxMd5.setOnAction(a -> {
            ProgConfig.GUI_FILE_HASH.setValue(ProgConst.HASH_MD5);
            ProgConfig.GUI_FILE_HASH_SUFF.setValue(ProgConst.HASH_MD5_SUFFIX);
        });
        cbxSha1.setOnAction(a -> {
            ProgConfig.GUI_FILE_HASH.setValue(ProgConst.HASH_SHA1);
            ProgConfig.GUI_FILE_HASH_SUFF.setValue(ProgConst.HASH_SHA1_SUFFIX);
        });
        cbxSha256.setOnAction(a -> {
            ProgConfig.GUI_FILE_HASH.setValue(ProgConst.HASH_SHA256);
            ProgConfig.GUI_FILE_HASH_SUFF.setValue(ProgConst.HASH_SHA256_SUFFIX);
        });

        btnSaveHash1.disableProperty().bind(txtFile1.textProperty().isEmpty());
        btnSaveHash1.disableProperty().bind(txtHash1.textProperty().isEmpty());
        btnSaveHash2.disableProperty().bind(txtFile2.textProperty().isEmpty());
        btnSaveHash2.disableProperty().bind(txtHash2.textProperty().isEmpty());
    }

    private void addListener() {
        btnGenHash1.setOnAction(event -> {
            if (txtFile1.getText().trim().isEmpty()) {
                PAlert.showErrorAlert("Hash erstellen", "Es ist keine Datei angegeben!");
                return;
            }
            File file = new File(txtFile1.getText().trim());
            if (!file.exists()) {
                PAlert.showErrorAlert("Hash erstellen", "Die angegebene Datei existiert nicht!");
                return;
            }
            progData.worker.getHash(file, txtHash1.textProperty());
        });
        btnGenHash2.setOnAction(event -> {
            if (txtFile2.getText().trim().isEmpty()) {
                PAlert.showErrorAlert("Hash erstellen", "Es ist keine Datei angegeben!");
                return;
            }
            File file = new File(txtFile2.getText().trim());
            if (!file.exists()) {
                PAlert.showErrorAlert("Hash erstellen", "Die angegebene Datei existiert nicht!");
                return;
            }
            progData.worker.getHash(file, txtHash2.textProperty());
        });

        btnGetFile1.setOnAction(a -> {
            DirFileChooser.FileChooser(ProgData.getInstance().primaryStage, txtFile1);
            txtHash1.clear();
        });
        btnGetFile2.setOnAction(a -> {
            DirFileChooser.FileChooser(ProgData.getInstance().primaryStage, txtFile2);
            txtHash2.clear();
        });

        btnSaveHash1.setOnAction(a -> {
            if (txtFile1.getText().isEmpty()) {
                return;
            }

            Path hashFile = Paths.get(txtFile1.getText());
            String initDirStr = hashFile.getParent().toString();
            String initFileStr = hashFile.getFileName().toString() + "." + ProgConfig.GUI_FILE_HASH_SUFF.get();

            String fileStr = DirFileChooser.FileChooserSave(ProgData.getInstance().primaryStage, initDirStr, initFileStr).trim();
            if (fileStr == null || fileStr.isEmpty()) {
                return;
            }

            File file = new File(fileStr);
            if (file.exists()) {
                PAlert.BUTTON button = PAlert.showAlert_yes_no("Fehler",
                        "Datei existiert schon!",
                        "Die angegebene Datei existiert bereits, Datei überschreiben?");
                if (button != PAlert.BUTTON.YES) {
                    return;
                }
            }
            progData.worker.save(file, txtFile1.getText(), txtHash1.getText());


        });
        btnSaveHash2.setOnAction(a -> {
        });
    }

}
