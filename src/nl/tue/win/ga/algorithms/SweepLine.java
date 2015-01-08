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
public class SweepLine {

    final private List<Point> SortedPoints;
    final private TreeMap SearchTree = new TreeMap();
    final private List<LineSegment> Verticals = new ArrayList<>();
    final private List<LineSegment> edges = new ArrayList<>();
    final private BoundingBox Box;

    public SweepLine(ArrayList<Point> points) {
        MergeSort ms = new MergeSort();
        SortedPoints = ms.sort(points);
        initializeEdges(points);
        Box = new BoundingBox(SortedPoints, 0.1f);
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
                ls1 = new LineSegment(p, new Point(p.x, Box.getMiny()), null);
            }
            if (above != null) {
                Point intersect2 = calculateIntersection(p, above);
                ls2 = new LineSegment(p, new Point(intersect2.x, intersect2.y), null);
            } else {
                ls2 = new LineSegment(p, new Point(p.x, Box.getMaxy()), null);
            }

            if (ls1.getEndPoint().y > p.y && ls2.getEndPoint().y > p.y) {
                Collection all = SearchTree.values();
                List<Point> Result = EmergencyCalculations(all, p);
                ls1.getEndPoints()[0] = new Point(p.x, p.y);
                ls1.getEndPoints()[1] = new Point(Result.get(0).x, Result.get(0).y);
                ls2.getEndPoints()[0] = new Point(p.x, p.y);
                ls2.getEndPoints()[1] = new Point(Result.get(1).x, Result.get(1).y);
            }
            if (ls1.getEndPoint().y < p.y && ls2.getEndPoint().y < p.y) {
                Collection all =  SearchTree.values();
                List<Point> Result = EmergencyCalculations(all, p);
                ls1.getEndPoints()[0] = new Point(p.x, p.y);
                ls1.getEndPoints()[1] = new Point(Result.get(0).x, Result.get(0).y);
                ls2.getEndPoints()[0] = new Point(p.x, p.y);
                ls2.getEndPoints()[1] = new Point(Result.get(1).x, Result.get(1).y);
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

    private Point calculateIntersection(Point p, Point start) {
        List<Point> end = new ArrayList<>();
        List<Point> intersect = new ArrayList<>();
        Point winner = null;
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

            if (intersect.size() > 1) {
                winner = getClosestPoint(intersect, p);
            } else {
                winner = intersect.get(0);
            }
        }

        return winner;
    }

    private Point getClosestPoint(List<Point> candidates, Point p) {
        double distanceTo1 = calculateDistance(candidates.get(0), p);
        double distanceTo2 = calculateDistance(candidates.get(1), p);

        if (distanceTo1 < distanceTo2) {
            return candidates.get(0);
        } else {
            return candidates.get(1);
        }
    }

    private double calculateDistance(Point p, Point q) {
        double ydiff = Math.abs((double) p.y - (double) q.y);
        double xdiff = Math.abs((double) p.x - (double) q.x);
        return Math.sqrt((ydiff * ydiff) + (xdiff * xdiff));
    }

    private List<Point> EmergencyCalculations(Collection<Point> all, Point p) {
        List<Point> Smallery = new ArrayList<>();
        List<Point> Biggery = new ArrayList<>();
        List<Point> BigResult = new ArrayList<>();

        for (Point inter : all) {
            if (!inter.equals(p)) {
                Point res = calculateIntersection(p, inter);
                if (res.y < p.y) {
                    Smallery.add(res);
                } else {
                    Biggery.add(res);
                }
            }
        }
        if (Smallery.isEmpty()) {
            BigResult.add(new Point(p.x, Box.getMiny()));
        } else {
            Point smallesty = new Point(p.x, Integer.MIN_VALUE);
            for (Point small : Smallery) {
                if (small.y > smallesty.y) {
                    smallesty.y = small.y;
                }
            }
            BigResult.add(smallesty);
        }
        if (Biggery.isEmpty()) {
            BigResult.add(new Point(p.x, Box.getMaxy()));
        } else {
            Point biggesty = new Point(p.x, Integer.MAX_VALUE);
            for (Point big : Biggery) {
                if (big.y < biggesty.y) {
                    biggesty.y = big.y;
                }
            }
            BigResult.add(biggesty);
        }
        return BigResult;
    }
}
