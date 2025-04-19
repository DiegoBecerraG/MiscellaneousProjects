public class InsertionSort {
    /**The method for sorting the numbers */
    public static void insertionSort(int[] list) {
        int comparisons = 0;
        int movements = 0;
        long startTime = System.nanoTime();

        for (int i = 1; i < list.length; i++) {
            /** Insert list[i] into a sorted sublist list[0..i-1] so that
             * 	list[0..i] is sorted
             */
            int currentElement = list[i];
            int k;
            boolean loop = false; // Boolean used to check if loop below is used
            for (k = i-1; k>= 0 && list[k] > currentElement; k--) {
                loop = true;
                comparisons++;
                list[k+1] = list[k];
                movements++;
            }

            //insert the current element into list[k + 1]
            list[k + 1] = currentElement;
            movements++;

            // This check is needed in case loop in question is not used
            // If the loop is not used, no comparisons will be added
            if(loop == false) {
                comparisons++;
            }
        }
        long endTime = System.nanoTime();
        long timeTaken = endTime - startTime;
        double timeTakenMilli = timeTaken / 1000000;

        System.out.println("Comparisons: " + comparisons);
        System.out.println("Movements: " + movements);
        System.out.println("Total time: " + timeTakenMilli);
    }
}
