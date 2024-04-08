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


package de.p2tools.filerunner.controller.worker;

import de.p2tools.filerunner.controller.config.ProgConfig;
import de.p2tools.filerunner.controller.config.ProgData;
import de.p2tools.filerunner.controller.data.filedata.FileDataList;
import de.p2tools.filerunner.controller.worker.gethash.*;
import de.p2tools.p2lib.alert.P2Alert;
import de.p2tools.p2lib.dialogs.P2DialogFileChooser;
import de.p2tools.p2lib.dialogs.P2DirFileChooser;
import de.p2tools.p2lib.hash.WriteHashFile;
import de.p2tools.p2lib.tools.net.PUrlTools;
import javafx.animation.Animation;
import javafx.animation.Interpolator;
import javafx.animation.Transition;
import javafx.beans.property.StringProperty;
import javafx.scene.control.Button;
import javafx.util.Duration;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Worker {
    private final ProgData progData;

    private final DirHash_CreateDirHash dirHashCreateDirHash_1;
    private final DirHash_CreateDirHash dirHashCreateDirHash_2;
    private final DirHash_CreateZipHash dirHashCreateZipHash_1;
    private final DirHash_CreateZipHash dirHashCreateZipHash_2;
    private final DirHash_ReadHashFile dirHashReadDirHashFile_1;
    private final DirHash_ReadHashFile dirHashReadDirHashFile_2;
    private final FileHash_CreateFileHashFile fileHashCreateFileHashFile;
    private final FileHash_ReadFileHashFile fileHashReadFileHashFile;

    public Worker(ProgData progData) {
        this.progData = progData;
        dirHashCreateDirHash_1 = new DirHash_CreateDirHash(progData, true);
        dirHashCreateDirHash_2 = new DirHash_CreateDirHash(progData, false);
        dirHashCreateZipHash_1 = new DirHash_CreateZipHash(progData, true);
        dirHashCreateZipHash_2 = new DirHash_CreateZipHash(progData, false);
        dirHashReadDirHashFile_1 = new DirHash_ReadHashFile(progData, true);
        dirHashReadDirHashFile_2 = new DirHash_ReadHashFile(progData, false);

        fileHashCreateFileHashFile = new FileHash_CreateFileHashFile();
        fileHashReadFileHashFile = new FileHash_ReadFileHashFile();
    }

    public void setStop() {
        dirHashCreateDirHash_1.setStop();
        dirHashCreateZipHash_1.setStop();
        dirHashReadDirHashFile_1.setStop();
        fileHashCreateFileHashFile.setStop();
        fileHashReadFileHashFile.setStop();
    }

    public boolean createHashIsRunning() {
        return dirHashCreateDirHash_1.isRunning() || dirHashCreateDirHash_2.isRunning() ||
                dirHashCreateZipHash_1.isRunning() || dirHashCreateZipHash_2.isRunning() ||
                dirHashReadDirHashFile_1.isRunning() || dirHashReadDirHashFile_2.isRunning();
    }

    public boolean dirHash_readDirHash(boolean list1) {
        //Hash von Dateien eines Verzeichnisses erstellen
        boolean ret = false;
        String searchDirStr = list1 ? ProgConfig.searchDir1.getValueSafe() : ProgConfig.searchDir2.getValueSafe();
        if (searchDirStr.isEmpty()) {
            return false;
        }

        File searchDir = new File(searchDirStr);
        if (!searchDir.exists()) {
            P2DialogFileChooser.showErrorAlert("Verzeichnis einlesen", "Verzeichnis existiert nicht!");
        } else {
            ret = true;
            if (list1) {
                dirHashCreateDirHash_1.createHash(1);
            } else {
                dirHashCreateDirHash_2.createHash(1);
            }
        }
        ProgData.getInstance().guiDirRunner.resetFilter();
        return ret;
    }

    public boolean dirHash_readZipHash(boolean list1) {
        //Hash von Dateien einer Zip-Datei erstellen
        boolean ret = false;

        String hashZip = list1 ? ProgConfig.searchZip1.getValueSafe() : ProgConfig.searchZip2.getValueSafe();
        if (hashZip.isEmpty()) {
            return false;
        }

        File zipFile = new File(hashZip);
        if (!zipFile.exists()) {
            P2DialogFileChooser.showErrorAlert("Zipdatei einlesen", "Die Zipdatei existiert nicht!");
        } else {
            ret = true;
            if (list1) {
                dirHashCreateZipHash_1.createHash(zipFile);
            } else {
                dirHashCreateZipHash_2.createHash(zipFile);
            }
        }
        ProgData.getInstance().guiDirRunner.resetFilter();
        return ret;
    }

    public boolean dirHash_readHashFile(boolean list1) {
        //Hash aus einer Hash-Datei einlesen
        boolean ret = false;

        String hashFile = list1 ? ProgConfig.searchHashFile1.getValueSafe() : ProgConfig.searchHashFile2.getValueSafe();
        if (hashFile.isEmpty()) {
            return false;
        }

        File file = new File(hashFile);
        if (!file.exists() || !file.isFile()) {
            P2DialogFileChooser.showErrorAlert("Hashdatei einlesen", "Die Hashdatei existiert nicht!");
        } else {
            ret = true;
            if (list1) {
                dirHashReadDirHashFile_1.readFile();
            } else {
                dirHashReadDirHashFile_2.readFile();
            }
        }
        ProgData.getInstance().guiDirRunner.resetFilter();
        return ret;
    }

    public void dirHash_writeHashFile(Button button, String fileStr, FileDataList fileDataList) {
        File file;
        if (fileStr.isEmpty()) {
            return;
        }

        file = new File(fileStr);
        if (file.exists()) {
            P2Alert.BUTTON btn = P2DialogFileChooser.showAlert_yes_no("Datei existiert bereits!", "Überschreiben",
                    "Hashdatei existiert bereits, überschreiben?");
            if (btn.equals(P2Alert.BUTTON.NO)) {
                return;
            }
        }
        if (DirHashFileFactory.writeDirHashFile(file, fileDataList)) {
            final Animation animation = new Transition() {
                {
                    setCycleDuration(Duration.millis(2000));
                    setInterpolator(Interpolator.EASE_OUT);
                }

                @Override
                protected void interpolate(double frac) {
                    button.setStyle("-fx-border-color: green;");
                    if (frac == 1) {
                        button.setStyle(null);
                    }
                }
            };
            animation.play();
        }
    }

    public void fileHash_readHashFile(String file, StringProperty stringProperty) {
        //Beim Dateivergleich: Hash aus md5, .. Datei einlesen
        fileHashReadFileHashFile.readHash(file, stringProperty);
    }

    public void fileHash_createHashFile(String file, StringProperty stringProperty) {
        fileHashCreateFileHashFile.genHash(file, stringProperty);
    }

    public void fileHash_saveHashFile(String writeHash, String hashFromFile, String hash) {
        if (writeHash.isEmpty()) {
            return;
        }

        Path srcFile = Paths.get(hashFromFile); // ist die Datei, aus der der md5 generiert wird
        String initDirStr = ""; // ist der Vorschlag: Ordner
        String srcFileStr = ""; // wird in die md5-Datei geschrieben

        if (!HashFactory.checkFile(hashFromFile)) {
            srcFileStr = hashFromFile;
        } else {
            initDirStr = srcFile.getParent().toString();
            srcFileStr = srcFile.getFileName().toString();
        }

        if (PUrlTools.isUrl(hashFromFile)) {
            initDirStr = ""; // bei URLs gibts keinen Pfad
        }

        String md5FileStr = writeHash;
        md5FileStr = P2DirFileChooser.FileChooserSave(ProgData.getInstance().primaryStage, initDirStr, md5FileStr).trim();
        if (md5FileStr == null || md5FileStr.isEmpty()) {
            return;
        }

        File md5File = new File(md5FileStr);
        WriteHashFile.writeFileHash(md5File, srcFileStr, hash);
    }
}
