import java.util.Scanner;

public class Main
{
    public static void main(String[] args) {
        Scanner num_input = new Scanner(System.in);
        int properties_num = 0;
        int input_size = 0;
        int algorithm_num = 0;

        // Asks user which data type they would like to use; will repeat until valid value entered
        System.out.println("1. InOrder\n2. ReverseOrder\n3. AlmostOrder\n4. RandomOrder");
        System.out.print("List properties, select the data type of list you wish to use: ");
        properties_num = num_input.nextInt();
        System.out.println();

        // Asks user for input size; will repeat until valid value entered
        System.out.println("1. 5000\n2. 15000\n3. 50000");
        System.out.print("Input Size, select the size of list: ");
        input_size = num_input.nextInt();
        System.out.println();

        // Asks the user for a sorting algorithm; will repeat until valid value entered
        System.out.println("1. Insertion Sort\n2. Selection Sort\n3. Quick Sort\n4. Merge Sort\n5. Heap Sort\n6. Radix Sort");
        System.out.print("Sorting Algorithm, select the sorting algorithm: ");
        algorithm_num = num_input.nextInt();
        System.out.println();

        List completeList = new List(properties_num, input_size);

        //Prints out results
        System.out.println("Experimental Results:");
        basicResults(properties_num, input_size, algorithm_num);

        // Necessary since these sorts have extra functions, unlike InsertionSort and SelectionSort
        QuickSort qSort;
        MergeSort mSort;
        HeapSort hSort;
        RadixSort rSort;

        // Calls the appropriate file given user input
        if(algorithm_num == 1) {
            InsertionSort.insertionSort(completeList.getList());
        }
        if(algorithm_num == 2) {
            SelectionSort.selectionSort(completeList.getList());
        }
        if(algorithm_num == 3) {
            qSort = new QuickSort(completeList.getList());
            qSort.results();
        }
        if(algorithm_num == 4) {
            mSort = new MergeSort(completeList.getList());
            mSort.results();
        }
        if(algorithm_num == 5) {
            hSort = new HeapSort(completeList.getList());
        }
        if(algorithm_num == 6) {
            rSort = new RadixSort(completeList.getList(), completeList.getListLength());
            rSort.results();
        }
    }

    // Prints out part of the result that doesn't require the list to be built
    public static void basicResults(int prop, int inp, int alg) {
        if(inp == 1) {
            System.out.println("Input Size: 5000");
        }
        if(inp == 2) {
            System.out.println("Input Size: 15000");
        }
        if(inp == 3) {
            System.out.println("Input Size: 50000");
        }
        if(prop == 1) {
            System.out.println("Data type: InOrder");
        }
        if(prop == 2) {
            System.out.println("Data Type: ReverseOrder");
        }
        if(prop == 3) {
            System.out.println("Data Type: AlmostOrder");
        }
        if(prop == 4) {
            System.out.println("Data Type: RandomOrder");
        }
        if(alg == 1) {
            System.out.println("Sort:  Insertion");
        }
        if(alg == 2) {
            System.out.println("Sort: Selection");
        }
        if(alg == 3) {
            System.out.println("Sort: Quick");
        }
        if(alg == 4) {
            System.out.println("Sort: Merge");
        }
        if(alg == 5) {
            System.out.println("Sort: Heap");
        }
        if(alg == 6) {
            System.out.println("Sort: Radix");
        }
    }
}