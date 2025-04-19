public class SelectionSort {
    /** The method for sorting the numbers */
    public static void selectionSort(int[] list) { //int[] list?
        int comparisons = 0;
        int movements = 0;
        long startTime = System.nanoTime();

        for (int i = 0; i < list.length -1; i++) {
            // Find the minimum in the list[i..list.length-1]
            int currentMin = list[i];
            int currentMinIndex = i;
            comparisons++;

            for (int j = i+1; j < list.length; j++) {
                comparisons++;
                if (currentMin > list[j]) {
                    currentMin = list[j];
                    currentMinIndex = j;
                }
            }

            //	Swap list[i] wiht list[currentMinIndex[ if necessary
            if (currentMinIndex != i) {
                list[currentMinIndex] = list[i];
                list[i] = currentMin;
                movements++;    // If no swaps, no movements
            }
        }

        comparisons++;  // Needed since i < list.length - 1

        long endTime = System.nanoTime();
        long timeTaken = endTime - startTime;
        double timeTakenMilli = timeTaken / 1000000;

        System.out.println("Comparisons: " + comparisons);
        System.out.println("Movements: " + movements);
        System.out.println("Total time: " + timeTakenMilli);
    }
}
