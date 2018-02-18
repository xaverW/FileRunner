/*
 * MTPlayer Copyright (C) 2017 W. Xaver W.Xaver[at]googlemail.com
 * http://zdfmediathk.sourceforge.net/
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


package de.p2tools.p2Lib.configFile;

import de.p2tools.p2Lib.configFile.config.Config;

import java.util.ArrayList;

/**
 * this are the Classes in witch the configurationdata are stored
 * the configurationdata has a *TAG*, which is tha name of the XML-TAG
 * in the config-file and a array with the *DATA*, property in which the
 * infos are stored
 */
public interface ConfigsData {

    String getTag();

    String getComment();

    ArrayList<Config> getConfigsArr();

}
