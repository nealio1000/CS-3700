/**
 * Created by neal on 2/1/2015.
 */
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Scanner;

public class udpclient {
    public static void main(String[] args) throws IOException {

        File file = new File("testResultsClient.txt");
        PrintWriter outfile = new PrintWriter((file));

        System.out.println("Enter the DNS or IP of the server program. ");
        outfile.println("Enter the DNS or IP of the server program. ");

        Scanner input = new Scanner(System.in);
        String ip = input.nextLine();

        outfile.println(ip);
        String cont = "y";

        while (cont.equals("y")) {
            //Display Table
            System.out.println("Item ID\t\tItem Description");
            System.out.println("00001\t\tNew Inspiron 15");
            System.out.println("00002\t\tNew Inspiron 17");
            System.out.println("00003\t\tNew Inspiron 15R");
            System.out.println("00004\t\tNew Inspiron 15z Ultrabook");
            System.out.println("00005\t\tXPS 14 Ultrabook");
            System.out.println("00006\t\tNew XPS 12 UltrabookXPS");

            outfile.println("Item ID\t\tItem Description");
            outfile.println("00001\t\tNew Inspiron 15");
            outfile.println("00002\t\tNew Inspiron 17");
            outfile.println("00003\t\tNew Inspiron 15R");
            outfile.println("00004\t\tNew Inspiron 15z Ultrabook");
            outfile.println("00005\t\tXPS 14 Ultrabook");
            outfile.println("00006\t\tNew XPS 12 UltrabookXPS");


            boolean bool = false;
            String itemID = "";
            while (bool == false) {
                System.out.println("\nInput an ItemID: ");
                outfile.println("\nInput an ItemID: ");
                itemID = input.nextLine();
                outfile.println(itemID);
                if (itemID.equals("00001") || itemID.equals("00002") || itemID.equals("00003") ||
                        itemID.equals("00004") || itemID.equals("00005") || itemID.equals("00006"))
                    bool = true;
                else {
                    System.out.println("Please try again. ");
                    outfile.println("Please try again. ");
                }
            }

            // creat a UDP socket
            DatagramSocket udpSocket = new DatagramSocket();

            String fromServer;


            // send request
            InetAddress address = InetAddress.getByName(ip);
            byte[] buf = itemID.getBytes();
            DatagramPacket udpPacket = new DatagramPacket(buf, buf.length, address, 5678);
            long sendtime = System.currentTimeMillis();
            udpSocket.send(udpPacket);

            // get response
            byte[] buf2 = new byte[256];
            DatagramPacket udpPacket2 = new DatagramPacket(buf2, buf2.length);
            udpSocket.receive(udpPacket2);
            long receivetime = System.currentTimeMillis();

            long rtt = receivetime - sendtime;
            // display response
            fromServer = new String(udpPacket2.getData(), 0, udpPacket2.getLength());
            System.out.println("Item ID\t\tItem Description\t\t\tUnit Price\t\t\tInventory\t\t\tRTT of Query\t\t\t");
            outfile.println("Item ID\t\tItem Description\t\t\tUnit Price\t\t\tInventory\t\t\tRTT of Query\t\t\t");
            System.out.println(fromServer + "\t\t" + rtt);
            outfile.println(fromServer + "\t\t" + rtt);
            udpSocket.close();
            cont = "";
            while(!cont.equals("y") || !cont.equals("n")) {
                System.out.println("Would you like to continue? (y/n)");
                outfile.println("Would you like to continue? (y/n)");
                cont = input.nextLine();
                outfile.println(cont);
                if(cont.equals("y") || cont.equals("n"))
                    break;
                else {
                    System.out.println("Please try again.");
                    outfile.println("Please try again.");
                }
            }
            System.out.println();
        }
        System.out.println("Goodbye.");
        outfile.println("Goodbye.");
        outfile.close();
    }
}
