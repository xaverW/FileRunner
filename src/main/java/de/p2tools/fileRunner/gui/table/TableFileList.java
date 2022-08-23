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
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
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
        getColumns().clear();

        setTableMenuButtonVisible(true);
        setEditable(false);
        getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        setColumnResizePolicy(TableView.UNCONSTRAINED_RESIZE_POLICY);

        final TableColumn<FileData, Integer> idColumn = new TableColumn<>("FileID");
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        idColumn.setCellFactory(cellFactId);
        idColumn.getStyleClass().add("alignCenterRight");

        final TableColumn<FileData, String> pathFileNameColumn = new TableColumn<>("Datei");
        pathFileNameColumn.setCellValueFactory(new PropertyValueFactory<>("pathFileName"));

        final TableColumn<FileData, PFileSize> fileSizeColumn = new TableColumn<>("Größe");
        fileSizeColumn.setCellValueFactory(new PropertyValueFactory<>("fileSize"));

        final TableColumn<FileData, PDate> fileDateColumn = new TableColumn<>("Geändert");
        fileDateColumn.setCellValueFactory(new PropertyValueFactory<>("fileDate"));

        final TableColumn<FileData, Integer> hashIdColumn = new TableColumn<>("HashID");
        hashIdColumn.setCellValueFactory(new PropertyValueFactory<>("hashId"));
        hashIdColumn.setCellFactory(cellFactId);
        hashIdColumn.getStyleClass().add("alignCenterRight");

        final TableColumn<FileData, Boolean> diff = new TableColumn<>("Diff");
        diff.setCellValueFactory(new PropertyValueFactory<>("diff"));
        diff.setCellFactory(new PCheckBoxCell().cellFactoryBool);

        final TableColumn<FileData, Boolean> only = new TableColumn<>("Only");
        only.setCellValueFactory(new PropertyValueFactory<>("only"));
        only.setCellFactory(cellFactBool);

        idColumn.setPrefWidth(70);
        pathFileNameColumn.setPrefWidth(350);
        fileSizeColumn.setPrefWidth(75);
        fileDateColumn.setPrefWidth(90);
        diff.setPrefWidth(50);
        only.setPrefWidth(50);

        addRowFact();
        getColumns().addAll(idColumn, pathFileNameColumn, fileSizeColumn, fileDateColumn, hashIdColumn, diff, only);
    }

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
                        box.setMaxHeight(6);
                        box.setMinHeight(6);
                        box.setPrefSize(6, 6);
                        box.setDisable(true);
                        box.getStyleClass().add("checkbox-table");
                        box.setSelected(item.booleanValue());

                        FileData fileData = getTableView().getItems().get(getIndex());
                        if (item.booleanValue() && fileData.getHashId() > 0) {
                            //dann gibts doppelte
                            box.setIndeterminate(true);
                        }

                        setGraphic(box);
                    }
                };
                return cell;
            };

    private static Callback<TableColumn<FileData, Integer>, TableCell<FileData, Integer>> cellFactId =
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

    private void addRowFact() {
        setRowFactory(tableview -> new TableRow<>() {
            @Override
            public void updateItem(FileData fileData, boolean empty) {
                super.updateItem(fileData, empty);

                setOnMouseClicked(event -> {
                    if (event.getButton().equals(MouseButton.PRIMARY) && event.getClickCount() == 2) {
                        getSelectionModel().clearSelection();
                    }
                });

                setStyle("");


                for (int i = 0; i < getChildren().size(); i++) {
                    getChildren().get(i).setStyle("");
                }

                if (isSelected()) {
                    if (ProgColorList.TABLE_ROW_IS_SEL_BG.isUse()) {
                        setStyle(ProgColorList.TABLE_ROW_IS_SEL_BG.getCssBackgroundAndSel());
                    }
                    if (ProgColorList.TABLE_ROW_IS_SEL.isUse()) {
                        for (int i = 0; i < getChildren().size(); i++) {
                            getChildren().get(i).setStyle(ProgColorList.TABLE_ROW_IS_SEL.getCssFont());
                        }
                    }
                    return;
                }

                //===========================================
                if (fileData != null && !empty) {
                    if (fileData.getId() != 0) {
                        //dann gibts eine gleiche Datei
                        //Hintergrundfarbe wird nach FileID gefärbt
                        if (fileData.getId() % 2 == 0) {
                            if (ProgColorList.FILE_IS_ID1_BG.isUse()) {
                                setStyle(ProgColorList.FILE_IS_ID1_BG.getCssBackground());
                            }
                        } else if (ProgColorList.FILE_IS_ID2_BG.isUse()) {
                            setStyle(ProgColorList.FILE_IS_ID2_BG.getCssBackground());
                        }

                        //die Schriftfarbe wird nach FileID gefärbt
                        if (fileData.getId() % 2 == 0) {
                            if (ProgColorList.FILE_IS_ID1.isUse()) {
                                for (int i = 0; i < getChildren().size(); i++) {
                                    getChildren().get(i).setStyle(ProgColorList.FILE_IS_ID1.getCssFont());
                                }
                            }
                        } else if (ProgColorList.FILE_IS_ID2.isUse()) {
                            for (int i = 0; i < getChildren().size(); i++) {
                                getChildren().get(i).setStyle(ProgColorList.FILE_IS_ID2.getCssFont());
                            }
                        }
                    }

                    //so werden auch gleiche Dateien im selben Verzeichnisbaum als
                    //only gekennzeichnet
                    if (fileData.isLink()) {
                        //Datei ist ein Symlink
                        if (ProgColorList.FILE_LINK_BG.isUse()) {
                            setStyle(ProgColorList.FILE_LINK_BG.getCssBackground());
                        }
                        if (ProgColorList.FILE_LINK.isUse()) {
                            for (int i = 0; i < getChildren().size(); i++) {
                                getChildren().get(i).setStyle(ProgColorList.FILE_LINK.getCssFont());
                            }
                        }

                    } else if (fileData.isDiff()) {
                        //Dateien sind unterschiedlich
                        if (ProgColorList.FILE_IS_DIFF_BG.isUse()) {
                            setStyle(ProgColorList.FILE_IS_DIFF_BG.getCssBackground());
                        }
                        if (ProgColorList.FILE_IS_DIFF.isUse()) {
                            for (int i = 0; i < getChildren().size(); i++) {
                                getChildren().get(i).setStyle(ProgColorList.FILE_IS_DIFF.getCssFont());
                            }
                        }

                    } else if (fileData.isOnly()) {
                        //Datei gibts nur einmal
                        if (ProgColorList.FILE_IS_ONLY_BG.isUse()) {
                            setStyle(ProgColorList.FILE_IS_ONLY_BG.getCssBackground());
                        }
                        if (ProgColorList.FILE_IS_ONLY.isUse()) {
                            for (int i = 0; i < getChildren().size(); i++) {
                                getChildren().get(i).setStyle(ProgColorList.FILE_IS_ONLY.getCssFont());
                            }
                        }

                    }
                }
                //===========================================
            }
        });
    }
}



