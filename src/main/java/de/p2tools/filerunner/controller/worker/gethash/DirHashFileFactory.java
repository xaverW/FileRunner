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


package de.p2tools.filerunner.controller.worker.gethash;

import de.p2tools.filerunner.controller.data.filedata.FileData;
import de.p2tools.filerunner.controller.data.filedata.FileDataList;
import de.p2tools.p2lib.P2LibConst;
import de.p2tools.p2lib.alert.PAlert;
import de.p2tools.p2lib.tools.date.P2Date;
import de.p2tools.p2lib.tools.file.PFileSize;
import de.p2tools.p2lib.tools.log.PLog;
import javafx.application.Platform;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Iterator;

public class DirHashFileFactory {

    private DirHashFileFactory() {
    }

    public static boolean writeDirHashFile(File hashOutFile, FileDataList fileDataList) {
        // todo Fehler melden
        OutputStreamWriter out = null;
        boolean ret = false;
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
                out.write(HashFactory.hashStart + fileData.getHash() + HashFactory.hashEnd + HashFactory.pause +
                        HashFactory.fileStart + fileData.getPathFileName() + HashFactory.fileEnd + HashFactory.pause +
                        HashFactory.sizeStart + fileData.getFileSize() + HashFactory.sizeEnd + HashFactory.pause +
                        HashFactory.dateStart + fileData.getFileDate() + HashFactory.dateEnd + HashFactory.pause +
                        HashFactory.linkStart + fileData.isLink() + HashFactory.linkEnd +
                        P2LibConst.LINE_SEPARATOR);
            }
            out.flush();
            ret = true;
        } catch (Exception ex) {
            ret = false;
            PLog.errorLog(986532014, ex, "Fehler beim Schreiben der Hashdatei!");
            Platform.runLater(() -> PAlert.showErrorAlert("Hashfile schreiben", hashOutFile.toString() + P2LibConst.LINE_SEPARATOR +
                    "Die Datei konnte nicht gespeichert werden." + P2LibConst.LINE_SEPARATOR +
                    ex.getLocalizedMessage()));
        } finally {
            try {
                if (out != null) out.close();
            } catch (IOException ex) {
                PLog.errorLog(203064547, ex, "Fehler beim Schießen der Hashdatei!");
            }
        }
        return ret;
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

    public static boolean hasLineBreak(String line) {
        boolean ret = false;
        line = changeLine(line);
        if (line.contains(HashFactory.hashStart) && !line.endsWith(HashFactory.linkEnd)) {
            // dann ists neues Format und kein normales Ende
            ret = true;
        }
        return ret;
    }

    public static FileData getFileData(String line) {
        FileData fileData;
        line = changeLine(line);

        if (line.contains(HashFactory.hashStart)) {
            //neues Format
            fileData = new FileData(getFileNew(line), getDateNew(line), getFileSizeNew(line),
                    getHashNew(line), getFileIsLinkNew(line));

        } else {
            fileData = new FileData(getFile(line), new P2Date(0), new PFileSize(""), getHash(line), false);
        }
        return fileData;
    }

    public static String getHashNew(String line) {
        String ret = "";
        int l1, l2;
        l1 = line.indexOf(HashFactory.hashStart);
        l2 = line.indexOf(HashFactory.hashEnd);
        if (l1 < 0 || l2 < 0) {
            return "";
        }
        l1 += HashFactory.hashStart.length();
        ret = line.substring(l1, l2);
        ret = ret.trim();
        return ret;
    }

    public static String getHash(String zeile) {
        String ret = "";
        if (zeile.contains(" ")) {
            ret = zeile.substring(0, zeile.indexOf(" "));
        }
        return ret;
    }

    public static String getFileNew(String line) {
        String file = "";
        int l1, l2;
        l1 = line.indexOf(HashFactory.fileStart);
        if (l1 < 0) {
            return "";
        }
        l2 = line.indexOf(HashFactory.fileEnd, l1);
        if (l2 < 0) {
            return "";
        }
        l1 += HashFactory.fileStart.length();
        file = line.substring(l1, l2);
//        file = file.trim();
        return file;
    }

    public static String getFile(String zeile) {
        String file = "";
        if (zeile.contains("*")) {
            file = zeile.substring(zeile.indexOf("*") + 1);
        } else if (zeile.contains("  ")) {
            file = zeile.substring(zeile.indexOf("  ") + 2);
        }
        return file;
    }

    public static P2Date getDateNew(String line) {
        String date = "";
        int l1, l2;
        l1 = line.indexOf(HashFactory.dateStart);
        if (l1 < 0) {
            return new P2Date(0);
        }
        l2 = line.indexOf(HashFactory.dateEnd, l1);
        if (l2 < 0) {
            return new P2Date(0);
        }
        l1 += HashFactory.dateStart.length();
        date = line.substring(l1, l2);
        date = date.trim();
        return new P2Date(date);
    }

    public static PFileSize getFileSizeNew(String line) {
        String size = "";
        int l1, l2;
        l1 = line.indexOf(HashFactory.sizeStart);
        if (l1 < 0) {
            return new PFileSize("");
        }
        l2 = line.indexOf(HashFactory.sizeEnd, l1);
        if (l2 < 0) {
            return new PFileSize("");
        }
        l1 += HashFactory.sizeStart.length();
        size = line.substring(l1, l2);
        size = size.trim();
        return new PFileSize(PFileSize.convertToLong(size));
    }

    public static boolean getFileIsLinkNew(String line) {
        String link = "";
        int l1, l2;
        l1 = line.indexOf(HashFactory.linkStart);
        if (l1 < 0) {
            return false;
        }
        l2 = line.indexOf(HashFactory.linkEnd, l1);
        if (l2 < 0) {
            return false;
        }
        l1 += HashFactory.linkStart.length();
        link = line.substring(l1, l2);
        link = link.trim();
        return Boolean.parseBoolean(link);
    }
}
