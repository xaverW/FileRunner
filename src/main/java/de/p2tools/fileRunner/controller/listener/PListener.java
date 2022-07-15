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

import javafx.application.Platform;

import java.util.EventListener;

public class PListener implements EventListener {

    private final Events.EVENT event;

    public PListener(Events.EVENT event) {
        this.event = event;
    }

    public Events.EVENT getEvent() {
        return event;
    }

    public synchronized void notify(PRunEvent runEvent) {
        ping(runEvent);
    }

    public synchronized void notifyGui(PRunEvent runEvent) {
        Platform.runLater(() -> ping(runEvent));
    }

    /**
     * @param runEvent
     */
    public void ping(PRunEvent runEvent) {
    }
}
