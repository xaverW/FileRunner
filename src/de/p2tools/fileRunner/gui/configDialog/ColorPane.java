/*
 * MTPlayer Copyright (C) 2017 W. Xaver W.Xaver[at]googlemail.com
 * https://sourceforge.net/projects/mtplayer/
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

package de.p2tools.fileRunner.gui.configDialog;

import de.p2tools.fileRunner.controller.config.ProgColorList;
import de.p2tools.p2Lib.configFile.pConfData.PColorData;
import de.p2tools.p2Lib.configFile.pConfData.PColorList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.util.Callback;

public class ColorPane extends AnchorPane {

    public ColorPane() {
        makeColor();
    }

    private void makeColor() {
        final VBox vBox = new VBox();
        vBox.setSpacing(10);

        TableView<PColorData> tableView = new TableView<>();
        tableView.setMinHeight(Region.USE_PREF_SIZE);
        VBox.setVgrow(tableView, Priority.ALWAYS);
        initTableColor(tableView);

        Button button = new Button("Alle Farben zurücksetzen");
        button.setOnAction(event -> {
            ProgColorList.resetAllColor();
//            Listener.notify(Listener.EREIGNIS_GUI_COLOR_CHANGED, ColorPane.class.getSimpleName());
        });

        HBox hBox = new HBox();
        hBox.getChildren().add(button);
        hBox.setPadding(new Insets(5, 5, 5, 5));
        hBox.setAlignment(Pos.CENTER_RIGHT);

        vBox.getChildren().addAll(tableView, hBox);
        AnchorPane.setTopAnchor(vBox, 0.0);
        AnchorPane.setRightAnchor(vBox, 0.0);
        AnchorPane.setBottomAnchor(vBox, 0.0);
        AnchorPane.setLeftAnchor(vBox, 0.0);

        this.getChildren().add(vBox);
    }

    private void initTableColor(TableView<PColorData> tableView) {

        final TableColumn<PColorData, String> textColumn = new TableColumn<>("Beschreibung");
        textColumn.setCellValueFactory(new PropertyValueFactory<>("text"));

        final TableColumn<PColorData, String> changeColumn = new TableColumn<>("Farbe");
        changeColumn.setCellFactory(cellFactoryChange);
        changeColumn.getStyleClass().add("center");

        final TableColumn<PColorData, String> resetColumn = new TableColumn<>("Reset");
        resetColumn.setCellFactory(cellFactoryReset);
        resetColumn.getStyleClass().add("center");

        final TableColumn<PColorData, Color> colorColumn = new TableColumn<>("Farbe");
        colorColumn.setCellValueFactory(new PropertyValueFactory<>("color"));
        colorColumn.setCellFactory(cellFactoryColor);

        final TableColumn<PColorData, Color> colorOrgColumn = new TableColumn<>("Original");
        colorOrgColumn.setCellValueFactory(new PropertyValueFactory<>("colorReset"));
        colorOrgColumn.setCellFactory(cellFactoryColorReset);

        tableView.setColumnResizePolicy(TableView.UNCONSTRAINED_RESIZE_POLICY);

        tableView.getColumns().addAll(textColumn, changeColumn, colorColumn, colorOrgColumn, resetColumn);
        tableView.setItems(PColorList.getColorList());
    }

    private Callback<TableColumn<PColorData, String>, TableCell<PColorData, String>> cellFactoryChange
            = (final TableColumn<PColorData, String> param) -> {

        final TableCell<PColorData, String> cell = new TableCell<PColorData, String>() {

            @Override
            public void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);

                if (empty) {
                    setGraphic(null);
                    setText(null);
                    return;
                }

                PColorData MTC = getTableView().getItems().get(getIndex());

                final HBox hbox = new HBox();
                hbox.setSpacing(5);
                hbox.setAlignment(Pos.CENTER);
                hbox.setPadding(new Insets(0, 2, 0, 2));

                final ColorPicker colorPicker = new ColorPicker();
                colorPicker.getStyleClass().add("split-button");

                colorPicker.setValue(MTC.getColor());
                colorPicker.setOnAction(a -> {
                    Color fxColor = colorPicker.getValue();
                    MTC.setColor(fxColor);
//                    Daten.mTColor.save();
//                    Listener.notify(Listener.EREIGNIS_GUI_COLOR_CHANGED, ColorPane.class.getSimpleName());
                });
                hbox.getChildren().addAll(colorPicker);
                setGraphic(hbox);
            }
        };

        return cell;
    };

    private Callback<TableColumn<PColorData, String>, TableCell<PColorData, String>> cellFactoryReset
            = (final TableColumn<PColorData, String> param) -> {

        final TableCell<PColorData, String> cell = new TableCell<PColorData, String>() {

            @Override
            public void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);

                if (empty) {
                    setGraphic(null);
                    setText(null);
                    return;
                }

                PColorData MTC = getTableView().getItems().get(getIndex());

                final HBox hbox = new HBox();
                hbox.setSpacing(5);
                hbox.setAlignment(Pos.CENTER);
                hbox.setPadding(new Insets(0, 2, 0, 2));

                final Button button = new Button("Reset");
                button.setOnAction(a -> {
                    MTC.resetColor();
//                    Daten.mTColor.save();
//                    Listener.notify(Listener.EREIGNIS_GUI_COLOR_CHANGED, ColorPane.class.getSimpleName());
                });

                hbox.getChildren().add(button);
                setGraphic(hbox);
            }
        };

        return cell;
    };

    private Callback<TableColumn<PColorData, Color>, TableCell<PColorData, Color>> cellFactoryColor
            = (final TableColumn<PColorData, Color> param) -> {

        final TableCell<PColorData, Color> cell = new TableCell<PColorData, Color>() {


            @Override
            public void updateItem(Color item, boolean empty) {
                super.updateItem(item, empty);

                if (empty) {
                    setGraphic(null);
                    setText(null);
                    return;
                }

                PColorData MTC = getTableView().getItems().get(getIndex());
                setStyle("-fx-background-color:" + MTC.getColorToWeb());
            }

        };

        return cell;
    };
    private Callback<TableColumn<PColorData, Color>, TableCell<PColorData, Color>> cellFactoryColorReset
            = (final TableColumn<PColorData, Color> param) -> {

        final TableCell<PColorData, Color> cell = new TableCell<PColorData, Color>() {


            @Override
            public void updateItem(Color item, boolean empty) {
                super.updateItem(item, empty);

                if (empty) {
                    setGraphic(null);
                    setText(null);
                    return;
                }

                PColorData MTC = getTableView().getItems().get(getIndex());
                setStyle("-fx-background-color:" + MTC.getColorToWeb(MTC.getColorReset()));
            }

        };

        return cell;
    };

}
