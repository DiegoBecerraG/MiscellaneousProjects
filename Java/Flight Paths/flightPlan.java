import java.io.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Stack;

//This class is used to store all necessary information from the FlightData file
class flightData {
    String cityOne;
    String cityTwo;
    int costWeight;
    int timeWeight;
    LinkedList<flightData> connectedCities;
    int minCost;
    int minTime;
    int costToTravel;
    int timeToTravel;
    boolean touched;

    public flightData() {
        this.cityOne = "";
        this.cityTwo = "";
        this.costWeight = 0;
        this.timeWeight = 0;
        this.costToTravel = Integer.MAX_VALUE;
        this.timeToTravel = Integer.MAX_VALUE;
        this.minCost = Integer.MAX_VALUE;
        this.minTime = Integer.MAX_VALUE;
        this.touched = false;
        this.connectedCities = new LinkedList<>();
    }
}

public class flightPlan {
    static ArrayList<String> shortestPathAlgorithm(ArrayList<String> pathsInput, LinkedList<flightData> citiesLinkedList,
                                                   int numLines) {
        StringBuilder stringToReturn;
        Stack<flightData> pathsStack = new Stack<>();
        LinkedList<flightData> changeLL = new LinkedList<>();
        flightData startingNode;
        flightData currentNode;
        int totalNodesTouched;
        String firstCity;
        String secondCity;
        String typeOfFlightValue;

        ArrayList<String> outputString = new ArrayList<>();
        for(int n = 1; n < (numLines + 1); n++) {
            //Grabs input from PathsToCalculate file
            firstCity = pathsInput.get(0);
            secondCity = pathsInput.get(1);
            typeOfFlightValue = pathsInput.get(2);

            //Used to reset values every loop (.clone() is bugged and doesn't work as intended)
            //Data caries over to the original LL (citiesLinkedList) when it should have been cloned
            changeLL.clear();
            changeLL = (LinkedList<flightData>) citiesLinkedList.clone();
            for(int i = 0; i < changeLL.size(); i++) {
                changeLL.get(i).connectedCities = (LinkedList<flightData>) citiesLinkedList.get(i).connectedCities.clone();
            }

            startingNode = new flightData();
            currentNode = new flightData();
            totalNodesTouched = 0;

            //Finds the starting node using the provided string
            for (flightData data : changeLL) {
                //firstCity will depend on PathsToCalculate file
                if (firstCity.equals(data.cityOne)) {
                    data.touched = true;
                    startingNode = data;
                    startingNode.costToTravel = 0;
                    startingNode.timeToTravel = 0;
                    startingNode.minTime = 0;
                    startingNode.minCost = 0;
                    currentNode = startingNode;
                    totalNodesTouched = 1;
                }
            }

            //Updates all other instances of the startingNode within the LL
            for (flightData data : changeLL) {
                for (int j = 0; j < data.connectedCities.size(); j++) {
                    if (typeOfFlightValue.equals("C")) {
                        if (startingNode.cityOne.equals(data.connectedCities.get(j).cityOne)) {
                            data.connectedCities.get(j).touched = true;
                            data.connectedCities.get(j).minCost =
                                    data.connectedCities.get(j).costWeight;
                        }
                    } else {
                        if (startingNode.cityOne.equals(data.connectedCities.get(j).cityOne)) {
                            data.connectedCities.get(j).touched = true;
                            data.connectedCities.get(j).minTime =
                                    data.connectedCities.get(j).timeWeight;
                        }
                    }
                }
            }

            //Finds the cities connected to the starting node and updates their minCost/minTime
            for(int i = 0; i < currentNode.connectedCities.size(); i++) {
                if(typeOfFlightValue.equals("C")) {
                    startingNode.connectedCities.get(i).minCost =
                            startingNode.connectedCities.get(i).costWeight;
                } else {
                    startingNode.connectedCities.get(i).minTime =
                            startingNode.connectedCities.get(i).timeWeight;
                }
            }
            pathsStack.push(startingNode);

            int whereInLL = 0;
            int costToCheck;
            int timeToCheck;
            flightData nodeToCheck = new flightData();
            ArrayList<String> printingHelper = new ArrayList<>();
            startingNode.costToTravel = 0;
            startingNode.timeToTravel = 0;
            currentNode = startingNode;
            while(totalNodesTouched < changeLL.size()) {
                costToCheck = Integer.MAX_VALUE; //Needs to be updated, or else previous data will be used
                timeToCheck = Integer.MAX_VALUE;

                //Runs through all nodes that have minCost/minTime != inf
                //Finds the node with the smallest value of the requested type (C or T)
                for (flightData flightData : changeLL) {
                    for (int j = 0; j < flightData.connectedCities.size(); j++) {
                        if (!flightData.connectedCities.get(j).touched) {
                            if (typeOfFlightValue.equals("C")) {
                                if (costToCheck > flightData.connectedCities.get(j).minCost) {
                                    nodeToCheck = flightData.connectedCities.get(j);
                                    costToCheck = flightData.connectedCities.get(j).minCost;
                                }
                            } else {
                                if (timeToCheck > flightData.connectedCities.get(j).minTime) {
                                    nodeToCheck = flightData.connectedCities.get(j);
                                    timeToCheck = flightData.connectedCities.get(j).minTime;
                                }
                            }
                        }
                    }
                }

                //Make sure that all values in LL with the same city = touched
                for (int i = 0; i < changeLL.size(); i++) {
                    assert nodeToCheck != null;
                    if (nodeToCheck.cityOne.equals(changeLL.get(i).cityOne)) {
                        changeLL.get(i).touched = true;
                        currentNode.touched = true;
                        whereInLL = i;
                        if(typeOfFlightValue.equals("C")) {
                            changeLL.get(i).costToTravel = costToCheck; //Cost reset bug here
                        } else {
                            changeLL.get(i).timeToTravel = timeToCheck; //Time reset bug here
                        }
                        currentNode = changeLL.get(i); //Get back to original LL
                    }
                    //Looks into the connected LL and updates those values
                    for (int j = 0; j < changeLL.get(i).connectedCities.size(); j++) {
                        if (nodeToCheck.cityOne.equals(changeLL.get(i).connectedCities.get(j).cityOne)) {
                            changeLL.get(i).connectedCities.get(j).touched = true;
                            if(typeOfFlightValue.equals("C")) {
                                changeLL.get(i).connectedCities.get(j).costToTravel = costToCheck;
                            } else {
                                changeLL.get(i).connectedCities.get(j).timeToTravel = timeToCheck;
                            }
                        }
                    }
                }

                //Updates minCost/minTime for the nodes attached to the currentNode
                for(int i = 0; i < currentNode.connectedCities.size(); i++) {
                    if(!currentNode.connectedCities.get(i).touched) {
                        if(typeOfFlightValue.equals("C")) {
                            if(currentNode.connectedCities.get(i).minCost > currentNode.costToTravel +
                                    currentNode.connectedCities.get(i).costWeight) {
                                currentNode.connectedCities.get(i).minCost = currentNode.costToTravel +
                                        currentNode.connectedCities.get(i).costWeight;
                                changeLL.get(whereInLL).connectedCities.get(i).minCost = currentNode.costToTravel +
                                        currentNode.connectedCities.get(i).costWeight;
                            }
                        } else {
                            if (currentNode.connectedCities.get(i).minTime > currentNode.timeToTravel +
                                    currentNode.connectedCities.get(i).timeWeight) {
                                currentNode.connectedCities.get(i).minTime = currentNode.timeToTravel +
                                        currentNode.connectedCities.get(i).timeWeight;
                                changeLL.get(whereInLL).connectedCities.get(i).minTime = currentNode.timeToTravel +
                                        currentNode.connectedCities.get(i).timeWeight;
                            }
                        }
                    }
                }

                pathsStack.push(currentNode);
                //If secondCity has been reached, break out of the loop
                if (currentNode.cityOne.equals(secondCity)) {
                    break;
                }
                totalNodesTouched++;
            }

            //Sets up outputToFile()
            if(typeOfFlightValue.equals("C")) {
                outputString.add("Flight " + n + ": " + firstCity + ", " + secondCity + " (Cost)");
                stringToReturn = new StringBuilder("Path 1: " + startingNode.cityOne + " ");
                while(!pathsStack.empty()) {
                    printingHelper.add(pathsStack.pop().cityOne);
                }
                for(int i = printingHelper.size() - 2; i >= 0; i--) {
                    stringToReturn.append("-> ").append(printingHelper.get(i)).append(" ");
                }
                outputString.add(stringToReturn.toString());
                outputString.add("Cost: " + currentNode.costToTravel + "\n");

            } else {
                outputString.add("Flight " + n + ": " + firstCity + ", " + secondCity + " (Time)");
                stringToReturn = new StringBuilder("Path 1: " + startingNode.cityOne + " ");
                while(!pathsStack.empty()) {
                    printingHelper.add(pathsStack.pop().cityOne);
                }
                for(int i = printingHelper.size() - 2; i >= 0; i--) {
                    stringToReturn.append("-> ").append(printingHelper.get(i)).append(" ");
                }
                outputString.add(stringToReturn.toString());
                outputString.add("Time: " + currentNode.timeToTravel + "\n");
            }

            //Resets values for next loop
            while(printingHelper.size() != 0) {
                printingHelper.remove(0);
            }

            pathsInput.subList(0, 3).clear();
            System.out.print("\n");
        }

        return outputString;
    }

    static void outputToFile(ArrayList<String> arr) throws IOException {
        BufferedWriter writeToFile = new BufferedWriter(new FileWriter("src/output.txt")); //args[2]
        for (String s : arr) {
            writeToFile.write(s);
            System.out.println(s);
        }
        writeToFile.close();
    }

    public static void main(String[] args) throws IOException {
        String firstCity;
        String secondCity;
        String typeOfFlightValue;
        String valueOfFlight;

        //Grab Data needed to start the algorithm
        FileReader file = new FileReader("src/FlightData.txt"); //args[0]
        BufferedReader br = new BufferedReader(file);
        int numLines = Integer.valueOf(br.readLine()); //Necessary to read the first line in the file
        String[] values;

        //Creates the needed lists to compile all data presented
        ArrayList<flightData> flightDataList = new ArrayList<>();
        LinkedList<flightData> citiesLinkedList = new LinkedList<>();

        //Grabs all the data from the FlightData file and stores
        //it into an arrayList
        while((valueOfFlight = br.readLine()) != null) {
            flightData currFlight = new flightData();
            values = valueOfFlight.split("\\|");

            currFlight.cityOne = values[0];
            currFlight.cityTwo = values[1];
            currFlight.costWeight = Integer.parseInt(values[2]);
            currFlight.timeWeight = Integer.parseInt(values[3]);
            flightDataList.add(currFlight);

            //This checks the linkedList for original instances of a city;
            //If there is no instance of a city, add it to the linkedList
            if(citiesLinkedList.size() == 0) {
                citiesLinkedList.add(currFlight);
            } else {
                for(int i = 0; i < citiesLinkedList.size(); i++) {
                    String s = citiesLinkedList.get(i).cityOne;
                    int counter = 0;
                    int doubles = 0;
                    while(counter < citiesLinkedList.size()) {
                        if(citiesLinkedList.get(counter).cityOne.equals(currFlight.cityOne)) {
                            doubles++;
                        }
                        counter++;
                    }
                    if(!s.equals(currFlight.cityOne) && !citiesLinkedList.contains(currFlight) && doubles == 0) {
                        citiesLinkedList.add(currFlight);
                        i++;
                    }
                }
            }
        }
        br.close();

        //Begins to gain access to PathsToCalculate file
        file = new FileReader("src/PathsToCalculate.txt"); //args[1]
        br = new BufferedReader(file);
        numLines = Integer.parseInt(br.readLine());
        String[] inputFlights;
        ArrayList<String> pathsInput = new ArrayList<>();

        //Grabs the input from our PathToCalculate file and
        //stores the data into strings for later use
        while((valueOfFlight = br.readLine()) != null) {
            inputFlights = valueOfFlight.split("\\|");
            firstCity = inputFlights[0];
            pathsInput.add(firstCity);
            secondCity = inputFlights[1];
            pathsInput.add(secondCity);
            typeOfFlightValue = inputFlights[2];
            pathsInput.add(typeOfFlightValue);
        }
        br.close();

        //Connects all original cities to a linked list with the cities they can reach
        for (flightData flightData : citiesLinkedList) {
            for (flightData data : flightDataList) {
                if (flightData.cityOne.equals(data.cityTwo)) {
                    flightData.connectedCities.add(data);
                }
            }
        }

        //Will use the provided data and find the shortest path from the first
        //city to the second depending on cost or time
        ArrayList<String> oS;
        oS = shortestPathAlgorithm(pathsInput, citiesLinkedList, numLines);

        //Prints data and writes it to output file
        outputToFile(oS);
    }
}