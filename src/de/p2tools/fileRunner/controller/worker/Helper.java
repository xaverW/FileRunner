/*
 * MTPlayer Copyright (C) 2018 W. Xaver W.Xaver[at]googlemail.com
 * http://zdfmediathk.sourceforge.net/
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


package de.p2tools.fileRunner.controller.worker;

import de.p2tools.fileRunner.controller.config.ProgConst;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.security.MessageDigest;

public class Helper {

    public static String toHexString(byte b) {
        int value = (b & 0x7F) + (b < 0 ? 128 : 0);
        String ret = (value < 16 ? "0" : "");
        ret += Integer.toHexString(value).toLowerCase();
        return ret;
    }

    public static String getHash(String zeile) {
        String ret = "";
        if (zeile.contains(" ")) {
            ret = zeile.substring(0, zeile.indexOf(" "));
        }
        return ret;
    }

    public static String getFile(String zeile) {
        String fil = "";
        if (zeile.contains("*")) {
            fil = zeile.substring(zeile.indexOf("*") + 1);
        } else if (zeile.contains("  ")) {
            fil = zeile.substring(zeile.indexOf("  ") + 2);
        }
        return fil;
    }

    public static byte[] get(String pwd) throws IOException, GeneralSecurityException {
        MessageDigest md = MessageDigest.getInstance(ProgConst.HASH);
        byte[] digest = md.digest(pwd.getBytes());
        byte[] hKey = new byte[24];
        System.arraycopy(digest, 0, hKey, 0, 8);
        System.arraycopy(digest, 8, hKey, 8, 8);
        // Erster und dritter Schlüssel sind bei TripleDES identisch
        System.arraycopy(digest, 0, hKey, 16, 8);
        return hKey;
    }
}
