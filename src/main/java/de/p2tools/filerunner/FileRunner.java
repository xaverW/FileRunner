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
import de.p2tools.filerunner.controller.ProgStartFactory;
import de.p2tools.filerunner.controller.config.ProgColorList;
import de.p2tools.filerunner.controller.config.ProgConfig;
import de.p2tools.filerunner.controller.config.ProgConst;
import de.p2tools.filerunner.controller.config.ProgData;
import de.p2tools.filerunner.icon.ProgIconsFileRunner;
import de.p2tools.p2lib.P2LibInit;
import de.p2tools.p2lib.css.P2CssFactory;
import de.p2tools.p2lib.guitools.P2GuiSize;
import de.p2tools.p2lib.tools.duration.P2Duration;
import de.p2tools.p2lib.tools.log.P2Log;
import javafx.application.Application;
import javafx.beans.property.SimpleObjectProperty;
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
        boolean firstProgramStart;

        P2Duration.counterStart(LOG_TEXT_PROGRAMMSTART);
        progData = ProgData.getInstance();
        progData.primaryStage = primaryStage;

        firstProgramStart = ProgStartFactory.workBeforeGui(progData);
        initP2lib();
        initRootLayout();
        ProgStartFactory.workAfterGui(progData, firstProgramStart);
        P2Duration.onlyPing("Gui steht!");
        P2Duration.counterStop(LOG_TEXT_PROGRAMMSTART);
    }

    private void initP2lib() {
        ProgIconsFileRunner.initIcons();
        P2LibInit.initLib(primaryStage, ProgConst.PROGRAM_NAME, "",
                ProgConfig.SYSTEM_THEME_CHANGED,
                ProgConfig.SYSTEM_DARK_THEME,
                ProgConfig.SYSTEM_GUI_THEME_1,
                ProgConfig.SYSTEM_ICON_COLOR,
                ProgConfig.SYSTEM_CSS_ADDER,

                new String[]{
                        "de/p2tools/filerunner/css/mtfx.css",
                        "de/p2tools/filerunner/css/pFuncBtn.css",
                        "de/p2tools/filerunner/css/pFuncMenu.css",
                        "de/p2tools/filerunner/css/pFuncTitleBar.css",
                        "de/p2tools/filerunner/css/pFuncTable.css",
                        "de/p2tools/filerunner/css/pFuncToolBar.css",
                        "de/p2tools/filerunner/css/pFuncTips.css",
                        "de/p2tools/filerunner/css/pFuncStartDialog.css"
                },

                new String[]{
                        "de/p2tools/filerunner/css/mtfx-dark.css"
                },

                new SimpleObjectProperty<>(P2CssFactory.CSS.CSS_1),
                ProgConfig.SYSTEM_FONT_SIZE,
                null,
                "", "",
                ProgData.debug, ProgData.duration);
    }

    private void initRootLayout() {
        try {
            progData.fileRunnerController = new FileRunnerController();
            scene = new Scene(progData.fileRunnerController,
                    P2GuiSize.getSceneSize(ProgConfig.SYSTEM_GUI_SIZE, true),
                    P2GuiSize.getSceneSize(ProgConfig.SYSTEM_GUI_SIZE, false));

            ProgColorList.setColorTheme();
            primaryStage.setScene(scene);
            primaryStage.setOnCloseRequest(e -> {
                e.consume();
                ProgQuittFactory.quit();
            });

            P2GuiSize.setPos(ProgConfig.SYSTEM_GUI_SIZE, primaryStage);
            P2Log.sysLog("Programmgröße: " + ProgConfig.SYSTEM_GUI_SIZE);
            scene.heightProperty().addListener((v, o, n) -> P2GuiSize.getSize(ProgConfig.SYSTEM_GUI_SIZE, primaryStage));
            scene.widthProperty().addListener((v, o, n) -> P2GuiSize.getSize(ProgConfig.SYSTEM_GUI_SIZE, primaryStage));
            primaryStage.xProperty().addListener((v, o, n) -> P2GuiSize.getSize(ProgConfig.SYSTEM_GUI_SIZE, primaryStage));
            primaryStage.yProperty().addListener((v, o, n) -> P2GuiSize.getSize(ProgConfig.SYSTEM_GUI_SIZE, primaryStage));

            P2CssFactory.addP2CssToScene(scene); // und jetzt noch CSS einstellen
            ProgConfig.SYSTEM_DARK_THEME.addListener((u, o, n) -> {
                ProgColorList.setColorTheme();
            });

            primaryStage.show();
        } catch (final Exception e) {
            e.printStackTrace();
        }
    }
}
