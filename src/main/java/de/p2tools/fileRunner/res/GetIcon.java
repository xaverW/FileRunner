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

package de.p2tools.fileRunner.res;

import de.p2tools.fileRunner.controller.config.ProgConst;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class GetIcon {

    public static ImageView getImageView(String strIcon, int w, int h) {
        return new ImageView(getStdImage(strIcon, ProgConst.PFAD_PROGRAMM_ICONS, w, h));
    }

    public static Image getImage(String strIcon, String path, int w, int h) {
        return getStdImage(strIcon, path, w, h);
    }

    private static Image getStdImage(String strIcon, String path, int w, int h) {
        return new Image(GetIcon.class.getResource(path + strIcon).toExternalForm(),
                w, h, false, true);
    }
}
