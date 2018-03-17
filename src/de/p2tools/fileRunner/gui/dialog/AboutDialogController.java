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

package de.p2tools.fileRunner.gui.dialog;

import de.p2tools.fileRunner.controller.config.ProgConst;
import de.p2tools.fileRunner.controller.config.ProgData;
import de.p2tools.fileRunner.controller.config.ProgInfos;
import de.p2tools.fileRunner.gui.tools.MTOpen;
import de.p2tools.fileRunner.res.GetIcon;
import de.p2tools.p2Lib.dialog.MTDialogExtra;
import de.p2tools.p2Lib.tools.Functions;
import de.p2tools.p2Lib.tools.Log;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

import java.nio.file.Path;

public class AboutDialogController extends MTDialogExtra {

    private final ProgData progData;
    Button btnOk = new Button("Ok");
    private final Color GRAY = Color.DARKSLATEGRAY;


    public AboutDialogController(ProgData progData) {
        super(null, null, "Über das Programm", true);

        this.progData = progData;

        getTilePaneOk().getChildren().addAll(btnOk);
        init(getvBoxDialog(), true);
    }


    @Override
    public void make() {
        btnOk.setOnAction(a -> close());
        HBox hBox = new HBox(10);
        getVboxCont().getChildren().add(hBox);

        ImageView iv = new ImageView();
        Image im = getImage();
        if (im != null) {
            iv.setSmooth(true);
            iv.setCache(true);
            iv.setImage(im);
            iv.setFitWidth(150);
            iv.setPreserveRatio(true);
            iv.setSmooth(true);

            hBox.getChildren().add(iv);
        }

        final GridPane gridPane = new GridPane();
        gridPane.setHgap(5);
        gridPane.setVgap(10);
        gridPane.setPadding(new Insets(10, 10, 10, 10));

        int row = 0;

        // top
        Text text = new Text(ProgConst.PROGRAMMNAME);
        text.setFont(Font.font(null, FontWeight.BOLD, 40));
        gridPane.add(text, 0, row, 2, 1);

        text = new Text("\nVersion: " + Functions.getProgVersion());
        text.setFont(new Font(18));
        gridPane.add(text, 0, ++row, 2, 1);

        text = new Text("[ Build: " + Functions.getBuild() + " vom " + Functions.getCompileDate() + " ]");
        text.setFont(new Font(15));
        text.setFill(GRAY);
        gridPane.add(text, 0, ++row, 2, 1);

        text = new Text("\n\nAutor");
        text.setFont(Font.font(null, FontWeight.BOLD, 15));
        gridPane.add(text, 0, ++row, 2, 1);

        text = new Text("Xaver W. (xaverW)");
        text.setFont(new Font(15));
        gridPane.add(text, 0, ++row, 2, 1);


        // Pfade
        text = new Text("\n\nProgramm Informationen");
        text.setFont(Font.font(null, FontWeight.BOLD, 15));
        gridPane.add(text, 0, ++row, 2, 1);

        Hyperlink hyperlink = new Hyperlink(ProgConst.WEBSITE_P2);
        hyperlink.setStyle("-fx-font-size: 15px;");
        hyperlink.setOnAction(a -> {
            try {
                MTOpen.openURL(ProgConst.WEBSITE_P2);
            } catch (Exception e) {
                Log.errorLog(974125469, e);
            }
        });

        text = new Text("Website:");
        text.setFont(new Font(15));
        text.setFill(GRAY);
        gridPane.add(text, 0, ++row);
        gridPane.add(hyperlink, 1, row);

        text = new Text("Einstellungen:");
        text.setFont(new Font(15));
        text.setFill(GRAY);
        gridPane.add(text, 0, ++row);

        final Path xmlFilePath = new ProgInfos().getXmlFilePath();
        text = new Text(xmlFilePath.toAbsolutePath().toString());
        text.setFont(new Font(15));
        text.setFill(GRAY);
        gridPane.add(text, 1, row);


        // Java
        text = new Text("\n\nJava Informationen");
        text.setFont(Font.font(null, FontWeight.BOLD, 15));
        gridPane.add(text, 0, ++row, 2, 1);

        text = new Text("Version:");
        text.setFont(new Font(15));
        text.setFill(GRAY);
        gridPane.add(text, 0, ++row);

        text = new Text(System.getProperty("java.version"));
        text.setFont(new Font(15));
        text.setFill(GRAY);
        gridPane.add(text, 1, row);

        text = new Text("Type:");
        text.setFont(new Font(15));
        text.setFill(GRAY);
        gridPane.add(text, 0, ++row);

        String strVmType = System.getProperty("java.vm.name");
        strVmType += " (" + System.getProperty("java.vendor") + ")";
        text = new Text(strVmType);
        text.setFont(new Font(15));
        text.setFill(GRAY);
        gridPane.add(text, 1, row);

        text = new Text("\n\nEin Dankeschön an alle,\ndie zu dieser Software beigetragen haben.");
        text.setFont(Font.font(null, FontWeight.BOLD, 15));
        gridPane.add(text, 0, ++row, 2, 1);


        hBox.getChildren().add(gridPane);

    }

    private javafx.scene.image.Image getImage() {
        Image img = null;
        try {
            img = GetIcon.getImage(ProgConst.LOGO_NAME, ProgConst.ICON_PATH, 150, 150);
        } catch (Exception ex) {
            Log.errorLog(975421305, ex);
        }
        return img;
    }

}
