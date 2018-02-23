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


package de.p2tools.p2Lib.tools;

import java.awt.*;
import java.awt.datatransfer.StringSelection;

public class SysTools {
    public static void copyToClipboard(String s) {
        Toolkit.getDefaultToolkit().getSystemClipboard().setContents(new StringSelection(s), null);
    }

    /**
     * Center a component (e.g. Dialog) on screen
     *
     * @param c        The reference component
     * @param absolute if true, use absolute position, otherwise relative
     */
    public static void centerOnScreen(final Component c, final boolean absolute) {
        final int width = c.getWidth();
        final int height = c.getHeight();
        final Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int x = (screenSize.width / 2) - (width / 2);
        int y = (screenSize.height / 2) - (height / 2);
        if (!absolute) {
            x /= 2;
            y /= 2;
        }
        c.setLocation(x, y);
    }

    /**
     * Liefert den Standardpfad für Downloads.
     *
     * @return Standardpfad zu den Downloads.
     */
    public static String getStandardDownloadPath() {
        return FileUtils.addsPfad(FileUtils.getHomePath(), "Downloads");
    }
}
