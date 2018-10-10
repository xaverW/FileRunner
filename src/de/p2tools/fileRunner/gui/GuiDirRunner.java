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
import de.p2tools.fileRunner.gui.tools.GuiTools;
import de.p2tools.fileRunner.gui.tools.Table;
import de.p2tools.p2Lib.dialog.DirFileChooser;
import de.p2tools.p2Lib.dialog.PAlert;
import de.p2tools.p2Lib.dialog.PAlertFileChosser;
import de.p2tools.p2Lib.guiTools.PComboBoxString;
import javafx.animation.Animation;
import javafx.animation.Interpolator;
import javafx.animation.Transition;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.util.Duration;

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

    private final PComboBoxString pCboDir1 = new PComboBoxString();
    private final PComboBoxString pCboDir2 = new PComboBoxString();
    private final PComboBoxString pCboZip1 = new PComboBoxString();
    private final PComboBoxString pCboZip2 = new PComboBoxString();
    private final PComboBoxString pCboHash1 = new PComboBoxString();
    private final PComboBoxString pCboHash2 = new PComboBoxString();
    private final PComboBoxString pCboWriteHash1 = new PComboBoxString();
    private final PComboBoxString pCboWriteHash2 = new PComboBoxString();
    private final PComboBoxString pCboSearch1 = new PComboBoxString();
    private final PComboBoxString pCboSearch2 = new PComboBoxString();

    private final Button btnSelectDir1 = new Button("");
    private final Button btnSelectDir2 = new Button("");
    private final Button btnSelectZip1 = new Button("");
    private final Button btnSelectZip2 = new Button("");
    private final Button btnSelectHash1 = new Button("");
    private final Button btnSelectHash2 = new Button("");
    private final Button btnSelectHashList1 = new Button();
    private final Button btnSelectHashList2 = new Button();

    private final Button btnProposeHashName1 = new Button();
    private final Button btnProposeHashName2 = new Button();

    private final Button btnClearFilter1 = new Button();
    private final Button btnClearFilter2 = new Button();

    private final Button btnReadDir1 = new Button("");
    private final Button btnReadDir2 = new Button("");
    private final Button btnReadZip1 = new Button("");
    private final Button btnReadZip2 = new Button("");
    private final Button btnReadHash1 = new Button("");
    private final Button btnReadHash2 = new Button("");
    private final Button btnWriteHash1 = new Button("Liste in Datei schreiben");
    private final Button btnWriteHash2 = new Button("Liste in Datei schreiben");

    private final Label lblWriteHash1 = new Label("Ok");
    private final Label lblWriteHash2 = new Label("Ok");

    private final ToggleButton tglShowAll = new ToggleButton("");
    private final ToggleButton tglShowSame = new ToggleButton("");
    private final ToggleButton tglShowDiff = new ToggleButton("");
    private final ToggleButton tglShowDiffAll = new ToggleButton("");
    private final ToggleButton tglShowOnly1 = new ToggleButton("");
    private final ToggleButton tglShowOnly2 = new ToggleButton("");

    private final TabPane tabPane1 = new TabPane();
    private final Tab tabDir1 = new Tab("Ordner");
    private final Tab tabZip1 = new Tab("Zipdatei");
    private final Tab tabFile1 = new Tab("Hashdatei");
    private final Tab tabFilter1 = new Tab("Suchen");

    private final TabPane tabPane2 = new TabPane();
    private final Tab tabDir2 = new Tab("Ordner");
    private final Tab tabZip2 = new Tab("Zipdatei");
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
        setTabText();
        addListener();
    }

    public void isShown() {
    }

    private void initCont() {
        //=======================
        // dir1
        VBox vBoxDir1 = new VBox(10);
        vBoxDir1.setPadding(new Insets(10));

        HBox hBoxDir1 = new HBox(10);
        HBox.setHgrow(pCboDir1, Priority.ALWAYS);
        pCboDir1.setMaxWidth(Double.MAX_VALUE);
        hBoxDir1.getChildren().addAll(pCboDir1, btnSelectDir1, btnReadDir1);
        vBoxDir1.getChildren().addAll(new Label("Verzeichnis 1"), hBoxDir1);

        // zip1
        VBox vBoxZip1 = new VBox(10);
        vBoxZip1.setPadding(new Insets(10));

        HBox hBoxZip1 = new HBox(10);
        HBox.setHgrow(pCboZip1, Priority.ALWAYS);
        pCboZip1.setMaxWidth(Double.MAX_VALUE);
        hBoxZip1.getChildren().addAll(pCboZip1, btnSelectZip1, btnReadZip1);
        vBoxZip1.getChildren().addAll(new Label("Zipfile 1"), hBoxZip1);

        // hash1
        VBox vBoxFile1 = new VBox(10);
        vBoxFile1.setPadding(new Insets(10));

        HBox hBoxFile1 = new HBox(10);
        HBox.setHgrow(pCboHash1, Priority.ALWAYS);
        pCboHash1.setMaxWidth(Double.MAX_VALUE);
        pCboHash1.setEditable(true);
        hBoxFile1.getChildren().addAll(pCboHash1, btnSelectHash1, btnReadHash1);
        vBoxFile1.getChildren().addAll(new Label("Hashdatei"), hBoxFile1);

        // filter1
        VBox vBoxSearch1 = new VBox(10);
        vBoxSearch1.setPadding(new Insets(10));

        HBox hBoxSearch1 = new HBox(10);
        hBoxSearch1.setAlignment(Pos.CENTER_LEFT);
        HBox.setHgrow(pCboSearch1, Priority.ALWAYS);
        pCboSearch1.setMaxWidth(Double.MAX_VALUE);
        hBoxSearch1.getChildren().addAll(pCboSearch1, btnClearFilter1);

        vBoxSearch1.getChildren().addAll(new Label("Dateien suchen"), hBoxSearch1);

        // Tabpane1
        tabDir1.setClosable(false);
        tabDir1.setContent(vBoxDir1);

        tabZip1.setClosable(false);
        tabZip1.setContent(vBoxZip1);

        tabFile1.setClosable(false);
        tabFile1.setContent(vBoxFile1);

        tabFilter1.setClosable(false);
        tabFilter1.setContent(vBoxSearch1);

        tabPane1.getTabs().addAll(tabDir1, tabZip1, tabFile1, tabFilter1);


        // =====================
        // dir2
        VBox vBoxDir2 = new VBox(10);
        vBoxDir2.setPadding(new Insets(10));

        HBox hBoxDir2 = new HBox(10);
        HBox.setHgrow(pCboDir2, Priority.ALWAYS);
        pCboDir2.setMaxWidth(Double.MAX_VALUE);
        pCboDir2.setEditable(true);
        hBoxDir2.getChildren().addAll(pCboDir2, btnSelectDir2, btnReadDir2);
        vBoxDir2.getChildren().addAll(new Label("Verzeichnis 2"), hBoxDir2);

        // zip2
        VBox vBoxZip2 = new VBox(10);
        vBoxZip2.setPadding(new Insets(10));

        HBox hBoxZip2 = new HBox(10);
        HBox.setHgrow(pCboZip2, Priority.ALWAYS);
        pCboZip2.setMaxWidth(Double.MAX_VALUE);
        pCboZip2.setEditable(true);
        hBoxZip2.getChildren().addAll(pCboZip2, btnSelectZip2, btnReadZip2);
        vBoxZip2.getChildren().addAll(new Label("Zipdatei 2"), hBoxZip2);

        // hash2
        VBox vBoxFile2 = new VBox(10);
        vBoxFile2.setPadding(new Insets(10));
        HBox hBoxFile2 = new HBox(10);
        HBox.setHgrow(pCboHash2, Priority.ALWAYS);
        pCboHash2.setMaxWidth(Double.MAX_VALUE);
        hBoxFile2.getChildren().addAll(pCboHash2, btnSelectHash2, btnReadHash2);
        vBoxFile2.getChildren().addAll(new Label("Hashdatei"), hBoxFile2);

        // filter2
        VBox vBoxSearch2 = new VBox(10);
        vBoxSearch2.setPadding(new Insets(10));

        HBox hBoxSearch2 = new HBox(10);
        hBoxSearch2.setAlignment(Pos.CENTER_LEFT);
        HBox.setHgrow(pCboSearch2, Priority.ALWAYS);
        pCboSearch2.setMaxWidth(Double.MAX_VALUE);
        hBoxSearch2.getChildren().addAll(pCboSearch2, btnClearFilter2);

        vBoxSearch2.getChildren().addAll(new Label("Dateien suchen"), hBoxSearch2);

        // =======================
        // Tabpane2
        tabDir2.setClosable(false);
        tabDir2.setContent(vBoxDir2);

        tabZip2.setClosable(false);
        tabZip2.setContent(vBoxZip2);

        tabFile2.setClosable(false);
        tabFile2.setContent(vBoxFile2);

        tabFilter2.setClosable(false);
        tabFilter2.setContent(vBoxSearch2);

        tabPane2.getTabs().addAll(tabDir2, tabZip2, tabFile2, tabFilter2);


        // =======================
        // write hash
        HBox hBoxWriteHash1 = new HBox(10);
        HBox.setHgrow(pCboWriteHash1, Priority.ALWAYS);
        pCboWriteHash1.setMaxWidth(Double.MAX_VALUE);
        pCboWriteHash1.setEditable(true);
        hBoxWriteHash1.getChildren().addAll(btnProposeHashName1, pCboWriteHash1, btnSelectHashList1);

        HBox hBoxWrite1 = new HBox(10);
        hBoxWrite1.setAlignment(Pos.CENTER_RIGHT);
        hBoxWrite1.getChildren().addAll(lblWriteHash1, btnWriteHash1);

        HBox hBoxWriteHash2 = new HBox(10);
        HBox.setHgrow(pCboWriteHash2, Priority.ALWAYS);
        pCboWriteHash2.setMaxWidth(Double.MAX_VALUE);
        pCboWriteHash2.setEditable(true);
        hBoxWriteHash2.getChildren().addAll(btnProposeHashName2, pCboWriteHash2, btnSelectHashList2);

        HBox hBoxWrite2 = new HBox(10);
        hBoxWrite2.setAlignment(Pos.CENTER_RIGHT);
        hBoxWrite2.getChildren().addAll(lblWriteHash2, btnWriteHash2);


        // ================================
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
        vBoxBtn.getChildren().addAll(tglShowAll, tglShowSame, tglShowDiffAll, tglShowDiff, tglShowOnly1, tglShowOnly2);

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

        // =================================
        lblWriteHash1.setVisible(false);
        lblWriteHash1.setPadding(new Insets(10));
        lblWriteHash2.setVisible(false);
        lblWriteHash2.setPadding(new Insets(10));

        ToggleGroup tg = new ToggleGroup();
        tg.getToggles().addAll(tglShowAll, tglShowSame, tglShowDiffAll, tglShowDiff, tglShowOnly1, tglShowOnly2);
        tglShowAll.setSelected(true);
        tglShowAll.setGraphic(new Icons().ICON_BUTTON_GUI_ALL);
        tglShowSame.setGraphic(new Icons().ICON_BUTTON_GUI_SAME);
        tglShowDiffAll.setGraphic(new Icons().ICON_BUTTON_GUI_DIFF_ALL);
        tglShowDiff.setGraphic(new Icons().ICON_BUTTON_GUI_DIFF);
        tglShowOnly1.setGraphic(new Icons().ICON_BUTTON_GUI_ONLY_1);
        tglShowOnly2.setGraphic(new Icons().ICON_BUTTON_GUI_ONLY_2);
        btnReadDir1.setMinWidth(btnReadDir1.getPrefWidth());
        btnReadDir2.setMinWidth(btnReadDir2.getPrefWidth());
        btnReadZip1.setMinWidth(btnReadZip1.getPrefWidth());
        btnReadZip2.setMinWidth(btnReadZip2.getPrefWidth());
        btnReadHash1.setMinWidth(btnReadHash1.getPrefWidth());
        btnReadHash2.setMinWidth(btnReadHash2.getPrefWidth());

        btnSelectDir1.setGraphic(new Icons().ICON_BUTTON_FILE_OPEN);
        btnSelectDir2.setGraphic(new Icons().ICON_BUTTON_FILE_OPEN);
        btnSelectZip1.setGraphic(new Icons().ICON_BUTTON_FILE_OPEN);
        btnSelectZip2.setGraphic(new Icons().ICON_BUTTON_FILE_OPEN);
        btnSelectHash1.setGraphic(new Icons().ICON_BUTTON_FILE_OPEN);
        btnSelectHash2.setGraphic(new Icons().ICON_BUTTON_FILE_OPEN);
        btnProposeHashName1.setGraphic(new Icons().ICON_BUTTON_GUI_GEN_NAME);
        btnProposeHashName2.setGraphic(new Icons().ICON_BUTTON_GUI_GEN_NAME);
        btnSelectHashList1.setGraphic(new Icons().ICON_BUTTON_FILE_OPEN);
        btnSelectHashList2.setGraphic(new Icons().ICON_BUTTON_FILE_OPEN);
        btnReadDir1.setGraphic(new Icons().ICON_BUTTON_GEN_HASH);
        btnReadDir2.setGraphic(new Icons().ICON_BUTTON_GEN_HASH);
        btnReadZip1.setGraphic(new Icons().ICON_BUTTON_GEN_HASH);
        btnReadZip2.setGraphic(new Icons().ICON_BUTTON_GEN_HASH);
        btnReadHash1.setGraphic(new Icons().ICON_BUTTON_GEN_HASH);
        btnReadHash2.setGraphic(new Icons().ICON_BUTTON_GEN_HASH);

        btnClearFilter1.setGraphic(new Icons().ICON_BUTTON_GUI_CLEAR);
        btnClearFilter2.setGraphic(new Icons().ICON_BUTTON_GUI_CLEAR);

        btnReadDir1.setTooltip(new Tooltip("Verzeichnis einlesen."));
        btnReadDir2.setTooltip(new Tooltip("Verzeichnis einlesen."));
        btnReadZip1.setTooltip(new Tooltip("Zipdatei einlesen."));
        btnReadZip2.setTooltip(new Tooltip("Zipdatei einlesen."));
        btnReadHash1.setTooltip(new Tooltip("Hashdatei einlesen."));
        btnReadHash2.setTooltip(new Tooltip("Hashdatei einlesen."));
        btnSelectDir1.setTooltip(new Tooltip("Verzeichnis auswählen."));
        btnSelectDir2.setTooltip(new Tooltip("Verzeichnis auswählen."));
        btnSelectZip1.setTooltip(new Tooltip("Zipdatei auswählen."));
        btnSelectZip2.setTooltip(new Tooltip("Zipdatei auswählen."));
        btnSelectHash1.setTooltip(new Tooltip("Hashdatei auswählen"));
        btnSelectHash2.setTooltip(new Tooltip("Hashdatei auswählen"));
        btnSelectHashList1.setTooltip(new Tooltip("Datei zum Speichern auswählen."));
        btnSelectHashList2.setTooltip(new Tooltip("Datei zum Speichern auswählen."));
        btnWriteHash1.setTooltip(new Tooltip("Hashliste in Datei schreiben."));
        btnWriteHash2.setTooltip(new Tooltip("Hashliste in Datei schreiben."));
        btnProposeHashName1.setTooltip(new Tooltip("Einen Dateinamen vorschlagen."));
        btnProposeHashName2.setTooltip(new Tooltip("Einen Dateinamen vorschlagen."));
        tglShowAll.setTooltip(new Tooltip("Alle Dateien anzeigen."));
        tglShowSame.setTooltip(new Tooltip("Alle gleichen Dateien anzeigen."));
        tglShowDiffAll.setTooltip(new Tooltip("Dateien suchen, die sich unterscheiden oder nur in einer Liste enthalten sind."));
        tglShowDiff.setTooltip(new Tooltip("Dateien suchen, die in beiden Listen enthalten sind, sich aber unterscheiden."));
        tglShowOnly1.setTooltip(new Tooltip("Dateien suchen, die nur in Liste 1 enthalten sind."));
        tglShowOnly2.setTooltip(new Tooltip("Dateien suchen, die nur in Liste 2 enthalten sind."));
        btnClearFilter1.setTooltip(new Tooltip("Filter löschen."));
        btnClearFilter2.setTooltip(new Tooltip("Filter löschen."));
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
        projectData = progData.projectData;

        pCboDir1.init(projectData.getSrcDirList(), projectData.srcDir1Property());
        pCboDir2.init(projectData.getSrcDirList(), projectData.srcDir2Property());
        pCboZip1.init(projectData.getSrcZipList(), projectData.srcZip1Property());
        pCboZip2.init(projectData.getSrcZipList(), projectData.srcZip2Property());
        pCboHash1.init(projectData.getSrcHashList(), projectData.srcHash1Property());
        pCboHash2.init(projectData.getSrcHashList(), projectData.srcHash2Property());
        pCboSearch1.init(projectData.getSearchList(), projectData.search1Property());
        pCboSearch2.init(projectData.getSearchList(), projectData.search2Property());
        pCboWriteHash1.init(projectData.getWriteHashList(), projectData.writeHash1Property());
        pCboWriteHash2.init(projectData.getWriteHashList(), projectData.writeHash2Property());

        tabPane1.getSelectionModel().select(projectData.getSelTab1());
        tabPane2.getSelectionModel().select(projectData.getSelTab2());

        projectData.selTab1Property().bind(tabPane1.getSelectionModel().selectedIndexProperty());
        projectData.selTab2Property().bind(tabPane2.getSelectionModel().selectedIndexProperty());
    }


    private void addListener() {
        progData.worker.addAdListener(new RunListener() {
            @Override
            public void ping(RunEvent runEvent) {
                if (runEvent.nixLos()) {
                    Table.refresh_table(table1);
                    Table.refresh_table(table2);
                }
            }
        });

        btnSelectDir1.setOnAction(event -> DirFileChooser.DirChooser(ProgData.getInstance().primaryStage, pCboDir1));
        btnSelectDir2.setOnAction(event -> DirFileChooser.DirChooser(ProgData.getInstance().primaryStage, pCboDir2));
        btnSelectZip1.setOnAction(event -> DirFileChooser.FileChooser(ProgData.getInstance().primaryStage, pCboZip1));
        btnSelectZip2.setOnAction(event -> DirFileChooser.FileChooser(ProgData.getInstance().primaryStage, pCboZip2));
        btnSelectHash1.setOnAction(event -> DirFileChooser.FileChooser(ProgData.getInstance().primaryStage, pCboHash1));
        btnSelectHash2.setOnAction(event -> DirFileChooser.FileChooser(ProgData.getInstance().primaryStage, pCboHash2));

        btnSelectHashList1.setOnAction(event -> DirFileChooser.FileChooser(ProgData.getInstance().primaryStage, pCboWriteHash1));
        btnSelectHashList2.setOnAction(event -> DirFileChooser.FileChooser(ProgData.getInstance().primaryStage, pCboWriteHash1));
        btnProposeHashName1.setOnAction(event -> {
            String file = progData.fileDataList1.getSourceDir();
            if (file.isEmpty() && tabDir1.isSelected()) {
                file = projectData.getSrcDir1();
            } else if (file.isEmpty() && tabFile1.isSelected()) {
                file = projectData.getSrcHash1();
            }

            if (file.isEmpty()) {
                return;
            }

            if (!pCboWriteHash1.getEditor().getText().startsWith(file)) {
                pCboWriteHash1.getEditor().setText(file);
            }
            pCboWriteHash1.selectElement(GuiTools.getNextName(pCboWriteHash1.getEditor().getText()));
        });
        btnProposeHashName2.setOnAction(event -> {
            String file = progData.fileDataList2.getSourceDir();
            if (file.isEmpty() && tabDir2.isSelected()) {
                file = projectData.getSrcDir2();
            } else if (file.isEmpty() && tabFile2.isSelected()) {
                file = projectData.getSrcHash2();
            }

            if (file.isEmpty()) {
                return;
            }

            if (!pCboWriteHash2.getEditor().getText().startsWith(file)) {
                pCboWriteHash2.getEditor().setText(file);
            }
            pCboWriteHash2.selectElement(GuiTools.getNextName(pCboWriteHash2.getEditor().getText()));
        });


        btnReadDir1.disableProperty().bind(pCboDir1.getEditor().textProperty().isNull()
                .or(pCboDir1.getEditor().textProperty().isEqualTo("")));
        btnReadDir2.disableProperty().bind(pCboDir2.getEditor().textProperty().isNull()
                .or(pCboDir2.getEditor().textProperty().isEqualTo("")));
        btnReadZip1.disableProperty().bind(pCboZip1.getEditor().textProperty().isNull()
                .or(pCboZip1.getEditor().textProperty().isEqualTo("")));
        btnReadZip2.disableProperty().bind(pCboZip2.getEditor().textProperty().isNull()
                .or(pCboZip2.getEditor().textProperty().isEqualTo("")));
        btnReadHash1.disableProperty().bind(pCboHash1.getEditor().textProperty().isNull()
                .or(pCboHash1.getEditor().textProperty().isEqualTo("")));
        btnReadHash2.disableProperty().bind(pCboHash2.getEditor().textProperty().isNull()
                .or(pCboHash2.getEditor().textProperty().isEqualTo("")));


        btnReadDir1.setOnAction(a -> {
            if (readDirHash(projectData.getSrcDir1(), progData.fileDataList1, projectData.isFollowLink1())) {
                setTabDirFile(DIR_ZIP_HASH.DIR_1);
            }
            changeTextFilter();
        });
        btnReadDir2.setOnAction(a -> {
            if (readDirHash(projectData.getSrcDir2(), progData.fileDataList2, projectData.isFollowLink2())) {
                setTabDirFile(DIR_ZIP_HASH.DIR_2);
            }
            changeTextFilter();
        });


        btnReadZip1.setOnAction(a -> {
            if (readZipHash(projectData.getSrcZip1(), progData.fileDataList1)) {
                setTabDirFile(DIR_ZIP_HASH.ZIP_1);
            }
            changeTextFilter();
        });
        btnReadZip2.setOnAction(a -> {
            if (readZipHash(projectData.getSrcZip2(), progData.fileDataList2)) {
                setTabDirFile(DIR_ZIP_HASH.ZIP_2);
            }
            changeTextFilter();
        });


        btnReadHash1.setOnAction(a -> {
            if (readHashFile(projectData.getSrcHash1(), progData.fileDataList1)) {
                setTabDirFile(DIR_ZIP_HASH.HASH_1);
            }
            changeTextFilter();
        });
        btnReadHash2.setOnAction(a -> {
            if (readHashFile(projectData.getSrcHash2(), progData.fileDataList2)) {
                setTabDirFile(DIR_ZIP_HASH.HASH_2);
            }
            changeTextFilter();
        });


        btnWriteHash1.setOnAction(e -> {
            writeHashFile(true);
        });
        btnWriteHash2.setOnAction(event -> {
            writeHashFile(false);
        });


        tglShowAll.setOnAction(e -> {
            fileDataFilter1.setFilter_types(FileDataFilter.FILTER_TYPES.ALL);
            fileDataFilter2.setFilter_types(FileDataFilter.FILTER_TYPES.ALL);
            progData.fileDataList1.setPred(fileDataFilter1);
            progData.fileDataList2.setPred(fileDataFilter2);
        });
        tglShowSame.setOnAction(e -> {
            fileDataFilter1.setFilter_types(FileDataFilter.FILTER_TYPES.SAME);
            fileDataFilter2.setFilter_types(FileDataFilter.FILTER_TYPES.SAME);
            progData.fileDataList1.setPred(fileDataFilter1);
            progData.fileDataList2.setPred(fileDataFilter2);
        });
        tglShowDiffAll.setOnAction(e -> {
            fileDataFilter1.setFilter_types(FileDataFilter.FILTER_TYPES.DIFF_ALL);
            fileDataFilter2.setFilter_types(FileDataFilter.FILTER_TYPES.DIFF_ALL);
            progData.fileDataList1.setPred(fileDataFilter1);
            progData.fileDataList2.setPred(fileDataFilter2);
        });
        tglShowDiff.setOnAction(e -> {
            fileDataFilter1.setFilter_types(FileDataFilter.FILTER_TYPES.DIFF);
            fileDataFilter2.setFilter_types(FileDataFilter.FILTER_TYPES.DIFF);
            progData.fileDataList1.setPred(fileDataFilter1);
            progData.fileDataList2.setPred(fileDataFilter2);
        });
        tglShowOnly1.setOnAction(e -> {
            fileDataFilter1.setFilter_types(FileDataFilter.FILTER_TYPES.ONLY);
            progData.fileDataList1.setPred(fileDataFilter1);
            progData.fileDataList2.setPred(false);
        });
        tglShowOnly2.setOnAction(e -> {
            progData.fileDataList1.setPred(false);
            fileDataFilter2.setFilter_types(FileDataFilter.FILTER_TYPES.ONLY);
            progData.fileDataList2.setPred(fileDataFilter2);
        });
        pCboSearch1.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            changeTextFilter();
            setTabText();
        });
        pCboSearch2.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            changeTextFilter();
            setTabText();
        });

        btnClearFilter1.disableProperty().bind(pCboSearch1.valueProperty().isNull()
                .or(pCboSearch1.valueProperty().isEqualTo("")));
        btnClearFilter2.disableProperty().bind(pCboSearch2.valueProperty().isNull()
                .or(pCboSearch2.valueProperty().isEqualTo("")));
        btnClearFilter1.setOnAction(a -> pCboSearch1.getSelectionModel().select(""));
        btnClearFilter2.setOnAction(a -> pCboSearch2.getSelectionModel().select(""));
        btnWriteHash1.disableProperty().bind(progData.fileDataList1.emptyProperty().
                or(pCboWriteHash1.getEditor().textProperty().isEmpty()));
        btnWriteHash2.disableProperty().bind(progData.fileDataList2.emptyProperty().
                or(pCboWriteHash2.getEditor().textProperty().isEmpty()));
    }

    private void setTabText() {
        if (pCboSearch1.getValue() == null || pCboSearch1.getValue().isEmpty()) {
            tabFilter1.setGraphic(null);
            tabFilter1.setStyle("-fx-font-weight: normal;");
        } else {
            tabFilter1.setGraphic(new Icons().ICON_TAB_SEARCH);
            tabFilter1.setStyle("-fx-font-weight: bold; -fx-underline: true;");
        }
        if (pCboSearch2.getValue() == null || pCboSearch2.getValue().isEmpty()) {
            tabFilter2.setGraphic(null);
            tabFilter2.setStyle("-fx-font-weight: normal;");
        } else {
            tabFilter2.setGraphic(new Icons().ICON_TAB_SEARCH);
            tabFilter2.setStyle("-fx-font-weight: bold; -fx-underline: true;");
        }
    }

    enum DIR_ZIP_HASH {DIR_1, DIR_2, ZIP_1, ZIP_2, HASH_1, HASH_2}

    private void setTabDirFile(DIR_ZIP_HASH dir_zip_hash) {
        // todo
        switch (dir_zip_hash) {
            case DIR_1:
                tabDir1.setGraphic(new Icons().ICON_TAB_DIR_FILE);
                tabZip1.setGraphic(null);
                tabFile1.setGraphic(null);
                break;
            case DIR_2:
                tabDir2.setGraphic(new Icons().ICON_TAB_DIR_FILE);
                tabZip2.setGraphic(null);
                tabFile2.setGraphic(null);
                break;
            case ZIP_1:
                tabDir1.setGraphic(null);
                tabZip1.setGraphic(new Icons().ICON_TAB_DIR_FILE);
                tabFile1.setGraphic(null);
                break;
            case ZIP_2:
                tabDir2.setGraphic(null);
                tabZip2.setGraphic(new Icons().ICON_TAB_DIR_FILE);
                tabFile2.setGraphic(null);
                break;
            case HASH_1:
                tabDir1.setGraphic(null);
                tabZip1.setGraphic(null);
                tabFile1.setGraphic(new Icons().ICON_TAB_DIR_FILE);
                break;
            case HASH_2:
                tabDir2.setGraphic(null);
                tabZip2.setGraphic(null);
                tabFile2.setGraphic(new Icons().ICON_TAB_DIR_FILE);
                break;
        }
    }


    private void changeTextFilter() {
        fileDataFilter1.setSearchStr(pCboSearch1.getSelectionModel().getSelectedItem());
        fileDataFilter2.setSearchStr(pCboSearch2.getSelectionModel().getSelectedItem());
        progData.fileDataList1.setPred(fileDataFilter1);
        progData.fileDataList2.setPred(fileDataFilter2);
    }

    private void clearFilter() {
        tglShowAll.setSelected(true);
        fileDataFilter1.setSearchStr(pCboSearch1.getSelectionModel().getSelectedItem());
        fileDataFilter2.setSearchStr(pCboSearch2.getSelectionModel().getSelectedItem());

        fileDataFilter1.setFilter_types(FileDataFilter.FILTER_TYPES.ALL);
        fileDataFilter2.setFilter_types(FileDataFilter.FILTER_TYPES.ALL);

        progData.fileDataList1.setPred(fileDataFilter1);
        progData.fileDataList2.setPred(fileDataFilter2);
    }

    public void saveTable() {
        new Table().saveTable(table1, Table.TABLE.FILELIST1);
        new Table().saveTable(table2, Table.TABLE.FILELIST2);
    }

    private boolean readDirHash(String hashDir, FileDataList fileDataList, boolean followLink) {
        boolean ret = false;
        if (hashDir.isEmpty()) {
            return false;
        }

        File dir = new File(hashDir);
        if (!dir.exists()) {
            PAlertFileChosser.showErrorAlert("Verzeichnis einlesen", "Verzeichnis1 existiert nicht!");
        } else {
            ret = true;
            progData.worker.createDirHash(dir, fileDataList, 1, true, followLink);
            fileDataList.setSourceDir(hashDir);
        }

        clearFilter();
        return ret;
    }

    private boolean readZipHash(String hashZip, FileDataList fileDataList) {
        boolean ret = false;
        if (hashZip.isEmpty()) {
            return false;
        }

        File zipFile = new File(hashZip);
        if (!zipFile.exists()) {
            PAlertFileChosser.showErrorAlert("Zipdatei einlesen", "Die Zipdatei existiert nicht!");
        } else {
            ret = true;
            progData.worker.createZipHash(zipFile, fileDataList);
            fileDataList.setSourceDir(hashZip);
        }

        clearFilter();
        return ret;
    }

    private boolean readHashFile(String hashFile, FileDataList fileDataList) {
        boolean ret = false;
        if (hashFile.isEmpty()) {
            return false;
        }

        File file = new File(hashFile);
        if (!file.exists() || !file.isFile()) {
            PAlertFileChosser.showErrorAlert("Hashdatei einlesen", "Die Hashdatei existiert nicht!");
        } else {
            ret = true;
            progData.worker.readDirHashFile(file, fileDataList);
            fileDataList.setSourceDir(hashFile);
        }

        clearFilter();
        return ret;
    }

    private void writeHashFile(boolean hash1) {
        File file;
        String str;
        FileDataList fileDataList;
        Label lbl;
        if (hash1) {
            lbl = lblWriteHash1;
            str = projectData.getWriteHash1().trim();
            fileDataList = progData.fileDataList1;
        } else {
            lbl = lblWriteHash2;
            str = projectData.getWriteHash2().trim();
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
        if (progData.worker.writeDirHashFile(file, fileDataList)) {

            lbl.setVisible(true);
            final Animation animation = new Transition() {
                {
                    setCycleDuration(Duration.millis(2000));
                    setInterpolator(Interpolator.EASE_OUT);
                }

                @Override
                protected void interpolate(double frac) {
                    Color vColor = Color.web("#90ee90", 1 - frac);
                    lbl.setBackground(new Background(new BackgroundFill(vColor, CornerRadii.EMPTY, Insets.EMPTY)));
                    if (frac == 1) {
                        lbl.setVisible(false);
                    }
                }
            };
            animation.play();
        }
    }
}
