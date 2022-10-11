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

package de.p2tools.fileRunner.gui;

import de.p2tools.p2Lib.P2LibConst;

public class HelpText {

    public static final String STORE_PROG_CONFIG = "Die aktuellen Einstellungen des Programms können beim " +
            "nächsten Programmstart wieder hergestellt werden oder das Programm startet mit " +
            "den Standardeinstellungen.";

    public static final String COMPARE_BUTTON = "" +
            "1. Zeigt alle Dateien" + P2LibConst.LINE_SEPARATORx2 +

            "2. Zeigt gleiche Dateien die in beiden Listen enthalten sind" + P2LibConst.LINE_SEPARATORx2 +
//            "3. Zeigt Dateien mit gleichem Hash, " +
//            "auch wenn sie einen andern Namen oder Pfad haben" + P2LibConst.LINE_SEPARATORx2 +

            "3. Zeigt Dateien die sich unterscheiden oder nur in einer Liste enthalten sind" + P2LibConst.LINE_SEPARATOR +
            "4. Zeigt Dateien die sich unterscheiden und in beiden Listen enthalten sind" + P2LibConst.LINE_SEPARATORx2 +

            "5. Zeigt Dateien die nur in der rechten Liste enthalten sind" + P2LibConst.LINE_SEPARATOR +
            "6. Zeigt Dateien die nur in der linken Liste enthalten sind" + P2LibConst.LINE_SEPARATORx2 +
            "Der Button unten startet das Einlesen der beiden Testordner (oder Zipdatei/Hashdatei)";

    public static final String READ_DIR_RECURSIVE = "" +
            "Wird \"Unterverzeichnisse ...\" ausgewählt, werden beim Durchsuchen auch Unterverzeichnisse " +
            "durchsucht. Andernfalls werden nur Dateien im angegebenen " +
            "Suchverzeichnis verglichen." + P2LibConst.LINE_SEPARATOR +
            "Wird umgeschaltet, muss das zu durchsuchende Verzeichnis " +
            "neu eingelesen werden, die Tabelle wird deshalb gelöscht.";

    public static final String READ_DIR_HASH = "" +
            "Beim Vergleichen der Dateien werden gleiche Dateien mit der " +
            "gleichen HashID markiert" + P2LibConst.LINE_SEPARATORx2 +

            "Ist \"Pfad/Dateiname und Hash\" ausgewählt, sind die Dateien \"gleich\" wenn der Pfad zur Datei, " +
            "der Dateiname und der Hash identisch ist." + P2LibConst.LINE_SEPARATORx2 +

            "Ist \"Dateiname und Hash\" ausgewählt, sind die Dateien \"gleich\" wenn " +
            "der Dateiname und der Hash identisch ist. Egal, in welchem Verzeichnis sie liegen. Es müssen alle " +
            "Dateien mit dem Dateinamen einen gleichen Hash haben (also gleich sein)." + P2LibConst.LINE_SEPARATORx2 +

            "Ist \"Hash\" gewählt, sind sie \"gleich\" " +
            "wenn der Hash identisch ist, egal wie sie heißen oder wo sie liegen. \"Only\" wird markiert, " +
            "wenn es im anderen Suchverzeichnis keine Datei mit dem Hash gibt. \"Diff\" kann es hier nicht geben.";

    public static final String FOLLOW_SYMLINK = "Eine symbolische Verknüpfung ist eine Verknüpfung in einem Dateisystem " +
            "die auf eine andere Datei oder ein anderes Verzeichnis verweist." + P2LibConst.LINE_SEPARATORx2 +
            "In der Tabelle wird der Speicherort des Links angezeigt. " +
            "Wenn der Link aufgelöst werden soll, wird der Pfad auf den der Link zeigt, angezeigt.";

    public static final String DARK_THEME = "Das Programm wird damit mit einer dunklen " +
            "Programmoberfläche angezeigt. Damit alle Elemente der Programmoberfläche " +
            "geändert werden, kann ein Programmneustart notwendig sein.";

    public static final String COLORS = "Hier könne die Farben in der Tabelle mit den Dateien geändert werden. " +
            "Es kann die Hintergrundfarbe der Tabellenzeilen und die Schriftfarbe vorgegeben werden." + P2LibConst.LINE_SEPARATORx2 +
            "\"HashID ist gerade\\ungerade\" meint die Zeilen mit Dateien für die es eine gleiche Datei im " +
            "anderen abzusuchenden Verzeichnis gibt. Gleiche Dateien haben die gleiche HashID. Eine unterschiedliche Farbe " +
            "für gerade/ungerade HashIDs soll die Übersichtlichkeit erhöhen. Ist für gerade/ungerade HashIDs die gleiche " +
            "Farbe vorgegeben, werden alle gleichen Dateien mit der selben Farbe markiert (oder mit keiner wenn ausgeschaltet).";

    public static final String FILEMANAGER =
            "In der Ansicht \"Downloads\" kann man über das Kontextmenü den Downloadordner " +
                    "(Zielordner) des jeweiligen Downloads öffnen. Normalerweise wird dafür der " +
                    "Dateimanager des Betriebssystems gefunden und geöffnet. Klappt das nicht, kann " +
                    "hier ein Programm dafür angegeben werden." +
                    "\n";

    public static final String WEBBROWSER =
            "Wenn das Programm versucht, einen Link zu öffnen (z.B. \"Anleitung im Web\" im " +
                    "Programm-Menü unter \"Hilfe\") und der Standardbrowser nicht startet, " +
                    "kann damit ein Programm (Firefox, Chromium, …) ausgewählt und fest " +
                    "zugeordnet werden." +
                    "\n";
}
