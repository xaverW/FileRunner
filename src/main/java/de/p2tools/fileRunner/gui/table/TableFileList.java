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


package de.p2tools.fileRunner.gui.table;

import de.p2tools.fileRunner.controller.config.ProgColorList;
import de.p2tools.fileRunner.controller.data.fileData.FileData;
import de.p2tools.p2Lib.guiTools.PCheckBoxCell;
import de.p2tools.p2Lib.tools.date.PDate;
import de.p2tools.p2Lib.tools.file.PFileSize;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

public class TableFileList {

    private static TableView tableView;
//    private static Table.TABLE eTable;
//    private static Table.TAB eTab = Table.TAB.TAB_DIR;

    public TableColumn[] initFileRunnerColumn(TableView tableView, Table.TABLE eTable) {
        this.tableView = tableView;
//        this.eTable = eTable;

        tableView.getColumns().clear();

        tableView.setTableMenuButtonVisible(true);
        tableView.setEditable(false);
        tableView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        tableView.setColumnResizePolicy(TableView.UNCONSTRAINED_RESIZE_POLICY);

        final TableColumn<FileData, Integer> nrColumn = new TableColumn<>("Nr");
        nrColumn.setCellValueFactory(new PropertyValueFactory<>("nr"));

        final TableColumn<FileData, String> pathFileNameColumn = new TableColumn<>("Datei");
        pathFileNameColumn.setCellValueFactory(new PropertyValueFactory<>("pathFileName"));

//        final TableColumn<FileData, String> buttonColumn = new TableColumn<>("");
//        buttonColumn.setCellValueFactory(new PropertyValueFactory<>("pathFileName"));
//        if (eTab.equals(Table.TAB.TAB_DIR)) {
//            buttonColumn.setCellFactory(cellFactoryButton);
//        }
//        buttonColumn.getStyleClass().add("alignCenter");

        final TableColumn<PFileSize, String> fileSizeColumn = new TableColumn<>("Größe");
        fileSizeColumn.setCellValueFactory(new PropertyValueFactory<>("fileSize"));

        final TableColumn<FileData, PDate> fileDateColumn = new TableColumn<>("Geändert");
        fileDateColumn.setCellValueFactory(new PropertyValueFactory<>("fileDate"));

        final TableColumn<FileData, Boolean> diff = new TableColumn<>("Diff");
        diff.setCellValueFactory(new PropertyValueFactory<>("diff"));
        diff.setCellFactory(new PCheckBoxCell().cellFactoryBool);

        final TableColumn<FileData, Boolean> only = new TableColumn<>("Only");
        only.setCellValueFactory(new PropertyValueFactory<>("only"));
        only.setCellFactory(new PCheckBoxCell().cellFactoryBool);

        nrColumn.setMaxWidth(1f * Integer.MAX_VALUE * 10);
        pathFileNameColumn.setMaxWidth(1f * Integer.MAX_VALUE * 50);
        fileSizeColumn.setMaxWidth(1f * Integer.MAX_VALUE * 10);
        fileDateColumn.setMaxWidth(1f * Integer.MAX_VALUE * 20);

//        tableView.setOnMousePressed(m -> {
//            if (m.getButton().equals(MouseButton.SECONDARY)) {
//                tableView.setContextMenu(getMenu());
//            }
//        });
        addRowFact(tableView);
        return new TableColumn[]{
                nrColumn, pathFileNameColumn/*, buttonColumn*/, fileSizeColumn, fileDateColumn, diff, only
        };
    }

//    public void setETab(Table.TAB eTab) {
//        this.eTab = eTab;
//    }

//    private ContextMenu getMenu() {
//        final ContextMenu contextMenu = new ContextMenu();
//
//        if (eTab.equals(Table.TAB.TAB_DIR)) {
//            MenuItem miOpenDirectory = new MenuItem("Ordner öffnen");
//            if (eTable.equals(Table.TABLE.FILELIST_1)) {
//                miOpenDirectory.setOnAction((ActionEvent event) ->
//                        ProgData.getInstance().guiDirRunner.getGuiDirPane(1).openSelDir());
//            } else {
//                miOpenDirectory.setOnAction((ActionEvent event) ->
//                        ProgData.getInstance().guiDirRunner.getGuiDirPane(2).openSelDir());
//            }
//            contextMenu.getItems().addAll(miOpenDirectory);
//        }
//
//        MenuItem miResetTable = new MenuItem("Tabelle zurücksetzen");
//        miResetTable.setOnAction(a -> new Table().resetTable(tableView, eTable));
//        contextMenu.getItems().addAll(miResetTable);
//
//        return contextMenu;
//    }

//    private Callback<TableColumn<FileData, String>, TableCell<FileData, String>> cellFactoryButton
//            = (final TableColumn<FileData, String> param) -> {
//
//        final TableCell<FileData, String> cell = new TableCell<FileData, String>() {
//
//            @Override
//            public void updateItem(String item, boolean empty) {
//                super.updateItem(item, empty);
//
//                if (item == null || empty) {
//                    setGraphic(null);
//                    setText(null);
//                    return;
//                }
//
//                FileData fileData = getTableView().getItems().get(getIndex());
//                setText(fileData.getPathFileName());
//
//                final HBox hbox = new HBox();
//                hbox.setSpacing(5);
//                hbox.setAlignment(Pos.CENTER);
//                hbox.setPadding(new Insets(0, 2, 0, 2));
//
//                final Button btnOpenDirectory;
//                btnOpenDirectory = new Button();
//                btnOpenDirectory.setTooltip(new Tooltip("Ordner öffnen"));
//                btnOpenDirectory.setGraphic(ProgIcons.Icons.IMAGE_TABLE_OPEN_DIR.getImageView());
//                btnOpenDirectory.setOnAction((ActionEvent event) -> {
//                    if (eTable.equals(Table.TABLE.FILELIST_1)) {
//                        ProgData.getInstance().guiDirRunner.getGuiDirPane(1).openSelDir();
//                    } else {
//                        ProgData.getInstance().guiDirRunner.getGuiDirPane(2).openSelDir();
//                    }
//                });
//
//                hbox.getChildren().addAll(btnOpenDirectory);
//                setGraphic(hbox);
//                setText("");
//            }
//        };
//        return cell;
//    };

    private void addRowFact(TableView<FileData> table) {

        table.setRowFactory(tableview -> new TableRow<>() {
            @Override
            public void updateItem(FileData film, boolean empty) {
                super.updateItem(film, empty);

                setStyle("");
                for (int i = 0; i < getChildren().size(); i++) {
                    getChildren().get(i).setStyle("");
                }

                if (film != null && !empty) {
                    if (film.isLink()) {
                        //Datei ist ein Symlink
                        if (ProgColorList.FILE_LINK_BG.isUse()) {
                            setStyle(ProgColorList.FILE_LINK_BG.getCssBackground());
                        }
                        if (ProgColorList.FILE_LINK.isUse()) {
                            for (int i = 0; i < getChildren().size(); i++) {
                                getChildren().get(i).setStyle(ProgColorList.FILE_LINK.getCssFont());
                            }
                        }

                    } else if (film.isDiff()) {
                        //Datei ist ein Symlink
                        if (ProgColorList.FILE_IS_DIFF_BG.isUse()) {
                            setStyle(ProgColorList.FILE_IS_DIFF_BG.getCssBackground());
                        }
                        if (ProgColorList.FILE_IS_DIFF.isUse()) {
                            for (int i = 0; i < getChildren().size(); i++) {
                                getChildren().get(i).setStyle(ProgColorList.FILE_IS_DIFF.getCssFont());
                            }
                        }

                    } else if (film.isOnly()) {
                        //Datei ist ein Symlink
                        if (ProgColorList.FILE_IS_ONLY_BG.isUse()) {
                            setStyle(ProgColorList.FILE_IS_ONLY_BG.getCssBackground());
                        }
                        if (ProgColorList.FILE_IS_ONLY.isUse()) {
                            for (int i = 0; i < getChildren().size(); i++) {
                                getChildren().get(i).setStyle(ProgColorList.FILE_IS_ONLY.getCssFont());
                            }
                        }

                    } else {
                        //dann ist die Datei gleich
                        if (ProgColorList.FILE_IS_OK_BG.isUse()) {
                            setStyle(ProgColorList.FILE_IS_OK_BG.getCssBackground());
                        }
                        if (ProgColorList.FILE_IS_OK.isUse()) {
                            for (int i = 0; i < getChildren().size(); i++) {
                                getChildren().get(i).setStyle(ProgColorList.FILE_IS_OK.getCssFont());
                            }
                        }
                    }
                }
            }
        });
    }
}
