/*
 * MTPlayer Copyright (C) 2017 W. Xaver W.Xaver[at]googlemail.com
 * https://sourceforge.net/projects/mtplayer/
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

public class HelpText {

    public static final String PROJECT_PATH = "Hier wird der Ordner ausgewählt, " +
            "in dem das Mosaik erstellt wird. In dem Ordner werden auch die " +
            "erstellten Vorschaubilder zum Bauen des Mosaik gespeichert.";

    public static final String GET_THUMB_DIR = "Aus den Fotos in dem gewählten Ordner werden " +
            "Miniaturbilder erstellt. Diese werden dann im Verzeichnis des Projekts gespeichert. " +
            "Die Miniaturbilder werden zum Erstellen des Mosaik verwendet";

    public static final String IMAGE_TAMPLATE = "Dieses Foto wird als Vorlage für das Mosaik verwendet. " +
            "Das Mosaik ist ein neues Bild bei dem die Bildpunkte der Vorlage durch " +
            "ein passendes Miniaturbild ersetzt werden.";

    public static final String MOSAIK_DEST = "Das erstellte Mosaik wird im angegebenen Ordner " +
            "gespeichert.";

    public static final String WALLPAPER_DEST = "Die erstellte Fototapete wird im angegebenen Ordner " +
            "gespeichert.";

    public static final String MOSAIK_PIXEL_SIZE = "Hier kann die Größe der Miniaturbilder (in Pixel) " +
            "vorgegeben werden. Die Größe der Miniaturbilder die die einzelnen Pixel " +
            "des Vorlagenfoto ersetzen wird damit eingestellt. Ein guter Wert liegt zwischen " +
            "50 und 100.";

    public static final String WALLPAPER_PIXEL_SIZE = "Hier kann die Größe der Miniaturbilder (in Pixel) " +
            "vorgegeben werden. Mit den Miniaturbildern wird dann die Fototapete erstellt.";

    public static final String MOSAIK_PIXEL_COUNT = "Dadurch wird die Größe des erstellten Mosaik vorgegeben. " +
            "Das Mosaik wird mit \"Anzahl\" Miniaturbildern pro Zeile aufgebaut. Dadurch hat " +
            "das erstellte Mosaik eine Breite von \"Anzahl\" * \"Pixelgröße\" in Pixeln. " +
            "Je größer das Mosaik wird, desto größer wird auch die erstellte Datei. " +
            "Ein gutes Ergebnis bekommt man etwa ab 100 " +
            "Miniaturbildern pro Zeile.";

    public static final String WALLPAPER_PIXEL_COUNT = "Dadurch wird die Größe der erstellten " +
            "Fototapete vorgegeben. " +
            "Die Fototapete wird mit \"Anzahl\" Miniaturbildern pro Zeile aufgebaut. Dadurch hat " +
            "die erstellte Fototapete eine Breite von \"Anzahl\" * \"Pixelgröße\" in Pixeln.";

    public static final String MOSAIK_PIXEL_FOTO = "Hier kann ausgewählt werden, was als Miniaturbilder " +
            "zum Bauen des Mosaik verwendet wird.\n" +
            "Möglichkeit 1: Es werden die erstellten Miniaturbilder des Projekts verwendet.\n" +
            "Möglichkeit 2: Aus der Fotovorlage werden Miniaturbilder erstellt, diese werden dann passend \"eingefärbt\" " +
            "und dann zum Bauen des Mosaik verwendet.";

    public static final String MOSAIK_BW = "Das Mosaik wird als Schwarz-Weiß Bild erstellt.";

}
