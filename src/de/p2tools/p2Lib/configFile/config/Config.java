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

import javafx.beans.property.ObjectProperty;

/**
 * this is the class for one configuration
 * for example: the configsData for a USER(name,age,size)
 * then it has 3 Config: one for the "name", "age", "size"
 * a config can store the info in a STRING or a PROPERTY
 */

public abstract class Config {

    private final String key;
    private final Object initValue;
    private Object actValue;
    private ObjectProperty actValueProperty = null;

    public Config() {
        this.key = "";
        initValue = "";
        actValue = "";
    }

    public Config(String key) {
        this.key = key;
        initValue = "";
        actValue = "";
    }

    public Config(String key, Object initValue, Object actValue) {
        this.key = key;
        this.initValue = initValue;
        this.actValue = actValue;
    }

    public String getKey() {
        return key;
    }

    public void setActValue(String act) {
        actValue = act;
    }

    public Object getActValue() {
        return actValue;
    }

    public String getActValueString() {
        return actValue.toString();
    }

    public Object getActValueProperty() {
        return actValueProperty;
    }

    public Object getInitValue() {
        return initValue;
    }
}


