/*
 * P2tools Copyright (C) 2022 W. Xaver W.Xaver[at]googlemail.com
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


package de.p2tools.filerunner.controller.config;

import de.p2tools.p2lib.tools.events.P2Event;

public class Events {
    private static int count = 0;

    public static int COMPARE_OF_FILE_LISTS_FINISHED = count;
    public static int GENERATE_COMPARE_FILE_LIST = ++count;
    public static int COLORS_CHANGED = ++count;
    public static int TIMER = ++count;

    public static P2Event event(int i) {
        return new P2Event(i, "");
    }
}
