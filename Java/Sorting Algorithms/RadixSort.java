//import java.io.*;
import java.util.*;

public class RadixSort {
    // Radix sort is seperated into multiple function, so creating values is necessary
    private static int comparisons;
    private static int movements;
    private long startTime;

    public RadixSort(int[] list, int n) {
        comparisons = 0;
        movements = 0;
        startTime = System.nanoTime();
        radixsort(list, n);
    }

    static int getMax(int arr[], int n) {
        int mx = arr[0];
        for (int i = 1; i < n; i++) {
            comparisons++;
            if (arr[i] > mx) {
                mx = arr[i];
            }
        }
        return mx;
    }

    static void countSort(int arr[], int n, int exp) {
        int output[] = new int[n];
        int i;
        int count[] = new int[10];
        Arrays.fill(count, 0);
        for (i = 0; i < n; i++) {
            count[(arr[i] / exp) % 10]++;
        }
        // Change count[i] so that count[i] now contains
        // actual position of this digit in output[]
        for (i = 1; i < 10; i++) {
            count[i] += count[i - 1];
        }
        // Build the output array
        for (i = n - 1; i >= 0; i--) {
            output[count[(arr[i] / exp) % 10] - 1] = arr[i];
            count[(arr[i] / exp) % 10]--;
        }
        for (i = 0; i < n; i++) {
            arr[i] = output[i];
            movements++;
        }
    }

    static void radixsort(int arr[], int n) { // Find the maximum number to know number of digits
        int m = getMax(arr, n);
        for (int exp = 1; m / exp > 0; exp *= 10) {
            countSort(arr, n, exp);
        }
    }

    static void print(int arr[], int n) {
        for (int i = 0; i < n; i++) {
            System.out.print(arr[i] + " ");
        }
    }

    public void results() {
        long endTime = System.nanoTime();
        long timeTaken = endTime - startTime;
        double timeTakenMilli = timeTaken / 1000000;

        System.out.println("Comparisons: " + comparisons);
        System.out.println("Movements: " + movements);
        System.out.println("Total time: " + timeTakenMilli);
    }
    /**
     public static void main(String[] args) {
     int arr[] = { 170, 45, 75, 90, 802, 24, 2, 66 };
     int n = arr.length;
     radixsort(arr, n);
     print(arr, n);
     }
     */
}