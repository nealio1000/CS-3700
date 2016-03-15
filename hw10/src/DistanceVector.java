import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Scanner;

/**
 * Implementation of the Distance Vector Algorithm
 * @author Neal Friedman and William Holden
 * @version CS 3700 HW10
 */
public class DistanceVector {
    private static int totalRouters;
    private static int[][] costData;
    private static int[][] sourceVectorData;
    private static int[][] neighborVectorData;

    /**
     * Constructor for Distance Vector Algorithm Application
     */
    public DistanceVector(){}

    /**
     * Start the application
     */
    public void start(){
        costData = getCostData();
        sourceVectorData = getSourceVectorData(totalRouters);
        neighborVectorData = getNeighborVectorData(costData.length);
        int eventNumber = pickEvent();
        distanceVectorAlgorithm(costData, sourceVectorData, neighborVectorData, eventNumber);
    }

    /**
     * Scans in a file stored in the source directory named 'cost.txt'
     * and then creates a table out of the data representing neighboring node id
     * in the left column and link cost in the right column.
     * @return Returns a table of the cost data.
     */
    public static int[][] getCostData() {
        int[][] routers = new int[0][];
        totalRouters = 0;
        BufferedReader sysIn = new BufferedReader(new InputStreamReader(System.in));

        int lineCounter = 0;
        try {
            Scanner fileInput = new Scanner(new FileReader("cost.txt"));
            while (fileInput.hasNext()) { // Find out how many lines are in the cost file
                lineCounter++;
                fileInput.nextLine();
            }

            // Validate that the total routers that the user entered was greater
            // than or equal to 2.
            while (totalRouters < 2) {
                System.out.println("Enter the number of routers: ");
                try {
                    totalRouters = Integer.parseInt(sysIn.readLine());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if (totalRouters < 2)
                    System.out.println("Please enter a number greater than or equal to 2");
            }
            routers = new int[lineCounter][2];

            // Assume the file is false, validate as true at end
            // once the file has been checked
            boolean validFile = false;

            while (!validFile) {
                boolean flag = true;
                int index = 0; //index for while

                fileInput = new Scanner(new FileReader("cost.txt")); //reset filereader to beginning of file

                while (fileInput.hasNext()) { // build cost data array
                    routers[index][0] = fileInput.nextInt();
                    routers[index][1] = fileInput.nextInt();

                    if (routers[index][0] < 1 && routers[index][0] > (totalRouters - 1)) {
                        System.out.println("BAD DATA FILE. CHECK ROW: " + (index + 1) + " COLUMN: 1");
                        flag = false;
                    }
                    if (routers[index][1] < 1) {
                        System.out.println("BAD DATA FILE. CHECK ROW: " + (index + 1) + " COLUMN: 2");
                        flag = false;
                    }
                    index++;
                }

                // If the flag is still set to true then
                // the file is valid. Set validFile to
                // true.
                if (flag)
                    validFile = true;
            }
        }
        catch(FileNotFoundException e){
            e.printStackTrace();
        }
        System.out.println("\nCost Data Table: ");
        printTable(routers);
        return routers;
    }

    /**
     * Scans in a file stored in the source directory named 'source_vectors.txt'
     * and then creates a table out of the data representing the distance vectors in
     * the first row and the corresponding link vectors in the second row.
     * @param n The total number of routers.
     * @return Returns a table of with the source vector data.
     */
    public static int[][] getSourceVectorData(int n) {
        int[][] sourceVectors = new int[2][n];
        Scanner inFile = null;
        try {
            inFile = new Scanner(new FileReader("source_vectors.txt"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        // Make source vector array
        for(int i = 0; i < sourceVectors.length; i++){
            for(int k = 0; k < sourceVectors[i].length; k++){
                if (inFile != null) {
                    sourceVectors[i][k] = inFile.nextInt();
                }
            }
        }
        System.out.println("\n\nSource Vectors Table: ");
        printTable(sourceVectors);

        return sourceVectors;
    }

    /**
     * Scans in a file stored in the source directory named 'neighbor_vectors.txt'
     * and then creates a table out of the data representing the topology of the
     * network. x values in the first column on the left, Dx[0]... Dx[n] in the
     * following columns.
     * @param n The total number of routers.
     * @return Returns a table with the neighbor vector data.
     */
    public static int[][] getNeighborVectorData(int n) {
        int[][] neighborVectors = new int[n][totalRouters + 1];
        Scanner inFile = null;
        try {
            inFile = new Scanner(new FileReader("neighbor_vectors.txt"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        for(int i = 0; i < neighborVectors.length; i++){
            for(int k = 0; k < neighborVectors[i].length; k++){
                if (inFile != null) {
                    neighborVectors[i][k] = inFile.nextInt();
                }
            }
        }
        System.out.println("\n\nNeighbor Vectors Table:");
        printTable(neighborVectors);
        return neighborVectors;
    }

    /**
     * Asks the user to select a specific event
     * @return eventNumber Returns the event number chosen by the user
     */
    public static int pickEvent(){
        int eventNumber = 0;
        try {
            BufferedReader sysIn = new BufferedReader(new InputStreamReader(System.in));

            // prompt the user to select either event 1 and 2. Verify they made correct choice
            // ** TO DO ** add regex to cause the prompt to repeat if user fails proper input
            while (eventNumber != 1 && eventNumber != 2) {
                System.out.println("\n\nSelect from the following events by entering the event number.");
                System.out.println("Event 1: a change in local link cost to a neighbor of router V0");
                System.out.println("Event 2: receiving a distance vector message from a neighbor of router V0");
                eventNumber = Integer.parseInt(sysIn.readLine());

                if(eventNumber != 1 && eventNumber != 2)
                    System.out.println("You entered an incorrect event number");
            }

            if(eventNumber == 1){
                int index = -1;
                int cost = 0;
                while(index < 0 || index > totalRouters) {
                    System.out.println("Enter the index of this neighboring router: ");
                    index = Integer.parseInt(sysIn.readLine());
                }
                while(cost < 1) {
                    System.out.println("Enter the new link cost to this neighboring router: ");
                    cost = Integer.parseInt(sysIn.readLine());
                }
                updateCost(index, cost, costData);

            }
            if(eventNumber == 2){
                int index = -1;
                int cost = 0;
                int counter = 1;
                while(index < 0 || index > totalRouters) {
                    System.out.println("Enter the index of the neighbor from which the distance\n" +
                            "vector message is received: ");
                    index = Integer.parseInt(sysIn.readLine());
                }
                while(cost < 1) {
                    System.out.println("Enter the new least cost from this neighbor to router Vj, Dx(j): ");
                    cost = Integer.parseInt(sysIn.readLine());
                    while(counter < totalRouters){
                        System.out.println("Enter the new least cost from this neighbor to router Vj, Dx(j): ");
                        cost = Integer.parseInt(sysIn.readLine());
                        counter++;
                    }
                }
            }
        }
        catch(IOException e){
            e.printStackTrace();
        }
        return eventNumber;
    }

    /**
     * Takes a 2d array and prints it to the console
     * @param a A table to print
     */
    public static void printTable(int[][]a){
        for (int[] anA : a) {
            System.out.println();
            for (int anAnA : anA) {
                System.out.print(anAnA + "\t");
            }
        }
    }

    /**
     * This method performs the distance vector algorithm
     * on a given set of tables.
     * @param cd The cost data table
     * @param sv The source vector table
     * @param nv The neighbor vector table
     * @param eventNum the event number chosen by the user
     */
    public static void distanceVectorAlgorithm(int[][] cd, int[][] sv, int[][] nv, int eventNum){
        // Initialization


    }

    public static void updateCost(int index, int newCost, int[][] cd){
        for(int i = 0; i < cd.length; i++){
            if(index == cd[i][0]){
                cd[i][1] = newCost;
            }
        }
        System.out.print("\nThe new cost data table looks like this: ");
        printTable(cd);
    }
}
