public class QuickSort {
    // Quick sort is separated into multiple function, so creating values is necessary
    private static int comparisons;
    private static int movements;
    private long startTime;

    public QuickSort(int[] list){
        comparisons = 0;
        movements = 0;
        startTime = System.nanoTime();
        quickSort(list);
    }

    public static void quickSort(int[] list) {
        quickSort(list, 0, list.length-1);
    }

    public static void quickSort(int[] list, int first, int last) {
        if (last > first) {
            int pivotIndex = partition(list, first, last);
            quickSort(list, first, pivotIndex -1);
            quickSort(list, pivotIndex + 1, last);
        }
    }

    /** Partition the array list[first..last] */
    public static int partition(int[] list, int first, int last) {
        int pivot = list[(first + last) / 2]; // Changed due to stackOverflowError
        int low = first + 1; // Index for forward search
        int high = last; //Index for backward search
        comparisons++;

        while (high > low) {
            // Search forward from left
            while (low <= high && list[low] <= pivot) {
                low++;
                comparisons++;
            }

            // Search backward from right
            while (low <= high && list[high] > pivot) {
                high--;
                comparisons++;
            }

            comparisons++;
            //	Swap two elements in the list
            if (high > low) {
                int temp = list[high];
                list[high] = list[low];
                list[low] = temp;
                movements++;
            }
        }

        while (high > first && list[high] >= pivot) {
            high--;
            comparisons++;
        }

        comparisons++;
        //	Swap pivot with list[high]
        if (pivot > list[high]) {
            list[first] = list[high];
            list[high] = pivot;
            movements++;
            return high;
        }
        else {
            return first;
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
}
