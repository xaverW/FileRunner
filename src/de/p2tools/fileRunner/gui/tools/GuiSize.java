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

import de.p2tools.p2Lib.tools.PConfigs;
import javafx.stage.Stage;

public class GuiSize {

    public static void getSizeScene(PConfigs nr, Stage stage) {
        if (stage != null && stage.getScene() != null && nr != null) {
            nr.setValue(
                    (int) stage.getScene().getWidth() + ":"
                            + (int) stage.getScene().getHeight()
                            + ':'
                            + (int) stage.getX()
                            + ':'
                            + (int) stage.getY());
        }
    }

    public static int getWidth(PConfigs nr) {
        int breite = 0;
        final String[] arr = nr.get().split(":");

        try {
            if (arr.length == 4 || arr.length == 2) {
                breite = Integer.parseInt(arr[0]);
            }
        } catch (final Exception ex) {
            breite = 0;
        }

        return breite;
    }

    public static int getHeight(PConfigs nr) {
        int hoehe = 0;
        final String[] arr = nr.get().split(":");

        try {
            if (arr.length == 4 || arr.length == 2) {
                hoehe = Integer.parseInt(arr[1]);
            }
        } catch (final Exception ex) {
            hoehe = 0;
        }

        return hoehe;
    }

    public static void setPos(PConfigs nr, Stage stage) {
        int posX, posY;
        posX = 0;
        posY = 0;
        final String[] arr = nr.get().split(":");
        try {
            if (arr.length == 4) {
                posX = Integer.parseInt(arr[2]);
                posY = Integer.parseInt(arr[3]);
            }
        } catch (final Exception ex) {
            posX = 0;
            posY = 0;
        }
        if (posX > 0 && posY > 0) {
            stage.setX(posX);
            stage.setY(posY);
        }
    }

}
