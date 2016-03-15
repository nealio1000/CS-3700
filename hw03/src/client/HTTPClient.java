package client;/*
 * Client App upon TCP
 *
 * Weiying Zhu
 *
 */ 

import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Scanner;

public class HTTPClient {

    private static String USER_AGENT = "Mozilla/5.0";

    public static void main(String[] args) throws IOException {

        Socket tcpSocket = null;
        PrintWriter socketOut = null;
        BufferedReader socketIn = null;
        Scanner input = new Scanner(System.in);

        String request;
        String cont = "y";
        while(cont.equals("y")) {
           System.out.println("Enter DNS or public ip of HTTP Server: ");
           String ip = input.nextLine();
           try {

               long startTime = System.currentTimeMillis();
               tcpSocket = new Socket(ip, 4567);
               long rtt = System.currentTimeMillis() - startTime;
               System.out.println("TCP socket buildup RTT: " + rtt + " msecs.");
               socketOut = new PrintWriter(tcpSocket.getOutputStream(), true);
               socketIn = new BufferedReader(new InputStreamReader(tcpSocket.getInputStream()));
           } catch (UnknownHostException e) {
               System.err.println("Don't know about host: " + ip);
               System.exit(1);
           } catch (IOException e) {
               System.err.println("Couldn't get I/O for the connection to: " + ip);
               System.exit(1);
           }

           System.out.println("Enter HTTP method type: ");
           String methodType = input.nextLine().toUpperCase();
           System.out.println("Enter name of HTM/L file: ");
           String filename = "/" + input.nextLine();
           System.out.println("Enter HTTP Version: ");
           String version = input.nextLine();
           // System.out.println("Enter User Agent: ");
           String userAgent = USER_AGENT;

           String fromUser = methodType + " " + filename + " " + version + "\r\n" + "Host: " + ip + "\r\n"
                   + "User-Agent: " + userAgent + "\r\n\r\n\r\n\r\n";


           String fromServer;

           System.out.println("Client: " + fromUser);
           socketOut.println(fromUser);
           System.out.println("Server: ");
           long httpQueryStart = System.currentTimeMillis();
           ArrayList<String> responseLines = new ArrayList<String>();
           while ((fromServer = socketIn.readLine()) != null) {
              // System.out.println(fromServer);
               responseLines.add(fromServer);
           }

           System.out.println("HTTP Query RTT: " + (System.currentTimeMillis() - httpQueryStart) + "msecs");

           System.out.println(responseLines.get(0) + "\n" + responseLines.get(1) + "\n" + responseLines.get(2));

           File file = new File("output.htm");
           PrintWriter outFile = new PrintWriter(file);
           for (int i = 3; i < responseLines.size(); i++)
               outFile.println(responseLines.get(i));
           outFile.close();



           cont = "";
           while(!cont.equals("y") || !cont.equals("n")) {
               System.out.println("Would you like to continue? (y/n)");
               cont = input.nextLine();
               if(cont.equals("y") || cont.equals("n"))
                   break;
               else {
                   System.out.println("Please try again.");
               }
           }
           System.out.println();
       }
        System.out.println("Goodbye.");
           socketOut.close();
           socketIn.close();
           tcpSocket.close();

    }
}
