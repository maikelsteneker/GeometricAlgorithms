package nl.tue.win.ga.algorithms;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;
import nl.tue.win.ga.model.*;
import java.util.TreeMap;

/**
 *
 * @author Administrator
 */
public class SweepLine {

    final private List<Point> SortedPoints;
    final private TreeMap SearchTree = new TreeMap();
    final private List<LineSegment> Verticals = new ArrayList<>();
    final private List<LineSegment> edges = new ArrayList<>();

    public SweepLine(ArrayList<Point> points) {
        MergeSort ms = new MergeSort();
        SortedPoints = ms.sort(points);
        initializeEdges(points);

    }

    public void sweep() {
        for (Point p : SortedPoints) {
            Point below = null;
            Point above = null;
            if (isStartPoint(p)) {
                SearchTree.put(p.y, p);
            }

            List<Point> begin = getBeginPoint(p);

            if (begin != null) {
                for (Point po : begin) {
                    SearchTree.remove(po.y);
                }

            }

            if (SearchTree.lowerKey(p.y) != null) {
                below = (Point) SearchTree.get(SearchTree.lowerKey(p.y));
            }
            if (SearchTree.higherKey(p.y) != null) {
                above = (Point) SearchTree.get(SearchTree.higherKey(p.y));
            }

            LineSegment ls1;
            LineSegment ls2;

            if (below != null) {
                Point intersect1 = calculateIntersection(p, below);
                ls1 = new LineSegment(p, new Point(intersect1.x, intersect1.y), null);
            } else {
                ls1 = new LineSegment(p, new Point(p.x, p.y - 5000), null);
            }
            if (above != null) {
                Point intersect2 = calculateIntersection(p, above);
                ls2 = new LineSegment(p, new Point(intersect2.x, intersect2.y), null);
            } else {
                ls2 = new LineSegment(p, new Point(p.x, p.y + 5000), null);
            }

            Verticals.add(ls1);
            Verticals.add(ls2);
        }

    }

    public List<LineSegment> getVerticals() {
        return Verticals;
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
        for(Point sPoint : multiple) {
            if(isStartPoint(sPoint)) {
                removePoint.add(sPoint);
            }
        }
        
        for(Point rPoint : removePoint) {
            multiple.remove(rPoint);
        }
        if(multiple.isEmpty()) {
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

    private Point calculateIntersection(Point p, Point start) {
        List<Point> end = new ArrayList<>();
        Point intersect = null;
        for (LineSegment edge : edges) {
            if (edge.getStartPoint().x == start.x && edge.getStartPoint().y == start.y) {
                end.add(new Point(edge.getEndPoint().x, edge.getEndPoint().y));
            }
        }
        if (end.size() > 1) {
            intersect = getClosestPoint(end, p);
        } else if (end.isEmpty()) {
            return null;
        } else {
            intersect = end.get(0);
        }

        double slope = ((double) start.y - (double) intersect.y) / ((double) start.x - (double) intersect.x);
        double y = start.y + slope * p.x - slope * start.x;

        return new Point(p.x,
                (int) y
        );

    }

    private Point getClosestPoint(List<Point> candidates, Point p) {
        double distanceTo1 = calculateDistance(candidates.get(0), p);
        double distanceTo2 = calculateDistance(candidates.get(0), p);
        
        if(distanceTo1 < distanceTo2) {
            return candidates.get(0);
        }
        else {
            return candidates.get(1);
        }
    }
    
    private double calculateDistance(Point p, Point q){
        double ydiff = Math.abs((double) p.y - (double) q.y);
        double xdiff = Math.abs((double) p.x - (double) q.x);
        return Math.sqrt((ydiff * ydiff) + (xdiff*xdiff));
    }
}
