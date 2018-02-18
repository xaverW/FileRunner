/*
 * MTPlayer Copyright (C) 2017 W. Xaver W.Xaver[at]googlemail.com
 * https://sourceforge.net/projects/mtplayer/
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

package de.p2tools.p2Lib.tools;

import de.p2tools.p2Lib.PConst;
import org.apache.commons.lang3.SystemUtils;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;

public class Log {

    public static final String LILNE = "################################################################################";
    public static final Date startZeit = new Date(System.currentTimeMillis());

    private static String FEHLER = "Fehler(" + PConst.progName + "): ";
    private static final LinkedList<Error> fehlerListe = new LinkedList<>();
    private static final ArrayList<String> logList = new ArrayList<>();
    private static boolean progress = false;
    private static File logFile = null;

    private static class Error {
        String callClass = "";
        int errorNr = 0;
        int count = 0;
        boolean ex = false;

        public Error(int errorNr, String callClass, boolean ex) {
            this.errorNr = errorNr;
            this.callClass = callClass;
            this.ex = ex;
            this.count = 1;
        }

    }

    public static synchronized void setLogfile(String logFileString) {
        logFile = new File(logFileString);
        File dir = new File(logFile.getParent());
        if (!dir.exists()) {
            if (!dir.mkdirs()) {
                logFile = null;
                Log.errorLog(632012165, "Kann den Pfad nicht anlegen: " + dir.toString());
            }
        }

    }

    private static final SimpleDateFormat dateFormatter = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");

    private static final long TO_MEGABYTE = 1000L * 1000L;

    public static String LOGO = "\n" +
            "\n" +
            " ███╗   ███╗   ██████╗   ███████╗   █████╗   ██╗  ██╗  ██╗\n" +
            " ████╗ ████║  ██╔═══██╗  ██╔════╝  ██╔══██╗  ██║  ██║ ██╔╝\n" +
            " ██╔████╔██║  ██║   ██║  ███████╗  ███████║  ██║  █████╔╝ \n" +
            " ██║╚██╔╝██║  ██║   ██║  ╚════██║  ██╔══██║  ██║  ██╔═██╗ \n" +
            " ██║ ╚═╝ ██║  ╚██████╔╝  ███████║  ██║  ██║  ██║  ██║  ██╗\n" +
            " ╚═╝     ╚═╝   ╚═════╝   ╚══════╝  ╚═╝  ╚═╝  ╚═╝  ╚═╝  ╚═╝\n";

    public static void versionMsg(String progName) {
        if (!SystemUtils.IS_OS_MAC_OSX) {
            sysLog("");
            sysLog("");
            sysLog("");
            sysLog(LOGO);
            sysLog("");
            sysLog("");
        }

        sysLog(LILNE);
        sysLog("Programmstart: " + dateFormatter.format(Log.startZeit));
        sysLog(LILNE);
        sysLog("");
        final long totalMem = Runtime.getRuntime().totalMemory();
        sysLog("totalMemory: " + totalMem / TO_MEGABYTE + " MB");
        final long maxMem = Runtime.getRuntime().maxMemory();
        sysLog("maxMemory: " + maxMem / TO_MEGABYTE + " MB");
        final long freeMem = Runtime.getRuntime().freeMemory();
        sysLog("freeMemory: " + freeMem / TO_MEGABYTE + " MB");
        sysLog("");
        sysLog(LILNE);
        sysLog("");
        //Version
        sysLog(progName + Functions.getProgVersionString());
        String compile = Functions.getCompileDate();
        if (!compile.isEmpty()) {
            sysLog("Compiled: " + compile);
        }
        sysLog("");
        sysLog(LILNE);
        sysLog("");
        sysLog("Java");
        final String[] java = Functions.getJavaVersion();
        for (String ja : java) {
            sysLog(ja);
        }
        sysLog("");
    }

    public static void endMsg() {
        sysLog("");
        sysLog("");
        sysLog("");
        sysLog("");

        printErrorMsg().forEach(Log::sysLog);

        // Laufzeit ausgeben
        Date stopZeit = new Date(System.currentTimeMillis());
        int minuten;
        try {
            minuten = Math.round((stopZeit.getTime() - Log.startZeit.getTime()) / (1000 * 60));
        } catch (Exception ex) {
            minuten = -1;
        }
        sysLog("");
        sysLog("");
        sysLog(LILNE);
        sysLog("   --> Beginn: " + dateFormatter.format(Log.startZeit));
        sysLog("   --> Fertig: " + dateFormatter.format(stopZeit));
        sysLog("   --> Dauer[Min]: " + (minuten == 0 ? "<1" : minuten));
        sysLog(LILNE);
        sysLog("");
        sysLog("   und Tschuess");
        sysLog("");
        sysLog("");
        sysLog(LILNE);
    }

    public static synchronized ArrayList<String> printErrorMsg() {
        int max = 0;
        ArrayList<String> retList = new ArrayList<>();
        retList.add("");
        retList.add(LILNE);
        if (fehlerListe.isEmpty()) {
            retList.add(" Keine Fehler :)");
        } else {
            // Fehler ausgeben
            int i_1;
            int i_2;
            for (Error e : fehlerListe) {
                if (e.callClass.length() > max) {
                    max = e.callClass.length();
                }
            }
            max++;
            for (Error e : fehlerListe) {
                while (e.callClass.length() < max) {
                    e.callClass = e.callClass + ' ';
                }
            }
            for (int i = 1; i < fehlerListe.size(); ++i) {
                for (int k = i; k > 0; --k) {
                    i_1 = fehlerListe.get(k - 1).errorNr;
                    i_2 = fehlerListe.get(k).errorNr;
                    // if (str1.compareToIgnoreCase(str2) > 0) {
                    if (i_1 < i_2) {
                        fehlerListe.add(k - 1, fehlerListe.remove(k));
                    } else {
                        break;
                    }
                }
            }
            for (Error e : fehlerListe) {
                String strEx;
                if (e.ex) {
                    strEx = "Ex! ";
                } else {
                    strEx = "    ";
                }
                retList.add(strEx + e.callClass + " Fehlernummer: " + e.errorNr + " Anzahl: " + e.count);
            }
        }
        retList.add(LILNE);
        return retList;
    }

    // Fehlermeldung mit Exceptions
    public static synchronized void errorLog(int fehlerNummer, Exception ex) {
        fehlermeldung_(fehlerNummer, ex, new String[]{});
    }

    public static synchronized void errorLog(int fehlerNummer, Exception ex, String text) {
        fehlermeldung_(fehlerNummer, ex, new String[]{text});
    }

    public static synchronized void errorLog(int fehlerNummer, Exception ex, String text[]) {
        fehlermeldung_(fehlerNummer, ex, text);
    }

    // Fehlermeldungen
    public static synchronized void errorLog(int fehlerNummer, String text) {
        fehlermeldung_(fehlerNummer, null, new String[]{text});
    }

    public static synchronized void errorLog(int fehlerNummer, String[] text) {
        fehlermeldung_(fehlerNummer, null, text);
    }

    public static synchronized void sysLog(String text) {
        systemmeldung_(new String[]{text});
    }

    public static synchronized void progress(String texte) {
        progress = true;
        if (!texte.isEmpty()) {
            System.out.print(texte + '\r');
        }
    }

    private static void resetProgress() {
        // Leerzeile um die Progresszeile zu löschen
        if (progress) {
            System.out.print("                                                                                                             \r");
            progress = false;
        }
    }

    private static void addFehlerNummer(int nr, String classs, boolean exception) {
        for (Error e : fehlerListe) {
            if (e.errorNr == nr) {
                ++e.count;
                return;
            }
        }
        // dann gibts die Nummer noch nicht
        fehlerListe.add(new Error(nr, classs, exception));
    }

    private static void fehlermeldung_(int fehlerNummer, Exception ex, String[] texte) {
        final Throwable t = new Throwable();
        final StackTraceElement methodCaller = t.getStackTrace()[2];
        final String klasse = methodCaller.getClassName() + '.' + methodCaller.getMethodName();
        String kl;
        try {
            kl = klasse;
            while (kl.contains(".")) {
                if (Character.isUpperCase(kl.charAt(0))) {
                    break;
                } else {
                    kl = kl.substring(kl.indexOf('.') + 1);
                }
            }
        } catch (Exception ignored) {
            kl = klasse;
        }
        addFehlerNummer(fehlerNummer, kl, ex != null);
        if (ex != null || PConst.debug) {
            // Exceptions immer ausgeben
            resetProgress();
            String x, z;
            if (ex != null) {
                x = "!";
            } else {
                x = "=";
            }
            z = "*";
            logList.add(x + x + x + x + x + x + x + x + x + x
                    + x + x + x + x + x + x + x + x + x + x + x + x + x + x + x + x + x + x + x + x + x + x + x + x + x + x + x + x);

            try {
                // Stacktrace
                try (StringWriter sw = new StringWriter(); PrintWriter pw = new PrintWriter(sw)) {
                    if (ex != null) {
                        ex.printStackTrace(pw);
                    }
                    pw.flush();
                    sw.flush();
                    logList.add(sw.toString());
                }
            } catch (Exception ignored) {
            }

            logList.add(z + " Fehlernr: " + fehlerNummer);
            if (ex != null) {
                logList.add(z + " Exception: " + ex.getMessage());
            }
            logList.add(z + ' ' + FEHLER + kl);
            for (String aTexte : texte) {
                logList.add(z + "           " + aTexte);
            }
            logList.add("");
            printLog();
        }
    }

    private static void systemmeldung_(String[] texte) {
        resetProgress();
        final String z = ". ";
        if (texte.length <= 1) {
            logList.add(z + ' ' + texte[0]);
        } else {
            String zeile = "---------------------------------------";
            String txt;
            logList.add(z + zeile);
            for (String aTexte : texte) {
                txt = "| " + aTexte;
                logList.add(z + txt);
            }
            logList.add(z + zeile);
        }
        printLog();
    }

    private static void printLog() {
        logList.forEach(System.out::println);

        if (logFile != null) {
            try (OutputStreamWriter out = new OutputStreamWriter(new FileOutputStream(logFile, true), StandardCharsets.UTF_8)) {
                for (String s : logList) {
                    out.write(s);
                    out.write("\n");
                }
            } catch (Exception ex) {
                System.out.println(ex.getMessage());
            }
        }
        logList.clear();
    }
}
