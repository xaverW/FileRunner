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

package de.p2tools.filerunner.gui;

import de.p2tools.filerunner.controller.config.Events;
import de.p2tools.filerunner.controller.config.ProgConfig;
import de.p2tools.filerunner.controller.config.ProgData;
import de.p2tools.filerunner.icon.ProgIconsFileRunner;
import de.p2tools.p2lib.guitools.P2ColumnConstraints;
import de.p2tools.p2lib.guitools.P2TextField;
import de.p2tools.p2lib.hash.HashConst;
import de.p2tools.p2lib.p2event.P2Event;
import de.p2tools.p2lib.p2event.P2Listener;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class GuiFileRunner extends AnchorPane {

    private final GridPane gridPane = new GridPane();

    private final GuiFilePane guiFilePane1;
    private final GuiFilePane guiFilePane2;
    private final ScrollPane scrollPane = new ScrollPane();
    private final VBox vBoxCont = new VBox(10);

    private final RadioButton rbMd5 = new RadioButton("MD5");
    private final RadioButton rbSha1 = new RadioButton("Sha-1");
    private final RadioButton rbSha256 = new RadioButton("Sha-256");
    private final RadioButton rbSha512 = new RadioButton("Sha-512");
    private final Button btnCheckFile = new Button("Beide Dateien einlesen und vergleichen");

    private final BooleanProperty isRunning = new SimpleBooleanProperty(false);
    private final ProgData progData;

    public GuiFileRunner() {
        progData = ProgData.getInstance();

        guiFilePane1 = new GuiFilePane(progData, true);
        guiFilePane2 = new GuiFilePane(progData, false);

        AnchorPane.setLeftAnchor(scrollPane, 0.0);
        AnchorPane.setBottomAnchor(scrollPane, 0.0);
        AnchorPane.setRightAnchor(scrollPane, 0.0);
        AnchorPane.setTopAnchor(scrollPane, 0.0);
        getChildren().addAll(scrollPane);
        scrollPane.setFitToHeight(true);
        scrollPane.setFitToWidth(true);
        scrollPane.setContent(vBoxCont);

        ToggleGroup tg = new ToggleGroup();
        rbMd5.setToggleGroup(tg);
        rbSha1.setToggleGroup(tg);
        rbSha256.setToggleGroup(tg);
        rbSha512.setToggleGroup(tg);
        rbMd5.setSelected(true);

        initPane(true);
        initPane(false);

        initCont();
        initData();
        initColor();
        addListener();
    }

    public void isShown() {
    }

    private void initPane(boolean one) {
        Label lblDir = new Label("Datei " + (one ? "1" : "2"));
        lblDir.getStyleClass().add("headerLabelVertikal");
        lblDir.setMaxWidth(Double.MAX_VALUE);
        gridPane.add(lblDir, 0, one ? 0 : 1);
        gridPane.add(one ? guiFilePane1 : guiFilePane2, 1, one ? 0 : 1, 2, 1);
    }

    private void initCont() {
        btnCheckFile.setGraphic(ProgIconsFileRunner.ICON_BUTTON_GUI_START_ALL.getImageView());
        btnCheckFile.setTooltip(new Tooltip("Es werden beide Dateien eingelesen, " +
                "der Hash wird erstellt und die Dateien damit vergleichen."));

        rbMd5.setTooltip(new Tooltip("Es wird ein MD5-Hash erstellt."));
        rbSha1.setTooltip(new Tooltip("Es wird ein SHA-1 Hash erstellt."));
        rbSha256.setTooltip(new Tooltip("Es wird ein SHA-256 Hash erstellt."));
        rbSha512.setTooltip(new Tooltip("Es wird ein SHA-512 Hash erstellt."));
        Label lbl = new Label("Hash:   ");
        HBox hBox = new HBox(10);
        hBox.setPadding(new Insets(10, 0, 10, 0));
        hBox.setAlignment(Pos.CENTER_LEFT);
        hBox.getChildren().addAll(lbl, rbMd5, rbSha1, rbSha256, rbSha512);

        gridPane.setPadding(new Insets(10));
        gridPane.setStyle("-fx-border-width: 5; -fx-border-color: #d6d6d6;");
        gridPane.setVgap(10);
        gridPane.setHgap(10);
        gridPane.add(hBox, 1, 2);
        gridPane.add(btnCheckFile, 2, 2);

        gridPane.getColumnConstraints().addAll(P2ColumnConstraints.getCcPrefSize(),
                P2ColumnConstraints.getCcComputedSizeAndHgrow(), P2ColumnConstraints.getCcPrefSize());

        vBoxCont.setPadding(new Insets(25));
        vBoxCont.getChildren().addAll(gridPane);
    }

    private void initData() {
        switch (ProgConfig.GUI_FILE_HASH.get()) {
            case HashConst.HASH_MD5:
                rbMd5.setSelected(true);
                break;
            case HashConst.HASH_SHA1:
                rbSha1.setSelected(true);
                break;
            case HashConst.HASH_SHA256:
                rbSha256.setSelected(true);
                break;
            case HashConst.HASH_SHA512:
                rbSha512.setSelected(true);
                break;
        }
        rbMd5.setOnAction(a -> {
            guiFilePane1.clearHash();
            guiFilePane2.clearHash();
            ProgConfig.GUI_FILE_HASH.set(HashConst.HASH_MD5);
            ProgConfig.GUI_FILE_HASH_SUFF.set(HashConst.HASH_MD5_SUFFIX);
        });
        rbSha1.setOnAction(a -> {
            guiFilePane1.clearHash();
            guiFilePane2.clearHash();
            ProgConfig.GUI_FILE_HASH.set(HashConst.HASH_SHA1);
            ProgConfig.GUI_FILE_HASH_SUFF.set(HashConst.HASH_SHA1_SUFFIX);
        });
        rbSha256.setOnAction(a -> {
            guiFilePane1.clearHash();
            guiFilePane2.clearHash();
            ProgConfig.GUI_FILE_HASH.set(HashConst.HASH_SHA256);
            ProgConfig.GUI_FILE_HASH_SUFF.set(HashConst.HASH_SHA256_SUFFIX);
        });
        rbSha512.setOnAction(a -> {
            guiFilePane1.clearHash();
            guiFilePane2.clearHash();
            ProgConfig.GUI_FILE_HASH.set(HashConst.HASH_SHA512);
            ProgConfig.GUI_FILE_HASH_SUFF.set(HashConst.HASH_SHA512_SUFFIX);
        });

        btnCheckFile.disableProperty().bind(guiFilePane1.disableCompareButtonProperty()
                .or(guiFilePane2.disableCompareButtonProperty())
                .or(isRunning));
    }

    private void initColor() {
        setColor();
        P2TextField pTextField1 = guiFilePane1.getTxtHash();
        P2TextField pTextField2 = guiFilePane2.getTxtHash();
        pTextField1.textProperty().addListener((observable, oldValue, newValue) -> {
            setColor();
        });
        pTextField2.textProperty().addListener((observable, oldValue, newValue) -> {
            setColor();
        });
    }

    private void setColor() {
        P2TextField pTextField1 = guiFilePane1.getTxtHash();
        P2TextField pTextField2 = guiFilePane2.getTxtHash();
        String hash1 = pTextField1.getText().trim().toLowerCase();
        String hash2 = pTextField2.getText().trim().toLowerCase();

        hash1 = hash1.replaceAll(" ", "");
        hash2 = hash2.replaceAll(" ", "");

        if (!hash1.isEmpty() && !hash2.isEmpty() &&
                hash1.equals(hash2)) {
            pTextField1.getStyleClass().removeAll("txtHash");
            pTextField2.getStyleClass().removeAll("txtHash");
            pTextField1.getStyleClass().removeAll("txtHashNotOk");
            pTextField2.getStyleClass().removeAll("txtHashNotOk");
            pTextField1.getStyleClass().add("txtHashOk");
            pTextField2.getStyleClass().add("txtHashOk");
            guiFilePane1.setTxtHashOk(true, true);
            guiFilePane2.setTxtHashOk(true, true);

        } else if (!hash1.isEmpty() && !hash2.isEmpty() &&
                !hash1.equals(hash2)) {
            pTextField1.getStyleClass().removeAll("txtHash");
            pTextField2.getStyleClass().removeAll("txtHash");
            pTextField1.getStyleClass().removeAll("txtHashOk");
            pTextField2.getStyleClass().removeAll("txtHashOk");
            pTextField1.getStyleClass().add("txtHashNotOk");
            pTextField2.getStyleClass().add("txtHashNotOk");
            guiFilePane1.setTxtHashOk(false, true);
            guiFilePane2.setTxtHashOk(false, true);

        } else {
            pTextField1.getStyleClass().removeAll("txtHashOk");
            pTextField2.getStyleClass().removeAll("txtHashOk");
            pTextField1.getStyleClass().removeAll("txtHashNotOk");
            pTextField2.getStyleClass().removeAll("txtHashNotOk");
            pTextField1.getStyleClass().add("txtHash");
            pTextField2.getStyleClass().add("txtHash");
            guiFilePane1.setTxtHashOk(false, false);
            guiFilePane2.setTxtHashOk(false, false);
        }
    }

    private void addListener() {
        progData.pEventHandler.addListener(new P2Listener(Events.COMPARE_OF_FILE_LISTS_FINISHED) {
            public void ping(P2Event event) {
                if (event.getMax() == 0) {
                    isRunning.setValue(false);
                } else {
                    isRunning.setValue(true);
                }
            }
        });

        btnCheckFile.setOnAction(a -> {
            guiFilePane1.clearHash();
            guiFilePane2.clearHash();
            guiFilePane1.genLoadHash();
            guiFilePane2.genLoadHash();
        });

        rbMd5.disableProperty().bind(isRunning);
        rbSha1.disableProperty().bind(isRunning);
        rbSha256.disableProperty().bind(isRunning);
        rbSha512.disableProperty().bind(isRunning);
    }
}
