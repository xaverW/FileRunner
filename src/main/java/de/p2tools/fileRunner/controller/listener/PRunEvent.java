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

package de.p2tools.fileRunner.controller.listener;

import java.util.EventObject;

public class PRunEvent extends EventObject {
    // meldet eine Änderung
    private Events.EVENT event;
    private int progress;
    private int max;
    private String text;

    public PRunEvent(Events.EVENT event, Object source, int progress, int max, String text) {
        super(source);
        this.event = event;
        this.progress = progress;
        this.max = max;
        this.text = text;
    }

    public PRunEvent(Events.EVENT event, Object source) {
        super(source);
        this.event = event;
        this.progress = 0;
        this.max = 0;
        this.text = "";
    }

    public Events.EVENT getEvent() {
        return event;
    }

    public void setEvent(Events.EVENT event) {
        this.event = event;
    }

    public String getText() {
        return text;
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
}
