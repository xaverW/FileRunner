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

package de.p2tools.fileRunner.controller;

import de.p2tools.fileRunner.controller.config.ProgConfig;
import de.p2tools.fileRunner.controller.config.ProgData;
import de.p2tools.p2Lib.guiTools.PGuiSize;
import de.p2tools.p2Lib.tools.log.LogMessage;
import javafx.application.Platform;

public class ProgQuittFactory {

    private ProgQuittFactory() {
    }

    /**
     * Quit the application
     */
    public static void quit() {
        //Tabelleneinstellungen merken
        ProgData.getInstance().guiDirRunner.saveTable();
        //Hauptfenster
        PGuiSize.getSizeStage(ProgConfig.SYSTEM_GUI_SIZE, ProgData.getInstance().primaryStage);

        new ProgSaveFactory().save();
        LogMessage.endMsg();

        //dann jetzt beenden -> Tschüss
        Platform.runLater(() -> {
            Platform.exit();
            System.exit(0);
        });
    }
}
