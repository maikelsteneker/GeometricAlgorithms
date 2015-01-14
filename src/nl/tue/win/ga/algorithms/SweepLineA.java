package nl.tue.win.ga.algorithms;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import nl.tue.win.ga.model.*;
import java.util.TreeMap;
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

    public SweepLineA(ArrayList<Point> points) {
        MergeSort ms = new MergeSort();
        sortedPoints = ms.sort(points);
        initializeEdges(points);
        box = new BoundingBox(sortedPoints, 0.1f);
    }

    public void sweep() {
        for (Point p : sortedPoints) {
            if (isStartPoint(p)) {
                currentPoints.add(p);
            }

            List<Point> begin = getBeginPoint(p);

            if (begin != null) {
                for (Point po : begin) {
                    currentPoints.remove(po);
                }

            }
            LineSegment ls1;
            LineSegment ls2;
            
            List<Point> result = intersectionsCalculations(currentPoints, p);
            ls1 = new LineSegment(p, result.get(0));
            ls2 = new LineSegment(p, result.get(1));
            verticals.add(ls1);
            verticals.add(ls2);
        }

    }

    public List<LineSegment> getVerticals() {
        return verticals;
    }

    private void initializeEdges(List<Point> hull) {

        Point prev = null;
        for (Point p : hull) {
            if (prev == null) {
                prev = p;
            } else {
                Face f = null;//p.x > prev.x ? outside : inside;
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

    private boolean isStartPoint(Point p) {
        for (LineSegment edge : edges) {
            if (edge.getStartPoint().x == p.x && edge.getStartPoint().y == p.y) {
                return true;
            }
        }
        return false;

    }

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

    private List<Point> intersectionsCalculations(Collection<Point> all, Point p) {
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
