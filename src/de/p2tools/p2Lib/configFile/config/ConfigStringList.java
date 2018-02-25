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

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class ConfigStringList {

    private String tag = "";
    private String tag_str = "";

    private Config[] configArr = new Config[]{};
    public static ObservableList<String> list = FXCollections.observableArrayList();


    public void setTagName(String tag) {
        this.tag = tag;
        this.tag_str = tag + "-str";
    }

    public String getTagName() {
        return tag;
    }

    public void setConfigArr(Config[] configArr) {
        this.configArr = configArr;
        list.clear();
        for (Config str : configArr) {
            list.add(str.getActValueString());
        }
    }

    public ObservableList<String> getList() {
        return list;
    }

    public Config[] getConfigArr() {
        if (list.isEmpty()) {
            return new Config[]{};
        }
        configArr = new Config[list.size()];
        for (int i = 0; i < list.size(); ++i) {
            String str = list.get(i);
            ConfigString cs = new ConfigString(tag_str, str, str);
            configArr[i] = cs;
        }
        return configArr;
    }


}
