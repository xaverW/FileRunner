/*
 * P2tools Copyright (C) 2019 W. Xaver W.Xaver[at]googlemail.com
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
import de.p2tools.filerunner.controller.config.RunPEvent;
import de.p2tools.filerunner.controller.worker.gethash.HashFactory;
import de.p2tools.filerunner.icon.ProgIconsFileRunner;
import de.p2tools.p2lib.alert.PAlert;
import de.p2tools.p2lib.dialogs.PDirFileChooser;
import de.p2tools.p2lib.guitools.PColumnConstraints;
import de.p2tools.p2lib.guitools.PComboBoxString;
import de.p2tools.p2lib.guitools.PTextField;
import de.p2tools.p2lib.tools.events.PEvent;
import de.p2tools.p2lib.tools.events.PListener;
import de.p2tools.p2lib.tools.file.PFileName;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.StringProperty;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

public class GuiFilePane extends VBox {

    private final Button btnSelectFile = new Button("");
    private final Button btnSelectHashFile = new Button("");
    private final RadioButton rbFileOrUrl = new RadioButton("Datei/URL");
    private final RadioButton rbReadHashFile = new RadioButton("Hashdatei");
    private final RadioButton rbHash = new RadioButton("Hash");
    private final Button btnProposeStoringHashFileName = new Button("");
    private final PComboBoxString cboWriteHash = new PComboBoxString();
    private final Button btnSelectHashFileForStoring = new Button();

    private final Button btnReadFileAndGenHash = new Button("");
    private final Button btnReadHashFile = new Button("");
    private final Button btnSaveHash = new Button("Hash speichern");

    private final PComboBoxString cboGenHashFromFile = new PComboBoxString();
    private final PComboBoxString cboReadHashFile = new PComboBoxString();
    private final PTextField txtHash = new PTextField();
    private final Label lblHashOk = new Label();

    private final IntegerProperty sel;
    private final StringProperty srcFile;
    private final StringProperty hashFile;
    private final BooleanProperty isRunning = new SimpleBooleanProperty(false);
    private final BooleanProperty disableCompareButton = new SimpleBooleanProperty(false);
    private final StringProperty writeHash;
    private final ProgData progData;

    public GuiFilePane(ProgData progData, boolean panel1) {
        this.progData = progData;
        if (panel1) {
            sel = ProgConfig.compFileSel1;
            srcFile = ProgConfig.compFileSrcFile1;
            hashFile = ProgConfig.compFileHashFile1;
            writeHash = ProgConfig.writeFileHash1;
        } else {
            sel = ProgConfig.compFileSel2;
            srcFile = ProgConfig.compFileSrcFile2;
            hashFile = ProgConfig.compFileHashFile2;
            writeHash = ProgConfig.writeFileHash2;
        }

        initButton();
        initPane();
        addListener();
    }

    public BooleanProperty disableCompareButtonProperty() {
        return disableCompareButton;
    }

    public void clearHash() {
        if (!rbHash.isSelected()) {
            //dann ist ein fester Hash vorgegeben, den besser lassen?
            txtHash.setText("");
            setTxtHashOk(false, false);
        }
    }

    public PTextField getTxtHash() {
        return txtHash;
    }

    public void setTxtHashOk(boolean ok, boolean vis) {
        if (ok) {
            lblHashOk.setGraphic(ProgIconsFileRunner.ICON_LABEL_FILE_OK.getImageView());
        } else {
            lblHashOk.setGraphic(ProgIconsFileRunner.ICON_LABEL_FILE_NOT_OK.getImageView());
        }
        lblHashOk.setVisible(vis);
    }

    private void initButton() {
        ToggleGroup tg = new ToggleGroup();
        tg.getToggles().addAll(rbFileOrUrl, rbReadHashFile, rbHash);
        switch (sel.get()) {
            case 0:
                rbFileOrUrl.setSelected(true);
                break;
            case 1:
                rbReadHashFile.setSelected(true);
                break;
            case 2:
                rbHash.setSelected(true);
                break;
        }

        //=======================
        //file/URL
        rbFileOrUrl.disableProperty().bind(isRunning);
        rbFileOrUrl.setOnAction(event -> {
            txtHash.clear();
            setTxtHashOk(false, false);
            sel.set(0);
        });

        cboGenHashFromFile.setMaxWidth(Double.MAX_VALUE);
        cboGenHashFromFile.init(ProgConfig.compFileSrcFileList, srcFile);
        cboGenHashFromFile.disableProperty().bind(rbFileOrUrl.selectedProperty().not()
                .or(isRunning));

        btnSelectFile.setGraphic(ProgIconsFileRunner.ICON_BUTTON_FILE_OPEN.getImageView());
        btnSelectFile.setTooltip(new Tooltip("Datei zum Erstellen des Hash auswählen."));
        btnSelectFile.disableProperty().bind(rbFileOrUrl.selectedProperty().not()
                .or(isRunning));

        btnReadFileAndGenHash.setGraphic(ProgIconsFileRunner.ICON_BUTTON_GEN_HASH.getImageView());
        btnReadFileAndGenHash.setTooltip(new Tooltip("Datei einlesen und Hash erstellen."));
        btnReadFileAndGenHash.disableProperty().bind(rbFileOrUrl.selectedProperty().not()
                .or(cboGenHashFromFile.getEditor().textProperty().isNull())
                .or(cboGenHashFromFile.getEditor().textProperty().isEqualTo(""))
                .or(isRunning));

        //=======================
        //hashfile
        rbReadHashFile.disableProperty().bind(isRunning);
        rbReadHashFile.setOnAction(event -> {
            txtHash.clear();
            setTxtHashOk(false, false);
            sel.set(1);
        });

        cboReadHashFile.setMaxWidth(Double.MAX_VALUE);
        cboReadHashFile.init(ProgConfig.compFileHashFileList, hashFile);
        cboReadHashFile.disableProperty().bind(rbReadHashFile.selectedProperty().not()
                .or(isRunning));

        btnSelectHashFile.setGraphic(ProgIconsFileRunner.ICON_BUTTON_FILE_OPEN.getImageView());
        btnSelectHashFile.setTooltip(new Tooltip("Datei mit Hash-Werten auswählen."));
        btnSelectFile.disableProperty().bind(rbReadHashFile.selectedProperty().not()
                .or(isRunning));

        btnReadHashFile.setGraphic(ProgIconsFileRunner.ICON_BUTTON_GEN_HASH.getImageView());
        btnReadHashFile.setTooltip(new Tooltip("Hash aus Datei lesen."));
        btnReadHashFile.disableProperty().bind(rbReadHashFile.selectedProperty().not()
                .or(cboReadHashFile.getEditor().textProperty().isNull())
                .or(cboReadHashFile.getEditor().textProperty().isEqualTo(""))
                .or(isRunning));

        //=======================
        //hash
        rbHash.disableProperty().bind(isRunning);
        rbHash.selectedProperty().addListener((v, o, n) -> {
            txtHash.setStateLabel(!rbHash.isSelected());
        });
        rbHash.setOnAction(event -> {
            txtHash.clear();
            setTxtHashOk(false, false);
            sel.set(2);
        });
        txtHash.setStateLabel(!rbHash.isSelected());

        cboWriteHash.setMaxWidth(Double.MAX_VALUE);
        cboWriteHash.init(ProgConfig.writeFileHashList, writeHash);
        cboWriteHash.disableProperty().bind(txtHash.textProperty().isEmpty()
                .or(isRunning));


        btnSaveHash.setTooltip(new Tooltip("Hash der Datei speichern."));
        btnSaveHash.disableProperty().bind(cboWriteHash.getEditor().textProperty().isEmpty()
                .or(txtHash.textProperty().isEmpty())
                .or(isRunning));

        btnSelectHashFileForStoring.setGraphic(ProgIconsFileRunner.ICON_BUTTON_FILE_OPEN.getImageView());
        btnSelectHashFileForStoring.setTooltip(new Tooltip("Datei zum Speichern auswählen."));
        btnSelectHashFileForStoring.disableProperty().bind(cboWriteHash.disableProperty()
                .or(isRunning));

        btnProposeStoringHashFileName.setGraphic(ProgIconsFileRunner.ICON_BUTTON_GUI_GEN_NAME.getImageView());
        btnProposeStoringHashFileName.setTooltip(new Tooltip("Einen Dateinamen vorschlagen."));
        btnProposeStoringHashFileName.disableProperty().bind(cboWriteHash.disableProperty());
    }

    private void initPane() {
        int r = 0;
        GridPane gridPane = new GridPane();
        gridPane.setStyle(" -fx-border-width: 1; -fx-border-color: -style-tab-border-color-1;");
        gridPane.setPadding(new Insets(10));
        gridPane.setVgap(10);
        gridPane.setHgap(10);
        VBox.setVgrow(gridPane, Priority.ALWAYS);

        gridPane.add(rbFileOrUrl, 0, r);
        gridPane.add(cboGenHashFromFile, 1, r);
        gridPane.add(btnSelectFile, 2, r);
        gridPane.add(btnReadFileAndGenHash, 3, r++);

        gridPane.add(rbReadHashFile, 0, r);
        gridPane.add(cboReadHashFile, 1, r);
        gridPane.add(btnSelectHashFile, 2, r);
        gridPane.add(btnReadHashFile, 3, r++);

        gridPane.add(rbHash, 0, r);
        gridPane.add(txtHash, 1, r);
        gridPane.add(lblHashOk, 2, r++);
        GridPane.setHalignment(lblHashOk, HPos.CENTER);

        gridPane.add(new Label("Hash speichern"), 0, r);
        gridPane.add(cboWriteHash, 1, r);
        gridPane.add(btnSelectHashFileForStoring, 2, r);
        gridPane.add(btnProposeStoringHashFileName, 3, r++);

        GridPane.setHalignment(btnSaveHash, HPos.RIGHT);
        gridPane.add(btnSaveHash, 1, ++r, 3, 1);

        gridPane.getColumnConstraints().addAll(PColumnConstraints.getCcPrefSize(),
                PColumnConstraints.getCcComputedSizeAndHgrow(),
                PColumnConstraints.getCcPrefSize());

        getChildren().addAll(gridPane);
    }

    private void addListener() {
        progData.pEventHandler.addListener(
                new PListener(Events.COMPARE_OF_FILE_LISTS_FINISHED) {
                    public <T extends PEvent> void ping(T runEvent) {
                        if (runEvent.getClass().equals(RunPEvent.class)) {
                            RunPEvent runE = (RunPEvent) runEvent;
                            if (runE.nixLos()) {
                                isRunning.setValue(false);
                            } else {
                                isRunning.setValue(true);
                            }
                        }
                    }
                });

        btnSelectFile.setOnAction(event -> PDirFileChooser.FileChooser(progData.primaryStage, cboGenHashFromFile));
        btnSelectFile.disableProperty().bind(isRunning);
        btnSelectHashFile.setOnAction(event -> PDirFileChooser.FileChooser(progData.primaryStage, cboReadHashFile));
        btnSelectHashFile.disableProperty().bind(isRunning);
        btnSaveHash.setOnAction(event -> saveHash());
        btnReadHashFile.setOnAction(event -> genLoadHash());
        btnReadFileAndGenHash.setOnAction(event -> genLoadHash());
        btnSelectHashFileForStoring.setOnAction(event -> PDirFileChooser.FileChooser(progData.primaryStage, cboWriteHash));
        btnProposeStoringHashFileName.setOnAction(event -> {
            String file;
            file = cboGenHashFromFile.getSelValue();
            if (file.isEmpty()) {
                return;
            }

            if (cboWriteHash.getEditor().getText().isEmpty()) {
                cboWriteHash.getEditor().setText(file);
            }
            final String nextElement =
                    PFileName.getNextFileNameWithDate(cboWriteHash.getEditor().getText(), ProgConfig.GUI_FILE_HASH_SUFF.getValue());
            cboWriteHash.selectElement(nextElement);
        });

        cboGenHashFromFile.getEditor().textProperty().addListener((ob, o, n) -> {
            txtHash.clear();
            setTxtHashOk(false, false);
        });
        cboReadHashFile.getEditor().textProperty().addListener((ob, o, n) -> {
            txtHash.clear();
            setTxtHashOk(false, false);
        });

        disableCompareButton.bind((rbFileOrUrl.selectedProperty().and(btnReadFileAndGenHash.disableProperty()))
                .or((rbReadHashFile.selectedProperty().and(btnReadHashFile.disableProperty())))
                .or((rbHash.selectedProperty().and(txtHash.textProperty().isEmpty()))));
    }

    public void genLoadHash() {
        if (rbFileOrUrl.isSelected()) {
            genHash();
        } else if (rbReadHashFile.isSelected()) {
            readHash();
        }
    }

    private void genHash() {
        txtHash.clear();
        setTxtHashOk(false, false);
        if (cboGenHashFromFile.getSelValue().trim().isEmpty()) {
            PAlert.showErrorAlert("Hash erstellen", "Es ist keine Datei angegeben!");
            return;
        } else {
            String file = cboGenHashFromFile.getSelValue().trim();
            if (!HashFactory.checkFile(file)) {
                return;
            }
            progData.worker.fileHash_createHashFile(file, txtHash.textProperty());
        }
    }

    private void readHash() {
        txtHash.clear();
        setTxtHashOk(false, false);
        if (cboReadHashFile.getSelValue().trim().isEmpty()) {
            PAlert.showErrorAlert("Hash einlesen", "Es ist keine Datei angegeben!");
            return;
        } else {
            String file = cboReadHashFile.getSelValue().trim();
            if (!HashFactory.checkFile(file)) {
                return;
            }
            progData.worker.fileHash_readHashFile(file, txtHash.textProperty());
        }
    }

    private void saveHash() {
        progData.worker.fileHash_saveHashFile(cboWriteHash.getEditor().getText(), cboGenHashFromFile.getSelValue().trim(), txtHash.getText());
    }
}
