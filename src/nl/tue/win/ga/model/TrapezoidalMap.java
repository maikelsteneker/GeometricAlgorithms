/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package nl.tue.win.ga.model;

import java.util.ArrayList;
import java.util.Random;

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
    private Node tree;
    /**
     * Random Generator
     */
    private Random randomGen = new Random();    
    
    public TrapezoidalMap() {
        linesegments = null;
        tree = null;
        //init the bounding box
        boundingbox = new Face();
        Face[] neighbours = {null, null, null, null};
        boundingbox.setAllNeighbours(neighbours);
    }
    
    public TrapezoidalMap(ArrayList<LineSegment> linesegments){
        this.linesegments = linesegments;
        tree = null;
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
       
}
