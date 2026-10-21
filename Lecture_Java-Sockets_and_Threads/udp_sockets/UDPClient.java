import java.io.*;
import java.net.*;

public class UDPClient {
    public static void main(String args[]) throws Exception {
        // Δημιουργία αντικειμένου BufferedReader για ανάγνωση εισόδου από τον χρήστη
        BufferedReader inFromUser = new BufferedReader(new InputStreamReader(System.in));

        // Δημιουργία DatagramSocket για επικοινωνία μέσω UDP
        DatagramSocket clientSocket = new DatagramSocket();

        // Ανάλυση της διεύθυνσης IP του διακομιστή (σε αυτή την περίπτωση "localhost")
        InetAddress IPAddress = InetAddress.getByName("localhost");

        // Δημιουργία byte array για την αποστολή και λήψη δεδομένων
        byte[] sendData = new byte[1024];
        byte[] receiveData = new byte[1024];
        
        // Δημιουργία DatagramPacket για την λήψη δεδομένων από τον διακομιστή
        DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);


        // Διάβασμα μιας γραμμής κειμένου από τον χρήστη
        String sentence = inFromUser.readLine();

        // Μετατροπή του κειμένου σε bytes και αποθήκευση στο sendData
        sendData = sentence.getBytes();

        // Δημιουργία DatagramPacket για την αποστολή των δεδομένων στον διακομιστή
        DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, 9876);

        // Αποστολή του πακέτου στον διακομιστή
        clientSocket.send(sendPacket);

        // Δημιουργία DatagramPacket για την λήψη δεδομένων από τον διακομιστή
        DatagramPacket receivePacket1 = new DatagramPacket(receiveData, receiveData.length);

        // Λήψη ενός πακέτου από τον διακομιστή
        clientSocket.receive(receivePacket1);

        // Μετατροπή των ληφθέντων δεδομένων σε string
        String modifiedSentence = new String(receivePacket1.getData());

        // Εκτύπωση των ληφθέντων δεδομένων από τον διακομιστή
        System.out.println("FROM SERVER:" + modifiedSentence);

        // Κλείσιμο του DatagramSocket
        clientSocket.close();
}
}