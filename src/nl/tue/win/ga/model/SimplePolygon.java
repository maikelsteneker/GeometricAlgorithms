package nl.tue.win.ga.model;

import java.awt.Graphics;
import java.awt.Point;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

/**
 * Represents a simple polygon.
 *
 * @author maikel
 */
public class SimplePolygon implements Iterable<Point> {

    final private Point[] hull;
    final static int XRES = 1024;
    final static int YRES = 768;

    public SimplePolygon() {
        this(new Point[0]);
    }

    public SimplePolygon(List<Point> hull) {
        this(hull.toArray(new Point[0]));
    }

    public SimplePolygon(Point[] hull) {
        this.hull = hull;
    }

    public int size() {
        return hull.length;
    }

    @Override
    public Iterator<Point> iterator() {
        return getHull().iterator();
    }

    public List<Point> getHull() {
        return Arrays.asList(hull);
    }

    public void draw(Graphics g, boolean scale) {
        int minx = Integer.MAX_VALUE, miny = Integer.MAX_VALUE, maxx = Integer.MIN_VALUE, maxy = Integer.MIN_VALUE;
        if (scale) {
            for (Point p : hull) {
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

        Point prev = size() > 0 ? hull[size() - 1] : null;

        for (Point p : hull) {
            final Point scaled;
            if (scale) {
                scaled = scaled(p, minx, miny, maxx, maxy);
            } else {
                scaled = p;
            }
            g.drawOval((int) scaled.getX(), (int) scaled.getY(), 3, 3);
            g.fillOval((int) scaled.getX(), (int) scaled.getY(), 3, 3);
            if (prev != null) {
                g.drawLine(prev.x, prev.y, scaled.x, scaled.y);
            }
            prev = scaled;
        }
    }

    public boolean invariant() {
        return true; // TODO: implement
    }

    public boolean isConvex() {
        // check if polygon is convex
        // we check this by computing the cross product between consecutive edges
        // these should all have the same sign
        boolean sign = false; // previous sign
        if (hull.length <= 2) {
            return true;
        }
        for (int i = 0; i < hull.length; i++) {
            int dx1 = hull[(i + 1) % hull.length].x - hull[i % hull.length].x;
            int dy1 = hull[(i + 1) % hull.length].y - hull[i % hull.length].y;
            int dx2 = hull[(i + 2) % hull.length].x - hull[(i + 1) % hull.length].x;
            int dy2 = hull[(i + 2) % hull.length].y - hull[(i + 1) % hull.length].y;
            int crossProduct = dx1 * dy2 - dy1 * dx2;
            if (i > 0) {
                if ((crossProduct < 0) != sign) {
                    return false;
                }
            } else {
                sign = (crossProduct < 0);
            }
        }
        return true;
    }

    private Point scaled(Point p, int minx, int miny, int maxx, int maxy) {
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
}
