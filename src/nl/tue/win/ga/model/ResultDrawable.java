package nl.tue.win.ga.model;

import java.awt.Graphics;
import nl.tue.win.ga.utilities.DrawingUtilities;

/**
 * Class to draw polygon with line segments.
 *
 * @author maikel
 */
public class ResultDrawable implements Drawable {

    private final SimplePolygon polygon;
    private final Iterable<LineSegment> segments;
    private final DrawingUtilities drawingUtilities;

    public ResultDrawable(SimplePolygon polygon, Iterable<LineSegment> segments) {
        this.polygon = polygon;
        this.segments = segments;
        this.drawingUtilities = polygon.drawingUtilities;
        for (LineSegment s : segments) {
            s.drawingUtilities = drawingUtilities;
        }
    }

    @Override
    public void draw(Graphics g, boolean scale, boolean invertY) {
        polygon.draw(g, scale, invertY);
        for (LineSegment s : segments) {
            s.draw(g, scale, invertY);
        }
    }

}
