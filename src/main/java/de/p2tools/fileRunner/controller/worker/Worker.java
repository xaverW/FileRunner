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


package de.p2tools.fileRunner.controller.worker;

import de.p2tools.fileRunner.controller.config.ProgData;
import de.p2tools.fileRunner.controller.data.fileData.FileDataList;
import de.p2tools.fileRunner.controller.worker.GetHash.*;
import de.p2tools.p2Lib.alert.PAlert;
import de.p2tools.p2Lib.dialogs.PDialogFileChosser;
import de.p2tools.p2Lib.dialogs.PDirFileChooser;
import de.p2tools.p2Lib.hash.WriteHashFile;
import de.p2tools.p2Lib.tools.net.PUrlTools;
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

    private final DirHash_CreateDirHash dirHashCreateDirHash;
    private final DirHash_CreateZipHash dirHashCreateZipHash;
    private final DirHash_ReadHashFile dirHashReadDirHashFile;
    private final FileHash_CreateFileHashFile fileHashCreateFileHashFile;
    private final FileHash_ReadFileHashFile fileHashReadFileHashFile;

    public Worker(ProgData progData) {
        this.progData = progData;
        dirHashCreateDirHash = new DirHash_CreateDirHash(progData);
        dirHashCreateZipHash = new DirHash_CreateZipHash(progData);
        dirHashReadDirHashFile = new DirHash_ReadHashFile(progData);
        fileHashCreateFileHashFile = new FileHash_CreateFileHashFile();
        fileHashReadFileHashFile = new FileHash_ReadFileHashFile();
    }

    public void setStop() {
        dirHashCreateDirHash.setStop();
        dirHashCreateZipHash.setStop();
        dirHashReadDirHashFile.setStop();
        fileHashCreateFileHashFile.setStop();
        fileHashReadFileHashFile.setStop();
    }

    public boolean dirHash_readDirHash(String hashDir, FileDataList fileDataList, boolean followLink) {
        //Hash von Dateien eines Verzeichnisses erstellen
        ProgData progData = ProgData.getInstance();
        boolean ret = false;
        if (hashDir.isEmpty()) {
            return false;
        }

        File dir = new File(hashDir);
        if (!dir.exists()) {
            PDialogFileChosser.showErrorAlert("Verzeichnis einlesen", "Verzeichnis existiert nicht!");
        } else {
            ret = true;
            dirHashCreateDirHash.createHash(dir, fileDataList, 1, followLink);
            fileDataList.setSourceDir(hashDir);
        }

        ProgData.getInstance().guiDirRunner.resetFilter();
        return ret;
    }

    public boolean dirHash_readZipHash(String hashZip, FileDataList fileDataList) {
        //Hash von Dateien einer Zip-Datei erstellen
        ProgData progData = ProgData.getInstance();
        boolean ret = false;
        if (hashZip.isEmpty()) {
            return false;
        }

        File zipFile = new File(hashZip);
        if (!zipFile.exists()) {
            PDialogFileChosser.showErrorAlert("Zipdatei einlesen", "Die Zipdatei existiert nicht!");
        } else {
            ret = true;
            dirHashCreateZipHash.createHash(zipFile, fileDataList);
            fileDataList.setSourceDir(hashZip);
        }

        ProgData.getInstance().guiDirRunner.resetFilter();
        return ret;
    }

    public boolean dirHash_readHashFile(String hashFile, FileDataList fileDataList) {
        //Hash aus einer Hash-Datei einlesen
        ProgData progData = ProgData.getInstance();
        boolean ret = false;
        if (hashFile.isEmpty()) {
            return false;
        }

        File file = new File(hashFile);
        if (!file.exists() || !file.isFile()) {
            PDialogFileChosser.showErrorAlert("Hashdatei einlesen", "Die Hashdatei existiert nicht!");
        } else {
            ret = true;
            dirHashReadDirHashFile.readFile(file, fileDataList);
            fileDataList.setSourceDir(hashFile);
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
            PAlert.BUTTON btn = PDialogFileChosser.showAlert_yes_no("Datei existiert bereits!", "Überschreiben",
                    "Hashdatei existiert bereits, überschreiben?");
            if (btn.equals(PAlert.BUTTON.NO)) {
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
        md5FileStr = PDirFileChooser.FileChooserSave(ProgData.getInstance().primaryStage, initDirStr, md5FileStr).trim();
        if (md5FileStr == null || md5FileStr.isEmpty()) {
            return;
        }

        File md5File = new File(md5FileStr);
        WriteHashFile.writeFileHash(md5File, srcFileStr, hash);
    }
}
