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
            "1. Zeigt alle Dateien" + P2LibConst.LINE_SEPARATOR +
            "2. Zeigt gleiche Dateien die in beiden Listen enthalten sind" + P2LibConst.LINE_SEPARATOR +
            "3. Zeigt Dateien die sich unterscheiden oder nur in einer Liste enthalten sind" + P2LibConst.LINE_SEPARATORx2 +

            "4. Zeigt Dateien die sich unterscheiden und in beiden Listen enthalten sind" + P2LibConst.LINE_SEPARATOR +
            "5. Zeigt Dateien die nur in der rechten Liste enthalten sind" + P2LibConst.LINE_SEPARATOR +
            "6. Zeigt Dateien die nur in der linken Liste enthalten sind" + P2LibConst.LINE_SEPARATORx2 +

            "7. Ist das geklickt, werden bei dem Dateivergleich die Pfade in denen die Dateien liegen, " +
            "nicht berücksichtigt. Dateien sind also auch dann gleich, wenn sie in unterschiedlichen " +
            "Verzeichnissen liegen.";

    public static final String FOLLOW_SYMLINK = "Eine symbolische Verknüpfung ist eine Verknüpfung in einem Dateisystem " +
            "die auf eine andere Datei oder ein anderes Verzeichnis verweist." + P2LibConst.LINE_SEPARATORx2 +
            "In der Tabelle wird der Speicherort des Links angezeigt. " +
            "Wenn der Link aufgelöst werden soll, wird der Pfad auf den der Link zeigt, angezeigt.";

    public static final String DARK_THEME = "Das Programm wird damit mit einer dunklen " +
            "Programmoberfläche angezeigt. Damit alle Elemente der Programmoberfläche " +
            "geändert werden, kann ein Programmneustart notwendig sein.";

}
