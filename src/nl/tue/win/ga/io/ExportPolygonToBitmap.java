package nl.tue.win.ga.io;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import nl.tue.win.ga.model.drawing.Drawable;
import nl.tue.win.ga.utilities.DrawingUtilities;

/**
 * Exports a polygon as an image.
 *
 * @author maikel
 */
public class ExportPolygonToBitmap {

    public static void exportPolygonToImage(String filename, Drawable polygon) throws IOException {
        File file = new File(filename);
        exportPolygonToImage(file, polygon);
    }

    public static void exportPolygonToImage(File file, Drawable polygon) throws IOException {
        exportPolygonToImage(file, polygon, "bmp");
    }

    public static void exportPolygonToImage(String filename, Drawable polygon, String format) throws IOException {
        File file = new File(filename);
        exportPolygonToImage(file, polygon, format);
    }

    public static void exportPolygonToImage(File file, Drawable polygon, String format) throws IOException {
        exportPolygonToImage(file, polygon, format, true);
    }

    public static void exportPolygonToImage(File file, Drawable polygon, String format, boolean invertY) throws IOException {
        BufferedImage image = new BufferedImage(DrawingUtilities.XRES, DrawingUtilities.YRES, BufferedImage.TYPE_INT_RGB);
        Graphics graphics = image.getGraphics();
        graphics.setColor(Color.WHITE);
        graphics.fillRect(0, 0, DrawingUtilities.XRES, DrawingUtilities.YRES);
        graphics.setColor(Color.BLACK);
        polygon.draw(graphics, true, invertY);
        ImageIO.write(image, format, file);
    }
}
