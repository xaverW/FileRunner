/*
 * MTPlayer Copyright (C) 2017 W. Xaver W.Xaver[at]googlemail.com
 * https://sourceforge.net/projects/mtplayer/
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

import org.apache.commons.lang3.SystemUtils;

import java.io.File;
import java.util.ResourceBundle;

public class Functions {

    private static final String VERSION = "version";

    public static String textLaenge(int max, String text, boolean mitte, boolean addVorne) {
        if (text.length() > max) {
            if (mitte) {
                text = text.substring(0, 25) + " .... " + text.substring(text.length() - (max - 31));
            } else {
                text = text.substring(0, max - 1);
            }
        }
        while (text.length() < max) {
            if (addVorne) {
                text = ' ' + text;
            } else {
                text = text + ' ';
            }
        }
        return text;
    }

    public static String minTextLaenge(int max, String text) {
        while (text.length() < max) {
            text = text + ' ';
        }
        return text;
    }

    public enum OperatingSystemType {

        UNKNOWN(""), WIN32("Windows"), WIN64("Windows"), LINUX("Linux"), MAC("Mac");
        private final String name;

        OperatingSystemType(String name) {
            this.name = name;
        }

        @Override
        public String toString() {
            return name;
        }
    }

    /**
     * Detect and return the currently used operating system.
     *
     * @return The enum for supported Operating Systems.
     */
    public static OperatingSystemType getOs() {
        OperatingSystemType os = OperatingSystemType.UNKNOWN;

        if (System.getProperty("os.name").toLowerCase().contains("windows")) {
            if (System.getenv("ProgramFiles(x86)") != null) {
                // win 64Bit
                os = OperatingSystemType.WIN64;
            } else if (System.getenv("ProgramFiles") != null) {
                // win 32Bit
                os = OperatingSystemType.WIN32;
            }

        } else if (SystemUtils.IS_OS_LINUX) {
            os = OperatingSystemType.LINUX;
        } else if (System.getProperty("os.name").toLowerCase().contains("freebsd")) {
            os = OperatingSystemType.LINUX;

        } else if (SystemUtils.IS_OS_MAC_OSX) {
            os = OperatingSystemType.MAC;
        }
        return os;
    }

    public static String getOsString() {
        return getOs().toString();
    }

    public static String getProgVersionString() {
        return " [Vers.: " + getProgVersion() + " - " + getBuild() + " ]";
    }

    public static String[] getJavaVersion() {
        final String[] ret = new String[4];
        int i = 0;
        ret[i++] = "Vendor: " + System.getProperty("java.vendor");
        ret[i++] = "VMname: " + System.getProperty("java.vm.name");
        ret[i++] = "Version: " + System.getProperty("java.version");
        ret[i++] = "Runtimeversion: " + System.getProperty("java.runtime.version");
        return ret;
    }

    public static String getCompileDate() {
        final String propToken = "DATE";
        String msg = "";
        try {
            ResourceBundle.clearCache();
            final ResourceBundle rb = ResourceBundle.getBundle(VERSION);
            if (rb.containsKey(propToken)) {
                msg = rb.getString(propToken);
            }
        } catch (final Exception e) {
            Log.errorLog(807293847, e);
        }
        return msg;
    }

    public static String getProgVersion() {
        final String TOKEN_VERSION = "VERSION";
        try {
            ResourceBundle.clearCache();
            final ResourceBundle rb = ResourceBundle.getBundle(VERSION);
            if (rb.containsKey(TOKEN_VERSION)) {
                return rb.getString(TOKEN_VERSION);
            }
        } catch (final Exception e) {
            Log.errorLog(134679898, e);
        }
        return "";
    }

    public static int getProgVersionInt() {
        final String TOKEN_VERSION = "VERSION";
        try {
            ResourceBundle.clearCache();
            final ResourceBundle rb = ResourceBundle.getBundle(VERSION);
            if (rb.containsKey(TOKEN_VERSION)) {
                return Integer.parseInt(rb.getString(TOKEN_VERSION));
            }
        } catch (final Exception e) {
            Log.errorLog(134679898, e);
        }
        return 0;
    }

    public static String getBuild() {
        final String TOKEN_VERSION = "BUILD";
        try {
            ResourceBundle.clearCache();
            final ResourceBundle rb = ResourceBundle.getBundle(VERSION);
            if (rb.containsKey(TOKEN_VERSION)) {
                return rb.getString(TOKEN_VERSION);
            }
        } catch (final Exception e) {
            Log.errorLog(134679898, e);
        }
        return "0";
    }

    public static String addsPfad(String pfad1, String pfad2) {
        String ret = "";
        if (pfad1 != null && pfad2 != null) {
            if (pfad1.isEmpty()) {
                ret = pfad2;
            } else if (pfad2.isEmpty()) {
                ret = pfad1;
            } else if (!pfad1.isEmpty() && !pfad2.isEmpty()) {
                if (pfad1.endsWith(File.separator)) {
                    ret = pfad1.substring(0, pfad1.length() - 1);
                } else {
                    ret = pfad1;
                }
                if (pfad2.charAt(0) == File.separatorChar) {
                    ret += pfad2;
                } else {
                    ret += File.separator + pfad2;
                }
            }
        }
        if (ret.isEmpty()) {
            Log.errorLog(283946015, pfad1 + " - " + pfad2);
        }
        return ret;
    }

    public static String addUrl(String u1, String u2) {
        if (u1.endsWith("/")) {
            return u1 + u2;
        } else {
            return u1 + '/' + u2;
        }
    }

    public static boolean istUrl(String dateiUrl) {
        // return dateiUrl.startsWith("http") ? true : false || dateiUrl.startsWith("www") ? true :
        // false;
        return dateiUrl.startsWith("http") || dateiUrl.startsWith("www");
    }

    public static String getDateiName(String pfad) {
        // Dateinamen einer URL extrahieren
        String ret = "";
        if (pfad != null) {
            if (!pfad.isEmpty()) {
                ret = pfad.substring(pfad.lastIndexOf('/') + 1);
            }
        }
        if (ret.contains("?")) {
            ret = ret.substring(0, ret.indexOf('?'));
        }
        if (ret.contains("&")) {
            ret = ret.substring(0, ret.indexOf('&'));
        }
        if (ret.isEmpty()) {
            Log.errorLog(395019631, pfad);
        }
        return ret;
    }

    public static String removeHtml(String in) {
        return in.replaceAll("\\<.*?>", "");
    }
}
