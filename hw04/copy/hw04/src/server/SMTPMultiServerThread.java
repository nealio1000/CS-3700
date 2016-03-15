
/*
 * Server App upon TCP
 * A thread is started to handle every client TCP connection to this server
 * Weiying Zhu
 */ 


import java.net.*;
import java.io.*;
import java.util.ArrayList;

public class SMTPMultiServerThread extends Thread {
    private Socket clientTCPSocket = null;

    public SMTPMultiServerThread(Socket socket) {
		super("TCPMultiServerThread");
		clientTCPSocket = socket;
    }

    public void run() {
		try {
            PrintWriter cSocketOut = new PrintWriter(clientTCPSocket.getOutputStream(), true);
            BufferedReader cSocketIn = new BufferedReader(new InputStreamReader(clientTCPSocket.getInputStream()));
            String fromClient, toClient;
            String serverIp = cSocketIn.readLine(); // Read server ip message
            String clientIp = clientTCPSocket.getInetAddress().toString();
            cSocketOut.println("220 OK " + serverIp); //Send server ip response
            String[] message;

            do {
                do {  // Listen for HELO
                    fromClient = cSocketIn.readLine();
                    System.out.println(fromClient);
                    message = fromClient.split(" ");
                    if (message[0].equals("HELO")) {
                        cSocketOut.println("250 " + serverIp + " Hello " + clientIp);
                    } else
                        cSocketOut.println("503 5.5.2 Send hello first");
                }
                while (!message[0].equals("HELO"));

                do { // Listen for MAIL FROM:
                    fromClient = cSocketIn.readLine();
                    System.out.println(fromClient);
                    message = fromClient.split(" ");
                    if (message[0].equals("MAIL") && message[1].equals("FROM:"))
                        cSocketOut.println("250 2.1.0 Sender OK");
                    else
                        cSocketOut.println("503 5.5.2 Need mail command");
                }
                while (!message[0].equals("MAIL") && !message[1].equals("FROM:"));

                do { //Listen for RCPT TO:
                    fromClient = cSocketIn.readLine();
                    System.out.println(fromClient);
                    message = fromClient.split(" ");
                    if (message[0].equals("RCPT") && message[1].equals("TO:"))
                        cSocketOut.println("250 2.1.5 Recipient OK");
                    else
                        cSocketOut.println("503 5.5.2 Need rcpt command");
                }
                while (!message[0].equals("RCPT") && !message[1].equals("TO:"));

                do { //Listen for DATA
                    fromClient = cSocketIn.readLine();
                    System.out.println(fromClient);

                    if (fromClient.equals("DATA"))
                        cSocketOut.println("250 2.1.5 Recipient OK");
                    else
                        cSocketOut.println("354 Start mail input; end with <CRLF>.<CRLF>");
                }
                while (!fromClient.equals("DATA"));

                // Read body of email into an arraylist line by line
                ArrayList<String> data = new ArrayList<String>();
                while (!(fromClient = cSocketIn.readLine()).equals("."))
                    data.add(fromClient);
                for (int i = 0; i < data.size(); i++)
                    System.out.println(data.get(i));

                cSocketOut.println("250 Message received and to be delivered");

                //Listen for QUIT
                fromClient = cSocketIn.readLine();
            }
            while(!fromClient.equals("QUIT"));

            // Send QUIT response
            cSocketOut.println("221 <" + serverIp +"> closing connection");


		    cSocketOut.close();
		    cSocketIn.close();
		    clientTCPSocket.close();
		} catch (IOException e) {
		    e.printStackTrace();
		}
    }
}
