package nl.tue.win.ga.utilities;

import java.awt.Graphics;
import java.awt.Point;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import nl.tue.win.ga.model.LineSegment;
import nl.tue.win.ga.model.drawing.Drawable;

/**
 * Computes a bounding box for a set of points.
 *
 * @author maikel
 */
public class BoundingBox implements Iterable<LineSegment>, Drawable {

    private int minx = Integer.MAX_VALUE, miny = Integer.MAX_VALUE, maxx = Integer.MIN_VALUE, maxy = Integer.MIN_VALUE;

    public BoundingBox(Iterable<Point> points) {
        this(points, 0);
    }
    
    public BoundingBox(Iterable<Point> points, float verticalOffset) {
        for (Point p : points) {
            if (p.x < minx) {
                minx = p.x;
            }
            if (p.y < miny) {
                miny = p.y;
            }
            if (p.x > maxx) {
                maxx = p.x;
            }
            if (p.y > maxy) {
                maxy = p.y;
            }
        }
        miny -= verticalOffset * height();
        maxy += verticalOffset * height();
    }

    public int getMinx() {
        return minx;
    }

    public int getMiny() {
        return miny;
    }

    public int getMaxx() {
        return maxx;
    }

    public int getMaxy() {
        return maxy;
    }
    
    public final int height() {
        return maxy - miny;
    }
    
    public final int width() {
        return maxx - minx;
    }

    /**
     * Returns a line in which all points are contained in the x direction, with
     * a y coordinate higher than (or equal to) all points.
     *
     * @return the line
     */
    public LineSegment getTopLine() {
        final Point leftPoint = new Point(minx, maxy);
        final Point rightPoint = new Point(maxx, maxy);
        return new LineSegment(leftPoint, rightPoint, null);
    }

    /**
     * Returns a line in which all points are contained in the x direction, with
     * a y coordinate smaller than (or equal to) all points.
     *
     * @return the line
     */
    public LineSegment getBottomLine() {
        final Point leftPoint = new Point(minx, miny);
        final Point rightPoint = new Point(maxx, miny);
        return new LineSegment(leftPoint, rightPoint, null);
    }

    /**
     * Returns a point that is left to all other points.
     *
     * @return the point
     */
    public Point getLeftPoint() {
        return new Point(minx, (miny + maxy) / 2);
    }

    /**
     * Returns a point that is right to all other points.
     *
     * @return the point
     */
    public Point getRightPoint() {
        return new Point(maxx, (miny + maxy) / 2);
    }

    @Override
    public Iterator<LineSegment> iterator() {
        final List<LineSegment> segments = new ArrayList<>();
        segments.add(this.getBottomLine());
        segments.add(this.getTopLine());
        return segments.iterator();
    }

    @Override
    public void draw(Graphics g, boolean scale, boolean invertY) {
        for (LineSegment ls : this) {
            ls.draw(g, scale, invertY);
        }
    }
}
