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

import javafx.scene.control.Control;
import org.apache.commons.lang3.time.FastDateFormat;

import java.util.Date;


public class GuiTools {

    private static final String STR = "__";
    private static final String FORMATTER_ddMMyyyy_str = STR + "yyyyMMdd";
    private static final String FORMATTER_ddMMyyyyHHmmss_str = STR + "yyyyMMdd_HHmmss";

    private static final FastDateFormat FORMATTER_ddMMyyyy = FastDateFormat.getInstance(FORMATTER_ddMMyyyy_str);
    private static final FastDateFormat FORMATTER_ddMMyyyyHHmmss = FastDateFormat.getInstance(FORMATTER_ddMMyyyyHHmmss_str);
    private final static String MD5 = ".md5";

    public static void setColor(Control tf, boolean set) {
        if (set) {
            tf.getStyleClass().add("txtIsEmpty");
        } else {
            tf.getStyleClass().removeAll("txtIsEmpty");
        }
    }

    public static String getNextName(String name) {
        String ret = name;

        final String date1 = FORMATTER_ddMMyyyy.format(new Date());
        final String date2 = FORMATTER_ddMMyyyyHHmmss.format(new Date());

        final String s1 = getTime(name, FORMATTER_ddMMyyyy);
        final String s2 = getTime(name, FORMATTER_ddMMyyyyHHmmss);

        if (!name.endsWith(MD5)) {
            ret = name + MD5;

        } else if (!s1.isEmpty()) {
            ret = cleanName(name);
            ret = ret.replace(s1, date2) + MD5;

        } else if (!s2.isEmpty()) {
            ret = cleanName(name);
            ret = ret.replace(s2, MD5);

        } else if (name.endsWith(MD5)) {
            ret = cleanName(name);
            ret = ret + date1 + MD5;
        }

        return ret;
    }

    private static String cleanName(String name) {
        // falls sich da mehre MD5 angesammelt haben
        while (!name.equals(MD5) && name.endsWith(MD5)) {
            name = name.substring(0, name.lastIndexOf(MD5));
        }
        return name;
    }

    private static String getTime(String name, FastDateFormat format) {
        String ret = "";
        Date d = null;
        if (name.contains(STR) && name.endsWith(MD5)) {
            try {
                ret = name.substring(name.lastIndexOf(STR)).replaceAll(MD5, "");
                d = new Date(format.parse(ret).getTime());
            } catch (Exception ignore) {
                d = null;
            }
        }

        if (d != null && format.getPattern().length() == ret.length()) {
            return ret;
        }

        return "";
    }
}
