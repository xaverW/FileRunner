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


package de.p2tools.fileRunner.controller.data.fileData;

import de.p2tools.p2Lib.tools.Datum;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;

import java.util.ArrayList;

public class FileDataList extends SimpleListProperty<FileData> {
    private int nr = 1;

    private final FilteredList<FileData> filteredFileDate;
    private final SortedList<FileData> sortedFileData;

    public FileDataList() {
        super(FXCollections.observableArrayList());
        filteredFileDate = new FilteredList<>(this, p -> true);
        sortedFileData = new SortedList<>(filteredFileDate);
    }

    public SortedList<FileData> getSortedFileData() {
        return sortedFileData;
    }

    public void clear() {
        nr = 1;
        super.clear();
    }

    public synchronized boolean addHashString(String file, Datum fileDate, String hash) {
        FileData fileData = new FileData(file, fileDate, hash);
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
