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

package de.p2tools.fileRunner.gui.configDialog;

import de.p2tools.fileRunner.controller.config.*;
import de.p2tools.fileRunner.gui.HelpText;
import de.p2tools.p2Lib.configFile.pConfData.PColorData;
import de.p2tools.p2Lib.dialogs.accordion.PAccordionPane;
import de.p2tools.p2Lib.guiTools.PButton;
import de.p2tools.p2Lib.guiTools.PColumnConstraints;
import de.p2tools.p2Lib.guiTools.pToggleSwitch.PToggleSwitch;
import de.p2tools.p2Lib.tools.PColorFactory;
import de.p2tools.p2Lib.tools.events.PEvent;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
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

import java.util.ArrayList;
import java.util.Collection;

public class ColorPane extends PAccordionPane {

    private final Stage stage;
    BooleanProperty propDarkTheme = ProgConfig.SYSTEM_DARK_THEME;
    BooleanProperty propEvenOdd = ProgConfig.SYSTEM_EVEN_ODD;
    IntegerProperty propEvenOddValue = ProgConfig.SYSTEM_EVEN_ODD_VALUE;

    private final PToggleSwitch tglDarkTheme = new PToggleSwitch("Dunkles Erscheinungsbild der Programmoberfläche");
    private final PToggleSwitch tglEvenOdd = new PToggleSwitch("gerade/ungerade Zeilen farblich etwas absetzen");
    private final Slider slOdd = new Slider();

    public ColorPane(Stage stage) {
        super(stage, ProgConfig.CONFIG_DIALOG_ACCORDION, ProgConfig.SYSTEM_CONFIG_DIALOG_COLOR);
        this.stage = stage;

        init();
    }

    public void close() {
        super.close();
        tglDarkTheme.selectedProperty().unbindBidirectional(propDarkTheme);
        tglEvenOdd.selectedProperty().unbindBidirectional(propEvenOdd);
    }

    public Collection<TitledPane> createPanes() {
        Collection<TitledPane> result = new ArrayList<>();
        makeColor(result);
        return result;
    }

    private void makeColor(Collection<TitledPane> result) {
        final VBox vBox = new VBox();
        vBox.setPadding(new Insets(20));
        vBox.setFillWidth(true);
        vBox.setSpacing(10);

        final Button btnHelpTheme = PButton.helpButton(stage, "Erscheinungsbild der Programmoberfläche",
                HelpText.DARK_THEME);
        final Button btnHelpEvenOdd = PButton.helpButton(stage, "Erscheinungsbild der Programmoberfläche",
                HelpText.EVEN_ODD);

        tglDarkTheme.selectedProperty().bindBidirectional(propDarkTheme);
        tglDarkTheme.selectedProperty().addListener((u, o, n) -> {
            ProgData.getInstance().pEventHandler.notifyListener(new PEvent(Events.COLORS_CHANGED));
        });

        tglEvenOdd.selectedProperty().bindBidirectional(propEvenOdd);
        tglEvenOdd.selectedProperty().addListener((v, o, n) -> ProgColorList.setColorTheme());
        tglEvenOdd.selectedProperty().addListener((u, o, n) ->
                ProgData.getInstance().pEventHandler.notifyListener(new PEvent(Events.COLORS_CHANGED)));


        Label lblSlider = new Label();
        slOdd.setMin(1);
        slOdd.setMax(100);
        slOdd.setBlockIncrement(5.0);
        slOdd.setSnapToTicks(true);
        slOdd.setShowTickMarks(true);
        slOdd.setMinWidth(300);
        slOdd.valueProperty().bindBidirectional(propEvenOddValue);
        slOdd.valueChangingProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) {
                PColorData.ODD_DIV = slOdd.getValue() / 250;
                ProgColorList.setColorTheme();
                ProgData.getInstance().pEventHandler.notifyListener(new PEvent(Events.COLORS_CHANGED));
            }
        });
        slOdd.valueProperty().addListener((v, o, n) -> lblSlider.setText(n.intValue() + ""));
        lblSlider.setText(((int) slOdd.getValue()) + "");


        TitledPane tpConfig = new TitledPane("Farben", vBox);
        result.add(tpConfig);


        final GridPane gridPane = new GridPane();
        gridPane.setHgap(15);
        gridPane.setVgap(15);
        gridPane.setPadding(new Insets(0, 0, 10, 0));

        gridPane.add(tglDarkTheme, 0, 0);
        gridPane.add(btnHelpTheme, 1, 0);
        gridPane.add(tglEvenOdd, 0, 1);
        gridPane.add(btnHelpEvenOdd, 1, 1);

        HBox h = new HBox(5);
        h.getChildren().addAll(new Label("    "), slOdd, lblSlider);
        gridPane.add(h, 0, 2);
        GridPane.setHgrow(h, Priority.ALWAYS);
        GridPane.setHalignment(h, HPos.RIGHT);
        gridPane.getColumnConstraints().addAll(PColumnConstraints.getCcComputedSizeAndHgrow(), PColumnConstraints.getCcPrefSize());


        TableView<PColorData> tableView = new TableView<>();
        VBox.setVgrow(tableView, Priority.ALWAYS);
        initTableColor(tableView);
        tglDarkTheme.selectedProperty().addListener((u, o, n) -> {
            tableView.refresh();
        });


        Button button = new Button("Alle _Farben zurücksetzen");
        button.setOnAction(event -> {
            ProgColorList.resetAllColor();
            ProgData.getInstance().pEventHandler.notifyListener(new PEvent(Events.COLORS_CHANGED));
        });
        final Button btnHelpColor = PButton.helpButton(stage, "Farben",
                HelpText.COLORS);
        HBox hBox = new HBox();
        hBox.getChildren().addAll(button, btnHelpColor);
        hBox.setSpacing(15);
        hBox.setPadding(new Insets(0));
        hBox.setAlignment(Pos.CENTER_RIGHT);


        vBox.getChildren().addAll(gridPane, tableView, hBox);
    }

    private void initTableColor(TableView<PColorData> tableView) {
        final TableColumn<PColorData, String> useColumn = new TableColumn<>("Verwenden");
        useColumn.setCellFactory(cellFactoryUse);
        useColumn.getStyleClass().add("alignCenter");

        final TableColumn<PColorData, String> textColumn = new TableColumn<>("Beschreibung");
        textColumn.setCellValueFactory(new PropertyValueFactory<>("text"));
        textColumn.setCellFactory(cellFactoryText);
        textColumn.getStyleClass().add("alignCenterLeft");

        final TableColumn<PColorData, String> changeColumn = new TableColumn<>("Farbe");
        changeColumn.setCellFactory(cellFactoryChange);
        changeColumn.getStyleClass().add("alignCenter");

        final TableColumn<PColorData, Color> colorColumn = new TableColumn<>("Farbe");
        colorColumn.setCellValueFactory(new PropertyValueFactory<>("color"));
        colorColumn.setCellFactory(cellFactoryColor);
        colorColumn.getStyleClass().add("alignCenter");

        final TableColumn<PColorData, Color> colorOrgColumn = new TableColumn<>("Original");
        colorOrgColumn.setCellValueFactory(new PropertyValueFactory<>("resetColor"));
        colorOrgColumn.setCellFactory(cellFactoryColorReset);
        colorOrgColumn.getStyleClass().add("alignCenter");

        final TableColumn<PColorData, String> resetColumn = new TableColumn<>("Reset");
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

    private Callback<TableColumn<PColorData, String>, TableCell<PColorData, String>> cellFactoryUse
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

                PColorData pColorData = getTableView().getItems().get(getIndex());

                final HBox hbox = new HBox();
                hbox.setSpacing(5);
                hbox.setAlignment(Pos.CENTER);
                hbox.setPadding(new Insets(0, 2, 0, 2));

                final CheckBox checkBox = new CheckBox("");
                checkBox.selectedProperty().bindBidirectional(pColorData.useProperty());
                checkBox.setOnAction(a -> {
                    ProgData.getInstance().pEventHandler.notifyListener(new PEvent(Events.COLORS_CHANGED));
                });

                hbox.getChildren().add(checkBox);
                setGraphic(hbox);
            }
        };

        return cell;
    };

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

                PColorData pColorData = getTableView().getItems().get(getIndex());

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
                    ProgData.getInstance().pEventHandler.notifyListener(new PEvent(Events.COLORS_CHANGED));
                });
                hbox.getChildren().addAll(colorPicker);
                setGraphic(hbox);
            }
        };

        return cell;
    };
    private Callback<TableColumn<PColorData, String>, TableCell<PColorData, String>> cellFactoryText
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

                PColorData pColorData = getTableView().getItems().get(getIndex());
                if (pColorData.getMark() == 1) {
                    setStyle("-fx-font-weight: bold;");
                }
                setText(item);
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

                PColorData pColorData = getTableView().getItems().get(getIndex());
                setStyle("-fx-background-color:" + pColorData.getColorSelectedToWeb());
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

                PColorData pColorData = getTableView().getItems().get(getIndex());
                setStyle("-fx-background-color:" + PColorFactory.getColorToWeb(pColorData.getResetColor()));
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

                PColorData pColorData = getTableView().getItems().get(getIndex());

                final HBox hbox = new HBox();
                hbox.setSpacing(5);
                hbox.setAlignment(Pos.CENTER);
                hbox.setPadding(new Insets(0, 2, 0, 2));

                final Button button = new Button("Reset");
                button.setOnAction(a -> {
                    pColorData.resetColor();
                    ProgData.getInstance().pEventHandler.notifyListener(new PEvent(Events.COLORS_CHANGED));
                });

                hbox.getChildren().add(button);
                setGraphic(hbox);
            }
        };

        return cell;
    };

}
