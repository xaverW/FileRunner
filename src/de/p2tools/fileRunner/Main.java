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
package de.p2tools.fileRunner;

import de.p2tools.fileRunner.controller.config.ProgConst;
import de.p2tools.p2Lib.guiTools.LinuxJavaFx;
import javafx.application.Application;
import javafx.application.Platform;
import org.apache.commons.lang3.SystemUtils;

public class Main {

    /*
     * Aufruf: java -jar fileRunner [Pfad zur Konfigdatei, sonst homeverzeichnis] [Schalter]
     *
     * Programmschalter:
     *
     * -d,--debug        show debug info
     * -h,--help         show help
     * -p,--path <arg>   path of configuration file
     * -v,--version      show version
     *
     */

    /**
     * @param args the command line arguments
     */
    public static void main(final String args[]) {
        new Main().start(args);
    }

    private void start(String... args) {
        if (LinuxJavaFx.hasJavaFx()) {

            new AppParameter().processArgs(args);
            startGui(args);
        }
    }


    private void startGui(final String[] args) {
        Platform.setImplicitExit(false);

        if (SystemUtils.IS_OS_UNIX) {
            LinuxJavaFx.setupX11WindowManagerClassName(ProgConst.PROGRAMMNAME);
        }

        Application.launch(FileRunner.class, args);
    }

}
