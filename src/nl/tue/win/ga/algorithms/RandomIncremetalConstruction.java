package nl.tue.win.ga.algorithms;

import java.awt.Point;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.Set;
import nl.tue.win.ga.gui.*;
import nl.tue.win.ga.model.*;
import nl.tue.win.ga.utilities.BoundingBox;

/**
 *
 * @author Coen
 */
public class RandomIncremetalConstruction {

    private List<LineSegment> edges = new ArrayList<>();
    private Face boundingBox;
    private Graph searchGraph;
    private boolean done = false;
    private List<Face> trapezoidalMap = new ArrayList<>();
    public int lastStep;
    private int currentStep;
    private final static boolean SEPARATE_LINES = true; // no overlapping face borders
    public List<LineSegment> handled = new ArrayList<>();

    public RandomIncremetalConstruction(List<Point> points) {
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
        while (iterator.hasNext()) {
            LineSegment s = iterator.next();
            List<Face> intersections = getIntersectedFaces(s);
            Point start = s.getStartPoint();
            Point end = s.getEndPoint();
            
            ArrayList<Face> newFaces = new ArrayList<>();
            
            if(intersections.size() == 1) {
                Face face = intersections.get(0);
                trapezoidalMap.remove(face);
                
                Face A = new Face(face.getTop(), face.getBottom(), face.getLeftp(), start);
                Face B = new Face(face.getTop(), face.getBottom(), end, face.getRightp());
                Face C = new Face(face.getTop(), s, start, end);
                Face D = new Face(s, face.getBottom(), start, end);
                
                A.setAllSideNeighbours(face.getUpperLeftNeighbour(), face.getLowerLeftNeighbour(), C, D);
                B.setAllSideNeighbours(C, D, face.getUpperRightNeighbour(), face.getLowerRightNeighbour());
                C.setAllSideNeighbours(A, A, B, B);
                D.setAllSideNeighbours(A, A, B, B);
                
            }

        }
    }

    private List<Face> getIntersectedFaces(LineSegment linesegment) {

        List<Face> faces = new ArrayList<>();

        Point[] endpoints = linesegment.getEndPoints();
        Point startp = endpoints[0];
        Point endp = endpoints[1];

        Face start = searchGraph.getFace(startp, endp);

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
}
