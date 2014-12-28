package nl.tue.win.ga.model;

import java.awt.Point;

/**
 * Represents a line segment (as given in the lecture).
 *
 * @author maikel
 */
public class LineSegment {
    private Point[] endPoints = new Point[2];
    private Face face; // face directly above this segment in the original subdivision
}
