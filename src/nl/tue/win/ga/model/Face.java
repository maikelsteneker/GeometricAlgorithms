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
    private Node node;

    private Face[] neighbours = new Face[6];

    public Face() {
        top = null;
        bottom = null;
        leftp = null;
        rightp = null;
    }

    public Face(LineSegment top, LineSegment bottom, Point leftp, Point rightp) {
        this.top = top;
        this.bottom = bottom;
        this.leftp = leftp;
        this.rightp = rightp;
    }

    public Face(Face upperleft, Face lowerleft, Face upperright, Face lowerright) {
        top = null;
        bottom = null;
        leftp = null;
        rightp = null;
        neighbours[2] = upperleft;
        neighbours[3] = lowerleft;
        neighbours[4] = upperright;
        neighbours[5] = lowerright;
    }

    public Node getNode() {
        return node;
    }

    public void setNode(Node node) {
        this.node = node;
    }

    public Point getLeftp() {
        return leftp;
    }

    public void setLeftp(Point leftp) {
        this.leftp = leftp;
    }

    public Point getRightp() {
        return rightp;
    }

    public void setRightp(Point rightp) {
        this.rightp = rightp;
    }

    public LineSegment getTop() {
        return top;
    }

    public void setTop(LineSegment top) {
        this.top = top;
    }

    public LineSegment getBottom() {
        return bottom;
    }

    public void setBottom(LineSegment bottom) {
        this.bottom = bottom;
    }

    public void setAllNeighbours(Face[] neighbours) {
        this.neighbours = neighbours;
    }

    public void setAllSideNeighbours(Face upperleft, Face lowerleft, Face upperright, Face lowerright) {

        neighbours[2] = upperleft;
        neighbours[3] = lowerleft;
        neighbours[4] = upperright;
        neighbours[5] = lowerright;

    }

    public Face getTopNeighbour() {
        return neighbours[0];
    }

    public void setTopNeighbour(Face top) {
        neighbours[0] = top;
    }

    public Face getBottomNeighbour() {
        return neighbours[1];
    }

    public void setBottomNeighbour(Face bot) {
        neighbours[1] = bot;
    }

    public Face getUpperLeftNeighbour() {
        return neighbours[2];
    }

    public void setUpperLeftNeighbour(Face left) {
        neighbours[2] = left;
    }

    public Face getLowerLeftNeighbour() {
        return neighbours[3];
    }

    public void setLowerLeftNeighbour(Face left) {
        neighbours[3] = left;
    }

    public Face getUpperRightNeighbour() {
        return neighbours[4];
    }

    public void setUpperRightNeighbour(Face right) {
        neighbours[4] = right;
    }

    public Face getLowerRightNeighbour() {
        return neighbours[5];
    }

    public void setLowerRightNeighbour(Face right) {
        neighbours[5] = right;
    }

    public LineSegment getLeftLineSegment() {
        return getSideLineSegment(true);
    }

    public LineSegment getRightLineSegment() {
        return getSideLineSegment(false);
    }

    private LineSegment getSideLineSegment(boolean left) {
        // left == true: left side; left == false: right side
        final int x = left ? getLeftp().x : getRightp().x;
        final int yLo = LineSegment.getIntersection(bottom, x);
        final int yHi = LineSegment.getIntersection(top, x);

        final Point topPoint = new Point(x, yHi);
        final Point bottomPoint = new Point(x, yLo);
        return new LineSegment(topPoint, bottomPoint, null);
    }
}
