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

    public LineSegment(Point p1, Point p2, Face f) {
        this.endPoints[0] = p1;
        this.endPoints[1] = p2;
        this.face = f;
    }

    @Override
    public String toString() {
        return endPoints[0] + ", " + endPoints[1] + ", " + face;
    }

    public Point getStartPoint() {
        return endPoints[0];
    }

    public Point getEndPoint() {
        return endPoints[1];
    }
}
