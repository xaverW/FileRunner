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
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import javafx.util.Callback;

public class TableFileList {

    private TableView tableView;

    public TableColumn[] initFileRunnerColumn(TableView tableView) {
        this.tableView = tableView;

        tableView.getColumns().clear();

        tableView.setTableMenuButtonVisible(true);
        tableView.setEditable(false);
        tableView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        tableView.setColumnResizePolicy(TableView.UNCONSTRAINED_RESIZE_POLICY);

        final TableColumn<FileData, Integer> nrColumn = new TableColumn<>("Nr");
        nrColumn.setCellValueFactory(new PropertyValueFactory<>("nr"));

        final TableColumn<FileData, String> pathFileNameColumn = new TableColumn<>("Datei");
        pathFileNameColumn.setCellValueFactory(new PropertyValueFactory<>("pathFileName"));
        pathFileNameColumn.setCellFactory(cellFactoryStart);

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

        tableView.setOnMousePressed(m -> {
            if (m.getButton().equals(MouseButton.SECONDARY)) {
                tableView.setContextMenu(getMenu());
            }
        });
        addRowFact(tableView);
        return new TableColumn[]{
                nrColumn, pathFileNameColumn, fileSizeColumn, fileDateColumn, diff, only
        };
    }

    private ContextMenu getMenu() {
        final ContextMenu contextMenu = new ContextMenu();

        MenuItem resetTable = new MenuItem("Tabelle zurücksetzen");
        resetTable.setOnAction(a -> new Table().resetTable(tableView, Table.TABLE.FILELIST1));
        contextMenu.getItems().addAll(resetTable);
        return contextMenu;
    }

    private Callback<TableColumn<FileData, String>, TableCell<FileData, String>> cellFactoryStart
            = (final TableColumn<FileData, String> param) -> {

        final TableCell<FileData, String> cell = new TableCell<>() {

            @Override
            public void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);

                if (empty) {
                    setGraphic(null);
                    setText(null);
                    return;
                }
                FileData fileData = getTableView().getItems().get(getIndex());
                setText(fileData.getPathFileName());
            }
        };
        return cell;
    };

    private void addRowFact(TableView<FileData> table) {

        table.setRowFactory(tableview -> new TableRow<>() {
            @Override
            public void updateItem(FileData film, boolean empty) {
                super.updateItem(film, empty);

                if (film == null || empty) {
                    setStyle("");
                } else {
                    if (film.isLink()) {
                        // Datei ist ein Symlink
                        setStyle(ProgColorList.FILE_LINK_BG.getCssBackground());
                        for (int i = 0; i < getChildren().size(); i++) {
                            getChildren().get(i).setStyle(ProgColorList.FILE_LINK.getCssFont());
                        }
                    } else if (film.isDiff()) {
                        // Datei ist ein Symlink
                        setStyle(ProgColorList.FILE_IS_DIFF_BG.getCssBackground());
                        for (int i = 0; i < getChildren().size(); i++) {
                            getChildren().get(i).setStyle(ProgColorList.FILE_IS_DIFF.getCssFont());
                        }
                    } else if (film.isOnly()) {
                        // Datei ist ein Symlink
                        setStyle(ProgColorList.FILE_IS_ONLY_BG.getCssBackground());
                        for (int i = 0; i < getChildren().size(); i++) {
                            getChildren().get(i).setStyle(ProgColorList.FILE_IS_ONLY.getCssFont());
                        }
                    } else {
                        for (int i = 0; i < getChildren().size(); i++) {
                            getChildren().get(i).setStyle("");
                        }
                    }
                }
            }
        });
    }
}
