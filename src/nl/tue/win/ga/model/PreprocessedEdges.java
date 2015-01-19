package nl.tue.win.ga.model;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

/**
 * Converts a point representation of a hull to an edge representation.
 *
 * @author maikel
 */
public class PreprocessedEdges {
    
    public static List<LineSegment> preprocess(SimplePolygon polygon) {
        return preprocess(polygon.getHull());
    }

    public static List<LineSegment> preprocess(List<Point> hull) {
        //Face inside = new Face();
        //Face outside = new Face();
        List<LineSegment> segments = new ArrayList<>();

        Point prev = null;
        for (Point p : hull) {
            if (prev == null) {
                prev = p;
            } else {
                Face f = null;//p.x > prev.x ? outside : inside;
                LineSegment l = new LineSegment(prev, p, f);
                segments.add(l);
                prev = p;
            }
        }
        LineSegment closingSegment = new LineSegment(hull.get(hull.size() - 1), hull.get(0), null);
        segments.add(closingSegment);

        return segments;
    }
}
