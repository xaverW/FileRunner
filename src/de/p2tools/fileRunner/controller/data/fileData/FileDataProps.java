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


package de.p2tools.fileRunner.controller.data.fileData;

import de.p2tools.p2Lib.tools.FileSize;
import de.p2tools.p2Lib.tools.PDate;

public class FileDataProps {

    public static final String TAG = "FileData";

    private int nr = 0;
    private String fileName = "";
    private PDate fileDate = null;
    private FileSize fileSize = null;
    private String hash = "";
    private boolean diff = false;
    private boolean only = false;

    public String getTag() {
        return TAG;
    }

    public int getNr() {
        return nr;
    }

    public void setNr(int nr) {
        this.nr = nr;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public PDate getFileDate() {
        return fileDate;
    }

    public void setFileDate(PDate fileDate) {
        this.fileDate = fileDate;
    }

    public FileSize getFileSize() {
        return fileSize;
    }

    public void setFileSize(FileSize fileSize) {
        this.fileSize = fileSize;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public boolean isDiff() {
        return diff;
    }

    public void setDiff(boolean diff) {
        this.diff = diff;
    }

    public boolean isOnly() {
        return only;
    }

    public void setOnly(boolean only) {
        this.only = only;
    }
}
