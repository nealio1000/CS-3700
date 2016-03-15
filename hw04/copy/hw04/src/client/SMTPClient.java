/*
 * Client App upon TCP
 *
 * Weiying Zhu
 *
 */

// TO DO:
// Send message body in mail message format


import java.io.*;
import java.net.*;
import java.util.ArrayList;

public class SMTPClient {
    public static void main(String[] args) throws IOException {

        Socket tcpSocket = null;
        PrintWriter socketOut = null;
        BufferedReader socketIn = null;
        BufferedReader sysIn = new BufferedReader(new InputStreamReader(System.in));
        String cont = "y";
        while(cont.equals("y")) {
            System.out.println("Enter public ip / dns of server: ");
            String ip = sysIn.readLine();
            System.out.println("Enter sender's email address: ");
            String sender = sysIn.readLine();
            while (!sender.matches("\\w+\\.*\\w*@(\\w+\\.\\w+)(\\.*\\w*)*")) {
                System.out.println("Enter sender's email address: ");
                sender = sysIn.readLine();
            }
            System.out.println("Enter receiver's email address: ");
            String receiver = sysIn.readLine();
            while (!receiver.matches("\\w+\\.*\\w*@(\\w+\\.\\w+)(\\.*\\w*)*")) {
                System.out.println("Enter receiver's email address: ");
                receiver = sysIn.readLine();
            }
            System.out.println("Enter subject: ");
            String subject = sysIn.readLine();
            ArrayList<String> data = new ArrayList<String>();
            String line = "";
            System.out.println("Enter email contents: ");
            while (!(line = sysIn.readLine()).equals("."))
                data.add(line);

            try {
                tcpSocket = new Socket(ip, 4567);
                socketOut = new PrintWriter(tcpSocket.getOutputStream(), true);
                socketIn = new BufferedReader(new InputStreamReader(tcpSocket.getInputStream()));
            } catch (UnknownHostException e) {
                System.err.println("Don't know about host: " + ip);
                System.exit(1);
            } catch (IOException e) {
                System.err.println("Couldn't get I/O for the connection to: " + ip);
                System.exit(1);
            }

            socketOut.println(ip); //Send server ip to server
            line = socketIn.readLine(); // read response from server
            System.out.println(line);
            int index = 0;
            while (sender.charAt(index) != '@' && index < sender.length()) {
                index++;
            }
            String senderDomain = sender.substring(index, sender.length());

            //********* HARDCODE VERSION  *************
            if (line.contains("220 OK")) {
                System.out.println("Telnet console opened: ");

                line = "HELO " + senderDomain;
                do {
                    System.out.println(line);
                    long startTime = System.currentTimeMillis();
                    socketOut.println(line);  //send HELO
                    line = socketIn.readLine(); //Read HELO response
                    System.out.println("RTT: " + (System.currentTimeMillis() - startTime) + " msecs");
                    System.out.println(line);
                }
                while (line.equals("503 5.5.2 Send hello first"));
                do {
                    System.out.println("MAIL FROM: <" + sender + ">");
                    long startTime = System.currentTimeMillis();
                    socketOut.println("MAIL FROM: <" + sender + ">");
                    line = socketIn.readLine();
                    System.out.println("RTT: " + (System.currentTimeMillis() - startTime) + " msecs.");
                    System.out.println(line);
                }
                while (line.equals("503 5.5.2 Need mail command"));
                do {
                    System.out.println("RCPT TO: <" + receiver + ">");
                    long startTime = System.currentTimeMillis();
                    socketOut.println("RCPT TO: <" + receiver + ">");
                    line = socketIn.readLine();
                    System.out.println("RTT: " + (System.currentTimeMillis() - startTime) + " msecs.");
                    System.out.println(line);
                }
                while (line.equals("503 5.5.2 Need rcpt command"));

                do {
                    System.out.println("DATA");
                    long startTime = System.currentTimeMillis();
                    socketOut.println("DATA");
                    line = socketIn.readLine();
                    System.out.println("RTT: " + (System.currentTimeMillis() - startTime) + " msecs.");
                    System.out.println(line);
                }
                while (line.equals("503 5.5.2 Need data command"));

                data.add(".");
                long startTime = System.currentTimeMillis();
                socketOut.println("To: " + receiver);
                socketOut.println("From: " + sender);
                socketOut.println("Subject: " + subject);
                socketOut.println();
                for (int i = 0; i < data.size(); i++)
                    socketOut.println(data.get(i));
                System.out.println(socketIn.readLine());
                System.out.println("RTT: " + (System.currentTimeMillis() - startTime) + " msecs.");
            }
            // ****** END HARDCODE VERSION ***********
            cont = "";
            while(!cont.equals("y") || !cont.equals("n")) {
                System.out.println("\nWould you like to continue? (y/n)");

                cont = sysIn.readLine();
                if(cont.equals("y") || cont.equals("n"))
                    break;
                else {
                    System.out.println("Please try again.");

                }
            }
            if(cont.equals("n")){
                System.out.println("QUIT");
                socketOut.println("QUIT");
                System.out.println(socketIn.readLine());
            }
            System.out.println();
        }

        socketOut.close();
        socketIn.close();
        sysIn.close();
        tcpSocket.close();
    }
}
