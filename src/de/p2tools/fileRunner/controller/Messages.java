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

package de.p2tools.fileRunner.controller;

import de.p2tools.p2Lib.tools.Log;

/**
 * A enum which contains log messages.
 */
public enum Messages {

    ERROR_CANT_CREATE_FOLDER(Types.ERROR,
            "Der Ordner \"%s\" konnte nicht angelegt werden.%n Bitte prüfen Sie die " + "Dateirechte."),
    ERROR_NO_JAVAFX_INSTALLED(Types.ERROR,
            "JavaFX wurde nicht im Klassenpfad gefunden. %n Stellen Sie sicher, dass Sie "
                    + "ein Java JRE ab Version 8 benutzen. %n Falls Sie Linux nutzen, installieren Sie das openjfx-Paket ihres "
                    + "Package-Managers,%n oder nutzen Sie eine eigene JRE-Installation.%n");

    private final Types messageType;
    private final String textPattern;
    private final Integer errorCode;

    Messages(final Types aMessageType, final String aTextPattern) {
        this(aMessageType, aTextPattern, null);
    }

    Messages(final Types aMessageType, final String aTextPattern, Integer aErrorCode) {
        messageType = aMessageType;
        textPattern = aTextPattern;
        errorCode = aErrorCode;
    }

    public Integer getErrorCode() {
        return errorCode;
    }


    public Types getMessageType() {
        return messageType;
    }

    public String getTextFormatted(Object... aFormattingArguments) {
        return String.format(textPattern, aFormattingArguments);
    }

    public String getText() {
        return textPattern;
    }

    public static void logMessage(final Messages aMessage,
                                  final Exception aException,
                                  final Object... aFormattingArguments) {
        final String message = aFormattingArguments == null ? aMessage.getText() : aMessage.getTextFormatted(aFormattingArguments);
        switch (aMessage.getMessageType()) {
            case ERROR:
                if (aException == null) {
                    Log.errorLog(aMessage.getErrorCode() == null ? 0 : aMessage.getErrorCode(), message);
                } else {
                    Log.errorLog(aMessage.getErrorCode() == null ? 0 : aMessage.getErrorCode(), aException, message);
                }
                break;
            case WARNING:
                Log.sysLog(aMessage.getMessageType().toString() + ": " + message);
                break;
            case INFO:
                Log.sysLog(message);
        }
    }

    public enum Types {
        ERROR, WARNING, INFO
    }
}
