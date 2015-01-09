package nl.tue.win.ga.model;

import nl.tue.win.ga.model.drawing.Drawable;
import java.awt.Graphics;
import java.awt.Point;
import nl.tue.win.ga.utilities.DrawingUtilities;

/**
 * Represents a line segment (as given in the lecture).
 *
 * @author maikel
 */
public class LineSegment implements Drawable {

    public static int getIntersection(LineSegment s, int x) {
        final Point startPoint = s.getStartPoint();
        final Point endPoint = s.getEndPoint();
        final float slope = (float) (endPoint.y - startPoint.y) / (endPoint.x - startPoint.x);
        final int y = (int) (startPoint.y + (x - startPoint.x) * slope);
        return y;
    }
    
    public int getIntersection(int x) {
        return getIntersection(this, x);
    }

    private Point[] endPoints = new Point[2];
    private Face face; // face directly above this segment in the original subdivision
    public DrawingUtilities drawingUtilities;

    public LineSegment(Point p1, Point p2) {
        this(p1, p2, null);
    }
    
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

    public void setEndPoint(int x, int y) {
        this.endPoints[1].x = x;
        this.endPoints[1].y = y;
    }
    public Point getStartPoint() {
        return endPoints[0];
    }

    public Point getEndPoint() {
        return endPoints[1];
    }
    
    @Override
    public void draw(Graphics g, boolean scale, boolean invertY) {
        Point beginPoint = endPoints[0];
        Point endPoint = endPoints[1];
        if (invertY) {
            beginPoint = drawingUtilities.invert(beginPoint);
            endPoint = drawingUtilities.invert(endPoint);
        }
        if (scale) {
            beginPoint = drawingUtilities.scaled(beginPoint);
            endPoint = drawingUtilities.scaled(endPoint);
        }
        g.drawLine(beginPoint.x, beginPoint.y, endPoint.x, endPoint.y);
    }
}
