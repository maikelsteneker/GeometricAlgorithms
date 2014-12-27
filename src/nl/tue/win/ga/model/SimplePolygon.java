package nl.tue.win.ga.model;

import java.awt.Point;
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
}
