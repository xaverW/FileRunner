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

import de.p2tools.fileRunner.controller.RunEvent;
import de.p2tools.fileRunner.controller.RunListener;
import de.p2tools.fileRunner.controller.config.ProgConfig;
import de.p2tools.fileRunner.controller.config.ProgData;
import de.p2tools.fileRunner.controller.data.Icons;
import de.p2tools.fileRunner.controller.data.fileData.FileDataFilter;
import de.p2tools.fileRunner.controller.data.fileData.FileDataList;
import de.p2tools.fileRunner.controller.data.projectData.ProjectData;
import de.p2tools.fileRunner.gui.dialog.MTAlert;
import de.p2tools.fileRunner.gui.tools.Table;
import de.p2tools.p2Lib.tools.DirFileChooser;
import de.p2tools.p2Lib.tools.PAlert;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
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
    private final TextField txtSearch1 = new TextField();
    private final TextField txtSearch2 = new TextField();

    private final Button btnSetDir1 = new Button("");
    private final Button btnSetDir2 = new Button("");
    private final Button btnSetHash1 = new Button("");
    private final Button btnSetHash2 = new Button("");
    private final Button btnSetWriteHash1 = new Button();
    private final Button btnSetWriteHash2 = new Button();

    private final Button btnReadDir1 = new Button("Lesen");
    private final Button btnReadHash1 = new Button("Lesen");
    private final Button btnReadDir2 = new Button("Lesen");
    private final Button btnReadHash2 = new Button("Lesen");
    private final Button btnWriteHash1 = new Button("Liste in Datei schreiben");
    private final Button btnWriteHash2 = new Button("Liste in Datei schreiben");

    private final Button btnShowAll = new Button("");
    private final Button btnShowSame = new Button("");
    private final Button btnShowDiff = new Button("");
    private final Button btnShowOnly1 = new Button("");
    private final Button btnShowOnly2 = new Button("");

    private final TabPane tabPane1 = new TabPane();
    private final Tab tabDir1 = new Tab("Ordner");
    private final Tab tabFile1 = new Tab("Hashdatei");
    private final Tab tabFilter1 = new Tab("Suchen");

    private final TabPane tabPane2 = new TabPane();
    private final Tab tabDir2 = new Tab("Ordner");
    private final Tab tabFile2 = new Tab("Hashdatei");
    private final Tab tabFilter2 = new Tab("Suchen");

    double orgX, orgDiv0, orgDiv1, orgSize;

    private ProjectData projectData = null;
    private final ProgData progData;
    private final FileDataFilter fileDataFilter1 = new FileDataFilter();
    private final FileDataFilter fileDataFilter2 = new FileDataFilter();

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
        btnSetDir1.setGraphic(new Icons().ICON_BUTTON_FILE_OPEN);
        btnSetDir2.setGraphic(new Icons().ICON_BUTTON_FILE_OPEN);
        btnSetHash1.setGraphic(new Icons().ICON_BUTTON_FILE_OPEN);
        btnSetHash2.setGraphic(new Icons().ICON_BUTTON_FILE_OPEN);
        btnSetWriteHash1.setGraphic(new Icons().ICON_BUTTON_FILE_OPEN);
        btnSetWriteHash2.setGraphic(new Icons().ICON_BUTTON_FILE_OPEN);


        // dir1
        VBox vBoxDir1 = new VBox(10);
        vBoxDir1.setPadding(new Insets(10));

        Label lblDir1 = new Label("Verzeichnis 1");
        lblDir1.setMaxWidth(Double.MAX_VALUE);
        HBox hBoxDirText = new HBox(10);
        HBox.setHgrow(lblDir1, Priority.ALWAYS);
        hBoxDirText.getChildren().addAll(lblDir1);

        // hash1
        HBox hBoxDir1 = new HBox(10);
        HBox.setHgrow(cbDir1, Priority.ALWAYS);
        hBoxDir1.getChildren().addAll(cbDir1, btnSetDir1, btnReadDir1);

        vBoxDir1.getChildren().addAll(hBoxDirText, hBoxDir1);

        VBox vBoxFile1 = new VBox(10);
        vBoxFile1.setPadding(new Insets(10));
        HBox hBoxFile1 = new HBox(10);
        HBox.setHgrow(txtHash1, Priority.ALWAYS);
        hBoxFile1.getChildren().addAll(txtHash1, btnSetHash1, btnReadHash1);
        vBoxFile1.getChildren().addAll(new Label("Hashdatei"), hBoxFile1);

        // filter1
        HBox hBoxSearch1 = new HBox(10);
        hBoxSearch1.setPadding(new Insets(10));
        hBoxSearch1.setAlignment(Pos.CENTER_LEFT);
        HBox.setHgrow(txtSearch1, Priority.ALWAYS);
        hBoxSearch1.getChildren().addAll(new Label("Dateien suchen"), txtSearch1);

        // Tabpane1
        tabDir1.setClosable(false);
        tabDir1.setContent(vBoxDir1);

        tabFile1.setClosable(false);
        tabFile1.setContent(vBoxFile1);

        tabFilter1.setClosable(false);
        tabFilter1.setContent(hBoxSearch1);

        tabPane1.getTabs().addAll(tabDir1, tabFile1, tabFilter1);


        // dir1
        VBox vBoxDir2 = new VBox(10);
        vBoxDir2.setPadding(new Insets(10));

        Label lblDir2 = new Label("Verzeichnis 1");
        lblDir2.setMaxWidth(Double.MAX_VALUE);
        HBox hBoxDirText2 = new HBox(10);
        HBox.setHgrow(lblDir2, Priority.ALWAYS);
        hBoxDirText2.getChildren().addAll(lblDir2);

        // hash1
        HBox hBoxDir2 = new HBox(10);
        HBox.setHgrow(txtDir2, Priority.ALWAYS);
        hBoxDir2.getChildren().addAll(txtDir2, btnSetDir2, btnReadDir2);

        vBoxDir2.getChildren().addAll(hBoxDirText2, hBoxDir2);

        VBox vBoxFile2 = new VBox(10);
        vBoxFile2.setPadding(new Insets(10));
        HBox hBoxFile2 = new HBox(10);
        HBox.setHgrow(txtHash2, Priority.ALWAYS);
        hBoxFile2.getChildren().addAll(txtHash2, btnSetHash2, btnReadHash2);
        vBoxFile2.getChildren().addAll(new Label("Hashdatei"), hBoxFile2);

        // filter1
        HBox hBoxSearch2 = new HBox(10);
        hBoxSearch2.setPadding(new Insets(10));
        hBoxSearch2.setAlignment(Pos.CENTER_LEFT);
        HBox.setHgrow(txtSearch2, Priority.ALWAYS);
        hBoxSearch2.setPadding(new Insets(10));
        hBoxSearch2.getChildren().addAll(new Label("Dateien suchen"), txtSearch2);

        // Tabpane1
        tabDir2.setClosable(false);
        tabDir2.setContent(vBoxDir2);

        tabFile2.setClosable(false);
        tabFile2.setContent(vBoxFile2);

        tabFilter2.setClosable(false);
        tabFilter2.setContent(hBoxSearch2);

        tabPane2.getTabs().addAll(tabDir2, tabFile2, tabFilter2);

        // write hash
        HBox hBoxWriteHash1 = new HBox(10);
        hBoxWriteHash1.getChildren().addAll(txtWriteHash1, btnSetWriteHash1);
        HBox hBoxWrite1 = new HBox(10);
        hBoxWrite1.getChildren().add(btnWriteHash1);
        hBoxWrite1.setAlignment(Pos.CENTER_RIGHT);

        HBox hBoxWriteHash2 = new HBox(10);
        hBoxWriteHash2.getChildren().addAll(txtWriteHash2, btnSetWriteHash2);
        HBox hBoxWrite2 = new HBox(10);
        hBoxWrite2.getChildren().add(btnWriteHash2);
        hBoxWrite2.setAlignment(Pos.CENTER_RIGHT);

        cbDir1.setMaxWidth(Double.MAX_VALUE);
        txtDir2.setMaxWidth(Double.MAX_VALUE);
        txtHash1.setMaxWidth(Double.MAX_VALUE);
        txtHash2.setMaxWidth(Double.MAX_VALUE);
        txtWriteHash1.setMaxWidth(Double.MAX_VALUE);
        txtWriteHash2.setMaxWidth(Double.MAX_VALUE);


        GridPane.setHgrow(cbDir1, Priority.ALWAYS);
        GridPane.setHgrow(txtDir2, Priority.ALWAYS);
        GridPane.setHgrow(txtHash1, Priority.ALWAYS);
        GridPane.setHgrow(txtHash2, Priority.ALWAYS);
        HBox.setHgrow(txtWriteHash1, Priority.ALWAYS);
        HBox.setHgrow(txtWriteHash2, Priority.ALWAYS);

        VBox.setVgrow(scrollPane1, Priority.ALWAYS);
        VBox.setVgrow(scrollPane2, Priority.ALWAYS);


        vBox1.setPadding(new Insets(10));
        vBox1.getChildren().addAll(tabPane1, scrollPane1,
                new Label("Hashdatei schreiben"), hBoxWriteHash1, hBoxWrite1);

        vBox2.setPadding(new Insets(10));
        vBox2.getChildren().addAll(tabPane2, scrollPane2,
                new Label("Hashdatei schreiben"), hBoxWriteHash2, hBoxWrite2);

        vBoxBtn.setStyle("-fx-border-color: blue;");
        vBoxBtn.setAlignment(Pos.CENTER);
        vBoxBtn.setPadding(new Insets(10));

        btnShowAll.setGraphic(new Icons().ICON_BUTTON_GUI_ALL);
        btnShowAll.setTooltip(new Tooltip("Alle Dateien anzeigen."));
        btnShowSame.setGraphic(new Icons().ICON_BUTTON_GUI_SAME);
        btnShowSame.setTooltip(new Tooltip("Dateien suchen, die in beiden Listen sind und gleich sind."));
        btnShowDiff.setGraphic(new Icons().ICON_BUTTON_GUI_DIFF);
        btnShowDiff.setTooltip(new Tooltip("Dateien suchen, die in beiden Listen sind und sich unterscheiden."));
        btnShowOnly1.setGraphic(new Icons().ICON_BUTTON_GUI_ONLY_1);
        btnShowOnly1.setTooltip(new Tooltip("Dateien suchen, die nur in der Liste 1 sind."));
        btnShowOnly2.setGraphic(new Icons().ICON_BUTTON_GUI_ONLY_2);
        btnShowOnly2.setTooltip(new Tooltip("Dateien suchen, die nur in Liste 2 sind."));
        vBoxBtn.getChildren().addAll(btnShowAll, btnShowSame, btnShowDiff, btnShowOnly1, btnShowOnly2);

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

        fileDataFilter1.setName(txtSearch1.getText());
        progData.fileDataList1.setPred(fileDataFilter1);

        fileDataFilter2.setName(txtSearch2.getText());
        progData.fileDataList2.setPred(fileDataFilter2);

        progData.fileDataList1.getSortedFileData().comparatorProperty().bind(table1.comparatorProperty());
        progData.fileDataList2.getSortedFileData().comparatorProperty().bind(table2.comparatorProperty());
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
        progData.worker.addAdListener(new RunListener() {
            @Override
            public void ping(RunEvent runEvent) {
                if (runEvent.nixLos()) {
                    System.out.println("Table refresh");
                    table1.refresh();
                    table2.refresh();
                }
            }
        });
        cbDir1.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) ->
                selectProjectData()
        );

        btnSetDir1.setOnAction(event -> {
            String srcDir1 = DirFileChooser.DirChooser(ProgData.getInstance().primaryStage, projectData.getSrcDir1());
            ProjectData pd = getProjectData(srcDir1);
            cbDir1.getSelectionModel().select(pd);
        });
        btnSetDir2.setOnAction(event -> DirFileChooser.DirChooser(ProgData.getInstance().primaryStage, txtDir2));
        btnSetHash1.setOnAction(event -> DirFileChooser.FileChooser(ProgData.getInstance().primaryStage, txtHash1));
        btnSetHash2.setOnAction(event -> DirFileChooser.FileChooser(ProgData.getInstance().primaryStage, txtHash2));
        btnSetWriteHash1.setOnAction(event -> DirFileChooser.FileChooser(ProgData.getInstance().primaryStage, txtWriteHash1));
        btnSetWriteHash2.setOnAction(event -> DirFileChooser.FileChooser(ProgData.getInstance().primaryStage, txtWriteHash1));


        btnReadDir1.setOnAction(a -> readDirHash(projectData.getSrcDir1(), projectData.getSearch1(), progData.fileDataList1));
        btnReadHash1.setOnAction(a -> readHashFile(projectData.getSrcHash1(), projectData.getSearch1(), progData.fileDataList1));
        btnReadDir2.setOnAction(a -> readDirHash(projectData.getSrcDir2(), projectData.getSearch2(), progData.fileDataList2));
        btnReadHash2.setOnAction(a -> readHashFile(projectData.getSrcHash2(), projectData.getSearch2(), progData.fileDataList2));

        btnWriteHash1.setOnAction(e -> {
            writeHashFile(true);
        });
        btnWriteHash2.setOnAction(event -> {
            writeHashFile(false);
        });

        btnShowAll.setOnAction(e -> {
            fileDataFilter1.setAll(true);
            progData.fileDataList1.setPred(fileDataFilter1);

            fileDataFilter2.setAll(true);
            progData.fileDataList2.setPred(fileDataFilter2);
        });
        btnShowSame.setOnAction(e -> {
            fileDataFilter1.setAll(false);
            fileDataFilter1.setDiff(false);
            fileDataFilter1.setOnly(false);
            progData.fileDataList1.setPred(fileDataFilter1);

            fileDataFilter2.setAll(false);
            fileDataFilter2.setDiff(false);
            fileDataFilter2.setOnly(false);
            progData.fileDataList2.setPred(fileDataFilter2);
        });
        btnShowDiff.setOnAction(e -> {
            fileDataFilter1.setAll(false);
            fileDataFilter1.setDiff(true);
            fileDataFilter1.setOnly(false);
            progData.fileDataList1.setPred(fileDataFilter1);

            fileDataFilter2.setAll(false);
            fileDataFilter2.setDiff(true);
            fileDataFilter2.setOnly(false);
            progData.fileDataList2.setPred(fileDataFilter2);
        });
        btnShowOnly1.setOnAction(e -> {
            fileDataFilter1.setAll(false);
            fileDataFilter1.setDiff(false);
            fileDataFilter1.setOnly(true);
            progData.fileDataList1.setPred(fileDataFilter1);

            progData.fileDataList2.setPred(false);
        });
        btnShowOnly2.setOnAction(e -> {
            progData.fileDataList1.setPred(false);

            fileDataFilter2.setAll(false);
            fileDataFilter2.setDiff(false);
            fileDataFilter2.setOnly(true);
            progData.fileDataList2.setPred(fileDataFilter2);
        });
        txtSearch1.textProperty().addListener((observable, oldValue, newValue) -> {
            fileDataFilter1.setAll(true);
            fileDataFilter1.setName(txtSearch1.getText());
            progData.fileDataList1.setPred(fileDataFilter1);
            if (txtSearch1.getText().isEmpty()) {
                tabFilter1.setStyle("-fx-font-weight: normal;");
            } else {
                tabFilter1.setStyle("-fx-font-weight: bold;");
            }
        });
        txtSearch2.textProperty().addListener((observable, oldValue, newValue) -> {
            fileDataFilter2.setAll(true);
            fileDataFilter2.setName(txtSearch2.getText());
            progData.fileDataList2.setPred(fileDataFilter2);
            if (txtSearch2.getText().isEmpty()) {
                tabFilter2.setStyle("-fx-font-weight: normal;");
            } else {
                tabFilter2.setStyle("-fx-font-weight: bold;");
            }
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

            txtSearch1.textProperty().bindBidirectional(projectData.search1Property());
            txtSearch2.textProperty().bindBidirectional(projectData.search2Property());

            txtWriteHash1.textProperty().bindBidirectional(projectData.writeHash1Property());
            txtWriteHash2.textProperty().bindBidirectional(projectData.writeHash2Property());
        }
    }

    private void unBindProjectDate() {
        if (projectData != null) {
            txtDir2.textProperty().unbindBidirectional(projectData.srcDir2Property());
            txtHash1.textProperty().unbindBidirectional(projectData.srcHash1Property());
            txtHash2.textProperty().unbindBidirectional(projectData.srcHash2Property());

            txtSearch1.textProperty().unbindBidirectional(projectData.search1Property());
            txtSearch1.textProperty().unbindBidirectional(projectData.search2Property());

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

    private void readDirHash(String hashDir, String search, FileDataList fileDataList) {
        if (hashDir.isEmpty()) {
            return;
        }
        File dir = new File(hashDir);
        if (!dir.exists()) {
            MTAlert.showErrorAlert("Verzeichnis einlesen", "Verzeichnis1 existiert nicht!");
        } else {
            progData.worker.readDirHash(dir, search, fileDataList, 1, true);
        }
        fileDataFilter1.setAll(true);
        progData.fileDataList1.setPred(fileDataFilter1);
        fileDataFilter2.setAll(true);
        progData.fileDataList2.setPred(fileDataFilter2);
    }

    private void readHashFile(String hashFile, String search, FileDataList fileDataList) {
        if (hashFile.isEmpty()) {
            return;
        }

        File file = new File(hashFile);
        if (!file.exists()) {
            MTAlert.showErrorAlert("Hashdatei einlesen", "Die Hashdatei existiert nicht!");
        } else {
            progData.worker.readHashFile(file, search, fileDataList);
        }
        fileDataFilter1.setAll(true);
        progData.fileDataList1.setPred(fileDataFilter1);
        fileDataFilter2.setAll(true);
        progData.fileDataList2.setPred(fileDataFilter2);
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
