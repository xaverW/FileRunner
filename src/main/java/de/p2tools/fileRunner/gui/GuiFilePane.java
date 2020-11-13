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

import de.p2tools.fileRunner.controller.RunEvent;
import de.p2tools.fileRunner.controller.RunListener;
import de.p2tools.fileRunner.controller.config.ProgConfig;
import de.p2tools.fileRunner.controller.config.ProgData;
import de.p2tools.fileRunner.controller.data.Icons;
import de.p2tools.fileRunner.controller.data.projectData.ProjectData;
import de.p2tools.fileRunner.controller.worker.HashFactory;
import de.p2tools.p2Lib.alert.PAlert;
import de.p2tools.p2Lib.dialogs.PDirFileChooser;
import de.p2tools.p2Lib.guiTools.PColumnConstraints;
import de.p2tools.p2Lib.guiTools.PComboBoxString;
import de.p2tools.p2Lib.guiTools.PTextField;
import de.p2tools.p2Lib.tools.net.PUrlTools;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.StringProperty;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

public class GuiFilePane extends VBox {

    private final Button btnGetFile = new Button("");
    private final Button btnGetHashFile = new Button("");
    private final RadioButton rbFileUrl = new RadioButton("Datei/URL");
    private final RadioButton rbHashFile = new RadioButton("Hashdatei");
    private final RadioButton rbHash = new RadioButton("Hash");

    private final PComboBoxString pCboFile = new PComboBoxString();
    private final PComboBoxString pCboHashFile = new PComboBoxString();
    private final PTextField txtHash = new PTextField();

    private final Button btnGenHash = new Button("Hash erstellen");
    private final Button btnSaveHash = new Button("Hash speichern");
    private final String textGenHash = "Hash erstellen";
    private final String textLoadHash = "Hash einlesen";

    private final IntegerProperty sel;
    private final StringProperty srcFile;
    private final StringProperty hashFile;
    private final StringProperty hash;
    private final BooleanProperty isRunning = new SimpleBooleanProperty(false);
    private final BooleanProperty compareNot = new SimpleBooleanProperty(false);

    private final ProjectData projectData;
    private final ProgData progData;

    public GuiFilePane(ProgData progData, boolean panel1) {
        this.progData = progData;
        this.projectData = progData.projectData;

        if (panel1) {
            sel = projectData.compFileSel1Property();
            srcFile = projectData.compFileSrcFile1Property();
            hashFile = projectData.compFileHashFile1Property();
            hash = projectData.compFileHash1Property();
        } else {
            sel = projectData.compFileSel2Property();
            srcFile = projectData.compFileSrcFile2Property();
            hashFile = projectData.compFileHashFile2Property();
            hash = projectData.compFileHash2Property();
        }

        initProjectData();
        initPane();
        addListener();
        setVis(false);
    }

    public boolean getCompareNot() {
        return compareNot.get();
    }

    public BooleanProperty compareNotProperty() {
        return compareNot;
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
        pCboFile.init(projectData.getCompFileSrcFileList(), srcFile);
        pCboHashFile.init(projectData.getCompFileHashFileList(), hashFile);
        txtHash.textProperty().bindBidirectional(hash);
    }

    private void initPane() {
        ToggleGroup tg = new ToggleGroup();
        tg.getToggles().addAll(rbFileUrl, rbHashFile, rbHash);
        switch (sel.get()) {
            case 0:
                rbFileUrl.setSelected(true);
                break;
            case 1:
                rbHashFile.setSelected(true);
                break;
            case 2:
                rbHash.setSelected(true);
                break;
        }

        btnGetFile.setGraphic(new Icons().ICON_BUTTON_FILE_OPEN);
        btnGetFile.setTooltip(new Tooltip("Datei zum Erstellen des Hash auswählen."));
        btnGetHashFile.setGraphic(new Icons().ICON_BUTTON_FILE_OPEN);
        btnGetHashFile.setTooltip(new Tooltip("Datei mit Hash-Werten auswählen."));
        btnSaveHash.setTooltip(new Tooltip("Hash der Datei 2 speichern."));

        pCboFile.setMaxWidth(Double.MAX_VALUE);
        pCboHashFile.setMaxWidth(Double.MAX_VALUE);

        int r = 0;
        GridPane gridPane = new GridPane();
        gridPane.getStyleClass().add("pane-border");
        gridPane.setPadding(new Insets(10));
        gridPane.setVgap(10);
        gridPane.setHgap(10);
        VBox.setVgrow(gridPane, Priority.ALWAYS);

        gridPane.add(rbFileUrl, 0, r);
        gridPane.add(pCboFile, 1, r);
        gridPane.add(btnGetFile, 2, r++);

        gridPane.add(rbHashFile, 0, r);
        gridPane.add(pCboHashFile, 1, r);
        gridPane.add(btnGetHashFile, 2, r++);

        gridPane.add(rbHash, 0, r);
        gridPane.add(txtHash, 1, r++);

        HBox hBox = new HBox(10);
        hBox.setAlignment(Pos.CENTER_RIGHT);
        hBox.getChildren().addAll(btnGenHash, btnSaveHash);
        gridPane.add(hBox, 1, ++r);

        gridPane.getColumnConstraints().addAll(PColumnConstraints.getCcPrefSize(),
                PColumnConstraints.getCcComputedSizeAndHgrow(),
                PColumnConstraints.getCcPrefSize());

        getChildren().addAll(gridPane);
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
        rbFileUrl.setOnAction(event -> setVis(true));
        rbHashFile.setOnAction(event -> setVis(true));
        rbHash.setOnAction(event -> setVis(true));

        btnGetFile.setOnAction(event -> PDirFileChooser.FileChooser(progData.primaryStage, pCboFile));
        btnGetHashFile.setOnAction(event -> PDirFileChooser.FileChooser(progData.primaryStage, pCboHashFile));
        btnGenHash.setOnAction(event -> genLoadHash());
        btnSaveHash.setOnAction(event -> saveHash());

        btnGenHash.disableProperty().bind(
                rbFileUrl.selectedProperty().and(pCboFile.getEditor().textProperty().isEqualTo(""))
                        .or(rbHashFile.selectedProperty().and(pCboHashFile.getEditor().textProperty().isEqualTo("")))
                        .or(rbHash.selectedProperty())
                        .or(isRunning)
        );
        compareNot.bind(btnGenHash.disableProperty());

        btnSaveHash.disableProperty().bind(
                rbFileUrl.selectedProperty().and(pCboFile.getEditor().textProperty().isEqualTo(""))
                        .or(rbHashFile.selectedProperty())
                        .or(rbHash.selectedProperty())
                        .or(txtHash.textProperty().isEmpty()
                                .or(isRunning))
        );
    }

    private void setVis(boolean clearHash) {
        if (clearHash) {
            txtHash.setText("");
        }

        pCboFile.setDisable(rbFileUrl.isSelected() ? false : true);
        pCboHashFile.setDisable(rbHashFile.isSelected() ? false : true);
        txtHash.setStateLabel(rbHash.isSelected() ? false : true);

        if (rbFileUrl.isSelected()) {
            btnGenHash.setText(textGenHash);
            sel.set(0);
        } else if (rbHashFile.isSelected()) {
            btnGenHash.setText(textLoadHash);
            sel.set(1);
        } else if (rbHash.isSelected()) {
            sel.set(2);
//            txtHash.setText("");
        }

    }

    public boolean genLoadHash() {
        if (rbFileUrl.isSelected()) {
            genHash();
            return true;
        } else if (rbHashFile.isSelected()) {
            readHash();
            return true;
        } else {
            return false;
        }
    }

    private void genHash() {
        txtHash.clear();
        if (!rbFileUrl.isSelected()) {
            return;
        }

        if (pCboFile.getSelValue().trim().isEmpty()) {
            PAlert.showErrorAlert("Hash erstellen", "Es ist keine Datei angegeben!");
            return;
        } else {
            String file = pCboFile.getSelValue().trim();
            if (!HashFactory.checkFile(file)) {
                return;
            }
            progData.worker.createFileHash(file, txtHash.textProperty());
        }
    }

    private void readHash() {
        txtHash.clear();
        if (!rbHashFile.isSelected()) {
            return;
        }

        if (pCboHashFile.getSelValue().trim().isEmpty()) {
            PAlert.showErrorAlert("Hash einlesen", "Es ist keine Datei angegeben!");
            return;
        } else {
            String file = pCboHashFile.getSelValue().trim();
            if (!HashFactory.checkFile(file)) {
                return;
            }
            progData.worker.readHashFile(file, txtHash.textProperty());
        }
    }

    private void saveHash() {
        if (!rbFileUrl.isSelected()) {
            return;
        }

        if (pCboFile.getSelValue().trim().isEmpty() || txtHash.getText().isEmpty()) {
            return;
        }

        String src = pCboFile.getSelValue().trim();
        Path srcFile = Paths.get(src); // ist die Datei aus der der md5 generiert wird
        String initDirStr = ""; // ist der Vorschlag: Ordner
        String initMd5FileStr = ""; // ist der Vorschlag: Dateiname
        String srcFileStr = ""; // wird in die md5-Datei geschrieben

        if (!HashFactory.checkFile(src)) {
            initMd5FileStr = src + "." + ProgConfig.GUI_FILE_HASH_SUFF.get();
            srcFileStr = src;
        } else {
            initDirStr = srcFile.getParent().toString();
            initMd5FileStr = srcFile.getFileName().toString() + "." + ProgConfig.GUI_FILE_HASH_SUFF.get();
            srcFileStr = srcFile.getFileName().toString();
        }

        if (PUrlTools.isUrl(pCboFile.getSelValue().trim())) {
            initDirStr = ""; // bei URLs gibts keinen Pfad
        }

        String md5FileStr = PDirFileChooser.FileChooserSave(ProgData.getInstance().primaryStage, initDirStr, initMd5FileStr).trim();
        if (md5FileStr == null || md5FileStr.isEmpty()) {
            return;
        }

        File md5File = new File(md5FileStr);
        progData.worker.writeFileHash(md5File, srcFileStr, txtHash.getText());
    }
}
