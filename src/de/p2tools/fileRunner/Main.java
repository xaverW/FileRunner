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
import de.p2tools.p2Lib.tools.log.PLog;
import javafx.application.Application;
import javafx.application.Platform;
import org.apache.commons.lang3.SystemUtils;

import java.awt.*;

public class Main {

    private static final String JAVAFX_CLASSNAME_APPLICATION_PLATFORM = "javafx.application.Platform";
    private static final String X11_AWT_APP_CLASS_NAME = "awtAppClassName";
    private static final String ERROR_NO_JAVAFX_INSTALLED = "JavaFX wurde nicht im Klassenpfad gefunden. \n" +
            "Stellen Sie sicher, dass Sie ein Java JRE ab Version 8 benutzen. \n" +
            "Falls Sie Linux nutzen, installieren Sie das openjfx-Paket ihres \n" +
            "Package-Managers, oder nutzen Sie eine eigene JRE-Installation.";
    public static final String TEXT_LINE = "===========================================";

    /**
     * Tests if javafx is in the classpath by loading a well known class.
     */
    private static boolean hasJavaFx() {
        try {
            Class.forName(JAVAFX_CLASSNAME_APPLICATION_PLATFORM);
            return true;

        } catch (final ClassNotFoundException e) {
            PLog.errorLog(487651240, new String[]{TEXT_LINE, ERROR_NO_JAVAFX_INSTALLED, TEXT_LINE});
            return false;
        }
    }

    /*
     * Aufruf: java -jar fileRunner [Pfad zur Konfigdatei, sonst homeverzeichnis] [Schalter]
     *
     * Programmschalter:
     *
     * -d debug
     * -v Programmversion
     *
     */

    /**
     * @param args the command line arguments
     */
    public static void main(final String args[]) {
        new Main().start(args);
    }

    private void start(String... args) {
        if (hasJavaFx()) {

            new AppParameter().processArgs(args);
            startGui(args);
        }
    }


    private void startGui(final String[] args) {
        // JavaFX stuff
        Platform.setImplicitExit(false);

        if (SystemUtils.IS_OS_UNIX) {
            setupX11WindowManagerClassName();
        }

        Application.launch(FileRunner.class, args);
    }

    /**
     * Setup the X11 window manager WM_CLASS hint. Enables e.g. GNOME to determine application name
     * and to enable app specific functionality.
     */
    private void setupX11WindowManagerClassName() {
        try {
            final Toolkit xToolkit = Toolkit.getDefaultToolkit();
            final java.lang.reflect.Field awtAppClassNameField = xToolkit.getClass().getDeclaredField(X11_AWT_APP_CLASS_NAME);
            awtAppClassNameField.setAccessible(true);
            awtAppClassNameField.set(xToolkit, ProgConst.PROGRAMMNAME);
        } catch (final Exception ignored) {
            System.err.println("Couldn't set awtAppClassName");
        }
    }

}
