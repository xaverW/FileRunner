/*
 * MTPlayer Copyright (C) 2017 W. Xaver W.Xaver[at]googlemail.com
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


package de.p2tools.fileRunner.controller.data.projectData;

import de.p2tools.fileRunner.controller.data.Data;
import de.p2tools.p2Lib.configFile.config.Config;
import de.p2tools.p2Lib.configFile.config.ConfigStringProp;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.util.ArrayList;
import java.util.Arrays;

public class ProjectDataProps extends Data<ProjectData> {
    public static final String TAG = "ProjectData";

    private final StringProperty srcDir1 = new SimpleStringProperty("");
    private final StringProperty srcDir2 = new SimpleStringProperty("");

    private final StringProperty srcHash1 = new SimpleStringProperty("");
    private final StringProperty srcHash2 = new SimpleStringProperty("");

    private final StringProperty search1 = new SimpleStringProperty("");
    private final StringProperty search2 = new SimpleStringProperty("");

    private final StringProperty writeHash1 = new SimpleStringProperty("");
    private final StringProperty writeHash2 = new SimpleStringProperty("");


    public String getTag() {
        return TAG;
    }

    public ArrayList<Config> getConfigsArr() {
        return new ArrayList<>(Arrays.asList(
                new ConfigStringProp("src-dir-1", "", srcDir1),
                new ConfigStringProp("src-dir-2", "", srcDir2),
                new ConfigStringProp("src-hash-1", "", srcHash1),
                new ConfigStringProp("src-hash-2", "", srcHash2),
                new ConfigStringProp("search-1", "", search1),
                new ConfigStringProp("search-2", "", search2),
                new ConfigStringProp("write-hash-1", "", writeHash1),
                new ConfigStringProp("write-hash-2", "", writeHash2)
        ));
    }

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

    public String getSearch1() {
        return search1.get();
    }

    public StringProperty search1Property() {
        return search1;
    }

    public void setSearch1(String search1) {
        this.search1.set(search1);
    }

    public String getSearch2() {
        return search2.get();
    }

    public StringProperty search2Property() {
        return search2;
    }

    public void setSearch2(String search2) {
        this.search2.set(search2);
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

    @Override
    public int compareTo(ProjectData arg0) {
        return getSrcDir1().compareTo(arg0.getSrcDir1());
    }

}
