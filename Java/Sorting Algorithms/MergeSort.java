public class MergeSort {
    // Merge sort is seperated into multiple function, so creating values is necessary
    private static int comparisons;
    private static int movements;
    private long startTime;

    public MergeSort(int[] list) {
        comparisons = 0;
        movements = 0;
        startTime = System.nanoTime();
        mergeSort(list);
    }

    /**	The method for sorting the numbers */
    public static void mergeSort(int[] list) {
        if (list.length > 1) {
            //	Merge sort the first half
            int [] firstHalf = new int[list.length / 2];
            System.arraycopy(list, 0, firstHalf, 0, list.length / 2);
            mergeSort(firstHalf);

            //Merge sort the second half
            int secondHalfLength = list.length - list.length / 2;
            int[] secondHalf = new int[secondHalfLength];
            System.arraycopy(list, list.length / 2,
                    secondHalf, 0, secondHalfLength);
            mergeSort(secondHalf);

            //	Merge firstHalf with secondHalf into list
            merge(firstHalf, secondHalf, list);
        }
    }

    /** Merge two sorted list */
    public static void merge(int[] list1, int[] list2, int[] temp) {
        int current1 = 0; // Current index in list1
        int current2 = 0; // Current index in list2
        int current3 = 0; // Current index in temp

        while (current1 < list1.length && current2 < list2.length) {
            comparisons++;
            if (list1[current1] < list2[current2]) {
                temp[current3++] = list1[current1++];
                movements++;
            } else {
                temp[current3++] = list2[current2++];
                movements++;
            }
        }

        while (current1 < list1.length) {
            comparisons++;
            temp[current3++] = list1[current1++];
            movements++;
        }

        while (current2 < list2.length) {
            comparisons++;
            temp[current3++] = list2[current2++];
            movements++;
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
     /** A test method *
     public static void main(String[] args) {
     int[] list = {2, 3, 2, 5, 6, 1, -2, 3, 14, 12};
     mergeSort(list);
     for (int i = 0; i < list.length; i++)
     System.out.println(list[i] + " ");
     }
     */
}