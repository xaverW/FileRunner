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


package de.p2tools.fileRunner.gui.tools;

import de.p2tools.fileRunner.controller.config.ProgConfig;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.paint.Color;

public class MTColor {

    // Tabelle Dateien
    public static final MTC FILE_LINK = new MTC(ProgConfig.FARBE__FILE_LINK, Color.rgb(190, 0, 0), "Dateien sind ein Link");
    public static final MTC FILE_LINK_BG = new MTC(ProgConfig.FARBE__FILE_LINK, Color.rgb(240, 240, 255), "Dateien sind ein Link");

    private static ObservableList<MTC> colorList = FXCollections.observableArrayList();
    public static final int MVC_TEXT = 0;
    public static final int MVC_COLOR = 1;
    public static final int MVC_MAX = 2;

    public MTColor() {
        colorList.add(FILE_LINK);
    }

    public static synchronized ObservableList<MTC> getColorList() {
        return colorList;
    }


    public final void load() {
        colorList.stream().filter(MTC -> !MTC.getMlConfigs().get().isEmpty()).forEach(MTC -> {
            try {
                MTC.setColorFromHex(MTC.getMlConfigs().get());
            } catch (final Exception ignored) {
                MTC.resetColor();
            }
        });
    }

    public final void save() {
        for (final MTC mtc : colorList) {
            mtc.getMlConfigs().setValue(String.valueOf(mtc.getColorToHex()));
        }
    }

    public void reset() {
        colorList.forEach(MTC::resetColor);
        save();
    }
}
