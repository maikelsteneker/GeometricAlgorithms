package nl.tue.win.ga.utilities;

import java.awt.Point;

/**
 * Provides methods to help with drawing.
 *
 * @author maikel
 */
public class DrawingUtilities {

    final public static int XRES = 1024;
    final public static int YRES = 768;

    final private BoundingBox bb;
    final private int minx;
    final private int miny;
    final private int maxx;
    final private int maxy;

    public DrawingUtilities(Iterable<Point> points) {
        bb = new BoundingBox(points);
        this.minx = bb.getMinx();
        this.maxx = bb.getMaxx();
        this.miny = bb.getMiny();
        this.maxy = bb.getMaxy();
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
