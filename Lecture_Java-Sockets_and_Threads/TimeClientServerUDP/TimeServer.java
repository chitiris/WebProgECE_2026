import java.io.*;
import java.net.*;
import java.util.*; // For the date functions...

public class TimeServer {
    public static void main(String args[]) {
        // Our datagram socket for I/O
        DatagramSocket s = null;
        // Our packet for getting the request and sending the 
        // reply
        DatagramPacket packet = null;
        // Create a receive buffer
        byte[] buf = new byte[256];
        // Create the packet to receive the request
        packet = new DatagramPacket(buf, buf.length);
        // now create a socket to listen in
        try {
            s = new DatagramSocket(8505);
        } catch(Exception e) {
            System.out.println("Error : " + 
            e.toString());
        }
        // Now sit in an infinite loop and reply to the 
        // queries...
        while (true) {
            // sit around and wait for a request
            try {
                s.receive(packet);
            } catch (Exception e) {
                System.out.println("Error : " + 
                e.toString());
            }
            // Get the client info
            InetAddress cl = packet.getAddress();
            int port = packet.getPort();
            System.out.println("Client from " + cl.getHostAddress() 
                + ":" + port + " requested the time.");
            // Construct the response and sent it back...
            //String localtime = new Date().toString();
            Calendar rightNow = Calendar.getInstance();
     
            String localtime =""+rightNow.get(Calendar.DATE)+"/"+(rightNow.get(Calendar.MONTH)+1)+"/"+ rightNow.get(Calendar.YEAR)+ "-"+rightNow.get(Calendar.HOUR )+":"+rightNow.get(Calendar.MINUTE)+":"+ rightNow.get(Calendar.SECOND);

            //String localtime ="The Current Date is:" + rightNow.getTime()
			
            // Convert the localtime string to an array 
            // of bytes
            buf = localtime.getBytes();
            //localtime.getBytes(0, localtime.length(), buf, 0);
            // Construct the packet
            try {
                packet = new DatagramPacket(buf,
                buf.length, cl, port);
                // Send it!
                s.send(packet);
            } catch (Exception e) {
                System.out.println("Error : " + 
                e.toString());
            }
            System.out.println("Client served...");
        }
    }
}