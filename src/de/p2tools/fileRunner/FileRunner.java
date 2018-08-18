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

import de.p2tools.fileRunner.controller.ProgQuitt;
import de.p2tools.fileRunner.controller.ProgStart;
import de.p2tools.fileRunner.controller.config.ProgConfig;
import de.p2tools.fileRunner.controller.config.ProgConst;
import de.p2tools.fileRunner.controller.config.ProgData;
import de.p2tools.fileRunner.res.GetIcon;
import de.p2tools.p2Lib.guiTools.PGuiSize;
import de.p2tools.p2Lib.tools.log.Duration;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class FileRunner extends Application {

    private Stage primaryStage;
    private FileRunnerController root;

    private static final String TEXT_LINE = "==========================================";
    private static final String LOG_TEXT_STARTPARAMETER_PATTERN = "Startparameter: %s";

    private static final String LOG_TEXT_PROGRAMMSTART = "***Programmstart***";
    private static final String ARGUMENT_PREFIX = "-";

    protected ProgData progData;
    Scene scene = null;

    @Override
    public void init() throws Exception {
    }

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;

        Duration.counterStart(LOG_TEXT_PROGRAMMSTART);
        progData = ProgData.getInstance();
        progData.primaryStage = primaryStage;
        new ProgStart(progData).loadConfigData();

        initRootLayout();
        losGehts();
    }

    private void initRootLayout() {
        try {
            root = new FileRunnerController();
            progData.fileRunnerController = root;
            scene = new Scene(root,
                    PGuiSize.getWidth(ProgConfig.SYSTEM_GUI_SIZE),
                    PGuiSize.getHeight(ProgConfig.SYSTEM_GUI_SIZE));

            String css = this.getClass().getResource(ProgConst.CSS_FILE).toExternalForm();
            scene.getStylesheets().add(css);

            primaryStage.setScene(scene);
            primaryStage.setOnCloseRequest(e -> {
                e.consume();
                new ProgQuitt().beenden(true);
            });

            PGuiSize.setPos(ProgConfig.SYSTEM_GUI_SIZE, primaryStage);
            primaryStage.show();

        } catch (final Exception e) {
            e.printStackTrace();
        }
    }

    public void losGehts() {
        Duration.counterStop(LOG_TEXT_PROGRAMMSTART);
        primaryStage.getIcons().add(GetIcon.getImage(ProgConst.ICON_NAME, ProgConst.ICON_PATH, 32, 32));

        Duration.staticPing("Erster Start");
        primaryStage.setTitle(ProgConst.PROGRAMMNAME);

        Duration.staticPing("Gui steht!");
    }

}
