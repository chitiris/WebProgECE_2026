import java.io.*;
import java.net.*;

public class TCPClient  {
    public static void main(String argv[]) throws Exception {
        // Δηλώνουμε τις μεταβλητές που θα χρησιμοποιηθούν
        String sentence; // Για την είσοδο από τον χρήστη
        String modifiedSentence; // Για την απάντηση από τον διακομιστή

        // Δημιουργούμε ένα αντικείμενο BufferedReader για την είσοδο από τον χρήστη
        BufferedReader inFromUser = new BufferedReader(
            new InputStreamReader(System.in));

        // Δημιουργούμε ένα socket για τη σύνδεση με τον διακομιστή (server)
        Socket clientSocket = new Socket("localhost", 6789);

        // Δημιουργούμε ένα αντικείμενο DataOutputStream για την εξαγωγή δεδομένων προς τον διακομιστή
        DataOutputStream outToServer = new DataOutputStream(
            clientSocket.getOutputStream());

        // Δημιουργούμε ένα αντικείμενο BufferedReader για την είσοδο από τον διακομιστή
        BufferedReader inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

        // Διαβάζουμε την είσοδο από τον χρήστη
        sentence = inFromUser.readLine();

        // Στέλνουμε τα δεδομένα προς τον διακομιστή
        outToServer.writeBytes(sentence + '\n');

        // Διαβάζουμε την απάντηση από τον διακομιστή
        modifiedSentence = inFromServer.readLine();

        // Εκτυπώνουμε την απάντηση από τον διακομιστή
        System.out.println("FROM SERVER: " + modifiedSentence);

        // Κλείνουμε το socket
        clientSocket.close();
    }
}