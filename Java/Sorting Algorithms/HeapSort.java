import java.util.*;
public class HeapSort {
    /** Heap sort method */
    public HeapSort(int[] list) {
        // heapSort cannot take an int[], so this is used to turn list into an E[]
        Integer[] intHeapList = Arrays.stream(list).boxed().toArray(Integer[]::new);
        heapSort(intHeapList);
    }

    public static <E extends Comparable<E>> void heapSort(E[] list) {
        long startTime = System.nanoTime();
        //	Create a Heap of integers
        Heap<E> heap = new Heap<>();

        //	Add elements to the heap
        for (int i = 0; i < list.length; i++) {
            heap.add(list[i]);
        }

        //	Remove elements from the heap
        for (int i = list.length -1; i >= 0; i--) {
            list[i] = heap.remove();
        }

        long endTime = System.nanoTime();
        long timeTaken = endTime - startTime;
        double timeTakenMilli = timeTaken / 1000000;

        System.out.println("Comparisons: " + heap.getComparisons());
        System.out.println("Movements: " + heap.getMovements());
        System.out.println("Total time: " + timeTakenMilli);
    }

	/*
 	/*	A test method
	public static void main(String[] args) {
		Integer[] list = {-44, -5, -3, 3, 3, 1, -4, 0, 1, 2, 4, 5, 53};
		heapSort(list);
		for(int i = 0; i < list.length; i++)
			System.out.println(list[i] + " ");
	}
	*/
}
