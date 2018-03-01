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


package de.p2tools.fileRunner.controller.worker.GetHash;

import de.p2tools.fileRunner.controller.data.fileData.FileData;
import de.p2tools.fileRunner.controller.data.fileData.FileDataList;
import de.p2tools.p2Lib.tools.FileSize;
import de.p2tools.p2Lib.tools.Log;
import de.p2tools.p2Lib.tools.PDate;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Iterator;

public class HashTools {

    public static void writeDirHashFile(File hashOutFile, FileDataList fileDataList) {
        OutputStreamWriter out = null;
        try {
            if (hashOutFile.exists()) {
                hashOutFile.delete();
            }
            hashOutFile.createNewFile();
            out = new OutputStreamWriter(new FileOutputStream(hashOutFile, true));
            Iterator<FileData> it = fileDataList.iterator();
            FileData fileData;
            while (it.hasNext()) {
                fileData = it.next();
                out.write(fileData.getHash() + " " + "*" + fileData.getFileName() + "\n");
            }
            out.flush();
        } catch (Exception ex) {
            Log.errorLog(986532014, ex, "Fehler beim Schreiben der Hashdatei!");
        } finally {
            try {
                out.close();
            } catch (IOException ex) {
                Log.errorLog(203064547, ex, "Fehler beim Schießen der Hashdatei!");
            }
        }
    }

    public static String changeLine(String line) {
        char search;
        char c = File.separatorChar;
        if (c == '\\') {
            search = '/';
        } else {
            search = '\\';
        }
        line = line.replace(search, File.separatorChar);
        return line;
    }

    public static FileData getFileData(String line) {
        line = HashTools.changeLine(line);
        FileData fileData = new FileData(getFile(line), getDate(line), getFileSize(line), getHash(line), getFileIsLink(line));
        return fileData;
    }

    public static String getHashString(byte[] hash) {
        StringBuffer stringBuffer = new StringBuffer();
        for (int k = 0; k < hash.length; ++k) {
            stringBuffer.append(toHexString(hash[k]));
        }
        return stringBuffer.toString();
    }

    private static String toHexString(byte b) {
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

    public static PDate getDate(String zeile) {
        //todo
        String fil = "";
        if (zeile.contains("*")) {
            fil = zeile.substring(zeile.indexOf("*") + 1);
        } else if (zeile.contains("  ")) {
            fil = zeile.substring(zeile.indexOf("  ") + 2);
        }
        return new PDate(0);
    }

    public static FileSize getFileSize(String zeile) {
        //todo
        return new FileSize("");
    }

    public static boolean getFileIsLink(String zeile) {
        //todo
        return false;
    }

}
