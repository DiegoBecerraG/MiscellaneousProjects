import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;
import java.util.concurrent.Semaphore;

public class project2 implements Runnable{
    public static Semaphore max_employee = new Semaphore( 2, true );
    public static Semaphore max_bellhop = new Semaphore( 2, true );
    public static Semaphore mutex1 = new Semaphore( 1, true );
    public static Semaphore mutex2 = new Semaphore( 1, true );
    public static Semaphore register = new Semaphore( 0, true );
    public static Semaphore room_assigned = new Semaphore( 0, true );
    public static Semaphore bag_help = new Semaphore( 0, true );
    public static Semaphore bags_delivered = new Semaphore( 0, true );
    public static Queue<guest> frontDeskQueue = new LinkedList<>();
    public static Queue<guest> bellhopQueue = new LinkedList<>();
    public static int totalJoined;


    public static void main(String[] args) {
        for(int i = 0; i < 2; i++) {    // Creates Front Desk Employees
            frontDesk employeeObj = new frontDesk(i);
            Thread employeeThr = new Thread(employeeObj);
            employeeThr.setDaemon(true);
            employeeThr.start();
        }

        for(int i = 0; i < 2; i++) {        // Creates Bellhops
            bellhop bellhopObj = new bellhop(i);
            Thread bellhopThr = new Thread(bellhopObj);
            bellhopThr.setDaemon(true);
            bellhopThr.start();
        }

        for(int i = 0; i < 25; i++) {       // Creates Guests
            new guest(i);
        }

        while(project2.totalJoined < 25) {  // Wait for all guest threads to join
            System.out.print((char[]) null);
        }
        System.out.println("Simulation ends");
    }

    @Override
    public void run() {}
}

class guest implements Runnable{
    int guestNum;
    int bags;
    public int roomNum;
    public int employeeNum;
    public int bhpNumb;
    public Thread guestThr;
    public guest(int num) {
        Random rand = new Random();
        this.guestNum = num;
        bags = rand.nextInt(5+1);
        guestThr = new Thread(this);
        guestThr.setDaemon(true);
        guestThr.start();
        System.out.println("Guest " + num + " created");
    }

    public void run() {
        try {
            System.out.println("Guest " + guestNum + " enters the hotel with " + bags + " bags");
            project2.mutex1.acquire();  // Makes sure this process has mutual exclusion
            project2.frontDeskQueue.add(this);  // Adds Guest Value to queue
            project2.mutex1.release();

            project2.max_employee.acquire(); // Waits until a front desk employee is free
            project2.register.release();    // Signals that a Guest is ready
            project2.room_assigned.acquire();   // Waits until a Guest has been given a room
            System.out.println("Guest " + guestNum + " receives room key for room " + roomNum + " from front desk employee " + employeeNum);
            if(bags > 2) {      //Only if bags > 2, signal to bellhop that the Guest needs help
                project2.max_bellhop.acquire();     // Waits for a bellhop to be ready
                System.out.println("Guest " + guestNum + " requests help with bags");
                project2.bellhopQueue.add(this);    // Queues the guest information for the bellhop to use
                project2.bag_help.release();    // Signal to bellhop that the guest need bag help

                System.out.println("Guest " + guestNum + " enters room " + roomNum);
                project2.bags_delivered.acquire();  // Waits for the bellhop to return the Guests' bags
                System.out.println("Guest " + guestNum + " receives bags from bellhop " + bhpNumb + " and gives tip");
            } else {
                System.out.println("Guest " + guestNum + " enters room " + roomNum);
            }
            System.out.println("Guest " + guestNum + " retires for the evening");
            project2.totalJoined++;
            System.out.println("Guest " + guestNum + " joined");
            guestThr.join();

        }
        catch (InterruptedException ignored) {}
    }
}

class bellhop implements Runnable{
    int bellhopNum;
    public bellhop(int num) {
        this.bellhopNum = num;
        System.out.println("Bellhop " + num + " created");
    }

    public void run() {
        try {
            while(true) {
                project2.bag_help.acquire();    // Waits for Guest to ask for bag help
                guest g = project2.bellhopQueue.remove();
                System.out.println("Bellhop " + bellhopNum + " receives bags from guest " + g.guestNum);
                System.out.println("Bellhop " + bellhopNum + " delivers bags to guest " + g.guestNum);
                g.bhpNumb = bellhopNum;
                project2.bags_delivered.release();      // Signals to Guest that bags have been delivered
                project2.max_bellhop.release();
            }
        }
        catch (InterruptedException ignored) {}
    }
}

class frontDesk implements Runnable{
    int frontDeskNum;
    public static int roomNumber;
    public frontDesk(int num) {
        this.frontDeskNum = num;
        System.out.println("Front desk employee " + num + " created");
    }

    public void run() {
        try{
            roomNumber = 1;
            while(true) {
                project2.register.acquire();    // Waits for a guest to be ready
                project2.mutex2.acquire();
                guest g = project2.frontDeskQueue.remove(); // Pops Guest value from queue
                g.roomNum = roomNumber;
                g.employeeNum = frontDeskNum;
                roomNumber++;
                project2.mutex2.release();
                System.out.println("Front desk employee " + frontDeskNum + " registers guest " + g.guestNum + " and assigns room " + g.roomNum);
                project2.room_assigned.release();   // Signals to Guest that their room has been assigned
                project2.max_employee.release();    // Signals that a front desk employee is available
            }
        }
        catch (InterruptedException ignored) {}
    }
}
