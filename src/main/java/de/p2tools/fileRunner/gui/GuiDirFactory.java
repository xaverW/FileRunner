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


package de.p2tools.fileRunner.gui;

import de.p2tools.fileRunner.controller.config.ProgData;
import de.p2tools.fileRunner.controller.data.fileData.FileDataList;
import de.p2tools.p2Lib.alert.PAlert;
import de.p2tools.p2Lib.dialog.PDialogFileChosser;
import javafx.animation.Animation;
import javafx.animation.Interpolator;
import javafx.animation.Transition;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;
import javafx.util.Duration;

import java.io.File;

public class GuiDirFactory {
    private final ProgData progData;

    public GuiDirFactory(ProgData progData) {
        this.progData = progData;
    }

    public boolean readDirHash(String hashDir, FileDataList fileDataList, boolean followLink) {
        boolean ret = false;
        if (hashDir.isEmpty()) {
            return false;
        }

        File dir = new File(hashDir);
        if (!dir.exists()) {
            PDialogFileChosser.showErrorAlert("Verzeichnis einlesen", "Verzeichnis existiert nicht!");
        } else {
            ret = true;
            progData.worker.createDirHash(dir, fileDataList, 1, true, followLink);
            fileDataList.setSourceDir(hashDir);
        }

        return ret;
    }

    public boolean readZipHash(String hashZip, FileDataList fileDataList) {
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

    public boolean readHashFile(String hashFile, FileDataList fileDataList) {
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

    public void writeHashFile(Label lbl, String fileStr, FileDataList fileDataList) {
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

            lbl.setVisible(true);
            final Animation animation = new Transition() {
                {
                    setCycleDuration(Duration.millis(2000));
                    setInterpolator(Interpolator.EASE_OUT);
                }

                @Override
                protected void interpolate(double frac) {
                    Color vColor = Color.web("#90ee90", 1 - frac);
                    lbl.setBackground(new Background(new BackgroundFill(vColor, CornerRadii.EMPTY, Insets.EMPTY)));
                    if (frac == 1) {
                        lbl.setVisible(false);
                    }
                }
            };
            animation.play();
        }
    }
}
