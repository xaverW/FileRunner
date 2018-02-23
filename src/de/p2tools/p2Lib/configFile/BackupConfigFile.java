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


package de.p2tools.p2Lib.configFile;

import de.p2tools.fileRunner.gui.dialog.MTAlert;
import de.p2tools.p2Lib.tools.Log;
import de.p2tools.p2Lib.tools.PAlert;
import de.p2tools.p2Lib.tools.SysMsg;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;
import java.util.ArrayList;
import java.util.Calendar;

class BackupConfigFile {
    private static final String CONFIG_FILE_COPY_ADDON = "_copy_";

    private boolean alreadyMadeBackup = false;
    private final int maxCopyBackup;
    private final String backupFileName;
    private final Path filePath;

    BackupConfigFile(int maxCopyBackup, Path filePath) {
        this.maxCopyBackup = maxCopyBackup;
        this.filePath = filePath;
        this.backupFileName = filePath.getFileName().toString() + CONFIG_FILE_COPY_ADDON;
    }

    /**
     * Create backup copies of settings file.
     */
    void konfigCopy() {
        if (!alreadyMadeBackup) {
            // nur einmal pro Programmstart machen
            SysMsg.sysMsg("-------------------------------------------------------");
            SysMsg.sysMsg("Einstellungen sichern");

            try {
                long creatTime = -1;

                Path confFileCopy = filePath.getParent().resolve(backupFileName + 1);
                if (Files.exists(confFileCopy)) {
                    final BasicFileAttributes attrs = Files.readAttributes(confFileCopy, BasicFileAttributes.class);
                    final FileTime d = attrs.lastModifiedTime();
                    creatTime = d.toMillis();
                }

                if (creatTime == -1 || creatTime < getHeute_0Uhr()) {
                    // nur dann ist die letzte Kopie älter als einen Tag
                    for (int i = maxCopyBackup; i > 1; --i) {
                        confFileCopy = filePath.getParent().resolve(backupFileName + (i - 1));
                        final Path confFileCopy_2 = filePath.getParent().resolve(backupFileName + i);
                        if (Files.exists(confFileCopy)) {
                            Files.move(confFileCopy, confFileCopy_2, StandardCopyOption.REPLACE_EXISTING);
                        }
                    }
                    if (Files.exists(filePath)) {
                        Files.move(filePath,
                                filePath.getParent().resolve(backupFileName + 1),
                                StandardCopyOption.REPLACE_EXISTING);
                    }
                    SysMsg.sysMsg("Einstellungen wurden gesichert");
                } else {
                    SysMsg.sysMsg("Einstellungen wurden heute schon gesichert");
                }
            } catch (final IOException e) {
                SysMsg.sysMsg("Die Einstellungen konnten nicht komplett gesichert werden!");
                Log.errorLog(795623147, e);
            }

            alreadyMadeBackup = true;
            SysMsg.sysMsg("-------------------------------------------------------");
        }
    }

    boolean loadBackup(ArrayList<ConfigsList> configsListArr, ArrayList<ConfigsData> configsDataArr) {

        boolean ret = false;
        final ArrayList<Path> path = new ArrayList<>();
        getXmlCopyFilePath(path);
        if (path.isEmpty()) {
            SysMsg.sysMsg("Es gibt kein Backup");
            return false;
        }

        // dann gibts ein Backup
        SysMsg.sysMsg("Es gibt ein Backup");


        if (PAlert.BUTTON.YES != new MTAlert().showAlert_yes_no("Gesicherte Einstellungen laden?",
                "Die Einstellungen sind beschädigt\n" +
                        "und können nicht geladen werden.",
                "Soll versucht werden, mit gesicherten\n"
                        + "Einstellungen zu starten?\n\n"
                        + "(ansonsten startet das Programm mit\n"
                        + "Standardeinstellungen)")) {

            SysMsg.sysMsg("User will kein Backup laden.");
            return false;
        }


        for (final Path p : path) {
            // teils geladene Reste entfernen
            SysMsg.sysMsg(new String[]{"Versuch Backup zu laden:", p.toString()});
            if (new LoadConfigFile(p, configsListArr, configsDataArr).readConfiguration()) {
                SysMsg.sysMsg(new String[]{"Backup hat geklappt:", p.toString()});
                ret = true;
                break;
            }
        }

        return ret;
    }

    /**
     * Return the path to "p2tools.xml_copy_" first copy exists
     *
     * @param xmlFilePath Path to file.
     */
    private void getXmlCopyFilePath(ArrayList<Path> xmlFilePath) {
        for (int i = 1; i <= maxCopyBackup; ++i) {
            final Path path = filePath.getParent().resolve(backupFileName + i);
            if (Files.exists(path)) {
                xmlFilePath.add(path);
            }
        }
    }

    /**
     * Return the number of milliseconds from today´s midnight.
     *
     * @return Number of milliseconds from today´s midnight.
     */
    private long getHeute_0Uhr() {
        final Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);

        return cal.getTimeInMillis();
    }
}
