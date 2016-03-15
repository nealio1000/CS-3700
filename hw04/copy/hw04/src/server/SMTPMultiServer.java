
/*
 * Server App upon TCP
 * A thread is created for each connection request from a client
 * So it can handle Multiple Client Connections at the same time
 * Weiying Zhu
 */ 

import java.net.*;
import java.io.*;

public class SMTPMultiServer {
    public static void main(String[] args) throws IOException {
        ServerSocket serverTCPSocket = null;
        boolean listening = true;
        try {
            serverTCPSocket = new ServerSocket(4567);
        } catch (IOException e) {
            System.err.println("Could not listen on port: 4567.");
            System.exit(-1);
        }
        while (listening){
            new SMTPMultiServerThread(serverTCPSocket.accept()).start();
        }
        serverTCPSocket.close();
    }
}
