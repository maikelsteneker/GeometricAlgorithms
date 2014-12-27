package nl.tue.win.ga.io;

import java.awt.Point;
import java.io.File;
import java.io.IOException;
import java.util.List;
import nl.tue.win.ga.model.SimplePolygon;

/**
 * Writes a polygon to a file in the file format specified for the 2IL55 course.
 *
 * first line contains one integer: the number of vertices of the simple polygon
 *
 * remaining lines contain two positive integers each: the two coordinates of a
 * vertex. The vertices should be given in clock-wise order along the polygon.
 *
 * @author maikel
 */
public class WritePolygonToFile {

    public static void writePolygonToFile(String filename, List<Point> hull) throws IOException {
        File file = new File(filename);
        SimplePolygon polygon = new SimplePolygon(hull);
        writePolygonToFile(file, polygon);
    }

    public static void writePolygonToFile(String filename, SimplePolygon polygon) throws IOException {
        File file = new File(filename);
        writePolygonToFile(file, polygon);
    }

    public static void writePolygonToFile(File file, List<Point> hull) throws IOException {
        SimplePolygon polygon = new SimplePolygon(hull);
        writePolygonToFile(file, polygon);
    }

    public static void writePolygonToFile(File file, SimplePolygon polygon) throws IOException {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
