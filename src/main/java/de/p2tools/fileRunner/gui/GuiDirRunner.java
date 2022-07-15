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

import de.p2tools.fileRunner.controller.config.ProgConfig;
import de.p2tools.fileRunner.controller.config.ProgData;
import de.p2tools.fileRunner.controller.data.ProgIcons;
import de.p2tools.fileRunner.controller.data.fileData.FileDataFilter;
import de.p2tools.fileRunner.controller.worker.compare.CompareFileList;
import de.p2tools.p2Lib.guiTools.PButton;
import de.p2tools.p2Lib.guiTools.pToggleSwitch.PToggleSwitchOnly;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

public class GuiDirRunner extends AnchorPane {
    private final SplitPane splitPane = new SplitPane();

    private final VBox vBoxBtn = new VBox(10);
    private final ScrollPane scrollPane = new ScrollPane();

    private final ToggleButton tglShowAll = new ToggleButton("");
    private final ToggleButton tglShowSame = new ToggleButton("");
    private final ToggleButton tglShowDiff = new ToggleButton("");
    private final ToggleButton tglShowDiffAll = new ToggleButton("");
    private final ToggleButton tglShowOnly1 = new ToggleButton("");
    private final ToggleButton tglShowOnly2 = new ToggleButton("");
    private Label lblPath = new Label("ohne\nPfad");
    private final PToggleSwitchOnly tglWithPath = new PToggleSwitchOnly();

    private final ProgData progData;
    private final FileDataFilter fileDataFilter1 = new FileDataFilter();
    private final FileDataFilter fileDataFilter2 = new FileDataFilter();
    private final GuiDirPane guiDirPane1;
    private final GuiDirPane guiDirPane2;
    private double orgX, orgDiv0, orgDiv1, orgSize;

    public GuiDirRunner() {
        progData = ProgData.getInstance();

        AnchorPane.setLeftAnchor(scrollPane, 0.0);
        AnchorPane.setBottomAnchor(scrollPane, 0.0);
        AnchorPane.setRightAnchor(scrollPane, 0.0);
        AnchorPane.setTopAnchor(scrollPane, 0.0);
        getChildren().addAll(scrollPane);

        guiDirPane1 = new GuiDirPane(progData, fileDataFilter1, true);
        guiDirPane2 = new GuiDirPane(progData, fileDataFilter2, false);

        initCont();
        addListener();
    }

    public void isShown() {
    }

    public void resetFilter() {
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

    private void initCont() {
        vBoxBtn.getStyleClass().add("pane-border");
        vBoxBtn.setAlignment(Pos.TOP_CENTER);
        vBoxBtn.setPadding(new Insets(10));
        vBoxBtn.setMaxWidth(Region.USE_PREF_SIZE);
        Region spacerTop = new Region();
        spacerTop.setMinSize(10, 150);
        Region spacer1 = new Region();
        spacer1.setMinSize(10, 10);
        Region spacer2 = new Region();
        spacer2.setMinSize(10, 25);
        vBoxBtn.getChildren().addAll(spacerTop, tglShowAll, tglShowSame, tglShowDiffAll,
                spacer1, tglShowDiff, tglShowOnly1, tglShowOnly2,
                spacer2, lblPath, tglWithPath);

        Button btnHelp = PButton.helpButton(progData.primaryStage, "Vergleichen", HelpText.COMPARE_BUTTON);
        VBox vBox = new VBox();
        VBox.setVgrow(vBox, Priority.ALWAYS);
        vBox.setMaxHeight(Double.MAX_VALUE);
        vBox.setAlignment(Pos.BOTTOM_CENTER);
        vBox.getChildren().add(btnHelp);
        vBoxBtn.getChildren().add(vBox);

        SplitPane.setResizableWithParent(vBoxBtn, Boolean.FALSE);

        splitPane.getItems().addAll(guiDirPane1, vBoxBtn, guiDirPane2);
        scrollPane.setContent(splitPane);
        scrollPane.setFitToHeight(true);
        scrollPane.setFitToWidth(true);

        vBoxBtn.setOnMousePressed(e -> {
            orgX = e.getSceneX();
            orgDiv0 = splitPane.getDividers().get(0).getPosition();
            orgDiv1 = splitPane.getDividers().get(1).getPosition();
            orgSize = splitPane.getWidth();
        });

        vBoxBtn.setOnMouseReleased(e -> {
            vBoxBtn.setMaxWidth(Region.USE_PREF_SIZE);
        });

        vBoxBtn.setOnMouseDragged(e -> {
            double offsetX = e.getSceneX() - orgX;
            double move = offsetX / orgSize;
            double ddiv0 = orgDiv0 + move;
            double ddiv1 = orgDiv1 + move;

            vBoxBtn.setMaxWidth(Region.USE_COMPUTED_SIZE);
            splitPane.getDividers().get(1).setPosition(ddiv1);
            splitPane.getDividers().get(0).setPosition(ddiv0);
        });

        // =================================
        ToggleGroup tg = new ToggleGroup();
        tg.selectedToggleProperty().addListener((obsVal, oldVal, newVal) -> {
            if (newVal == null)
                oldVal.setSelected(true);
        });
        tg.getToggles().addAll(tglShowAll, tglShowSame, tglShowDiffAll, tglShowDiff, tglShowOnly1, tglShowOnly2);
        tglShowAll.setSelected(true);
        tglShowAll.setGraphic(new ProgIcons().ICON_BUTTON_GUI_ALL);
        tglShowSame.setGraphic(new ProgIcons().ICON_BUTTON_GUI_SAME);
        tglShowDiffAll.setGraphic(new ProgIcons().ICON_BUTTON_GUI_DIFF_ALL);
        tglShowDiff.setGraphic(new ProgIcons().ICON_BUTTON_GUI_DIFF);
        tglShowOnly1.setGraphic(new ProgIcons().ICON_BUTTON_GUI_ONLY_1);
        tglShowOnly2.setGraphic(new ProgIcons().ICON_BUTTON_GUI_ONLY_2);

        tglShowAll.setTooltip(new Tooltip("Alle Dateien anzeigen."));
        tglShowSame.setTooltip(new Tooltip("Alle gleichen Dateien anzeigen."));
        tglShowDiffAll.setTooltip(new Tooltip("Dateien suchen, die sich unterscheiden oder nur in einer Liste enthalten sind."));
        tglShowDiff.setTooltip(new Tooltip("Dateien suchen, die in beiden Listen enthalten sind, sich aber unterscheiden."));
        tglShowOnly1.setTooltip(new Tooltip("Dateien suchen, die nur in Liste 1 enthalten sind."));
        tglShowOnly2.setTooltip(new Tooltip("Dateien suchen, die nur in Liste 2 enthalten sind."));
        tglWithPath.setTooltip(new Tooltip("Es werden die Dateien mit oder ohne Pfad verglichen"));
    }

    private void addListener() {
        setTglButton();
        tglShowAll.setOnAction(e -> {
            setTglButton();
        });
        tglShowSame.setOnAction(e -> {
            setTglButton();
        });
        tglShowDiffAll.setOnAction(e -> {
            setTglButton();
        });
        tglShowDiff.setOnAction(e -> {
            setTglButton();
        });
        tglShowOnly1.setOnAction(e -> {
            setTglButton();
        });
        tglShowOnly2.setOnAction(e -> {
            setTglButton();
        });

        tglWithPath.selectedProperty().bindBidirectional(ProgConfig.CONFIG_COMPARE_WITH_PATH);
        tglWithPath.selectedProperty().addListener((v, o, n) -> {
            setTextPath();
            new CompareFileList().compareList();
            setTglButton();
        });
        lblPath.setMinWidth(lblPath.getPrefWidth());
        lblPath.setAlignment(Pos.CENTER_RIGHT);
        lblPath.setPadding(new Insets(0, 0, 5, 0));

        setTextPath();
    }

    private void setTextPath() {
        if (tglWithPath.isSelected()) {
            lblPath.setText("mit\nPfad");
        } else {
            lblPath.setText("ohne\nPfad");
        }
    }

    private void setTglButton() {
        if (tglShowAll.isSelected()) {
            fileDataFilter1.setFilter_types(FileDataFilter.FILTER_TYPES.ALL);
            fileDataFilter2.setFilter_types(FileDataFilter.FILTER_TYPES.ALL);
            progData.fileDataList1.setPred(fileDataFilter1);
            progData.fileDataList2.setPred(fileDataFilter2);
        } else if (tglShowSame.isSelected()) {
            fileDataFilter1.setFilter_types(FileDataFilter.FILTER_TYPES.SAME);
            fileDataFilter2.setFilter_types(FileDataFilter.FILTER_TYPES.SAME);
            progData.fileDataList1.setPred(fileDataFilter1);
            progData.fileDataList2.setPred(fileDataFilter2);
        } else if (tglShowDiffAll.isSelected()) {
            fileDataFilter1.setFilter_types(FileDataFilter.FILTER_TYPES.DIFF_ALL);
            fileDataFilter2.setFilter_types(FileDataFilter.FILTER_TYPES.DIFF_ALL);
            progData.fileDataList1.setPred(fileDataFilter1);
            progData.fileDataList2.setPred(fileDataFilter2);
        } else if (tglShowDiff.isSelected()) {
            fileDataFilter1.setFilter_types(FileDataFilter.FILTER_TYPES.DIFF);
            fileDataFilter2.setFilter_types(FileDataFilter.FILTER_TYPES.DIFF);
            progData.fileDataList1.setPred(fileDataFilter1);
            progData.fileDataList2.setPred(fileDataFilter2);
        } else if (tglShowOnly1.isSelected()) {
            fileDataFilter1.setFilter_types(FileDataFilter.FILTER_TYPES.ONLY);
            progData.fileDataList1.setPred(fileDataFilter1);
            progData.fileDataList2.setPred(false);
        } else if (tglShowOnly2.isSelected()) {
            progData.fileDataList1.setPred(false);
            fileDataFilter2.setFilter_types(FileDataFilter.FILTER_TYPES.ONLY);
            progData.fileDataList2.setPred(fileDataFilter2);
        }
    }
}
