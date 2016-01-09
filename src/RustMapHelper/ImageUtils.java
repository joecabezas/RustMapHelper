/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package RustMapHelper;

import static RustMapHelper.MainApplication.TRANSPARENT_COLOR;
import java.io.File;
import static java.lang.Math.sqrt;
import java.util.EnumSet;
import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.input.Clipboard;
import javafx.scene.paint.Color;

/**
 *
 * @author joeca_000
 */
public final class ImageUtils {

    public enum FLAGS {

        TRANSPARENT_IMAGE,
        REMOVE_ALPHA_LABEL,
        TRIM_IMAGE,
        NEW_IMAGE
    }

    public static Image createImage(String image_path, EnumSet<FLAGS> enum_set) {
        File file = new File(image_path);
        return CreateImage(file, enum_set);
    }

    public static Image CreateImage(File file, EnumSet<FLAGS> enum_set) {
        Image image = new Image(file.toURI().toString());
        image = ApplyImageTransformations(image, enum_set);
        return image;
    }

    public static Image createImageFromClipboard(EnumSet<FLAGS> enum_set) {
        Image image_clipboard = Clipboard.getSystemClipboard().getImage();
        if (image_clipboard != null) {
            System.out.println("image_clipboard:" + image_clipboard);
            image_clipboard = ApplyImageTransformations(image_clipboard, enum_set);
        }
        return image_clipboard;

    }

    public static Image ApplyImageTransformations(Image image, EnumSet<FLAGS> enum_set) {
        if (enum_set.contains(FLAGS.REMOVE_ALPHA_LABEL)) {
            image = removeAlphaLabel(image);
        }
        if (enum_set.contains(FLAGS.TRANSPARENT_IMAGE)) {
            image = crateTransparentImage(image);
        }
        if (enum_set.contains(FLAGS.TRIM_IMAGE)) {
            image = trimImage(image);
        }
        if (enum_set.contains(FLAGS.NEW_IMAGE)) {
            image = colorizeImage(image);
        }

        return image;
    }

    public static Image removeAlphaLabel(Image image) {
        // Obtain PixelReader
        PixelReader pixelReader = image.getPixelReader();
        int w = (int) image.getWidth();
        int h = (int) image.getHeight();

        // Create WritableImage
        WritableImage wImage = new WritableImage(pixelReader, w, h);
        PixelWriter pixelWriter = wImage.getPixelWriter();

        // Determine the color of each pixel in a specified row
        for (int readY = 0; readY < 150; readY++) {
            for (int readX = w - 150; readX < w; readX++) {
                pixelWriter.setColor(readX, readY, TRANSPARENT_COLOR);
            }
        }
        return wImage;
    }

    public static Image crateTransparentImage(Image image) {
        // Obtain PixelReader
        PixelReader pixelReader = image.getPixelReader();
        int w = (int) image.getWidth();
        int h = (int) image.getHeight();

        Color transparent_color;
        transparent_color = Color.rgb(40, 40, 35);

        // Create WritableImage
        WritableImage wImage = new WritableImage(w, h);
        PixelWriter pixelWriter = wImage.getPixelWriter();

        // Determine the color of each pixel in a specified row
        for (int readY = 0; readY < h; readY++) {
            for (int readX = 0; readX < w; readX++) {
                Color color = pixelReader.getColor(readX, readY);
                double color_distance = ColorDistance(transparent_color, color);
                if (color_distance < 50.0) {
                    color = TRANSPARENT_COLOR;
                }
                pixelWriter.setColor(readX, readY, color);
            }
        }
        return wImage;
    }

    public static double ColorDistance(Color c1, Color c2) {
        double c1r = c1.getRed() * 255;
        double c1g = c1.getGreen() * 255;
        double c1b = c1.getBlue() * 255;

        double c2r = c2.getRed() * 255;
        double c2g = c2.getGreen() * 255;
        double c2b = c2.getBlue() * 255;

        long rmean = ((long) c1r + (long) c2r) / 2;
        long r = (long) c1r - (long) c2r;
        long g = (long) c1g - (long) c2g;
        long b = (long) c1b - (long) c2b;
        return sqrt((((512 + rmean) * r * r) >> 8) + 4 * g * g + (((767 - rmean) * b * b) >> 8));
    }

    public static Image trimImage(Image image) {
        return trimImageByColor(image, TRANSPARENT_COLOR);
    }

    public static Image trimImageByColor(Image image, Color target_color) {
        // Obtain PixelReader
        PixelReader pixelReader = image.getPixelReader();

        int x = Integer.MAX_VALUE;
        int y = Integer.MAX_VALUE;
        int w = -1;
        int h = -1;

        // Determine the color of each pixel in a specified row
        for (int i = 0; i < image.getWidth(); i++) {
            for (int j = 0; j < image.getHeight(); j++) {
                Color color = pixelReader.getColor(i, j);
                if (!color.equals(target_color)) {
                    if (x > i) {
                        x = i;
                    }
                    if (y > j) {
                        y = j;
                    }
                    if (w < i) {
                        w = i;
                    }
                    if (h < j) {
                        h = j;
                    }
                }
            }
        }

        return cropImage(image, x, y, w - x, h - y);
    }

    public static Image cropImage(Image image, int x, int y, int width, int height) {
        return new WritableImage(image.getPixelReader(), x, y, width, height);
    }

    private static Image colorizeImage(Image image) {
        PixelReader pixelReader = image.getPixelReader();
        int w = (int) image.getWidth();
        int h = (int) image.getHeight();

        // Create WritableImage
        WritableImage wImage = new WritableImage(w, h);
        PixelWriter pixelWriter = wImage.getPixelWriter();

        double r = Color.CORNFLOWERBLUE.getRed() + TRANSPARENT_COLOR.getRed();
        double g = Color.CORNFLOWERBLUE.getGreen() + TRANSPARENT_COLOR.getGreen();
        double b = Color.CORNFLOWERBLUE.getBlue() + TRANSPARENT_COLOR.getBlue();
        Color new_transparent_color = new Color(r, g, b, 0.3);

        // Determine the color of each pixel in a specified row
        for (int i = 0; i < image.getWidth(); i++) {
            for (int j = 0; j < image.getHeight(); j++) {
                Color color = pixelReader.getColor(i, j);
                if (color.equals(TRANSPARENT_COLOR)) {
                    pixelWriter.setColor(i, j, new_transparent_color);
                } else {
                    color = new Color(color.getRed(), color.getGreen(), color.getBlue(), 0.5);
                    pixelWriter.setColor(i, j, color);
                }
            }
        }

        return wImage;
    }
}
