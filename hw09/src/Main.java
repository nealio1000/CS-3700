/* Main.java */
/**
 * Main program file. Builds router data
 * in the getRouterData method and puts it in
 * an array. Then constructs a graph and builds
 * the graph based on the data in the array that
 * its given.
 @author Neal Friedman
 @version CS3700 Hw09
 */

import java.io.*;
import java.util.*;

public class Main {
    public static int totalRouters;

    /**
     * Main method
     * @param args Program Arguments
     * @throws IOException
     */
    public static void main (String[] args) throws IOException {
        Graph graph = new Graph();
        graph = graph.build(getRouterData());
        dijkstra(graph);
    }

    /**
     * Prompts the user with some questions.
     * Scans the text file provided by the user.
     * Makes an array out of this data and returns it
     * after the data has been checked to be valid
     * @return The array with the router data
     * @throws FileNotFoundException
     */
    public static int[][] getRouterData() throws FileNotFoundException {
        totalRouters = 0;
        BufferedReader sysIn = new BufferedReader(new InputStreamReader(System.in));
        int[][]routers = new int[0][];

        // Validate that the total routers that the user entered was greater
        // than or equal to 2.
        while(totalRouters < 2) {
            System.out.println("Enter the number of routers: ");
            try {
                totalRouters = Integer.parseInt(sysIn.readLine());
            } catch (IOException e) {
                e.printStackTrace();
            }
            if(totalRouters < 2)
                System.out.println("Please enter a number greater than or equal to 2");
        }

        // Assume the file is false, validate as true at end
        // once the file has been checked
        boolean validFile = false;
        while(!validFile) {

            System.out.println("Enter text file name (please make sure it is in the source directory): ");
            String filename = null;
            try {
                filename = sysIn.readLine();
            } catch (IOException e) {
                e.printStackTrace();
            }

            // Find out how many lines are in the file
            // since each router can have multiple exits
            int lineCounter = 0;
            Scanner fileInput = new Scanner(new FileReader(filename));

            while(fileInput.hasNext()){
                lineCounter++;
                fileInput.nextLine();
            }
            fileInput = new Scanner(new FileReader(filename)); //reset scanner back to first line
            routers = new int[lineCounter][3];

            int index = 0; //index for while
            boolean flag = true;
            while (fileInput.hasNext()) {

                routers[index][0] = fileInput.nextInt();
                routers[index][1] = fileInput.nextInt();
                routers[index][2] = fileInput.nextInt();
                if (routers[index][0] < 1 || routers[index][0] > totalRouters) {
                    System.out.println("BAD DATA FILE. CHECK ROW: " + (index + 1) + " COLUMN: 1");
                    flag = false;
                }
                if (routers[index][1] < 1 || routers[index][1] > totalRouters) {
                    System.out.println("BAD DATA FILE. CHECK ROW: " + (index + 1) + " COLUMN: 2");
                    flag = false;
                }
                if (routers[index][2] < 0) {
                    System.out.println("BAD DATA FILE. Third column must be positive.\nCHECK ROW: " + (index + 1));
                    flag = false;
                }
                index++;
            }

            // If the flag is still set to true then
            // the file is valid. Set validFile to
            // true.
            if(flag)
                validFile = true;
        }
        return routers;
    }

    /**
     * Perform Dijkstra's Shortest Path Algorithm on the graph
     * @param g The graph to perform the algorithm on
     */
    public static void dijkstra(Graph g){

        //Initialization
        Set<Node> nPrime = new HashSet<>();
        ArrayList<Map<Node, Node>> yPrime = new ArrayList<>();
        nPrime.add(g.getStartNode());
        ArrayList<Node> pSubi = new ArrayList<>();
        Node current = g.getStartNode();


        // Make cost matrix. Note: Infinity is represented as -1 in the table
        // as this is an impossible cost anyways.
        int[][] costMatrix = buildCostMatrix(g);

        System.out.println("******* REMEMBER!! INFINITY IS DENOTED BY '-1' !!!!! *********");

//        while(!nPrime.contains(g.getFinishNode())) {
            //Perform Dijkstra's Algorithm

            for (int i = 0; i < costMatrix.length-1; i++) {

                System.out.println("\n\n******* Iteration #" + (i + 1) + "***********");
                printCostMatrix(costMatrix);
                int tmp;
                int minIndex = 0;
                Map<Node, Node> trans = new HashMap<>();
                int currentMin = 2147483647;  //literally highest number you can represent as an int
                for (int k = 0; k < costMatrix.length; k++) {
                    if (!nPrime.contains(g.getNode(k + 1)) && costMatrix[i][k] > 0) {
                        tmp = costMatrix[i][k];
                        if (tmp < currentMin) {
                            currentMin = tmp;
                            minIndex = k+1;
                        }
                    }
                }

                pSubi.add(current);
                trans.put(current, g.getNode(minIndex));
                System.out.println("\n\nCurrent N':");
                System.out.println(nPrime);
                nPrime.add(g.getNode(minIndex));
                System.out.println("\n\nCurrent Y':");
                System.out.println(yPrime);
                System.out.println("\n\nCurrent p(i): ");
                System.out.println(pSubi);
                yPrime.add(trans);

                for (int k = 0; k < costMatrix.length; k++) {
                    if (!nPrime.contains(g.getNode(k + 1)) && costMatrix[i + 1][k] > 0) {
                        costMatrix[i + 1][k] = costMatrix[i + 1][k] + g.getCost(g, current, g.getNode(minIndex));
                    }
                }
                current = g.getNode(minIndex);
            }
        buildForwardingTable(pSubi);

        //}
    }

    public static void buildForwardingTable(ArrayList<Node> p){
        System.out.println("\n\nHere is the resulting forwarding table: ");
        System.out.println("Desination\t\tLink");
        for(int i = 1; i < p.size(); i++){
            System.out.print(p.get(i) + "\t\t\t");
            for(int k = 0; k < i; k++){
                System.out.print(p.get(k) +", ");
            }
            System.out.println();
        }
    }

    public static int[][] buildCostMatrix(Graph g){
        int[][] costMatrix = new int[g.nodes().size()][g.nodes().size()];
        for(int i = 0; i < costMatrix.length; i++){
            for(int k = 0; k < costMatrix.length; k++){
                if( i == k){
                    costMatrix[i][k] = 0; // zeroes on the diagonal
                }
                else if(g.transitionFunction().get(g.getNode(i+1)).containsKey(g.getNode(k+ 1))) { // costs for each transition
                    costMatrix[i][k] = g.transitionFunction().get(g.getNode(i+1)).get(g.getNode(k+1));
                }
                else{
                    costMatrix[i][k] = -1; //Infinity is denoted as -1
                }
            }
        }

        return costMatrix;
    }

    public static void printCostMatrix(int[][] costMatrix){
        System.out.println("Current D(i): ");
        for(int i = 0; i < costMatrix.length; i++) {
            System.out.println();
            System.out.print(i + ":  ");
            for (int k = 0; k < costMatrix.length; k++)
                System.out.print(costMatrix[i][k] + ", ");
        }

    }
}