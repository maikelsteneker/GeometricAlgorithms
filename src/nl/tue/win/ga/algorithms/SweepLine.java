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

    private List<Point> SortedPoints;
    private BinarySearchTree Bst;
    private TreeMap SearchTree = new TreeMap();
    private List<LineSegment> Verticals = new ArrayList<>();
    private List<LineSegment> edges = new ArrayList<>();

    public SweepLine(ArrayList<Point> points) {
        MergeSort ms = new MergeSort();
        SortedPoints = ms.sort(points);
        initializeEdges(points);
        Bst = new BinarySearchTree();

    }

    public void sweep() {
        int i = 0;
        for (Point p : SortedPoints) {
            Point below = null;
            Point above = null;
            SearchTree.put(p.y, p);

            Point begin = getBeginPoint(p);
            if (i != 1) {
                if (begin != null) {
                    SearchTree.remove(begin.y);
                }
            }

            if (i == SortedPoints.size() - 1) {
                Point begin2 = getBeginPoint(p);
                if (begin2 != null) {
                    SearchTree.remove(begin2.y);
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
            i++;
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

    private Point getBeginPoint(Point end) {
        for (LineSegment edge : edges) {
            if (edge.getEndPoint().x == end.x && edge.getEndPoint().y == end.y) {
                edges.remove(edge);
                return edge.getStartPoint();
            }
        }
        return null;
    }

    private Point calculateIntersection(Point p, Point start) {
        Point end = null;
        for (LineSegment edge : edges) {
            if (edge.getStartPoint().x == start.x && edge.getStartPoint().y == start.y) {
                end = new Point(edge.getEndPoint().x, edge.getEndPoint().y);
                break;
            }
        }
        if (end != null) {
            double slope = ((double) start.y - (double) end.y) / ((double) start.x - (double) end.x);
            double y = start.y + slope * p.x - slope * start.x;
            return new Point(p.x, (int) y);
        }

        return null;
    }
}
