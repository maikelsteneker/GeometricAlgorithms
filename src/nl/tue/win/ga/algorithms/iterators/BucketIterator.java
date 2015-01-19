package nl.tue.win.ga.algorithms.iterators;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import nl.tue.win.ga.model.LineSegment;

/**
 * Iterator that distributes line segments in a fixed number of buckets.
 *
 * Segments within a bucket are sorted on size (descendingly), and then the
 * iterator goes through all buckets.
 *
 * @author maikel
 */
public class BucketIterator implements Iterator<LineSegment> {

    List<LineSegment>[] buckets;
    final static Comparator<LineSegment> COMP = Collections.reverseOrder(new LengthComparator());
    final Iterator<LineSegment>[] iterators;
    private int currentBucket = 0;

    /**
     * Constructs a bucket iterator.
     *
     * @param linesegments the segments
     * @param nBuckets the number of buckets to use
     */
    public BucketIterator(List<LineSegment> linesegments, int nBuckets) {
        buckets = new List[nBuckets];
        iterators = new Iterator[nBuckets];
        Iterator<LineSegment> it = new RandomIterator(linesegments);
        for (int i = 0; i < nBuckets; i++) {
            buckets[i] = new ArrayList<>(); // construct a bucket
        }
        int i = 0;
        while (it.hasNext()) {
            buckets[(i++) % buckets.length].add(it.next());
        }
        for (int j = 0; j < buckets.length; j++) {
            Collections.sort(buckets[j], COMP); // sort the bucket
            assert buckets[j].size() > 0 : "Empty bucket " + i;
            iterators[j] = buckets[j].iterator();
        }
        assert !it.hasNext() : "Leftover segments were not assigned to bucket";
    }

    public final static BucketIterator fixedSizeBucketIterator(List<LineSegment> lineSegments, int bucketSize) {
        return new BucketIterator(lineSegments, (lineSegments.size() / bucketSize)+1);
    }

    @Override
    public boolean hasNext() {
        return iterators[currentBucket].hasNext() || iterators[(currentBucket + 1) % iterators.length].hasNext();
    }

    @Override
    public LineSegment next() {
        if (iterators[currentBucket].hasNext() || currentBucket >= iterators.length) {
            return iterators[currentBucket].next();
        } else {
            return iterators[++currentBucket].next();
        }
    }

    @Override
    public void remove() {
        iterators[currentBucket].remove();
    }

    private static class LengthComparator implements Comparator<LineSegment> {

        @Override
        public int compare(LineSegment t, LineSegment t1) {
            final float l = t.getLength();
            final float l1 = t1.getLength();
            return l < l1 ? -1 // t shorter
                    : l > l1 ? 1 // t1 longer
                            : 0; // same length
        }

    }

}
