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


package de.p2tools.p2Lib.tools;

public class FileSize implements Comparable<FileSize> {
    private long sizeL = 0;
    private String sizeStr = "";
    private static final long GBYTE = 1000L * 1000L * 1000L;
    private static final long MBYTE = 1000L * 1000L;
    private static final long KBYTE = 1000L;

    public FileSize(long sizeL) {
        this.sizeL = sizeL;
        convertToStr();
    }

    public FileSize(String sizeStr) {
        try {
            sizeL = Long.parseLong(sizeStr);
        } catch (Exception ex) {
            sizeL = 0;
        }
        convertToStr();
    }

    private void convertToStr() {
        if (sizeL == 0) {
            sizeStr = "";
        } else if (sizeL < KBYTE) {
            sizeStr = sizeL + " Byte";

        } else if (sizeL < MBYTE) {
            sizeStr = sizeL / KBYTE + " kB";

        } else if (sizeL < GBYTE) {
            sizeStr = sizeL / MBYTE + " MB";

        } else {
            sizeStr = sizeL / GBYTE + " GB";
        }
    }


    public String toString() {
        return sizeStr;
    }

    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }

        final FileSize other = (FileSize) obj;
        return other.sizeL == sizeL;
    }

    @Override
    public int compareTo(FileSize o) {
        if (sizeL < o.sizeL) {
            return -1;
        }
        if (sizeL == o.sizeL) {
            return 0;
        }
        return 1;
    }
}
