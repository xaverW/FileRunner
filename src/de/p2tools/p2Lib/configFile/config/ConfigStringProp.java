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


package de.p2tools.p2Lib.configFile.config;

import javafx.beans.property.StringProperty;

public class ConfigStringProp extends Config {

    private String initValue;
    private StringProperty actValue;

    public ConfigStringProp(String key, String initValue, StringProperty actValue) {
        super(key);
        this.initValue = initValue;
        this.actValue = actValue;
    }

    public String getInitValue() {
        return initValue;
    }

    public String getActValue() {
        return actValue.getValue();
    }

    public String getActValueString() {
        return getActValue();
    }

    public StringProperty getActValueProperty() {
        return actValue;
    }

    public void setActValue(String act) {
        actValue.setValue(act);
    }
}
