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

package de.p2tools.fileRunner.controller.worker.genMosaik;

import de.p2tools.fileRunner.controller.RunListener;
import de.p2tools.fileRunner.controller.config.ProgData;
import de.p2tools.fileRunner.controller.data.mosaikData.MosaikData;

import javax.swing.event.EventListenerList;

public class GenMosaik {

    private EventListenerList listeners = new EventListenerList();
    private boolean stopAll = false;
    private ProgData progData;

    public GenMosaik(ProgData progData) {
        this.progData = progData;
    }


    /**
     * @param listener
     */
    public void addAdListener(RunListener listener) {
        listeners.add(RunListener.class, listener);
    }

    public void setStop() {
        stopAll = true;
    }

    /**
     * @param mosaikData
     */
    public void create(MosaikData mosaikData) {
    }

}
