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


package de.p2tools.filerunner.controller.data.filedata;

import de.p2tools.p2lib.tools.date.PDate;
import de.p2tools.p2lib.tools.file.PFileSize;

import java.io.File;

public class FileData extends FileDataProps {

    public FileData(String pathFileName, PDate date, PFileSize fileSize, String hash, boolean link) {
        String path, fileName;
        if (pathFileName.contains(File.separator)) {
            path = pathFileName.substring(0, pathFileName.lastIndexOf(File.separator));
            fileName = pathFileName.substring(pathFileName.lastIndexOf(File.separator) + 1);
        } else {
            path = "";
            fileName = pathFileName;
        }

        setPath(path);
        setPathFileName(pathFileName);
        setFileName(fileName);
        setFileDate(date);
        setFileSize(fileSize);
        setHash(hash);
        setLink(link);
    }
}
