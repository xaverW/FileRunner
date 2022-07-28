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

import de.p2tools.fileRunner.controller.config.Events;
import de.p2tools.fileRunner.controller.config.ProgConfig;
import de.p2tools.fileRunner.controller.config.ProgData;
import de.p2tools.fileRunner.controller.data.fileData.FileDataFilter;
import de.p2tools.fileRunner.controller.data.fileData.FileDataList;
import de.p2tools.fileRunner.controller.worker.HashFactory;
import de.p2tools.fileRunner.controller.worker.compare.CompareFileList;
import de.p2tools.fileRunner.gui.table.Table;
import de.p2tools.fileRunner.icon.ProgIcons;
import de.p2tools.p2Lib.dialogs.PDirFileChooser;
import de.p2tools.p2Lib.guiTools.PComboBoxString;
import de.p2tools.p2Lib.tools.events.Event;
import de.p2tools.p2Lib.tools.events.PListener;
import de.p2tools.p2Lib.tools.events.RunEvent;
import de.p2tools.p2Lib.tools.file.PFileName;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.StringProperty;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

public class GuiDirPane extends VBox {

    private final ScrollPane scrollPane = new ScrollPane();
    private final TableView table = new TableView();

    private final PComboBoxString pCboDir = new PComboBoxString();
    private final PComboBoxString pCboZip = new PComboBoxString();
    private final PComboBoxString pCboHash = new PComboBoxString();
    private final PComboBoxString pCboWriteHash = new PComboBoxString();
    private final PComboBoxString pCboFilter = new PComboBoxString();

    private final Button btnSelectDir = new Button("");
    private final Button btnSelectZipfile = new Button("");
    private final Button btnSelectHashfile = new Button("");
    private final Button btnSelectHashListFileForSaving = new Button();

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

    //    private final ProjectData projectData;
    private final ProgData progData;
    private final FileDataFilter fileDataFilter;

    private FileDataList fileDataList;
    private Table.TABLE TABLE;
    private final boolean panel1;

    private final StringProperty srcDir;
    private final StringProperty srcZip;
    private final StringProperty srcHash;
    private final StringProperty filter;
    private final StringProperty writeHash;
    private final int selTab;
    private final IntegerProperty selIndex;


    enum DIR_ZIP_HASH {DIR, ZIP, HASH}

    public GuiDirPane(ProgData progData, FileDataFilter fileDataFilter, boolean panel1) {
        this.progData = progData;
        this.fileDataFilter = fileDataFilter;
        this.panel1 = panel1;

        if (panel1) {
            fileDataList = progData.fileDataList1;
            TABLE = Table.TABLE.FILELIST1;

            srcDir = ProgConfig.srcDir1;
            srcZip = ProgConfig.srcZip1;
            srcHash = ProgConfig.srcHash1;
            filter = ProgConfig.filter1;
            writeHash = ProgConfig.writeHash1;

            selTab = ProgConfig.selTab1.get();
            selIndex = ProgConfig.selTab1;

        } else {
            fileDataList = progData.fileDataList2;
            TABLE = Table.TABLE.FILELIST2;

            srcDir = ProgConfig.srcDir2;
            srcZip = ProgConfig.srcZip2;
            srcHash = ProgConfig.srcHash2;
            filter = ProgConfig.filter2;
            writeHash = ProgConfig.writeHash2;

            selTab = ProgConfig.selTab2.get();
            selIndex = ProgConfig.selTab2;
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
        hBoxZip.getChildren().addAll(pCboZip, btnSelectZipfile, btnReadZip);
        vBoxZip.getChildren().addAll(new Label("Zipdatei:"), hBoxZip);

        // hash
        VBox vBoxFile = new VBox(2);
        vBoxFile.setPadding(new Insets(10));

        HBox hBoxFile = new HBox(10);
        HBox.setHgrow(pCboHash, Priority.ALWAYS);
        pCboHash.setMaxWidth(Double.MAX_VALUE);
        pCboHash.setEditable(true);
        hBoxFile.getChildren().addAll(pCboHash, btnSelectHashfile, btnReadHash);
        vBoxFile.getChildren().addAll(new Label("Hashdatei:"), hBoxFile);

        // filter
        VBox vBoxSearch = new VBox(2);
        vBoxSearch.setPadding(new Insets(10));

        HBox hBoxSearch = new HBox(10);
        hBoxSearch.setAlignment(Pos.CENTER_LEFT);
        HBox.setHgrow(pCboFilter, Priority.ALWAYS);
        pCboFilter.setMaxWidth(Double.MAX_VALUE);
        hBoxSearch.getChildren().addAll(pCboFilter, btnClearFilter);

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
        tabPane.setMinHeight(Region.USE_PREF_SIZE);

        // =======================
        // write hash
        HBox hBoxWriteHash = new HBox(10);
        HBox.setHgrow(pCboWriteHash, Priority.ALWAYS);
        pCboWriteHash.setMaxWidth(Double.MAX_VALUE);
        pCboWriteHash.setEditable(true);
        hBoxWriteHash.getChildren().addAll(pCboWriteHash, btnSelectHashListFileForSaving, btnProposeHashName);

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

        btnSelectDir.setGraphic(ProgIcons.Icons.ICON_BUTTON_FILE_OPEN.getImageView());
        btnSelectZipfile.setGraphic(ProgIcons.Icons.ICON_BUTTON_FILE_OPEN.getImageView());
        btnSelectHashfile.setGraphic(ProgIcons.Icons.ICON_BUTTON_FILE_OPEN.getImageView());
        btnProposeHashName.setGraphic(ProgIcons.Icons.ICON_BUTTON_GUI_GEN_NAME.getImageView());
        btnSelectHashListFileForSaving.setGraphic(ProgIcons.Icons.ICON_BUTTON_FILE_OPEN.getImageView());
        btnReadDir.setGraphic(ProgIcons.Icons.ICON_BUTTON_GEN_HASH.getImageView());
        btnReadZip.setGraphic(ProgIcons.Icons.ICON_BUTTON_GEN_HASH.getImageView());
        btnReadHash.setGraphic(ProgIcons.Icons.ICON_BUTTON_GEN_HASH.getImageView());
        btnClearFilter.setGraphic(ProgIcons.Icons.ICON_BUTTON_GUI_CLEAR.getImageView());

        btnReadDir.setTooltip(new Tooltip("Verzeichnis einlesen."));
        btnReadZip.setTooltip(new Tooltip("Zipdatei einlesen."));
        btnReadHash.setTooltip(new Tooltip("Hashdatei einlesen."));
        btnSelectDir.setTooltip(new Tooltip("Verzeichnis auswählen."));
        btnSelectZipfile.setTooltip(new Tooltip("Zipdatei auswählen."));
        btnSelectHashfile.setTooltip(new Tooltip("Hashdatei auswählen"));
        btnSelectHashListFileForSaving.setTooltip(new Tooltip("Datei zum Speichern auswählen."));
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
        pCboDir.init(ProgConfig.srcDirList, srcDir);
        pCboZip.init(ProgConfig.srcZipList, srcZip);
        pCboHash.init(ProgConfig.srcHashList, srcHash);
        pCboFilter.init(ProgConfig.filterList, filter);
        pCboWriteHash.init(ProgConfig.writeHashList, writeHash);

        tabPane.getSelectionModel().select(selTab);
        selIndex.bind(tabPane.getSelectionModel().selectedIndexProperty());

        setTabFilterText();
    }

    private void addListener() {
        progData.pEventHandler.addListener(new PListener(Events.COMPARE_OF_FILE_LISTS_FINISHED) {
            public <T extends Event> void pingGui(T runEvent) {
                if (runEvent.getClass().equals(RunEvent.class)) {
                    RunEvent runE = (RunEvent) runEvent;
                    if (runE.nixLos()) {
                        Table.refresh_table(table);
                    }
                }
            }
        });
        progData.pEventHandler.addListener(new PListener(Events.COLORS_CHANGED) {
            public void pingGui(Event event) {
                Table.refresh_table(table);
            }
        });

        btnSelectDir.setOnAction(event -> {
            String dir = "";
            if (pCboDir.getEditor().getText().isEmpty()) {
                dir = PDirFileChooser.DirChooser(progData.primaryStage, pCboDir,
                        panel1 ? ProgConfig.lastUsedDir1.getValueSafe() : ProgConfig.lastUsedDir2.getValueSafe());
            } else {
                dir = PDirFileChooser.DirChooser(progData.primaryStage, pCboDir);
            }
            GuiFactory.setLastUsedDir(dir, panel1);
        });
        btnSelectZipfile.setOnAction(event -> {
            String dir = "";
            if (pCboZip.getEditor().getText().isEmpty()) {
                dir = PDirFileChooser.FileChooser(progData.primaryStage, pCboZip,
                        panel1 ? ProgConfig.lastUsedDir1.getValueSafe() : ProgConfig.lastUsedDir2.getValueSafe());
            } else {
                dir = PDirFileChooser.FileChooser(progData.primaryStage, pCboZip);
            }
            GuiFactory.setLastUsedDir(dir, panel1);
        });
        btnSelectHashfile.setOnAction(event -> {
            String dir = "";
            if (pCboHash.getEditor().getText().isEmpty()) {
                dir = PDirFileChooser.FileChooser(progData.primaryStage, pCboHash,
                        panel1 ? ProgConfig.lastUsedDir1.getValueSafe() : ProgConfig.lastUsedDir2.getValueSafe());
            } else {
                dir = PDirFileChooser.FileChooser(progData.primaryStage, pCboHash);
            }
            GuiFactory.setLastUsedDir(dir, panel1);
        });
        btnSelectHashListFileForSaving.setOnAction(event -> PDirFileChooser.FileChooser(progData.primaryStage, pCboWriteHash));

        btnProposeHashName.setOnAction(event -> {
            String file;
            file = fileDataList.getSourceDir();
            if (file.isEmpty() && tabDir.isSelected()) {
                file = (panel1 ? ProgConfig.srcDir1.getValueSafe() : ProgConfig.srcDir2.getValueSafe());
            } else if (file.isEmpty() && tabFile.isSelected()) {
                file = (panel1 ? ProgConfig.srcHash1.getValueSafe() : ProgConfig.srcHash2.getValueSafe());
            }

            if (file.isEmpty()) {
                return;
            }

            if (pCboWriteHash.getEditor().getText().isEmpty()) {
                pCboWriteHash.getEditor().setText(file);
            }
            final String nextElement = PFileName.getNextFileNameWithDate(pCboWriteHash.getEditor().getText(), "md5");
            pCboWriteHash.selectElement(nextElement);
        });

        pCboDir.getEditor().textProperty().addListener((c, o, n) -> {
            fileDataList.clear();
            new CompareFileList().compareList();
        });
        pCboZip.getEditor().textProperty().addListener((c, o, n) -> {
            fileDataList.clear();
            new CompareFileList().compareList();
        });
        pCboHash.getEditor().textProperty().addListener((c, o, n) -> {
            fileDataList.clear();
            new CompareFileList().compareList();
        });

        btnReadDir.disableProperty().bind(pCboDir.getEditor().textProperty().isNull()
                .or(pCboDir.getEditor().textProperty().isEqualTo("")));
        btnReadZip.disableProperty().bind(pCboZip.getEditor().textProperty().isNull()
                .or(pCboZip.getEditor().textProperty().isEqualTo("")));
        btnReadHash.disableProperty().bind(pCboHash.getEditor().textProperty().isNull()
                .or(pCboHash.getEditor().textProperty().isEqualTo("")));

        btnReadDir.setOnAction(a -> {
            readDir();
        });
        btnReadZip.setOnAction(a -> {
            readZip();
        });
        btnReadHash.setOnAction(a -> {
            readHashfile();
        });
        btnWriteHash.setOnAction(e -> writeHashFile());

        pCboFilter.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            changeTextFilter();
            setTabFilterText();
        });

        btnClearFilter.disableProperty().bind(pCboFilter.valueProperty().isNull()
                .or(pCboFilter.valueProperty().isEqualTo("")));
        btnClearFilter.setOnAction(a -> pCboFilter.getSelectionModel().select(""));
        btnWriteHash.disableProperty().bind(fileDataList.emptyProperty().
                or(pCboWriteHash.getEditor().textProperty().isEmpty()));
    }

    private void readDir() {
        if (GuiFactory.readDirHash((panel1 ? ProgConfig.srcDir1.getValueSafe() : ProgConfig.srcDir2.getValueSafe()),
                fileDataList,
                (panel1 ? ProgConfig.followLink1.get() : ProgConfig.followLink2.get()))) {
            setTabDirFile(DIR_ZIP_HASH.DIR);
        }
        changeTextFilter();
    }

    private void readZip() {
        if (GuiFactory.readZipHash((panel1 ? ProgConfig.srcZip1.getValueSafe() : ProgConfig.srcZip2.getValueSafe()), fileDataList)) {
            setTabDirFile(DIR_ZIP_HASH.ZIP);
        }
        changeTextFilter();
    }

    private void readHashfile() {
        if (GuiFactory.readHashFile((panel1 ? ProgConfig.srcHash1.getValueSafe() : ProgConfig.srcHash2.getValueSafe()), fileDataList)) {
            setTabDirFile(DIR_ZIP_HASH.HASH);
        }
        changeTextFilter();
    }

    private void changeTextFilter() {
        fileDataFilter.setSearchStr(pCboFilter.getSelectionModel().getSelectedItem());
        fileDataList.setPred(fileDataFilter);
    }

    private void setTabDirFile(DIR_ZIP_HASH dir_zip_hash) {
        switch (dir_zip_hash) {
            case DIR:
                tabDir.setGraphic(ProgIcons.Icons.ICON_TAB_DIR_FILE.getImageView());
                tabZip.setGraphic(null);
                tabFile.setGraphic(null);
                break;
            case ZIP:
                tabDir.setGraphic(null);
                tabZip.setGraphic(ProgIcons.Icons.ICON_TAB_DIR_FILE.getImageView());
                tabFile.setGraphic(null);
                break;
            case HASH:
                tabDir.setGraphic(null);
                tabZip.setGraphic(null);
                tabFile.setGraphic(ProgIcons.Icons.ICON_TAB_DIR_FILE.getImageView());
                break;
        }
    }

    private void setTabFilterText() {
        if (pCboFilter.getValue() == null || pCboFilter.getValue().isEmpty()) {
            tabFilter.setGraphic(null);
            tabFilter.setStyle("-fx-font-weight: normal;");
        } else {
            tabFilter.setGraphic(ProgIcons.Icons.ICON_TAB_SEARCH.getImageView());
            tabFilter.setStyle("-fx-font-weight: bold; -fx-underline: true;");
        }
    }

    public void saveTable() {
        new Table().saveTable(table, TABLE);
    }

    private void writeHashFile() {
        HashFactory.writeHashFile(lblWriteHash,
                (panel1 ? ProgConfig.writeHash1.getValueSafe().trim() : ProgConfig.writeHash2.getValueSafe().trim()),
                fileDataList);
    }
}
