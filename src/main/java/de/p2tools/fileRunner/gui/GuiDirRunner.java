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

package de.p2tools.fileRunner.gui;

import de.p2tools.fileRunner.controller.config.ProgData;
import de.p2tools.fileRunner.controller.data.Icons;
import de.p2tools.fileRunner.controller.data.fileData.FileDataFilter;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

public class GuiDirRunner extends AnchorPane {
    private final SplitPane splitPane = new SplitPane();
//    private final HBox splitHbox = new HBox();

    private final VBox vBoxBtn = new VBox(10);
    private final ScrollPane scrollPane = new ScrollPane();

    private final ToggleButton tglShowAll = new ToggleButton("");
    private final ToggleButton tglShowSame = new ToggleButton("");
    private final ToggleButton tglShowDiff = new ToggleButton("");
    private final ToggleButton tglShowDiffAll = new ToggleButton("");
    private final ToggleButton tglShowOnly1 = new ToggleButton("");
    private final ToggleButton tglShowOnly2 = new ToggleButton("");

    private final ProgData progData;
    private final FileDataFilter fileDataFilter1 = new FileDataFilter();
    private final FileDataFilter fileDataFilter2 = new FileDataFilter();
    private final GuiDirPane guiDirPane1;
    private final GuiDirPane guiDirPane2;

    private final int SHOW_ALL = 0;
    private final int SHOW_PANE_1 = 1;
    private final int SHOW_PANE_2 = 2;
    private int show = SHOW_PANE_1;
    private boolean dragged = false;

    private double orgX, orgDiv0, orgDiv1, orgSize;

    public GuiDirRunner() {
        progData = ProgData.getInstance();

        AnchorPane.setLeftAnchor(scrollPane, 0.0);
        AnchorPane.setBottomAnchor(scrollPane, 0.0);
        AnchorPane.setRightAnchor(scrollPane, 0.0);
        AnchorPane.setTopAnchor(scrollPane, 0.0);
        getChildren().addAll(scrollPane);

        guiDirPane1 = new GuiDirPane(progData, this, fileDataFilter1, true);
        guiDirPane2 = new GuiDirPane(progData, this, fileDataFilter2, false);

        initCont();
        addListener();
    }

    public void isShown() {
    }

    private void initCont() {
        vBoxBtn.getStyleClass().add("pane-border");
//        vBoxBtn.setStyle("-fx-border-color: red;");

        vBoxBtn.setAlignment(Pos.CENTER);
        vBoxBtn.setPadding(new Insets(10));
//        vBoxBtn.setMaxWidth(Region.USE_PREF_SIZE);

        Region spacer = new Region();
        spacer.setMinSize(10, 10);
        vBoxBtn.getChildren().addAll(tglShowAll, tglShowSame, tglShowDiffAll, spacer, tglShowDiff, tglShowOnly1, tglShowOnly2);

        SplitPane.setResizableWithParent(vBoxBtn, Boolean.FALSE);

//        HBox.setHgrow(guiDirPane1, Priority.ALWAYS);
//        HBox.setHgrow(guiDirPane2, Priority.ALWAYS);

        splitPane.getItems().addAll(guiDirPane1, vBoxBtn, guiDirPane2);
        scrollPane.setContent(splitPane);
//        splitHbox.getChildren().addAll(guiDirPane1, vBoxBtn, guiDirPane2);
//        scrollPane.setContent(splitHbox);

        scrollPane.setFitToHeight(true);
        scrollPane.setFitToWidth(true);

        vBoxBtn.setOnMouseClicked(e -> {
            if (!dragged) {
                setVis();
            }
            System.out.println("dragged=false");
            dragged = false;
        });

        vBoxBtn.setOnMousePressed(e -> {
            dragged = false;
            if (splitPane.getItems().size() == 3) {
                orgX = e.getSceneX();
                orgDiv0 = splitPane.getDividers().get(0).getPosition();
                orgDiv1 = splitPane.getDividers().get(1).getPosition();
                orgSize = splitPane.getWidth();
            }
        });

        vBoxBtn.setOnMouseDragged(e -> {
            dragged = true;
            if (splitPane.getItems().size() == 3) {
                double offsetX = e.getSceneX() - orgX;
                double move = offsetX / orgSize;

                double ddiv0 = orgDiv0 + move;
                double ddiv1 = orgDiv1 + move;

                splitPane.getDividers().get(0).setPosition(ddiv0);
                splitPane.getDividers().get(1).setPosition(ddiv1);
//            System.out.println("dragged: " + ddiv0 + " " + ddiv1 + " " + move);
            }
        });

        // =================================
        ToggleGroup tg = new ToggleGroup();
        tg.selectedToggleProperty().addListener((obsVal, oldVal, newVal) -> {
            if (newVal == null)
                oldVal.setSelected(true);
        });
        tg.getToggles().addAll(tglShowAll, tglShowSame, tglShowDiffAll, tglShowDiff, tglShowOnly1, tglShowOnly2);
        tglShowAll.setSelected(true);
        tglShowAll.setGraphic(new Icons().ICON_BUTTON_GUI_ALL);
        tglShowSame.setGraphic(new Icons().ICON_BUTTON_GUI_SAME);
        tglShowDiffAll.setGraphic(new Icons().ICON_BUTTON_GUI_DIFF_ALL);
        tglShowDiff.setGraphic(new Icons().ICON_BUTTON_GUI_DIFF);
        tglShowOnly1.setGraphic(new Icons().ICON_BUTTON_GUI_ONLY_1);
        tglShowOnly2.setGraphic(new Icons().ICON_BUTTON_GUI_ONLY_2);

        tglShowAll.setTooltip(new Tooltip("Alle Dateien anzeigen."));
        tglShowSame.setTooltip(new Tooltip("Alle gleichen Dateien anzeigen."));
        tglShowDiffAll.setTooltip(new Tooltip("Dateien suchen, die sich unterscheiden oder nur in einer Liste enthalten sind."));
        tglShowDiff.setTooltip(new Tooltip("Dateien suchen, die in beiden Listen enthalten sind, sich aber unterscheiden."));
        tglShowOnly1.setTooltip(new Tooltip("Dateien suchen, die nur in Liste 1 enthalten sind."));
        tglShowOnly2.setTooltip(new Tooltip("Dateien suchen, die nur in Liste 2 enthalten sind."));
    }

    private synchronized void setVis() {
        switch (show) {
            case SHOW_ALL:
                splitPane.getItems().clear();
                splitPane.getItems().addAll(guiDirPane1, vBoxBtn, guiDirPane2);

                show = SHOW_PANE_1;
                break;

            case SHOW_PANE_1:
                orgDiv0 = splitPane.getDividers().get(0).getPosition();
                orgDiv1 = splitPane.getDividers().get(1).getPosition();
                splitPane.getItems().clear();
                splitPane.getItems().addAll(guiDirPane1, vBoxBtn);

                splitPane.getDividers().get(0).setPosition(orgDiv0 + orgDiv1);
                show = SHOW_PANE_2;
                break;

            case SHOW_PANE_2:
                orgDiv0 = splitPane.getDividers().get(0).getPosition();
                splitPane.getItems().clear();
                splitPane.getItems().addAll(vBoxBtn, guiDirPane2);

                splitPane.getDividers().get(0).setPosition(1 - orgDiv0);
                show = SHOW_ALL;
                break;
        }
    }

//    private synchronized void setVis() {
//        double v;
//        switch (show) {
//            case SHOW_ALL:
//                splitHbox.getChildren().clear();
//                splitHbox.getChildren().addAll(guiDirPane1, vBoxBtn, guiDirPane2);
//
//                show = SHOW_PANE_1;
//                break;
//
//            case SHOW_PANE_1:
//                splitHbox.getChildren().clear();
//                splitHbox.getChildren().addAll(guiDirPane1, vBoxBtn);
//
//                show = SHOW_PANE_2;
//                break;
//
//            case SHOW_PANE_2:
//                splitHbox.getChildren().clear();
//                splitHbox.getChildren().addAll(vBoxBtn, guiDirPane2);
//
//                show = SHOW_ALL;
//                break;
//        }
//    }

    private void addListener() {
        tglShowAll.setOnAction(e -> {
            fileDataFilter1.setFilter_types(FileDataFilter.FILTER_TYPES.ALL);
            fileDataFilter2.setFilter_types(FileDataFilter.FILTER_TYPES.ALL);
            progData.fileDataList1.setPred(fileDataFilter1);
            progData.fileDataList2.setPred(fileDataFilter2);
        });
        tglShowSame.setOnAction(e -> {
            fileDataFilter1.setFilter_types(FileDataFilter.FILTER_TYPES.SAME);
            fileDataFilter2.setFilter_types(FileDataFilter.FILTER_TYPES.SAME);
            progData.fileDataList1.setPred(fileDataFilter1);
            progData.fileDataList2.setPred(fileDataFilter2);
        });
        tglShowDiffAll.setOnAction(e -> {
            fileDataFilter1.setFilter_types(FileDataFilter.FILTER_TYPES.DIFF_ALL);
            fileDataFilter2.setFilter_types(FileDataFilter.FILTER_TYPES.DIFF_ALL);
            progData.fileDataList1.setPred(fileDataFilter1);
            progData.fileDataList2.setPred(fileDataFilter2);
        });
        tglShowDiff.setOnAction(e -> {
            fileDataFilter1.setFilter_types(FileDataFilter.FILTER_TYPES.DIFF);
            fileDataFilter2.setFilter_types(FileDataFilter.FILTER_TYPES.DIFF);
            progData.fileDataList1.setPred(fileDataFilter1);
            progData.fileDataList2.setPred(fileDataFilter2);
        });
        tglShowOnly1.setOnAction(e -> {
            fileDataFilter1.setFilter_types(FileDataFilter.FILTER_TYPES.ONLY);
            progData.fileDataList1.setPred(fileDataFilter1);
            progData.fileDataList2.setPred(false);
        });
        tglShowOnly2.setOnAction(e -> {
            progData.fileDataList1.setPred(false);
            fileDataFilter2.setFilter_types(FileDataFilter.FILTER_TYPES.ONLY);
            progData.fileDataList2.setPred(fileDataFilter2);
        });
    }

    public void clearFilter() {
        tglShowAll.setSelected(true);

        fileDataFilter1.setFilter_types(FileDataFilter.FILTER_TYPES.ALL);
        fileDataFilter2.setFilter_types(FileDataFilter.FILTER_TYPES.ALL);

        progData.fileDataList1.setPred(fileDataFilter1);
        progData.fileDataList2.setPred(fileDataFilter2);
    }

    public void saveTable() {
        guiDirPane1.saveTable();
        guiDirPane2.saveTable();
    }
}
