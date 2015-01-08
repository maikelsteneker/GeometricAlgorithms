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

    private int minx = Integer.MAX_VALUE, miny = Integer.MAX_VALUE, maxx = Integer.MIN_VALUE, maxy = Integer.MIN_VALUE;

    public DrawingUtilities(Iterable<Point> points) {
        for (Point p : points) {
            if (p.x < minx) {
                minx = p.x;
            }
            if (p.y < miny) {
                miny = p.y;
            }
            if (p.x > maxx) {
                maxx = p.x;
            }
            if (p.y > maxy) {
                maxy = p.y;
            }
        }
    }

    public Point scaled(Point p) {
        return scaled(p, minx, miny, maxx, maxy);
    }

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

    public Point invert(Point p) {
        return invert(p, miny, maxy);
    }

    public static Point invert(Point p, int miny, int maxy) {
        return new Point(p.x, -(p.y - maxy) + miny);
    }
}
