package nl.tue.win.ga.io;

import java.awt.Point;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.regex.Pattern;
import nl.tue.win.ga.model.SimplePolygon;

/**
 * Reads a polygon from a file in the file format specified for the 2IL55
 * course.
 *
 * first line contains one integer: the number of vertices of the simple polygon
 *
 * remaining lines contain two positive integers each: the two coordinates of a
 * vertex. The vertices should be given in clock-wise order along the polygon.
 *
 * @author maikel
 */
public class ReadPolygonFromFile {

    final static Pattern FIRST_LINE = Pattern.compile("\\d*");
    final static Pattern REMAINING_LINES = Pattern.compile("-?\\d*[ \\t]+-?\\d*");

    public static SimplePolygon readPolygonFromFile(File file)
            throws IOException {
        if (!file.exists()) {
            throw new IOException("The specified file does not exist!");
        }
        return readInput(new FileReader(file));
    }

    public static SimplePolygon readPolygonFromFile(String filename)
            throws IOException {
        return readPolygonFromFile(new File(filename));
    }

    public static SimplePolygon readPolygonFromStream(InputStream input)
            throws IOException {
        return readInput(new InputStreamReader(input));
    }

    private static SimplePolygon readInput(Reader input) throws IOException {
        BufferedReader buffer = new BufferedReader(input);
        int nVertices = readFirstLine(buffer);
        Point[] vertices = new Point[nVertices];
        for (int i = 0; i < vertices.length; i++) {
            vertices[i] = readLine(buffer);
        }
        return new SimplePolygon(vertices);
    }

    private static int readFirstLine(BufferedReader input) throws IOException {
        String currentLine = input.readLine();
        if (!FIRST_LINE.matcher(currentLine).matches()) {
            throw new IOException("First line does not contain a single integer!");
        }
        return Integer.parseInt(currentLine);
    }

    private static Point readLine(BufferedReader input) throws IOException {
        String currentLine = input.readLine();
        if (!REMAINING_LINES.matcher(currentLine).matches()) {
            throw new IOException("Line does not contain two positive integers!");
        }
        String[] tokens = currentLine.split("[ \\t]+");
        int x = Integer.parseInt(tokens[0]);
        int y = Integer.parseInt(tokens[1]);
        return new Point(x, y);
    }
}
