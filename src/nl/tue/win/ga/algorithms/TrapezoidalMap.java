package nl.tue.win.ga.algorithms;

import java.awt.Point;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import nl.tue.win.ga.model.Face;
import nl.tue.win.ga.model.Graph;
import nl.tue.win.ga.model.LineSegment;
import nl.tue.win.ga.model.Node;
import nl.tue.win.ga.model.Node.NodeType;
import nl.tue.win.ga.model.PreprocessedEdges;
import nl.tue.win.ga.utilities.BoundingBox;

/**
 *
 * @author s102231
 */
public class TrapezoidalMap {

    /**
     * list containing all the line segments (to be inserted)
     */
    private List<LineSegment> linesegments = new ArrayList<>();
    /**
     * The bounding box of all the points
     */
    private Face boundingbox;
    /**
     * The node containing all the faces
     */
    private Graph graph;
    /**
     * Random Generator
     */
    private Random randomGen = new Random(20);
    /**
     * If the actual trapezoidalMap has been made
     */
    private boolean done = false;
    private ArrayList<Face> trapFaces = new ArrayList<>();

    private final static boolean SEPARATE_LINES = true; // no overlapping face borders

    public TrapezoidalMap() {
        /*//init the bounding box
         boundingbox = null;
         Face[] neighbours = {null, null, null, null, null, null};
         boundingbox.setAllNeighbours(neighbours);
         //init the graph
         graph = new Graph(new Node(boundingbox));*/
        this(new ArrayList<Point>());
    }

    public TrapezoidalMap(List<Point> points) {
        this.linesegments = PreprocessedEdges.preprocess(points);
        //init the bounding box
        boundingbox = new BoundingBox(points, 0.1f).asFace();
        Face[] neighbours = {null, null, null, null, null, null};
        boundingbox.setAllNeighbours(neighbours);
        Node root = new Node(boundingbox);
        boundingbox.setNode(root);
        graph = new Graph(root);
    }

    public void RandomIncrementalMap() {

        ArrayList<LineSegment> handled = new ArrayList<>();

        while (linesegments.size() > 0) {
            LineSegment seg = getRandomLineSegment();
            linesegments.remove(seg);
            handled.add(seg);
            List<Face> intersections = getIntersectedFaces(seg);
            Point begin = seg.getStartPoint();
            Point end = seg.getEndPoint();

            ArrayList<Face> newFaces = new ArrayList<>();

            if (intersections.size() == 1) {
                Face face = intersections.get(0);
                trapFaces.remove(face);

                Face A = new Face(face.getTop(), face.getBottom(), face.getLeftp(), begin);
                Face B = new Face(face.getTop(), face.getBottom(), end, face.getRightp());
                Face C = new Face(face.getTop(), seg, begin, end);
                Face D = new Face(seg, face.getBottom(), begin, end);

                A.setAllSideNeighbours(face.getUpperLeftNeighbour(), face.getUpperRightNeighbour(), C, D);
                B.setAllSideNeighbours(C, D, face.getUpperRightNeighbour(), face.getLowerRightNeighbour());
                C.setAllSideNeighbours(A, A, B, B);
                D.setAllSideNeighbours(A, A, B, B);

                trapFaces.add(A);
                trapFaces.add(B);
                trapFaces.add(C);
                trapFaces.add(D);

                if (face.getUpperLeftNeighbour() != null) {
                    if (face.getUpperLeftNeighbour().getUpperRightNeighbour() == face) {
                        face.getUpperLeftNeighbour().setUpperRightNeighbour(A);
                    }
                    face.getUpperLeftNeighbour().setLowerRightNeighbour(A);
                }

                if (face.getLowerLeftNeighbour() != null) {
                    if (face.getLowerLeftNeighbour().getLowerRightNeighbour() == face) {
                        face.getLowerLeftNeighbour().setLowerRightNeighbour(A);
                    }
                    face.getLowerLeftNeighbour().setUpperRightNeighbour(A);
                }

                if (face.getUpperRightNeighbour() != null) {
                    if (face.getUpperRightNeighbour().getUpperLeftNeighbour() == face) {
                        face.getUpperRightNeighbour().setUpperLeftNeighbour(B);
                    }
                    face.getUpperRightNeighbour().setLowerLeftNeighbour(B);
                }

                if (face.getLowerRightNeighbour() != null) {
                    if (face.getLowerRightNeighbour().getLowerLeftNeighbour() == face) {
                        face.getLowerRightNeighbour().setLowerLeftNeighbour(B);
                    }
                    face.getLowerRightNeighbour().setUpperLeftNeighbour(B);
                }

                Node n = face.getNode();

                n.setFace(null);
                n.setType(NodeType.POINT);
                n.setPoint(begin);

                //set lefttree
                Node leftchild = new Node(A);
                n.setLchild(leftchild);

                //set righttree
                Node rightchild = new Node(end);
                n.setRchild(rightchild);

                Node rrchild = new Node(B);
                rightchild.setRchild(rrchild);

                Node rlchild = new Node(seg);
                rightchild.setLchild(rlchild);

                Node rlrchild = new Node(C);
                rlchild.setRchild(rlrchild);

                Node rllchild = new Node(D);
                rlchild.setLchild(rllchild);

            } else {
                Face prev = null;
                Face upper = null;
                Face lower = null;
                for (Face intersect : intersections) {

                    trapFaces.remove(intersect);

                    //this is the first of the intersections
                    if (intersections.indexOf(intersect) == 0 && intersect.getRightp() != begin) {

                        prev = intersect;

                        Face A = new Face(intersect.getTop(), intersect.getBottom(), intersect.getLeftp(), begin);
                        Face C = new Face(intersect.getTop(), seg, begin, intersect.getRightp());
                        Face D = new Face(seg, intersect.getBottom(), begin, intersect.getRightp());
                        A.setAllSideNeighbours(intersect.getUpperLeftNeighbour(), intersect.getLowerLeftNeighbour(), C, D);
                        C.setAllSideNeighbours(A, A, intersect.getUpperRightNeighbour(), null);
                        D.setAllSideNeighbours(A, A, null, intersect.getLowerRightNeighbour());

                        upper = C;
                        lower = D;

                        if (intersect.getUpperLeftNeighbour() != null) {
                            if (intersect.getUpperLeftNeighbour().getUpperRightNeighbour() == intersect) {
                                intersect.getUpperLeftNeighbour().setUpperRightNeighbour(A);
                            }
                            intersect.getUpperLeftNeighbour().setLowerRightNeighbour(A);
                        }

                        if (intersect.getLowerLeftNeighbour() != null) {
                            if (intersect.getLowerLeftNeighbour().getLowerRightNeighbour() == intersect) {
                                intersect.getLowerLeftNeighbour().setLowerRightNeighbour(A);
                            }
                            intersect.getLowerLeftNeighbour().setUpperRightNeighbour(A);
                        }

                        trapFaces.add(A);
                        //trapFaces.add(C);
                        //trapFaces.add(D);

                        newFaces.add(C);
                        newFaces.add(D);

                        Node n = intersect.getNode();

                        n.setFace(null);
                        n.setType(NodeType.POINT);
                        n.setPoint(begin);

                        //set lefttree
                        Node leftchild = new Node(A);
                        n.setLchild(leftchild);

                        //set righttree
                        Node rightchild = new Node(seg);
                        n.setRchild(rightchild);

                        Node rrchild = new Node(C);
                        rightchild.setRchild(rrchild);

                        Node rlchild = new Node(D);
                        rightchild.setLchild(rlchild);

                    } else if (intersections.indexOf(intersect) == intersections.size() - 1 && intersect.getRightp() != end) {
                        //this is the last of the intersected faces

                        Face B = new Face(intersect.getTop(), intersect.getBottom(), intersect.getRightp(), end);
                        Face C = new Face(intersect.getTop(), seg, intersect.getLeftp(), end);
                        Face D = new Face(seg, intersect.getBottom(), intersect.getLeftp(), end);
                        B.setAllSideNeighbours(C, D, intersect.getUpperRightNeighbour(), intersect.getLowerRightNeighbour());

                        Face CUpper = (intersect.getUpperLeftNeighbour() == prev) ? upper : intersect.getUpperLeftNeighbour();
                        Face DLower = (intersect.getLowerLeftNeighbour() == prev) ? lower : intersect.getLowerLeftNeighbour();

                        C.setAllSideNeighbours(CUpper, upper, B, B);
                        D.setAllSideNeighbours(lower, DLower, B, B);

                        trapFaces.add(B);
                        //trapFaces.add(C);
                        //trapFaces.add(D);

                        newFaces.add(C);
                        newFaces.add(D);

                        if (upper.getUpperRightNeighbour() == intersect) {
                            upper.setUpperRightNeighbour(C);
                        }

                        if (lower.getLowerRightNeighbour() == intersect) {
                            lower.setLowerRightNeighbour(D);
                        }

                        upper.setLowerRightNeighbour(C);
                        lower.setUpperRightNeighbour(D);

                        if (intersect.getUpperRightNeighbour() != null) {
                            if (intersect.getUpperRightNeighbour().getUpperLeftNeighbour() == intersect) {
                                intersect.getUpperRightNeighbour().setUpperLeftNeighbour(B);
                            }
                            intersect.getUpperRightNeighbour().setLowerLeftNeighbour(B);
                        }

                        if (intersect.getLowerRightNeighbour() != null) {
                            if (intersect.getLowerRightNeighbour().getLowerLeftNeighbour() == intersect) {
                                intersect.getLowerRightNeighbour().setLowerLeftNeighbour(B);
                            }
                            intersect.getLowerRightNeighbour().setUpperLeftNeighbour(B);
                        }

                        Node n = intersect.getNode();
                        n.setFace(null);
                        n.setType(NodeType.POINT);
                        n.setPoint(end);

                        Node rchild = new Node(B);
                        n.setRchild(rchild);

                        Node lchild = new Node(seg);
                        n.setLchild(lchild);

                        Node lrchild = new Node(C);
                        lchild.setRchild(lrchild);

                        Node llchild = new Node(D);
                        lchild.setLchild(llchild);

                    } else {
                        //these represent the faces between the first and last

                        Face C = new Face(intersect.getTop(), seg, intersect.getLeftp(), intersect.getRightp());
                        Face D = new Face(seg, intersect.getBottom(), intersect.getLeftp(), intersect.getRightp());

                        Face CUpper = (intersect.getUpperLeftNeighbour() == prev) ? upper : intersect.getUpperLeftNeighbour();
                        Face DLower = (intersect.getLowerLeftNeighbour() == prev) ? lower : intersect.getLowerLeftNeighbour();

                        C.setAllSideNeighbours(CUpper, upper, intersect.getUpperRightNeighbour(), null);
                        D.setAllSideNeighbours(lower, DLower, null, intersect.getLowerRightNeighbour());

                        //trapFaces.add(C);
                        //trapFaces.add(D);
                        newFaces.add(C);
                        newFaces.add(D);

                        if (upper.getUpperRightNeighbour() == intersect) {
                            upper.setUpperRightNeighbour(C);
                        }

                        if (lower.getLowerRightNeighbour() == intersect) {
                            lower.setLowerRightNeighbour(D);
                        }

                        upper.setLowerRightNeighbour(C);
                        lower.setUpperRightNeighbour(D);

                        if (intersect.getUpperRightNeighbour() != null && intersect.getUpperRightNeighbour().getUpperLeftNeighbour() == intersect) {
                            intersect.getUpperRightNeighbour().setUpperLeftNeighbour(C);
                        }

                        if (intersect.getLowerRightNeighbour() != null && intersect.getLowerRightNeighbour().getLowerLeftNeighbour() == intersect) {
                            intersect.getLowerRightNeighbour().setLowerLeftNeighbour(D);
                        }

                        if (intersect.getUpperLeftNeighbour() != null && intersect.getUpperLeftNeighbour().getUpperRightNeighbour() == intersect) {
                            intersect.getUpperLeftNeighbour().setUpperRightNeighbour(C);
                        }

                        if (intersect.getLowerLeftNeighbour() != null && intersect.getLowerLeftNeighbour().getLowerRightNeighbour() == intersect) {
                            intersect.getLowerLeftNeighbour().setLowerRightNeighbour(D);
                        }

                        upper = C;
                        lower = D;

                        Node n = intersect.getNode();
                        n.setFace(null);
                        n.setType(NodeType.SEGMENT);
                        n.setSegment(seg);

                        Node rchild = new Node(C);
                        n.setRchild(rchild);

                        Node lchild = new Node(D);
                        n.setLchild(lchild);
                    }
                }
            }

            //start possible merges
            while (!newFaces.isEmpty()) {
                final Face f = newFaces.get(0);
                if (f.getUpperRightNeighbour() != null && f.getUpperRightNeighbour().getTop() == f.getTop() && f.getUpperRightNeighbour().getBottom() == f.getBottom()) {
                    Face neigh = f.getUpperRightNeighbour();
                    f.setUpperRightNeighbour(neigh.getUpperRightNeighbour());
                    f.setLowerRightNeighbour(neigh.getLowerRightNeighbour());
                    f.setRightp(neigh.getRightp());

                    Node neighn = neigh.getNode();
                    if (neighn.getParent().getLchild() == neighn) {
                        neighn.getParent().setLchild(f.getNode());
                    } else {
                        neighn.getParent().setRchild(f.getNode());
                    }

                    newFaces.remove(neigh);
                } else if (f.getLowerRightNeighbour() != null && f.getLowerRightNeighbour().getBottom() == f.getBottom() && f.getLowerRightNeighbour().getTop() == f.getTop()) {
                    Face neigh = f.getLowerRightNeighbour();
                    f.setUpperRightNeighbour(neigh.getUpperRightNeighbour());
                    f.setLowerRightNeighbour(neigh.getLowerRightNeighbour());
                    f.setRightp(neigh.getRightp());

                    Node neighn = neigh.getNode();
                    if (neighn.getParent().getLchild() == neighn) {
                        neighn.getParent().setLchild(f.getNode());
                    } else {
                        neighn.getParent().setRchild(f.getNode());
                    }

                    newFaces.remove(neigh);
                } else {
                    newFaces.remove(f);
                    trapFaces.add(f);
                }
            }

        }
        linesegments = handled;
    }

    private LineSegment getRandomLineSegment() {
        if (linesegments == null) {

            return null;

        } else {

            int index = randomGen.nextInt(linesegments.size());
            return linesegments.get(index);

        }
    }

    private List<Face> getIntersectedFaces(LineSegment linesegment) {

        List<Face> faces = new ArrayList<>();

        Point[] endpoints = linesegment.getEndPoints();
        Point startp = endpoints[0];
        Point endp = endpoints[1];

        Face start = graph.getFace(startp);

        faces.add(start);

        Face help = start;

        while (help != null && (help.getRightp() != null && endp.x > help.getRightp().x)) {
            if (linesegment.belowPoint(help.getRightp())) {
                help = help.getLowerRightNeighbour();
            } else {
                help = help.getUpperRightNeighbour();
            }
            if (help != null) {
                faces.add(help);
            }
        }

        return faces;
    }

    public List<LineSegment> getResult() {
        if (SEPARATE_LINES) {
            Set<Integer> xs = new HashSet<>();
            for (Face f : trapFaces) {
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
        for (Face f : trapFaces) {
            result.add(f.getLeftLineSegment());
            result.add(f.getRightLineSegment());
        }
        return result;
    }

    public List<Face> getFaces() {
        return trapFaces;
    }
}
