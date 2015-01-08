package nl.tue.win.ga.algorithms;
import java.awt.Point;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
/**
 *
 * @author Coen
 */
public class MergeSort {

    private Point[] numbers;
    private Point[] helper;

    private int number;

    public List<Point> sort(List<Point> values) {
        numbers = new Point[values.size()];
        for(int i = 0; i < values.size(); i++) {
            numbers[i] = new Point(values.get(i).x, values.get(i).y);
        }
        number = values.size();
        this.helper = new Point[number];
        mergesort(0, number - 1);
        return Arrays.asList(numbers);

    }

    private void mergesort(int low, int high) {
        // check if low is smaller then high, if not then the array is sorted
        if (low < high) {
            // Get the index of the element which is in the middle
            int middle = low + (high - low) / 2;
            // Sort the left side of the array
            mergesort(low, middle);
            // Sort the right side of the array
            mergesort(middle + 1, high);
            // Combine them both
            merge(low, middle, high);
        }
    }

    private void merge(int low, int middle, int high) {

        // Copy both parts into the helper array
        for (int i = low; i <= high; i++) {
            helper[i] = numbers[i];
        }

        int i = low;
        int j = middle + 1;
        int k = low;
    // Copy the smallest values from either the left or the right side back
        // to the original array
        while (i <= middle && j <= high) {
            if (helper[i].x <= helper[j].x) {
                numbers[k] = helper[i];
                i++;
            } else {
                numbers[k] = helper[j];
                j++;
            }
            k++;
        }
        // Copy the rest of the left side of the array into the target array
        while (i <= middle) {
            numbers[k] = helper[i];
            k++;
            i++;
        }

    }
     
}
