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

package de.p2tools.fileRunner.controller.config;

import de.p2tools.fileRunner.Main;
import de.p2tools.fileRunner.controller.Messages;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.CodeSource;

public class ProgInfos {

    public static String getUserAgent() {
        return "";
    }


    /**
     * Retrieve the path to the program jar file.
     *
     * @return The program jar file path with a separator added.
     */
    public static String getPathJar() {
        // macht Probleme bei Win und Netzwerkpfaden, liefert dann Absolute Pfade zB. \\VBOXSVR\share\Mediathek\...
        final String pFilePath = "pFile";
        File propFile = new File(pFilePath);
        if (!propFile.exists()) {
            try {
                final CodeSource cS = Main.class.getProtectionDomain().getCodeSource();
                final File jarFile = new File(cS.getLocation().toURI().getPath());
                final String jarDir = jarFile.getParentFile().getPath();
                propFile = new File(jarDir + File.separator + pFilePath);
            } catch (final Exception ignored) {
            }
        }
        String s = propFile.getAbsolutePath().replace(pFilePath, "");
        if (!s.endsWith(File.separator)) {
            s = s + File.separator;
        }
        return s;
    }

    public static String pathProgramIcons() {
        return getPathJar() + ProgConst.VERZEICHNIS_PROGRAMM_ICONS;
    }

    /**
     * Return the path to "p2tools.xml"
     *
     * @return Path object to p2tools.xml file
     */
    public Path getXmlFilePath() {
        return ProgInfos.getSettingsDirectory().resolve(ProgConst.CONFIG_FILE);
    }

    /**
     * Return the location of the settings directory. If it does not exist, create one.
     *
     * @return Path to the settings directory
     * @throws IllegalStateException Will be thrown if settings directory don't exist and if there is
     *                               an error on creating it.
     */
    public static Path getSettingsDirectory() throws IllegalStateException {
        final Path baseDirectoryPath;
        if (ProgData.configDir == null || ProgData.configDir.isEmpty()) {
            baseDirectoryPath = Paths.get(System.getProperty("user.home"), ProgConst.VERZEICHNIS_EINSTELLUNGEN);
        } else {
            baseDirectoryPath = Paths.get(ProgData.configDir);
        }

        if (Files.notExists(baseDirectoryPath)) {
            try {
                Files.createDirectories(baseDirectoryPath);
            } catch (final IOException ioException) {
                Messages.logMessage(Messages.ERROR_CANT_CREATE_FOLDER, ioException, baseDirectoryPath.toString());
                throw new IllegalStateException(
                        Messages.ERROR_CANT_CREATE_FOLDER.getTextFormatted(baseDirectoryPath.toString()), ioException);
            }
        }

        return baseDirectoryPath;
    }

    public static String getSettingsDirectory_String() {
        return getSettingsDirectory().toString();
    }

}
