/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package nl.tue.win.ga.model;

import java.awt.Point;
import nl.tue.win.ga.model.Node.NodeType;

/**
 *
 * @author s102231
 */
public class Graph {
    /**
     * Root of the graph
     */
    private Node root;
    
    public Graph(Node root){
        this.root = root;
    }
    
    /**
     * Function to get the face this point is in
     * @param p point to find the face for
     * @return Current face the point is in
     */
    public Face getFace(Point p){   
        Node check = this.root;
        while (check.getType() != NodeType.LEAF){
            if (check.getType() == NodeType.POINT){
                if (check.getPoint().x > p.x) {
                    check = check.getLchild();
                } else {
                    check = check.getRchild();
                }
            } else if (check.getType() == NodeType.SEGMENT){
                if (check.getSegment().belowPoint(p)){
                    check = check.getRchild();
                } else {
                    check = check.getLchild();
                }
            } else {
                throw new IllegalArgumentException("Reached the last else in getFace");
            }
        }
        return check.getFace();
    }
}
