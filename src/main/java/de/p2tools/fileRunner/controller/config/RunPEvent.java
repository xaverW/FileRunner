/*
 *    Copyright (C) 2008
 *
 *    This program is free software: you can redistribute it and/or modify
 *    it under the terms of the GNU General Public License as published by
 *    the Free Software Foundation, either version 3 of the License, or
 *    any later version.
 *
 *    This program is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *    GNU General Public License for more details.
 *
 *    You should have received a copy of the GNU General Public License
 *    along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package de.p2tools.fileRunner.controller.config;

import de.p2tools.p2Lib.tools.events.PEvent;

public class RunPEvent extends PEvent {
    // meldet eine Änderung
    private int progress;
    private int max;
    private String text2 = "";
    private boolean error = false;

    public RunPEvent(int eventNo, int progress, int max, String text, String text2, boolean error) {
        super(eventNo, text);
        setEventNo(eventNo);
        this.progress = progress;
        this.max = max;
        this.text2 = text2;
        this.error = error;
    }

    public RunPEvent(int eventNo, int progress, int max, String text) {
        super(eventNo, text);
        setEventNo(eventNo);
        this.progress = progress;
        this.max = max;
    }

    public RunPEvent(int eventNo) {
        super(eventNo, "");
        setEventNo(eventNo);
        this.progress = 0;
        this.max = 0;
    }

    public int getProgress() {
        return progress;
    }

    public int getMax() {
        return max;
    }

    public boolean nixLos() {
        return max == 0;
    }

    public String getText2() {
        return text2;
    }


    public boolean isError() {
        return error;
    }


}
