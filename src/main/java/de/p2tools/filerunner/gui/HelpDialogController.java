/*
 * Copyright (C) 2017 W. Xaver W.Xaver[at]googlemail.com
 * https://www.p2tools.de
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

package de.p2tools.filerunner.gui;


import de.p2tools.filerunner.controller.config.ProgData;
import de.p2tools.p2lib.dialogs.dialog.PDialogExtra;
import javafx.scene.control.Button;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.Tooltip;

public class HelpDialogController extends PDialogExtra {

    private final TabPane tabPane = new TabPane();
    private Button btnOk;
    private Tab helpTab1;
    private Tab helpTab2;
    private HelpPane helpPage;
    private final ProgData progData;

    public HelpDialogController() {
        super(ProgData.getInstance().primaryStage, null, "Programmoberfläche",
                true, false, DECO.NO_BORDER);
        this.progData = ProgData.getInstance();
        init(true);
    }

    @Override
    public void make() {
        initStack();
        initButton();
    }

    private void initStack() {
        getVBoxCont().getChildren().add(tabPane);
        //startPane 1
        helpPage = new HelpPane(getStage());
        helpTab1 = helpPage.getTab1();
        //startPane 2
        helpTab2 = helpPage.getTab2();
        tabPane.getTabs().addAll(helpTab1, helpTab2);
    }

    private void initButton() {
        btnOk = new Button("_Ok");
        btnOk.setOnAction(a -> {
            closeDialog(true);
        });

        btnOk.getStyleClass().add("btnStartDialog");
        btnOk.setTooltip(new Tooltip("Dialog wieder schließen"));
        addOkButton(btnOk);
        getButtonBar().setButtonOrder("BX+CO");
    }

    private void closeDialog(boolean ok) {
        helpPage.close();
        super.close();
    }
}
