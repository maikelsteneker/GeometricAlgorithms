package nl.tue.win.ga;

import java.io.IOException;
import nl.tue.win.ga.algorithms.RandomIncrementalConstruction;
import nl.tue.win.ga.algorithms.SweepLineA;
import nl.tue.win.ga.io.ReadPolygonFromFile;
import nl.tue.win.ga.model.SimplePolygon;

/**
 * Entry point for command-line interface.
 *
 * @author maikel
 */
public class Main {
    
    private final static String INSTRUCTION = "{RIC, bucket, InOrder, sweep} filename [nbuckets]";

    /**
     * @param args the command line arguments
     * @throws java.io.IOException
     */
    public static void main(String[] args) throws IOException {
        if (args.length == 2) {
            main(args[0], args[1], -1);
        } else if (args.length == 3) {
            main(args[0], args[1], Integer.parseInt(args[2]));
        } else {
            throw new IllegalArgumentException(INSTRUCTION);
        }
    }

    public static void main(String algorithm, String inputFile, int nBuckets) throws IOException {
        SimplePolygon polygon = ReadPolygonFromFile.readPolygonFromFile(inputFile);

        switch (algorithm) {
            case "RIC":
            case "RandomizedIncrementalConstruction":
                new RandomIncrementalConstruction(polygon.getHull()).randomIncrementalMap();
                return;
            case "bucket":
            case "Bucket":
            case "buckets":
            case "Buckets":
                new RandomIncrementalConstruction(polygon.getHull()).bucketIncrementalMap(nBuckets);
                return;
            case "InOrder":
                new RandomIncrementalConstruction(polygon.getHull()).inOrderIncrementalMap();
                return;
            case "sweep":
            case "Sweep":
            case "sweepline":
            case "Sweepline":
            case "SweepLine":
                new SweepLineA(polygon.getHull()).sweep();
                return;
            default:
                throw new IllegalArgumentException(INSTRUCTION);
        }
    }

}
