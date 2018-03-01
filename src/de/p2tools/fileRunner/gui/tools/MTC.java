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


package de.p2tools.fileRunner.gui.tools;

import de.p2tools.p2Lib.tools.PConfigs;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.paint.Color;

public class MTC {

    private String cssFontBold = "";
    private String cssFont = "";
    private String cssBackground = "";

    private PConfigs mlConfigs = null;
    private String text = "";
    public Color colorReset = Color.WHITE;

    private final ObjectProperty<Color> color = new SimpleObjectProperty<>(this, "color", Color.WHITE);

    public MTC(PConfigs mlConfigs, Color color, String text) {
        this.setMlConfigs(mlConfigs);
        setText(text);
        setColor(color);
        setColorReset(color);
    }

    public Color getColor() {
        return color.get();
    }

    public void setColor(Color newColor) {
        color.set(newColor);
        cssFontBold = "-fx-font-weight: bold; -fx-text-fill: " + getColorToWeb() + ";";
        cssFont = "-fx-text-fill: " + getColorToWeb() + ";";
        cssBackground = "-fx-control-inner-background: " + getColorToWeb() + ";"
                + "-fx-control-inner-background-alt: derive(-fx-control-inner-background, 25%);";
    }

    public ObjectProperty<Color> colorProperty() {
        return color;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public PConfigs getMlConfigs() {
        return mlConfigs;
    }

    public void setMlConfigs(PConfigs mlConfigs) {
        this.mlConfigs = mlConfigs;
    }

    public Color getColorReset() {
        return colorReset;
    }

    public void setColorReset(Color colorReset) {
        this.colorReset = colorReset;
    }

    public void resetColor() {
        setColor(getColorReset());
    }

    public void setColorFromHex(String hex) {
        setColor(Color.web(hex));
    }

    public String getCssBackground() {
        return cssBackground;
    }

    public String getCssFont() {
        return cssFont;
    }

    public String getCssFontBold() {
        return cssFontBold;
    }

    public String getColorToWeb() {
        return "#" + getColorToHex(color.getValue());
    }

    public static String getColorToWeb(Color color) {
        return "#" + colorChanelToHex(color.getRed())
                + colorChanelToHex(color.getGreen())
                + colorChanelToHex(color.getBlue())
                + colorChanelToHex(color.getOpacity()
        );
    }

    public static String getColorToHex(Color color) {
        return colorChanelToHex(color.getRed())
                + colorChanelToHex(color.getGreen())
                + colorChanelToHex(color.getBlue())
                + colorChanelToHex(color.getOpacity()
        );
    }

    public String getColorToHex() {
        return colorChanelToHex(color.getValue().getRed())
                + colorChanelToHex(color.getValue().getGreen())
                + colorChanelToHex(color.getValue().getBlue())
                + colorChanelToHex(color.getValue().getOpacity()
        );
    }

    private static String colorChanelToHex(double chanelValue) {
        String rtn = Integer.toHexString((int) Math.min(Math.round(chanelValue * 255), 255));
        if (rtn.length() == 1) {
            rtn = "0" + rtn;
        }
        return rtn;
    }

}
