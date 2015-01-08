package nl.tue.win.ga.model;

import java.awt.Point;

/**
 *
 * @author maikel
 */
public class Node {

    public enum NodeType {

        POINT, SEGMENT, LEAF
    }

    private NodeType type;
    private Node parent, lchild, rchild;
    private Face face;
    private Point point;
    private LineSegment segment;
    
    public Node(){
        
    }
    
    public Node(Face face){
        this.face = face;
        this.type = NodeType.LEAF;
    }
    
    public Node(Point point){
        this.point = point;
        this.type = NodeType.POINT;
    }
    
    public Node(LineSegment linesegment){
        this.segment = linesegment;
        this.type = NodeType.SEGMENT;
    }

    public LineSegment getSegment() {
        return segment;
    }

    public void setSegment(LineSegment segment) {
        this.segment = segment;
    }

    public Point getPoint() {
        return point;
    }

    public void setPoint(Point point) {
        this.point = point;
    }

    public Face getFace() {
        return face;
    }

    public void setFace(Face face) {
        this.face = face;
    }

    public Node(NodeType type, Node parent) {
        this.type = type;
        this.parent = parent;
    }

    public NodeType getType() {
        return type;
    }

    public void setType(NodeType type) {
        this.type = type;
    }

    public Node getParent() {
        return parent;
    }

    public void setParent(Node parent) {
        this.parent = parent;
    }

    public Node getLchild() {
        return lchild;
    }

    public void setLchild(Node lchild) {
        this.lchild = lchild;
        lchild.setParent(this);
    }

    public Node getRchild() {
        return rchild;
    }

    public void setRchild(Node rchild) {
        this.rchild = rchild;
        rchild.setParent(this);
    }
    
    public boolean isRoot() {
        return this.parent == null;
    }
}
