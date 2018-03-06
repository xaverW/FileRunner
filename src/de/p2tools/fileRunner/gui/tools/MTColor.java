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

import de.p2tools.fileRunner.controller.config.ProgConfList;
import de.p2tools.p2Lib.configFile.pConfData.PConfDataColor;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.paint.Color;

public class MTColor {

    // Tabelle Dateien
    public static final PConfDataColor FILE_LINK = new PConfDataColor(ProgConfList.FARBE__FILE_LINK, Color.rgb(190, 0, 0), "Dateien sind ein Link");
    public static final PConfDataColor FILE_LINK_BG = new PConfDataColor(ProgConfList.FARBE__FILE_LINK, Color.rgb(240, 240, 255), "Dateien sind ein Link");

    private static ObservableList<PConfDataColor> colorList = FXCollections.observableArrayList();

    public MTColor() {
        colorList.add(FILE_LINK);
    }

    public static synchronized ObservableList<PConfDataColor> getColorList() {
        return colorList;
    }


    public final void load() {
        colorList.stream().filter(PConfDataColor -> !PConfDataColor.getPConfData().get().isEmpty()).forEach(PConfDataColor -> {
            try {
                PConfDataColor.setColorFromHex(PConfDataColor.getPConfData().get());
            } catch (final Exception ignored) {
                PConfDataColor.resetColor();
            }
        });
    }

    public final void save() {
        for (final PConfDataColor PConfDataColor : colorList) {
            PConfDataColor.getPConfData().setValue(String.valueOf(PConfDataColor.getColorToHex()));
        }
    }

    public void reset() {
        colorList.forEach(PConfDataColor::resetColor);
        save();
    }
}
