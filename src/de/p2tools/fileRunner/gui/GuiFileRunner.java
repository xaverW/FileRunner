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
import de.p2tools.fileRunner.controller.config.ProgData;
import de.p2tools.fileRunner.controller.data.Icons;
import de.p2tools.p2Lib.dialog.DirFileChooser;
import de.p2tools.p2Lib.dialog.PAlert;
import de.p2tools.p2Lib.hash.HashConst;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.control.*;
import javafx.scene.layout.*;

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

    private final Button btnGetFile1 = new Button("");
    private final Button btnGetFile2 = new Button("");

    private final Button btnGenHash1 = new Button("Hash erstellen");
    private final Button btnGenHash2 = new Button("Hash erstellen");

    private final Button btnSaveHash1 = new Button("Hash speichern");
    private final Button btnSaveHash2 = new Button("Hash speichern");

    private final Button btnCheckFile = new Button("Dateien vergleichen");


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
        setColor();
        addListener();
    }

    public void isShown() {
    }

    private void initCont() {
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
        GridPane.setHalignment(btnSaveHash1, HPos.RIGHT);
        GridPane.setHalignment(btnSaveHash2, HPos.RIGHT);

        // Datei1
        int r = 0;
        GridPane gridPane1 = new GridPane();
        gridPane1.setPadding(new Insets(10));
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

        HBox hBox = new HBox(10);
        hBox.setAlignment(Pos.CENTER_RIGHT);
        hBox.getChildren().addAll(btnSaveHash1, btnGenHash1);
        gridPane1.add(hBox, 2, ++r);


        // Datei2
        r = 0;
        GridPane gridPane2 = new GridPane();
        gridPane2.setPadding(new Insets(10));
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

        hBox = new HBox(10);
        hBox.setAlignment(Pos.CENTER_RIGHT);
        hBox.getChildren().addAll(btnSaveHash2, btnGenHash2);
        gridPane2.add(hBox, 2, ++r);

        // Hash
        r = 0;
        GridPane gridPaneHash = new GridPane();
        gridPaneHash.setPadding(new Insets(20));
        gridPaneHash.getStyleClass().add("pane-border");
        gridPaneHash.setVgap(20);
        gridPaneHash.setHgap(20);

        gridPaneHash.add(cbxMd5, 0, r);
        gridPaneHash.add(cbxSha1, 1, r);
        gridPaneHash.add(cbxSha256, 2, r);

        HBox hBoxCheck = new HBox(10);
        hBoxCheck.setAlignment(Pos.CENTER_RIGHT);
        hBoxCheck.getChildren().add(btnCheckFile);
        GridPane.setHgrow(hBoxCheck, Priority.ALWAYS);
        gridPaneHash.add(hBoxCheck, 3, r);

        vBoxCont.setPadding(new Insets(25));
        vBoxCont.getChildren().addAll(gridPane1, gridPane2, gridPaneHash);

        btnGenHash1.setTooltip(new Tooltip("Hash der Datei 1 erstellen"));
        btnGenHash2.setTooltip(new Tooltip("Hash der Datei 2 erstellen"));
        btnCheckFile.setTooltip(new Tooltip("Hash für beide Dateien erstellen und die Dateien damit vergeleichen."));
        btnSaveHash1.setTooltip(new Tooltip("Hash der Datei 1 speichern."));
        btnSaveHash2.setTooltip(new Tooltip("Hash der Datei 2 speichern."));
        btnGetFile1.setTooltip(new Tooltip("Datei zum Erstellen des Hash auswählen."));
        btnGetFile2.setTooltip(new Tooltip("Datei zum Erstellen des Hash auswählen."));
        cbxMd5.setTooltip(new Tooltip("Es wird ein MD5-Hash erstellt."));
        cbxSha1.setTooltip(new Tooltip("Es wird ein SHA-1 Hash erstellt."));
        cbxSha256.setTooltip(new Tooltip("Es wird ein SHA-256 Hash erstellt."));
    }

    private void initData() {
        txtFile1.textProperty().bindBidirectional(ProgConfig.GUI_FILE_FILE1.getStringProperty());
        txtHash1.textProperty().bindBidirectional(ProgConfig.GUI_FILE_HASH1.getStringProperty());
        txtFile2.textProperty().bindBidirectional(ProgConfig.GUI_FILE_FILE2.getStringProperty());
        txtHash2.textProperty().bindBidirectional(ProgConfig.GUI_FILE_HASH2.getStringProperty());

        switch (ProgConfig.GUI_FILE_HASH.get()) {
            case HashConst.HASH_MD5:
                cbxMd5.setSelected(true);
                break;
            case HashConst.HASH_SHA1:
                cbxSha1.setSelected(true);
                break;
            case HashConst.HASH_SHA256:
                cbxSha256.setSelected(true);
                break;
        }
        cbxMd5.setOnAction(a -> {
            clearHash();
            ProgConfig.GUI_FILE_HASH.setValue(HashConst.HASH_MD5);
            ProgConfig.GUI_FILE_HASH_SUFF.setValue(HashConst.HASH_MD5_SUFFIX);
        });
        cbxSha1.setOnAction(a -> {
            clearHash();
            ProgConfig.GUI_FILE_HASH.setValue(HashConst.HASH_SHA1);
            ProgConfig.GUI_FILE_HASH_SUFF.setValue(HashConst.HASH_SHA1_SUFFIX);
        });
        cbxSha256.setOnAction(a -> {
            clearHash();
            ProgConfig.GUI_FILE_HASH.setValue(HashConst.HASH_SHA256);
            ProgConfig.GUI_FILE_HASH_SUFF.setValue(HashConst.HASH_SHA256_SUFFIX);
        });

        btnCheckFile.disableProperty().bind(txtFile1.textProperty().isEmpty().or(txtFile2.textProperty().isEmpty()));
        btnGenHash1.disableProperty().bind(txtFile1.textProperty().isEmpty());
        btnGenHash2.disableProperty().bind(txtFile2.textProperty().isEmpty());
        btnSaveHash1.disableProperty().bind(txtFile1.textProperty().isEmpty().or(txtHash1.textProperty().isEmpty()));
        btnSaveHash2.disableProperty().bind(txtFile2.textProperty().isEmpty().or(txtHash2.textProperty().isEmpty()));
    }

    private void clearHash() {
        txtHash1.clear();
        txtHash2.clear();
    }

    private void setColor() {
        if (!txtHash1.getText().isEmpty() && !txtHash2.getText().isEmpty() &&
                txtHash1.getText().equals(txtHash2.getText())) {
            txtHash1.getStyleClass().add("txtHash");
            txtHash2.getStyleClass().add("txtHash");
        } else {
            txtHash1.getStyleClass().removeAll("txtHash");
            txtHash2.getStyleClass().removeAll("txtHash");
        }
    }

    private void addListener() {
        txtFile1.textProperty().addListener((observable, oldValue, newValue) -> {
            txtHash1.clear();
        });
        txtFile2.textProperty().addListener((observable, oldValue, newValue) -> {
            txtHash2.clear();
        });

        txtHash1.textProperty().addListener((observable, oldValue, newValue) -> {
            setColor();
        });
        txtHash2.textProperty().addListener((observable, oldValue, newValue) -> {
            setColor();
        });


        btnGenHash1.setOnAction(event -> {
            txtHash1.clear();
            if (txtFile1.getText().trim().isEmpty()) {
                PAlert.showErrorAlert("Hash erstellen", "Es ist keine Datei angegeben!");
                return;
            }
            File file = new File(txtFile1.getText().trim());
            if (!file.exists()) {
                PAlert.showErrorAlert("Hash erstellen", "Die angegebene Datei existiert nicht!");
                return;
            }
            progData.worker.genFileHash(file, txtHash1.textProperty());
        });
        btnGenHash2.setOnAction(event -> {
            txtHash2.clear();
            if (txtFile2.getText().trim().isEmpty()) {
                PAlert.showErrorAlert("Hash erstellen", "Es ist keine Datei angegeben!");
                return;
            }
            File file = new File(txtFile2.getText().trim());
            if (!file.exists()) {
                PAlert.showErrorAlert("Hash erstellen", "Die angegebene Datei existiert nicht!");
                return;
            }
            progData.worker.genFileHash(file, txtHash2.textProperty());
        });

        btnGetFile1.setOnAction(a -> {
            txtHash1.clear();
            DirFileChooser.FileChooser(ProgData.getInstance().primaryStage, txtFile1);
        });
        btnGetFile2.setOnAction(a -> {
            txtHash2.clear();
            DirFileChooser.FileChooser(ProgData.getInstance().primaryStage, txtFile2);
        });

        btnSaveHash1.setOnAction(a -> {
            if (txtFile1.getText().isEmpty() || txtHash1.getText().isEmpty()) {
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
            progData.worker.save(file, txtFile1.getText(), txtHash1.getText());

        });
        btnSaveHash2.setOnAction(a -> {
            if (txtFile2.getText().isEmpty() || txtHash2.getText().isEmpty()) {
                return;
            }

            Path hashFile = Paths.get(txtFile2.getText());
            String initDirStr = hashFile.getParent().toString();
            String initFileStr = hashFile.getFileName().toString() + "." + ProgConfig.GUI_FILE_HASH_SUFF.get();

            String fileStr = DirFileChooser.FileChooserSave(ProgData.getInstance().primaryStage, initDirStr, initFileStr).trim();
            if (fileStr == null || fileStr.isEmpty()) {
                return;
            }

            File file = new File(fileStr);
            progData.worker.save(file, txtFile2.getText(), txtHash2.getText());
        });

        btnCheckFile.setOnAction(a -> {
            txtHash1.clear();
            txtHash2.clear();
            if (txtFile1.getText().trim().isEmpty()) {
                PAlert.showErrorAlert("Hash erstellen", "Es ist keine Datei (1) angegeben!");
                return;
            }
            if (txtFile2.getText().trim().isEmpty()) {
                PAlert.showErrorAlert("Hash erstellen", "Es ist keine Datei (2) angegeben!");
                return;
            }
            File file1 = new File(txtFile1.getText().trim());
            if (!file1.exists()) {
                PAlert.showErrorAlert("Hash erstellen", "Die angegebene Datei (1) existiert nicht!");
                return;
            }
            File file2 = new File(txtFile2.getText().trim());
            if (!file2.exists()) {
                PAlert.showErrorAlert("Hash erstellen", "Die angegebene Datei (2) existiert nicht!");
                return;
            }
            progData.worker.genFileHash(file1, txtHash1.textProperty(), file2, txtHash2.textProperty());
        });
    }
}
