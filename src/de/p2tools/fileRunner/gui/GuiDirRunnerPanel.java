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
import de.p2tools.p2Lib.guiTools.PComboBoxString;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

public class GuiDirRunnerPanel extends VBox {

    private final ScrollPane scrollPane = new ScrollPane();
    private final TableView table = new TableView();

    private final PComboBoxString pCboDir = new PComboBoxString();
    private final PComboBoxString pCboZip = new PComboBoxString();
    private final PComboBoxString pCboHash = new PComboBoxString();
    private final PComboBoxString pCboWriteHash = new PComboBoxString();
    private final PComboBoxString pCboSearch = new PComboBoxString();

    private final Button btnSelectDir = new Button("");
    private final Button btnSelectZip = new Button("");
    private final Button btnSelectHash = new Button("");
    private final Button btnSelectHashList = new Button();

    private final Button btnProposeHashName = new Button();

    private final Button btnClearFilter = new Button();

    private final Button btnReadDir = new Button("");
    private final Button btnReadZip = new Button("");
    private final Button btnReadHash = new Button("");
    private final Button btnWriteHash = new Button("Liste in Datei schreiben");

    private final Label lblWriteHash = new Label("Ok");

    private final TabPane tabPane = new TabPane();
    private final Tab tabDir = new Tab("Verzeichnis");
    private final Tab tabZip = new Tab("Zipdatei");
    private final Tab tabFile = new Tab("Hashdatei");
    private final Tab tabFilter = new Tab("Filter");

    private ProjectData projectData;
    private final ProgData progData;
    private final FileDataFilter fileDataFilter;

    private final GuiDirFactory guiDirFactory;
    private final GuiDirRunner guiDirRunner;
    private FileDataList fileDataList;
    private Table.TABLE TABLE;
    private final boolean panel1;

    enum DIR_ZIP_HASH {DIR, ZIP, HASH}

    public GuiDirRunnerPanel(ProgData progData, GuiDirRunner guiDirRunner, FileDataFilter fileDataFilter, boolean panel1) {
        this.progData = progData;
        this.guiDirRunner = guiDirRunner;
        this.fileDataFilter = fileDataFilter;
        this.guiDirFactory = new GuiDirFactory(progData);
        this.panel1 = panel1;

        this.projectData = progData.projectData;
        if (panel1) {
            fileDataList = progData.fileDataList1;
            TABLE = Table.TABLE.FILELIST1;
        } else {
            fileDataList = progData.fileDataList2;
            TABLE = Table.TABLE.FILELIST2;
        }
        generatePanel();
        initTable();
        initProjectData();
        addListener();
    }

    private void generatePanel() {

        scrollPane.setFitToHeight(true);
        scrollPane.setFitToWidth(true);
        scrollPane.setContent(table);

        //=======================
        // dir
        VBox vBoxDir = new VBox(2);
        vBoxDir.setPadding(new Insets(10));

        HBox hBoxDir = new HBox(10);
        HBox.setHgrow(pCboDir, Priority.ALWAYS);
        pCboDir.setMaxWidth(Double.MAX_VALUE);
        hBoxDir.getChildren().addAll(pCboDir, btnSelectDir, btnReadDir);
        vBoxDir.getChildren().addAll(new Label("Verzeichnis:"), hBoxDir);

        // zip
        VBox vBoxZip = new VBox(2);
        vBoxZip.setPadding(new Insets(10));

        HBox hBoxZip = new HBox(10);
        HBox.setHgrow(pCboZip, Priority.ALWAYS);
        pCboZip.setMaxWidth(Double.MAX_VALUE);
        hBoxZip.getChildren().addAll(pCboZip, btnSelectZip, btnReadZip);
        vBoxZip.getChildren().addAll(new Label("Zipdatei:"), hBoxZip);

        // hash
        VBox vBoxFile = new VBox(2);
        vBoxFile.setPadding(new Insets(10));

        HBox hBoxFile = new HBox(10);
        HBox.setHgrow(pCboHash, Priority.ALWAYS);
        pCboHash.setMaxWidth(Double.MAX_VALUE);
        pCboHash.setEditable(true);
        hBoxFile.getChildren().addAll(pCboHash, btnSelectHash, btnReadHash);
        vBoxFile.getChildren().addAll(new Label("Hashdatei:"), hBoxFile);

        // filter
        VBox vBoxSearch = new VBox(2);
        vBoxSearch.setPadding(new Insets(10));

        HBox hBoxSearch = new HBox(10);
        hBoxSearch.setAlignment(Pos.CENTER_LEFT);
        HBox.setHgrow(pCboSearch, Priority.ALWAYS);
        pCboSearch.setMaxWidth(Double.MAX_VALUE);
        hBoxSearch.getChildren().addAll(pCboSearch, btnClearFilter);

        vBoxSearch.getChildren().addAll(new Label("Dateien filtern:"), hBoxSearch);

        // Tabpane
        tabDir.setClosable(false);
        tabDir.setContent(vBoxDir);

        tabZip.setClosable(false);
        tabZip.setContent(vBoxZip);

        tabFile.setClosable(false);
        tabFile.setContent(vBoxFile);

        tabFilter.setClosable(false);
        tabFilter.setContent(vBoxSearch);

        tabPane.getTabs().addAll(tabDir, tabZip, tabFile, tabFilter);


        // =======================
        // write hash
        HBox hBoxWriteHash = new HBox(10);
        HBox.setHgrow(pCboWriteHash, Priority.ALWAYS);
        pCboWriteHash.setMaxWidth(Double.MAX_VALUE);
        pCboWriteHash.setEditable(true);
        hBoxWriteHash.getChildren().addAll(btnProposeHashName, pCboWriteHash, btnSelectHashList);

        HBox hBoxWrite = new HBox(10);
        hBoxWrite.setAlignment(Pos.CENTER_RIGHT);
        hBoxWrite.getChildren().addAll(lblWriteHash, btnWriteHash);

        // ================================
        VBox.setVgrow(scrollPane, Priority.ALWAYS);

        setPadding(new Insets(10));
        getChildren().addAll(tabPane, scrollPane,
                new Label("Hashdatei schreiben"), hBoxWriteHash, hBoxWrite);


        // =================================
        lblWriteHash.setVisible(false);
        lblWriteHash.setPadding(new Insets(10));

        btnReadDir.setMinWidth(btnReadDir.getPrefWidth());
        btnReadZip.setMinWidth(btnReadZip.getPrefWidth());
        btnReadHash.setMinWidth(btnReadHash.getPrefWidth());

        btnSelectDir.setGraphic(new Icons().ICON_BUTTON_FILE_OPEN);
        btnSelectZip.setGraphic(new Icons().ICON_BUTTON_FILE_OPEN);
        btnSelectHash.setGraphic(new Icons().ICON_BUTTON_FILE_OPEN);
        btnProposeHashName.setGraphic(new Icons().ICON_BUTTON_GUI_GEN_NAME);
        btnSelectHashList.setGraphic(new Icons().ICON_BUTTON_FILE_OPEN);
        btnReadDir.setGraphic(new Icons().ICON_BUTTON_GEN_HASH);
        btnReadZip.setGraphic(new Icons().ICON_BUTTON_GEN_HASH);
        btnReadHash.setGraphic(new Icons().ICON_BUTTON_GEN_HASH);
        btnClearFilter.setGraphic(new Icons().ICON_BUTTON_GUI_CLEAR);

        btnReadDir.setTooltip(new Tooltip("Verzeichnis einlesen."));
        btnReadZip.setTooltip(new Tooltip("Zipdatei einlesen."));
        btnReadHash.setTooltip(new Tooltip("Hashdatei einlesen."));
        btnSelectDir.setTooltip(new Tooltip("Verzeichnis auswählen."));
        btnSelectZip.setTooltip(new Tooltip("Zipdatei auswählen."));
        btnSelectHash.setTooltip(new Tooltip("Hashdatei auswählen"));
        btnSelectHashList.setTooltip(new Tooltip("Datei zum Speichern auswählen."));
        btnWriteHash.setTooltip(new Tooltip("Hashliste in Datei schreiben."));
        btnProposeHashName.setTooltip(new Tooltip("Einen Dateinamen vorschlagen."));
        btnClearFilter.setTooltip(new Tooltip("Filter löschen."));
    }

    private void initTable() {
        new Table().setTable(table, TABLE);
        table.setItems(fileDataList.getSortedFileData());
        changeTextFilter();
        fileDataList.getSortedFileData().comparatorProperty().bind(table.comparatorProperty());
    }

    private void initProjectData() {
        if (panel1) {
            pCboDir.init(projectData.getSrcDirList(), projectData.srcDir1Property());
            pCboZip.init(projectData.getSrcZipList(), projectData.srcZip1Property());
            pCboHash.init(projectData.getSrcHashList(), projectData.srcHash1Property());
            pCboSearch.init(projectData.getSearchList(), projectData.search1Property());
            pCboWriteHash.init(projectData.getWriteHashList(), projectData.writeHash1Property());
            tabPane.getSelectionModel().select(projectData.getSelTab1());
            projectData.selTab1Property().bind(tabPane.getSelectionModel().selectedIndexProperty());
        } else {
            pCboDir.init(projectData.getSrcDirList(), projectData.srcDir2Property());
            pCboZip.init(projectData.getSrcZipList(), projectData.srcZip2Property());
            pCboHash.init(projectData.getSrcHashList(), projectData.srcHash2Property());
            pCboSearch.init(projectData.getSearchList(), projectData.search2Property());
            pCboWriteHash.init(projectData.getWriteHashList(), projectData.writeHash2Property());
            tabPane.getSelectionModel().select(projectData.getSelTab2());
            projectData.selTab2Property().bind(tabPane.getSelectionModel().selectedIndexProperty());
        }
        setTabFilterText();
    }


    private void addListener() {
        progData.worker.addAdListener(new RunListener() {
            @Override
            public void ping(RunEvent runEvent) {
                if (runEvent.nixLos()) {
                    Table.refresh_table(table);
                }
            }
        });

        btnSelectDir.setOnAction(event -> DirFileChooser.DirChooser(ProgData.getInstance().primaryStage, pCboDir));
        btnSelectZip.setOnAction(event -> DirFileChooser.FileChooser(ProgData.getInstance().primaryStage, pCboZip));
        btnSelectHash.setOnAction(event -> DirFileChooser.FileChooser(ProgData.getInstance().primaryStage, pCboHash));
        btnSelectHashList.setOnAction(event -> DirFileChooser.FileChooser(ProgData.getInstance().primaryStage, pCboWriteHash));

        btnProposeHashName.setOnAction(event -> {
            String file;
            file = fileDataList.getSourceDir();
            if (file.isEmpty() && tabDir.isSelected()) {
                file = (panel1 ? projectData.getSrcDir1() : projectData.getSrcDir2());
            } else if (file.isEmpty() && tabFile.isSelected()) {
                file = (panel1 ? projectData.getSrcHash1() : projectData.getSrcHash2());
            }

            if (file.isEmpty()) {
                return;
            }

            if (!pCboWriteHash.getEditor().getText().startsWith(file)) {
                pCboWriteHash.getEditor().setText(file);
            }
            pCboWriteHash.selectElement(GuiTools.getNextName(pCboWriteHash.getEditor().getText()));
        });


        btnReadDir.disableProperty().bind(pCboDir.getEditor().textProperty().isNull()
                .or(pCboDir.getEditor().textProperty().isEqualTo("")));
        btnReadZip.disableProperty().bind(pCboZip.getEditor().textProperty().isNull()
                .or(pCboZip.getEditor().textProperty().isEqualTo("")));
        btnReadHash.disableProperty().bind(pCboHash.getEditor().textProperty().isNull()
                .or(pCboHash.getEditor().textProperty().isEqualTo("")));


        btnReadDir.setOnAction(a -> {
            if (readDirHash((panel1 ? projectData.getSrcDir1() : projectData.getSrcDir2()),
                    fileDataList,
                    (panel1 ? projectData.isFollowLink1() : projectData.isFollowLink2()))) {
                setTabDirFile(DIR_ZIP_HASH.DIR);
            }
            changeTextFilter();
        });


        btnReadZip.setOnAction(a -> {
            if (readZipHash((panel1 ? projectData.getSrcZip1() : projectData.getSrcZip2()), fileDataList)) {
                setTabDirFile(DIR_ZIP_HASH.ZIP);
            }
            changeTextFilter();
        });


        btnReadHash.setOnAction(a -> {
            if (readHashFile((panel1 ? projectData.getSrcHash1() : projectData.getSrcHash2()), fileDataList)) {
                setTabDirFile(DIR_ZIP_HASH.HASH);
            }
            changeTextFilter();
        });


        btnWriteHash.setOnAction(e -> writeHashFile());


        pCboSearch.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            changeTextFilter();
            setTabFilterText();
        });

        btnClearFilter.disableProperty().bind(pCboSearch.valueProperty().isNull()
                .or(pCboSearch.valueProperty().isEqualTo("")));
        btnClearFilter.setOnAction(a -> pCboSearch.getSelectionModel().select(""));
        btnWriteHash.disableProperty().bind(fileDataList.emptyProperty().
                or(pCboWriteHash.getEditor().textProperty().isEmpty()));
    }

    private void setTabFilterText() {
        if (pCboSearch.getValue() == null || pCboSearch.getValue().isEmpty()) {
            tabFilter.setGraphic(null);
            tabFilter.setStyle("-fx-font-weight: normal;");
        } else {
            tabFilter.setGraphic(new Icons().ICON_TAB_SEARCH);
            tabFilter.setStyle("-fx-font-weight: bold; -fx-underline: true;");
        }
    }

    private void setTabDirFile(DIR_ZIP_HASH dir_zip_hash) {
        // todo
        switch (dir_zip_hash) {
            case DIR:
                tabDir.setGraphic(new Icons().ICON_TAB_DIR_FILE);
                tabZip.setGraphic(null);
                tabFile.setGraphic(null);
                break;
            case ZIP:
                tabDir.setGraphic(null);
                tabZip.setGraphic(new Icons().ICON_TAB_DIR_FILE);
                tabFile.setGraphic(null);
                break;
            case HASH:
                tabDir.setGraphic(null);
                tabZip.setGraphic(null);
                tabFile.setGraphic(new Icons().ICON_TAB_DIR_FILE);
                break;
        }
    }

    private void changeTextFilter() {
        fileDataFilter.setSearchStr(pCboSearch.getSelectionModel().getSelectedItem());
        fileDataList.setPred(fileDataFilter);
    }

    private void clearFilter() {
        guiDirRunner.clearFilter();

//        fileDataFilter.setSearchStr(pCboSearch.getSelectionModel().getSelectedItem());
//        fileDataFilter.setFilter_types(FileDataFilter.FILTER_TYPES.ALL);
//        fileDataList.setPred(fileDataFilter);
    }

    public void saveTable() {
        new Table().saveTable(table, TABLE);
    }

    private boolean readDirHash(String hashDir, FileDataList fileDataList, boolean followLink) {
        boolean ret = guiDirFactory.readDirHash(hashDir, fileDataList, followLink);
        clearFilter();
        return ret;
    }

    private boolean readZipHash(String hashZip, FileDataList fileDataList) {
        boolean ret = guiDirFactory.readZipHash(hashZip, fileDataList);
        clearFilter();
        return ret;
    }

    private boolean readHashFile(String hashFile, FileDataList fileDataList) {
        boolean ret = guiDirFactory.readHashFile(hashFile, fileDataList);
        clearFilter();
        return ret;
    }

    private void writeHashFile() {
        guiDirFactory.writeHashFile(lblWriteHash,
                (panel1 ? projectData.getWriteHash1().trim() : projectData.getWriteHash2().trim()),
                fileDataList);
    }
}


