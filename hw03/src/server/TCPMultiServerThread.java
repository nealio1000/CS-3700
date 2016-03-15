package server;/*
 * Server App upon TCP
 * A thread is started to handle every client TCP connection to this server
 * Weiying Zhu
 */ 

import java.io.*;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Scanner;

public class TCPMultiServerThread extends Thread {
    private Socket clientTCPSocket = null;

    public TCPMultiServerThread(Socket socket) {
		super("TCPMultiServerThread");
		clientTCPSocket = socket;
    }

    public void run() {
        try {
	 	        PrintWriter cSocketOut = new PrintWriter(clientTCPSocket.getOutputStream(), true);
	  		    BufferedReader cSocketIn = new BufferedReader(new InputStreamReader(
				                                                clientTCPSocket.getInputStream()));

	            String toClient, status, filename;


                // read first line of message from client
                String fromClient = cSocketIn.readLine();

                // parse by space to get the request type
                String []message = fromClient.split(" ");

                // read next line of message to get host info
                String host = cSocketIn.readLine();

                // read next line of message to get user agent info
                String userAgent = cSocketIn.readLine();

                // filename is the requested file minus the '/' character
                filename = message[1];
                filename = filename.substring(1, filename.length());

                File file = new File(filename);

                // Does the file exist? if not, 404
                if(!file.exists()) {
                    status = "404 Not Found";
                    toClient = sendResponse(status, message);
                    cSocketOut.println(toClient);
                }
                // file exists, request is get, respond 200 OK
			    else if(message[0].equals("GET") && file.exists()) {
                    status = "200 OK";
                    toClient = sendResponse(status, message, file);
                    cSocketOut.println(toClient);
                }
                // file exists, request is not get, respond 400 Bad Request
                else if (!message[0].equals("GET")){
                    status = "400 Bad Request";
                    toClient = sendResponse(status, message);
                    cSocketOut.println(toClient);
                }

		        cSocketOut.close();
		        cSocketIn.close();

		        clientTCPSocket.close();

		} catch (IOException e) {
		    e.printStackTrace();
		}
    }
    // response for non get
    public static String sendResponse(String status, String[] message){
        String timeStamp = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(Calendar.getInstance().getTime());
        return message[2] + " " + status + "\r\n" + "Date: "
                + timeStamp + "\r\n" + "Server: whatsup" + "\r\n";
    }
    // response for get
    public static String sendResponse(String status, String[] message, File file){
        String timeStamp = new SimpleDateFormat(
                        "dd/MM/yyyy HH:mm:ss").format(Calendar.getInstance().getTime());
        String response = message[2] + " " + status + "\r\n" + "Date: "
                            + timeStamp + "\r\n" + "Server: whatsup" + "\r\n";
        try {
            Scanner inFile = new Scanner(new FileReader(file));
            while(inFile.hasNext())
                response = response + inFile.nextLine() + "\n";
        }
        catch (FileNotFoundException e){
            System.out.println(e);
        }
        return response;
    }
}
