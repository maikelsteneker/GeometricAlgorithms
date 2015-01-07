import java.awt.Point;
import java.io.IOException;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Random;
import nl.tue.win.ga.io.WritePolygonToFile;
import nl.tue.win.ga.model.SimplePolygon;

/**
 * Class dedicated to generating random polygons.
 *
 * @author maikel
 */
public class GenerateRandomPolygon {

    final static int XRES = 1024;
    final static int YRES = 768;
    final static int MAX_SIZE = 100;
    final static private Random GENERATOR = new Random();
    final static private Point CENTER = new Point(XRES / 2, YRES / 2);

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
        Arrays.sort(hull, new PolarPointComparator());
        return new SimplePolygon(hull);
    }

    public static void main(String[] args) throws IOException {
        WritePolygonToFile.writePolygonToFile("random.txt", generateRandomPolygon());
    }

    private static class PolarPointComparator implements Comparator<Point> {

        private int crossProduct(Point p1, Point p2) {
            return p1.x * p2.y - p1.y * p2.x;
        }

        @Override
        public int compare(Point p, Point q) {
            final Point p1 = new Point(p.x - CENTER.x, p.y - CENTER.y);
            final Point p2 = new Point(q.x - CENTER.x, q.y - CENTER.y);

            if (p1.y == 0 && p1.x > 0) {
                return 1; //angle of p1 is 0, thus p2>p1
            }
            if (p2.y == 0 && p2.x > 0) {
                return -1; //angle of p2 is 0 , thus p1>p2
            }
            if (p1.y > 0 && p2.y < 0) {
                return 1; //p1 is between 0 and 180, p2 between 180 and 360
            }
            if (p1.y < 0 && p2.y > 0) {
                return -1;
            }
            return crossProduct(p1, p2) > 0 ? 1 : -1; //return true if p1 is clockwise from p2
        }
    }
}
