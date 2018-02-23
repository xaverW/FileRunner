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
import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;

import java.util.ArrayList;
import java.util.function.Predicate;

public class FileDataList extends SimpleListProperty<FileData> {
    private int nr = 1;

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

        if (!fileDataFilter.isAll()) {
            predicate = predicate.and(f -> fileDataFilter.isDiff() == f.isDiff());
            predicate = predicate.and(f -> fileDataFilter.isOnly() == f.isOnly());
        }
        if (!fileDataFilter.getName().trim().isEmpty()) {
            predicate = predicate.and(f -> f.getFileName().toLowerCase().contains(fileDataFilter.getName().toLowerCase()));
        }

        filteredFileDate.setPredicate(predicate);
    }

    public synchronized void setPred(boolean pred) {
        filteredFileDate.setPredicate(p -> pred);
    }

//    public void setPred(boolean diff, boolean only) {
//        Predicate<FileData> predicate = film -> true;
//
//        predicate = predicate.and(f -> diff == f.isDiff());
//        predicate = predicate.and(f -> only == f.isOnly());
//
//        filteredFileDate.setPredicate(predicate);
//    }

    public void clear() {
        nr = 1;
        super.clear();
    }

    public synchronized boolean addHashString(String file, PDate fileDate, FileSize fileSize, String hash) {
        FileData fileData = new FileData(file, fileDate, fileSize, hash);
        fileData.setNr(nr++);
        return super.add(fileData);
    }

    public synchronized boolean add(FileData fileData) {
        fileData.setNr(nr++);
        return super.add(fileData);
    }


    public synchronized boolean addAll(ArrayList<FileData> d) {
        d.forEach(fileData -> fileData.setNr(nr++));
        return super.addAll(d);
    }


}
