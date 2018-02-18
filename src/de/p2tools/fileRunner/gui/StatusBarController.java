/*
 * MTPlayer Copyright (C) 2017 W. Xaver W.Xaver[at]googlemail.com
 * https://sourceforge.net/projects/mtplayer/
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

import de.p2tools.fileRunner.controller.RunEvent;
import de.p2tools.fileRunner.controller.RunListener;
import de.p2tools.fileRunner.controller.config.ProgData;
import de.p2tools.fileRunner.controller.data.Icons;
import de.p2tools.fileRunner.gui.tools.Listener;
import de.p2tools.p2Lib.tools.Log;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;

public class StatusBarController extends AnchorPane {

    StackPane stackPane = new StackPane();

    // loadPane
    Label lblProgress = new Label();
    ProgressBar progressBar = new ProgressBar();
    Button btnStop = new Button("");

    Label lblLeft = new Label();
    Label lblRight = new Label();

    AnchorPane workerPane = new AnchorPane();
    AnchorPane textPane = new AnchorPane();


    public enum StatusbarIndex {
        NONE, Start, Thumb, Mosaik, Wallpaper
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
        hBox.getChildren().addAll(lblLeft, lblRight);
        textPane.getChildren().add(hBox);
        textPane.setStyle("-fx-background-color: -fx-background ;");

        hBox = getHbox();
        btnStop.setGraphic(new Icons().ICON_BUTTON_STOP);
        hBox.getChildren().addAll(lblProgress, progressBar, btnStop);
        progressBar.setPrefWidth(200);
        workerPane.getChildren().add(hBox);
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

        btnStop.setOnAction(e -> progData.worker.setStop());

        progData.worker.addAdListener(new RunListener() {
            @Override
            public void ping(RunEvent runEvent) {
                if (runEvent.nixLos()) {
                    running = false;
                } else {
                    running = true;
                }
                updateProgressBar(runEvent);
                setStatusbar();
            }
        });

        Listener.addListener(new Listener(Listener.EREIGNIS_TIMER, StatusBarController.class.getSimpleName()) {
            @Override
            public void ping() {
                try {
                    if (!running) {
                        setStatusbar();
                    }
                } catch (final Exception ex) {
                    Log.errorLog(936251087, ex);
                }
            }
        });
    }

    private void updateProgressBar(RunEvent event) {
        int max = event.getMax();
        int progress = event.getProgress();
        double prog = 1.0;
        if (max > 0) {
            prog = 1.0 * progress / max;
        }

        progressBar.setProgress(prog);
        lblProgress.setText(event.getText());
    }

    public void setStatusbar() {
        setStatusbarIndex(statusbarIndex);
    }

    public void setStatusbarIndex(StatusbarIndex statusbarIndex) {
        this.statusbarIndex = statusbarIndex;
        if (running) {
            workerPane.toFront();
            return;
        }

        textPane.toFront();
        switch (statusbarIndex) {
            case Start:
                setTextNone();
                break;
            case Thumb:
            case Mosaik:
            case Wallpaper:
                setInfoMosaik();
                break;
            case NONE:
            default:
                setTextNone();
        }
    }


    private void setTextNone() {
        lblLeft.setText("");
        lblRight.setText("");
    }

    private void setInfoMosaik() {
//        if (progData.selectedProjectData == null) {
//            lblLeft.setText("");
//            lblRight.setText("");
//            return;
//        }
//
//        String textLinks = "Miniaturbilder: " + (progData.selectedProjectData.getThumbCollection() != null ?
//                progData.selectedProjectData.getName() : "");
//        lblLeft.setText(textLinks);
//        String strText = progData.selectedProjectData.getThumbCollection() != null ?
//                progData.selectedProjectData.getThumbCollection().getThumbList().size() + " Miniaturbilder" : "";
//        lblRight.setText(strText);
    }

}
