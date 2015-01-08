package nl.tue.win.ga.model;

import java.awt.Point;
import java.util.ArrayList;
import nl.tue.win.ga.model.Node.NodeType;

/**
 * Represents a line segment (as given in the lecture).
 *
 * @author maikel
 */
public class LineSegment {

    private Point[] endPoints = new Point[2];
    private Face face; // face directly above this segment in the original subdivision

    public LineSegment(Point p1, Point p2, Face f) {
        if (p1.x <= p2. x){
            this.endPoints[0] = p1;
            this.endPoints[1] = p2;
        } else {
            this.endPoints[0] = p2;
            this.endPoints[1] = p1;
        }

        this.face = f;
    }

    public Point[] getEndPoints(){
        return endPoints;
    }

    public Face getFace() {
        return face;
    }

    public Boolean belowPoint(Point p){
        return ((endPoints[1].y-endPoints[0].y)/(endPoints[1].x-endPoints[0].x)*(p.x - endPoints[0].x))
                + endPoints[0].y < p.y;
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
