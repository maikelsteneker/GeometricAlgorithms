package nl.tue.win.ga.model;

import nl.tue.win.ga.model.drawing.Drawable;
import java.awt.Graphics;
import java.awt.Point;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import nl.tue.win.ga.utilities.DrawingUtilities;

/**
 * Represents a simple polygon.
 *
 * @author maikel
 */
public class SimplePolygon implements Iterable<Point>, Drawable {

    final private Point[] hull;
    final public DrawingUtilities drawingUtilities;

    public SimplePolygon() {
        this(new Point[0]);
    }

    public SimplePolygon(List<Point> hull) {
        this(hull.toArray(new Point[0]));
    }

    public SimplePolygon(Point[] hull) {
        this.hull = hull;
        drawingUtilities = new DrawingUtilities(this);
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

    @Override
    public void draw(Graphics g, boolean scale, boolean invertY) {
        Point prev = size() > 0 ? hull[size() - 1] : null;
        if (prev != null) {
            if (invertY) {
                prev = drawingUtilities.invert(prev);
            }
            if (scale) {
                prev = drawingUtilities.scaled(prev);
            }
            prev = drawingUtilities.zoom(prev);
        }

        for (Point p : hull) {
            Point transformed = p;
            if (invertY) {
                transformed = drawingUtilities.invert(transformed);
            }
            if (scale) {
                transformed = drawingUtilities.scaled(transformed);
            }
            transformed = drawingUtilities.zoom(transformed);
            g.drawOval(transformed.x - 1, transformed.y - 1, 3, 3);
            g.fillOval(transformed.x - 1, transformed.y - 1, 3, 3);
            if (prev != null) {
                g.drawLine(prev.x, prev.y, transformed.x, transformed.y);
            }
            prev = transformed;
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
}
