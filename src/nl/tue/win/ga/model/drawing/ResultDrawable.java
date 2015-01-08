package nl.tue.win.ga.model.drawing;

import java.awt.Graphics;
import java.util.ArrayList;
import nl.tue.win.ga.model.LineSegment;
import nl.tue.win.ga.model.SimplePolygon;
import nl.tue.win.ga.utilities.BoundingBox;
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
    private final Iterable<LineSegment> bb;

    public ResultDrawable(SimplePolygon polygon, Iterable<LineSegment> segments) {
        this(polygon,segments, new ArrayList<LineSegment>());
    }
    
    public ResultDrawable(SimplePolygon polygon, Iterable<LineSegment> segments,
            Iterable<LineSegment> bb) {
        this.polygon = polygon;
        this.segments = segments;
        this.drawingUtilities = polygon.drawingUtilities;
        this.bb = bb;
    }

    @Override
    public void draw(Graphics g, boolean scale, boolean invertY) {
        polygon.draw(g, scale, invertY);
        for (LineSegment s : segments) {
            s.drawingUtilities = drawingUtilities;
            s.draw(g, scale, invertY);
        }
        for (LineSegment s : bb) {
            s.drawingUtilities = drawingUtilities;
            s.draw(g, scale, invertY);
        }
    }

}
