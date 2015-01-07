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
    
    public Face(){
        top = null;
        bottom = null;
        leftp = null;
        rightp = null;
    }
    
    public Face(LineSegment top, LineSegment bottom, Point leftp, Point rightp){
        this.top = top;
        this.bottom = bottom;
        this.leftp = leftp;
        this.rightp = rightp;
    }
    
    public void setAllNeighbours(Face[] neighbours){
        this.neighbours = neighbours;
    }
    
    public Face getTopNeighbour() {
        return neighbours[0];
    }
    
    public void setTopNeighbour(Face top){
        neighbours[0] = top;
    }
    
    public Face getBottomNeighbour() {
        return neighbours[1];
    }
    
    public void setBottomNeighbour(Face bot){
        neighbours[0] = bot;
    }
    
    public Face getLeftNeighbour() {
        return neighbours[2];
    }
    
    public void setLeftNeighbour(Face left){
        neighbours[0] = left;
    }
    
    public Face getRightNeighbour() {
        return neighbours[3];
    }
    
    public void setRightNeighbour(Face right){
        neighbours[0] = right;
    }
}
