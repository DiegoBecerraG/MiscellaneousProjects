import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class project {
    public static void main(String[] args) {
        //User Interface
        String fileName;
        String newLocation;
        String value;
        int fileAllocation;

        //Creates the bitmap that will be stored in the second block
        disk.initiateBitmap();

        // Depending on the command line input, the File Allocation Method will change
        if(args.length  == 0 || args[0].equals("contiguous") || args[0].equals("Contiguous")) {
            fileAllocation = 1;
        } else if(args[0].equals("chained") || args[0].equals("Chained")) {
            fileAllocation = 2;
        } else if(args[0].equals("indexed") || args[0].equals("Indexed")) {
            fileAllocation = 3;
        } else {
            fileAllocation = 1;
        }

        // Main UI function loop, will ask the user for input
        // Depending on their answer, a specific function fill go through
        label:
        while(true) {
            Scanner input = new Scanner(System.in);
            System.out.println("Choose a value from the list:");
            System.out.println("1) Display a file \n" +
                    "2) Display the file table \n" +
                    "3) Display the free space bitmap \n" +
                    "4) Display a disk block \n" +
                    "5) Copy a file from the simulation to a file on the real system \n" +
                    "6) Copy a file from the real system to a file in the simulation \n" +
                    "7) Delete a file \n" +
                    "8) Exit");
            String listVal = input.nextLine();
            System.out.println();
            System.out.println("Choice: " + listVal);
            System.out.println();

            switch (listVal) {
                case "1":
                    // Display a File
                    System.out.print("File to print: ");
                    fileName = input.nextLine();
                    System.out.println(disk.displayFile(fileName, fileAllocation));
                    System.out.println();
                    break;
                case "2":
                    // Display the file table
                    System.out.println(disk.printFileTable(fileAllocation));
                    System.out.println();
                    break;
                case "3":
                    // Display the free space bitmap
                    System.out.println(disk.printBitmap());
                    System.out.println();
                    break;
                case "4":
                    //  Display a disk block
                    System.out.print("Disk Block Number: ");
                    value = input.nextLine();
                    System.out.println(disk.displayBlock(Integer.parseInt(value), fileAllocation));
                    System.out.println();
                    break;
                case "5":
                    // Copy a file from the simulation to a file on the real system
                    System.out.print("Copy from: ");
                    fileName = input.nextLine();
                    System.out.print("Copy to: ");
                    newLocation = input.nextLine();
                    file_system.writeFile(fileName, newLocation, fileAllocation);
                    System.out.println();
                    break;
                case "6":
                    // Copy a file from the real system to a file in the simulation
                    System.out.print("Copy from: ");
                    fileName = input.nextLine();
                    System.out.print("Copy to: ");
                    newLocation = input.nextLine();
                    file_system.copyFile(fileName, newLocation, fileAllocation);
                    System.out.println();
                    break;
                case "7":
                    // Delete a file
                    System.out.print("File to delete: ");
                    fileName = input.nextLine();
                    System.out.println(disk.deleteFile(fileName, fileAllocation));
                    System.out.println();
                    break;
                case "8":
                    // Exit
                    System.out.println("Exiting...");
                    break label;
                default:
                    // If an input is not recognized, give error
                    System.out.println("Invalid Value Detected. Please try again.");
                    System.out.println();
                    break;
            }
        }
    }
}


class disk {
    static String [][] storage = new String[256][512];  // Main storage for all files
    static int lineSplitLocation;
    static String firstPartString;
    static String secondPartString;

    public static String storeFile(String fullFile, String nf, String name, int fileAlloc){
        boolean escape = false;
        String returnPrompt = "";
        int blockLocation = 2;
        int byteLocation = 0;
        int count = 0;
        int byteCount = 0;
        ArrayList<String> fileSubstring = new ArrayList<>();
        ArrayList<String> placements = new ArrayList<>();

        boolean outEscape = false;
        // Make loop through entire storage to find available space.
        // If no space, return error
        for(int i = 2; i < storage.length; i++) {
            if(outEscape){
                break;
            }
            for(int j = 0; j < storage[i].length; j++) {
                if(outEscape){
                    break;
                }
                if(storage[i][j] == null) {
                    if (fileAlloc == 1 || fileAlloc == 3) {
                        blockLocation = i;
                        byteLocation = j + count;
                        int tempBlockLoc = blockLocation;
                        int tempByteLoc = byteLocation;

                        // This loop checks for sequential null values, keeps looping until values are found
                        // If no values are found, return error message
                        for(int k = 0; k < fullFile.length(); k++) {
                            if (escape) {
                                escape = false;
                                outEscape = true;
                                break;
                            }
                            if(tempByteLoc + k == 512) {
                                tempBlockLoc++;
                                tempByteLoc = 0;
                                byteCount = 0;
                            }
                            if(storage[tempBlockLoc][tempByteLoc + byteCount] != null) {
                                escape = true;
                                i = tempBlockLoc - 1;
                                count = tempByteLoc + byteCount - 1;
                                break;
                            }
                            byteCount++;
                        }
                        if(!escape) {
                            outEscape = true;
                        } else {
                            break;
                        }
                        count++;
                    } else {
                        blockLocation = i;
                        byteLocation = j;
                        outEscape = true;
                        break;
                    }
                }
            }
        }

        if(fileAlloc == 1 || fileAlloc == 3){
            if(escape) {
                returnPrompt = "Error storing file";
            } else {
                int startBlock = blockLocation;
                int startByte = byteLocation;

                // If the file value will be larger than the remaining possible values in a block,
                // split the file/index value in as many segments necessary to store it properly
                // else, store it normally in sequential order
                if (fullFile.length() + byteLocation > 512) {
                    lineSplitLocation = 512 - byteLocation;
                    firstPartString = fullFile.substring(0, lineSplitLocation);
                    secondPartString = fullFile.substring(lineSplitLocation);
                    fileSubstring.add(firstPartString);
                    fileSubstring.add(secondPartString);

                    while(true) {
                        if(secondPartString.length() > 512) {
                            firstPartString = secondPartString.substring(0, 512);
                            secondPartString = secondPartString.substring(512);
                            fileSubstring.add(firstPartString);
                            fileSubstring.add(secondPartString);
                        } else {
                            break;
                        }
                    }
                    for (String s : fileSubstring) {
                        for (int k = 0; k < s.length(); k++) {
                            if (byteLocation == 512) {
                                blockLocation++;
                                byteLocation = 0;
                            }
                            if(fileAlloc == 1){
                                storage[blockLocation][byteLocation] = String.valueOf(s.charAt(k));
                            } else {
                                placements.add(blockLocation + " " + byteLocation);
                                storage[blockLocation][byteLocation] = ".";
                            }
                            byteLocation++;
                            updateBitmap(blockLocation, 1);
                        }
                    }
                } else {
                    for (int j = 0; j < fullFile.length(); j++) {
                        if(fileAlloc == 1){
                            storage[blockLocation][byteLocation] = String.valueOf(fullFile.charAt(j));
                        } else {
                            placements.add(blockLocation + " " + byteLocation);
                            storage[blockLocation][byteLocation] = ".";
                        }
                        byteLocation++;
                        updateBitmap(blockLocation, 1);
                    }
                }

                // If contiguous, end here, update the file table with new values, and print success
                if(fileAlloc == 1){
                    updateFileTable(startBlock, startByte, fullFile.length(), nf);
                    returnPrompt = name + " copied";
                }

                // If indexed, loop through the entire storage to find null values to store the file
                if (fileAlloc == 3) {
                    count = 0;
                    for(int k = 2; k < storage.length; k++){
                        if(escape){
                            break;
                        }
                        for(int l = 0; l < storage[k].length; l++){
                            if(storage[k][l] == null){
                                placements.add(k + " " + l);
                                storage[k][l] = String.valueOf(fullFile.charAt(count));
                                updateBitmap(k, 1);
                                count++;
                            }
                            if(count == fullFile.length()){
                                escape = true;
                                break;
                            }
                        }
                    }

                    // Go back to the stored values for the index block and store the location values for the file
                    for(int k = 0; k < (placements.size() / 2); k++){
                        String[] indexInput = placements.get(k).split("\\W+");
                        storage[Integer.parseInt(indexInput[0])][Integer.parseInt(indexInput[1])] = placements.get(k + fullFile.length());
                        updateBitmap(Integer.parseInt(indexInput[0]), 1);
                        Arrays.fill(indexInput, null);
                    }

                    updateFileTable(startBlock, startByte, fullFile.length(), nf);
                    returnPrompt = name + " copied";
                }
            }
        } else {
            // If Chained
            for (String[] strings : storage) {
                if (escape) {
                    break;
                }
                for (String string : strings) {
                    if (string == null) {
                        count++;
                        if (count == fullFile.length()) {
                            escape = true;
                            break;
                        }
                    }
                }
            }

            if(count != fullFile.length()){
                returnPrompt = "Error storing file";
            } else {
                int startBlock = blockLocation;
                int startByte = byteLocation;

                for(int k = 0; k < fullFile.length(); k++){
                    // Store each character and check what next available spot is null,
                    // then add those as a string (x y z)
                    // x = actual value; y = next block; z = next byte
                    if(storage[blockLocation][byteLocation] == null){
                        storage[blockLocation][byteLocation] = String.valueOf(fullFile.charAt(k));
                        int blockCount = blockLocation;
                        byteCount = byteLocation;
                        while(true){
                            if(byteCount == 512){
                                byteCount = 0;
                                blockCount++;
                            }
                            if(storage[blockCount][byteCount] == null){
                                storage[blockLocation][byteLocation] = storage[blockLocation][byteLocation] + " " + blockCount + " " + byteCount;
                                break;
                            }
                            byteCount++;
                        }
                        blockLocation = blockCount;
                        byteLocation = byteCount;
                        updateBitmap(blockLocation, 1);
                    }
                }

                updateFileTable(startBlock, startByte, fullFile.length(), nf);
                returnPrompt = name + " copied";
            }
        }

        return returnPrompt;
    }

    public static void updateFileTable(int sb, int sby, int len, String name) {
        int fileTableLocation = 0;
        // if given name is longer than 8 characters, shorten it to 8 characters
        if (name.length() > 8) {
            name = name.substring(0, 8);
        }

        // Look for the next available selection of null values
        for(int i = 0; i < storage[0].length; i+=11) {
            if(storage[0][i] == null) {
                fileTableLocation = i;
                break;
            }
        }

        // Bytes 0-7: name of file
        for (int i = 0; i < name.length(); i++) {
            storage[0][fileTableLocation] = String.valueOf(name.charAt(i));
            fileTableLocation++;
        }

        // If file name is < 8, fill in null space with whitespace
        for (int i = 0; i < (8 - name.length()); i++) {
            storage[0][fileTableLocation] = " ";
            fileTableLocation++;
        }

        // Byte 8: Starting Block
        storage[0][fileTableLocation] = String.valueOf(sb);
        fileTableLocation++;
        // Byte 9: Length of file
        storage[0][fileTableLocation] = String.valueOf(len);
        fileTableLocation++;
        // Byte 10: Starting Byte
        storage[0][fileTableLocation] = String.valueOf(sby);
    }

    public static void removeFileTable(String name) {
        boolean esc = false;
        int charCount = 0;
        int index = 0;

        // Loop through all the file tables to find if the given name exists in the simulation
        for(int i = 0; i < storage[0].length; i+=11) {
            if(esc) {
                break;
            }
            if(storage[0][i] != null) {
                for(int j = 0; j < 8; j++) {
                    if(String.valueOf(name.charAt(j)).equals(storage[0][i+j])) {
                        charCount++;
                        if(charCount == 8) {
                            esc = true;
                            index = i;
                            break;
                        }
                    }
                }
                if(charCount != 8){
                    charCount = 0;
                }
            }
        }

        // Once found, make all Bytes 0-10 null
        if(esc) {
            for(int i = index; i < (index + 11); i++) {
                storage[0][i] = null;
            }
        }
    }


    public static String displayFile(String givenFile, int fileAlloc) {
        StringBuilder fullFile = new StringBuilder();
        boolean fileFound = false;
        int charCount = 0;
        int block = 0;
        int length = 0;
        int startByte = 0;
        int count;

        // if given name is less than 8 characters, add whitespace
        if(givenFile.length() < 8) {
            for(int i = givenFile.length(); i < 8; i++){
                givenFile = givenFile + " ";
            }
        }

        // Look through the file tables, if the names are exactly the same, collect other information
        while(!fileFound) {
            for(int i = 0; i < storage[0].length; i+=11) {
                if(fileFound) {
                    break;
                }
                if(storage[0][i] != null) {
                    for(int j = 0; j < 8; j++) {
                        if(String.valueOf(givenFile.charAt(j)).equals(storage[0][i+j])) {
                            charCount++;
                            if(charCount == 8) {
                                fileFound = true;
                                block = Integer.parseInt(storage[0][i+j+1]);
                                length = Integer.parseInt(storage[0][i+j+2]);
                                startByte = Integer.parseInt(storage[0][i+j+3]);
                                break;
                            }
                        }
                    }
                    if(charCount != 8){
                        charCount = 0;
                    }
                }
            }
            if(!fileFound) {
                fullFile = new StringBuilder("File not found");
                return fullFile.toString();
            }
        }

        // Depending on the File Allocation method, store the file value into a string and return it
        count = startByte;
        for(int i = startByte; i < startByte + length; i++) {
            if(fileAlloc == 1){
                fullFile.append(storage[block][count]);
                count++;
            } else if(fileAlloc == 2){
                // Reads the file byte and grab the pointer to the next file byte
                String[] storageInput = storage[block][count].split("\\W+");
                fullFile.append(storage[block][count].charAt(0));
                block = Integer.parseInt(storageInput[1]);
                count = Integer.parseInt(storageInput[2]);
                Arrays.fill(storageInput, null);
            } else {
                // Loops through the index block to find the file byte
                String[] storageInput = storage[block][count].split("\\W+");
                fullFile.append(storage[Integer.parseInt(storageInput[0])][Integer.parseInt(storageInput[1])].charAt(0));
                count++;
            }
            // String Output format
            if(i % 50 == 0 &&  i != 0) {
                fullFile.append("\n");
            }
            // If crossing new block, update values
            if(count == 512){
                block++;
                count = 0;
            }
        }


        return fullFile.toString();
    }

    public static String deleteFile(String file, int fileAlloc) {
        StringBuilder fullFile;
        boolean fileFound = false;
        int charCount = 0;
        int block = 0;
        int length = 0;
        int startByte = 0;
        int nextBlock;
        int nextByte;
        int count;

        // if given name is less than 8 characters, add whitespace
        if(file.length() < 8) {
            for(int i = file.length(); i < 8; i++){
                file = file + " ";
            }
        }

        // Look through the file tables, if the names are exactly the same, collect other information
        while(!fileFound) {
            for(int i = 0; i < storage[0].length; i+=11) {
                if(fileFound) {
                    break;
                }
                if(storage[0][i] != null) {
                    for(int j = 0; j < 8; j++) {
                        System.out.println();
                        if(String.valueOf(file.charAt(j)).equals(storage[0][i+j])) {
                            charCount++;
                            if(charCount == 8) {
                                fileFound = true;
                                block = Integer.parseInt(storage[0][i+j+1]);
                                length = Integer.parseInt(storage[0][i+j+2]);
                                startByte = Integer.parseInt(storage[0][i+j+3]);
                                break;
                            }
                        }
                    }
                    if(charCount != 8){
                        charCount = 0;
                    }
                }
            }

            if(!fileFound) {
                fullFile = new StringBuilder("File not found");
                return fullFile.toString();
            }
        }

        // For Contiguous, make bytes null in sequential order
        if(fileAlloc == 1){
            count = startByte;
            for(int i = startByte; i < startByte + length; i++) {
                if(count == 512){
                    updateBitmap(block, 0);
                    count = 0;
                    block++;
                }
                storage[block][count] = null;
                count++;
                updateBitmap(block, 0);
            }
        // For Chained, get the pointer for the next byte, store it, delete current byte, and loop
        // to make the entire file null
        } else if(fileAlloc == 2){
            String[] storageInput = storage[block][startByte].split("\\W+");
            storage[block][startByte] = null;
            nextBlock = Integer.parseInt(storageInput[1]);
            nextByte = Integer.parseInt(storageInput[2]);
            Arrays.fill(storageInput, null);
            // Once first pointer is found, loop through the file
            for(int i = 0; i < length - 1; i++){
                storageInput = storage[nextBlock][nextByte].split("\\W+");
                storage[nextBlock][nextByte] = null;
                nextBlock = Integer.parseInt(storageInput[1]);
                nextByte = Integer.parseInt(storageInput[2]);
                Arrays.fill(storageInput, null);
                updateBitmap(block, 0);
            }
        // For Indexed, loop through index block to remove itself and the byte it points to
        } else {
            count = startByte;
            for(int i = startByte; i < startByte + length; i++){
                if(count == 512){
                    updateBitmap(block, 0);
                    count = 0;
                    block++;
                }
                String[] storageInput = storage[block][count].split("\\W+");
                storage[block][count] = null;
                nextBlock = Integer.parseInt(storageInput[0]);
                nextByte = Integer.parseInt(storageInput[1]);
                storage[nextBlock][nextByte] = null;
                count++;
                updateBitmap(block, 0);
            }
        }

        // Print success prompt and return
        fullFile = new StringBuilder(file + " deleted");
        removeFileTable(file);
        return fullFile.toString();
    }

    public static String displayBlock(int blockNum, int fileAlloc) {
        // Loop though a block with 512 bytes and print them in 32 bytes per line
        StringBuilder blockContent = new StringBuilder();
        for(int i = 0; i < storage[blockNum].length; i++){
            if(i % 32 == 0) {
                blockContent.append("\n");
            }
            if(storage[blockNum][i] != null) {
                if(fileAlloc == 1){
                    blockContent.append(storage[blockNum][i]);
                } else{
                    blockContent.append(storage[blockNum][i].charAt(0));
                }
            } else {
                blockContent.append(".");
            }
        }

        return blockContent.toString();
    }

    public static String printFileTable(int fileAlloc) {
        // Print the file table that hold the necessary information for files
        StringBuilder toReturn = new StringBuilder();
        int counter = 0;
        // If Contiguous or Chained, print file name, start block, and file length
        if(fileAlloc == 1 || fileAlloc == 2){
            for(int i = 0; i < storage[0].length; i++) {
                if(storage[0][i] != null) {
                    toReturn.append(storage[0][i]);
                    if(counter == 7 || counter == 8) {
                        toReturn.append(" ");
                    }
                    counter++;
                }
                if(counter == 10) {
                    toReturn.append("\n");
                    counter = 0;
                    i++;
                }
            }
        // If Indexed, print file name and start block
        } else {
            for(int i = 0; i < storage[0].length; i++) {
                if(storage[0][i] != null) {
                    if(counter < 9){
                        toReturn.append(storage[0][i]);
                        if(counter == 7) {
                            toReturn.append(" ");
                        }
                    }
                    counter++;
                }
                if(counter == 10) {
                    toReturn.append("\n");
                    counter = 0;
                    i++;
                }
            }
        }
        return toReturn.toString();
    }

    public static String printBitmap() {
        StringBuilder bMap = new StringBuilder();
        for(int i = 0; i < storage[1].length; i++) {
            if(i % 32 == 0) {
                bMap.append('\n');
            }
            bMap.append(storage[1][i]);
        }
        return bMap.toString();
    }

    public static void initiateBitmap() {
        // Gives each value in the second block a "0" value, unless it's the first or second value
        // The first and second values represent the File Table and Bitmap respectively
        for(int i = 0; i < 512; i++) {
            if(i == 0 || i == 1) {
                storage[1][i] = "1";
            } else {
                storage[1][i] = "0";
            }
        }
    }

    public static void updateBitmap(int blockNum, int typeOfChange) {
        // Depending on the type of change, change a bitmap value to either '0' or '1'
        int count = 0;

        // If adding bytes to a block, make sure the bitmap displays '1' if at least one value exists
        if(typeOfChange == 1) {
            for(int i = 0; i < storage[blockNum].length; i++) {
                if(storage[blockNum][i] != null) {
                    count++;
                }
            }
            if(count > 0){
                storage[1][blockNum] = "1";
            }
        // If deleting bytes to a block, make sure the bitmap displays '0' if full block is null
        } else {
            for(int i = 0; i < storage[blockNum].length; i++) {
                if(storage[blockNum][i] == null) {
                    count++;
                }
            }
            if(count == 512){
                storage[1][blockNum] = "0";
            }
        }
    }
}


class file_system {
    public static void copyFile(String file, String newLoc, int fileAlloc) {
        // Looks through the physical files
        // If file is found, read through the file, copy it, anc store it
        // Else, return error value
        String s;
        int c = 0;
        try{
            StringBuilder myFile = new StringBuilder();
            File f = new File(file);
            Scanner readFile = new Scanner(f);
            while (readFile.hasNextLine()){
                String line = readFile.nextLine();
                c += line.length();
                myFile.append(line);
            }

            if(c > 5120) {
                System.out.println("File Too Large");
            } else {
                s = disk.storeFile(myFile.toString(), newLoc, file, fileAlloc);
                System.out.println(s);
            }
        } catch (IOException e) {
            System.out.println("File not found");
        }
    }

    public static void writeFile(String storedFile, String newFile, int fileAlloc){
        // Looks through the simulation for the file
        // If a file is found, write to a physical file
        // Else, return error message
        try {
            FileWriter f = new FileWriter(newFile);
            f.write(disk.displayFile(storedFile, fileAlloc));
            f.close();
            System.out.println(storedFile + " copied");
        } catch (IOException e) {
            System.out.println("File not found");
        }
    }
}
