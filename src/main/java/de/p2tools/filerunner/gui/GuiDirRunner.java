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

package de.p2tools.filerunner.gui;

import de.p2tools.filerunner.controller.config.Events;
import de.p2tools.filerunner.controller.config.ProgConfig;
import de.p2tools.filerunner.controller.config.ProgConst;
import de.p2tools.filerunner.controller.config.ProgData;
import de.p2tools.filerunner.controller.data.filedata.FileDataFilter;
import de.p2tools.filerunner.controller.worker.CompareFileListFactory;
import de.p2tools.filerunner.gui.picon.PIconFactory;
import de.p2tools.p2lib.guitools.P2Button;
import de.p2tools.p2lib.guitools.P2GuiTools;
import de.p2tools.p2lib.p2event.P2Event;
import de.p2tools.p2lib.p2event.P2Listener;
import de.p2tools.p2lib.tools.log.P2Log;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;

public class GuiDirRunner extends AnchorPane {
    private final SplitPane splitPane = new SplitPane();

    private final VBox vBoxBtn = new VBox(10);
    private final ScrollPane scrollPane = new ScrollPane();
    private final Button btnRead = new Button("");

    private final ToggleButton tglShowAll = new ToggleButton("");
    private final ToggleButton tglShowSamePath = new ToggleButton("");
    private final ToggleButton tglShowDiff_IN_BOTH = new ToggleButton("");
    private final ToggleButton tglShowDiff_OR_ONLY = new ToggleButton("");
    private final ToggleButton tglShowOnlyLeft = new ToggleButton("");
    private final ToggleButton tglShowOnlyRight = new ToggleButton("");

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
        vBoxBtn.setStyle(" -fx-border-width: 1; -fx-border-color: -style-tab-border-color-1;");
        vBoxBtn.setAlignment(Pos.TOP_CENTER);
        vBoxBtn.setPadding(new Insets(10));
        vBoxBtn.setMaxWidth(Region.USE_PREF_SIZE);
        vBoxBtn.getChildren().addAll(P2GuiTools.getHDistance(150), tglShowAll,
                P2GuiTools.getHDistance(10), tglShowSamePath,
                P2GuiTools.getHDistance(10), tglShowDiff_OR_ONLY, tglShowDiff_IN_BOTH,
                P2GuiTools.getHDistance(10), tglShowOnlyLeft, tglShowOnlyRight);

        Button btnHelp = P2Button.helpButton(progData.primaryStage, "Vergleichen", HelpText.COMPARE_BUTTON);

        VBox vBox = new VBox(10);
        VBox.setVgrow(vBox, Priority.ALWAYS);
        vBox.setMaxHeight(Double.MAX_VALUE);
        vBox.setAlignment(Pos.BOTTOM_CENTER);
        vBox.getChildren().addAll(btnRead, btnHelp);
        vBoxBtn.getChildren().add(vBox);

        SplitPane.setResizableWithParent(vBoxBtn, Boolean.FALSE);
        splitPane.getItems().addAll(guiDirPane1, vBoxBtn, guiDirPane2);

        //==================================
        //== Unten ==
        Label lblHash = new Label("Gleiche Dateien haben gleichen");
        Button btnHelpPathHash = P2Button.helpButton(progData.primaryStage, "", HelpText.READ_DIR_HASH);

        RadioButton rbFilePath = new RadioButton("Pfad/Dateinamen und Hash");
        RadioButton rbFileName = new RadioButton("Dateinamen und Hash");
        RadioButton rbFileAll = new RadioButton("Hash");
        rbFilePath.setTooltip(new Tooltip("Dateien sind gleich, wenn der \"Pfad/Name/Hash\" gleich ist"));
        rbFileName.setTooltip(new Tooltip("Dateien sind gleich, wenn der \"Name/Hash\" gleich ist"));
        rbFileAll.setTooltip(new Tooltip("Dateien sind gleich, wenn der \"Hash\" gleich ist, Name und Pfad sind egal"));
        ToggleGroup tg = new ToggleGroup();
        tg.getToggles().addAll(rbFilePath, rbFileName, rbFileAll);
        switch (ProgConfig.CONFIG_COMPARE_FILE.getValue()) {
            case ProgConst.COMPARE_PATH_NAME:
                rbFilePath.setSelected(true);
                break;
            case ProgConst.COMPARE_NAME:
                rbFileName.setSelected(true);
                break;
            case ProgConst.COMPARE_ALL:
                rbFileAll.setSelected(true);
                break;
        }

        markRadio(rbFilePath, rbFileName, rbFileAll);
        rbFilePath.selectedProperty().addListener((v, o, n) -> {
            markRadio(rbFilePath, rbFileName, rbFileAll);
            ProgConfig.CONFIG_COMPARE_FILE.setValue(ProgConst.COMPARE_PATH_NAME);
            CompareFileListFactory.compareList();
        });
        rbFileName.selectedProperty().addListener((v, o, n) -> {
            markRadio(rbFilePath, rbFileName, rbFileAll);
            ProgConfig.CONFIG_COMPARE_FILE.setValue(ProgConst.COMPARE_NAME);
            CompareFileListFactory.compareList();
        });
        rbFileAll.selectedProperty().addListener((v, o, n) -> {
            markRadio(rbFilePath, rbFileName, rbFileAll);
            ProgConfig.CONFIG_COMPARE_FILE.setValue(ProgConst.COMPARE_ALL);
            CompareFileListFactory.compareList();
        });

        HBox hBoxHash = new HBox(10);
        hBoxHash.getStyleClass().add("extra-pane");
        hBoxHash.setPadding(new Insets(10));
        hBoxHash.setAlignment(Pos.CENTER);
        hBoxHash.getChildren().addAll(/*new Label("Beide Suchordner einlesen"), btnRead,
                PGuiTools.getHDistance(40),*/
                lblHash, P2GuiTools.getHDistance(20),
                rbFilePath, P2GuiTools.getHDistance(20),
                rbFileName, P2GuiTools.getHDistance(20),
                rbFileAll, P2GuiTools.getHBoxGrower(), btnHelpPathHash);

        //== add all ==
        VBox vBoxAll = new VBox();
        VBox.setVgrow(splitPane, Priority.ALWAYS);
        vBoxAll.getChildren().addAll(splitPane, hBoxHash);
        scrollPane.setContent(vBoxAll);
        scrollPane.setFitToHeight(true);
        scrollPane.setFitToWidth(true);

        progData.pEventHandler.addListener(new P2Listener(Events.COMPARE_OF_FILE_LISTS_FINISHED) {
            public <T extends P2Event> void pingGui(T runEvent) {
                setTglButton();
            }
        });
    }

    private void markRadio(RadioButton r1, RadioButton r2, RadioButton r3) {
        if (r1.isSelected()) {
            r1.setStyle("-fx-underline: true;");
            r2.setStyle("-fx-underline: false;");
            r3.setStyle("-fx-underline: false;");
        } else if (r2.isSelected()) {
            r1.setStyle("-fx-underline: false;");
            r2.setStyle("-fx-underline: true;");
            r3.setStyle("-fx-underline: false;");
        } else {
            r1.setStyle("-fx-underline: false;");
            r2.setStyle("-fx-underline: false;");
            r3.setStyle("-fx-underline: true;");
        }
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
        btnRead.setGraphic(PIconFactory.PICON.BTN_START_ALL.getFontIcon());
        btnRead.setTooltip(new Tooltip("Die beiden Suchordner (oder die Zipdatei/Hashdatei) werden eingelesen"));

        ToggleGroup tg = new ToggleGroup();
        tg.selectedToggleProperty().addListener((obsVal, oldVal, newVal) -> {
            if (newVal == null) {
                oldVal.setSelected(true);
            }
        });
        tg.getToggles().addAll(tglShowAll, tglShowSamePath, tglShowDiff_OR_ONLY, tglShowDiff_IN_BOTH, tglShowOnlyLeft, tglShowOnlyRight);

        tglShowAll.setGraphic(PIconFactory.PICON.BTN_FILTER_ALL.getFontIcon());
        tglShowSamePath.setGraphic(PIconFactory.PICON.BTN_FILTER_EQUAL.getFontIcon());
        tglShowDiff_OR_ONLY.setGraphic(PIconFactory.PICON.BTN_FILTER_DIFF_ONLY.getFontIcon());
        tglShowDiff_IN_BOTH.setGraphic(PIconFactory.PICON.BTN_FILTER_DIFF_ALL.getFontIcon());
        tglShowOnlyLeft.setGraphic(PIconFactory.PICON.BTN_FILTER_DIFF_LEFT.getFontIcon());
        tglShowOnlyRight.setGraphic(PIconFactory.PICON.BTN_FILTER_DIFF_RIGHT.getFontIcon());

        tglShowAll.getStyleClass().add("btnFilter");
        tglShowSamePath.getStyleClass().add("btnFilter");
        tglShowDiff_OR_ONLY.getStyleClass().add("btnFilter");
        tglShowDiff_IN_BOTH.getStyleClass().add("btnFilter");
        tglShowOnlyLeft.getStyleClass().add("btnFilter");
        tglShowOnlyRight.getStyleClass().add("btnFilter");

        tglShowAll.setTooltip(new Tooltip("Alle Dateien anzeigen."));
        tglShowSamePath.setTooltip(new Tooltip("Gleiche Dateien (gleicher Pfad/Name/Hash oder nur gleicher Hash), anzeigen."));
        tglShowDiff_OR_ONLY.setTooltip(new Tooltip("Dateien suchen, die sich unterscheiden oder nur in einer Liste enthalten sind."));
        tglShowDiff_IN_BOTH.setTooltip(new Tooltip("Dateien suchen, die in beiden Listen enthalten sind, sich aber unterscheiden."));
        tglShowOnlyLeft.setTooltip(new Tooltip("Dateien suchen, die nur in Liste 1 enthalten sind."));
        tglShowOnlyRight.setTooltip(new Tooltip("Dateien suchen, die nur in Liste 2 enthalten sind."));
    }

    private void addListener() {
        btnRead.setOnAction(e -> {
            if (progData.worker.createHashIsRunning()) {
                return;
            }
            guiDirPane1.read();
            CreateHash createHash = new CreateHash();//nur so klappts mit der Maskerpane
            new Thread(createHash).start();
        });


        tglShowAll.setOnAction(e -> setTglButton());
        tglShowSamePath.setOnAction(e -> setTglButton());
        tglShowDiff_OR_ONLY.setOnAction(e -> setTglButton());
        tglShowDiff_IN_BOTH.setOnAction(e -> setTglButton());
        tglShowOnlyLeft.setOnAction(e -> setTglButton());
        tglShowOnlyRight.setOnAction(e -> setTglButton());
        tglShowAll.setSelected(true);
        setTglButton();
    }

    private class CreateHash implements Runnable {
        public synchronized void run() {
            // Dateien auflisten
            try {
                while (progData.worker.createHashIsRunning()) {
                    try {
                        // sleep(250);
                        this.wait(250);
                    } catch (Exception ex) {
                    }
                }
                Platform.runLater(() -> {
                    guiDirPane2.read();
                });
            } catch (Exception ex) {
                P2Log.errorLog(102540879, ex.getMessage());
            }
        }
    }

    private void setTglButton() {
//        clear();
        if (tglShowAll.isSelected()) {
            fileDataFilter1.setFilter_types(FileDataFilter.FILTER_TYPES.ALL);
            fileDataFilter2.setFilter_types(FileDataFilter.FILTER_TYPES.ALL);
            progData.fileDataList_1.setPred(fileDataFilter1);
            progData.fileDataList_2.setPred(fileDataFilter2);

        } else if (tglShowSamePath.isSelected()) {
            fileDataFilter1.setFilter_types(FileDataFilter.FILTER_TYPES.SAME_NAME);
            fileDataFilter2.setFilter_types(FileDataFilter.FILTER_TYPES.SAME_NAME);
            progData.fileDataList_1.setPred(fileDataFilter1);
            progData.fileDataList_2.setPred(fileDataFilter2);

        } else if (tglShowDiff_OR_ONLY.isSelected()) {
            fileDataFilter1.setFilter_types(FileDataFilter.FILTER_TYPES.DIFF_OR_ONLY);
            fileDataFilter2.setFilter_types(FileDataFilter.FILTER_TYPES.DIFF_OR_ONLY);
            progData.fileDataList_1.setPred(fileDataFilter1);
            progData.fileDataList_2.setPred(fileDataFilter2);

        } else if (tglShowDiff_IN_BOTH.isSelected()) {
            fileDataFilter1.setFilter_types(FileDataFilter.FILTER_TYPES.DIFF_IN_BOTHE);
            fileDataFilter2.setFilter_types(FileDataFilter.FILTER_TYPES.DIFF_IN_BOTHE);
            progData.fileDataList_1.setPred(fileDataFilter1);
            progData.fileDataList_2.setPred(fileDataFilter2);

        } else if (tglShowOnlyLeft.isSelected()) {
            fileDataFilter1.setFilter_types(FileDataFilter.FILTER_TYPES.ONLY);
            progData.fileDataList_1.setPred(fileDataFilter1);
            progData.fileDataList_2.setPred(false);

        } else if (tglShowOnlyRight.isSelected()) {
            progData.fileDataList_1.setPred(false);
            fileDataFilter2.setFilter_types(FileDataFilter.FILTER_TYPES.ONLY);
            progData.fileDataList_2.setPred(fileDataFilter2);
        }
    }

//    private void clear() {
//        tglShowAll.getStyleClass().removeAll("btnFilterSel");
//        tglShowSamePath.getStyleClass().removeAll("btnFilterSel");
//        tglShowDiff_OR_ONLY.getStyleClass().removeAll("btnFilterSel");
//        tglShowDiff_IN_BOTH.getStyleClass().removeAll("btnFilterSel");
//        tglShowOnlyLeft.getStyleClass().removeAll("btnFilterSel");
//        tglShowOnlyRight.getStyleClass().removeAll("btnFilterSel");
//
//        if (tglShowAll.isSelected()) {
//            tglShowAll.getStyleClass().add("btnFilterSel");
//        } else if (tglShowSamePath.isSelected()) {
//            tglShowSamePath.getStyleClass().add("btnFilterSel");
//        } else if (tglShowDiff_OR_ONLY.isSelected()) {
//            tglShowDiff_OR_ONLY.getStyleClass().add("btnFilterSel");
//        } else if (tglShowDiff_IN_BOTH.isSelected()) {
//            tglShowDiff_IN_BOTH.getStyleClass().add("btnFilterSel");
//        } else if (tglShowOnlyLeft.isSelected()) {
//            tglShowOnlyLeft.getStyleClass().add("btnFilterSel");
//        } else if (tglShowOnlyRight.isSelected()) {
//            tglShowOnlyRight.getStyleClass().add("btnFilterSel");
//        }
//    }
}
