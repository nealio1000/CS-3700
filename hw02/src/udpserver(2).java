/**
 * Created by neal on 2/1/2015.
 */

import java.io.BufferedReader;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;


public class udpserver {
    public static void main(String[] args) throws IOException {

        DatagramSocket udpServerSocket = null;
        BufferedReader in = null;
        DatagramPacket udpPacket = null, udpPacket2 = null;
        String fromClient = null, toClient = null;
        boolean morePackets = true;




        // Inventory
        String inventory[][] = new String[6][4];
        inventory[0][0] = "00001"; inventory[0][1] = "New Inspiron 15"; inventory [0][2]="$379.99"; inventory[0][3] = "157";
        inventory[1][0] = "00002"; inventory[1][1] = "New Inspiron 17"; inventory [1][2]="$449.99"; inventory[1][3] = "128";
        inventory[2][0] = "00003"; inventory[2][1] = "New Inspiron 15R"; inventory [2][2]="$549.99"; inventory[2][3] = "202";
        inventory[3][0] = "00004"; inventory[3][1] = "New Inspiron 15z Ultrabook"; inventory [3][2]="$749.99"; inventory[3][3] = "315";
        inventory[4][0] = "00005"; inventory[4][1] = "XPS 14 Ultrabook"; inventory [4][2]="$999.99"; inventory[4][3] = "261";
        inventory[5][0] = "00001"; inventory[5][1] = "New XPS 12 Ultrabook XPS"; inventory [5][2]="$1199.99"; inventory[5][3] = "178";

        byte[] buf = new byte[256];

        udpServerSocket = new DatagramSocket(5678);

        while (morePackets) {
            try {
                // receive UDP packet from client
                udpPacket = new DatagramPacket(buf, buf.length);
                udpServerSocket.receive(udpPacket);
                fromClient = new String(udpPacket.getData(), 0, udpPacket.getLength());
                if(fromClient.equals(null))
                    System.out.println("Message not received");
                else
                    System.out.println("Message Recieved: " + fromClient);

                int id = Integer.parseInt(fromClient);

                // get the response
                if(inventory[id - 1][1].length() > 17)
                    toClient = inventory[id - 1][0] + "\t\t" + inventory[id - 1][1] + "\t\t"
                            +  inventory[id - 1][2] + "\t\t\t" + inventory[id - 1][3] +"\t\t\t\t";
                else
                    toClient = inventory[id - 1][0] + "\t\t" + inventory[id - 1][1] + "\t\t\t\t"
                            +  inventory[id - 1][2] + "\t\t\t\t" + inventory[id - 1][3] +"\t\t\t\t";

                    // send the response to the client at "address" and "port"
                    InetAddress address = udpPacket.getAddress();
                    int port = udpPacket.getPort();
                    byte[] buf2 = toClient.getBytes();
                    udpPacket2 = new DatagramPacket(buf2, buf2.length, address, port);
                    udpServerSocket.send(udpPacket2);

            } catch (IOException e) {
                e.printStackTrace();
                morePackets = false;
            }
        }
        udpServerSocket.close();
    }
}