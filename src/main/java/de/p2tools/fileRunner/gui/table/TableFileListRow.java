/*
 * P2tools Copyright (C) 2021 W. Xaver W.Xaver[at]googlemail.com
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


package de.p2tools.fileRunner.gui.table;

import de.p2tools.fileRunner.controller.config.ProgColorList;
import de.p2tools.fileRunner.controller.config.ProgConfig;
import de.p2tools.fileRunner.controller.data.fileData.FileData;
import javafx.scene.input.MouseButton;

public class TableFileListRow<T> extends javafx.scene.control.TableRow {
    private TableFileList tableFileList;

    public TableFileListRow(TableFileList tableFileList) {
        this.tableFileList = tableFileList;
    }

    @Override
    public void updateItem(Object o, boolean empty) {
        super.updateItem(o, empty);

        //erst mal einrichten/aufräumen
        FileData fileData;
        if (o != null && o.getClass().equals(FileData.class)) {
            fileData = (FileData) o;
        } else {
            fileData = null;
        }
        boolean odd = getIndex() % 2 > 0 && ProgConfig.SYSTEM_EVEN_ODD.getValue();

        setOnMouseClicked(event -> {
            if (event.getButton().equals(MouseButton.PRIMARY) && event.getClickCount() == 2) {
                tableFileList.getSelectionModel().clearSelection();
            }
        });

        setStyle("");
        for (int i = 0; i < getChildren().size(); i++) {
            getChildren().get(i).setStyle("");
        }

        //und jetzt die Farben setzen
        if (isSelected()) {
            if (ProgColorList.TABLE_ROW_IS_SEL_BG.isUse()) {
                setStyle(ProgColorList.TABLE_ROW_IS_SEL_BG.getCssBackgroundAndSel(odd));
            }
            if (ProgColorList.TABLE_ROW_IS_SEL.isUse()) {
                for (int i = 0; i < getChildren().size(); i++) {
                    getChildren().get(i).setStyle(ProgColorList.TABLE_ROW_IS_SEL.getCssFont(odd));
                }
            }
            return;
        }

        //===========================================
        if (fileData != null && !empty) {

            //dann gibts eine gleiche Datei
            int id = fileData.getFilePathId() +
                    fileData.getFileNameId() + fileData.getHashId();
            if (id != 0) {
                //Hintergrundfarbe wird nach HashID gefärbt
                if (id % 2 == 0 || !ProgColorList.FILE_IS_ID2_BG.isUse()) {
                    //gerade ID
                    if (ProgColorList.FILE_IS_ID1_BG.isUse()) {
                        setStyle(ProgColorList.FILE_IS_ID1_BG.getCssBackground(odd));
                    }
                } else if (ProgColorList.FILE_IS_ID2_BG.isUse()) {
                    //ungerade ID
                    setStyle(ProgColorList.FILE_IS_ID2_BG.getCssBackground(odd));
                }

                //die Schriftfarbe wird nach HashID gefärbt
                if (id % 2 == 0 || !ProgColorList.FILE_IS_ID2.isUse()) {
                    //gerade ID
                    if (ProgColorList.FILE_IS_ID1.isUse()) {
                        for (int i = 0; i < getChildren().size(); i++) {
                            getChildren().get(i).setStyle(ProgColorList.FILE_IS_ID1.getCssFont(odd));
                        }
                    }
                } else if (ProgColorList.FILE_IS_ID2.isUse()) {
                    //ungerade ID
                    for (int i = 0; i < getChildren().size(); i++) {
                        getChildren().get(i).setStyle(ProgColorList.FILE_IS_ID2.getCssFont(odd));
                    }
                }
            }

//            if (id != 0) {
//                //Hintergrundfarbe wird nach HashID gefärbt
//                if (ProgColorList.FILE_IS_ID1_BG.isUse()) {
//                    setStyle(ProgColorList.FILE_IS_ID1_BG.getCssBackground(odd));
//                }
//
//                //die Schriftfarbe wird nach HashID gefärbt
//                if (ProgColorList.FILE_IS_ID1.isUse()) {
//                    for (int i = 0; i < getChildren().size(); i++) {
//                        getChildren().get(i).setStyle(ProgColorList.FILE_IS_ID1.getCssFont(odd));
//                    }
//                }
//            }

            //so werden auch gleiche Dateien im selben Verzeichnisbaum als
            //only gekennzeichnet
            if (fileData.isLink()) {
                //Datei ist ein Symlink
                if (ProgColorList.FILE_LINK_BG.isUse()) {
                    setStyle(ProgColorList.FILE_LINK_BG.getCssBackground(odd));
                }
                if (ProgColorList.FILE_LINK.isUse()) {
                    for (int i = 0; i < getChildren().size(); i++) {
                        getChildren().get(i).setStyle(ProgColorList.FILE_LINK.getCssFont(odd));
                    }
                }

            } else if (fileData.isDiff()) {
                //Dateien sind unterschiedlich
                if (ProgColorList.FILE_IS_DIFF_BG.isUse()) {
                    setStyle(ProgColorList.FILE_IS_DIFF_BG.getCssBackground(odd));
                }
                if (ProgColorList.FILE_IS_DIFF.isUse()) {
                    for (int i = 0; i < getChildren().size(); i++) {
                        getChildren().get(i).setStyle(ProgColorList.FILE_IS_DIFF.getCssFont(odd));
                    }
                }

            } else if (fileData.isOnly()) {
                if (fileData.getHashId() > 0) {
                    //dann gibts doppelte
                    if (ProgColorList.FILE_IS_ONLY_HASH_BG.isUse()) {
                        setStyle(ProgColorList.FILE_IS_ONLY_HASH_BG.getCssBackground(odd));
                    }
                    if (ProgColorList.FILE_IS_ONLY_HASH.isUse()) {
                        for (int i = 0; i < getChildren().size(); i++) {
                            getChildren().get(i).setStyle(ProgColorList.FILE_IS_ONLY_HASH.getCssFont(odd));
                        }
                    }

                } else {
                    //Datei gibts nur einmal
                    if (ProgColorList.FILE_IS_ONLY_BG.isUse()) {
                        setStyle(ProgColorList.FILE_IS_ONLY_BG.getCssBackground(odd));
                    }
                    if (ProgColorList.FILE_IS_ONLY.isUse()) {
                        for (int i = 0; i < getChildren().size(); i++) {
                            getChildren().get(i).setStyle(ProgColorList.FILE_IS_ONLY.getCssFont(odd));
                        }
                    }
                }
            }
        }
        //===========================================
    }
}
