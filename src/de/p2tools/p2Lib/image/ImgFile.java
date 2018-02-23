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


package de.p2tools.p2Lib.image;

import de.p2tools.p2Lib.tools.Log;

import javax.imageio.*;
import javax.imageio.plugins.jpeg.JPEGImageWriteParam;
import javax.imageio.stream.ImageInputStream;
import javax.imageio.stream.ImageOutputStream;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.File;
import java.util.Iterator;
import java.util.Locale;

public class ImgFile {

    public static final String IMAGE_FORMAT_JPG = "jpg";
    public static final String IMAGE_FORMAT_PNG = "png";

    public static final BufferedImage cloneImage(BufferedImage image) {
        BufferedImage clone = new BufferedImage(image.getWidth(),
                image.getHeight(), image.getType());
        Graphics2D g2d = clone.createGraphics();
        g2d.drawImage(image, 0, 0, null);
        g2d.dispose();
        return clone;
    }


    public static BufferedImage getBufferedImage(File source) {
        BufferedImage img = null;
        ImageReader reader = getReader(source);
        try {
            img = reader.read(0);
        } catch (Exception e) {
        }
        reader.dispose();
        return img;
    }

    public static RenderedImage getRenderedImage(File file) {
        RenderedImage img;
        ImageReader reader = getReader(file);
        try {
            img = reader.readAsRenderedImage(0, null);
        } catch (Exception e) {
            System.out.println(e.getMessage() + "BildArchiv_ - getRenderedImage");
            return null;
        }
        reader.dispose();
        return img;
    }

    private static ImageReader getReader(File source) {
        try {
            Iterator readers = ImageIO.getImageReadersByFormatName(ImgTools.fileType(source));
            ImageReader reader = (ImageReader) readers.next();
            ImageInputStream iis = ImageIO.createImageInputStream(source);
            reader.setInput(iis, true);
            return reader;
        } catch (Exception e) {
            System.out.println(e.getMessage() + "\n" + "Funktionen - getReader");
            return null;
        }
    }

    public static void writeImage(BufferedImage img, String dest, String suffix) {
        ImageOutputStream ios = null;
        ImageWriter writer = null;
        try {
            if (suffix.equals(IMAGE_FORMAT_PNG)) {
                writer = ImageIO.getImageWritersBySuffix(IMAGE_FORMAT_PNG).next();
                ios = ImageIO.createImageOutputStream(new File(dest));
                writer.setOutput(ios);

                writer.write(new IIOImage(img, null, null));
                ios.flush();

            } else {
                writer = ImageIO.getImageWritersBySuffix(IMAGE_FORMAT_JPG).next();
                ios = ImageIO.createImageOutputStream(new File(dest));
                writer.setOutput(ios);

                ImageWriteParam iwparam = new JPEGImageWriteParam(Locale.getDefault());
                iwparam.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
                iwparam.setCompressionQuality(1f);

                writer.write(null, new IIOImage(img, null, null), iwparam);
                ios.flush();
            }
        } catch (Exception e) {
            Log.errorLog(784520369, e, ImgFile.class.toString());
        } finally {
            try {
                ios.close();
                writer.dispose();
            } catch (Exception ex) {
            }
        }
    }

}
