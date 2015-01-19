package nl.tue.win.ga.algorithms;

import java.util.Iterator;
import java.util.List;
import java.util.Random;
import nl.tue.win.ga.model.LineSegment;

/**
 * Iterator to randomly select a line segment.
 *
 * @author maikel
 */
class RandomIterator implements Iterator<LineSegment> {
    
    private final List<LineSegment> linesegments;
    private final Random generator = new Random(0);

    public RandomIterator(List<LineSegment> linesegments) {
        this.linesegments = linesegments;
    }

    @Override
    public boolean hasNext() {
        return !linesegments.isEmpty();
    }

    @Override
    public LineSegment next() {
        if (linesegments == null) {
            return null;
        } else {
            int index = generator.nextInt(linesegments.size());
            final LineSegment result = linesegments.get(index);
            linesegments.remove(result);
            return result;
        }
    }
    
    @Override
    public void remove() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
