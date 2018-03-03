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


package de.p2tools.fileRunner.controller.data.fileData;

public class FileDataFilter {

    public enum FILTER_TYPES {
        ALL, SAME, DIFF, DIFF_ALL, ONLY
    }

    private String searchStr = "";
    private FILTER_TYPES filter_types = FILTER_TYPES.ALL;

    public String getSearchStr() {
        return searchStr;
    }

    public void setSearchStr(String searchStr) {
        if (searchStr == null) {
            this.searchStr = "";
            return;
        }
        this.searchStr = searchStr;
    }

    public FILTER_TYPES getFilter_types() {
        return filter_types;
    }

    public void setFilter_types(FILTER_TYPES filter_types) {
        this.filter_types = filter_types;
    }
}
