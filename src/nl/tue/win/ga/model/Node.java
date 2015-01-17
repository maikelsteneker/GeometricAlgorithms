package nl.tue.win.ga.model;

import java.awt.Point;
import nl.tue.win.ga.gui.DrawInterface;

/**
 *
 * @author maikel
 */
public final class Node {

    public enum NodeType {

        POINT, SEGMENT, LEAF
    }

    private NodeType type;
    private Node parent, lchild, rchild;
    private Face face;
    private Point point;
    private LineSegment segment;

    public Node(Face face) {
        setFace(face);
        this.type = NodeType.LEAF;
    }

    public Node(Point point) {
        this.point = point;
        this.type = NodeType.POINT;
    }

    public Node(LineSegment linesegment) {
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
        checkInvariant();
        if (face != null) {
            face.setNode(this);
        }
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
    
    public void checkInvariant() {
        assert !DrawInterface.ASSERTIONS || this.invariant():
                "Leaf has a null face" + type + face;
    }
    
    public boolean invariant() {
        return this.type != NodeType.LEAF || this.face != null;
    }
    
    public boolean contains(Node n) {
        boolean result = this == n;
        if (this.getLchild() != null) {
            result |= this.getLchild().contains(n);
        }
        if (this.getRchild() != null) {
            result |= this.getRchild().contains(n);
        }
        return result;
    }
}
