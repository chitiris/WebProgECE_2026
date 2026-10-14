import java.io.*;
import java.net.*;

public class UDPServer {
    public static void main(String args[]) throws Exception {
        // Δημιουργία DatagramSocket για να ακούει σε ένα συγκεκριμένο θύρα (9876)
        DatagramSocket serverSocket = new DatagramSocket(9876);
        
        // Εμφάνιση μηνύματος που δείχνει ότι ο UDP server είναι ενεργός
        System.out.println("UDP Server is ON");
  
        while (true) {
            // Δημιουργία byte arrays για λήψη και αποστολή δεδομένων
            byte[] receiveData = new byte[1024];
            byte[] sendData = new byte[1024];
            
            // Δημιουργία DatagramPacket για τη λήψη δεδομένων από τους πελάτες
            DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
            
            // Λήψη πακέτου από τον πελάτη
            serverSocket.receive(receivePacket);
            
            // Μετατροπή των ληφθέντων δεδομένων σε συμβολοσειρά (string)
            String sentence = new String(receivePacket.getData());
            InetAddress IPAddress = receivePacket.getAddress();
            int port = receivePacket.getPort();
            
            // Μετατροπή της συμβολοσειράς σε κεφαλαία γράμματα
            String capitalizedSentence = sentence.toUpperCase();
            System.out.println(capitalizedSentence);
            
            // Μετατροπή της κεφαλαίας συμβολοσειράς σε bytes
            sendData = capitalizedSentence.getBytes();
            
            // Δημιουργία DatagramPacket για την αποστολή δεδομένων προς τον πελάτη
            DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, port);
            
            // Αποστολή του πακέτου προς τον πελάτη
            serverSocket.send(sendPacket);
        }
    }
}