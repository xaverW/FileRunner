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

            "2. Zeigt gleiche Dateien die in beiden Listen enthalten sind" + P2LibConst.LINE_SEPARATOR +
            "3. Zeigt Dateien die sich unterscheiden oder nur in einer Liste enthalten sind" + P2LibConst.LINE_SEPARATOR +
            "4. Zeigt Dateien die sich unterscheiden und in beiden Listen enthalten sind" + P2LibConst.LINE_SEPARATORx2 +

            "5. Zeigt Dateien die nur in der rechten Liste enthalten sind" + P2LibConst.LINE_SEPARATOR +
            "6. Zeigt Dateien die nur in der linken Liste enthalten sind";

    public static final String READ_DIR_RECURSIVE_HASH = "" +
            "Wird \"Unterverzeichnis ...\" ausgewählt, werden beim Durchsuchen auch Unterverzeichnisse " +
            "durchsucht. Andernfalls werden nur Dateien im angegebenen " +
            "Suchverzeichnis verglichen." + P2LibConst.LINE_SEPARATORx2 +
            
            "Ist \"mit dem Hashwert vergleichen\" aktiv, werden die Dateien auch " +
            "nur über den Hash der Datei verglichen. " +
            "D.h. Dateien sind gleich wenn der Hash gleich ist, auch " +
            "dann wenn sie in unterschiedlichen Verzeichnissen liegen oder " +
            "unterschiedliche Dateinamen haben. Bei ausgeschalteter Option muss die " +
            "Datei gleich sein, der Dateiname muss gleich sein und sie muss auch im " +
            "gleich Verzeichnis liegen (Unterverzeichnis des Suchverzeichnisses).";

    public static final String FOLLOW_SYMLINK = "Eine symbolische Verknüpfung ist eine Verknüpfung in einem Dateisystem " +
            "die auf eine andere Datei oder ein anderes Verzeichnis verweist." + P2LibConst.LINE_SEPARATORx2 +
            "In der Tabelle wird der Speicherort des Links angezeigt. " +
            "Wenn der Link aufgelöst werden soll, wird der Pfad auf den der Link zeigt, angezeigt.";

    public static final String DARK_THEME = "Das Programm wird damit mit einer dunklen " +
            "Programmoberfläche angezeigt. Damit alle Elemente der Programmoberfläche " +
            "geändert werden, kann ein Programmneustart notwendig sein.";

}
