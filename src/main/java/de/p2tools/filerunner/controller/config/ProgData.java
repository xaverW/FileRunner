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


package de.p2tools.filerunner.controller.config;

import de.p2tools.filerunner.FileRunnerController;
import de.p2tools.filerunner.controller.data.filedata.FileDataList;
import de.p2tools.filerunner.controller.worker.Worker;
import de.p2tools.filerunner.gui.GuiDirRunner;
import de.p2tools.filerunner.gui.GuiFileRunner;
import de.p2tools.p2lib.p2event.P2EventHandler;
import javafx.stage.Stage;

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
    //    public PMaskerPane maskerPane = null;
    public FileRunnerController fileRunnerController = null;

    public GuiDirRunner guiDirRunner = null; // DirTab
    public GuiFileRunner guiFileRunner = null; // FileTab

    public P2EventHandler pEventHandler;

    private ProgData() {
        pEventHandler = new P2EventHandler();
        fileDataList_1 = new FileDataList();
        fileDataList_2 = new FileDataList();
        worker = new Worker(this);
    }


    public synchronized static final ProgData getInstance(String dir) {
        configDir = dir;
        return getInstance();
    }

    public synchronized static final ProgData getInstance() {
        return instance == null ? instance = new ProgData() : instance;
    }
}
