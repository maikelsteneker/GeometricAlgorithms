/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nl.tue.win.ga.io;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import nl.tue.win.ga.model.SimplePolygon;

/**
 * Reads a polygon from a file in the file format specified for the 2IL55 course.
 * 
 * first line contains one integer: the number of vertices of the simple polygon
 *
 * remaining lines contain two positive integers each: the two coordinates of a
 * vertex. The vertices should be given in clock-wise order along the polygon.
 *
 * @author maikel
 */
public class ReadPolygonFromFile {

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
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
