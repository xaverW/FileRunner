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


package de.p2tools.filerunner.controller.config;

import de.p2tools.p2lib.colordata.P2ColorData;
import de.p2tools.p2lib.colordata.P2ColorList;
import javafx.scene.paint.Color;

public class ProgColorList extends P2ColorList {

    //OK
    public static P2ColorData TABLE_ROW_IS_SEL_BG = addNewKey("table-row-is-sel-bg",
            Color.rgb(77, 77, 77),
            Color.rgb(77, 77, 77),
            true, "Ist die momentan ausgewählte Tabellenzeile", 1);
    public static P2ColorData TABLE_ROW_IS_SEL = addNewKey("table-row-is-sel",
            Color.rgb(255, 255, 255),
            Color.rgb(255, 255, 255),
            true, "Ist die momentan ausgewählte Tabellenzeile, Schriftfarbe", 1);

    //gleich
//    public static PColorData FILE_IS_ID1_BG = addNewKey("file-is-id1-bg",
//            Color.rgb(179, 230, 179),
//            Color.rgb(179, 230, 179),
//            true, "Datei ist gleich, Tabellenzeile");
//    public static PColorData FILE_IS_ID1 = addNewKey("file-is-id1",
//            Color.rgb(0, 0, 0),
//            Color.rgb(0, 0, 0),
//            false, "Datei ist gleich, Schriftfarbe");
    public static P2ColorData FILE_IS_ID1_BG = addNewKey("file-is-id1-bg",
            Color.rgb(179, 230, 179),
            Color.rgb(179, 230, 179),
            true, "Datei ist gleich, HashID ist gerade oder \"HashID ist ungerade\" ist ausgeschaltet, Tabellenzeile");
    public static P2ColorData FILE_IS_ID1 = addNewKey("file-is-id1",
            Color.rgb(0, 0, 0),
            Color.rgb(0, 0, 0),
            false, "Datei ist gleich, HashID ist gerade, Schriftfarbe");

    public static P2ColorData FILE_IS_ID2_BG = addNewKey("file-is-id2-bg",
            Color.rgb(153, 204, 153),
            Color.rgb(153, 204, 153),
            false, "Datei ist gleich, HashID ist ungerade, Tabellenzeile");
    public static P2ColorData FILE_IS_ID2 = addNewKey("file-is-id2",
            Color.rgb(0, 0, 0),
            Color.rgb(0, 0, 0),
            false, "Datei ist gleich, HashID ist ungerade, Schriftfarbe");

    //diff
    public static P2ColorData FILE_IS_DIFF_BG = addNewKey("file-diff-bg",
            Color.rgb(255, 230, 230),
            Color.rgb(218, 195, 195),
            true, "Dateien sind unterschiedlich, Tabellenzeile");
    public static P2ColorData FILE_IS_DIFF = addNewKey("file-diff",
            Color.rgb(0, 0, 0),
            Color.rgb(0, 0, 0),
            false, "Dateien sind unterschiedlich, Schriftfarbe");

    //only-echt
    public static P2ColorData FILE_IS_ONLY_BG = addNewKey("file-only-bg",
            Color.rgb(255, 251, 233),
            Color.rgb(217, 211, 189),
            true, "Gleiche Datei gibt's nicht, Tabellenzeile");
    public static P2ColorData FILE_IS_ONLY = addNewKey("file-only",
            Color.rgb(0, 0, 0),
            Color.rgb(0, 0, 0),
            false, "Gleiche Datei gibt's nicht, Schriftfarbe");

    //only-hash
    public static P2ColorData FILE_IS_ONLY_HASH_BG = addNewKey("file-only-hash-bg",
            Color.rgb(255, 251, 233),
            Color.rgb(217, 211, 189),
            true, "Gleiche Datei gibt's nur mit anderem Namen/Verzeichnis, Tabellenzeile");
    public static P2ColorData FILE_IS_ONLY_HASH = addNewKey("file-only-hash",
            Color.rgb(0, 0, 0),
            Color.rgb(0, 0, 0),
            false, "Gleiche Datei gibt's nur mit anderem Namen/Verzeichnis, Schriftfarbe");

    //link
    public static P2ColorData FILE_LINK_BG = addNewKey("file-link-bg",
            Color.rgb(223, 240, 255),
            Color.rgb(216, 236, 255),
            false, "Datei ist eine Dateiverknüpfung, Tabellenzeile");
    public static P2ColorData FILE_LINK = addNewKey("file-link",
            Color.rgb(0, 0, 0),
            Color.rgb(0, 0, 0),
            false, "Datei ist eine Dateiverknüpfung, Schriftfarbe");

    public synchronized static P2ColorList getInstance() {
        return P2ColorList.getInst();
    }

    public static void setColorTheme() {
        final boolean dark = ProgConfig.SYSTEM_DARK_THEME.get();
        for (int i = 0; i < getInstance().size(); ++i) {
            getInstance().get(i).setColorTheme(dark);
        }
    }
}
