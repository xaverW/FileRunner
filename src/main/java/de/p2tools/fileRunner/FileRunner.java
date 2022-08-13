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
package de.p2tools.fileRunner;

import de.p2tools.fileRunner.controller.ProgQuittFactory;
import de.p2tools.fileRunner.controller.ProgStartFactory;
import de.p2tools.fileRunner.controller.config.ProgColorList;
import de.p2tools.fileRunner.controller.config.ProgConfig;
import de.p2tools.fileRunner.controller.config.ProgConst;
import de.p2tools.fileRunner.controller.config.ProgData;
import de.p2tools.p2Lib.P2LibConst;
import de.p2tools.p2Lib.P2LibInit;
import de.p2tools.p2Lib.guiTools.PGuiSize;
import de.p2tools.p2Lib.tools.duration.PDuration;
import de.p2tools.p2Lib.tools.log.PLog;
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
        boolean firstProgramStart;

        PDuration.counterStart(LOG_TEXT_PROGRAMMSTART);
        progData = ProgData.getInstance();
        progData.primaryStage = primaryStage;

        initP2lib();
        firstProgramStart = ProgStartFactory.workBeforeGui(progData);
        initRootLayout();
        ProgStartFactory.workAfterGui(progData, firstProgramStart);
        PDuration.onlyPing("Gui steht!");
        PDuration.counterStop(LOG_TEXT_PROGRAMMSTART);
    }

    private void initP2lib() {
        P2LibInit.initLib(primaryStage, ProgConst.PROGRAM_NAME, "", ProgData.debug, ProgData.duration);
        P2LibInit.addCssFile(P2LibConst.CSS_GUI);
        P2LibInit.addCssFile(ProgConst.CSS_FILE);
    }

    private void initRootLayout() {
        try {
            progData.fileRunnerController = new FileRunnerController();
            scene = new Scene(progData.fileRunnerController,
                    PGuiSize.getWidth(ProgConfig.SYSTEM_GUI_SIZE),
                    PGuiSize.getHeight(ProgConfig.SYSTEM_GUI_SIZE));

            ProgConfig.SYSTEM_DARK_THEME.addListener((u, o, n) -> {
                ProgColorList.setColorTheme();
                addThemeCss();
            });
            ProgColorList.setColorTheme();
            addThemeCss();

            primaryStage.setScene(scene);
            primaryStage.setOnCloseRequest(e -> {
                e.consume();
                ProgQuittFactory.quit();
            });

            PLog.sysLog("Programmgröße: " + ProgConfig.SYSTEM_GUI_SIZE);
            scene.heightProperty().addListener((v, o, n) -> PGuiSize.getSizeScene(ProgConfig.SYSTEM_GUI_SIZE, primaryStage, scene));
            scene.widthProperty().addListener((v, o, n) -> PGuiSize.getSizeScene(ProgConfig.SYSTEM_GUI_SIZE, primaryStage, scene));
            primaryStage.xProperty().addListener((v, o, n) -> PGuiSize.getSizeScene(ProgConfig.SYSTEM_GUI_SIZE, primaryStage, scene));
            primaryStage.yProperty().addListener((v, o, n) -> PGuiSize.getSizeScene(ProgConfig.SYSTEM_GUI_SIZE, primaryStage, scene));

            if (!PGuiSize.setPos(ProgConfig.SYSTEM_GUI_SIZE, primaryStage)) {
                primaryStage.centerOnScreen();
            }
            primaryStage.show();

        } catch (final Exception e) {
            e.printStackTrace();
        }
    }

    private void addThemeCss() {
        if (ProgConfig.SYSTEM_DARK_THEME.get()) {
            P2LibInit.addCssFile(P2LibConst.CSS_GUI_DARK);
            P2LibInit.addCssFile(ProgConst.CSS_FILE_DARK_THEME);
        } else {
            P2LibInit.removeCssFile(P2LibConst.CSS_GUI_DARK);
            P2LibInit.removeCssFile(ProgConst.CSS_FILE_DARK_THEME);
        }
        P2LibInit.addP2LibCssToScene(scene);
    }
}
