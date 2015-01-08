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
    
    public TrapezoidalMap() {
        linesegments = null;
        //init the bounding box
        boundingbox = new Face();
        Face[] neighbours = {null, null, null, null, null, null};
        boundingbox.setAllNeighbours(neighbours);
        //init the graph
        Node root = new Node();
        root.setFace(boundingbox);
        graph = new Graph(root);
    }
    
    public TrapezoidalMap(ArrayList<LineSegment> linesegments){
        this.linesegments = linesegments;
        graph = null;
        //init the bounding box
        boundingbox = new Face();
        Face[] neighbours = {null, null, null, null};
        boundingbox.setAllNeighbours(neighbours);        
    }
    
    private LineSegment getRandomLineSegment(){
        if (linesegments == null){
            
            return null;
            
        } else {
            
            int index = randomGen.nextInt(linesegments.size());
            return linesegments.get(index);
            
        }
    }
    
    private List<Face> getIntersectedFaces(LineSegment linesegment){
        
        List<Face> faces = new ArrayList<>();
        
        Point[] endpoints = linesegment.getEndPoints();
        Point startp = endpoints[0]; 
        Point endp = endpoints[1];
        
        Face start = graph.getFace(startp);
        
        faces.add(start);
        
        Face help = start;
        
        while (help != null && (help.getRightp() != null && endp.x > help.getRightp().x)){
            if (linesegment.belowPoint(help.getRightp())){
                help = help.getLowerRightNeighbour();
            } else {
                help = help.getUpperRightNeighbour();
            }
            if(help != null){
                faces.add(help);
            }
        }
        
        return faces;
    }
       
}
