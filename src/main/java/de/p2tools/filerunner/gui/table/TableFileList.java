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


package de.p2tools.filerunner.gui.table;

import de.p2tools.filerunner.controller.config.Events;
import de.p2tools.filerunner.controller.config.ProgData;
import de.p2tools.filerunner.controller.data.filedata.FileData;
import de.p2tools.p2lib.guitools.ptable.P2CellCheckBox;
import de.p2tools.p2lib.p2event.P2Event;
import de.p2tools.p2lib.p2event.P2Listener;
import de.p2tools.p2lib.tools.date.P2Date;
import de.p2tools.p2lib.tools.file.P2FileSize;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Callback;

public class TableFileList extends PTable<FileData> {

    public TableFileList(Table.TABLE_ENUM table_enum) {
        super(table_enum);
        this.table_enum = table_enum;
        initFileRunnerColumn();

        Label lbl = new Label("Kein Inhalt in Tabelle");
        lbl.getStyleClass().add("lblTable");
        setPlaceholder(lbl);
    }

    public Table.TABLE_ENUM getETable() {
        return table_enum;
    }

    public void resetTable() {
        initFileRunnerColumn();
        Table.resetTable(this);
    }

    private void initFileRunnerColumn() {
        ProgData.getInstance().pEventHandler.addListener(new P2Listener(Events.COLORS_CHANGED) {
            public void pingGui(P2Event PEvent) {
                refresh();
            }
        });

        getColumns().clear();

        setTableMenuButtonVisible(true);
        setEditable(false);
        getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        setColumnResizePolicy(TableView.UNCONSTRAINED_RESIZE_POLICY);

        final TableColumn<FileData, Integer> idColumn = new TableColumn<>("HashID");
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        idColumn.setCellFactory(cellFileId);
        idColumn.getStyleClass().add("alignCenterRight");

        final TableColumn<FileData, Integer> filePathIdColumn = new TableColumn<>("FilePathID");
        filePathIdColumn.setCellValueFactory(new PropertyValueFactory<>("filePathId"));
        filePathIdColumn.setCellFactory(cellFileId);
        filePathIdColumn.getStyleClass().add("alignCenterRight");

        final TableColumn<FileData, Integer> fileNameIdColumn = new TableColumn<>("FileNameID");
        fileNameIdColumn.setCellValueFactory(new PropertyValueFactory<>("fileNameId"));
        fileNameIdColumn.setCellFactory(cellFileId);
        fileNameIdColumn.getStyleClass().add("alignCenterRight");

        final TableColumn<FileData, Integer> hashIdColumn = new TableColumn<>("HashID");
        hashIdColumn.setCellValueFactory(new PropertyValueFactory<>("hashId"));
        hashIdColumn.setCellFactory(cellFileId);
        hashIdColumn.getStyleClass().add("alignCenterRight");

        final TableColumn<FileData, String> filePathColumn = new TableColumn<>("Pfad");
        filePathColumn.setCellValueFactory(new PropertyValueFactory<>("path"));

        final TableColumn<FileData, String> fileNameColumn = new TableColumn<>("Dateiname");
        fileNameColumn.setCellValueFactory(new PropertyValueFactory<>("fileName"));

        final TableColumn<FileData, P2FileSize> fileSizeColumn = new TableColumn<>("Größe");
        fileSizeColumn.setCellValueFactory(new PropertyValueFactory<>("fileSize"));

        final TableColumn<FileData, P2Date> fileDateColumn = new TableColumn<>("Geändert");
        fileDateColumn.setCellValueFactory(new PropertyValueFactory<>("fileDate"));


        final TableColumn<FileData, Boolean> diff = new TableColumn<>("Diff");
        diff.setCellValueFactory(new PropertyValueFactory<>("diff"));
        diff.setCellFactory(new P2CellCheckBox().cellFactory);

        final TableColumn<FileData, Boolean> only = new TableColumn<>("Only");
        only.setCellValueFactory(new PropertyValueFactory<>("only"));
        only.setCellFactory(cellFactBool);

        idColumn.setPrefWidth(70);
        fileNameColumn.setPrefWidth(350);
        fileSizeColumn.setPrefWidth(75);
        fileDateColumn.setPrefWidth(90);
        diff.setPrefWidth(50);
        only.setPrefWidth(50);

        addRowFact();
        if (ProgData.debug) {
            getColumns().addAll(idColumn, filePathIdColumn, fileNameIdColumn, hashIdColumn,
                    filePathColumn, fileNameColumn,
                    diff, only, fileSizeColumn, fileDateColumn);
        } else {
            getColumns().addAll(idColumn, filePathColumn, fileNameColumn,
                    diff, only, fileSizeColumn, fileDateColumn);
        }
    }

    private void addRowFact() {
        setRowFactory(tv -> {
            TableFileListRow<FileData> row = new TableFileListRow<>(this);
            return row;
        });
    }

    private static Callback<TableColumn<FileData, Integer>, TableCell<FileData, Integer>> cellFileId =
            (final TableColumn<FileData, Integer> param) -> {

                final TableCell<FileData, Integer> cell = new TableCell<>() {

                    @Override
                    public void updateItem(Integer item, boolean empty) {
                        super.updateItem(item, empty);

                        if (item == null || empty) {
                            setGraphic(null);
                            setText(null);
                            return;
                        }

                        if (item == 0) {
                            setText("");
                        } else {
                            setText(item.toString());
                        }
                    }
                };
                return cell;
            };

    private static Callback<TableColumn<FileData, Boolean>, TableCell<FileData, Boolean>> cellFactBool =
            (final TableColumn<FileData, Boolean> param) -> {

                final TableCell<FileData, Boolean> cell = new TableCell<>() {

                    @Override
                    public void updateItem(Boolean item, boolean empty) {
                        super.updateItem(item, empty);

                        if (item == null || empty) {
                            setGraphic(null);
                            setText(null);
                            return;
                        }

                        setAlignment(Pos.CENTER);
                        CheckBox box = new CheckBox();
                        box.setDisable(true);
                        box.getStyleClass().add("checkbox-table");
                        box.setSelected(item.booleanValue());
                        setGraphic(box);
                    }
                };
                return cell;
            };
}
