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


package de.p2tools.p2Lib.hash;

import de.p2tools.p2Lib.tools.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;

public class WriteHashFile {

    public static void writeFileHash(File file, String fileHash, String hash) {

        try (OutputStreamWriter out = new OutputStreamWriter(
                new FileOutputStream(file, false), StandardCharsets.UTF_8)) {

            out.write(hash + " " + "*" + fileHash + "\n");

        } catch (Exception ex) {
            Log.errorLog(620120124, ex, "Fehler beim Schreiben der Hashdatei!");
        }
    }

}
