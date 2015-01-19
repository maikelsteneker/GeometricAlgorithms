package nl.tue.win.ga.model;

import java.awt.Point;
import java.util.HashSet;
import java.util.Set;
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
    private Node lchild, rchild;
    private final Set<Node> parents = new HashSet<>();
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
        this.parents.add(parent);
    }

    public NodeType getType() {
        return type;
    }

    public void setType(NodeType type) {
        this.type = type;
    }

    public void addParent(Node parent) {
        if (parent != null) {
            this.parents.add(parent);
        }
    }

    @Deprecated
    public Node getParent() {
        return this.parents.iterator().next(); // returns one of the parents
    }

    public Set<Node> getParents() {
        return this.parents;
    }

    public Node getLchild() {
        return lchild;
    }

    public void setLchild(Node lchild) {
        this.lchild = lchild;
        lchild.addParent(this);
    }

    public Node getRchild() {
        return rchild;
    }

    public void setRchild(Node rchild) {
        this.rchild = rchild;
        rchild.addParent(this);
    }

    public boolean isRoot() {
        return this.parents.isEmpty();
    }

    public void checkInvariant() {
        assert !DrawInterface.ASSERTIONS || this.invariant() :
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
