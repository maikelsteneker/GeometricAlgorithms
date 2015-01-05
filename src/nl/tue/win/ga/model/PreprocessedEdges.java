package nl.tue.win.ga.model;

import java.awt.Point;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import nl.tue.win.ga.io.ReadPolygonFromFile;

/**
 *
 * @author maikel
 */
public class PreprocessedEdges {

    public static List<LineSegment> preprocess(SimplePolygon polygon) {
        List<Point> hull = polygon.getHull();
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

    public static void main(String[] args) throws IOException {
        SimplePolygon polygon = ReadPolygonFromFile.readPolygonFromFile("/home/maikel/polygon.txt");
        List<LineSegment> segments = preprocess(polygon);
        for (LineSegment s : segments) {
            System.out.println(s);
        }
    }
}
