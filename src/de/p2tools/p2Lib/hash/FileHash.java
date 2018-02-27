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


package de.p2tools.p2Lib.hash;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class FileHash {


    public static String hashString(String text, String algorithm) throws HashException {
        try {
            MessageDigest md = MessageDigest.getInstance(algorithm);
            byte[] hashedBytes = md.digest(text.getBytes("UTF-8"));

            return convertToHexString(hashedBytes);
        } catch (NoSuchAlgorithmException | UnsupportedEncodingException ex) {
            throw new HashException("Could not generate hash", ex);
        }
    }

    public static String hashFile(File file, String hAlgo) throws HashException {
        try (FileInputStream inputStream = new FileInputStream(file)) {
            MessageDigest md = MessageDigest.getInstance(hAlgo);

            byte[] bytesBuffer = new byte[1024];
            int bytesRead = -1;

            while ((bytesRead = inputStream.read(bytesBuffer)) != -1) {
                md.update(bytesBuffer, 0, bytesRead);
            }

            byte[] hashedBytes = md.digest();

            return convertToHexString(hashedBytes);
        } catch (NoSuchAlgorithmException | IOException ex) {
            throw new HashException("Could not generate hash", ex);
        }
    }

    private static String convertToHexString(byte[] arrayBytes) {
        StringBuffer stringBuffer = new StringBuffer();
        for (int i = 0; i < arrayBytes.length; i++) {
            stringBuffer.append(Integer.toString((arrayBytes[i] & 0xff) + 0x100, 16)
                    .substring(1));
        }
        return stringBuffer.toString();
    }
}
