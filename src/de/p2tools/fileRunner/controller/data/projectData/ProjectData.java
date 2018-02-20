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

public class ProjectData extends ProjectDataProps {

    public ProjectData() {
    }

    public ProjectData(String dir1, ProjectData projectData) {
        setSrcDir1(dir1);
        if (projectData != null) {
            setSrcDir2(projectData.getSrcDir2());
            setSrcHash1(projectData.getSrcHash1());
            setSrcHash2(projectData.getSrcHash2());
            setWriteHash1(projectData.getWriteHash1());
            setWriteHash2(projectData.getWriteHash2());
        }
    }


}
