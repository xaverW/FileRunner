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
import de.p2tools.filerunner.controller.config.ProgData;
import de.p2tools.p2lib.p2event.P2Event;
import de.p2tools.p2lib.p2event.P2Listener;
import de.p2tools.p2lib.tools.log.P2Log;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;

import java.text.NumberFormat;
import java.util.Locale;

public class StatusBarController extends AnchorPane {

    StackPane stackPane = new StackPane();
    Label lblLeft = new Label();
    Label lblRight = new Label();
    Label lblLeftCount = new Label();
    Label lblRightCount = new Label();
    AnchorPane workerPane = new AnchorPane();
    AnchorPane textPane = new AnchorPane();

    public enum StatusbarIndex {
        NONE, DIR_RUNNER, FILE_RUNNER
    }

    private StatusbarIndex statusbarIndex = StatusbarIndex.NONE;
    private boolean running = false;
    private final ProgData progData;

    public StatusBarController(ProgData progData) {
        this.progData = progData;

        getChildren().addAll(stackPane);
        AnchorPane.setLeftAnchor(stackPane, 0.0);
        AnchorPane.setBottomAnchor(stackPane, 0.0);
        AnchorPane.setRightAnchor(stackPane, 0.0);
        AnchorPane.setTopAnchor(stackPane, 0.0);

        HBox hBox = getHbox();
        lblLeft.setMaxWidth(Double.MAX_VALUE);
        HBox.setHgrow(lblLeft, Priority.ALWAYS);
        hBox.getChildren().addAll(lblLeftCount, lblLeft, lblRight, lblRightCount);
        lblLeftCount.getStyleClass().add("lblSize");
        lblRightCount.getStyleClass().add("lblSize");

        textPane.getChildren().add(hBox);
        textPane.setStyle("-fx-background-color: -fx-background ;");
        workerPane.setStyle("-fx-background-color: -fx-background ;");
        make();
    }

    private HBox getHbox() {
        HBox hBox = new HBox();
        hBox.setPadding(new Insets(2, 5, 2, 5));
        hBox.setSpacing(10);
        hBox.setAlignment(Pos.CENTER_RIGHT);
        AnchorPane.setLeftAnchor(hBox, 0.0);
        AnchorPane.setBottomAnchor(hBox, 0.0);
        AnchorPane.setRightAnchor(hBox, 0.0);
        AnchorPane.setTopAnchor(hBox, 0.0);
        return hBox;
    }


    private void make() {
        stackPane.getChildren().addAll(textPane, workerPane);
        textPane.toFront();

        progData.pEventHandler.addListener(new P2Listener(Events.GENERATE_COMPARE_FILE_LIST) {
            public void pingGui(P2Event event) {
                if (event.getMax() == 0) {
                    if (!progData.worker.createHashIsRunning()) {
                        //sonst läuft noch was
                        running = false;
                    }
                } else {
                    running = true;
                }
                setStatusbarIndex(statusbarIndex);
            }
        });
        progData.pEventHandler.addListener(new P2Listener(Events.EVENT_TIMER_SECOND) {
            public void pingGui(P2Event PEvent) {
                try {
                    if (!running) {
                        setStatusbarIndex(statusbarIndex);
                    }
                } catch (final Exception ex) {
                    P2Log.errorLog(936251087, ex);
                }
            }
        });
    }

    public void setStatusbarIndex(StatusbarIndex statusbarIndex) {
        this.statusbarIndex = statusbarIndex;
        if (running) {
            workerPane.toFront();
            return;
        }

        textPane.toFront();
        switch (statusbarIndex) {
            case DIR_RUNNER:
                setFileRunner();
                break;
            case FILE_RUNNER:
                setTextNone();
                break;
            case NONE:
            default:
                setTextNone();
        }
    }


    private void setTextNone() {
        lblLeft.setText("");
        lblRight.setText("");
        lblRightCount.setText("");
        lblLeftCount.setText("");
    }

    private void setFileRunner() {
        int tbl1Size = progData.fileDataList_1.getFilteredFileData().size();
        int tbl2Size = progData.fileDataList_2.getFilteredFileData().size();

        String src1 = progData.fileDataList_1.getSourceDir();
        String src2 = progData.fileDataList_2.getSourceDir();

        NumberFormat numberFormat = NumberFormat.getNumberInstance(Locale.GERMANY);
        final String anzListeStr1 = numberFormat.format(tbl1Size);
        final String anzListeStr2 = numberFormat.format(tbl2Size);

        if (tbl1Size <= 0) {
            lblLeftCount.setText("[ ]");
            lblLeft.setText(src1);
        } else {
            lblLeftCount.setText("[" + anzListeStr1 + "]");
            lblLeft.setText(src1);
        }

        if (tbl2Size <= 0) {
            lblRightCount.setText("[ ]");
            lblRight.setText(src2);
        } else {
            lblRightCount.setText("[" + anzListeStr2 + "]");
            lblRight.setText(src2);
        }
    }
}
