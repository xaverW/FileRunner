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

package de.p2tools.fileRunner.controller;

import javafx.application.Platform;

import java.util.EventListener;

public class RunListener implements EventListener {

    public synchronized void notify(RunEvent runEvent) {
        Platform.runLater(() -> ping(runEvent));
    }

    /**
     * @param runEvent
     */
    public void ping(RunEvent runEvent) {
    }

}
