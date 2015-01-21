package nl.tue.win.ga.generate;

import java.awt.Point;
import java.io.IOException;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Random;
import nl.tue.win.ga.io.WritePolygonToFile;
import nl.tue.win.ga.model.SimplePolygon;

/**
 * Class dedicated to generating random polygons.
 *
 * @author maikel
 */
public class GenerateRandomPolygon {

    final static int XRES = Integer.MAX_VALUE / 100;
    final static int YRES = Integer.MAX_VALUE / 100;
    final static int MAX_SIZE = 100;
    final static private Random GENERATOR = new Random();

    /**
     * Generates a random point.
     *
     * @return a point {@code p} with {@code 0 <= p.x < XRES && 0 <= p.y < YRES}
     */
    public static Point generateRandomPoint() {
        return new Point(GENERATOR.nextInt(XRES), GENERATOR.nextInt(YRES));
    }

    public static SimplePolygon generateRandomPolygon() {
        return generateRandomPolygon(GENERATOR.nextInt(MAX_SIZE));
    }

    public static SimplePolygon generateRandomPolygon(int size) {
        final Point[] hull = new Point[size];
        for (int i = 0; i < size; i++) {
            hull[i] = generateRandomPoint();
        }
        Arrays.sort(hull, new PolarPointComparator(hull));
        return new SimplePolygon(hull);
    }

    public static void main(String[] args) throws IOException {
        int[] sizes = {5000, 10000, 20000, 50000, 100000};
        for (int x : sizes)
        WritePolygonToFile.writePolygonToFile("random" + x + ".txt", generateRandomPolygon(x));
    }

    private static class PolarPointComparator implements Comparator<Point> {

        final private Point center;

        public PolarPointComparator(Point center) {
            this.center = center;
        }

        public PolarPointComparator(Point[] points) {
            this(Arrays.asList(points));
        }

        public PolarPointComparator(Iterable<Point> points) {
            this(determineCenter(points));
        }

        private static Point determineCenter(Iterable<Point> points) {
            int minx = Integer.MAX_VALUE, miny = Integer.MAX_VALUE,
                    maxx = Integer.MIN_VALUE, maxy = Integer.MIN_VALUE;
            for (Point p : points) {
                if (p.x < minx) {
                    minx = p.x;
                }
                if (p.x > maxx) {
                    maxx = p.x;
                }
                if (p.y < miny) {
                    miny = p.y;
                }
                if (p.y > maxy) {
                    maxy = p.y;
                }
            }
            final int cx = minx + (maxx - minx) / 2;
            final int cy = miny + (maxy - miny) / 2;
            return new Point(cx, cy);
        }

        private int crossProduct(Point p1, Point p2) {
            return p1.x * p2.y - p1.y * p2.x;
        }

        @Override
        public int compare(Point p, Point q) {
            final Point p1 = new Point(p.x - center.x, p.y - center.y);
            final Point p2 = new Point(q.x - center.x, q.y - center.y);

            if (p1.y == 0 && p1.x > 0) {
                return -1; //angle of p1 is 0, thus p2>p1
            }
            if (p2.y == 0 && p2.x > 0) {
                return 1; //angle of p2 is 0 , thus p1>p2
            }
            if (p1.y > 0 && p2.y < 0) {
                return -1; //p1 is between 0 and 180, p2 between 180 and 360
            }
            if (p1.y < 0 && p2.y > 0) {
                return 1;
            }
            return crossProduct(p1, p2);
        }
    }
}
