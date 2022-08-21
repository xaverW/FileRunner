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


package de.p2tools.fileRunner.icon;

import de.p2tools.fileRunner.controller.config.ProgConfig;
import de.p2tools.fileRunner.controller.config.ProgConst;
import de.p2tools.p2Lib.icons.GetIcon;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class ProgIcons {
    public static String ICON_PATH = ProgConst.PATH_PROGRAM_ICONS;

    public enum Icons {
        ICON_TAB_SEARCH("tab-search.png", 16, 16),
        ICON_TAB_DIR_FILE("tab-dir-file.png", 16, 16),

        ICON_BUTTON_HELP("button-help.png", 16, 16),
        ICON_BUTTON_FILE_OPEN("button-file-open.png", 16, 16),
        ICON_BUTTON_FILE_SAVE("button-file-save.png", 16, 16),
        ICON_BUTTON_GEN_HASH("button-gen-hash.png", 16, 16),
        ICON_BUTTON_CLEAR_SELECTION("button-clear-selection.png", 16, 16),
        
        ICON_BUTTON_GUI_NEXT("button-gui-next_.png", 40, 100),
        ICON_BUTTON_GUI_PREV("button-gui-prev_.png", 40, 100),

        ICON_BUTTON_GUI_CLEAR("button-gui-clear.png", 16, 16),
        ICON_BUTTON_GUI_ALL("button-gui-all.png", 24, 24),
        ICON_BUTTON_GUI_START_ALL("button-gui-start-all.png", 16, 16),
        ICON_BUTTON_GUI_SAME_1("button-gui-same_1.png", 24, 24),
        ICON_BUTTON_GUI_SAME_2("button-gui-same_2.png", 24, 24),
        ICON_BUTTON_GUI_DIFF("button-gui-diff.png", 24, 24),
        ICON_BUTTON_GUI_DIFF_ALL("button-gui-diff-all.png", 24, 24),
        ICON_BUTTON_GUI_ONLY_1("button-gui-only-1.png", 24, 24),
        ICON_BUTTON_GUI_ONLY_2("button-gui-only-2.png", 24, 24),
        IMAGE_TABLE_OPEN_DIR("table-open-dir.png", 14, 14),


        ICON_BUTTON_STOP("button-stop.png", 16, 16),
        ICON_BUTTON_GUI_GEN_NAME("button-gen-name.png", 16, 16),

        FX_ICON_TOOLBAR_MENUE_TOP("menue-top.png", 32, 18),
        FX_ICON_TOOLBAR_MENUE("menue.png", 18, 15);


        private String fileName;
        private String fileNameDark = "";
        private int w = 0;
        private int h = 0;

        Icons(String fileName, int w, int h) {
            this.fileName = fileName;
            this.w = w;
            this.h = h;
        }

        Icons(String fileName, String fileNameDark, int w, int h) {
            this.fileName = fileName;
            this.fileNameDark = fileNameDark;
            this.w = w;
            this.h = h;
        }

        Icons(String fileName) {
            this.fileName = fileName;
        }

        Icons(String fileName, String fileNameDark) {
            this.fileName = fileName;
            this.fileNameDark = fileNameDark;
        }

        public ImageView getImageView() {
            if (ProgConfig.SYSTEM_DARK_THEME.get() && !fileNameDark.isEmpty()) {
                return GetIcon.getImageView(fileNameDark, ICON_PATH, w, h);
            }
            return GetIcon.getImageView(fileName, ICON_PATH, w, h);
        }

        public Image getImage() {
            if (ProgConfig.SYSTEM_DARK_THEME.get() && !fileNameDark.isEmpty()) {
                return GetIcon.getImage(fileNameDark, ICON_PATH, w, h);
            }
            return GetIcon.getImage(fileName, ICON_PATH, w, h);
        }
    }
}
