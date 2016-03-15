package server;/*
 * Server App upon TCP
 * A thread is created for each connection request from a client
 * So it can handle Multiple Client Connections at the same time
 * Weiying Zhu
 */ 

import java.io.IOException;
import java.net.ServerSocket;

public class HTTPMultiServer {
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
            new TCPMultiServerThread(serverTCPSocket.accept()).start();
            System.out.println("TCP Socket opened with client");
        }
			
        serverTCPSocket.close();
        System.out.println("Socket closed."); //This never happens
    }
}
