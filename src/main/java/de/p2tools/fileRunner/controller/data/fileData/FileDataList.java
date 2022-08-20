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

import de.p2tools.p2Lib.tools.date.PDate;
import de.p2tools.p2Lib.tools.file.PFileSize;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;

import java.util.ArrayList;
import java.util.function.Predicate;

public class FileDataList extends SimpleListProperty<FileData> {
    private String sourceDir = "";
    private final FilteredList<FileData> filteredFileDate;
    private final SortedList<FileData> sortedFileData;

    public FileDataList() {
        super(FXCollections.observableArrayList());
        filteredFileDate = new FilteredList<>(this, p -> true);
        sortedFileData = new SortedList<>(filteredFileDate);
    }

    public FilteredList<FileData> getFilteredFileData() {
        return filteredFileDate;
    }

    public SortedList<FileData> getSortedFileData() {
        return sortedFileData;
    }

    public synchronized void clearPred() {
        filteredFileDate.setPredicate(p -> true);
    }

    public synchronized void setPred(FileDataFilter fileDataFilter) {
        Predicate<FileData> predicate = film -> true;

        switch (fileDataFilter.getFilter_types()) {
            case ALL:
                break;
            case SAME:
                predicate = predicate.and(f -> !f.isDiff() && !f.isOnly());
                break;
            case DIFF:
                predicate = predicate.and(f -> f.isDiff() && !f.isOnly());
                break;
            case DIFF_ALL:
                predicate = predicate.and(f -> f.isDiff() || f.isOnly());
                break;
            case ONLY:
                predicate = predicate.and(f -> f.isOnly());
                break;
            default:
        }


        if (!fileDataFilter.getSearchStr().trim().isEmpty()) {
            //todo
            predicate = predicate.and(f -> f.getPathFileName().toLowerCase().contains(fileDataFilter.getSearchStr().toLowerCase()));
        }
        filteredFileDate.setPredicate(predicate);
    }

    public void setSourceDir(String sourceDir) {
        this.sourceDir = sourceDir;
    }

    public String getSourceDir() {
        return sourceDir;
    }

    public synchronized void setPred(boolean pred) {
        filteredFileDate.setPredicate(p -> pred);
    }

    @Override
    public void clear() {
        sourceDir = "";
        super.clear();
    }

    public synchronized boolean addHashString(String pathFileName, PDate fileDate, PFileSize fileSize, String hash) {
        return addHashString(pathFileName, fileDate, fileSize, hash, false);
    }

    public synchronized boolean addHashString(String pathFileName, PDate fileDate, PFileSize fileSize, String hash, boolean link) {
        FileData fileData = new FileData(pathFileName, fileDate, fileSize, hash, link);
        return super.add(fileData);
    }

    public synchronized boolean add(FileData fileData) {
        return super.add(fileData);
    }


    public synchronized boolean addAll(ArrayList<FileData> d) {
        return super.addAll(d);
    }
}
