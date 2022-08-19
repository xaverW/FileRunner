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
import de.p2tools.fileRunner.controller.worker.compare.CompareFileList;
import de.p2tools.fileRunner.icon.ProgIcons;
import de.p2tools.p2Lib.P2LibConst;
import de.p2tools.p2Lib.dialogs.PDialogShowAgain;
import de.p2tools.p2Lib.guiTools.PButton;
import de.p2tools.p2Lib.guiTools.PGuiTools;
import de.p2tools.p2Lib.guiTools.pToggleSwitch.PToggleSwitchOnly;
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
    private final ToggleButton tglShowSame_1 = new ToggleButton("");
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

        vBoxBtn.getChildren().addAll(spacerTop, tglShowAll, spacer1,
                tglShowSame_1, tglShowSame_2, tglShowDiffAll, tglShowDiff, spacer2,
                tglShowOnly1, tglShowOnly2);

        Button btnHelp = PButton.helpButton(progData.primaryStage, "Vergleichen", HelpText.COMPARE_BUTTON);
        VBox vBox = new VBox();
        VBox.setVgrow(vBox, Priority.ALWAYS);
        vBox.setMaxHeight(Double.MAX_VALUE);
        vBox.setAlignment(Pos.BOTTOM_CENTER);
        vBox.getChildren().add(btnHelp);
        vBoxBtn.getChildren().add(vBox);

        SplitPane.setResizableWithParent(vBoxBtn, Boolean.FALSE);
        splitPane.getItems().addAll(guiDirPane1, vBoxBtn, guiDirPane2);


        //==================================
        //== Unten ==
        Label lblPath = new Label("Auch Unterverzeichnisse durchsuchen:");
        final PToggleSwitchOnly tglSubDir = new PToggleSwitchOnly();
        tglSubDir.setTooltip(new Tooltip("Es werden auch Dateien in Unterverzeichnissen verglichen"));
        tglSubDir.selectedProperty().bindBidirectional(ProgConfig.CONFIG_COMPARE_WITH_PATH);
        tglSubDir.selectedProperty().addListener((v, o, n) -> {
            new PDialogShowAgain(progData.primaryStage, ProgConfig.SYSTEM_SUBDIR_SHOW_AGAIN_DIALOG_SIZE,
                    "Unterverzeichnisse durchsuchen",
                    "Wenn \"Auch Unterverzeichnisse durchsuchen\" ein- oder ausgeschaltet wird, " +
                            "wird die Tabelle mit den Dateien gelöscht. " + P2LibConst.LINE_SEPARATORx2 +
                            "Das Verzeichnis muss neu eingelesen werden.",
                    ProgConfig.SYSTEM_SUBDIR_SHOW_AGAIN_DIALOG_SHOW);

            progData.fileDataList_1.clear();
            progData.fileDataList_2.clear();
            tglShowAll.fire();
            new CompareFileList().compareList();
        });

        Label lblHash = new Label("Gleiche Dateien mit FileID markieren:");
        RadioButton rbFile = new RadioButton("Mit Pfad/Dateiname/Hash vergleichen");
        RadioButton rbHash = new RadioButton("Nur mit Hash vergleichen");
        ToggleGroup tg = new ToggleGroup();
        tg.getToggles().addAll(rbFile, rbHash);
        VBox vb = new VBox(5);
        vb.getChildren().addAll(rbFile, rbHash);
        rbFile.setSelected(!ProgConfig.CONFIG_COMPARE_ONLY_WITH_HASH.getValue());
        rbHash.setSelected(ProgConfig.CONFIG_COMPARE_ONLY_WITH_HASH.getValue());
        rbHash.selectedProperty().bindBidirectional(ProgConfig.CONFIG_COMPARE_ONLY_WITH_HASH);
        rbHash.selectedProperty().addListener((v, o, n) -> {
            //das reicht dann, verglichen sind sie ja schon
            new CompareFileList().setFileId();
        });

        progData.pEventHandler.addListener(new PListener(Events.COMPARE_OF_FILE_LISTS_FINISHED) {
            public <T extends PEvent> void pingGui(T runEvent) {
                setTglButton();
            }
        });

        Button btnHelpPathHash = PButton.helpButton(progData.primaryStage, "", HelpText.READ_DIR_RECURSIVE_HASH);

        HBox hBox = new HBox(10);
        hBox.getStyleClass().add("extra-pane");
        hBox.setPadding(new Insets(10));
        hBox.setAlignment(Pos.CENTER);
        hBox.getChildren().addAll(lblPath, tglSubDir, PGuiTools.getHBoxGrower(),
                /*lblHash, vb, new Label("   "),*/ btnHelpPathHash);

        //== add all ==
        VBox vBoxAll = new VBox();
        VBox.setVgrow(splitPane, Priority.ALWAYS);
        vBoxAll.getChildren().addAll(splitPane, hBox);
        scrollPane.setContent(vBoxAll);
        scrollPane.setFitToHeight(true);
        scrollPane.setFitToWidth(true);
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
        tg.getToggles().addAll(tglShowAll, tglShowSame_1, tglShowSame_2, tglShowDiffAll, tglShowDiff, tglShowOnly1, tglShowOnly2);
        tglShowAll.setSelected(true);
        tglShowAll.setGraphic(ProgIcons.Icons.ICON_BUTTON_GUI_ALL.getImageView());
        tglShowSame_1.setGraphic(ProgIcons.Icons.ICON_BUTTON_GUI_SAME_1.getImageView());
        tglShowSame_2.setGraphic(ProgIcons.Icons.ICON_BUTTON_GUI_SAME_2.getImageView());
        tglShowDiffAll.setGraphic(ProgIcons.Icons.ICON_BUTTON_GUI_DIFF_ALL.getImageView());
        tglShowDiff.setGraphic(ProgIcons.Icons.ICON_BUTTON_GUI_DIFF.getImageView());
        tglShowOnly1.setGraphic(ProgIcons.Icons.ICON_BUTTON_GUI_ONLY_1.getImageView());
        tglShowOnly2.setGraphic(ProgIcons.Icons.ICON_BUTTON_GUI_ONLY_2.getImageView());

        tglShowAll.setTooltip(new Tooltip("Alle Dateien anzeigen."));
        tglShowSame_1.setTooltip(new Tooltip("Alle gleichen Dateien anzeigen."));
        tglShowSame_2.setTooltip(new Tooltip("Alle Dateien für die es eine andere mit gleichem Hash gibt, anzeigen."));
        tglShowDiffAll.setTooltip(new Tooltip("Dateien suchen, die sich unterscheiden oder nur in einer Liste enthalten sind."));
        tglShowDiff.setTooltip(new Tooltip("Dateien suchen, die in beiden Listen enthalten sind, sich aber unterscheiden."));
        tglShowOnly1.setTooltip(new Tooltip("Dateien suchen, die nur in Liste 1 enthalten sind."));
        tglShowOnly2.setTooltip(new Tooltip("Dateien suchen, die nur in Liste 2 enthalten sind."));
    }

    private void addListener() {
        setTglButton();
        tglShowAll.setOnAction(e -> setTglButton());
        tglShowSame_1.setOnAction(e -> setTglButton());
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
        } else if (tglShowSame_1.isSelected()) {
            tglShowSame_1.getStyleClass().clear();
            tglShowSame_1.getStyleClass().add("btnFilter-sel");

            fileDataFilter1.setFilter_types(FileDataFilter.FILTER_TYPES.SAME_1);
            fileDataFilter2.setFilter_types(FileDataFilter.FILTER_TYPES.SAME_1);
            progData.fileDataList_1.setPred(fileDataFilter1);
            progData.fileDataList_2.setPred(fileDataFilter2);
        } else if (tglShowSame_2.isSelected()) {
            tglShowSame_2.getStyleClass().clear();
            tglShowSame_2.getStyleClass().add("btnFilter-sel");

            fileDataFilter1.setFilter_types(FileDataFilter.FILTER_TYPES.SAME_2);
            fileDataFilter2.setFilter_types(FileDataFilter.FILTER_TYPES.SAME_2);
            progData.fileDataList_1.setPred(fileDataFilter1);
            progData.fileDataList_2.setPred(fileDataFilter2);
        } else if (tglShowDiffAll.isSelected()) {
            tglShowDiffAll.getStyleClass().clear();
            tglShowDiffAll.getStyleClass().add("btnFilter-sel");

            fileDataFilter1.setFilter_types(FileDataFilter.FILTER_TYPES.DIFF_ALL);
            fileDataFilter2.setFilter_types(FileDataFilter.FILTER_TYPES.DIFF_ALL);
            progData.fileDataList_1.setPred(fileDataFilter1);
            progData.fileDataList_2.setPred(fileDataFilter2);
        } else if (tglShowDiff.isSelected()) {
            tglShowDiff.getStyleClass().clear();
            tglShowDiff.getStyleClass().add("btnFilter-sel");

            fileDataFilter1.setFilter_types(FileDataFilter.FILTER_TYPES.DIFF);
            fileDataFilter2.setFilter_types(FileDataFilter.FILTER_TYPES.DIFF);
            progData.fileDataList_1.setPred(fileDataFilter1);
            progData.fileDataList_2.setPred(fileDataFilter2);
        } else if (tglShowOnly1.isSelected()) {
            tglShowOnly1.getStyleClass().clear();
            tglShowOnly1.getStyleClass().add("btnFilter-sel");

            fileDataFilter1.setFilter_types(FileDataFilter.FILTER_TYPES.ONLY);
            progData.fileDataList_1.setPred(fileDataFilter1);
            progData.fileDataList_2.setPred(false);
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
        tglShowSame_1.getStyleClass().clear();
        tglShowSame_2.getStyleClass().clear();
        tglShowDiffAll.getStyleClass().clear();
        tglShowDiff.getStyleClass().clear();
        tglShowOnly1.getStyleClass().clear();
        tglShowOnly2.getStyleClass().clear();
        tglShowAll.getStyleClass().add("btnFilter");
        tglShowSame_1.getStyleClass().add("btnFilter");
        tglShowSame_2.getStyleClass().add("btnFilter");
        tglShowDiffAll.getStyleClass().add("btnFilter");
        tglShowDiff.getStyleClass().add("btnFilter");
        tglShowOnly1.getStyleClass().add("btnFilter");
        tglShowOnly2.getStyleClass().add("btnFilter");
    }
}
