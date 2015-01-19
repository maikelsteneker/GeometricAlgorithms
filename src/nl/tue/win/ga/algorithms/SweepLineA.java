package nl.tue.win.ga.algorithms;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import nl.tue.win.ga.model.*;
import nl.tue.win.ga.utilities.*;

/**
 *
 * @author Administrator
 */
public class SweepLineA {

    final private List<Point> sortedPoints;
    final private List<Point> currentPoints = new ArrayList<>();
    final private List<LineSegment> verticals = new ArrayList<>();
    final private List<LineSegment> edges = new ArrayList<>();
    final private BoundingBox box;

    /**
     * Initialize the sweep line and the corresponding data structures
     * 
     * @param points of the polygon
     */
    public SweepLineA(List<Point> points) {
        MergeSort ms = new MergeSort();
        sortedPoints = ms.sort(points);
        initializeEdges(points);
        box = new BoundingBox(sortedPoints, 0.1f);
    }

    /**
     * Start sweeping and construct a trapezoidal map
     */
    public void sweep() {
        for (Point p : sortedPoints) {
            if (isStartPoint(p)) {  //check if p is a start, else we do not need to add it
                currentPoints.add(p);
            }

            List<Point> begin = getBeginPoint(p);

            if (begin != null) { //get all the start points for endpoint p and remove them
                for (Point po : begin) {
                    currentPoints.remove(po);
                }

            }
            
            //calculate all the intersections with rays trough p and take the closest ones
            List<Point> result = intersectionCalculations(currentPoints, p);
            LineSegment ls1 = new LineSegment(p, result.get(0));
            LineSegment ls2 = new LineSegment(p, result.get(1));
            verticals.add(ls1);
            verticals.add(ls2);
        }

    }

    /**
     * Get the vertical line segments of the map
     * @return The line segments of the map
     */
    public List<LineSegment> getVerticals() {
        return verticals;
    }

    /**
     * Initialize the points into edges
     * @param hull input points
     */
    private void initializeEdges(List<Point> hull) {

        Point prev = null;
        for (Point p : hull) {
            if (prev == null) {
                prev = p;
            } else {
                Face f = null;//
                LineSegment l;
                if (p.x > prev.x) {
                    l = new LineSegment(prev, p, f);
                } else {
                    l = new LineSegment(p, prev, f);
                }
                edges.add(l);
                prev = p;
            }
        }
        Point begin = hull.get(0);
        Point end = hull.get(hull.size() - 1);
        LineSegment closingSegment;
        if (begin.x > end.x) {
            closingSegment = new LineSegment(end, begin, null);
        } else {
            closingSegment = new LineSegment(begin, end, null);
        }
        edges.add(closingSegment);
    }

    /**
     * Get the begin points of an endpoint of a linesegment that need to be removed from the list
     * @param end endpoint of the line
     * @return Start points to be removed
     */
    private List<Point> getBeginPoint(Point end) {
        ArrayList<Point> multiple = new ArrayList<>();
        ArrayList<LineSegment> edge = new ArrayList<>();
        for (LineSegment firstEdge : edges) {
            if (firstEdge.getEndPoint().x == end.x && firstEdge.getEndPoint().y == end.y) {
                edge.add(firstEdge);
                multiple.add(firstEdge.getStartPoint());
            }
        }
        if (!edge.isEmpty()) {
            for (LineSegment edgePart : edge) {
                edges.remove(edgePart);
            }
        }

        List<Point> removePoint = new ArrayList<>();
        for (Point sPoint : multiple) {
            if (isStartPoint(sPoint)) {
                removePoint.add(sPoint);
            }
        }

        for (Point rPoint : removePoint) {
            multiple.remove(rPoint);
        }
        if (multiple.isEmpty()) {
            return null;
        }
        return multiple;
    }

    /**
     * Check if a point is a startpoint of a linesegment
     * @param p potential start point
     * @return true or false
     */
    private boolean isStartPoint(Point p) {
        for (LineSegment edge : edges) {
            if (edge.getStartPoint().x == p.x && edge.getStartPoint().y == p.y) {
                return true;
            }
        }
        return false;

    }

    /**
     * calculate all the intersections between a ray from point p trough the edges with start point start
     * @param p point p
     * @param start start point start of linesegment
     * @return list of intersection points
     */
    private List<Point> calculateAllIntersection(Point p, Point start) {
        List<Point> end = new ArrayList<>();
        List<Point> intersect = new ArrayList<>();
        for (LineSegment edge : edges) {
            if (edge.getStartPoint().x == start.x && edge.getStartPoint().y == start.y) {
                end.add(new Point(edge.getEndPoint().x, edge.getEndPoint().y));
            }
        }

        if (end.isEmpty()) {
            return null;
        } else {

            for (Point intersection : end) {
                double slope = ((double) start.y - (double) intersection.y) / ((double) start.x - (double) intersection.x);
                double y = start.y + slope * p.x - slope * start.x;
                intersect.add(new Point(p.x, (int) y));
            }

        }
        return intersect;
    }

    /**
     * Calculate for each point the intersection with a ray trough p
     * @param all all points to be possibly intersected
     * @param p point p
     * @return the two best intersection points
     */
    private List<Point> intersectionCalculations(Collection<Point> all, Point p) {
        List<Point> smallery = new ArrayList<>();
        List<Point> biggery = new ArrayList<>();
        List<Point> bigResult = new ArrayList<>();

        for (Point inter : all) {
            if (!inter.equals(p)) {
                List<Point> res = calculateAllIntersection(p, inter);
                for (Point q : res) {
                    if (q.y < p.y) {
                        smallery.add(q);
                    } else {
                        biggery.add(q);
                    }
                }
            }
        }
        if (smallery.isEmpty()) {
            bigResult.add(new Point(p.x, box.getMiny()));
        } else {
            Point smallesty = new Point(p.x, Integer.MIN_VALUE);
            for (Point small : smallery) {
                if (small.y > smallesty.y) {
                    smallesty.y = small.y;
                }
            }
            bigResult.add(smallesty);
        }
        if (biggery.isEmpty()) {
            bigResult.add(new Point(p.x, box.getMaxy()));
        } else {
            Point biggesty = new Point(p.x, Integer.MAX_VALUE);
            for (Point big : biggery) {
                if (big.y < biggesty.y) {
                    biggesty.y = big.y;
                }
            }
            bigResult.add(biggesty);
        }
        return bigResult;
    }
}
