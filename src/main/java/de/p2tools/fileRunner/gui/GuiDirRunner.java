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

import de.p2tools.fileRunner.controller.config.Events;
import de.p2tools.fileRunner.controller.config.ProgConfig;
import de.p2tools.fileRunner.controller.config.ProgData;
import de.p2tools.fileRunner.controller.data.fileData.FileDataFilter;
import de.p2tools.fileRunner.controller.worker.compare.CompareFileListFactory;
import de.p2tools.fileRunner.icon.ProgIcons;
import de.p2tools.p2Lib.guiTools.PButton;
import de.p2tools.p2Lib.guiTools.PGuiTools;
import de.p2tools.p2Lib.tools.events.PEvent;
import de.p2tools.p2Lib.tools.events.PListener;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;

public class GuiDirRunner extends AnchorPane {
    private final SplitPane splitPane = new SplitPane();

    private final VBox vBoxBtn = new VBox(10);
    private final ScrollPane scrollPane = new ScrollPane();

    private final ToggleButton tglShowAll = new ToggleButton("");
    private final ToggleButton tglShowSame = new ToggleButton("");
    private final ToggleButton tglShowSame_2 = new ToggleButton("");
    private final ToggleButton tglShowDiff = new ToggleButton("");
    private final ToggleButton tglShowDiffAll = new ToggleButton("");
    private final ToggleButton tglShowOnly1 = new ToggleButton("");
    private final ToggleButton tglShowOnly2 = new ToggleButton("");

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
        initSplit();
        addToggleGroup();
        addListener();
    }

    public void isShown() {
    }

    public void resetFilter() {
        tglShowAll.setSelected(true);
        fileDataFilter1.setFilter_types(FileDataFilter.FILTER_TYPES.ALL);
        fileDataFilter2.setFilter_types(FileDataFilter.FILTER_TYPES.ALL);
        progData.fileDataList_1.setPred(fileDataFilter1);
        progData.fileDataList_2.setPred(fileDataFilter2);
    }

    public void saveTable() {
        guiDirPane1.saveTable();
        guiDirPane2.saveTable();
    }

    private void initCont() {
        //== Oben ==
        vBoxBtn.getStyleClass().add("pane-border");
        vBoxBtn.setAlignment(Pos.TOP_CENTER);
        vBoxBtn.setPadding(new Insets(10));
        vBoxBtn.setMaxWidth(Region.USE_PREF_SIZE);
        Region spacerTop = new Region();
        spacerTop.setMinSize(10, 150);
        Region spacer1 = new Region();
        spacer1.setMinSize(10, 10);
        Region spacer2 = new Region();
        spacer2.setMinSize(10, 10);
        Region spacer3 = new Region();
        spacer3.setMinSize(10, 10);

        vBoxBtn.getChildren().addAll(spacerTop, tglShowAll, spacer1,
                tglShowSame, tglShowSame_2, spacer2, tglShowDiffAll, tglShowDiff, spacer3,
                tglShowOnly1, tglShowOnly2);

        final Button btnClearSelection = new Button();
        btnClearSelection.setGraphic(ProgIcons.Icons.ICON_BUTTON_CLEAR_SELECTION.getImageView());
        btnClearSelection.setTooltip(new Tooltip("Die Auswahl in den Tabellen löschen"));
        btnClearSelection.setOnAction(a -> {
            guiDirPane1.clearTableSelection();
            guiDirPane2.clearTableSelection();
        });

        Button btnHelp = PButton.helpButton(progData.primaryStage, "Vergleichen", HelpText.COMPARE_BUTTON);

        VBox vBox = new VBox(10);
        VBox.setVgrow(vBox, Priority.ALWAYS);
        vBox.setMaxHeight(Double.MAX_VALUE);
        vBox.setAlignment(Pos.BOTTOM_CENTER);
        vBox.getChildren().addAll(btnClearSelection, btnHelp);
        vBoxBtn.getChildren().add(vBox);

        SplitPane.setResizableWithParent(vBoxBtn, Boolean.FALSE);
        splitPane.getItems().addAll(guiDirPane1, vBoxBtn, guiDirPane2);


        //==================================
        //== Unten ==
        Label lblHash = new Label("Dateien sind gleich wenn:");
        Button btnHelpPathHash = PButton.helpButton(progData.primaryStage, "", HelpText.READ_DIR_HASH);

        RadioButton rbFile = new RadioButton("Pfad/Dateiname/Hash gleich sind");
        RadioButton rbHash = new RadioButton("Nur der Hash gleich ist");
        rbFile.setTooltip(new Tooltip("Dateien sind gleich wenn \"Pfad/Name/Hash\" gleich sind"));
        rbHash.setTooltip(new Tooltip("Dateien sind gleich wenn der \"Hash\" gleich ist"));
        ToggleGroup tg = new ToggleGroup();
        tg.getToggles().addAll(rbFile, rbHash);
        rbFile.setSelected(!ProgConfig.CONFIG_COMPARE_ONLY_WITH_HASH.getValue());
        rbHash.setSelected(ProgConfig.CONFIG_COMPARE_ONLY_WITH_HASH.getValue());
        rbHash.selectedProperty().bindBidirectional(ProgConfig.CONFIG_COMPARE_ONLY_WITH_HASH);
        rbHash.selectedProperty().addListener((v, o, n) -> {
            CompareFileListFactory.compareList();
        });

        if (rbHash.isSelected()) {
            rbHash.setStyle("-fx-font-weight: normal;");
            rbFile.setStyle("-fx-underline: true;");
        } else {
            rbHash.setStyle("-fx-font-weight: normal;");
            rbFile.setStyle("-fx-underline: true;");
        }
        rbHash.selectedProperty().addListener((v, o, n) -> {
            if (n) {
                rbHash.setStyle("-fx-underline: true;");
                rbFile.setStyle("-fx-font-weight: normal;");
            } else {
                rbHash.setStyle("-fx-font-weight: normal;");
                rbFile.setStyle("-fx-underline: true;");
            }
        });

        HBox hBoxHash = new HBox(10);
        hBoxHash.getStyleClass().add("extra-pane");
        hBoxHash.setPadding(new Insets(10));
        hBoxHash.setAlignment(Pos.CENTER);
        hBoxHash.getChildren().addAll(lblHash, PGuiTools.getHDistance(20),
                rbFile, PGuiTools.getHDistance(20),
                rbHash, PGuiTools.getHBoxGrower(), btnHelpPathHash);

        //== add all ==
        VBox vBoxAll = new VBox();
        VBox.setVgrow(splitPane, Priority.ALWAYS);
        vBoxAll.getChildren().addAll(splitPane, hBoxHash);
        scrollPane.setContent(vBoxAll);
        scrollPane.setFitToHeight(true);
        scrollPane.setFitToWidth(true);

        progData.pEventHandler.addListener(new PListener(Events.COMPARE_OF_FILE_LISTS_FINISHED) {
            public <T extends PEvent> void pingGui(T runEvent) {
                setTglButton();
            }
        });
    }

    private void initSplit() {
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
    }

    private void addToggleGroup() {
        ToggleGroup tg = new ToggleGroup();
        tg.selectedToggleProperty().addListener((obsVal, oldVal, newVal) -> {
            if (newVal == null)
                oldVal.setSelected(true);
        });
        tg.getToggles().addAll(tglShowAll, tglShowSame, tglShowSame_2, tglShowDiffAll, tglShowDiff, tglShowOnly1, tglShowOnly2);
        tglShowAll.setSelected(true);
        tglShowAll.setGraphic(ProgIcons.Icons.ICON_BUTTON_GUI_ALL.getImageView());
        tglShowSame.setGraphic(ProgIcons.Icons.ICON_BUTTON_GUI_SAME_1.getImageView());
        tglShowSame_2.setGraphic(ProgIcons.Icons.ICON_BUTTON_GUI_SAME_2.getImageView());
        tglShowDiffAll.setGraphic(ProgIcons.Icons.ICON_BUTTON_GUI_DIFF.getImageView());
        tglShowDiff.setGraphic(ProgIcons.Icons.ICON_BUTTON_GUI_DIFF_ALL.getImageView());
        tglShowOnly1.setGraphic(ProgIcons.Icons.ICON_BUTTON_GUI_ONLY_1.getImageView());
        tglShowOnly2.setGraphic(ProgIcons.Icons.ICON_BUTTON_GUI_ONLY_2.getImageView());

        tglShowAll.setTooltip(new Tooltip("Alle Dateien anzeigen."));
        tglShowSame.setTooltip(new Tooltip("Alle gleichen Dateien anzeigen."));
        tglShowSame_2.setTooltip(new Tooltip("Alle Dateien für die es eine andere mit gleichem Hash gibt, anzeigen."));
        tglShowDiffAll.setTooltip(new Tooltip("Dateien suchen, die sich unterscheiden oder nur in einer Liste enthalten sind."));
        tglShowDiff.setTooltip(new Tooltip("Dateien suchen, die in beiden Listen enthalten sind, sich aber unterscheiden."));
        tglShowOnly1.setTooltip(new Tooltip("Dateien suchen, die nur in Liste 1 enthalten sind."));
        tglShowOnly2.setTooltip(new Tooltip("Dateien suchen, die nur in Liste 2 enthalten sind."));
    }

    private void addListener() {
        setTglButton();
        tglShowAll.setOnAction(e -> setTglButton());
        tglShowSame.setOnAction(e -> setTglButton());
        tglShowSame_2.setOnAction(e -> setTglButton());
        tglShowDiffAll.setOnAction(e -> setTglButton());
        tglShowDiff.setOnAction(e -> setTglButton());
        tglShowOnly1.setOnAction(e -> setTglButton());
        tglShowOnly2.setOnAction(e -> setTglButton());
    }

    private void setTglButton() {
        clear();
        if (tglShowAll.isSelected()) {
            tglShowAll.getStyleClass().clear();
            tglShowAll.getStyleClass().add("btnFilter-sel");

            fileDataFilter1.setFilter_types(FileDataFilter.FILTER_TYPES.ALL);
            fileDataFilter2.setFilter_types(FileDataFilter.FILTER_TYPES.ALL);
            progData.fileDataList_1.setPred(fileDataFilter1);
            progData.fileDataList_2.setPred(fileDataFilter2);
        } else if (tglShowSame.isSelected()) {
            tglShowSame.getStyleClass().clear();
            tglShowSame.getStyleClass().add("btnFilter-sel");

            fileDataFilter1.setFilter_types(FileDataFilter.FILTER_TYPES.SAME);
            fileDataFilter2.setFilter_types(FileDataFilter.FILTER_TYPES.SAME);
            progData.fileDataList_1.setPred(fileDataFilter1);
            progData.fileDataList_2.setPred(fileDataFilter2);
        } else if (tglShowSame_2.isSelected()) {
            tglShowSame_2.getStyleClass().clear();
            tglShowSame_2.getStyleClass().add("btnFilter-sel");

            fileDataFilter1.setFilter_types(FileDataFilter.FILTER_TYPES.SAME2);
            fileDataFilter2.setFilter_types(FileDataFilter.FILTER_TYPES.SAME2);
            progData.fileDataList_1.setPred(fileDataFilter1);
            progData.fileDataList_2.setPred(fileDataFilter2);
        } else if (tglShowDiffAll.isSelected()) {
            tglShowDiffAll.getStyleClass().clear();
            tglShowDiffAll.getStyleClass().add("btnFilter-sel");

            fileDataFilter1.setFilter_types(FileDataFilter.FILTER_TYPES.DIFF_ALL);
            fileDataFilter2.setFilter_types(FileDataFilter.FILTER_TYPES.DIFF_ALL);
            progData.fileDataList_1.setPred(fileDataFilter1);
            progData.fileDataList_2.setPred(fileDataFilter2);
        } else if (tglShowOnly1.isSelected()) {
            tglShowOnly1.getStyleClass().clear();
            tglShowOnly1.getStyleClass().add("btnFilter-sel");

            fileDataFilter1.setFilter_types(FileDataFilter.FILTER_TYPES.ONLY);
            progData.fileDataList_1.setPred(fileDataFilter1);
            progData.fileDataList_2.setPred(false);
        } else if (tglShowDiff.isSelected()) {
            tglShowDiff.getStyleClass().clear();
            tglShowDiff.getStyleClass().add("btnFilter-sel");

            fileDataFilter1.setFilter_types(FileDataFilter.FILTER_TYPES.DIFF);
            fileDataFilter2.setFilter_types(FileDataFilter.FILTER_TYPES.DIFF);
            progData.fileDataList_1.setPred(fileDataFilter1);
            progData.fileDataList_2.setPred(fileDataFilter2);
        } else if (tglShowOnly2.isSelected()) {
            tglShowOnly2.getStyleClass().clear();
            tglShowOnly2.getStyleClass().add("btnFilter-sel");

            progData.fileDataList_1.setPred(false);
            fileDataFilter2.setFilter_types(FileDataFilter.FILTER_TYPES.ONLY);
            progData.fileDataList_2.setPred(fileDataFilter2);
        }
    }

    private void clear() {
        tglShowAll.getStyleClass().clear();
        tglShowSame.getStyleClass().clear();
        tglShowSame_2.getStyleClass().clear();
        tglShowDiff.getStyleClass().clear();
        tglShowDiffAll.getStyleClass().clear();
        tglShowOnly1.getStyleClass().clear();
        tglShowOnly2.getStyleClass().clear();
        tglShowAll.getStyleClass().add("btnFilter");
        tglShowSame.getStyleClass().add("btnFilter");
        tglShowSame_2.getStyleClass().add("btnFilter");
        tglShowDiff.getStyleClass().add("btnFilter");
        tglShowDiffAll.getStyleClass().add("btnFilter");
        tglShowOnly1.getStyleClass().add("btnFilter");
        tglShowOnly2.getStyleClass().add("btnFilter");
    }
}
