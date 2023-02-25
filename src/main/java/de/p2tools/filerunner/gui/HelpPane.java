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


import de.p2tools.p2lib.P2LibConst;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tab;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.stage.Stage;

public class HelpPane {
    private final Stage stage;

    public HelpPane(Stage stage) {
        this.stage = stage;
    }

    public void close() {
    }

    public Tab getTab1() {
        final String path = "/de/p2tools/filerunner/icon/fileRunner-helpPage-1.png";
        ImageView iv = new ImageView();
        Image im = new Image(path, 600, 600, true, true);
        iv.setSmooth(true);
        iv.setImage(im);

        Label text = new Label("1) Was verglichen werden soll" + P2LibConst.LINE_SEPARATOR +
                "(Verzeichnis, ZIP-Datei, Datei mit Hashwerten) " + P2LibConst.LINE_SEPARATOR +
                "kann hier ausgewählt werden." +

                P2LibConst.LINE_SEPARATORx2 +
                "2) \"Diff\": Das zeigt an, dass die entsprechende Datei" + P2LibConst.LINE_SEPARATOR +
                "im anderen Vergleichsordner nicht gleich ist." +

                P2LibConst.LINE_SEPARATORx2 +
                "3) \"Only\": Markierung \"OK\" zeigt an, dass es die Datei" + P2LibConst.LINE_SEPARATOR +
                "nur einmal gibt. Die Markierung \"-\" bedeutet, dass" + P2LibConst.LINE_SEPARATOR +
                "es die Vergleichsdatei im anderen Ordner nicht gibt," + P2LibConst.LINE_SEPARATOR +
                "dass aber andere Dateien (anderer Name oder Pfad)" + P2LibConst.LINE_SEPARATOR +
                "existieren, die identisch sind." +

                P2LibConst.LINE_SEPARATORx2 +
                "4) Die HashID ist eine Nummer für den Hashwert der" + P2LibConst.LINE_SEPARATOR +
                " Datei. Alle gleichen Dateien (Name und Pfad ist" + P2LibConst.LINE_SEPARATOR +
                " dabei egal) haben die gleiche HashID." +

                P2LibConst.LINE_SEPARATORx2 +
                "5) Gleiche Dateien haben eine \"HashID\" und eine" + P2LibConst.LINE_SEPARATOR +
                "\"HashID\" und keine Markierung in \"Diff\" oder \"Only\"." +

                P2LibConst.LINE_SEPARATORx2 +
                "6) Hier wird festgelegt, wie die Dateien verglichen" + P2LibConst.LINE_SEPARATOR +
                "werden. Mit \"Pafd/Name/Hash:\" Dateien sind gleich," + P2LibConst.LINE_SEPARATOR +
                "wenn eben das, alles gleich ist." + P2LibConst.LINE_SEPARATOR +
                "\"Nur mit dem Hash\": Dann sind die Dateien gleich, " + P2LibConst.LINE_SEPARATOR +
                "wenn der Hashwert der Dateien identisch ist" + P2LibConst.LINE_SEPARATOR +
                "und es ist egal, wie die Datei heißt" + P2LibConst.LINE_SEPARATOR +
                "oder wo sie liegt." +

                P2LibConst.LINE_SEPARATORx2 +
                "7) Ob nur das angegebene Verzeichnis oder auch" + P2LibConst.LINE_SEPARATOR +
                "Unterverzeichnisse durchsucht werden sollen," + P2LibConst.LINE_SEPARATOR +
                "kann hier ausgewählt werden." +

                P2LibConst.LINE_SEPARATORx2 +
                "8) Damit werden alle Dateien in der Tabelle" + P2LibConst.LINE_SEPARATOR +
                "angezeigt." +

                P2LibConst.LINE_SEPARATORx2 +
                "9) Hier werden (oben) alle gleichen Dateien angezeigt" + P2LibConst.LINE_SEPARATOR +
                "und (unten) werden alle gleichen Dateien und " + P2LibConst.LINE_SEPARATOR +
                "alle Dateien mit gleichem Hash angezeigt, also auch" + P2LibConst.LINE_SEPARATOR +
                "die mit unterschiedlichem Pfad oder Namen." +

                P2LibConst.LINE_SEPARATORx2 +
                "10) Dateien die unterschiedlich sind oder nur" + P2LibConst.LINE_SEPARATOR +
                "in einem Verzeichnis liegen, werden damit (oben)" + P2LibConst.LINE_SEPARATOR +
                "ausgewählt. Dateien die unterschiedlich sind und in" + P2LibConst.LINE_SEPARATOR +
                "beiden Verzeichnissen liegen werden (unten) damit" + P2LibConst.LINE_SEPARATOR +
                "angezeigt." +

                P2LibConst.LINE_SEPARATORx2 +
                "11) Hiermit werden Dateien die entweder nur in dem" + P2LibConst.LINE_SEPARATOR +
                "einen oder nur in dem anderen Verzeichnis enthalten" + P2LibConst.LINE_SEPARATOR +
                "sind, angezeigt." +

                P2LibConst.LINE_SEPARATORx2 +
                "12) Die Hashwerte aller Dateien können hier in" + P2LibConst.LINE_SEPARATOR +
                "eine Hashdatei geschrieben werden. Der Button mit" + P2LibConst.LINE_SEPARATOR +
                "dem \"Doppelpfeil\" schlägt verschiedene" + P2LibConst.LINE_SEPARATOR +
                "Dateinamen vor."
                + P2LibConst.LINE_SEPARATOR + " ");

        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setContent(text);
        scrollPane.setMinWidth(text.getWidth() + 100);
        scrollPane.setMaxHeight(im.getHeight());
        scrollPane.setFitToWidth(true);
        HBox.setHgrow(scrollPane, Priority.ALWAYS);

        HBox hBox = new HBox();
        hBox.setSpacing(25);
        hBox.setPadding(new Insets(20));
        hBox.getChildren().addAll(iv, scrollPane);
        Tab tab = new Tab("Ordnervergleich", hBox);
        tab.setClosable(false);
        return tab;
    }

    public Tab getTab2() {
        final String path = "/de/p2tools/filerunner/icon/fileRunner-helpPage-2.png";
        ImageView iv = new ImageView();
        Image im = new Image(path, 600, 600, true, true);

        iv.setSmooth(true);
        iv.setImage(im);

        Label text = new Label("1) Hier werden die beiden Dateien die" + P2LibConst.LINE_SEPARATOR +
                "verglichen werden sollen, angegeben. Es kann" + P2LibConst.LINE_SEPARATOR +
                "die Datei selbst, eine Hashdatei dieser Datei oder" + P2LibConst.LINE_SEPARATOR +
                "der Hashwert direkt angegeben werden." +

                P2LibConst.LINE_SEPARATORx2 +
                "2) Hier wird die zu vergleichende Datei angegeben." +

                P2LibConst.LINE_SEPARATORx2 +
                "3) Oder hier eine Hashdatei mit dem Hash von ihr." +

                P2LibConst.LINE_SEPARATORx2 +
                "4) Oder es kann auch der Hashwert direkt hier " + P2LibConst.LINE_SEPARATOR +
                "angegeben werden." +

                P2LibConst.LINE_SEPARATORx2 +
                "5) Der Hashwert der Dateie kann hier in" + P2LibConst.LINE_SEPARATOR +
                "eine Hashdatei geschrieben werden. Der Button mit" + P2LibConst.LINE_SEPARATOR +
                "dem \"Doppelpfeil\" schlägt verschiedene" + P2LibConst.LINE_SEPARATOR +
                "Dateinamen vor." +

                P2LibConst.LINE_SEPARATORx2 +
                "6) Welches Hashverfahren zum Vergleich verwendet wird," + P2LibConst.LINE_SEPARATOR +
                "wird hier ausgewählt." +

                P2LibConst.LINE_SEPARATORx2 +
                "7) Das Einlesen und Erstellen des Hash der Datei oder " + P2LibConst.LINE_SEPARATOR +
                "das Einlesen des Hash aus der Hashdatei mit" + P2LibConst.LINE_SEPARATOR +
                "anschließendem Vergleich wird hier gestartet."
                + P2LibConst.LINE_SEPARATOR + " ");

        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setContent(text);
        HBox.setHgrow(scrollPane, Priority.ALWAYS);
        scrollPane.setMaxHeight(im.getHeight());
        scrollPane.setFitToWidth(true);

        HBox hBox = new HBox();
        hBox.setSpacing(25);
        hBox.setPadding(new Insets(20));
        hBox.getChildren().addAll(iv, scrollPane);
        Tab tab = new Tab("Dateivergleich", hBox);
        tab.setClosable(false);
        return tab;
    }
}
