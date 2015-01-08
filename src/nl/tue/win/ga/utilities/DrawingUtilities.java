package nl.tue.win.ga.utilities;

import java.awt.Point;

/**
 * Provides classes to help with drawing.
 *
 * @author maikel
 */
public class DrawingUtilities {

    final public static int XRES = 1024;
    final public static int YRES = 768;

    public static Point scaled(Point p, int minx, int miny, int maxx, int maxy) {
        double x = p.x;
        x -= minx;
        x /= (maxx - minx);

        double y = p.y;
        y -= miny;
        y /= (maxy - miny);

        x *= XRES;
        y *= YRES;

        return new Point((int) x, (int) y);
    }

    public static Point invert(Point p, int miny, int maxy) {
        return new Point(p.x, -(p.y - maxy) + miny);
    }
}
