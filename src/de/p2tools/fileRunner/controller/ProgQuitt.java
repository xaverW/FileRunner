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
import de.p2tools.p2Lib.guiTools.GuiSize;
import de.p2tools.p2Lib.tools.log.LogMsg;
import javafx.application.Platform;

public class ProgQuitt {
    final ProgData progData;

    public ProgQuitt() {
        progData = ProgData.getInstance();
    }


    private void writeWindowSizes() {
        // Hauptfenster
        GuiSize.getSizeScene(ProgConfig.SYSTEM_GUI_SIZE, progData.primaryStage);
    }

    private void writeTabSettings() {
        // Tabelleneinstellungen merken
        progData.guiDirRunner.saveTable();
    }

    /**
     * Quit the application
     *
     * @param showOptionTerminate show options dialog when downloads are running
     */
    public void beenden(boolean showOptionTerminate) {
        if (beenden_()) {

            // dann jetzt beenden -> Thüss
            Platform.runLater(() -> {
                Platform.exit();
                System.exit(0);
            });

        }
    }

    private boolean beenden_() {
        writeTabSettings();
        writeWindowSizes();

        new ProgSave().save();
        LogMsg.endMsg();

        return true;
    }

}
