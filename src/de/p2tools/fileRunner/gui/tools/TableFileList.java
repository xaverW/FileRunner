/*
 * MTPlayer Copyright (C) 2018 W. Xaver W.Xaver[at]googlemail.com
 * http://zdfmediathk.sourceforge.net/
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


package de.p2tools.fileRunner.gui.tools;

import de.p2tools.fileRunner.controller.data.fileData.FileData;
import de.p2tools.p2Lib.tools.Datum;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;

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

        final TableColumn<FileData, String> fileaNameColumn = new TableColumn<>("Datei");
        fileaNameColumn.setCellValueFactory(new PropertyValueFactory<>("fileName"));

        final TableColumn<FileData, Datum> fileaDateColumn = new TableColumn<>("LastMod");
        fileaDateColumn.setCellValueFactory(new PropertyValueFactory<>("fileDate"));

        nrColumn.setMaxWidth(1f * Integer.MAX_VALUE * 10);
        fileaNameColumn.setMaxWidth(1f * Integer.MAX_VALUE * 50);
        fileaDateColumn.setMaxWidth(1f * Integer.MAX_VALUE * 30);

        tableView.setOnMousePressed(m -> {
            if (m.getButton().equals(MouseButton.SECONDARY)) {
                tableView.setContextMenu(getMenu());
            }
        });

        return new TableColumn[]{
                nrColumn, fileaNameColumn, fileaDateColumn
        };

    }

    private ContextMenu getMenu() {
        final ContextMenu contextMenu = new ContextMenu();

        MenuItem resetTable = new MenuItem("Tabelle zurücksetzen");
        resetTable.setOnAction(a -> new Table().resetTable(tableView, Table.TABLE.FILELIST1));
        contextMenu.getItems().addAll(resetTable);
        return contextMenu;
    }
}
