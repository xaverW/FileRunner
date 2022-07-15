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


package de.p2tools.fileRunner.controller.data.projectData;

import de.p2tools.fileRunner.controller.data.fileData.FileDataList;
import de.p2tools.p2Lib.configFile.config.Config;
import de.p2tools.p2Lib.configFile.config.ConfigBoolProp;
import de.p2tools.p2Lib.configFile.config.ConfigIntProp;
import de.p2tools.p2Lib.configFile.config.ConfigStringProp;
import de.p2tools.p2Lib.configFile.configList.ConfigStringList;
import de.p2tools.p2Lib.configFile.pData.PDataSample;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class ProjectDataProps extends PDataSample<ProjectData> {
    public static final String TAG = "ProjectData";

    // compare file
    private final IntegerProperty compFileSel1 = new SimpleIntegerProperty(0);
    private final IntegerProperty compFileSel2 = new SimpleIntegerProperty(0);

    private final StringProperty compFileSrcFile1 = new SimpleStringProperty("");
    private final StringProperty compFileSrcFile2 = new SimpleStringProperty("");
    private ObservableList<String> compFileSrcFileList = FXCollections.observableArrayList();

    private final StringProperty compFileHashFile1 = new SimpleStringProperty("");
    private final StringProperty compFileHashFile2 = new SimpleStringProperty("");
    private ObservableList<String> compFileHashFileList = FXCollections.observableArrayList();

    private final StringProperty compFileHash1 = new SimpleStringProperty("");
    private final StringProperty compFileHash2 = new SimpleStringProperty("");

    // compare dir
    private final StringProperty srcDir1 = new SimpleStringProperty("");
    private final StringProperty srcDir2 = new SimpleStringProperty("");
    private ObservableList<String> srcDirList = FXCollections.observableArrayList();

    private final StringProperty srcZip1 = new SimpleStringProperty("");
    private final StringProperty srcZip2 = new SimpleStringProperty("");
    private ObservableList<String> srcZipList = FXCollections.observableArrayList();

    private final StringProperty srcHash1 = new SimpleStringProperty("");
    private final StringProperty srcHash2 = new SimpleStringProperty("");
    private ObservableList<String> srcHashList = FXCollections.observableArrayList();


    private final StringProperty filter1 = new SimpleStringProperty("");
    private final StringProperty filter2 = new SimpleStringProperty("");
    private ObservableList<String> filterList = FXCollections.observableArrayList();

    private final StringProperty writeHash1 = new SimpleStringProperty("");
    private final StringProperty writeHash2 = new SimpleStringProperty("");
    private final StringProperty writeFileHash1 = new SimpleStringProperty("");
    private final StringProperty writeFileHash2 = new SimpleStringProperty("");
    private ObservableList<String> writeHashList = FXCollections.observableArrayList();
    private ObservableList<String> writeFileHashList = FXCollections.observableArrayList();
    private FileDataList fileDataList;

    private final IntegerProperty selTab1 = new SimpleIntegerProperty(0);
    private final IntegerProperty selTab2 = new SimpleIntegerProperty(0);

    private final BooleanProperty followLink1 = new SimpleBooleanProperty(false);
    private final BooleanProperty followLink2 = new SimpleBooleanProperty(false);


    public String getTag() {
        return TAG;
    }

    public String getComment() {
        return "project data";
    }

    public Config[] getConfigsArr() {
        return new Config[]{
                // comp file
                new ConfigIntProp("comp-file-sel-1", compFileSel1),
                new ConfigIntProp("comp-file-sel-2", compFileSel2),
                new ConfigStringProp("comp-file-src-file-1", compFileSrcFile1),
                new ConfigStringProp("comp-file-src-file-2", compFileSrcFile2),
                new ConfigStringList("comp-file-src-file-list", compFileSrcFileList),
                new ConfigStringProp("comp-file-hash-file-1", compFileHashFile1),
                new ConfigStringProp("comp-file-hash-file-2", compFileHashFile2),
                new ConfigStringList("comp-file-hash-file-list", compFileHashFileList),
                new ConfigStringProp("comp-file-hash-1", compFileHash1),
                new ConfigStringProp("comp-file-hash-2", compFileHash2),

                // comp dir
                new ConfigStringProp("src-dir-1", srcDir1),
                new ConfigStringProp("src-dir-2", srcDir2),
                new ConfigStringList("src-dir-list", srcDirList),
                new ConfigStringProp("src-zip-1", srcZip1),
                new ConfigStringProp("src-zip-2", srcZip2),
                new ConfigStringList("src-zip-list", srcZipList),
                new ConfigStringProp("src-hash-1", srcHash1),
                new ConfigStringProp("src-hash-2", srcHash2),
                new ConfigStringList("src-hash-list", srcHashList),
                new ConfigStringProp("filter-1", filter1),
                new ConfigStringProp("filter-2", filter2),
                new ConfigStringList("filter-list", filterList),
                new ConfigStringProp("write-hash-1", writeHash1),
                new ConfigStringProp("write-hash-2", writeHash2),
                new ConfigStringProp("write-file-hash-1", writeFileHash1),
                new ConfigStringProp("write-file-hash-2", writeFileHash2),
                new ConfigStringList("write-hash-list", writeHashList),
                new ConfigStringList("write-file-hash-list", writeFileHashList),
                new ConfigIntProp("tab-file-1", selTab1),
                new ConfigIntProp("tab-file-2", selTab2),
                new ConfigBoolProp("followLink1", followLink1),
                new ConfigBoolProp("followLink2", followLink2)};
    }

    //===============================================================
    // compare file

    public int getCompFileSel1() {
        return compFileSel1.get();
    }

    public IntegerProperty compFileSel1Property() {
        return compFileSel1;
    }

    public void setCompFileSel1(int compFileSel1) {
        this.compFileSel1.set(compFileSel1);
    }

    public int getCompFileSel2() {
        return compFileSel2.get();
    }

    public IntegerProperty compFileSel2Property() {
        return compFileSel2;
    }

    public void setCompFileSel2(int compFileSel2) {
        this.compFileSel2.set(compFileSel2);
    }

    public String getCompFileSrcFile1() {
        return compFileSrcFile1.get();
    }

    public StringProperty compFileSrcFile1Property() {
        return compFileSrcFile1;
    }

    public void setCompFileSrcFile1(String compFileSrcFile1) {
        this.compFileSrcFile1.set(compFileSrcFile1);
    }

    public String getCompFileSrcFile2() {
        return compFileSrcFile2.get();
    }

    public StringProperty compFileSrcFile2Property() {
        return compFileSrcFile2;
    }

    public void setCompFileSrcFile2(String compFileSrcFile2) {
        this.compFileSrcFile2.set(compFileSrcFile2);
    }

    public ObservableList<String> getCompFileSrcFileList() {
        return compFileSrcFileList;
    }

    public void setCompFileSrcFileList(ObservableList<String> compFileSrcFileList) {
        this.compFileSrcFileList = compFileSrcFileList;
    }

    public String getCompFileHashFile1() {
        return compFileHashFile1.get();
    }

    public StringProperty compFileHashFile1Property() {
        return compFileHashFile1;
    }

    public void setCompFileHashFile1(String compFileHashFile1) {
        this.compFileHashFile1.set(compFileHashFile1);
    }

    public String getCompFileHashFile2() {
        return compFileHashFile2.get();
    }

    public StringProperty compFileHashFile2Property() {
        return compFileHashFile2;
    }

    public void setCompFileHashFile2(String compFileHashFile2) {
        this.compFileHashFile2.set(compFileHashFile2);
    }

    public ObservableList<String> getCompFileHashFileList() {
        return compFileHashFileList;
    }

    public void setCompFileHashFileList(ObservableList<String> compFileHashFileList) {
        this.compFileHashFileList = compFileHashFileList;
    }

    public String getCompFileHash1() {
        return compFileHash1.get();
    }

    public StringProperty compFileHash1Property() {
        return compFileHash1;
    }

    public void setCompFileHash1(String compFileHash1) {
        this.compFileHash1.set(compFileHash1);
    }

    public String getCompFileHash2() {
        return compFileHash2.get();
    }

    public StringProperty compFileHash2Property() {
        return compFileHash2;
    }

    public void setCompFileHash2(String compFileHash2) {
        this.compFileHash2.set(compFileHash2);
    }

    //===============================================================
    // compare dir
    public String getSrcDir1() {
        return srcDir1.get();
    }

    public StringProperty srcDir1Property() {
        return srcDir1;
    }

    public void setSrcDir1(String srcDir1) {
        this.srcDir1.set(srcDir1);
    }

    public String getSrcDir2() {
        return srcDir2.get();
    }

    public StringProperty srcDir2Property() {
        return srcDir2;
    }

    public void setSrcDir2(String srcDir2) {
        this.srcDir2.set(srcDir2);
    }

    public ObservableList<String> getSrcDirList() {
        return srcDirList;
    }

    public void setSrcDirList(ObservableList<String> srcDirList) {
        this.srcDirList = srcDirList;
    }

    public String getSrcZip1() {
        return srcZip1.get();
    }

    public StringProperty srcZip1Property() {
        return srcZip1;
    }

    public void setSrcZip1(String srcZip1) {
        this.srcZip1.set(srcZip1);
    }

    public String getSrcZip2() {
        return srcZip2.get();
    }

    public StringProperty srcZip2Property() {
        return srcZip2;
    }

    public void setSrcZip2(String srcZip2) {
        this.srcZip2.set(srcZip2);
    }

    public ObservableList<String> getSrcZipList() {
        return srcZipList;
    }

    public void setSrcZipList(ObservableList<String> srcZipList) {
        this.srcZipList = srcZipList;
    }

    public String getSrcHash1() {
        return srcHash1.get();
    }

    public StringProperty srcHash1Property() {
        return srcHash1;
    }

    public void setSrcHash1(String srcHash1) {
        this.srcHash1.set(srcHash1);
    }

    public String getSrcHash2() {
        return srcHash2.get();
    }

    public StringProperty srcHash2Property() {
        return srcHash2;
    }

    public void setSrcHash2(String srcHash2) {
        this.srcHash2.set(srcHash2);
    }

    public ObservableList<String> getSrcHashList() {
        return srcHashList;
    }

    public void setSrcHashList(ObservableList<String> srcHashList) {
        this.srcHashList = srcHashList;
    }

    public String getFilter1() {
        return filter1.get();
    }

    public StringProperty filter1Property() {
        return filter1;
    }

    public void setFilter1(String filter1) {
        this.filter1.set(filter1);
    }

    public String getFilter2() {
        return filter2.get();
    }

    public StringProperty filter2Property() {
        return filter2;
    }

    public void setFilter2(String filter2) {
        this.filter2.set(filter2);
    }

    public ObservableList<String> getFilterList() {
        return filterList;
    }

    public void setFilterList(ObservableList<String> filterList) {
        this.filterList = filterList;
    }

    public String getWriteHash1() {
        return writeHash1.get();
    }

    public StringProperty writeHash1Property() {
        return writeHash1;
    }

    public void setWriteHash1(String writeHash1) {
        this.writeHash1.set(writeHash1);
    }

    public String getWriteHash2() {
        return writeHash2.get();
    }

    public StringProperty writeHash2Property() {
        return writeHash2;
    }

    public void setWriteHash2(String writeHash2) {
        this.writeHash2.set(writeHash2);
    }

    public String getWriteFileHash1() {
        return writeFileHash1.get();
    }

    public StringProperty writeFileHash1Property() {
        return writeFileHash1;
    }

    public void setWriteFileHash1(String writeFileHash1) {
        this.writeFileHash1.set(writeFileHash1);
    }

    public String getWriteFileHash2() {
        return writeFileHash2.get();
    }

    public StringProperty writeFileHash2Property() {
        return writeFileHash2;
    }

    public void setWriteFileHash2(String writeFileHash2) {
        this.writeFileHash2.set(writeFileHash2);
    }

    public ObservableList<String> getWriteHashList() {
        return writeHashList;
    }

    public void setWriteHashList(ObservableList<String> writeHashList) {
        this.writeHashList = writeHashList;
    }

    public ObservableList<String> getWriteFileHashList() {
        return writeFileHashList;
    }

    public void setWriteFileHashList(ObservableList<String> writeFileHashList) {
        this.writeFileHashList = writeFileHashList;
    }

    public int getSelTab1() {
        return selTab1.get();
    }

    public IntegerProperty selTab1Property() {
        return selTab1;
    }

    public void setSelTab1(int selTab1) {
        this.selTab1.set(selTab1);
    }

    public int getSelTab2() {
        return selTab2.get();
    }

    public IntegerProperty selTab2Property() {
        return selTab2;
    }

    public void setSelTab2(int selTab2) {
        this.selTab2.set(selTab2);
    }

    public boolean isFollowLink1() {
        return followLink1.get();
    }

    public BooleanProperty followLink1Property() {
        return followLink1;
    }

    public void setFollowLink1(boolean followLink1) {
        this.followLink1.set(followLink1);
    }

    public boolean isFollowLink2() {
        return followLink2.get();
    }

    public BooleanProperty followLink2Property() {
        return followLink2;
    }

    public void setFollowLink2(boolean followLink2) {
        this.followLink2.set(followLink2);
    }

    @Override
    public int compareTo(ProjectData arg0) {
        return getSrcDir1().compareTo(arg0.getSrcDir1());
    }

}
