package nl.tue.win.ga.algorithms;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import nl.tue.win.ga.model.*;
import java.util.TreeMap;
import nl.tue.win.ga.utilities.*;

/**
 *
 * @author Coen
 */
public class SweepLineB {

    final private List<Point> points;
    final private TreeMap searchTree;
    final private List<LineSegment> verticals = new ArrayList<>();
    private List<LineSegment> edges = new ArrayList<>();
    final private BoundingBox box;

    public SweepLineB(List<Point> polygonPoints) {
        MergeSort ms = new MergeSort();
        points = ms.sort(polygonPoints);
        edges = PreprocessedEdges.preprocess(polygonPoints);
        box = new BoundingBox(points, 0.1f);
        searchTree = new TreeMap();
    }

    public void sweep() {
        for (Point p : points) {
            updateTree(p);
            removeSegment(p);
            addSegment(p);
            LineSegment vup;
            LineSegment vdown;
            if (searchTree.higherKey(p.y) != null) {
                vdown = new LineSegment(p, new Point(p.x, (int) searchTree.higherKey(p.y)));
            } else {
                vdown = new LineSegment(p, new Point(p.x, box.getMaxy()));
            }

            if (searchTree.lowerKey(p.y) != null) {
                vup = new LineSegment(p, new Point(p.x, (int) searchTree.lowerKey(p.y)));
            } else {
                vup = new LineSegment(p, new Point(p.x, box.getMiny()));
            }

            verticals.add(vup);
            verticals.add(vdown);
            removeEmptySets();
        }
    }

    public List<LineSegment> getVerticals() {
        return verticals;
    }

    private void updateTree(Point p) {
        Set<Integer> keys = searchTree.keySet();

        List<Integer> oldKeys = new ArrayList<>();
        List<Integer> newKeys = new ArrayList<>();
        for (int k : keys) {
            List<LineSegment> s1 = (List<LineSegment>) searchTree.get(k);
            oldKeys.add(k);
            for (LineSegment s2 : s1) {
                newKeys.add(s2.getIntersection(p.x));
            }

        }

        List<LineSegment> s3 = new ArrayList<>();
        for (int old : oldKeys) {
            s3.addAll((List<LineSegment>) searchTree.get(old));
            searchTree.remove(old);
        }

        for (int i = 0; i < newKeys.size() - 1; i++) {
            List<LineSegment> s4 = new ArrayList<>();
            if (Objects.equals(newKeys.get(i), newKeys.get(i + 1))) {
                s4.add(s3.get(i));
                s4.add(s3.get(i + 1));
                searchTree.put(newKeys.get(i), s4);
                i++;
                if (i + 1 == newKeys.size() - 1) {
                    List<LineSegment> s5 = new ArrayList<>();
                    s5.add(s3.get(i + 1));
                    searchTree.put(newKeys.get(i + 1), s5);
                }
            } else {
                s4.add(s3.get(i));
                searchTree.put(newKeys.get(i), s4);
                if (i + 1 == newKeys.size() - 1) {
                    List<LineSegment> s5 = new ArrayList<>();
                    s5.add(s3.get(i + 1));
                    searchTree.put(newKeys.get(i + 1), s5);
                }
            }
        }
    }

    private void addSegment(Point p) {
        List<LineSegment> segments = new ArrayList<>();
        for (LineSegment s : edges) {
            if (s.getStartPoint().x == p.x && s.getStartPoint().y == p.y) {
                segments.add(s);
            }
        }
        searchTree.put(p.y, segments);
    }

    private void removeSegment(Point p) {
        searchTree.remove(p.y);
    }

    private void removeEmptySets() {
        Set<Integer> keys = searchTree.keySet();
        int empty = Integer.MIN_VALUE;
        for (int k : keys) {
            if (((List<LineSegment>) searchTree.get(k)).isEmpty()) {
                empty = k;
            }
        }
        if (empty != Integer.MIN_VALUE) {
            searchTree.remove(empty);
        }

    }
}
