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
package de.p2tools.filerunner;

import de.p2tools.filerunner.controller.ProgQuittFactory;
import de.p2tools.filerunner.controller.ProgStartAfterGui;
import de.p2tools.filerunner.controller.ProgStartBeforeGui;
import de.p2tools.filerunner.controller.config.ProgConfig;
import de.p2tools.filerunner.controller.config.ProgData;
import de.p2tools.p2lib.css.P2CssFactory;
import de.p2tools.p2lib.guitools.P2GuiSize;
import de.p2tools.p2lib.tools.duration.P2Duration;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class FileRunner extends Application {

    private Stage primaryStage;
    private static final String LOG_TEXT_PROGRAMMSTART = "***Programmstart***";

    protected ProgData progData;
    private Scene scene = null;

    @Override
    public void init() throws Exception {
    }

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;

        P2Duration.counterStart(LOG_TEXT_PROGRAMMSTART);
        progData = ProgData.getInstance();
        progData.primaryStage = primaryStage;

        ProgStartBeforeGui.workBeforeGui();
        initRootLayout();
        ProgStartAfterGui.workAfterGui();
        P2Duration.onlyPing("Gui steht!");
        P2Duration.counterStop(LOG_TEXT_PROGRAMMSTART);
    }

    private void initRootLayout() {
        try {
            progData.fileRunnerController = new FileRunnerController();
            scene = new Scene(progData.fileRunnerController,
                    P2GuiSize.getSceneSize(ProgConfig.SYSTEM_GUI_SIZE, true),
                    P2GuiSize.getSceneSize(ProgConfig.SYSTEM_GUI_SIZE, false));
            primaryStage.setScene(scene);
            primaryStage.setOnCloseRequest(e -> {
                e.consume();
                ProgQuittFactory.quit();
            });

            primaryStage.setOnShowing(e -> P2GuiSize.setSizePos(ProgConfig.SYSTEM_GUI_SIZE, primaryStage, null));
            primaryStage.setOnShown(e -> P2GuiSize.setSizePos(ProgConfig.SYSTEM_GUI_SIZE, primaryStage, null));
            P2CssFactory.addP2CssToScene(scene); // und jetzt noch CSS einstellen
            primaryStage.show();
        } catch (final Exception e) {
            e.printStackTrace();
        }
    }
}
