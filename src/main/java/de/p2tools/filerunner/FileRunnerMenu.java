/*
 * P2tools Copyright (C) 2022 W. Xaver W.Xaver[at]googlemail.com
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
import de.p2tools.filerunner.controller.SearchProgramUpdate;
import de.p2tools.filerunner.controller.config.ProgConfig;
import de.p2tools.filerunner.controller.config.ProgData;
import de.p2tools.filerunner.gui.HelpDialogController;
import de.p2tools.filerunner.gui.configdialog.ConfigDialogController;
import de.p2tools.filerunner.gui.dialog.AboutDialogController;
import de.p2tools.filerunner.gui.picon.PIconFactory;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.control.Tooltip;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.Region;
import org.kordamp.ikonli.javafx.FontIcon;

public class FileRunnerMenu extends MenuButton {
    public FileRunnerMenu() {
        makeButton();
        setOnMouseClicked(mouseEvent -> {
            if (mouseEvent.getButton().equals(MouseButton.PRIMARY)) {
                if (mouseEvent.getClickCount() > 1) {
                    ProgConfig.SYSTEM_GUI_THEME_1.set(!ProgConfig.SYSTEM_GUI_THEME_1.get());
                }
            }

            if (mouseEvent.getButton().equals(MouseButton.SECONDARY)) {
                ProgConfig.SYSTEM_DARK_THEME.set(!ProgConfig.SYSTEM_DARK_THEME.get());
            }
        });
    }

    private void makeButton() {
        // Menü
        final MenuItem miConfig = new MenuItem("Einstellungen");
        miConfig.setOnAction(e -> new ConfigDialogController());

        final MenuItem miHelp = new MenuItem("Hilfe");
        miHelp.setOnAction(e -> new HelpDialogController());

        final MenuItem miSearchUpdate = new MenuItem("Gibt's ein Update?");
        miSearchUpdate.setOnAction(a -> new SearchProgramUpdate(ProgData.getInstance()).searchNewProgramVersion(true));

        final MenuItem miAbout = new MenuItem("Über dieses Programm");
        miAbout.setOnAction(event -> new AboutDialogController(ProgData.getInstance()).showDialog());

        final MenuItem miQuitt = new MenuItem("Beenden");
        miQuitt.setOnAction(e -> ProgQuittFactory.quit());

        setTooltip(new Tooltip("Programmeinstellungen anzeigen"));
        setMinWidth(Region.USE_PREF_SIZE);
        getStyleClass().addAll("pFuncBtn", "btnProgMenu");
        setText("");
        FontIcon node = PIconFactory.PICON.PROG_MENU.getFontIcon();
        node.setScaleX(1.5);
        setGraphic(node);
        getItems().addAll(miConfig, miHelp, miSearchUpdate, miAbout,
                new SeparatorMenuItem(), miQuitt);
    }
}