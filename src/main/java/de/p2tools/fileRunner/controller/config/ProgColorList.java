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


package de.p2tools.fileRunner.controller.config;

import de.p2tools.p2Lib.configFile.pConfData.PColorData;
import de.p2tools.p2Lib.configFile.pConfData.PColorList;
import javafx.scene.paint.Color;

public class ProgColorList extends PColorList {

    //OK
    public static PColorData TABLE_ROW_IS_SEL_BG = addNewKey("table-row-is-sel-bg",
            Color.rgb(128, 0, 128),
            Color.rgb(128, 0, 128),
            true, "Ist die momentan ausgewählte Tabellenzeile", 1);
    public static PColorData TABLE_ROW_IS_SEL = addNewKey("table-row-is-sel",
            Color.rgb(0, 0, 0),
            Color.rgb(0, 0, 0),
            false, "Ist die momentan ausgewählte Tabellenzeile, Schriftfarbe", 1);

    public static PColorData FILE_IS_ID1_BG = addNewKey("file-is-id1-bg",
            Color.rgb(250, 250, 250),
            Color.rgb(250, 250, 250),
            true, "Datei ist gleich, HashID ist gerade, Tabellenzeile");
    public static PColorData FILE_IS_ID1 = addNewKey("file-is-id1",
            Color.rgb(0, 0, 0),
            Color.rgb(0, 0, 0),
            false, "Datei ist gleich, HashID ist gerade, Schriftfarbe");

    public static PColorData FILE_IS_ID2_BG = addNewKey("file-is-id2-bg",
            Color.rgb(220, 220, 220),
            Color.rgb(210, 210, 210),
            true, "Datei ist gleich, HashID ist ungerade, Tabellenzeile");
    public static PColorData FILE_IS_ID2 = addNewKey("file-is-id2",
            Color.rgb(0, 0, 0),
            Color.rgb(0, 0, 0),
            false, "Datei ist gleich, HashID ist ungerade, Schriftfarbe");

    //diff
    public static PColorData FILE_IS_DIFF_BG = addNewKey("file-diff-bg",
            Color.rgb(255, 230, 230),
            Color.rgb(218, 195, 195),
            true, "Dateien sind unterschiedlich, Tabellenzeile");
    public static PColorData FILE_IS_DIFF = addNewKey("file-diff",
            Color.rgb(0, 0, 0),
            Color.rgb(0, 0, 0),
            true, "Dateien sind unterschiedlich, Schriftfarbe");

    //only-echt
    public static PColorData FILE_IS_ONLY_BG = addNewKey("file-only-bg",
            Color.rgb(255, 247, 216),
            Color.rgb(217, 211, 189),
            true, "Gleiche Datei gibts nicht, Tabellenzeile");
    public static PColorData FILE_IS_ONLY = addNewKey("file-only",
            Color.rgb(0, 0, 0),
            Color.rgb(0, 0, 0),
            true, "Gleiche Datei gibts nicht, Schriftfarbe");

    //only-hash
    public static PColorData FILE_IS_ONLY_HASH_BG = addNewKey("file-only-hash-bg",
            Color.rgb(255, 247, 216),
            Color.rgb(217, 211, 189),
            true, "Gleiche Datei gibts nur mit anderem Namen/Verzeichnis, Tabellenzeile");
    public static PColorData FILE_IS_ONLY_HASH = addNewKey("file-only-hash",
            Color.rgb(0, 0, 0),
            Color.rgb(0, 0, 0),
            true, "Gleiche Datei gibts nur mit anderem Namen/Verzeichnis, Schriftfarbe");

    //link
    public static PColorData FILE_LINK_BG = addNewKey("file-link-bg",
            Color.rgb(223, 240, 255),
            Color.rgb(216, 236, 255),
            true, "Datei ist eine Dateiverknüpfung, Tabellenzeile");
    public static PColorData FILE_LINK = addNewKey("file-link",
            Color.rgb(0, 0, 0),
            Color.rgb(0, 0, 0),
            true, "Datei ist eine Dateiverknüpfung, Schriftfarbe");

    public synchronized static PColorList getInstance() {
        return PColorList.getInst();
    }

    public static void setColorTheme() {
        final boolean dark = ProgConfig.SYSTEM_DARK_THEME.get();
        for (int i = 0; i < getInstance().size(); ++i) {
            getInstance().get(i).setColorTheme(dark);
        }
    }
}
