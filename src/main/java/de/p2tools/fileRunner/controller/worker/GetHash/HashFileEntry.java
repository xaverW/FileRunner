/*
 * P2tools Copyright (C) 2019 W. Xaver W.Xaver[at]googlemail.com
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


package de.p2tools.fileRunner.controller.worker.GetHash;

public class HashFileEntry {
    private final String hash, file, size, date;

    public HashFileEntry(String hash, String file, String size, String date) {
        this.hash = hash;
        this.file = file;
        this.size = size;
        this.date = date;
    }

    public String getHash() {
        return hash;
    }

    public String getFile() {
        return file;
    }

    public String getSize() {
        return size;
    }

    public String getDate() {
        return date;
    }
}
