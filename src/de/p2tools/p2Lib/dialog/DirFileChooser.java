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

package de.p2tools.p2Lib.dialog;

import de.p2tools.p2Lib.tools.Log;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

public class DirFileChooser {

    public static String FileChooserSave(Stage stage, String initDirStr, String initFileStr) {
        String ret = "";
        File initDir;
        final FileChooser fileChooser = new FileChooser();

        if (initDirStr.isEmpty()) {
            initDir = new File(System.getProperty("user.home"));
        } else {
            initDir = new File(initDirStr);
        }
        fileChooser.setInitialDirectory(initDir);

        if (!initFileStr.isEmpty()) {
            fileChooser.setInitialFileName(initFileStr);
        }

        File selectedFile = fileChooser.showSaveDialog(stage);
        if (selectedFile != null) {
            try {
                ret = selectedFile.getAbsolutePath();
            } catch (final Exception ex) {
                Log.errorLog(912030654, ex);
            }
        }

        return ret;
    }

    public static void FileChooser(Stage stage, TextField txtFile) {
        final FileChooser fileChooser = new FileChooser();

        File initFile = new File(System.getProperty("user.home"));

        if (!txtFile.getText().isEmpty()) {
            File f = new File(txtFile.getText());
            if (f.exists() && f.isDirectory()) {
                initFile = f;
            } else if (f.exists() && f.isFile()) {
                initFile = f.getParentFile();
            }
        }

        fileChooser.setInitialDirectory(initFile);

        File selectedFile = fileChooser.showOpenDialog(stage);
        if (selectedFile != null) {
            txtFile.setText(selectedFile.getAbsolutePath());
        }

    }


    public static String FileChooser(Stage stage, ComboBox<String> cbPath) {
        String ret = "";

        final FileChooser fileChooser = new FileChooser();
        File initFile = new File(System.getProperty("user.home"));

        if (cbPath.getSelectionModel().getSelectedItem() != null &&
                !cbPath.getSelectionModel().getSelectedItem().isEmpty()) {
            Path path = Paths.get(cbPath.getSelectionModel().getSelectedItem());
            if (path.toFile().exists() && path.toFile().isDirectory()) {
                initFile = path.toFile();
            } else if (path.toFile().exists() && path.getParent().toFile().isDirectory()) {
                initFile = path.getParent().toFile();
            }
        }

        fileChooser.setInitialDirectory(initFile);
        File selectedFile = fileChooser.showOpenDialog(stage);
        if (selectedFile != null) {
            try {
                ret = selectedFile.getAbsolutePath();
                if (!cbPath.getItems().contains(ret)) {
                    cbPath.getItems().add(ret);
                }
                cbPath.getSelectionModel().select(ret);

            } catch (final Exception ex) {
                Log.errorLog(912030201, ex);
            }
        }

        return ret;
    }

    public static String DirChooser(Stage stage, String txtPath) {
        final DirectoryChooser directoryChooser = new DirectoryChooser();
        File initFile = new File(System.getProperty("user.home"));

        if (!txtPath.isEmpty()) {
            File f = new File(txtPath);
            if (f.exists() && f.isDirectory()) {
                initFile = new File(txtPath);
            }
        }
        directoryChooser.setInitialDirectory(initFile);
        File selectedFile = directoryChooser.showDialog(stage);
        if (selectedFile != null) {
            return selectedFile.getAbsolutePath();
        }
        return "";
    }

    public static String DirChooser(Stage stage, TextField txtPath) {
        String ret = "";
        final DirectoryChooser directoryChooser = new DirectoryChooser();
        File initFile = new File(System.getProperty("user.home"));

        if (!txtPath.getText().isEmpty()) {
            File f = new File(txtPath.getText());
            if (f.exists() && f.isDirectory()) {
                initFile = new File(txtPath.getText());
            }
        }
        directoryChooser.setInitialDirectory(initFile);
        File selectedFile = directoryChooser.showDialog(stage);
        if (selectedFile != null) {
            ret = selectedFile.getAbsolutePath();
            txtPath.setText(selectedFile.getAbsolutePath());
        }
        return ret;
    }

    public static String DirChooser(Stage stage, ComboBox<String> cbPath) {
        String ret = "";
        final DirectoryChooser directoryChooser = new DirectoryChooser();
        File initFile = new File(System.getProperty("user.home"));

        if (cbPath.getSelectionModel().getSelectedItem() != null &&
                !cbPath.getSelectionModel().getSelectedItem().isEmpty()) {
            File f = new File(cbPath.getSelectionModel().getSelectedItem());
            if (f.exists() && f.isDirectory()) {
                initFile = new File(cbPath.getSelectionModel().getSelectedItem());
            }
        }

        directoryChooser.setInitialDirectory(initFile);
        File selectedDir = directoryChooser.showDialog(stage);
        if (selectedDir != null) {
            try {
                ret = selectedDir.getAbsolutePath();
                if (!cbPath.getItems().contains(ret)) {
                    cbPath.getItems().add(ret);
                }
                cbPath.getSelectionModel().select(ret);

            } catch (final Exception ex) {
                Log.errorLog(912365478, ex);
            }
        }
        return ret;
    }

}