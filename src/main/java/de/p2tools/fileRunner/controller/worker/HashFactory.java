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
import de.p2tools.p2Lib.alert.PAlert;
import de.p2tools.p2Lib.dialogs.PDialogFileChosser;
import de.p2tools.p2Lib.tools.net.PUrlTools;
import javafx.animation.Animation;
import javafx.animation.Interpolator;
import javafx.animation.Transition;
import javafx.scene.control.Button;
import javafx.util.Duration;

import java.io.File;

public class HashFactory {

    private HashFactory() {
    }

    public static boolean readDirHash(String hashDir, FileDataList fileDataList, boolean followLink) {
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
            progData.worker.createDirHash(dir, fileDataList, 1, followLink);
            fileDataList.setSourceDir(hashDir);
        }

        return ret;
    }

    public static boolean readZipHash(String hashZip, FileDataList fileDataList) {
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
            progData.worker.createZipHash(zipFile, fileDataList);
            fileDataList.setSourceDir(hashZip);
        }

        return ret;
    }

    public static boolean readHashFile(String hashFile, FileDataList fileDataList) {
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
            progData.worker.readDirHashFile(file, fileDataList);
            fileDataList.setSourceDir(hashFile);
        }

        return ret;
    }

    public static void writeHashFile(Button button, String fileStr, FileDataList fileDataList) {
        ProgData progData = ProgData.getInstance();
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
        if (progData.worker.writeDirHashFile(file, fileDataList)) {
            final Animation animation = new Transition() {
                {
                    setCycleDuration(Duration.millis(2000));
                    setInterpolator(Interpolator.EASE_OUT);
                }

                @Override
                protected void interpolate(double frac) {
//                    Color vColor = Color.web("#90ee90", 1 - frac);
//                    button.setBackground(new Background(new BackgroundFill(vColor, CornerRadii.EMPTY, Insets.EMPTY)));
                    button.setStyle("-fx-border-color: green;");
                    if (frac == 1) {
//                        button.setBackground(new Button().getBackground());
                        button.setStyle("-fx-border-color: ;");
                    }
                }
            };
            animation.play();
        }
    }

    public static boolean checkFile(String file) {
        if (PUrlTools.isUrl(file) && PUrlTools.urlExists(file) || new File(file).exists()) {
            return true;
        }
        if (PUrlTools.isUrl(file)) {
            PAlert.showErrorAlert("Hash erstellen", "Die angegebene URL existiert nicht!");
        } else {
            PAlert.showErrorAlert("Hash erstellen", "Die angegebene Datei existiert nicht!");
        }
        return false;
    }
}
