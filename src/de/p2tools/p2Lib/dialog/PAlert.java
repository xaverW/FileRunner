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

import javafx.scene.control.*;
import javafx.scene.layout.Region;
import javafx.scene.text.TextFlow;

import java.util.Optional;

public class PAlert {

    public enum BUTTON {UNKNOWN, YES, NO, CANCEL}


    public static boolean showAlert(String title, String header, String content) {
        final Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);

        alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);

        final Optional<ButtonType> bt = alert.showAndWait();
        if (bt.isPresent() && bt.get() == ButtonType.OK) {
            return true;
        }
        return false;
    }

    public static BUTTON showAlert_yes_no(String title, String header, String content) {
        final Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);

        alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);

        ButtonType btnYes = new ButtonType("Ja", ButtonBar.ButtonData.YES);
        ButtonType btnNo = new ButtonType("Nein", ButtonBar.ButtonData.NO);

        alert.getButtonTypes().setAll(btnYes, btnNo);
        ((Button) alert.getDialogPane().lookupButton(btnYes)).setDefaultButton(true);
        ((Button) alert.getDialogPane().lookupButton(btnNo)).setDefaultButton(false);

        BUTTON ret = BUTTON.NO;
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == btnYes) {
            ret = BUTTON.YES;
        } else if (result.get() == btnNo) {
            ret = BUTTON.NO;
        }

        return ret;
    }


    public static BUTTON showAlert_yes_no_cancel(String title, String header, String content) {
        return showAlert_yes_no_cancel(title, header, content, true);
    }

    public static BUTTON showAlert_yes_no_cancel(String title, String header, String content, boolean noBtn) {
        final Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);

        alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);

        ButtonType btnYes = new ButtonType("Ja", ButtonBar.ButtonData.YES);
        ButtonType btnNo = new ButtonType("Nein", ButtonBar.ButtonData.NO);
        ButtonType btnCancel = new ButtonType("Abbrechen", ButtonBar.ButtonData.CANCEL_CLOSE);

        if (noBtn) {
            alert.getButtonTypes().setAll(btnYes, btnNo, btnCancel);
        } else {
            alert.getButtonTypes().setAll(btnYes, btnCancel);
        }

        ((Button) alert.getDialogPane().lookupButton(btnYes)).setDefaultButton(false);
        ((Button) alert.getDialogPane().lookupButton(btnCancel)).setDefaultButton(true);

        BUTTON ret = BUTTON.CANCEL;
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == btnYes) {
            ret = BUTTON.YES;
        } else if (result.get() == btnNo) {
            ret = BUTTON.NO;
        } else if (result.get() == btnCancel) {
            ret = BUTTON.CANCEL;
        }

        return ret;
    }

    public static BUTTON showAlert_yes_no_cancel(String title, String header, TextFlow content, boolean noBtn) {
        final Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.getDialogPane().setContent(content);

        alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);

        ButtonType btnYes = new ButtonType("Ja", ButtonBar.ButtonData.YES);
        ButtonType btnNo = new ButtonType("Nein", ButtonBar.ButtonData.NO);
        ButtonType btnCancel = new ButtonType("Abbrechen", ButtonBar.ButtonData.CANCEL_CLOSE);

        if (noBtn) {
            alert.getButtonTypes().setAll(btnYes, btnNo, btnCancel);
        } else {
            alert.getButtonTypes().setAll(btnYes, btnCancel);
        }

        ((Button) alert.getDialogPane().lookupButton(btnYes)).setDefaultButton(false);
        ((Button) alert.getDialogPane().lookupButton(btnCancel)).setDefaultButton(true);

        BUTTON ret = BUTTON.CANCEL;
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == btnYes) {
            ret = BUTTON.YES;
        } else if (result.get() == btnNo) {
            ret = BUTTON.NO;
        } else if (result.get() == btnCancel) {
            ret = BUTTON.CANCEL;
        }

        return ret;
    }


    public static boolean showHelpAlert(String header, TextFlow content) {
        final Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Hilfe");
        alert.setHeaderText(header);

        alert.getDialogPane().setContent(content);

        final Optional<ButtonType> bt = alert.showAndWait();
        if (bt.isPresent() && bt.get() == ButtonType.OK) {
            return true;
        }
        return false;
    }

    public static boolean showHelpAlert(String header, String content) {
        final Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Hilfe");
        alert.setHeaderText(header);

        ScrollPane scroll = new ScrollPane();
        TextArea ta = new TextArea(content);
        ta.setEditable(false);
        ta.setWrapText(true);
        scroll.setContent(ta);
        scroll.setFitToHeight(true);
        scroll.setFitToWidth(true);
        alert.getDialogPane().setContent(scroll);
        alert.setResizable(true);

        alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);

        final Optional<ButtonType> bt = alert.showAndWait();
        if (bt.isPresent() && bt.get() == ButtonType.OK) {
            return true;
        }
        return false;
    }

    public static boolean showInfoAlert(String title, String header, String content) {
        final Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(header);

        ScrollPane scroll = new ScrollPane();
        TextArea ta = new TextArea(content);
        ta.setEditable(false);
        scroll.setContent(ta);
        scroll.setFitToHeight(true);
        scroll.setFitToWidth(true);
        alert.getDialogPane().setContent(scroll);
        alert.setResizable(true);

        alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);

        final Optional<ButtonType> bt = alert.showAndWait();
        if (bt.isPresent() && bt.get() == ButtonType.OK) {
            return true;
        }
        return false;
    }

    public static boolean showInfoAlert(String title, String header, String content, boolean txtArea) {
        final Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(header);

        if (txtArea) {
            ScrollPane scroll = new ScrollPane();
            scroll.setContent(new TextArea(content));
            scroll.setFitToHeight(true);
            scroll.setFitToWidth(true);
            alert.getDialogPane().setContent(scroll);
            alert.setResizable(true);
        } else {
            alert.setContentText(content);
        }
        alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);

        final Optional<ButtonType> bt = alert.showAndWait();
        if (bt.isPresent() && bt.get() == ButtonType.OK) {
            return true;
        }
        return false;
    }

    public static boolean showErrorAlert(String header, String content) {
        return showErrorAlert("Fehler", header, content);
    }

    public static boolean showErrorAlert(String title, String header, String content) {
        final Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);

        alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);

        final Optional<ButtonType> bt = alert.showAndWait();
        if (bt.isPresent() && bt.get() == ButtonType.OK) {
            return true;
        }
        return false;
    }

    public static boolean showInfoNoSelection() {
        final Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("keine Auswahl");
        alert.setHeaderText("Es wurden nichts markiert.");
        alert.setContentText("Zeile auswählen!");

        alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);

        final Optional<ButtonType> bt = alert.showAndWait();
        if (bt.isPresent() && bt.get() == ButtonType.OK) {
            return true;
        }
        return false;
    }

}
