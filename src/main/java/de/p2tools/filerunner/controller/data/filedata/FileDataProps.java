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

import de.p2tools.p2lib.configfile.pdata.P2DataSample;
import de.p2tools.p2lib.tools.date.P2Date;
import de.p2tools.p2lib.tools.file.P2FileSize;

public class FileDataProps extends P2DataSample<FileData> {

    public static final String TAG = "FileData";

    private int id = 0;
    private int filePathId = 0;
    private int fileNameId = 0;
    private int hashId = 0;
    private String path = "";
    private String pathFileName = "";
    private String fileName = "";
    private P2Date fileDate = null;
    private P2FileSize fileSize = null;
    private String hash = "";
    private boolean diff = false;
    private boolean only = false;
    private boolean link = false;

    public String getTag() {
        return TAG;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getFilePathId() {
        return filePathId;
    }

    public void setFilePathId(int filePathId) {
        this.filePathId = filePathId;
    }

    public int getFileNameId() {
        return fileNameId;
    }

    public void setFileNameId(int fileNameId) {
        this.fileNameId = fileNameId;
    }

    public int getHashId() {
        return hashId;
    }

    public void setHashId(int hashId) {
        this.hashId = hashId;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getPathFileName() {
        return pathFileName;
    }

    public void setPathFileName(String pathFileName) {
        this.pathFileName = pathFileName;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public P2Date getFileDate() {
        return fileDate;
    }

    public void setFileDate(P2Date fileDate) {
        this.fileDate = fileDate;
    }

    public P2FileSize getFileSize() {
        return fileSize;
    }

    public void setFileSize(P2FileSize fileSize) {
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

    public boolean isLink() {
        return link;
    }

    public void setLink(boolean link) {
        this.link = link;
    }
}
