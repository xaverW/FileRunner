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

import de.p2tools.fileRunner.controller.config.ProgConfig;
import de.p2tools.fileRunner.controller.config.ProgData;
import de.p2tools.fileRunner.controller.data.Icons;
import de.p2tools.fileRunner.controller.data.fileData.FileDataList;
import de.p2tools.fileRunner.controller.data.projectData.ProjectData;
import de.p2tools.fileRunner.gui.dialog.MTAlert;
import de.p2tools.fileRunner.gui.tools.Table;
import de.p2tools.p2Lib.tools.DirFileChooser;
import de.p2tools.p2Lib.tools.PAlert;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.util.StringConverter;

import java.io.File;

public class GuiFileRunner extends AnchorPane {

    private final SplitPane splitPane = new SplitPane();

    private final VBox vBox1 = new VBox(10);
    private final VBox vBox2 = new VBox(10);
    private final VBox vBoxBtn = new VBox(10);

    private final ScrollPane scrollPane1 = new ScrollPane();
    private final ScrollPane scrollPane2 = new ScrollPane();
    private final TableView table1 = new TableView();
    private final TableView table2 = new TableView();

    private final ComboBox<ProjectData> cbDir1 = new ComboBox<>();
    private final TextField txtDir2 = new TextField("");
    private final TextField txtHash1 = new TextField("");
    private final TextField txtHash2 = new TextField("");
    private final TextField txtWriteHash1 = new TextField("");
    private final TextField txtWriteHash2 = new TextField("");

    private final RadioButton rb1Dir = new RadioButton("");
    private final RadioButton rb1Hash = new RadioButton("");

    private final RadioButton rb2Dir = new RadioButton("");
    private final RadioButton rb2Hash = new RadioButton("");

    private final Button btnDir1 = new Button("");
    private final Button btnDir2 = new Button("");
    private final Button btnhash1 = new Button("");
    private final Button btnhash2 = new Button("");
    private final Button btnDirWriteHash1 = new Button();
    private final Button btnDirWriteHash2 = new Button();
    private final Button btnWriteHash1 = new Button("Liste in Datei schreiben");
    private final Button btnWriteHash2 = new Button("Liste in Datei schreiben");

    private final Button btnRead1 = new Button("Verzeichnis lesen");
    private final Button btnRead2 = new Button("Verzeichnis lesen");

    private final Button btnAll = new Button("");
    private final Button btnSame = new Button("");
    private final Button btnOnly1 = new Button("");
    private final Button btnOnly2 = new Button("");

    double orgX, orgDiv0, orgDiv1, orgSize;

    private ProjectData projectData = null;
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
        initTable();
        initData();
        addListener();
        selectProjectData();
    }

    public void isShown() {
    }

    private void initCont() {
        ToggleGroup tg = new ToggleGroup();
        rb1Dir.setSelected(true);
        rb2Dir.setSelected(true);
        tg.getToggles().addAll(rb1Dir, rb1Hash);
        tg = new ToggleGroup();
        tg.getToggles().addAll(rb2Dir, rb2Hash);

        btnDir1.setGraphic(new Icons().ICON_BUTTON_FILE_OPEN);
        btnDir2.setGraphic(new Icons().ICON_BUTTON_FILE_OPEN);
        btnhash1.setGraphic(new Icons().ICON_BUTTON_FILE_OPEN);
        btnhash2.setGraphic(new Icons().ICON_BUTTON_FILE_OPEN);
        btnDirWriteHash1.setGraphic(new Icons().ICON_BUTTON_FILE_OPEN);
        btnDirWriteHash2.setGraphic(new Icons().ICON_BUTTON_FILE_OPEN);

        btnDir1.setOnAction(event -> {
            String srcDir1 = DirFileChooser.DirChooser(ProgData.getInstance().primaryStage, projectData.getSrcDir1());
            ProjectData pd = getProjectData(srcDir1);
            cbDir1.getSelectionModel().select(pd);
        });

        btnDir2.setOnAction(event -> DirFileChooser.DirChooser(ProgData.getInstance().primaryStage, txtDir2));
        btnhash1.setOnAction(event -> DirFileChooser.FileChooser(ProgData.getInstance().primaryStage, txtHash1));
        btnhash2.setOnAction(event -> DirFileChooser.FileChooser(ProgData.getInstance().primaryStage, txtHash2));
        btnDirWriteHash1.setOnAction(event -> DirFileChooser.FileChooser(ProgData.getInstance().primaryStage, txtWriteHash1));
        btnDirWriteHash2.setOnAction(event -> DirFileChooser.FileChooser(ProgData.getInstance().primaryStage, txtWriteHash1));

        HBox hBoxDir1 = new HBox(10);
        hBoxDir1.getChildren().addAll(rb1Dir, cbDir1, btnDir1);
        HBox hBoxDir2 = new HBox(10);
        hBoxDir2.getChildren().addAll(rb2Dir, txtDir2, btnDir2);

        HBox hBoxDir1Hash = new HBox(10);
        hBoxDir1Hash.getChildren().addAll(rb1Hash, txtHash1, btnhash1);
        HBox hBoxDir2Hash = new HBox(10);
        hBoxDir2Hash.getChildren().addAll(rb2Hash, txtHash2, btnhash2);

        HBox hBoxWriteHash1 = new HBox(10);
        hBoxWriteHash1.getChildren().addAll(txtWriteHash1, btnDirWriteHash1);

        HBox hBoxWriteHash2 = new HBox(10);
        hBoxWriteHash2.getChildren().addAll(txtWriteHash2, btnDirWriteHash2);

        HBox hBoxRead1 = new HBox(10);
        hBoxRead1.getChildren().addAll(btnRead1);
        HBox hBoxRead2 = new HBox(10);
        hBoxRead2.getChildren().addAll(btnRead2);
        hBoxRead1.setAlignment(Pos.CENTER_RIGHT);
        hBoxRead2.setAlignment(Pos.CENTER_RIGHT);

        HBox hBoxWrite1 = new HBox(10);
        hBoxWrite1.getChildren().add(btnWriteHash1);
        HBox hBoxWrite2 = new HBox(10);
        hBoxWrite2.getChildren().add(btnWriteHash2);
        hBoxWrite1.setAlignment(Pos.CENTER_RIGHT);
        hBoxWrite2.setAlignment(Pos.CENTER_RIGHT);

        cbDir1.setMaxWidth(Double.MAX_VALUE);
        txtDir2.setMaxWidth(Double.MAX_VALUE);
        txtHash1.setMaxWidth(Double.MAX_VALUE);
        txtHash2.setMaxWidth(Double.MAX_VALUE);
        txtWriteHash1.setMaxWidth(Double.MAX_VALUE);
        txtWriteHash2.setMaxWidth(Double.MAX_VALUE);

        HBox.setHgrow(cbDir1, Priority.ALWAYS);
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
                new Label("Hashdatei"), hBoxDir1Hash,
                hBoxRead1,
                scrollPane1,
                new Label("Hashdatei schreiben"), hBoxWriteHash1, hBoxWrite1);

        vBox2.getChildren().addAll(new Label("Verzeichnis 2"), hBoxDir2,
                new Label("Hashdatei"), hBoxDir2Hash,
                hBoxRead2,
                scrollPane2,
                new Label("Hashdatei schreiben"), hBoxWriteHash2, hBoxWrite2);

        vBoxBtn.setStyle("-fx-border-color: blue;");
        vBoxBtn.setAlignment(Pos.CENTER);
        vBoxBtn.setPadding(new Insets(10));

        btnAll.setGraphic(new Icons().ICON_BUTTON_GUI_ALL);
        btnAll.setTooltip(new Tooltip("Alle Dateien anzeigen."));
        btnSame.setGraphic(new Icons().ICON_BUTTON_GUI_SAME);
        btnSame.setTooltip(new Tooltip("Dateien suchen, die in beiden Listen sind."));
        btnOnly1.setGraphic(new Icons().ICON_BUTTON_GUI_ONLY_1);
        btnOnly1.setTooltip(new Tooltip("Dateien suchen, die nur in der Liste 1 sind."));
        btnOnly2.setGraphic(new Icons().ICON_BUTTON_GUI_ONLY_2);
        btnOnly2.setTooltip(new Tooltip("Dateien suchen, die nur in Liste 2 sind."));
        vBoxBtn.getChildren().addAll(btnAll, btnSame, btnOnly1, btnOnly2);

        SplitPane.setResizableWithParent(vBoxBtn, Boolean.FALSE);
        splitPane.getItems().addAll(vBox1, vBoxBtn, vBox2);

        vBoxBtn.setOnMousePressed(e -> {
            orgX = e.getSceneX();
            orgDiv0 = splitPane.getDividers().get(0).getPosition();
            orgDiv1 = splitPane.getDividers().get(1).getPosition();
            orgSize = splitPane.getWidth();
        });

        vBoxBtn.setOnMouseDragged(e -> {
            double offsetX = e.getSceneX() - orgX;
            double move = offsetX / orgSize;

            double ddiv0 = orgDiv0 + move;
            double ddiv1 = orgDiv1 + move;

            splitPane.getDividers().get(0).setPosition(ddiv0);
            splitPane.getDividers().get(1).setPosition(ddiv1);

        });
    }

    private void initTable() {
        new Table().setTable(table1, Table.TABLE.FILELIST1);
        new Table().setTable(table2, Table.TABLE.FILELIST2);

        table1.setItems(progData.fileDataList1.getSortedFileData());
        table2.setItems(progData.fileDataList2.getSortedFileData());
    }

    private void initData() {
        cbDir1.setItems(progData.projectDataList);
        try {
            String srcDir1 = ProgConfig.GUI_FILERUNNER_DIR1.get();
            ProjectData projectData = getProjectData(srcDir1);
            cbDir1.getSelectionModel().select(projectData);
        } catch (Exception ex) {
            cbDir1.getSelectionModel().selectFirst();
            System.out.println("Fehler!!!!!!!");
        }

        final StringConverter<ProjectData> converter = new StringConverter<ProjectData>() {
            @Override
            public String toString(ProjectData pd) {
                return pd == null ? "" : pd.getSrcDir1();
            }

            @Override
            public ProjectData fromString(String id) {
                final int i = cbDir1.getSelectionModel().getSelectedIndex();
                return progData.projectDataList.get(i);
            }
        };

        cbDir1.setConverter(converter);
    }

    private void addListener() {
        cbDir1.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) ->
                selectProjectData()
        );

        btnRead1.setOnAction(e -> {
            if (rb1Dir.isSelected()) {
                readDirHash(true);
            } else {
                readHashFile(true);
            }
        });
        btnRead2.setOnAction(e -> {
            if (rb1Dir.isSelected()) {
                readDirHash(false);
            } else {
                readHashFile(false);
            }
        });

        btnAll.setOnAction(e -> {
        });
        btnSame.setOnAction(e -> {
        });
        btnOnly1.setOnAction(e -> {
        });
        btnOnly2.setOnAction(e -> {
        });
        btnWriteHash1.setOnAction(e -> {
            writeHashFile(true);
        });
        btnWriteHash2.setOnAction(event -> {
            writeHashFile(false);
        });
    }

    private void selectProjectData() {
        unBindProjectDate();

        projectData = cbDir1.getSelectionModel().getSelectedItem();
        if (projectData == null) {
            projectData = new ProjectData();
            progData.projectDataList.addFirst(projectData);
        }

        ProgConfig.GUI_FILERUNNER_DIR1.setValue(projectData.getSrcDir1());
        bindProjectDate();
    }

    private void bindProjectDate() {
        if (projectData != null) {
            txtDir2.textProperty().bindBidirectional(projectData.srcDir2Property());
            txtHash1.textProperty().bindBidirectional(projectData.srcHash1Property());
            txtHash2.textProperty().bindBidirectional(projectData.srcHash2Property());
            txtWriteHash1.textProperty().bindBidirectional(projectData.writeHash1Property());
            txtWriteHash2.textProperty().bindBidirectional(projectData.writeHash2Property());
        }
    }

    private void unBindProjectDate() {
        if (projectData != null) {
            txtDir2.textProperty().unbindBidirectional(projectData.srcDir2Property());
            txtHash1.textProperty().unbindBidirectional(projectData.srcHash1Property());
            txtHash2.textProperty().unbindBidirectional(projectData.srcHash2Property());
            txtWriteHash1.textProperty().unbindBidirectional(projectData.writeHash1Property());
            txtWriteHash2.textProperty().unbindBidirectional(projectData.writeHash2Property());
        }
    }

    private ProjectData getProjectData(String srcDir1) {
        ProjectData pd;
        if (srcDir1.trim().isEmpty() && !progData.projectDataList.isEmpty()) {
            pd = progData.projectDataList.get(0);
        } else {
            pd = progData.projectDataList.getProjectDate(srcDir1);
        }

        if (pd == null) {
            pd = new ProjectData(srcDir1, projectData);
            progData.projectDataList.addFirst(pd);
        }

        return pd;
    }

    public void saveTable() {
        new Table().saveTable(table1, Table.TABLE.FILELIST1);
        new Table().saveTable(table2, Table.TABLE.FILELIST2);
    }

    private void readDirHash(boolean dir1) {
        String hashDir = dir1 ? projectData.getSrcDir1() : projectData.getSrcDir2();
        FileDataList fileDataList = dir1 ? progData.fileDataList1 : progData.fileDataList2;

        if (hashDir.isEmpty()) {
            return;
        }
        File dir = new File(hashDir);
        if (!dir.exists()) {
            MTAlert.showErrorAlert("Verzeichnis einlesen", "Verzeichnis1 existiert nicht!");
        } else {
            progData.worker.readDirHash(dir, fileDataList, 1, true);
        }
    }

    private void readHashFile(boolean hashFile1) {
        String hashFile = hashFile1 ? projectData.getSrcHash1() : projectData.getSrcHash2();
        FileDataList fileDataList = hashFile1 ? progData.fileDataList1 : progData.fileDataList2;

        if (hashFile.isEmpty()) {
            return;
        }

        File file = new File(hashFile);
        if (!file.exists()) {
            MTAlert.showErrorAlert("Hashdatei einlesen", "Die Hashdatei existiert nicht!");
        } else {
            progData.worker.readHashFile(file, fileDataList);
        }

    }

    private void writeHashFile(boolean hash1) {
        File file;
        String str;
        FileDataList fileDataList;
        if (hash1) {
            str = txtWriteHash1.getText().trim();
            fileDataList = progData.fileDataList1;
        } else {
            str = txtWriteHash2.getText().trim();
            fileDataList = progData.fileDataList2;
        }

        if (str.isEmpty()) {
            return;
        }
        file = new File(str);
        if (file.exists()) {
            PAlert.BUTTON btn = MTAlert.showAlert_yes_no("Datei existiert bereits!", "Überschreiben",
                    "Hashdatei existiert bereits, überschreiben?");
            if (btn.equals(PAlert.BUTTON.NO)) {
                return;
            }
        }
        progData.worker.writeHash(file, fileDataList);
    }
}
