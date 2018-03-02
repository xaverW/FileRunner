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
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import org.apache.commons.lang3.time.FastDateFormat;

import java.io.File;
import java.util.Date;

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

    private final Button btnGenName1 = new Button();
    private final Button btnGenName2 = new Button();

    private final Button btnClearFilter1 = new Button();
    private final Button btnClearFilter2 = new Button();

    private final Button btnReadDir1 = new Button("");
    private final Button btnReadHash1 = new Button("");
    private final Button btnReadDir2 = new Button("");
    private final Button btnReadHash2 = new Button("");
    private final Button btnWriteHash1 = new Button("Liste in Datei schreiben");
    private final Button btnWriteHash2 = new Button("Liste in Datei schreiben");

    private final ToggleButton btnShowAll = new ToggleButton("");
    private final ToggleButton btnShowSame = new ToggleButton("");
    private final ToggleButton btnShowDiff = new ToggleButton("");
    private final ToggleButton btnShowDiffAll = new ToggleButton("");
    private final ToggleButton btnShowOnly1 = new ToggleButton("");
    private final ToggleButton btnShowOnly2 = new ToggleButton("");

    private final CheckBox cbxFollowLink1 = new CheckBox("Symbolische Verknüpfungen auflösen");
    private final CheckBox cbxFollowLink2 = new CheckBox("Symbolische Verknüpfungen auflösen");
    private final Button btnHlpFollowLink1 = new Button("");
    private final Button btnHlpFollowLink2 = new Button("");

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
        HBox.setHgrow(cbDir1, Priority.ALWAYS);
        cbDir1.setMaxWidth(Double.MAX_VALUE);
        hBoxDir1.getChildren().addAll(cbDir1, btnSetDir1, btnReadDir1);
        vBoxDir1.getChildren().addAll(new Label("Verzeichnis 1"), hBoxDir1);

        // hash1
        VBox vBoxFile1 = new VBox(10);
        vBoxFile1.setPadding(new Insets(10));

        HBox hBoxFile1 = new HBox(10);
        HBox.setHgrow(cbHash1, Priority.ALWAYS);
        cbHash1.setMaxWidth(Double.MAX_VALUE);
        hBoxFile1.getChildren().addAll(cbHash1, btnSetHash1, btnReadHash1);
        vBoxFile1.getChildren().addAll(new Label("Hashdatei"), hBoxFile1);

        // filter1
        btnHlpFollowLink1.setGraphic(new Icons().ICON_BUTTON_HELP);
        btnHlpFollowLink1.setOnAction(a -> new PAlert().showHelpAlert("Filtern", HelpText.FOLLOW_SYMLINK));

        VBox vBoxSearch1 = new VBox(10);
        vBoxSearch1.setPadding(new Insets(10));

        HBox hBoxSearch1 = new HBox(10);
        hBoxSearch1.setAlignment(Pos.CENTER_LEFT);
        HBox.setHgrow(txtSearch1, Priority.ALWAYS);
        hBoxSearch1.getChildren().addAll(new Label("Dateien suchen"), txtSearch1, btnClearFilter1);

        HBox hBoxFollow1 = new HBox(10);
        HBox.setHgrow(cbxFollowLink1, Priority.ALWAYS);
        cbxFollowLink1.setMaxWidth(Double.MAX_VALUE);
        hBoxFollow1.getChildren().addAll(cbxFollowLink1, btnHlpFollowLink1);

        vBoxSearch1.getChildren().addAll(hBoxSearch1, hBoxFollow1);

        // Tabpane1
        tabDir1.setClosable(false);
        tabDir1.setContent(vBoxDir1);

        tabFile1.setClosable(false);
        tabFile1.setContent(vBoxFile1);

        tabFilter1.setClosable(false);
        tabFilter1.setContent(vBoxSearch1);

        tabPane1.getTabs().addAll(tabDir1, tabFile1, tabFilter1);


        // =====================
        // dir2
        VBox vBoxDir2 = new VBox(10);
        vBoxDir2.setPadding(new Insets(10));

        HBox hBoxDir2 = new HBox(10);
        HBox.setHgrow(cbDir2, Priority.ALWAYS);
        cbDir2.setMaxWidth(Double.MAX_VALUE);
        hBoxDir2.getChildren().addAll(cbDir2, btnSetDir2, btnReadDir2);
        vBoxDir2.getChildren().addAll(new Label("Verzeichnis 2"), hBoxDir2);

        // hash2
        VBox vBoxFile2 = new VBox(10);
        vBoxFile2.setPadding(new Insets(10));
        HBox hBoxFile2 = new HBox(10);
        HBox.setHgrow(cbHash2, Priority.ALWAYS);
        cbHash2.setMaxWidth(Double.MAX_VALUE);
        hBoxFile2.getChildren().addAll(cbHash2, btnSetHash2, btnReadHash2);
        vBoxFile2.getChildren().addAll(new Label("Hashdatei"), hBoxFile2);

        // filter2
        btnHlpFollowLink2.setGraphic(new Icons().ICON_BUTTON_HELP);
        btnHlpFollowLink2.setOnAction(a -> new PAlert().showHelpAlert("Filtern", HelpText.FOLLOW_SYMLINK));

        VBox vBoxSearch2 = new VBox(10);
        vBoxSearch2.setPadding(new Insets(10));

        HBox hBoxSearch2 = new HBox(10);
        hBoxSearch2.setAlignment(Pos.CENTER_LEFT);
        HBox.setHgrow(txtSearch2, Priority.ALWAYS);
        hBoxSearch2.getChildren().addAll(new Label("Dateien suchen"), txtSearch2, btnClearFilter2);

        HBox hBoxFollow2 = new HBox(10);
        HBox.setHgrow(cbxFollowLink2, Priority.ALWAYS);
        cbxFollowLink2.setMaxWidth(Double.MAX_VALUE);
        hBoxFollow2.getChildren().addAll(cbxFollowLink2, btnHlpFollowLink2);
        vBoxSearch2.getChildren().addAll(hBoxSearch2, hBoxFollow2);

        // =======================
        // Tabpane2
        tabDir2.setClosable(false);
        tabDir2.setContent(vBoxDir2);

        tabFile2.setClosable(false);
        tabFile2.setContent(vBoxFile2);

        tabFilter2.setClosable(false);
        tabFilter2.setContent(vBoxSearch2);

        tabPane2.getTabs().addAll(tabDir2, tabFile2, tabFilter2);


        // =======================
        // write hash
        HBox hBoxWriteHash1 = new HBox(10);
        HBox.setHgrow(txtWriteHash1, Priority.ALWAYS);
        hBoxWriteHash1.getChildren().addAll(btnGenName1, txtWriteHash1, btnSetWriteHash1);
        HBox hBoxWrite1 = new HBox(10);
        hBoxWrite1.setAlignment(Pos.CENTER_RIGHT);
        hBoxWrite1.getChildren().add(btnWriteHash1);

        HBox hBoxWriteHash2 = new HBox(10);
        HBox.setHgrow(txtWriteHash2, Priority.ALWAYS);
        hBoxWriteHash2.getChildren().addAll(btnGenName2, txtWriteHash2, btnSetWriteHash2);
        HBox hBoxWrite2 = new HBox(10);
        hBoxWrite2.setAlignment(Pos.CENTER_RIGHT);
        hBoxWrite2.getChildren().add(btnWriteHash2);


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

        // =================================
        ToggleGroup tg = new ToggleGroup();
        tg.getToggles().addAll(btnShowAll, btnShowSame, btnShowDiff, btnShowDiffAll, btnShowOnly1, btnShowOnly2);
        btnShowAll.setSelected(true);
        btnShowAll.setGraphic(new Icons().ICON_BUTTON_GUI_ALL);
        btnShowSame.setGraphic(new Icons().ICON_BUTTON_GUI_SAME);
        btnShowDiff.setGraphic(new Icons().ICON_BUTTON_GUI_DIFF);
        btnShowDiffAll.setGraphic(new Icons().ICON_BUTTON_GUI_DIFF_ALL);
        btnShowOnly1.setGraphic(new Icons().ICON_BUTTON_GUI_ONLY_1);
        btnShowOnly2.setGraphic(new Icons().ICON_BUTTON_GUI_ONLY_2);
        btnReadDir1.setMinWidth(btnReadDir1.getPrefWidth());
        btnReadDir2.setMinWidth(btnReadDir2.getPrefWidth());
        btnReadHash1.setMinWidth(btnReadHash1.getPrefWidth());
        btnReadHash2.setMinWidth(btnReadHash2.getPrefWidth());

        btnSetDir1.setGraphic(new Icons().ICON_BUTTON_FILE_OPEN);
        btnSetDir2.setGraphic(new Icons().ICON_BUTTON_FILE_OPEN);
        btnSetHash1.setGraphic(new Icons().ICON_BUTTON_FILE_OPEN);
        btnSetHash2.setGraphic(new Icons().ICON_BUTTON_FILE_OPEN);
        btnGenName1.setGraphic(new Icons().ICON_BUTTON_GUI_GEN_NAME);
        btnGenName2.setGraphic(new Icons().ICON_BUTTON_GUI_GEN_NAME);
        btnSetWriteHash1.setGraphic(new Icons().ICON_BUTTON_FILE_OPEN);
        btnSetWriteHash2.setGraphic(new Icons().ICON_BUTTON_FILE_OPEN);
        btnReadDir1.setGraphic(new Icons().ICON_BUTTON_GEN_HASH);
        btnReadDir2.setGraphic(new Icons().ICON_BUTTON_GEN_HASH);
        btnReadHash1.setGraphic(new Icons().ICON_BUTTON_GEN_HASH);
        btnReadHash2.setGraphic(new Icons().ICON_BUTTON_GEN_HASH);

        btnClearFilter1.setGraphic(new Icons().ICON_BUTTON_GUI_CLEAR);
        btnClearFilter2.setGraphic(new Icons().ICON_BUTTON_GUI_CLEAR);

        btnReadDir1.setTooltip(new Tooltip("Verzeichnis einlesen."));
        btnReadDir2.setTooltip(new Tooltip("Verzeichnis einlesen."));
        btnReadHash1.setTooltip(new Tooltip("Hashdatei einlesen."));
        btnReadHash2.setTooltip(new Tooltip("Hashdatei einlesen."));
        btnSetDir1.setTooltip(new Tooltip("Verzeichnis auswählen."));
        btnSetDir2.setTooltip(new Tooltip("Verzeichnis auswählen."));
        btnSetHash1.setTooltip(new Tooltip("Hashdatei auswählen"));
        btnSetHash2.setTooltip(new Tooltip("Hashdatei auswählen"));
        btnSetWriteHash1.setTooltip(new Tooltip("Datei zum Speichern auswählen."));
        btnSetWriteHash2.setTooltip(new Tooltip("Datei zum Speichern auswählen."));
        btnWriteHash1.setTooltip(new Tooltip("Hashliste in Datei schreiben."));
        btnWriteHash2.setTooltip(new Tooltip("Hashliste in Datei schreiben."));
        btnGenName1.setTooltip(new Tooltip("Einen Dateinamen vorschlagen."));
        btnGenName2.setTooltip(new Tooltip("Einen Dateinamen vorschlagen."));
        btnShowAll.setTooltip(new Tooltip("Alle Dateien anzeigen."));
        btnShowSame.setTooltip(new Tooltip("Alle gleichen Dateien anzeigen."));
        btnShowDiff.setTooltip(new Tooltip("Dateien suchen, die in beiden Listen enthalten sind aber nicht gleich sind."));
        btnShowDiffAll.setTooltip(new Tooltip("Dateien anzeigen, die sich unterscheiden oder nur in einer Liste sind."));
        btnShowOnly1.setTooltip(new Tooltip("Dateien anzeigen, die nur in Liste 1 sind."));
        btnShowOnly2.setTooltip(new Tooltip("Dateien anzeigen, die nur in Liste 2 sind."));
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
                    Table.refresh_table(table1);
                    Table.refresh_table(table2);
                }
            }
        });

        cbxFollowLink1.selectedProperty().bindBidirectional(projectData.followLink1Property());
        cbxFollowLink2.selectedProperty().bindBidirectional(projectData.followLink2Property());

        btnSetDir1.setOnAction(event -> DirFileChooser.DirChooser(ProgData.getInstance().primaryStage, cbDir1));
        btnSetDir2.setOnAction(event -> DirFileChooser.DirChooser(ProgData.getInstance().primaryStage, cbDir2));
        btnSetHash1.setOnAction(event -> DirFileChooser.FileChooser(ProgData.getInstance().primaryStage, cbHash1));
        btnSetHash2.setOnAction(event -> DirFileChooser.FileChooser(ProgData.getInstance().primaryStage, cbHash2));

        btnGenName1.setOnAction(event -> {
            String file = progData.fileDataList1.getSourceDir();
            if (file.isEmpty() && tabDir1.isSelected()) {
                file = projectData.getSrcDir1();
            } else if (file.isEmpty() && tabFile1.isSelected()) {
                file = projectData.getSrcHash1();
            }

            if (file.isEmpty()) {
                return;
            }

            if (!txtWriteHash1.getText().startsWith(file)) {
                txtWriteHash1.setText(file);
            }
            txtWriteHash1.setText(getNextName(txtWriteHash1.getText()));
        });
        btnGenName2.setOnAction(event -> {
            String file = progData.fileDataList2.getSourceDir();
            if (file.isEmpty() && tabDir2.isSelected()) {
                file = projectData.getSrcDir2();
            } else if (file.isEmpty() && tabFile2.isSelected()) {
                file = projectData.getSrcHash2();
            }

            if (file.isEmpty()) {
                return;
            }

            if (!txtWriteHash2.getText().startsWith(file)) {
                txtWriteHash2.setText(file);
            }
            txtWriteHash2.setText(getNextName(txtWriteHash2.getText()));
        });

        btnSetWriteHash1.setOnAction(event -> DirFileChooser.FileChooser(ProgData.getInstance().primaryStage, txtWriteHash1));
        btnSetWriteHash2.setOnAction(event -> DirFileChooser.FileChooser(ProgData.getInstance().primaryStage, txtWriteHash1));

        btnReadDir1.setOnAction(a -> {
            readDirHash(projectData.getSrcDir1(), progData.fileDataList1, projectData.isFollowLink1());
            changeTextFilter();
        });
        btnReadHash1.setOnAction(a -> {
            readHashFile(projectData.getSrcHash1(), progData.fileDataList1);
            changeTextFilter();
        });
        btnReadDir2.setOnAction(a -> {
            readDirHash(projectData.getSrcDir2(), progData.fileDataList2, projectData.isFollowLink2());
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
            setTabText();
        });
        txtSearch2.textProperty().addListener((observable, oldValue, newValue) -> {
            changeTextFilter();
            setTabText();
        });
        btnClearFilter1.disableProperty().bind(txtSearch1.textProperty().isEmpty());
        btnClearFilter2.disableProperty().bind(txtSearch2.textProperty().isEmpty());
        btnClearFilter1.setOnAction(a -> txtSearch1.clear());
        btnClearFilter2.setOnAction(a -> txtSearch2.clear());
        btnWriteHash1.disableProperty().bind(progData.fileDataList1.emptyProperty());
        btnWriteHash2.disableProperty().bind(progData.fileDataList2.emptyProperty());
    }

    private void setTabText() {
        if (txtSearch2.getText().isEmpty()) {
            tabFilter2.setGraphic(null);
            tabFilter2.setStyle("-fx-font-weight: normal;");
        } else {
            tabFilter2.setGraphic(new Icons().ICON_TAB_SEARCH);
            tabFilter2.setStyle("-fx-font-weight: bold; -fx-underline: true;");
        }
        if (txtSearch1.getText().isEmpty()) {
            tabFilter1.setGraphic(null);
            tabFilter1.setStyle("-fx-font-weight: normal;");
        } else {
            tabFilter1.setGraphic(new Icons().ICON_TAB_SEARCH);
            tabFilter1.setStyle("-fx-font-weight: bold; -fx-underline: true;");
        }
    }

    public static final FastDateFormat FORMATTER_ddMMyyyy = FastDateFormat.getInstance("__yyyyMMdd");
    public static final FastDateFormat FORMATTER_ddMMyyyyHHmmss = FastDateFormat.getInstance("__yyyyMMdd_HHmmss");
    private String lastDate2 = "";

    private String getNextName(String name) {
        String ret;
        final String MD5 = ".md5";
        final String date1 = FORMATTER_ddMMyyyy.format(new Date()) + ".md5";
        final String date2 = FORMATTER_ddMMyyyyHHmmss.format(new Date()) + ".md5";

        if (name.endsWith(date1)) {
            ret = name.replace(date1, date2);
            lastDate2 = date2;
        } else if (name.endsWith(date2)) {
            ret = name.replace(date2, MD5);
        } else if (!lastDate2.isEmpty() && name.endsWith(lastDate2)) {
            ret = name.replace(lastDate2, MD5);
        } else if (name.endsWith(MD5)) {
            ret = name.replace(MD5, date1);
        } else {
            ret = name + MD5;
        }
        return ret;

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

    private void readDirHash(String hashDir, FileDataList fileDataList, boolean followLink) {
        if (hashDir.isEmpty()) {
            return;
        }

        File dir = new File(hashDir);
        if (!dir.exists()) {
            PAlertFileChosser.showErrorAlert("Verzeichnis einlesen", "Verzeichnis1 existiert nicht!");
        } else {
            progData.worker.createDirHash(dir, fileDataList, 1, true, followLink);
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
            progData.worker.readDirHashFile(file, fileDataList);
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
        progData.worker.writeDirHashFile(file, fileDataList);
    }
}
