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


package de.p2tools.fileRunner.controller.config;

import de.p2tools.fileRunner.FileRunnerController;
import de.p2tools.fileRunner.controller.data.fileData.FileDataList;
import de.p2tools.fileRunner.controller.worker.Worker;
import de.p2tools.fileRunner.gui.GuiDirRunner;
import de.p2tools.fileRunner.gui.GuiFileRunner;
import de.p2tools.p2Lib.tools.events.PEvent;
import de.p2tools.p2Lib.tools.events.PEventHandler;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.stage.Stage;
import javafx.util.Duration;

public class ProgData {

    private static ProgData instance;

    // flags
    public static boolean debug = false; // Debugmodus
    public static boolean duration = false; // Duration ausgeben

    // Infos
    public static String configDir; // Verzeichnis zum Speichern der Programmeinstellungen

    public FileDataList fileDataList_1;
    public FileDataList fileDataList_2;

    // zentrale Klassen
    public Worker worker;

    // Gui
    public Stage primaryStage = null;
    public FileRunnerController fileRunnerController = null;

    public GuiDirRunner guiDirRunner = null; // DirTab
    public GuiFileRunner guiFileRunner = null; // FileTab

    public PEventHandler pEventHandler;

    private ProgData() {
        pEventHandler = new PEventHandler();
        fileDataList_1 = new FileDataList();
        fileDataList_2 = new FileDataList();
        worker = new Worker(this);

        Timeline timeline = new Timeline(new KeyFrame(
                Duration.millis(1000), ae -> {
            pEventHandler.notifyListener(new PEvent(Events.TIMER));
        }));

        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.setDelay(Duration.seconds(5));
        timeline.play();
    }


    public synchronized static final ProgData getInstance(String dir) {
        configDir = dir;
        return getInstance();
    }

    public synchronized static final ProgData getInstance() {
        return instance == null ? instance = new ProgData() : instance;
    }
}
