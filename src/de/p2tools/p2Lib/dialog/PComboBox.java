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
import javafx.scene.control.ComboBox;

import java.util.ArrayList;
import java.util.Collections;

public class PComboBox extends ComboBox<String> {

    private StringProperty stringProperty = null;
    private ObservableList<String> data = null;

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
        clearList();
    }

    private void clearList() {
        ArrayList<String> list = new ArrayList<>();
        list.add("");

        data.stream().forEach(d -> {
            if (!list.contains(d)) {
                list.add(d);
            }
        });
        data.setAll(list);
    }
}
