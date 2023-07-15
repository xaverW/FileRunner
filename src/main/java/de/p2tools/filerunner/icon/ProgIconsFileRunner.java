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


package de.p2tools.filerunner.icon;

import de.p2tools.filerunner.FileRunnerController;
import de.p2tools.filerunner.controller.config.ProgConst;
import de.p2tools.p2lib.P2LibConst;
import de.p2tools.p2lib.icons.P2Icon;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class ProgIconsFileRunner {
    public static String ICON_PATH = "icon/program/";
    ;
    public static String ICON_PATH_LONG = "de/p2tools/filerunner/icon/program/";
    ;

    private static final List<P2IconFileRunner> iconList = new ArrayList<>();

    public static P2IconFileRunner ICON_TAB_SEARCH = new P2IconFileRunner(ICON_PATH_LONG, ICON_PATH, "tab-search.png", 16, 16);
    public static P2IconFileRunner ICON_TAB_DIR_FILE = new P2IconFileRunner(ICON_PATH_LONG, ICON_PATH, "tab-dir-file.png", 16, 16);

    public static P2IconFileRunner ICON_BUTTON_HELP = new P2IconFileRunner(ICON_PATH_LONG, ICON_PATH, "button-help.png", 16, 16);
    public static P2IconFileRunner ICON_BUTTON_FILE_OPEN = new P2IconFileRunner(ICON_PATH_LONG, ICON_PATH, "button-file-open.png", 16, 16);
    public static P2IconFileRunner ICON_BUTTON_FILE_SAVE = new P2IconFileRunner(ICON_PATH_LONG, ICON_PATH, "button-file-save.png", 16, 16);
    public static P2IconFileRunner ICON_BUTTON_GEN_HASH = new P2IconFileRunner(ICON_PATH_LONG, ICON_PATH, "button-gen-hash.png", 16, 16);
    public static P2IconFileRunner ICON_BUTTON_START_READ_ALL = new P2IconFileRunner(ICON_PATH_LONG, ICON_PATH, "button-start-read-all.png", 16, 16);

    public static P2IconFileRunner ICON_LABEL_FILE_OK = new P2IconFileRunner(ICON_PATH_LONG, ICON_PATH, "label-file-ok.png", 20, 20);
    public static P2IconFileRunner ICON_LABEL_FILE_NOT_OK = new P2IconFileRunner(ICON_PATH_LONG, ICON_PATH, "label-file-not-ok.png", 20, 20);

    public static P2IconFileRunner ICON_BUTTON_GUI_NEXT = new P2IconFileRunner(ICON_PATH_LONG, ICON_PATH, "button-gui-next_.png", 40, 100);
    public static P2IconFileRunner ICON_BUTTON_GUI_PREV = new P2IconFileRunner(ICON_PATH_LONG, ICON_PATH, "button-gui-prev_.png", 40, 100);

    public static P2IconFileRunner ICON_BUTTON_GUI_CLEAR = new P2IconFileRunner(ICON_PATH_LONG, ICON_PATH, "button-gui-clear.png", 16, 16);
    public static P2IconFileRunner ICON_BUTTON_GUI_ALL = new P2IconFileRunner(ICON_PATH_LONG, ICON_PATH, "button-gui-all.png", 24, 24);
    public static P2IconFileRunner ICON_BUTTON_GUI_START_ALL = new P2IconFileRunner(ICON_PATH_LONG, ICON_PATH, "button-gui-start-all.png", 16, 16);
    public static P2IconFileRunner ICON_BUTTON_GUI_SAME_1 = new P2IconFileRunner(ICON_PATH_LONG, ICON_PATH, "button-gui-same_1.png", 24, 24);
    public static P2IconFileRunner ICON_BUTTON_GUI_SAME_2 = new P2IconFileRunner(ICON_PATH_LONG, ICON_PATH, "button-gui-same_2.png", 24, 24);
    public static P2IconFileRunner ICON_BUTTON_GUI_DIFF_OR_ONLY = new P2IconFileRunner(ICON_PATH_LONG, ICON_PATH, "button-gui-diff.png", 24, 24);
    public static P2IconFileRunner ICON_BUTTON_GUI_DIFF_IN_BOTHE = new P2IconFileRunner(ICON_PATH_LONG, ICON_PATH, "button-gui-diff-all.png", 24, 24);
    public static P2IconFileRunner ICON_BUTTON_GUI_ONLY_1 = new P2IconFileRunner(ICON_PATH_LONG, ICON_PATH, "button-gui-only-1.png", 24, 24);
    public static P2IconFileRunner ICON_BUTTON_GUI_ONLY_2 = new P2IconFileRunner(ICON_PATH_LONG, ICON_PATH, "button-gui-only-2.png", 24, 24);
    public static P2IconFileRunner IMAGE_TABLE_OPEN_DIR = new P2IconFileRunner(ICON_PATH_LONG, ICON_PATH, "table-open-dir.png", 14, 14);

    public static P2IconFileRunner ICON_BUTTON_STOP = new P2IconFileRunner(ICON_PATH_LONG, ICON_PATH, "button-stop.png", 16, 16);
    public static P2IconFileRunner ICON_BUTTON_GUI_GEN_NAME = new P2IconFileRunner(ICON_PATH_LONG, ICON_PATH, "button-gen-name.png", 16, 16);

    public static P2IconFileRunner FX_ICON_TOOLBAR_MENUE_TOP = new P2IconFileRunner(ICON_PATH_LONG, ICON_PATH, "menue-top.png", 32, 18);
    public static P2IconFileRunner FX_ICON_TOOLBAR_MENUE = new P2IconFileRunner(ICON_PATH_LONG, ICON_PATH, "menue.png", 18, 15);


    public static void initIcons() {
        iconList.forEach(p -> {
            String url = p.genUrl(P2IconFileRunner.class, FileRunnerController.class, ProgConst.class, ProgIconsFileRunner.class, P2LibConst.class);
            if (url.isEmpty()) {
                // dann wurde keine gefunden
                System.out.println("ProgIconsInfo: keine URL, icon: " + p.getPathFileNameDark() + " - " + p.getFileName());
            }
        });
    }

    public static class P2IconFileRunner extends P2Icon {
        public P2IconFileRunner(String longPath, String path, String fileName, int w, int h) {
            super(longPath, path, fileName, w, h);
            iconList.add(this);
        }

        public boolean searchUrl(String p, Class<?>... clazzAr) {
            URL url;
            url = FileRunnerController.class.getResource(p);
            if (set(url, p, "P2InfoController.class.getResource")) return true;
            url = ProgConst.class.getResource(p);
            if (set(url, p, "ProgConst.class.getResource")) return true;
            url = ProgIconsFileRunner.class.getResource(p);
            if (set(url, p, "ProgIconsInfo.class.getResource")) return true;
            url = this.getClass().getResource(p);
            if (set(url, p, "this.getClass().getResource")) return true;

            url = ClassLoader.getSystemResource(p);
            if (set(url, p, "ClassLoader.getSystemResource")) return true;
            url = P2LibConst.class.getClassLoader().getResource(p);
            if (set(url, p, "P2LibConst.class.getClassLoader().getResource")) return true;
            url = ProgConst.class.getClassLoader().getResource(p);
            if (set(url, p, "ProgConst.class.getClassLoader().getResource")) return true;
            url = this.getClass().getClassLoader().getResource(p);
            if (set(url, p, "this.getClass().getClassLoader().getResource")) return true;

            return false;
        }
    }
}