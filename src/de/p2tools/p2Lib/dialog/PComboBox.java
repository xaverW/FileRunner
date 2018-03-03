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


package de.p2tools.p2Lib.dialog;

import javafx.beans.property.StringProperty;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.scene.control.ComboBox;
import javafx.scene.input.MouseEvent;

import java.util.ArrayList;
import java.util.Collections;

public class PComboBox extends ComboBox<String> {

    public final int MAX_ELEMENTS = 15;

    private int maxElements = MAX_ELEMENTS;
    private StringProperty stringProperty = null;
    private ObservableList<String> data = null;

    public int getMaxElements() {
        return maxElements;
    }

    public void setMaxElements(int maxElements) {
        this.maxElements = maxElements;
    }

    public void init(ObservableList<String> data, StringProperty stringProperty) {
        this.stringProperty = stringProperty;
        this.data = data;

        if (!getItems().contains(stringProperty.getValueSafe())) {
            getItems().add(stringProperty.getValueSafe());
        }
        getSelectionModel().select(stringProperty.getValueSafe());

        setCombo();
    }

    public void init(ObservableList<String> data, String init, StringProperty stringProperty) {
        this.stringProperty = stringProperty;
        this.data = data;

        if (!getItems().contains(init)) {
            getItems().add(init);
        }
        getSelectionModel().select(init);

        setCombo();
    }

    private void setCombo() {
        if (data == null || stringProperty == null) {
            return;
        }

        Collections.sort(data);
        setItems(data);

        getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == null) {
                return;
            }
            if (!data.contains(newValue)) {
                data.add(newValue);
            }

        });
        stringProperty.bind(getSelectionModel().selectedItemProperty());
        getEditor().setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                System.out.println("MOUSE PRESSED!!!");
                setOnMousePressed(null);
            }
        });
        getEditor().setOnMouseClicked(null);
        getEditor().setOnMousePressed(null);
        getEditor().setOnMouseDragged(null);

        getEditor().setOnMouseClicked(e -> {
            if (e.getClickCount() > 1) {
                data.clear();
            }
        });

        cleanList();
    }

    private void cleanList() {
        ArrayList<String> list = new ArrayList<>();
        list.add("");

        data.stream().forEach(d -> {
            if (!list.contains(d) && list.size() < maxElements) {
                list.add(d);
            }
        });
        data.setAll(list);
    }

}
