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
import de.p2tools.p2lib.P2LibConst;
import de.p2tools.p2lib.guitools.P2Button;
import de.p2tools.p2lib.guitools.P2GuiTools;
import de.p2tools.p2lib.ikonli.P2IconFactory;
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
    private final Button btnRead = new Button("");
    private final Button btnSpeed = new Button("");

    private final ToggleButton tglShow_All = new ToggleButton("");
    private final ToggleButton tglShow_Same = new ToggleButton("");
    private final ToggleButton tglShow_Diff = new ToggleButton("");
    private final ToggleButton tglShow_DiffOrOnly = new ToggleButton("");
    private final ToggleButton tglShow_OnlyLeft = new ToggleButton("");
    private final ToggleButton tglShow_OnlyRight = new ToggleButton("");

    private final ProgData progData;
    private final FileDataFilter fileDataFilter1 = new FileDataFilter();
    private final FileDataFilter fileDataFilter2 = new FileDataFilter();
    private final GuiDirPane guiDirPane1;
    private final GuiDirPane guiDirPane2;
    private double orgX, orgDiv0, orgDiv1, orgSize;
    private final VBox vBoxAll = new VBox(P2LibConst.PADDING_VBOX);

    public GuiDirRunner() {
        progData = ProgData.getInstance();

        AnchorPane.setLeftAnchor(vBoxAll, 0.0);
        AnchorPane.setBottomAnchor(vBoxAll, 0.0);
        AnchorPane.setRightAnchor(vBoxAll, 0.0);
        AnchorPane.setTopAnchor(vBoxAll, 0.0);
        getChildren().addAll(vBoxAll);

        guiDirPane1 = new GuiDirPane(progData, fileDataFilter1, true);
        guiDirPane2 = new GuiDirPane(progData, fileDataFilter2, false);

        initCont();
        setSpeed();
        initSplit();
        addToggleGroup();
        addListener();
    }

    public void isShown() {
    }

    public void resetFilter() {
        tglShow_All.setSelected(true);
        fileDataFilter1.setFilter_types(FileDataFilter.FILTER_TYPES.ALL);
        fileDataFilter2.setFilter_types(FileDataFilter.FILTER_TYPES.ALL);
        progData.fileDataList_1.setPred(fileDataFilter1);
        progData.fileDataList_2.setPred(fileDataFilter2);
    }

    public void saveTable() {
        guiDirPane1.saveTable();
        guiDirPane2.saveTable();
    }

    private void setSpeed() {
        btnSpeed.setTooltip(new Tooltip("Damit kann der Speed-Mode ein-/ausgeschaltet werden"));
        if (ProgConfig.CONFIG_RUN_SPEED.get()) {
            btnSpeed.setGraphic(PIconFactory.PICON.BTN_SPEED.getFontIcon());
        } else {
            btnSpeed.setGraphic(PIconFactory.PICON.BTN_SPEED_LOW.getFontIcon());
        }
        ProgConfig.CONFIG_RUN_SPEED.addListener((u, o, n) -> {
            if (ProgConfig.CONFIG_RUN_SPEED.get()) {
                btnSpeed.setGraphic(PIconFactory.PICON.BTN_SPEED.getFontIcon());
            } else {
                btnSpeed.setGraphic(PIconFactory.PICON.BTN_SPEED_LOW.getFontIcon());
            }
        });
        btnSpeed.setOnAction(a -> ProgConfig.CONFIG_RUN_SPEED.set(!ProgConfig.CONFIG_RUN_SPEED.get()));
    }

    private void initCont() {
        //== Oben ==
        vBoxBtn.setStyle(" -fx-border-width: 1; -fx-border-color: -style-tab-border-color-1;");
        vBoxBtn.setAlignment(Pos.TOP_CENTER);
        vBoxBtn.setPadding(new Insets(10));
        vBoxBtn.setMaxWidth(Region.USE_PREF_SIZE);
        vBoxBtn.getChildren().addAll(P2GuiTools.getHDistance(50), tglShow_All,
                P2GuiTools.getHDistance(10), tglShow_Same,
                P2GuiTools.getHDistance(10), tglShow_Diff, tglShow_DiffOrOnly,
                P2GuiTools.getHDistance(10), tglShow_OnlyLeft, tglShow_OnlyRight);

        Button btnHelp = P2Button.helpButton(progData.primaryStage, "Vergleichen", HelpText.COMPARE_BUTTON);

        VBox vBox = new VBox(10);
        VBox.setVgrow(vBox, Priority.ALWAYS);
        vBox.setMaxHeight(Double.MAX_VALUE);
        vBox.setAlignment(Pos.BOTTOM_CENTER);
        vBox.getChildren().addAll(btnSpeed, btnRead, btnHelp);
        vBoxBtn.getChildren().add(vBox);

        SplitPane.setResizableWithParent(vBoxBtn, Boolean.FALSE);
        splitPane.getItems().addAll(guiDirPane1, vBoxBtn, guiDirPane2);

        //==================================
        //== Unten ==
        Label lblHash = new Label("Gleiche Dateien haben gleichen");
        Button btnHelpPathHash = P2Button.helpButton(progData.primaryStage, "", HelpText.READ_DIR_HASH);

        RadioButton rbFilePath = new RadioButton("Pfad/Dateinamen und Hash");
        RadioButton rbFileName = new RadioButton("Dateinamen und Hash");
        RadioButton rbFileHash = new RadioButton("Hash");
        rbFilePath.setTooltip(new Tooltip("Dateien sind gleich, wenn der \"Pfad/Name/Hash\" gleich ist"));
        rbFileName.setTooltip(new Tooltip("Dateien sind gleich, wenn der \"Name/Hash\" gleich ist"));
        rbFileHash.setTooltip(new Tooltip("Dateien sind gleich, wenn der \"Hash\" gleich ist, Name und Pfad sind egal"));
        ToggleGroup tg = new ToggleGroup();
        tg.getToggles().addAll(rbFilePath, rbFileName, rbFileHash);
        switch (ProgConfig.CONFIG_COMPARE_FILE.getValue()) {
            case ProgConst.COMPARE_PATH_NAME:
                rbFilePath.setSelected(true);
                break;
            case ProgConst.COMPARE_NAME:
                rbFileName.setSelected(true);
                break;
            case ProgConst.COMPARE_HASH:
                rbFileHash.setSelected(true);
                break;
        }

        markRadio(rbFilePath, rbFileName, rbFileHash);
        rbFilePath.selectedProperty().addListener((v, o, n) -> {
            if (n) {
                markRadio(rbFilePath, rbFileName, rbFileHash);
                ProgConfig.CONFIG_COMPARE_FILE.setValue(ProgConst.COMPARE_PATH_NAME);
                CompareFileListFactory.compareList();
            }
        });
        rbFileName.selectedProperty().addListener((v, o, n) -> {
            if (n) {
                markRadio(rbFilePath, rbFileName, rbFileHash);
                ProgConfig.CONFIG_COMPARE_FILE.setValue(ProgConst.COMPARE_NAME);
                CompareFileListFactory.compareList();
            }
        });
        rbFileHash.selectedProperty().addListener((v, o, n) -> {
            if (n) {
                markRadio(rbFilePath, rbFileName, rbFileHash);
                ProgConfig.CONFIG_COMPARE_FILE.setValue(ProgConst.COMPARE_HASH);
                CompareFileListFactory.compareList();
            }
        });

        HBox hBoxHash = new HBox(10);
        hBoxHash.getStyleClass().add("extra-pane");
        hBoxHash.setPadding(new Insets(10));
        hBoxHash.setAlignment(Pos.CENTER);
        hBoxHash.getChildren().addAll(
                lblHash, P2GuiTools.getHDistance(20),
                rbFilePath, P2GuiTools.getHDistance(20),
                rbFileName, P2GuiTools.getHDistance(20),
                rbFileHash, P2GuiTools.getHBoxGrower(), btnHelpPathHash);

        //== add all ==
        vBoxAll.getChildren().addAll(splitPane, hBoxHash);
        VBox.setVgrow(splitPane, Priority.ALWAYS);
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
        btnRead.setGraphic(P2IconFactory.P2ICON.P2_BTN_DOWNLOAD_ALL.getFontIcon());
        btnRead.setTooltip(new Tooltip("Die beiden Suchordner (oder die Zipdatei/Hashdatei) werden eingelesen"));

        ToggleGroup tg = new ToggleGroup();
        tg.selectedToggleProperty().addListener((obsVal, oldVal, newVal) -> {
            if (newVal == null) {
                oldVal.setSelected(true);
            }
        });
        tg.getToggles().addAll(tglShow_All, tglShow_Same, tglShow_Diff,
                tglShow_DiffOrOnly, tglShow_OnlyLeft, tglShow_OnlyRight);

        tglShow_All.setGraphic(PIconFactory.PICON.BTN_FILTER_ALL.getFontIcon());
        tglShow_Same.setGraphic(PIconFactory.PICON.BTN_FILTER_EQUAL.getFontIcon());
        tglShow_DiffOrOnly.setGraphic(PIconFactory.PICON.BTN_FILTER_DIFF_ONLY.getFontIcon());
        tglShow_Diff.setGraphic(PIconFactory.PICON.BTN_FILTER_NOT_EQUAL.getFontIcon());
        tglShow_OnlyLeft.setGraphic(PIconFactory.PICON.BTN_FILTER_DIFF_LEFT.getFontIcon());
        tglShow_OnlyRight.setGraphic(PIconFactory.PICON.BTN_FILTER_DIFF_RIGHT.getFontIcon());

        tglShow_All.getStyleClass().add("btnFilter");
        tglShow_Same.getStyleClass().add("btnFilter");
        tglShow_Diff.getStyleClass().add("btnFilter");
        tglShow_DiffOrOnly.getStyleClass().add("btnFilter");
        tglShow_OnlyLeft.getStyleClass().add("btnFilter");
        tglShow_OnlyRight.getStyleClass().add("btnFilter");

        tglShow_All.setTooltip(new Tooltip("Alle Dateien anzeigen."));
        tglShow_Same.setTooltip(new Tooltip("Gleiche Dateien (gleicher Pfad/Name/Hash oder nur gleicher Hash), anzeigen."));
        tglShow_Diff.setTooltip(new Tooltip("Dateien suchen, die in beiden Listen enthalten sind, sich aber unterscheiden."));
        tglShow_DiffOrOnly.setTooltip(new Tooltip("Dateien suchen, die sich unterscheiden oder nur in einer Liste enthalten sind."));
        tglShow_OnlyLeft.setTooltip(new Tooltip("Dateien suchen, die nur in Liste 1 enthalten sind."));
        tglShow_OnlyRight.setTooltip(new Tooltip("Dateien suchen, die nur in Liste 2 enthalten sind."));
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


        tglShow_All.setOnAction(e -> setTglButton());
        tglShow_Same.setOnAction(e -> setTglButton());
        tglShow_DiffOrOnly.setOnAction(e -> setTglButton());
        tglShow_Diff.setOnAction(e -> setTglButton());
        tglShow_OnlyLeft.setOnAction(e -> setTglButton());
        tglShow_OnlyRight.setOnAction(e -> setTglButton());
        tglShow_All.setSelected(true);
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
        if (tglShow_All.isSelected()) {
            fileDataFilter1.setFilter_types(FileDataFilter.FILTER_TYPES.ALL);
            fileDataFilter2.setFilter_types(FileDataFilter.FILTER_TYPES.ALL);
            progData.fileDataList_1.setPred(fileDataFilter1);
            progData.fileDataList_2.setPred(fileDataFilter2);

        } else if (tglShow_Same.isSelected()) {
            fileDataFilter1.setFilter_types(FileDataFilter.FILTER_TYPES.SAME_NAME);
            fileDataFilter2.setFilter_types(FileDataFilter.FILTER_TYPES.SAME_NAME);
            progData.fileDataList_1.setPred(fileDataFilter1);
            progData.fileDataList_2.setPred(fileDataFilter2);

        } else if (tglShow_DiffOrOnly.isSelected()) {
            fileDataFilter1.setFilter_types(FileDataFilter.FILTER_TYPES.DIFF_OR_ONLY);
            fileDataFilter2.setFilter_types(FileDataFilter.FILTER_TYPES.DIFF_OR_ONLY);
            progData.fileDataList_1.setPred(fileDataFilter1);
            progData.fileDataList_2.setPred(fileDataFilter2);

        } else if (tglShow_Diff.isSelected()) {
            fileDataFilter1.setFilter_types(FileDataFilter.FILTER_TYPES.DIFF_IN_BOTHE);
            fileDataFilter2.setFilter_types(FileDataFilter.FILTER_TYPES.DIFF_IN_BOTHE);
            progData.fileDataList_1.setPred(fileDataFilter1);
            progData.fileDataList_2.setPred(fileDataFilter2);

        } else if (tglShow_OnlyLeft.isSelected()) {
            fileDataFilter1.setFilter_types(FileDataFilter.FILTER_TYPES.ONLY);
            progData.fileDataList_1.setPred(fileDataFilter1);
            progData.fileDataList_2.setPred(false);

        } else if (tglShow_OnlyRight.isSelected()) {
            progData.fileDataList_1.setPred(false);
            fileDataFilter2.setFilter_types(FileDataFilter.FILTER_TYPES.ONLY);
            progData.fileDataList_2.setPred(fileDataFilter2);
        }
    }
}
