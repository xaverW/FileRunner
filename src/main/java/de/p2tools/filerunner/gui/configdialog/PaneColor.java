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

package de.p2tools.filerunner.gui.configdialog;

import de.p2tools.filerunner.controller.config.*;
import de.p2tools.filerunner.gui.HelpText;
import de.p2tools.p2lib.P2LibConst;
import de.p2tools.p2lib.colordata.P2ColorData;
import de.p2tools.p2lib.guitools.P2Button;
import de.p2tools.p2lib.guitools.P2ColumnConstraints;
import de.p2tools.p2lib.guitools.ptoggleswitch.P2ToggleSwitch;
import de.p2tools.p2lib.tools.P2ColorFactory;
import de.p2tools.p2lib.tools.events.P2Event;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.util.Collection;

public class PaneColor {

    private final Stage stage;

    private final P2ToggleSwitch tglDarkTheme = new P2ToggleSwitch("Dunkles Erscheinungsbild der Programmoberfläche");
    private final P2ToggleSwitch tglEvenOdd = new P2ToggleSwitch("Gerade/ungerade Zeilen farblich etwas absetzen");
    private final Slider slOdd = new Slider();

    public PaneColor(Stage stage) {
        this.stage = stage;
    }

    public void close() {
        tglDarkTheme.selectedProperty().unbindBidirectional(ProgConfig.SYSTEM_DARK_THEME);
        tglEvenOdd.selectedProperty().unbindBidirectional(ProgConfig.SYSTEM_EVEN_ODD);
        slOdd.valueProperty().unbindBidirectional(ProgConfig.SYSTEM_EVEN_ODD_VALUE);
    }

    public void makeColor(Collection<TitledPane> result) {
        final VBox vBox = new VBox();
        vBox.setPadding(new Insets(P2LibConst.PADDING));
        vBox.setFillWidth(true);
        vBox.setSpacing(10);

        final Button btnHelpTheme = P2Button.helpButton(stage, "Erscheinungsbild der Programmoberfläche",
                HelpText.DARK_THEME);
        final Button btnHelpEvenOdd = P2Button.helpButton(stage, "Erscheinungsbild der Programmoberfläche",
                HelpText.EVEN_ODD);

        tglDarkTheme.selectedProperty().bindBidirectional(ProgConfig.SYSTEM_DARK_THEME);
        tglDarkTheme.selectedProperty().addListener((u, o, n) -> {
            ProgData.getInstance().pEventHandler.notifyListener(new P2Event(Events.COLORS_CHANGED));
        });

        tglEvenOdd.selectedProperty().bindBidirectional(ProgConfig.SYSTEM_EVEN_ODD);
        tglEvenOdd.selectedProperty().addListener((v, o, n) -> ProgColorList.setColorTheme());
        tglEvenOdd.selectedProperty().addListener((u, o, n) ->
                ProgData.getInstance().pEventHandler.notifyListener(new P2Event(Events.COLORS_CHANGED)));


        Label lblSlider = new Label();
        slOdd.setMin(0);
        slOdd.setMax(50);
        slOdd.setBlockIncrement(5.0);
        slOdd.setShowTickMarks(true);
        slOdd.setMinWidth(300);
        slOdd.valueProperty().bindBidirectional(ProgConfig.SYSTEM_EVEN_ODD_VALUE);
        slOdd.valueChangingProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) {
                P2ColorData.ODD_DIV = (int) slOdd.getValue();
                ProgColorList.setColorTheme();
                ProgData.getInstance().pEventHandler.notifyListener(new P2Event(Events.COLORS_CHANGED));
            }
        });
        slOdd.valueProperty().addListener((v, o, n) -> lblSlider.setText(n.intValue() + ""));
        lblSlider.setText(((int) slOdd.getValue()) + "");


        TitledPane tpConfig = new TitledPane("Farben", vBox);
        result.add(tpConfig);


        final GridPane gridPane = new GridPane();
        gridPane.setHgap(P2LibConst.DIST_GRIDPANE_HGAP);
        gridPane.setVgap(P2LibConst.DIST_GRIDPANE_VGAP);
//        gridPane.setPadding(new Insets(P2LibConst.DIST_EDGE));
        gridPane.setPadding(new Insets(0, 0, P2LibConst.PADDING, 0));

        gridPane.add(tglDarkTheme, 0, 0);
        gridPane.add(btnHelpTheme, 1, 0);
        gridPane.add(tglEvenOdd, 0, 1);
        gridPane.add(btnHelpEvenOdd, 1, 1);

        HBox h = new HBox(5);
        h.getChildren().addAll(new Label("    "), slOdd, lblSlider);
        gridPane.add(h, 0, 2);
        GridPane.setHgrow(h, Priority.ALWAYS);
        GridPane.setHalignment(h, HPos.RIGHT);
        gridPane.getColumnConstraints().addAll(P2ColumnConstraints.getCcComputedSizeAndHgrow(), P2ColumnConstraints.getCcPrefSize());


        TableView<P2ColorData> tableView = new TableView<>();
        VBox.setVgrow(tableView, Priority.ALWAYS);
        initTableColor(tableView);
        tglDarkTheme.selectedProperty().addListener((u, o, n) -> {
            tableView.refresh();
        });


        Button button = new Button("Alle _Farben zurücksetzen");
        button.setOnAction(event -> {
            ProgColorList.resetAllColor();
            ProgData.getInstance().pEventHandler.notifyListener(new P2Event(Events.COLORS_CHANGED));
        });
        final Button btnHelpColor = P2Button.helpButton(stage, "Farben",
                HelpText.COLORS);
        HBox hBox = new HBox();
        hBox.getChildren().addAll(button, btnHelpColor);
        hBox.setSpacing(15);
        hBox.setPadding(new Insets(0));
        hBox.setAlignment(Pos.CENTER_RIGHT);


        vBox.getChildren().addAll(gridPane, tableView, hBox);
    }

    private void initTableColor(TableView<P2ColorData> tableView) {
        final TableColumn<P2ColorData, String> useColumn = new TableColumn<>("Verwenden");
        useColumn.setCellFactory(cellFactoryUse);
        useColumn.getStyleClass().add("alignCenter");

        final TableColumn<P2ColorData, String> textColumn = new TableColumn<>("Beschreibung");
        textColumn.setCellValueFactory(new PropertyValueFactory<>("text"));
        textColumn.setCellFactory(cellFactoryText);
        textColumn.getStyleClass().add("alignCenterLeft");

        final TableColumn<P2ColorData, String> changeColumn = new TableColumn<>("Farbe");
        changeColumn.setCellFactory(cellFactoryChange);
        changeColumn.getStyleClass().add("alignCenter");

        final TableColumn<P2ColorData, Color> colorColumn = new TableColumn<>("Farbe");
        colorColumn.setCellValueFactory(new PropertyValueFactory<>("color"));
        colorColumn.setCellFactory(cellFactoryColor);
        colorColumn.getStyleClass().add("alignCenter");

        final TableColumn<P2ColorData, Color> colorOrgColumn = new TableColumn<>("Original");
        colorOrgColumn.setCellValueFactory(new PropertyValueFactory<>("resetColor"));
        colorOrgColumn.setCellFactory(cellFactoryColorReset);
        colorOrgColumn.getStyleClass().add("alignCenter");

        final TableColumn<P2ColorData, String> resetColumn = new TableColumn<>("Reset");
        resetColumn.setCellFactory(cellFactoryReset);
        resetColumn.getStyleClass().add("alignCenter");

        tableView.setMinHeight(ProgConst.MIN_TABLE_HEIGHT);
        tableView.setColumnResizePolicy(TableView.UNCONSTRAINED_RESIZE_POLICY);

        textColumn.setPrefWidth(350);
        changeColumn.setPrefWidth(120);
        colorColumn.setPrefWidth(80);
        colorOrgColumn.setPrefWidth(80);

        tableView.getColumns().addAll(useColumn, textColumn, changeColumn, colorColumn, colorOrgColumn, resetColumn);
        tableView.setItems(ProgColorList.getInstance());
    }

    private Callback<TableColumn<P2ColorData, String>, TableCell<P2ColorData, String>> cellFactoryUse
            = (final TableColumn<P2ColorData, String> param) -> {

        final TableCell<P2ColorData, String> cell = new TableCell<P2ColorData, String>() {

            @Override
            public void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);

                if (empty) {
                    setGraphic(null);
                    setText(null);
                    return;
                }

                P2ColorData pColorData = getTableView().getItems().get(getIndex());

                final HBox hbox = new HBox();
                hbox.setSpacing(5);
                hbox.setAlignment(Pos.CENTER);
                hbox.setPadding(new Insets(0, 2, 0, 2));

                final CheckBox checkBox = new CheckBox("");
                checkBox.selectedProperty().bindBidirectional(pColorData.useProperty());
                checkBox.setOnAction(a -> {
                    ProgData.getInstance().pEventHandler.notifyListener(new P2Event(Events.COLORS_CHANGED));
                });

                hbox.getChildren().add(checkBox);
                setGraphic(hbox);
            }
        };

        return cell;
    };

    private Callback<TableColumn<P2ColorData, String>, TableCell<P2ColorData, String>> cellFactoryChange
            = (final TableColumn<P2ColorData, String> param) -> {

        final TableCell<P2ColorData, String> cell = new TableCell<P2ColorData, String>() {

            @Override
            public void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);

                if (empty) {
                    setGraphic(null);
                    setText(null);
                    return;
                }

                P2ColorData pColorData = getTableView().getItems().get(getIndex());

                final HBox hbox = new HBox();
                hbox.setSpacing(5);
                hbox.setAlignment(Pos.CENTER);
                hbox.setPadding(new Insets(0, 2, 0, 2));

                final ColorPicker colorPicker = new ColorPicker();
                colorPicker.getStyleClass().add("split-button");

                colorPicker.setValue(pColorData.getColor());
                colorPicker.setOnAction(a -> {
                    Color fxColor = colorPicker.getValue();
                    pColorData.setColor(fxColor);
                    ProgData.getInstance().pEventHandler.notifyListener(new P2Event(Events.COLORS_CHANGED));
                });
                hbox.getChildren().addAll(colorPicker);
                setGraphic(hbox);
            }
        };
        return cell;
    };
    private Callback<TableColumn<P2ColorData, String>, TableCell<P2ColorData, String>> cellFactoryText
            = (final TableColumn<P2ColorData, String> param) -> {

        final TableCell<P2ColorData, String> cell = new TableCell<P2ColorData, String>() {

            @Override
            public void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);

                if (empty) {
                    setGraphic(null);
                    setText(null);
                    return;
                }

                P2ColorData pColorData = getTableView().getItems().get(getIndex());
                if (pColorData.getMark() == 1) {
                    setStyle("-fx-font-weight: bold;");
                }
                setText(item);
            }
        };

        return cell;
    };

    private Callback<TableColumn<P2ColorData, Color>, TableCell<P2ColorData, Color>> cellFactoryColor
            = (final TableColumn<P2ColorData, Color> param) -> {

        final TableCell<P2ColorData, Color> cell = new TableCell<P2ColorData, Color>() {
            @Override
            public void updateItem(Color item, boolean empty) {
                super.updateItem(item, empty);

                if (empty) {
                    setGraphic(null);
                    setText(null);
                    return;
                }

                P2ColorData pColorData = getTableView().getItems().get(getIndex());
                setStyle("-fx-background-color:" + pColorData.getColorSelectedToWeb());
            }
        };

        return cell;
    };

    private Callback<TableColumn<P2ColorData, Color>, TableCell<P2ColorData, Color>> cellFactoryColorReset
            = (final TableColumn<P2ColorData, Color> param) -> {

        final TableCell<P2ColorData, Color> cell = new TableCell<P2ColorData, Color>() {


            @Override
            public void updateItem(Color item, boolean empty) {
                super.updateItem(item, empty);

                if (empty) {
                    setGraphic(null);
                    setText(null);
                    return;
                }

                P2ColorData pColorData = getTableView().getItems().get(getIndex());
                setStyle("-fx-background-color:" + P2ColorFactory.getColorToWeb(pColorData.getResetColor()));
            }

        };

        return cell;
    };

    private Callback<TableColumn<P2ColorData, String>, TableCell<P2ColorData, String>> cellFactoryReset
            = (final TableColumn<P2ColorData, String> param) -> {

        final TableCell<P2ColorData, String> cell = new TableCell<P2ColorData, String>() {

            @Override
            public void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);

                if (empty) {
                    setGraphic(null);
                    setText(null);
                    return;
                }

                P2ColorData pColorData = getTableView().getItems().get(getIndex());

                final HBox hbox = new HBox();
                hbox.setSpacing(5);
                hbox.setAlignment(Pos.CENTER);
                hbox.setPadding(new Insets(0, 2, 0, 2));

                final Button button = new Button("Reset");
                button.setOnAction(a -> {
                    pColorData.resetColor();
                    ProgData.getInstance().pEventHandler.notifyListener(new P2Event(Events.COLORS_CHANGED));
                });

                hbox.getChildren().add(button);
                setGraphic(hbox);
            }
        };
        return cell;
    };
}
