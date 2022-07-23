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


package de.p2tools.fileRunner.gui;

import de.p2tools.fileRunner.controller.config.ProgConfig;
import de.p2tools.fileRunner.controller.config.ProgData;
import de.p2tools.fileRunner.controller.data.ProgIcons;
import de.p2tools.fileRunner.controller.listener.Events;
import de.p2tools.fileRunner.controller.worker.HashFactory;
import de.p2tools.p2Lib.alert.PAlert;
import de.p2tools.p2Lib.dialogs.PDirFileChooser;
import de.p2tools.p2Lib.guiTools.PColumnConstraints;
import de.p2tools.p2Lib.guiTools.PComboBoxString;
import de.p2tools.p2Lib.guiTools.PTextField;
import de.p2tools.p2Lib.tools.events.PListener;
import de.p2tools.p2Lib.tools.events.RunEvent;
import de.p2tools.p2Lib.tools.file.PFileName;
import de.p2tools.p2Lib.tools.net.PUrlTools;
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

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

public class GuiFilePane extends VBox {

    private final Button btnSelectFile = new Button("");
    private final Button btnSelectHashFile = new Button("");
    private final RadioButton rbFileOrUrl = new RadioButton("Datei/URL");
    private final RadioButton rbHashFile = new RadioButton("Hashdatei");
    private final RadioButton rbHash = new RadioButton("Hash");
    private final Button btnProposeStoringHashFileName = new Button("");
    private final PComboBoxString cboWriteHash = new PComboBoxString();
    private final Button btnSelectHashFileForStoring = new Button();

    private final Button btnReadFileAndGenHash = new Button("");
    private final Button btnReadHashFile = new Button("");
    private final Button btnSaveHash = new Button("Hash speichern");

    private final PComboBoxString cboGenHashFromFile = new PComboBoxString();
    private final PComboBoxString cboReadHashFromFile = new PComboBoxString();
    private final PTextField txtHash = new PTextField();

    private final IntegerProperty sel;
    private final StringProperty srcFile;
    private final StringProperty hashFile;
    private final StringProperty hash;
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
            hash = ProgConfig.compFileHash1;
            writeHash = ProgConfig.writeFileHash1;
        } else {
            sel = ProgConfig.compFileSel2;
            srcFile = ProgConfig.compFileSrcFile2;
            hashFile = ProgConfig.compFileHashFile2;
            hash = ProgConfig.compFileHash2;
            writeHash = ProgConfig.writeFileHash2;
        }

        initProjectData();
        initButton();
        initPane();
        addListener();
        setVis(false);
    }

    public BooleanProperty disableCompareButtonProperty() {
        return disableCompareButton;
    }

    public void clearHash() {
        if (!rbHash.isSelected()) {
            //dann ist ein fester Hash vorgegeben, den besser lassen?
            txtHash.setText("");
        }
    }

    public PTextField getTxtHash() {
        return txtHash;
    }

    private void initProjectData() {
        cboGenHashFromFile.init(ProgConfig.compFileSrcFileList, srcFile);
        cboReadHashFromFile.init(ProgConfig.compFileHashFileList, hashFile);
        txtHash.textProperty().bindBidirectional(hash);
    }

    private void initButton() {
        ToggleGroup tg = new ToggleGroup();
        tg.getToggles().addAll(rbFileOrUrl, rbHashFile, rbHash);
        switch (sel.get()) {
            case 0:
                rbFileOrUrl.setSelected(true);
                break;
            case 1:
                rbHashFile.setSelected(true);
                break;
            case 2:
                rbHash.setSelected(true);
                break;
        }

        btnSelectFile.setGraphic(new ProgIcons().ICON_BUTTON_FILE_OPEN);
        btnSelectFile.setTooltip(new Tooltip("Datei zum Erstellen des Hash auswählen."));
        btnSelectHashFile.setGraphic(new ProgIcons().ICON_BUTTON_FILE_OPEN);
        btnSelectHashFile.setTooltip(new Tooltip("Datei mit Hash-Werten auswählen."));
        btnSaveHash.setTooltip(new Tooltip("Hash der Datei 2 speichern."));
        btnSaveHash.disableProperty().bind(cboWriteHash.getEditor().textProperty().isEmpty().or(txtHash.textProperty().isEmpty()));

        cboGenHashFromFile.setMaxWidth(Double.MAX_VALUE);
        cboReadHashFromFile.setMaxWidth(Double.MAX_VALUE);

        btnReadFileAndGenHash.setGraphic(new ProgIcons().ICON_BUTTON_GEN_HASH);
        btnReadFileAndGenHash.setTooltip(new Tooltip("Datei einlesen und Hash erstellen."));
        btnReadFileAndGenHash.disableProperty().bind(cboGenHashFromFile.getEditor().textProperty().isNull()
                .or(cboGenHashFromFile.getEditor().textProperty().isEqualTo("")));
        btnReadFileAndGenHash.disableProperty().bind(
                rbFileOrUrl.selectedProperty().and(cboGenHashFromFile.getEditor().textProperty().isEqualTo(""))
                        .or(rbHashFile.selectedProperty().and(cboReadHashFromFile.getEditor().textProperty().isEqualTo("")))
                        .or(rbHash.selectedProperty())
                        .or(isRunning)
        );

        btnReadHashFile.setGraphic(new ProgIcons().ICON_BUTTON_GEN_HASH);
        btnReadHashFile.setTooltip(new Tooltip("Hash aus Datei lesen."));
        btnReadHashFile.disableProperty().bind(cboReadHashFromFile.getEditor().textProperty().isNull()
                .or(cboReadHashFromFile.getEditor().textProperty().isEqualTo("")));

        btnSelectHashFileForStoring.setGraphic(new ProgIcons().ICON_BUTTON_FILE_OPEN);
        btnSelectHashFileForStoring.setTooltip(new Tooltip("Datei zum Speichern auswählen."));
        btnSelectHashFileForStoring.disableProperty().bind(cboWriteHash.disableProperty());

        btnProposeStoringHashFileName.setGraphic(new ProgIcons().ICON_BUTTON_GUI_GEN_NAME);
        btnProposeStoringHashFileName.setTooltip(new Tooltip("Einen Dateinamen vorschlagen."));
        btnProposeStoringHashFileName.disableProperty().bind(cboWriteHash.disableProperty());

        cboWriteHash.setEditable(true);
        cboWriteHash.setMaxWidth(Double.MAX_VALUE);
        cboWriteHash.init(ProgConfig.writeFileHashList, writeHash);

        cboWriteHash.disableProperty().bind(
                rbFileOrUrl.selectedProperty().and(cboGenHashFromFile.getEditor().textProperty().isEqualTo(""))
                        .or(rbHashFile.selectedProperty())
                        .or(rbHash.selectedProperty())
                        .or(txtHash.textProperty().isEmpty()
                                .or(isRunning)));
    }

    private void initPane() {
        int r = 0;
        GridPane gridPane = new GridPane();
        gridPane.getStyleClass().add("pane-border");
        gridPane.setPadding(new Insets(10));
        gridPane.setVgap(10);
        gridPane.setHgap(10);
        VBox.setVgrow(gridPane, Priority.ALWAYS);

        gridPane.add(rbFileOrUrl, 0, r);
        gridPane.add(cboGenHashFromFile, 1, r);
        gridPane.add(btnSelectFile, 2, r);
        gridPane.add(btnReadFileAndGenHash, 3, r++);

        gridPane.add(rbHashFile, 0, r);
        gridPane.add(cboReadHashFromFile, 1, r);
        gridPane.add(btnSelectHashFile, 2, r);
        gridPane.add(btnReadHashFile, 3, r++);

        gridPane.add(rbHash, 0, r);
        gridPane.add(txtHash, 1, r++);

        ++r;
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
                new PListener(Events.event(Events.COMPARE_OF_FILE_LISTS_FINISHED)) {
                    @Override
                    public void ping(RunEvent runEvent) {
                        if (runEvent.nixLos()) {
                            isRunning.setValue(false);
                        } else {
                            isRunning.setValue(true);
                        }
                    }
                });

        rbFileOrUrl.setOnAction(event -> setVis(true));
        rbFileOrUrl.disableProperty().bind(isRunning);
        rbHashFile.setOnAction(event -> setVis(true));
        rbHashFile.disableProperty().bind(isRunning);
        rbHash.setOnAction(event -> setVis(true));
        rbHash.disableProperty().bind(isRunning);

        btnSelectFile.setOnAction(event -> PDirFileChooser.FileChooser(progData.primaryStage, cboGenHashFromFile));
        btnSelectFile.disableProperty().bind(isRunning);
        btnSelectHashFile.setOnAction(event -> PDirFileChooser.FileChooser(progData.primaryStage, cboReadHashFromFile));
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
        });
        cboReadHashFromFile.getEditor().textProperty().addListener((ob, o, n) -> {
            txtHash.clear();
        });

        disableCompareButton.bind(btnReadFileAndGenHash.disableProperty());
    }

    private void setVis(boolean clearHash) {
        if (clearHash) {
            txtHash.setText("");
        }

        cboGenHashFromFile.setDisable(rbFileOrUrl.isSelected() ? false : true);
        cboReadHashFromFile.setDisable(rbHashFile.isSelected() ? false : true);
        txtHash.setStateLabel(rbHash.isSelected() ? false : true);

        if (rbFileOrUrl.isSelected()) {
            sel.set(0);
        } else if (rbHashFile.isSelected()) {
            sel.set(1);
        } else if (rbHash.isSelected()) {
            sel.set(2);
        }

    }

    public void genLoadHash() {
        if (rbFileOrUrl.isSelected()) {
            genHash();
        } else if (rbHashFile.isSelected()) {
            readHash();
        }
    }

    private void genHash() {
        txtHash.clear();
        if (cboGenHashFromFile.getSelValue().trim().isEmpty()) {
            PAlert.showErrorAlert("Hash erstellen", "Es ist keine Datei angegeben!");
            return;
        } else {
            String file = cboGenHashFromFile.getSelValue().trim();
            if (!HashFactory.checkFile(file)) {
                return;
            }
            progData.worker.createFileHash(file, txtHash.textProperty());
        }
    }

    private void readHash() {
        txtHash.clear();
        if (cboReadHashFromFile.getSelValue().trim().isEmpty()) {
            PAlert.showErrorAlert("Hash einlesen", "Es ist keine Datei angegeben!");
            return;
        } else {
            String file = cboReadHashFromFile.getSelValue().trim();
            if (!HashFactory.checkFile(file)) {
                return;
            }
            progData.worker.readHashFile(file, txtHash.textProperty());
        }
    }

    private void saveHash() {
        if (cboWriteHash.getEditor().getText().isEmpty()) {
            return;
        }

        String src = cboGenHashFromFile.getSelValue().trim();
        Path srcFile = Paths.get(src); // ist die Datei aus der der md5 generiert wird
        String initDirStr = ""; // ist der Vorschlag: Ordner
        String srcFileStr = ""; // wird in die md5-Datei geschrieben

        if (!HashFactory.checkFile(src)) {
            srcFileStr = src;
        } else {
            initDirStr = srcFile.getParent().toString();
            srcFileStr = srcFile.getFileName().toString();
        }

        if (PUrlTools.isUrl(cboGenHashFromFile.getSelValue().trim())) {
            initDirStr = ""; // bei URLs gibts keinen Pfad
        }

        String md5FileStr = cboWriteHash.getEditor().getText();
        md5FileStr = PDirFileChooser.FileChooserSave(ProgData.getInstance().primaryStage, initDirStr, md5FileStr).trim();
        if (md5FileStr == null || md5FileStr.isEmpty()) {
            return;
        }

        File md5File = new File(md5FileStr);
        progData.worker.writeFileHash(md5File, srcFileStr, txtHash.getText());
    }
}
