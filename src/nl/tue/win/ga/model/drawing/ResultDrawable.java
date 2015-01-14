package nl.tue.win.ga.model.drawing;

import java.awt.Graphics;
import java.util.ArrayList;
import java.util.Random;
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
            float f = new Random().nextFloat();
            int r1 = new Random().nextInt(1000);
            int r11 = new Random().nextInt(r1);
            int r2 = new Random().nextInt(1000);
            int r22 = new Random().nextInt(r2);
            g.setColor(java.awt.Color.getHSBColor(f, 1.0f-(float)r11/r1, 1.0f-(float)r22/r2));
            s.drawingUtilities = drawingUtilities;
            s.draw(g, scale, invertY);
        }
        for (LineSegment s : bb) {
            s.drawingUtilities = drawingUtilities;
            s.draw(g, scale, invertY);
        }
    }

}
