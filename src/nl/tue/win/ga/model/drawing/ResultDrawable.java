package nl.tue.win.ga.model.drawing;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.Random;
import nl.tue.win.ga.model.Face;
import nl.tue.win.ga.model.LineSegment;
import nl.tue.win.ga.model.SimplePolygon;
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
    private final Random generator = new Random(0);
    private final Iterable<Face> faces;
    private final static boolean COLORED_LINES = true;
    private final Iterable<LineSegment> partial;

    public ResultDrawable(SimplePolygon polygon, Iterable<LineSegment> segments) {
        this(polygon, segments, new ArrayList<LineSegment>());
    }

    public ResultDrawable(SimplePolygon polygon, Iterable<LineSegment> segments,
            Iterable<LineSegment> bb) {
        this(polygon, segments, bb, new ArrayList<Face>());
    }

    public ResultDrawable(SimplePolygon polygon, Iterable<LineSegment> segments,
            Iterable<LineSegment> bb, Iterable<Face> faces) {
        this(polygon, segments, bb, faces, new ArrayList<LineSegment>());
    }
    
    public ResultDrawable(SimplePolygon polygon, Iterable<LineSegment> segments,
            Iterable<LineSegment> bb, Iterable<Face> faces, Iterable<LineSegment> partial) {
        this.polygon = polygon;
        this.segments = segments;
        this.drawingUtilities = polygon.drawingUtilities;
        this.bb = bb;
        this.faces = faces;
        this.partial = partial;
    }

    @Override
    public void draw(Graphics g, boolean scale, boolean invertY) {
        polygon.draw(g, scale, invertY);
        for (LineSegment s : segments) {
            final Color c = new Color(generator.nextInt());
            final Color old = g.getColor();
            if (COLORED_LINES) {
                g.setColor(c);
            }
            s.drawingUtilities = drawingUtilities;
            s.draw(g, scale, invertY);
            g.setColor(old);
        }
        for (LineSegment s : bb) {
            s.drawingUtilities = drawingUtilities;
            s.draw(g, scale, invertY);
        }
        for (Face f : faces) {
            f.drawingUtilities = drawingUtilities;
            f.draw(g, scale, invertY);
        }
        for (LineSegment s : partial) {
            s.drawingUtilities = drawingUtilities;
            s.draw(g, scale, invertY);
        }
    }

}
