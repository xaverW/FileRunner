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

    public enum HASH_ALGORITHM {

        HASH_MD5("MD5"), HASH_SHA_1("SHA-1"), HASH_SHA_256("SHA-256");
        private final String name;

        HASH_ALGORITHM(String name) {
            this.name = name;
        }

        @Override
        public String toString() {
            return name;
        }
    }


    public static String hashString(String message, HASH_ALGORITHM algorithm)
            throws HashException {

        try {
            MessageDigest digest = MessageDigest.getInstance(algorithm.toString());
            byte[] hashedBytes = digest.digest(message.getBytes("UTF-8"));

            return convertByteArrayToHexString(hashedBytes);
        } catch (NoSuchAlgorithmException | UnsupportedEncodingException ex) {
            throw new HashException(
                    "Could not generate hash from String", ex);
        }
    }

    public static String hashFile(File file, HASH_ALGORITHM algorithm) throws HashException {
        return hashFile(file, algorithm.toString());
    }

    public static String generateMD5(File file) throws HashException {
        return hashFile(file, HASH_ALGORITHM.HASH_MD5.toString());
    }

    public static String generateSHA1(File file) throws HashException {
        return hashFile(file, "SHA-1");
    }

    public static String generateSHA256(File file) throws HashException {
        return hashFile(file, "SHA-256");
    }

    private static String hashFile(File file, String algorithm) throws HashException {
        try (FileInputStream inputStream = new FileInputStream(file)) {
            MessageDigest digest = MessageDigest.getInstance(algorithm);

            byte[] bytesBuffer = new byte[1024];
            int bytesRead = -1;

            while ((bytesRead = inputStream.read(bytesBuffer)) != -1) {
                digest.update(bytesBuffer, 0, bytesRead);
            }

            byte[] hashedBytes = digest.digest();

            return convertByteArrayToHexString(hashedBytes);
        } catch (NoSuchAlgorithmException | IOException ex) {
            throw new HashException("Could not generate hash from file", ex);
        }
    }

    private static String convertByteArrayToHexString(byte[] arrayBytes) {
        StringBuffer stringBuffer = new StringBuffer();
        for (int i = 0; i < arrayBytes.length; i++) {
            stringBuffer.append(Integer.toString((arrayBytes[i] & 0xff) + 0x100, 16)
                    .substring(1));
        }
        return stringBuffer.toString();
    }
}
