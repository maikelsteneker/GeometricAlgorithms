package nl.tue.win.ga.model;

import java.awt.Point;
import java.util.HashSet;
import java.util.Set;
import nl.tue.win.ga.gui.DrawInterface;
import nl.tue.win.ga.model.Node.NodeType;

/**
 *
 * @author s102231
 */
public class Graph {
    /**
     * Root of the graph
     */
    private final Node root;
    
    public Graph(Node root){
        this.root = root;
    }
    
    /**
     * Function to get the face this point is in
     * @param p point to find the face for
     * @return Current face the point is in
     */
    public Face getFace(Point p, Point p1){   
        Node check = this.root;
        while (check.getType() != NodeType.LEAF){
            if (check.getType() == NodeType.POINT){
                if (check.getPoint().x > p.x) {
                    check = check.getLchild();
                } else {
                    check = check.getRchild();
                }
            } else if (check.getType() == NodeType.SEGMENT){
                if (check.getSegment().getStartPoint() == p){
                    if (check.getSegment().getEndPoint().y < p1.y){
                        check = check.getRchild();
                    } else {
                        check = check.getLchild();
                    } 
                } else {
                    if (check.getSegment().belowPoint(p)){
                        check = check.getRchild();
                    } else {
                        check = check.getLchild();
                    }
                }
            } else {
                throw new IllegalArgumentException("Reached the last else in getFace");
            }
        }
        final Face result = check.getFace();
        assert !DrawInterface.ASSERTIONS ||
                result.contains(p): "Graph returns face that does not contain"
                + "query point (" + result.toString() + " for point " + p + ")";
        return result;
    }
    
    public Set<Face> allFaces() {
        Set<Face> result = new HashSet<>();
        allFaces(root, result);
        return result;
    }

    private static void allFaces(Node n, Set<Face> result) {
        if (n != null) {
            if (n.getFace() != null) {
                result.add(n.getFace());
            }
            if (n.getLchild() != null) {
                allFaces(n.getLchild(), result);
            }
            if (n.getRchild() != null) {
                allFaces(n.getRchild(), result);
            }
        }
    }
}
