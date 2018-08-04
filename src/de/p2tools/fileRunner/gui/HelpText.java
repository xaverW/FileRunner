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

import de.p2tools.p2Lib.PConst;

public class HelpText {

    public static final String STORE_PROG_CONFIG = "Die aktuellen Einstellungen des Programms können beim " +
            "nächsten Programmstart wieder hergestellt werden oder das Programm startet mit " +
            "den Standardeinstellungen.";

    public static final String FOLLOW_SYMLINK = "Eine symbolische Verknüpfung ist eine Verknüpfung in einem Dateisystem " +
            "die auf eine andere Datei oder ein anderes Verzeichnis verweist." + PConst.LINE_SEPARATORx2 +
            "In der Tabelle wird der Speicherort des Links angezeigt. " +
            "Wenn der Link aufgelöst werden soll, wird der Pfad auf den der Link zeigt, angezeigt.";
}
