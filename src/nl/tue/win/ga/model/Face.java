package nl.tue.win.ga.model;

import java.awt.Point;

/**
 * Represents a face (as given in the lecture).
 *
 * @author maikel
 */
public class Face {

    private LineSegment top;
    private LineSegment bottom;
    private Point leftp;
    private Point rightp;

    private Face[] neighbours = new Face[4];
    
    public Face getTopNeighbour() {
        return neighbours[0];
    }
    
    public Face getBottomNeighbour() {
        return neighbours[1];
    }
    
    public Face getLeftNeighbour() {
        return neighbours[2];
    }
    
    public Face getRightNeighbour() {
        return neighbours[3];
    }
}
