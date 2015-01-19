package nl.tue.win.ga.algorithms;

import java.awt.Point;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import nl.tue.win.ga.gui.DrawInterface;
import nl.tue.win.ga.model.*;
import nl.tue.win.ga.utilities.BoundingBox;

/**
 *
 * @author Coen
 */
public class RandomIncrementalConstruction {

    private List<LineSegment> edges = new ArrayList<>();
    private Face boundingBox;
    private Graph searchGraph;
    private boolean done = false;
    private List<Face> trapezoidalMap = new ArrayList<>();
    public int lastStep;
    private int currentStep;
    private final static boolean SEPARATE_LINES = true; // no overlapping face borders
    public List<Face> mergeFaces = new ArrayList<>();
    public List<LineSegment> handled = new ArrayList<>();
    public List<Point> presentPoints = new ArrayList<>();

    public RandomIncrementalConstruction(List<Point> points) {
        edges = PreprocessedEdges.preprocess(points);
        //init the bounding box
        boundingBox = new BoundingBox(points, 0.1f).asFace();
        Face[] neighbours = {null, null, null, null, null, null};
        boundingBox.setAllNeighbours(neighbours);
        Node root = new Node(boundingBox);
        boundingBox.setNode(root);
        searchGraph = new Graph(root);
    }

    public void inOrderIncrementalMap() {
        computeIncrementalMap(edges.iterator());
    }

    public void randomIncrementalMap() {
        computeIncrementalMap(new RandomIterator(edges));
    }

    public void computeIncrementalMap(final Iterator<LineSegment> iterator) {
        currentStep = 0;

        while (iterator.hasNext()) {
            if (currentStep++ >= lastStep) {
                return;
            }
            LineSegment s = iterator.next();
            Point start = s.getStartPoint();
            Point end = s.getEndPoint();
            List<Face> intersections = getIntersectedFaces(s);
            //distinguish 4 cases
            if (presentPoints.contains(start) && presentPoints.contains(end)) { //both points are in the plane already
                middleComputation(intersections, s, start, end);
            } else if (presentPoints.contains(start) && !presentPoints.contains(end)) { //start point is in the plane already
                presentPoints.add(end);
                endComputation(intersections, s, start, end);
            } else if (!presentPoints.contains(start) && presentPoints.contains(end)) { //endpoint is in the plane already
                presentPoints.add(start);
                startComputation(intersections, s, start, end);
            } else { //no points are in the plane
                presentPoints.add(start);
                presentPoints.add(end);
                fullComputation(intersections, s, start, end);
            }

            handled.add(s);
            merge(s);
            checkInvariant();
        }
    }

    /**
     * Merge the faces that need merging
     *
     * @param s
     */
    private void merge(LineSegment s) {
        while (!mergeFaces.isEmpty()) {
            Face merged = null;
            List<Face> help = new ArrayList<>();
            help.addAll(mergeFaces);
            Face f1 = mergeFaces.get(0);
            for (Face f2 : help) {
                if (f1 != f2) {
                    if (f1.getLeftp().x == f2.getRightp().x && f1.getLeftp().y == f2.getRightp().y) { //check if the faces are adjacent on one side
                        if (f1.getBottom().getStartPoint().x != s.getStartPoint().x && f1.getBottom().getStartPoint().y != s.getStartPoint().y) { //does the face lie above or beneath s
                            // case beneath
                            merged = new Face(handled.get(handled.size() - 1), f2.getBottom(), f2.getLeftp(), f1.getRightp());
                            setMergeNeighbours(merged, f1, f2);
                            setMergeTree(merged, f1, f2, true);
                            trapezoidalMap.remove(f1);
                            mergeFaces.remove(f1);
                            trapezoidalMap.remove(f2);
                            mergeFaces.remove(f2);
                            trapezoidalMap.add(merged);
                            mergeFaces.add(merged);
                            break;
                        } else {
                            // case above
                            merged = new Face(f2.getTop(), handled.get(handled.size() - 1), f2.getLeftp(), f1.getRightp());
                            setMergeNeighbours(merged, f1, f2);
                            setMergeTree(merged, f1, f2, false);
                            trapezoidalMap.remove(f1);
                            mergeFaces.remove(f1);
                            trapezoidalMap.remove(f2);
                            mergeFaces.remove(f2);
                            trapezoidalMap.add(merged);
                            mergeFaces.add(merged);
                            break;
                        }
                    } else if (f1.getRightp().x == f2.getLeftp().x && f1.getRightp().y == f2.getLeftp().y) { //check if adjacent on the other side
                        if (f2.getBottom().getStartPoint().x != s.getStartPoint().x && f2.getBottom().getStartPoint().y != s.getStartPoint().y) { //does the face lie above or beneath s
                            // case beneath
                            merged = new Face(handled.get(handled.size() - 1), f2.getBottom(), f1.getLeftp(), f2.getRightp());
                            setMergeNeighbours(merged, f2, f1);
                            setMergeTree(merged, f1, f2, true);
                            trapezoidalMap.remove(f1);
                            mergeFaces.remove(f1);
                            trapezoidalMap.remove(f2);
                            mergeFaces.remove(f2);
                            trapezoidalMap.add(merged);
                            mergeFaces.add(merged);
                            break;
                        } else {
                            // case above
                            merged = new Face(f2.getTop(), handled.get(handled.size() - 1), f1.getLeftp(), f2.getRightp());
                            setMergeNeighbours(merged, f2, f1);
                            setMergeTree(merged, f1, f2, false);
                            trapezoidalMap.remove(f1);
                            mergeFaces.remove(f1);
                            trapezoidalMap.remove(f2);
                            mergeFaces.remove(f2);
                            trapezoidalMap.add(merged);
                            mergeFaces.add(merged);
                            break;
                        }
                    }
                }
            }
            //cannot merge any further remove it from the merge list
            mergeFaces.remove(f1);

        }

    }

    /**
     * The endpoints of the linesegments are already processed. So we only have
     * to generated top and bottom faces for the linesegment
     *
     * @param ls
     * @param p
     * @param q
     */
    private void middleComputation(List<Face> intersections, LineSegment ls, Point p, Point q) {
        if (intersections.size() == 1) {
            // if the intersection size is 1, we only have to generate 1 face above and below ls
            calculateTopandBottomForMiddle(intersections.get(0), p, q, p, q, ls);
        } else {
            Point prevIntersection = calculateFirstForMiddle(intersections.get(0), ls, p);
            Point newIntersection;
            for (int i = 1; i < intersections.size() - 1; ++i) {
                //use intersection points as points for faces and generate the seperate faces that way
                Face next = intersections.get(i);
                newIntersection = new Point(next.getRightp().x, ls.getIntersection(next.getRightp().x));
                if (prevIntersection.y > next.getLeftp().y && newIntersection.y > next.getRightp().y) {
                    calculateTopandBottomForMiddle(next, prevIntersection, newIntersection, next.getLeftp(), next.getRightp(), ls);
                } else if (prevIntersection.y > next.getLeftp().y && newIntersection.y < next.getRightp().y) {
                    calculateTopandBottomForMiddle(next, prevIntersection, next.getRightp(), next.getLeftp(), newIntersection, ls);
                } else if (prevIntersection.y < next.getLeftp().y && newIntersection.y > next.getRightp().y) {
                    calculateTopandBottomForMiddle(next, next.getLeftp(), newIntersection, prevIntersection, next.getRightp(), ls);
                } else {
                    calculateTopandBottomForMiddle(next, next.getLeftp(), next.getRightp(), prevIntersection, newIntersection, ls);
                }
                prevIntersection = new Point(newIntersection.x, newIntersection.y);
            }
            calculateLastForMiddle(intersections.get(intersections.size() - 1), prevIntersection, q, ls);
        }
    }

    /**
     * Calculate the case where the end point of the line segment is new in the
     * plane.
     *
     * @param intersections
     * @param ls
     * @param p
     * @param q
     */
    private void endComputation(List<Face> intersections, LineSegment ls, Point p, Point q) {
        //Calculate the right face that needs to be added and then treat this as middle computation where both points are known in the plane
        Face last = intersections.get(intersections.size() - 1);
        Face A = splitLast(last, q);
        intersections.remove(last);
        intersections.add(A);
        middleComputation(intersections, ls, p, q);
    }

    /**
     * Calculate the case where the start point of the line segment is new in
     * the plane
     *
     * @param intersections
     * @param ls
     * @param p
     * @param q
     */
    private void startComputation(List<Face> intersections, LineSegment ls, Point p, Point q) {
        //Calculate the left face that needs to be added and then treat this as middle computation where both points are known in the plane
        Face first = intersections.get(0);
        Face B = splitFirst(first, p);
        intersections.remove(first);
        intersections.add(0, B);
        middleComputation(intersections, ls, p, q);
    }

    /**
     * Calculate the case where both the start and end point of the line segment
     * are new in the plane
     *
     * @param last
     * @param q
     * @return
     */
    private void fullComputation(List<Face> intersections, LineSegment ls, Point p, Point q) {
        //Calculate the left and right faces that need to be added and then treat this as middle computation where both points are known in the plane
        Face first = intersections.get(0);
        Face B = splitFirst(first, p);
        intersections.remove(first);
        intersections.add(0, B);
        Face last = intersections.get(intersections.size() - 1);
        Face A = splitLast(last, q);
        intersections.remove(last);
        intersections.add(A);
        middleComputation(intersections, ls, p, q);
    }

    /**
     * Split a face in which point q falls, in two faces with point q as border
     * of the 2
     *
     * @param last
     * @param q
     * @return
     */
    private Face splitLast(Face last, Point q) {
        Face A = new Face(last.getTop(), last.getBottom(), last.getLeftp(), q);
        Face B = new Face(last.getTop(), last.getBottom(), q, last.getRightp());

        FirstandLastNeighbours(last, A, B);

        Node n1 = last.getNode();
        n1.setType(Node.NodeType.POINT);
        n1.setFace(null);
        n1.setPoint(q);
        n1.setLchild(new Node(A));
        n1.setRchild(new Node(B));
        trapezoidalMap.remove(last);
        trapezoidalMap.add(A);
        trapezoidalMap.add(B);
        return A;
    }

    /**
     * Split a face in which point p falls, in two faces with point p as border
     * of the 2
     *
     * @param first
     * @param q
     * @return
     */
    private Face splitFirst(Face first, Point p) {
        Face A = new Face(first.getTop(), first.getBottom(), first.getLeftp(), p);
        Face B = new Face(first.getTop(), first.getBottom(), p, first.getRightp());

        FirstandLastNeighbours(first, A, B);

        Node n1 = first.getNode();
        n1.setType(Node.NodeType.POINT);
        n1.setFace(null);
        n1.setPoint(p);
        n1.setLchild(new Node(A));
        n1.setRchild(new Node(B));
        trapezoidalMap.remove(first);
        trapezoidalMap.add(A);
        trapezoidalMap.add(B);
        return B;
    }

    /**
     *
     */
    private void FirstandLastNeighbours(Face f, Face A, Face B) {
        A.setAllSideNeighbours(f.getUpperLeftNeighbour(), f.getLowerLeftNeighbour(), B, B);
        B.setAllSideNeighbours(A, A, f.getUpperRightNeighbour(), f.getLowerRightNeighbour());

        if (f.getUpperLeftNeighbour() != null) {
            if (f.getUpperLeftNeighbour().getUpperRightNeighbour() == f) {
                f.getUpperLeftNeighbour().setUpperRightNeighbour(A);
            }
            if (f.getUpperLeftNeighbour().getLowerRightNeighbour() == f) {
                f.getUpperLeftNeighbour().setLowerRightNeighbour(A);
            }
        }
        if (f.getLowerLeftNeighbour() != null) {
            if (f.getLowerLeftNeighbour().getLowerRightNeighbour() == f) {
                f.getLowerLeftNeighbour().setLowerRightNeighbour(A);
            }
            if (f.getLowerLeftNeighbour().getUpperRightNeighbour() == f) {
                f.getLowerLeftNeighbour().setUpperRightNeighbour(A);
            }
        }
        if (f.getUpperRightNeighbour() != null) {
            if (f.getUpperRightNeighbour().getUpperLeftNeighbour() == f) {
                f.getUpperRightNeighbour().setUpperLeftNeighbour(B);
            }
            if (f.getUpperRightNeighbour().getLowerLeftNeighbour() == f) {
                f.getUpperRightNeighbour().setLowerLeftNeighbour(B);
            }
        }
        if (f.getLowerRightNeighbour() != null) {
            if (f.getLowerRightNeighbour().getLowerLeftNeighbour() == f) {
                f.getLowerRightNeighbour().setLowerLeftNeighbour(B);
            }
            if (f.getLowerRightNeighbour().getUpperLeftNeighbour() == f) {
                f.getLowerRightNeighbour().setUpperLeftNeighbour(B);
            }
        }
    }

    /**
     * Calculate the first face when there are more than 1 intersection faces
     *
     * @param first
     * @param ls
     * @param p
     * @return
     */
    private Point calculateFirstForMiddle(Face first, LineSegment ls, Point p) {
        int firstY = ls.getIntersection(first.getRightp().x);
        Point intersect = new Point(first.getRightp().x, firstY);
        if (firstY > first.getRightp().y) {
            calculateTopandBottomForMiddle(first, p, intersect, p, first.getRightp(), ls);
        } else {
            calculateTopandBottomForMiddle(first, p, first.getRightp(), p, intersect, ls);
        }
        return intersect;
    }

    /**
     * Calculate the last face when there are more than 1 intersection faces
     */
    private void calculateLastForMiddle(Face last, Point intersect, Point q, LineSegment ls) {
        if (intersect.y > last.getLeftp().y) {
            calculateTopandBottomForMiddle(last, intersect, q, intersect, q, ls);
        } else {
            calculateTopandBottomForMiddle(last, last.getLeftp(), q, intersect, q, ls);
        }
    }

    /**
     * Calculate new top and bottom face for face f and linesegment ls
     *
     * @param f
     * @param p
     * @param q
     * @param r
     * @param u
     * @param ls
     */
    private void calculateTopandBottomForMiddle(Face f, Point p, Point q, Point r, Point u, LineSegment ls) {
        Face C = new Face(f.getTop(), ls, p, q);
        Face D = new Face(ls, f.getBottom(), r, u);

        Face invis1 = null;
        Face invis2 = null;

        if (trapezoidalMap.size() > 2) {
            invis1 = trapezoidalMap.get(trapezoidalMap.size() - 1);
            invis2 = trapezoidalMap.get(trapezoidalMap.size() - 2);
        }

        if (presentPoints.contains(p)) {
            if (invis2 == null) {
                C.setAllSideNeighbours(f.getUpperLeftNeighbour(), f.getUpperLeftNeighbour(), f.getUpperRightNeighbour(), f.getUpperRightNeighbour());
                D.setAllSideNeighbours(f.getLowerLeftNeighbour(), f.getLowerLeftNeighbour(), f.getLowerRightNeighbour(), f.getLowerRightNeighbour());
            } else if ((invis2.getLowerRightNeighbour() == f) && f.getLowerLeftNeighbour() != invis2) {
                C.setAllSideNeighbours(f.getUpperLeftNeighbour(), invis2, f.getUpperRightNeighbour(), f.getUpperRightNeighbour());
                D.setAllSideNeighbours(f.getLowerLeftNeighbour(), f.getLowerLeftNeighbour(), f.getLowerRightNeighbour(), f.getLowerRightNeighbour());
                invis2.setUpperRightNeighbour(C);
                invis2.setLowerRightNeighbour(C);
            } else {
                C.setAllSideNeighbours(f.getUpperLeftNeighbour(), f.getUpperLeftNeighbour(), f.getUpperRightNeighbour(), f.getUpperRightNeighbour());
                D.setAllSideNeighbours(f.getLowerLeftNeighbour(), f.getLowerLeftNeighbour(), f.getLowerRightNeighbour(), f.getLowerRightNeighbour());
            }

        } else {
            if (invis1 == null) {
                C.setAllSideNeighbours(f.getUpperLeftNeighbour(), f.getUpperLeftNeighbour(), f.getUpperRightNeighbour(), f.getUpperRightNeighbour());
                D.setAllSideNeighbours(f.getLowerLeftNeighbour(), f.getLowerLeftNeighbour(), f.getLowerRightNeighbour(), f.getLowerRightNeighbour());
            } else if (invis1.getLowerRightNeighbour() == f && f.getUpperLeftNeighbour() != invis1) {
                C.setAllSideNeighbours(f.getUpperLeftNeighbour(), f.getUpperLeftNeighbour(), f.getUpperRightNeighbour(), f.getUpperRightNeighbour());
                D.setAllSideNeighbours(invis1, f.getLowerLeftNeighbour(), f.getLowerRightNeighbour(), f.getLowerRightNeighbour());
                invis1.setUpperRightNeighbour(D);
                invis1.setLowerRightNeighbour(D);
            } else {
                C.setAllSideNeighbours(f.getUpperLeftNeighbour(), f.getUpperLeftNeighbour(), f.getUpperRightNeighbour(), f.getUpperRightNeighbour());
                D.setAllSideNeighbours(f.getLowerLeftNeighbour(), f.getLowerLeftNeighbour(), f.getLowerRightNeighbour(), f.getLowerRightNeighbour());
            }
        }
        
        if(presentPoints.contains(q)) {
            C.setLowerRightNeighbour(f.getLowerRightNeighbour());
        }
        if(presentPoints.contains(u)) {
            D.setUpperRightNeighbour(f.getUpperRightNeighbour());
        }

        if (f.getTop().getStartPoint().equals(ls.getStartPoint())) {
            C.setLowerLeftNeighbour(null);
            C.setUpperLeftNeighbour(null);
        }

        if (f.getBottom().getStartPoint().equals(ls.getStartPoint())) {
            D.setUpperLeftNeighbour(null);
            D.setLowerLeftNeighbour(null);
        }

        if (f.getTop().getEndPoint().equals(ls.getEndPoint())) {
            C.setUpperRightNeighbour(null);
            C.setLowerRightNeighbour(null);
        }
        
        if (f.getBottom().getEndPoint().equals(ls.getEndPoint())){
            D.setUpperRightNeighbour(null);
            D.setLowerRightNeighbour(null);
        }

        if (f.getLowerLeftNeighbour() != null) {
            if (f.getLowerLeftNeighbour().getUpperRightNeighbour() == f) {
                if (f.getLowerLeftNeighbour().getTop().getEndPoint().y <= ls.getStartPoint().y) {
                    f.getLowerLeftNeighbour().setUpperRightNeighbour(D);
                } else {
                    f.getLowerLeftNeighbour().setUpperRightNeighbour(C);
                }
            }
            if (f.getLowerLeftNeighbour().getLowerRightNeighbour() == f) {
                f.getLowerLeftNeighbour().setLowerRightNeighbour(D);
            }
        }
        if (f.getLowerRightNeighbour() != null) {
            if (f.getLowerRightNeighbour().getUpperLeftNeighbour() == f) {
                if (f.getLowerRightNeighbour().getTop().getStartPoint().y <= ls.getEndPoint().y) {
                    f.getLowerRightNeighbour().setUpperLeftNeighbour(D);
                } else {
                    f.getLowerRightNeighbour().setUpperLeftNeighbour(C);
                }
            }
            if (f.getLowerRightNeighbour().getLowerLeftNeighbour() == f) {
                f.getLowerRightNeighbour().setLowerLeftNeighbour(D);
            }
        }
        if (f.getUpperLeftNeighbour() != null) {
            if (f.getUpperLeftNeighbour().getUpperRightNeighbour() == f) {
                f.getUpperLeftNeighbour().setUpperRightNeighbour(C);
            }
            if (f.getUpperLeftNeighbour().getLowerRightNeighbour() == f) {
                if (f.getUpperLeftNeighbour().getBottom().getEndPoint().y >= ls.getStartPoint().y) {
                    f.getUpperLeftNeighbour().setLowerRightNeighbour(C);
                } else {
                    f.getUpperLeftNeighbour().setLowerRightNeighbour(D);
                }
            }
        }
        if (f.getUpperRightNeighbour() != null) {
            if (f.getUpperRightNeighbour().getUpperLeftNeighbour() == f) {
                f.getUpperRightNeighbour().setUpperLeftNeighbour(C);
            }
            if (f.getUpperRightNeighbour().getLowerLeftNeighbour() == f) {
                if (f.getUpperRightNeighbour().getBottom().getStartPoint().y >= ls.getEndPoint().y) {
                    f.getUpperRightNeighbour().setLowerLeftNeighbour(C);
                } else {
                    f.getUpperRightNeighbour().setLowerLeftNeighbour(D);
                }
            }
        }

        trapezoidalMap.remove(f);
        trapezoidalMap.add(C);
        trapezoidalMap.add(D);

        Node leaf = f.getNode();
        leaf.setType(Node.NodeType.SEGMENT);
        leaf.setFace(null);
        leaf.setSegment(ls);
        Node rchild = new Node(C);
        leaf.setRchild(rchild);
        Node lchild = new Node(D);
        leaf.setLchild(lchild);
        if (!(presentPoints.contains(p) && presentPoints.contains(q))) {
            mergeFaces.add(C);
        }
        if (!(presentPoints.contains(r) && presentPoints.contains(u))) {
            mergeFaces.add(D);
        }
    }

    /**
     * Set the correct neighbours after a merge
     *
     * @param merged
     * @param f1
     * @param f2
     */
    private void setMergeNeighbours(Face merged, Face f1, Face f2) {
        if (f1.getLowerRightNeighbour() != null) {
            if (f1.getLowerRightNeighbour().getLowerLeftNeighbour() == f1) {
                f1.getLowerRightNeighbour().setLowerLeftNeighbour(merged);
            }
            if (f1.getLowerRightNeighbour().getUpperLeftNeighbour() == f1) {
                f1.getLowerRightNeighbour().setUpperLeftNeighbour(merged);
            }
        }
        if (f1.getUpperRightNeighbour() != null) {
            if (f1.getUpperRightNeighbour().getLowerLeftNeighbour() == f1) {
                f1.getUpperRightNeighbour().setLowerLeftNeighbour(merged);
            }
            if (f1.getUpperRightNeighbour().getUpperLeftNeighbour() == f1) {
                f1.getUpperRightNeighbour().setUpperLeftNeighbour(merged);
            }
        }
        if (f2.getLowerLeftNeighbour() != null) {
            if (f2.getLowerLeftNeighbour().getLowerRightNeighbour() == f2) {
                f2.getLowerLeftNeighbour().setLowerRightNeighbour(merged);
            }
            if (f2.getLowerLeftNeighbour().getUpperRightNeighbour() == f2) {
                f2.getLowerLeftNeighbour().setUpperRightNeighbour(merged);
            }
        }
        if (f2.getUpperLeftNeighbour() != null) {
            if (f2.getUpperLeftNeighbour().getLowerRightNeighbour() == f2) {
                f2.getUpperLeftNeighbour().setLowerRightNeighbour(merged);
            }
            if (f2.getUpperLeftNeighbour().getUpperRightNeighbour() == f2) {
                f2.getUpperLeftNeighbour().setUpperRightNeighbour(merged);
            }
        }
        merged.setAllSideNeighbours(f2.getUpperLeftNeighbour(), f2.getLowerLeftNeighbour(), f1.getUpperRightNeighbour(), f1.getLowerRightNeighbour());
    }

    /**
     * Update the tree after a merge
     *
     * @param merged
     * @param f1
     * @param f2
     * @param left
     */
    private void setMergeTree(Face merged, Face f1, Face f2, boolean left) {
        Node n1 = f1.getNode();
        Node n2 = f2.getNode();
        //n1.setFace(null);
        //n2.setFace(null);
        Node nmerged = new Node(merged);
        Set<Node> parents = new HashSet<>();
        parents.addAll(n1.getParents());
        parents.addAll(n2.getParents());
        for (Node parent : parents) {
            if (parent.getLchild() == n1 || parent.getLchild() == n2) {
                assert !DrawInterface.ASSERTIONS || parent.getLchild() == n1 || parent.getLchild() == n2 : "child=" + parent.getLchild() + ", n1=" + n1 + ", n2=" + n2;
                parent.setLchild(nmerged);
                parent.setLchild(nmerged);
            } else {
                assert !DrawInterface.ASSERTIONS || parent.getRchild() == n1 || parent.getRchild() == n2 : "child=" + parent.getRchild() + ", n1=" + n1 + ", n2=" + n2;
                parent.setRchild(nmerged);
                parent.setRchild(nmerged);
            }
        }
        assert !DrawInterface.ASSERTIONS || (!inGraph(n1) && !inGraph(n2)) :
                "Merged node is still in graph";
    }

    /**
     * Get the faces that linesegment ls intersects
     *
     * @param ls
     * @return
     */
    private List<Face> getIntersectedFaces(LineSegment ls) {

        List<Face> faces = new ArrayList<>();

        Point startp = ls.getStartPoint();
        Point endp = ls.getEndPoint();

        Face start = searchGraph.getFace(startp, endp);

        faces.add(start);

        while (start != null && (start.getRightp() != null && endp.x > start.getRightp().x)) {
            if (ls.belowPoint(start.getRightp())) {
                start = start.getLowerRightNeighbour();
            } else {
                start = start.getUpperRightNeighbour();
            }
            if (start != null) {
                faces.add(start);
            }
        }

        return faces;
    }

    public List<LineSegment> getResult() {
        if (SEPARATE_LINES) {
            Set<Integer> xs = new HashSet<>();
            for (Face f : trapezoidalMap) {
                while (xs.contains(f.getLeftp().x)) {
                    Point p = f.getLeftp();
                    f.setLeftp(new Point(p.x + 2, p.y));
                }
                while (xs.contains(f.getRightp().x)) {
                    Point p = f.getRightp();
                    f.setRightp(new Point(p.x - 2, p.y));
                }
                xs.add(f.getLeftp().x);
                xs.add(f.getRightp().x);
            }
        }
        List<LineSegment> result = new ArrayList<>();
        for (Face f : trapezoidalMap) {
            result.add(f.getLeftLineSegment());
            result.add(f.getRightLineSegment());
        }
        return result;
    }

    public List<Face> getFaces() {
        return trapezoidalMap;
    }

    private boolean inGraph(Node n) {
        return this.searchGraph.contains(n);
    }

    private void checkInvariant() {
        assert !DrawInterface.ASSERTIONS
                || invariant() : "Graph does not contain the right faces";
        assert !DrawInterface.ASSERTIONS
                || invariant2() : "Neighbours not updated correctly";
    }

    public boolean invariant() {
        Set<Face> storedFaces = new HashSet<>(trapezoidalMap);
        Set<Face> graphFaces = searchGraph.allFaces();
        boolean result = storedFaces.equals(graphFaces);
        if (!result) {
            for (Face f : storedFaces) {
                if (!graphFaces.contains(f)) {
                    System.err.println(f.toString() + " missing in graph");
                }
            }
            for (Face f : graphFaces) {
                if (!storedFaces.contains(f)) {
                    System.err.println(f.toString() + " missing in map");
                }
            }
        }
        return result;
    }

    public boolean invariant2() {
        Set<Face> storedFaces = new HashSet<>(trapezoidalMap);
        for (Face f : storedFaces) {
            Face[] neighbours = f.getNeighbours();
            for (Face g : neighbours) {
                if (g != null && !storedFaces.contains(g)) {
                    System.err.println(g.toString() + " is still neighbour of "
                            + f.toString() + ", but it has been removed.");
                    return false;
                }
            }
        }
        return true;
    }
}
