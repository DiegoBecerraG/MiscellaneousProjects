import java.util.Random;
import java.util.ArrayList;

public class List {
    private int list[];

    public List(int list_properties, int list_size) {
        // Create list with the correct number of values
        if(list_size == 1) {
            list = new int[5000];
        }
        if(list_size == 2) {
            list = new int[15000];
        }
        if(list_size == 3) {
            list = new int[50000];
        }

        // pass the array with the correct property
        if(list_properties == 1) {
            inOrder();
        }
        if(list_properties == 2) {
            reverseOrder();
        }
        if(list_properties == 3) {
            almostOrder();
        }
        if(list_properties == 4) {
            randomOrder();
        }
    }

    // Adds values to the list in order from 1 to list.length
    public void inOrder() {
        for(int i = 1; i <= list.length; i++) {
            list[i - 1] = i;
        }
    }

    // Adds values to the list in reverse order from list.length to 1
    public void reverseOrder() {
        for(int i = list.length; i > 0; i--) {
            list[list.length - i] = i;
        }
    }

    // Adds values with 20% randomness and 80% order
    public void almostOrder() {
        inOrder();  // Creates an ordered list
        Random rand = new Random();
        int num_of_rand_int = list.length / 5; // Gets 20% of total integers
        int arrIndex = num_of_rand_int;
        ArrayList<Integer> randPortion = new ArrayList<Integer>();
        for(int i = 0; i < num_of_rand_int; i++) {
            randPortion.add(i + 1);
        }

        int randInt;
        // Populates the first 20% of the array with random numbers
        for(int i = 0; i < num_of_rand_int; i++) {
            randInt = rand.nextInt(num_of_rand_int);
            list[i] = randPortion.get(randInt);
            randPortion.remove(randInt);
            num_of_rand_int--;
        }

        // Populates the rest of the array with ordered values
        for(int i = arrIndex; i < list.length; i++) {
            list[i] = i;
        }
    }

    // Populates a list with complete random values from 1 to list.length
    public void randomOrder() {
        Random rand = new Random();
        // Must create an ArrayList of all integer in order for no repetition to happen
        ArrayList<Integer> integerList = new ArrayList<Integer>();

        // Populates array list in order up to list.length
        int randMax = list.length;  // Necessary for number to be between 0 and list.length
        for(int i = 1; i <= list.length; i++) {
            integerList.add(i);
        }

        // Randomizes array
        int randInt;
        for(int i = 0; i < list.length; i++) {
            randInt = rand.nextInt(randMax);    // Gets Random number between 0 and list.length
            list[i] = integerList.get(randInt); // Adds the random number, in order, to the list
            integerList.remove(randInt);        // Removes value for no repetition
            randMax--;                          // Lowers the bound by one
        }
    }

    // returns the fully complete list 
    public int[] getList() {
        return list;
    }

    public int getListLength() {
        return list.length;
    }
}