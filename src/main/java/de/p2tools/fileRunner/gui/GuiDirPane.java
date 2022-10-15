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
import de.p2tools.fileRunner.controller.config.RunPEvent;
import de.p2tools.fileRunner.controller.data.fileData.FileData;
import de.p2tools.fileRunner.controller.data.fileData.FileDataFilter;
import de.p2tools.fileRunner.controller.data.fileData.FileDataList;
import de.p2tools.fileRunner.controller.worker.CompareFileListFactory;
import de.p2tools.fileRunner.gui.table.Table;
import de.p2tools.fileRunner.gui.table.TableFileList;
import de.p2tools.fileRunner.icon.ProgIcons;
import de.p2tools.p2Lib.P2LibConst;
import de.p2tools.p2Lib.alert.PAlert;
import de.p2tools.p2Lib.dialogs.PDialogShowAgain;
import de.p2tools.p2Lib.dialogs.PDirFileChooser;
import de.p2tools.p2Lib.guiTools.*;
import de.p2tools.p2Lib.guiTools.pToggleSwitch.PToggleSwitchOnly;
import de.p2tools.p2Lib.tools.events.PEvent;
import de.p2tools.p2Lib.tools.events.PListener;
import de.p2tools.p2Lib.tools.file.PFileName;
import de.p2tools.p2Lib.tools.file.PFileUtils;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.StringProperty;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import org.apache.commons.io.FilenameUtils;

import java.nio.file.Path;
import java.util.Optional;

public class GuiDirPane extends VBox {

    private final ScrollPane scrollPane = new ScrollPane();
    private final TableFileList tableView;

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
    private final Button btnWriteHash = new Button("");

    private final TabPane tabPane = new TabPane();
    private final Tab tabDir = new Tab("Verzeichnis");
    private final Tab tabZip = new Tab("Zipdatei");
    private final Tab tabFile = new Tab("Hashdatei");
    private final Tab tabFilter = new Tab("Filter");

    private final ProgData progData;
    private final FileDataFilter fileDataFilter;

    private FileDataList fileDataList;
    private Table.TABLE_ENUM table_enum;
    private final boolean panel1;

    private final StringProperty srcDir;
    private final StringProperty srcZip;
    private final StringProperty srcHash;
    private final StringProperty filter;
    private final StringProperty writeHash;
    private final int selTab;
    private final IntegerProperty selTabIndex;
    private final BooleanProperty recursive;

    enum DIR_ZIP_HASH {DIR, ZIP, HASH}

    public GuiDirPane(ProgData progData, FileDataFilter fileDataFilter, boolean panel1) {
        this.progData = progData;
        this.fileDataFilter = fileDataFilter;
        this.panel1 = panel1;
        this.recursive = panel1 ? ProgConfig.CONFIG_COMPARE_WITH_PATH_1 : ProgConfig.CONFIG_COMPARE_WITH_PATH_2;

        if (panel1) {
            tableView = new TableFileList(Table.TABLE_ENUM.FILELIST_1);
            fileDataList = progData.fileDataList_1;
            table_enum = Table.TABLE_ENUM.FILELIST_1;

            srcDir = ProgConfig.searchDir1;
            srcZip = ProgConfig.searchZip1;
            srcHash = ProgConfig.searchHashFile1;
            filter = ProgConfig.filter1;
            writeHash = ProgConfig.writeHash1;

            selTab = ProgConfig.selTab1.get();
            selTabIndex = ProgConfig.selTab1;

        } else {
            tableView = new TableFileList(Table.TABLE_ENUM.FILELIST_2);
            fileDataList = progData.fileDataList_2;
            table_enum = Table.TABLE_ENUM.FILELIST_2;

            srcDir = ProgConfig.searchDir2;
            srcZip = ProgConfig.searchZip2;
            srcHash = ProgConfig.searchHashFile2;
            filter = ProgConfig.filter2;
            writeHash = ProgConfig.writeHash2;

            selTab = ProgConfig.selTab2.get();
            selTabIndex = ProgConfig.selTab2;
        }

        initTable();
        generatePanel();
        initProjectData();
        addListener();
    }

    public void saveTable() {
        Table.saveTable(tableView, table_enum);
    }

    public void openSelDir() {
        String path = getSelFilePath();
        if (path.isEmpty()) {
            return;
        }
        POpen.openDir(path, ProgConfig.SYSTEM_PROG_OPEN_DIR, ProgIcons.Icons.ICON_BUTTON_FILE_OPEN.getImageView());
    }

    public void copySelFile() {
        final Optional<FileData> fileData = getSel(true);
        if (!fileData.isPresent()) {
            return;
        }

        String fileName = fileData.get().getFileName();
        String srcFile = getSelFilePath();
        if (srcFile.isEmpty()) {
            return;
        }
        srcFile = PFileUtils.addsPath(srcFile, fileName);
        String destDir;
        if (table_enum.equals(Table.TABLE_ENUM.FILELIST_1)) {
            destDir = ProgConfig.lastUsedDir2.getValueSafe();
        } else {
            destDir = ProgConfig.lastUsedDir1.getValueSafe();
        }
        destDir = PDirFileChooser.DirChooser(progData.primaryStage, destDir);
        String destFile = PFileUtils.addsPath(destDir, fileName);

        Path srcPath = Path.of(srcFile);
        Path destPath = Path.of(destFile);
        PFileUtils.copyFile(srcPath, destPath, true);
    }

    public void copyAllSelFiles() {
        if (tableView.getSelectionModel().getSelectedItems().size() < 1) {
            //dann ist nichts markiert
            return;
        }

        String destDir;
        if (table_enum.equals(Table.TABLE_ENUM.FILELIST_1)) {
            destDir = ProgConfig.lastUsedDir2.getValueSafe();
        } else {
            destDir = ProgConfig.lastUsedDir1.getValueSafe();
        }
        destDir = PDirFileChooser.DirChooser(progData.primaryStage, destDir);

        for (FileData selected : tableView.getSelectionModel().getSelectedItems()) {
            String fileName = selected.getFileName();
            String s = selected.getPath();
            String srcFile = PFileUtils.addsPath(srcDir.getValueSafe(), s);
            if (srcFile.isEmpty()) {
                return;
            }
            srcFile = PFileUtils.addsPath(srcFile, fileName);
            String destFile = PFileUtils.addsPath(destDir, fileName);
            Path srcPath = Path.of(srcFile);
            Path destPath = Path.of(destFile);
            PFileUtils.copyFile(srcPath, destPath, true);
        }
    }

    private Optional<FileData> getSel(boolean show) {
        final int selectedTableRow = tableView.getSelectionModel().getSelectedIndex();
        if (selectedTableRow >= 0) {
            return Optional.of(tableView.getSelectionModel().getSelectedItem());
        } else {
            if (show) {
                PAlert.showInfoNoSelection();
            }
            return Optional.empty();
        }
    }

    private String getSelFilePath() {
        final Optional<FileData> fileData = getSel(true);
        if (!fileData.isPresent()) {
            return "";
        }

        String s = fileData.get().getPath();
        String path = PFileUtils.addsPath(srcDir.getValueSafe(), s);
        return path;
    }

    private void generatePanel() {
        scrollPane.setFitToHeight(true);
        scrollPane.setFitToWidth(true);
        scrollPane.setContent(tableView);

        //=======================
        // dir
        VBox vBoxDir = new VBox(2);
        vBoxDir.setPadding(new Insets(10));

        HBox hBoxDir = new HBox(10);
        HBox.setHgrow(pCboDir, Priority.ALWAYS);
        pCboDir.setMaxWidth(Double.MAX_VALUE);
        hBoxDir.getChildren().addAll(pCboDir, btnSelectDir, btnReadDir);
        vBoxDir.getChildren().addAll(new Label("Verzeichnis:"), hBoxDir);


        Label lblPath = new Label("Auch Unterverzeichnisse durchsuchen:");
        final PToggleSwitchOnly tglSubDir = new PToggleSwitchOnly();
        tglSubDir.setTooltip(new Tooltip("Es werden auch Dateien in Unterverzeichnissen verglichen"));
        tglSubDir.selectedProperty().bindBidirectional(recursive);
        tglSubDir.selectedProperty().addListener((v, o, n) -> {
            new PDialogShowAgain(progData.primaryStage, ProgConfig.SYSTEM_SUBDIR_SHOW_AGAIN_DIALOG_SIZE,
                    "Unterverzeichnisse durchsuchen",
                    "Wenn \"Auch Unterverzeichnisse durchsuchen\" ein- oder ausgeschaltet wird, " +
                            "wird die Tabelle mit den Dateien gelöscht. " + P2LibConst.LINE_SEPARATORx2 +
                            "Das Verzeichnis muss neu eingelesen werden.",
                    ProgConfig.SYSTEM_SUBDIR_SHOW_AGAIN_DIALOG_SHOW);

            //die HashID muss in der anderen Liste gelöscht werden, gibts ja nicht mehr
            (panel1 ? progData.fileDataList_1 : progData.fileDataList_2).clear();
            (!panel1 ? progData.fileDataList_1 : progData.fileDataList_2).stream().forEach(f -> f.setHashId(0));

            CompareFileListFactory.compareList();
        });
        Button btnHelpPathHash = PButton.helpButton(progData.primaryStage, "", HelpText.READ_DIR_RECURSIVE);
        HBox hBox = new HBox(10);
        hBox.setPadding(new Insets(5, 0, 0, 0));
        hBox.setAlignment(Pos.CENTER_RIGHT);
        hBox.getChildren().addAll(lblPath, PGuiTools.getHBoxGrower(), tglSubDir, btnHelpPathHash);
        vBoxDir.getChildren().addAll(hBox);


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
        hBoxWriteHash.getChildren().addAll(pCboWriteHash, btnSelectHashListFileForSaving, btnProposeHashName, btnWriteHash);

        // ================================
        VBox.setVgrow(scrollPane, Priority.ALWAYS);

        setPadding(new Insets(10));
        getChildren().addAll(tabPane, scrollPane,
                new Label("Hashdatei schreiben"), hBoxWriteHash/*, hBoxWrite*/);

        // =================================
        btnReadDir.setMinWidth(btnReadDir.getPrefWidth());
        btnReadZip.setMinWidth(btnReadZip.getPrefWidth());
        btnReadHash.setMinWidth(btnReadHash.getPrefWidth());

        btnSelectDir.setGraphic(ProgIcons.Icons.ICON_BUTTON_FILE_OPEN.getImageView());
        btnSelectZipfile.setGraphic(ProgIcons.Icons.ICON_BUTTON_FILE_OPEN.getImageView());
        btnSelectHashfile.setGraphic(ProgIcons.Icons.ICON_BUTTON_FILE_OPEN.getImageView());
        btnSelectHashListFileForSaving.setGraphic(ProgIcons.Icons.ICON_BUTTON_FILE_OPEN.getImageView());
        btnProposeHashName.setGraphic(ProgIcons.Icons.ICON_BUTTON_GUI_GEN_NAME.getImageView());
        btnWriteHash.setGraphic(ProgIcons.Icons.ICON_BUTTON_FILE_SAVE.getImageView());

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
        btnProposeHashName.setTooltip(new Tooltip("Einen Dateinamen vorschlagen."));
        btnWriteHash.setTooltip(new Tooltip("Hashliste in Datei schreiben."));
        btnClearFilter.setTooltip(new Tooltip("Filter löschen."));
    }

    private void initTable() {
        Table.setTable(tableView);
        tableView.setItems(fileDataList.getSortedFileData());
        changeTextFilter();
        fileDataList.getSortedFileData().comparatorProperty().bind(tableView.comparatorProperty());
        tableView.setOnMousePressed(m -> {
            if (m.getButton().equals(MouseButton.SECONDARY)) {
                tableView.setContextMenu(getMenu());
            }
        });
    }

    private ContextMenu getMenu() {
        final ContextMenu contextMenu = new ContextMenu();

        if (tabPane.getSelectionModel().getSelectedIndex() == 0) {
            MenuItem miOpenDirectory = new MenuItem("Ordner öffnen");
            if (table_enum.equals(Table.TABLE_ENUM.FILELIST_1)) {
                miOpenDirectory.setOnAction((ActionEvent event) ->
                        openSelDir());
            } else {
                miOpenDirectory.setOnAction((ActionEvent event) ->
                        openSelDir());
            }
            contextMenu.getItems().addAll(miOpenDirectory);
        }

        if (tabPane.getSelectionModel().getSelectedIndex() == 0) {
            if (tableView.getSelectionModel().getSelectedItems().size() == 1) {
                MenuItem miCopyFile = new MenuItem("Datei kopieren");
                miCopyFile.setOnAction((ActionEvent event) ->
                        copySelFile());
                contextMenu.getItems().addAll(miCopyFile);

            } else if (tableView.getSelectionModel().getSelectedItems().size() > 1) {
                MenuItem miCopyFile = new MenuItem("Markierte Dateien kopieren");
                miCopyFile.setOnAction((ActionEvent event) ->
                        copyAllSelFiles());
                contextMenu.getItems().addAll(miCopyFile);
            }
        }

        MenuItem miResetTable = new MenuItem("Tabelle zurücksetzen");
        miResetTable.setOnAction(a -> tableView.resetTable());
        contextMenu.getItems().addAll(miResetTable);

        return contextMenu;
    }

    private void initProjectData() {
        pCboDir.init(ProgConfig.searchDirList, srcDir);
        pCboZip.init(ProgConfig.searchZipList, srcZip);
        pCboHash.init(ProgConfig.searchHashFileList, srcHash);
        pCboFilter.init(ProgConfig.filterList, filter);
        pCboWriteHash.init(ProgConfig.writeHashList, writeHash);

        tabPane.getSelectionModel().select(selTab);
        selTabIndex.bind(tabPane.getSelectionModel().selectedIndexProperty());

        setTabFilterText();
    }

    private void addListener() {
        progData.pEventHandler.addListener(new PListener(Events.COMPARE_OF_FILE_LISTS_FINISHED) {
            public <T extends PEvent> void pingGui(T runEvent) {
                if (runEvent.getClass().equals(RunPEvent.class)) {
                    RunPEvent runE = (RunPEvent) runEvent;
                    if (runE.nixLos()) {
                        PTableFactory.refreshTable(tableView);
                        tableView.sort();
                    }
                }
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
                file = (panel1 ? ProgConfig.searchDir1.getValueSafe() : ProgConfig.searchDir2.getValueSafe());
            } else if (file.isEmpty() && tabZip.isSelected()) {
                file = (panel1 ? ProgConfig.searchZip1.getValueSafe() : ProgConfig.searchZip2.getValueSafe());
            } else if (file.isEmpty() && tabFile.isSelected()) {
                file = (panel1 ? ProgConfig.searchHashFile1.getValueSafe() : ProgConfig.searchHashFile2.getValueSafe());
            }

            if (file.isEmpty()) {
                return;
            }

            String onlyName = FilenameUtils.getName(file);
            onlyName = FilenameUtils.removeExtension(onlyName);
            String onlyPath = FilenameUtils.getFullPath(file);

            if (pCboWriteHash.getEditor().getText().isEmpty() ||
                    !pCboWriteHash.getEditor().getText().startsWith(onlyPath) ||
                    !pCboWriteHash.getEditor().getText().contains(onlyName)) {
                pCboWriteHash.getEditor().setText(file);
            }

            final String nextElement = PFileName.getNextFileNameWithDate(pCboWriteHash.getEditor().getText(), "md5");
            pCboWriteHash.selectElement(nextElement);
        });

        pCboDir.getEditor().textProperty().addListener((c, o, n) -> {
            fileDataList.clear();
            CompareFileListFactory.compareList();
        });
        pCboZip.getEditor().textProperty().addListener((c, o, n) -> {
            fileDataList.clear();
            CompareFileListFactory.compareList();
        });
        pCboHash.getEditor().textProperty().addListener((c, o, n) -> {
            fileDataList.clear();
            CompareFileListFactory.compareList();
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
            readHashFile();
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

    public void read() {
        switch (tabPane.getSelectionModel().getSelectedIndex()) {
            case 0:
                //Verzeichnis
                readDir();
                break;
            case 1:
                //Zip
                readZip();
                break;
            case 2:
                //Hashdatei
                readHashFile();
                break;
            default:
                //dann machmer nix
        }
    }

    private void readDir() {
        if (progData.worker.dirHash_readDirHash(panel1)) {
            setTabDirFile(DIR_ZIP_HASH.DIR);
        }
        changeTextFilter();
    }

    private void readZip() {
        if (progData.worker.dirHash_readZipHash(panel1)) {
            setTabDirFile(DIR_ZIP_HASH.ZIP);
        }
        changeTextFilter();
    }

    private void readHashFile() {
        if (progData.worker.dirHash_readHashFile(panel1)) {
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

    private void writeHashFile() {
        progData.worker.dirHash_writeHashFile(btnWriteHash,
                (panel1 ? ProgConfig.writeHash1.getValueSafe().trim() : ProgConfig.writeHash2.getValueSafe().trim()),
                fileDataList);
    }
}
