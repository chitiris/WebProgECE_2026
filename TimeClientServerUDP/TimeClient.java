import java.io.*;
import java.net.*;

public class TimeClient {
  
  public static void timeclient(DatagramSocket serversoc, InetAddress hostaddress, int port) {
  // Declare a datagram packet...
  DatagramPacket packet;
  // Declare a byte array to send as a request and
  // receive the answer
  byte[] message = new byte[256];
  try {
      // construct the packet with all the information 
      // needed
      packet = new DatagramPacket(message, 256, hostaddress, port);
      System.out.println("Sending the request for the time...");
      // since the socket is already open, send the 
      // request
      serversoc.send(packet);
      // Now, get the reply
      System.out.println("Waiting for reply...");
      serversoc.receive(packet);
     // String mesg = new String(packet.getData(), 0);
      String mesg = data(packet.getData()).toString();

      // Print the server"s time
      System.out.println("Time at server location " 
                  + packet.getAddress() + ":" 
                  + packet.getPort() + " is " + mesg );
  } catch(Exception exc) {
      System.out.println("Error : " + 
      exc.toString());
  }
} // End of timeclient()

  public static StringBuilder data(byte[] a) 
  { 
      if (a == null) 
          return null; 
      StringBuilder ret = new StringBuilder(); 
      int i = 0; 
      while (a[i] != 0) 
      { 
          ret.append((char) a[i]); 
          i++; 
      } 
      return ret; 
  } 
  
  public static void main(String args[]) {
  // We use the command line to get the address of the
  // time server
  InetAddress hostAddress;
  int portnum;
  // Declare a datagram socket
  DatagramSocket serversoc;

  // initialize the network
  try {
      // Get network info
      hostAddress = InetAddress.getByName("127.0.0.1");
      portnum = 8505;
      serversoc = new DatagramSocket();
      timeclient(serversoc, hostAddress, portnum);
      // Don"t forget to release the resource...
      // by closing the datagram socket.
      if (serversoc != null) 
    	  serversoc.close();
  } catch (UnknownHostException uhe) {
      System.out.println("Unknown host : " +    uhe.toString());
  } catch (SocketException exc) {
      System.out.println("Error : " + exc);
  }
}         // End of main()

	
	
}