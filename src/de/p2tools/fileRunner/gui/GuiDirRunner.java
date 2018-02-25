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
import de.p2tools.fileRunner.controller.config.ProgData;
import de.p2tools.fileRunner.controller.data.Icons;
import de.p2tools.fileRunner.controller.data.fileData.FileDataFilter;
import de.p2tools.fileRunner.controller.data.fileData.FileDataList;
import de.p2tools.fileRunner.controller.data.projectData.ProjectData;
import de.p2tools.fileRunner.gui.tools.Table;
import de.p2tools.p2Lib.dialog.DirFileChooser;
import de.p2tools.p2Lib.dialog.PAlert;
import de.p2tools.p2Lib.dialog.PAlertFileChosser;
import de.p2tools.p2Lib.dialog.PComboBox;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;

import java.io.File;

public class GuiDirRunner extends AnchorPane {

    private final SplitPane splitPane = new SplitPane();

    private final VBox vBox1 = new VBox(10);
    private final VBox vBox2 = new VBox(10);
    private final VBox vBoxBtn = new VBox(10);

    private final ScrollPane scrollPane1 = new ScrollPane();
    private final ScrollPane scrollPane2 = new ScrollPane();
    private final TableView table1 = new TableView();
    private final TableView table2 = new TableView();

    private final PComboBox cbDir1 = new PComboBox();
    private final PComboBox cbDir2 = new PComboBox();
    private final PComboBox cbHash1 = new PComboBox();
    private final PComboBox cbHash2 = new PComboBox();
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

    private final ToggleButton btnShowAll = new ToggleButton("");
    private final ToggleButton btnShowSame = new ToggleButton("");
    private final ToggleButton btnShowDiff = new ToggleButton("");
    private final ToggleButton btnShowDiffAll = new ToggleButton("");
    private final ToggleButton btnShowOnly1 = new ToggleButton("");
    private final ToggleButton btnShowOnly2 = new ToggleButton("");

    private final TabPane tabPane1 = new TabPane();
    private final Tab tabDir1 = new Tab("Ordner");
    private final Tab tabFile1 = new Tab("Hashdatei");
    private final Tab tabFilter1 = new Tab("Suchen");

    private final TabPane tabPane2 = new TabPane();
    private final Tab tabDir2 = new Tab("Ordner");
    private final Tab tabFile2 = new Tab("Hashdatei");
    private final Tab tabFilter2 = new Tab("Suchen");

    private double orgX, orgDiv0, orgDiv1, orgSize;

    private ProjectData projectData = null;
    private final ProgData progData;
    private final FileDataFilter fileDataFilter1 = new FileDataFilter();
    private final FileDataFilter fileDataFilter2 = new FileDataFilter();

    public GuiDirRunner() {
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
        initProjectData();
        addListener();
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
        HBox.setHgrow(cbHash1, Priority.ALWAYS);
        hBoxFile1.getChildren().addAll(cbHash1, btnSetHash1, btnReadHash1);
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

        Label lblDir2 = new Label("Verzeichnis 2");
        lblDir2.setMaxWidth(Double.MAX_VALUE);
        HBox hBoxDirText2 = new HBox(10);
        HBox.setHgrow(lblDir2, Priority.ALWAYS);
        hBoxDirText2.getChildren().addAll(lblDir2);

        // hash1
        HBox hBoxDir2 = new HBox(10);
        HBox.setHgrow(cbDir2, Priority.ALWAYS);
        hBoxDir2.getChildren().addAll(cbDir2, btnSetDir2, btnReadDir2);
        vBoxDir2.getChildren().addAll(hBoxDirText2, hBoxDir2);

        VBox vBoxFile2 = new VBox(10);
        vBoxFile2.setPadding(new Insets(10));
        HBox hBoxFile2 = new HBox(10);
        HBox.setHgrow(cbHash2, Priority.ALWAYS);
        hBoxFile2.getChildren().addAll(cbHash2, btnSetHash2, btnReadHash2);
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
        cbDir2.setMaxWidth(Double.MAX_VALUE);
        cbHash1.setMaxWidth(Double.MAX_VALUE);
        cbHash2.setMaxWidth(Double.MAX_VALUE);
        txtWriteHash1.setMaxWidth(Double.MAX_VALUE);
        txtWriteHash2.setMaxWidth(Double.MAX_VALUE);


        GridPane.setHgrow(cbDir1, Priority.ALWAYS);
        GridPane.setHgrow(cbDir2, Priority.ALWAYS);
        GridPane.setHgrow(cbHash1, Priority.ALWAYS);
        GridPane.setHgrow(cbHash2, Priority.ALWAYS);
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

        vBoxBtn.getStyleClass().add("pane-border");
        vBoxBtn.setAlignment(Pos.CENTER);
        vBoxBtn.setPadding(new Insets(10));

        ToggleGroup tg = new ToggleGroup();
        tg.getToggles().addAll(btnShowAll, btnShowSame, btnShowDiff, btnShowDiffAll, btnShowOnly1, btnShowOnly2);
        btnShowAll.setSelected(true);
        btnShowAll.setGraphic(new Icons().ICON_BUTTON_GUI_ALL);
        btnShowAll.setTooltip(new Tooltip("Alle Dateien anzeigen."));
        btnShowSame.setGraphic(new Icons().ICON_BUTTON_GUI_SAME);
        btnShowSame.setTooltip(new Tooltip("Dateien suchen, die in beiden Listen sind und gleich sind."));
        btnShowDiff.setGraphic(new Icons().ICON_BUTTON_GUI_DIFF);
        btnShowDiff.setTooltip(new Tooltip("Dateien suchen, die in beiden Listen enthalten sind und sich unterscheiden."));
        btnShowDiffAll.setGraphic(new Icons().ICON_BUTTON_GUI_DIFF_ALL);
        btnShowDiffAll.setTooltip(new Tooltip("Alle Dateien suchen, die sich unterscheiden."));
        btnShowOnly1.setGraphic(new Icons().ICON_BUTTON_GUI_ONLY_1);
        btnShowOnly1.setTooltip(new Tooltip("Dateien suchen, die nur in Liste 1 sind."));
        btnShowOnly2.setGraphic(new Icons().ICON_BUTTON_GUI_ONLY_2);
        btnShowOnly2.setTooltip(new Tooltip("Dateien suchen, die nur in Liste 2 sind."));
        vBoxBtn.getChildren().addAll(btnShowAll, btnShowSame, btnShowDiff, btnShowDiffAll, btnShowOnly1, btnShowOnly2);

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

        changeTextFilter();

        progData.fileDataList1.getSortedFileData().comparatorProperty().bind(table1.comparatorProperty());
        progData.fileDataList2.getSortedFileData().comparatorProperty().bind(table2.comparatorProperty());
    }

    private void initProjectData() {
        projectData = progData.projectDataList.getFirst();

        cbDir1.init(progData.projectDataList.getFirst().getSrcDirList(),
                projectData.getSrcDir1(), projectData.srcDir1Property());
        cbDir2.init(progData.projectDataList.getFirst().getSrcDirList(),
                projectData.getSrcDir2(), projectData.srcDir2Property());
        cbHash1.init(progData.projectDataList.getFirst().getSrcHashList(),
                projectData.getSrcHash1(), projectData.srcHash1Property());
        cbHash2.init(progData.projectDataList.getFirst().getSrcHashList(),
                projectData.getSrcHash2(), projectData.srcHash2Property());

        bindProjectDate();
    }

    private void addListener() {
        progData.worker.addAdListener(new RunListener() {
            @Override
            public void ping(RunEvent runEvent) {
                if (runEvent.nixLos()) {
//                    System.out.println("Table refresh");
//                    table1.refresh();
//                    table2.refresh();
                }
            }
        });

        btnSetDir1.setOnAction(event -> DirFileChooser.DirChooser(ProgData.getInstance().primaryStage, cbDir1));
        btnSetDir2.setOnAction(event -> DirFileChooser.DirChooser(ProgData.getInstance().primaryStage, cbDir2));
        btnSetHash1.setOnAction(event -> DirFileChooser.FileChooser(ProgData.getInstance().primaryStage, cbHash1));
        btnSetHash2.setOnAction(event -> DirFileChooser.FileChooser(ProgData.getInstance().primaryStage, cbHash2));
        btnSetWriteHash1.setOnAction(event -> DirFileChooser.FileChooser(ProgData.getInstance().primaryStage, txtWriteHash1));
        btnSetWriteHash2.setOnAction(event -> DirFileChooser.FileChooser(ProgData.getInstance().primaryStage, txtWriteHash1));

        btnReadDir1.setOnAction(a -> {
            readDirHash(projectData.getSrcDir1(), progData.fileDataList1);
            changeTextFilter();
        });
        btnReadHash1.setOnAction(a -> {
            readHashFile(projectData.getSrcHash1(), progData.fileDataList1);
            changeTextFilter();
        });
        btnReadDir2.setOnAction(a -> {
            readDirHash(projectData.getSrcDir2(), progData.fileDataList2);
            changeTextFilter();
        });
        btnReadHash2.setOnAction(a -> {
            readHashFile(projectData.getSrcHash2(), progData.fileDataList2);
            changeTextFilter();
        });

        btnWriteHash1.setOnAction(e -> writeHashFile(true));
        btnWriteHash2.setOnAction(event -> writeHashFile(false));

        btnShowAll.setOnAction(e -> {
            fileDataFilter1.setFilter_types(FileDataFilter.FILTER_TYPES.ALL);
            fileDataFilter2.setFilter_types(FileDataFilter.FILTER_TYPES.ALL);
            progData.fileDataList1.setPred(fileDataFilter1);
            progData.fileDataList2.setPred(fileDataFilter2);
        });
        btnShowSame.setOnAction(e -> {
            fileDataFilter1.setFilter_types(FileDataFilter.FILTER_TYPES.SAME);
            fileDataFilter2.setFilter_types(FileDataFilter.FILTER_TYPES.SAME);
            progData.fileDataList1.setPred(fileDataFilter1);
            progData.fileDataList2.setPred(fileDataFilter2);
        });
        btnShowDiff.setOnAction(e -> {
            fileDataFilter1.setFilter_types(FileDataFilter.FILTER_TYPES.DIFF);
            fileDataFilter2.setFilter_types(FileDataFilter.FILTER_TYPES.DIFF);
            progData.fileDataList1.setPred(fileDataFilter1);
            progData.fileDataList2.setPred(fileDataFilter2);
        });
        btnShowDiffAll.setOnAction(e -> {
            fileDataFilter1.setFilter_types(FileDataFilter.FILTER_TYPES.DIFF_ALL);
            fileDataFilter2.setFilter_types(FileDataFilter.FILTER_TYPES.DIFF_ALL);
            progData.fileDataList1.setPred(fileDataFilter1);
            progData.fileDataList2.setPred(fileDataFilter2);
        });
        btnShowOnly1.setOnAction(e -> {
            fileDataFilter1.setFilter_types(FileDataFilter.FILTER_TYPES.ONLY);
            progData.fileDataList1.setPred(fileDataFilter1);
            progData.fileDataList2.setPred(false);
        });
        btnShowOnly2.setOnAction(e -> {
            progData.fileDataList1.setPred(false);
            fileDataFilter2.setFilter_types(FileDataFilter.FILTER_TYPES.ONLY);
            progData.fileDataList2.setPred(fileDataFilter2);
        });
        txtSearch1.textProperty().addListener((observable, oldValue, newValue) -> {
            changeTextFilter();
            if (txtSearch1.getText().isEmpty()) {
                tabFilter1.setStyle("-fx-font-weight: normal;");
            } else {
                tabFilter1.setStyle("-fx-font-weight: bold;");
            }
        });
        txtSearch2.textProperty().addListener((observable, oldValue, newValue) -> {
            changeTextFilter();
            if (txtSearch2.getText().isEmpty()) {
                tabFilter2.setStyle("-fx-font-weight: normal;");
            } else {
                tabFilter2.setStyle("-fx-font-weight: bold;");
            }
        });
    }

    private void changeTextFilter() {
        fileDataFilter1.setSearchStr(txtSearch1.getText());
        fileDataFilter2.setSearchStr(txtSearch2.getText());
        progData.fileDataList1.setPred(fileDataFilter1);
        progData.fileDataList2.setPred(fileDataFilter2);
    }

    private void clearFilter() {
        btnShowAll.setSelected(true);
        fileDataFilter1.setSearchStr(txtSearch1.getText());
        fileDataFilter2.setSearchStr(txtSearch2.getText());

        fileDataFilter1.setFilter_types(FileDataFilter.FILTER_TYPES.ALL);
        fileDataFilter2.setFilter_types(FileDataFilter.FILTER_TYPES.ALL);

        progData.fileDataList1.setPred(fileDataFilter1);
        progData.fileDataList2.setPred(fileDataFilter2);
    }

    private void bindProjectDate() {
        if (projectData != null) {
            txtSearch1.textProperty().bindBidirectional(projectData.search1Property());
            txtSearch2.textProperty().bindBidirectional(projectData.search2Property());

            txtWriteHash1.textProperty().bindBidirectional(projectData.writeHash1Property());
            txtWriteHash2.textProperty().bindBidirectional(projectData.writeHash2Property());

            tabPane1.getSelectionModel().select(projectData.getSelTab1());
            tabPane2.getSelectionModel().select(projectData.getSelTab2());
            projectData.selTab1Property().bind(tabPane1.getSelectionModel().selectedIndexProperty());
            projectData.selTab2Property().bind(tabPane2.getSelectionModel().selectedIndexProperty());
        }
    }

    private void unBindProjectDate() {
        if (projectData != null) {
            projectData.srcDir1Property().unbind();
            projectData.srcDir2Property().unbind();

            txtSearch1.textProperty().unbindBidirectional(projectData.search1Property());
            txtSearch1.textProperty().unbindBidirectional(projectData.search2Property());

            txtWriteHash1.textProperty().unbindBidirectional(projectData.writeHash1Property());
            txtWriteHash2.textProperty().unbindBidirectional(projectData.writeHash2Property());

            projectData.selTab1Property().unbind();
            projectData.selTab2Property().unbind();
        }
    }

    public void saveTable() {
        new Table().saveTable(table1, Table.TABLE.FILELIST1);
        new Table().saveTable(table2, Table.TABLE.FILELIST2);
    }

    private void readDirHash(String hashDir, FileDataList fileDataList) {
        if (hashDir.isEmpty()) {
            return;
        }

        File dir = new File(hashDir);
        if (!dir.exists()) {
            PAlertFileChosser.showErrorAlert("Verzeichnis einlesen", "Verzeichnis1 existiert nicht!");
        } else {
            progData.worker.readDirHash(dir, fileDataList, 1, true);
            fileDataList.setSourceDir(hashDir);
        }

        clearFilter();
    }

    private void readHashFile(String hashFile, FileDataList fileDataList) {
        if (hashFile.isEmpty()) {
            return;
        }

        File file = new File(hashFile);
        if (!file.exists()) {
            PAlertFileChosser.showErrorAlert("Hashdatei einlesen", "Die Hashdatei existiert nicht!");
        } else {
            progData.worker.readHashFile(file, fileDataList);
            fileDataList.setSourceDir(hashFile);
        }

        clearFilter();
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
            PAlert.BUTTON btn = PAlertFileChosser.showAlert_yes_no("Datei existiert bereits!", "Überschreiben",
                    "Hashdatei existiert bereits, überschreiben?");
            if (btn.equals(PAlert.BUTTON.NO)) {
                return;
            }
        }
        progData.worker.writeHash(file, fileDataList);
    }
}
