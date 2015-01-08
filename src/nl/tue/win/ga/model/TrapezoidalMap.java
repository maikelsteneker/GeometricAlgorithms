/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package nl.tue.win.ga.model;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import nl.tue.win.ga.model.Node.NodeType;

/**
 *
 * @author s102231
 */
public class TrapezoidalMap {

    /**
     * list containing all the line segments (to be inserted)
     */
    private ArrayList<LineSegment> linesegments = new ArrayList<>();
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
    private Random randomGen = new Random();
    /**
     * If the actual trapezoidalMap has been made
     */
    private Boolean done = false;
    
    private ArrayList<Face> trapFaces = new ArrayList<>();

    public TrapezoidalMap() {
        //init the bounding box
        boundingbox = new Face();
        Face[] neighbours = {null, null, null, null, null, null};
        boundingbox.setAllNeighbours(neighbours);
        //init the graph
        Node root = new Node();
        root.setFace(boundingbox);
        graph = new Graph(root);
    }

    public TrapezoidalMap(ArrayList<LineSegment> linesegments) {
        this.linesegments = linesegments;
        graph = null;
        //init the bounding box
        boundingbox = new Face();
        Face[] neighbours = {null, null, null, null, null, null};
        boundingbox.setAllNeighbours(neighbours);
    }

    public void makeTrapezoidalMap() {

        ArrayList<LineSegment> handled = new ArrayList<>();

        while (linesegments.size() > 0) {
            LineSegment seg = getRandomLineSegment();
            linesegments.remove(seg);
            handled.add(seg);
            List<Face> intersections = getIntersectedFaces(seg);
            Point begin = seg.getStartPoint();
            Point end = seg.getEndPoint();
            
            if (intersections.size() == 1){
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
                
                Node n = face.getNode();
                
                
            } else {
                Face prev = null;
                Face upper = null;
                Face lower = null;
                for (Face intersect: intersections) {
                    
                    trapFaces.remove(intersect);
                    //this is the first of the intersections
                    if (intersections.indexOf(intersect) == 0){
                        
                        prev = intersect;
                        
                        Face A = new Face(intersect.getTop(), intersect.getBottom(), intersect.getLeftp(), begin);
                        Face C = new Face(intersect.getTop(), seg, begin, intersect.getRightp());
                        Face D = new Face(seg, intersect.getBottom(), begin, intersect.getRightp());
                        A.setAllSideNeighbours(intersect.getUpperLeftNeighbour(), intersect.getLowerLeftNeighbour(), C, D);
                        C.setAllSideNeighbours(A, A, intersect.getUpperRightNeighbour(), null);
                        D.setAllSideNeighbours(A, A, null, intersect.getLowerRightNeighbour());
                        
                        upper = C;
                        lower = D;
                        
                        
                    } else if (intersections.indexOf(intersect) == intersections.size() - 1 ){
                        //this is the last of the intersected faces
                        
                        Face B = new Face(intersect.getTop(), intersect.getBottom(), intersect.getRightp(), end);
                        Face C = new Face(intersect.getTop(), seg, intersect.getLeftp(), end);
                        Face D = new Face(seg, intersect.getBottom(), intersect.getLeftp(), end);
                        B.setAllSideNeighbours(C, D, intersect.getUpperRightNeighbour(), intersect.getLowerRightNeighbour());
                                                
                        Face CUpper = (intersect.getUpperLeftNeighbour() == prev) ? upper : intersect.getUpperLeftNeighbour();
                        Face DLower = (intersect.getLowerLeftNeighbour() == prev) ? lower : intersect.getLowerLeftNeighbour();
                        
                        C.setAllSideNeighbours(CUpper, upper, B, B);
                        D.setAllSideNeighbours(lower, DLower, B, B);
                        
                        if (upper.getUpperRightNeighbour() == intersect){
                            upper.setUpperRightNeighbour(C);
                        }
                        
                        if (lower.getLowerRightNeighbour() == intersect){
                            lower.setLowerRightNeighbour(D);
                        }
                        
                        upper.setLowerRightNeighbour(C);
                        lower.setUpperRightNeighbour(D);
                        
                        
                    } else {
                        //these represent the faces between the first and last
                        
                        Face C = new Face(intersect.getTop(), seg, intersect.getLeftp(), intersect.getRightp());
                        Face D = new Face(seg, intersect.getBottom(), intersect.getLeftp(), intersect.getRightp());
                        
                        Face CUpper = (intersect.getUpperLeftNeighbour() == prev) ? upper : intersect.getUpperLeftNeighbour();
                        Face DLower = (intersect.getLowerLeftNeighbour() == prev) ? lower : intersect.getLowerLeftNeighbour();
                        
                        C.setAllSideNeighbours(CUpper, upper, intersect.getUpperRightNeighbour(), null);
                        D.setAllSideNeighbours(lower, DLower, null, intersect.getLowerRightNeighbour());
                        
                        if (upper.getUpperRightNeighbour() == intersect){
                            upper.setUpperRightNeighbour(C);
                        }
                        
                        if (lower.getLowerRightNeighbour() == intersect){
                            lower.setLowerRightNeighbour(D);
                        }
                        
                        upper.setLowerRightNeighbour(C);
                        lower.setUpperRightNeighbour(D);
                        
                        upper = C;
                        lower = D;                        
                        
                    }
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
}
