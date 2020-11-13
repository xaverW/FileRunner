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

import de.p2tools.fileRunner.controller.RunEvent;
import de.p2tools.fileRunner.controller.RunListener;
import de.p2tools.fileRunner.controller.config.ProgConfig;
import de.p2tools.fileRunner.controller.config.ProgData;
import de.p2tools.p2Lib.alert.PAlert;
import de.p2tools.p2Lib.guiTools.PColumnConstraints;
import de.p2tools.p2Lib.guiTools.PTextField;
import de.p2tools.p2Lib.hash.HashConst;
import de.p2tools.p2Lib.tools.net.PUrlTools;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.io.File;

public class GuiFileRunner extends AnchorPane {

    private final GridPane gridPane = new GridPane();

    private final GuiFilePane guiFilePane1;
    private final GuiFilePane guiFilePane2;
    private final ScrollPane scrollPane = new ScrollPane();
    private final VBox vBoxCont = new VBox(10);

    private final RadioButton cbxMd5 = new RadioButton("MD5");
    private final RadioButton cbxSha1 = new RadioButton("Sha-1");
    private final RadioButton cbxSha256 = new RadioButton("Sha-256");
    private final RadioButton cbxSha512 = new RadioButton("Sha-512");

    private final Button btnCheckFile = new Button("Dateien vergleichen");

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
        cbxMd5.setToggleGroup(tg);
        cbxSha1.setToggleGroup(tg);
        cbxSha256.setToggleGroup(tg);
        cbxSha512.setToggleGroup(tg);
        cbxMd5.setSelected(true);

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
        Label lbl = new Label("Hash:   ");
        HBox hBox = new HBox(10);
        hBox.setPadding(new Insets(20, 0, 20, 0));
        hBox.setAlignment(Pos.CENTER_LEFT);
        hBox.getChildren().addAll(lbl, cbxMd5, cbxSha1, cbxSha256, cbxSha512);

        gridPane.setPadding(new Insets(10));
        gridPane.getStyleClass().add("pane-border-lightgray");
        gridPane.setVgap(10);
        gridPane.setHgap(10);
        gridPane.add(hBox, 1, 2);
        gridPane.add(btnCheckFile, 2, 2);
        gridPane.getColumnConstraints().addAll(PColumnConstraints.getCcPrefSize(),
                PColumnConstraints.getCcComputedSizeAndHgrow(), PColumnConstraints.getCcPrefSize());

        vBoxCont.setPadding(new Insets(25));
        vBoxCont.getChildren().addAll(gridPane);

        btnCheckFile.setTooltip(new Tooltip("Hash für beide Dateien erstellen und die Dateien damit vergeleichen."));
        cbxMd5.setTooltip(new Tooltip("Es wird ein MD5-Hash erstellt."));
        cbxSha1.setTooltip(new Tooltip("Es wird ein SHA-1 Hash erstellt."));
        cbxSha256.setTooltip(new Tooltip("Es wird ein SHA-256 Hash erstellt."));
        cbxSha512.setTooltip(new Tooltip("Es wird ein SHA-512 Hash erstellt."));
    }

    private void initData() {
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
            case HashConst.HASH_SHA512:
                cbxSha512.setSelected(true);
                break;
        }
        cbxMd5.setOnAction(a -> {
            guiFilePane1.clearHash();
            guiFilePane2.clearHash();
            ProgConfig.GUI_FILE_HASH.set(HashConst.HASH_MD5);
            ProgConfig.GUI_FILE_HASH_SUFF.set(HashConst.HASH_MD5_SUFFIX);
        });
        cbxSha1.setOnAction(a -> {
            guiFilePane1.clearHash();
            guiFilePane2.clearHash();
            ProgConfig.GUI_FILE_HASH.set(HashConst.HASH_SHA1);
            ProgConfig.GUI_FILE_HASH_SUFF.set(HashConst.HASH_SHA1_SUFFIX);
        });
        cbxSha256.setOnAction(a -> {
            guiFilePane1.clearHash();
            guiFilePane2.clearHash();
            ProgConfig.GUI_FILE_HASH.set(HashConst.HASH_SHA256);
            ProgConfig.GUI_FILE_HASH_SUFF.set(HashConst.HASH_SHA256_SUFFIX);
        });
        cbxSha512.setOnAction(a -> {
            guiFilePane1.clearHash();
            guiFilePane2.clearHash();
            ProgConfig.GUI_FILE_HASH.set(HashConst.HASH_SHA512);
            ProgConfig.GUI_FILE_HASH_SUFF.set(HashConst.HASH_SHA512_SUFFIX);
        });

        btnCheckFile.disableProperty().bind(guiFilePane1.compareNotProperty()
                .or(guiFilePane2.compareNotProperty())
                .or(isRunning));
    }

    private void initColor() {
        setColor();
        PTextField pTextField1 = guiFilePane1.getTxtHash();
        PTextField pTextField2 = guiFilePane2.getTxtHash();
        pTextField1.textProperty().addListener((observable, oldValue, newValue) -> {
            setColor();
        });
        pTextField2.textProperty().addListener((observable, oldValue, newValue) -> {
            setColor();
        });
    }

    public static String PTEXTFIELD_LABEL =
            "-fx-control-inner-background: lightgreen;";

    private void setColor() {
        PTextField pTextField1 = guiFilePane1.getTxtHash();
        PTextField pTextField2 = guiFilePane2.getTxtHash();
        String hash1 = pTextField1.getText().trim().toLowerCase();
        String hash2 = pTextField2.getText().trim().toLowerCase();

        if (!hash1.isEmpty() && !hash2.isEmpty() &&
                hash1.equals(hash2)) {

            pTextField1.getStyleClass().removeAll("txtHash");
            pTextField2.getStyleClass().removeAll("txtHash");
            pTextField1.getStyleClass().add("txtHashOk");
            pTextField2.getStyleClass().add("txtHashOk");

        } else {
            pTextField1.getStyleClass().removeAll("txtHashOk");
            pTextField2.getStyleClass().removeAll("txtHashOk");
            pTextField1.getStyleClass().add("txtHash");
            pTextField2.getStyleClass().add("txtHash");
        }
    }

    private void addListener() {
        progData.worker.addAdListener(new RunListener() {
            @Override
            public void ping(RunEvent runEvent) {
                if (runEvent.nixLos()) {
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
    }

    private boolean checkFile(String file, int nr) {
        if (PUrlTools.isUrl(file) && PUrlTools.urlExists(file) || new File(file).exists()) {
            return true;
        }
        if (PUrlTools.isUrl(file)) {
            PAlert.showErrorAlert("Hash erstellen", "Die angegebene URL " + nr + " existiert nicht!");
        } else {
            PAlert.showErrorAlert("Hash erstellen", "Die angegebene Datei " + nr + " existiert nicht!");
        }
        return false;
    }
}
