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

package de.p2tools.p2Lib.tools;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class Duration {

    private static Date stopZeitStatic = new Date(System.currentTimeMillis());
    private static final DecimalFormat DF = new DecimalFormat("###,##0.00");
    private static int sum = 0;
    private static final ArrayList<Counter> COUNTER_LIST = new ArrayList<>();

    private static class Counter {

        String text;
        List<String> pingText = new ArrayList<>();
        int count;
        long time;
        Date pingTime;
        Date start;

        public Counter(String nr, int count) {
            text = nr;
            this.count = count;
            pingTime = new Date();
            start = new Date();
        }

        public void startCounter() {
            pingTime = new Date();
            start = new Date();
        }

        public void startPingTime() {
            pingTime = new Date();
        }
    }

    public static synchronized void counterStart(String text) {
        Counter cc = null;
        for (final Counter c : COUNTER_LIST) {
            if (c.text.equals(text)) {
                cc = c;
                break;
            }
        }
        if (cc == null) {
            COUNTER_LIST.add(new Counter(text, 0));
        } else {
            cc.pingText.clear();
            cc.startCounter();
        }
    }

    public static synchronized void counterPing(String text) {
        Counter cc = null;
        for (final Counter c : COUNTER_LIST) {
            if (c.text.equals(text)) {
                cc = c;
                break;
            }
        }
        if (cc != null) {
            try {
                final long time = Math.round(new Date().getTime() - cc.pingTime.getTime());
                cc.startPingTime();
                String extra = "  --> Ping Dauer: " + roundDuration(time);
                cc.pingText.add(extra);
            } catch (final Exception ex) {
            }
        }
    }

    public static synchronized void counterStop(String text) {
        String extra = "";
        Counter cc = null;
        for (final Counter c : COUNTER_LIST) {
            if (c.text.equals(text)) {
                cc = c;
                break;
            }
        }
        if (cc != null) {
            cc.count++;
            try {
                final long time = Math.round(new Date().getTime() - cc.start.getTime());
                cc.time += time;
                extra = cc.text + " Anzahl: " + cc.count + "   Dauer: " + roundDuration(time);
            } catch (final Exception ex) {
            }
        }
        staticPing(getClassName(), text, extra, cc.pingText);
    }

    public static synchronized void printCounter() {
        int max = 0;
        for (final Counter c : COUNTER_LIST) {
            if (c.text.length() > max) {
                max = c.text.length();
            }
        }
        max++;
        for (final Counter c : COUNTER_LIST) {
            while (c.text.length() < max) {
                c.text = c.text + " ";
            }
        }

        System.out.println("");
        System.out.println("");
        System.out.println("#################################################################");
        for (final Counter c : COUNTER_LIST) {
            System.out.println(c.text + " Anzahl: " + c.count + "   Gesamtdauer: " + roundDuration(c.time));
        }
        System.out.println("#################################################################");
        System.out.println("");
    }

    public synchronized static void staticPing(String text) {
        final Throwable t = new Throwable();
        final StackTraceElement methodCaller = t.getStackTrace()[2];
        final String klasse = methodCaller.getClassName() + "." + methodCaller.getMethodName();
        String kl;
        try {
            kl = klasse;
            while (kl.contains(".")) {
                if (Character.isUpperCase(kl.charAt(0))) {
                    break;
                } else {
                    kl = kl.substring(kl.indexOf(".") + 1);
                }
            }
        } catch (final Exception ignored) {
            kl = klasse;
        }
        staticPing(kl, text, "", null);
    }

    private static String getClassName() {
        final Throwable t = new Throwable();
        final StackTraceElement methodCaller = t.getStackTrace()[2];
        final String klasse = methodCaller.getClassName() + "." + methodCaller.getMethodName();
        String kl;
        try {
            kl = klasse;
            while (kl.contains(".")) {
                if (Character.isUpperCase(kl.charAt(0))) {
                    break;
                } else {
                    kl = kl.substring(kl.indexOf(".") + 1);
                }
            }
        } catch (final Exception ignored) {
            kl = klasse;
        }
        return kl;
    }

    private static void staticPing(String klasse, String text, String extra, List pingText) {
        final Date now = new Date(System.currentTimeMillis());
        long sekunden;
        try {
            sekunden = Math.round(now.getTime() - stopZeitStatic.getTime());
        } catch (final Exception ex) {
            sekunden = -1;
        }
        System.out.println("");
        System.out.println("========== ========== ========== ========== ==========");
        System.out.println("DURATION " + sum++ + ":  " + text + "  [" + roundDuration(sekunden) + "]");
        System.out.println("   Klasse:  " + klasse);
        if (pingText != null && !pingText.isEmpty()) {
            pingText.stream().forEach(s -> System.out.println(s));
        }
        if (!extra.isEmpty()) {
            System.out.println("   " + extra);
        }
        System.out.println("========== ========== ========== ========== ==========");
        System.out.println("");

        stopZeitStatic = now;
    }

    public static String roundDuration(long s) {
        String ret;
        if (s > 1_000.0) {
            ret = DF.format(s / 1_000.0) + " s";
        } else {
            ret = DF.format(s) + " ms";
        }

        return ret;
    }

}
