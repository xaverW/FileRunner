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

package de.p2tools.fileRunner.gui.dialog;

import de.p2tools.fileRunner.controller.config.ProgConfig;
import de.p2tools.fileRunner.controller.config.ProgData;
import de.p2tools.fileRunner.controller.worker.GetHash.HashFileEntry;
import de.p2tools.p2Lib.dialog.PDialogExtra;
import javafx.collections.ObservableList;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

public class SelectHashDialogController extends PDialogExtra {

    private final ProgData progData;
    private HashFileEntry hashFileEntry = null;
    private final ObservableList<HashFileEntry> list;
    private final TableView<HashFileEntry> tableView = new TableView<>();
    private final ScrollPane scrollPaneTable = new ScrollPane();
    private final Button btnOk = new Button("Ok");
    final Button btnCancel = new Button("Abbrechen");

    public SelectHashDialogController(ProgData progData, ObservableList<HashFileEntry> list) {
        super(progData.primaryStage, ProgConfig.SYSTEM_SELECT_HASH_DIALOG_SIZE,
                "Hash auswählen", true, true);

        this.progData = progData;
        this.list = list;

        init(getvBoxDialog(), true);
    }


    @Override
    public void make() {
        btnOk.setOnAction(a -> {
            hashFileEntry = tableView.getSelectionModel().getSelectedItem();
            if (hashFileEntry == null && !list.isEmpty()) {
                hashFileEntry = list.get(0);
            }
            close();
        });
        btnCancel.setOnAction(a -> {
            hashFileEntry = null;
            close();
        });
        getHboxOk().getChildren().addAll(btnOk, btnCancel);

//        scrollPaneTable.setContent(tableView);
//        scrollPaneTable.setFitToHeight(true);
//        scrollPaneTable.setFitToWidth(true);
//        VBox.setVgrow(scrollPaneTable, Priority.ALWAYS);

        VBox.setVgrow(tableView, Priority.ALWAYS);

        getVboxCont().getChildren().add(tableView);
        initTable();
    }

    public HashFileEntry getHashFileEntry() {

        return hashFileEntry;
    }

    private void initTable() {
        tableView.setTableMenuButtonVisible(true);
        tableView.setEditable(false);
        tableView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        tableView.setColumnResizePolicy(TableView.UNCONSTRAINED_RESIZE_POLICY);

        final TableColumn<HashFileEntry, String> hashColumn = new TableColumn<>("Hash");
        hashColumn.setCellValueFactory(new PropertyValueFactory<>("hash"));

        final TableColumn<HashFileEntry, String> fileColumn = new TableColumn<>("Datei");
        fileColumn.setCellValueFactory(new PropertyValueFactory<>("file"));

        tableView.getColumns().addAll(hashColumn, fileColumn);

        tableView.setItems(list);
    }
}
