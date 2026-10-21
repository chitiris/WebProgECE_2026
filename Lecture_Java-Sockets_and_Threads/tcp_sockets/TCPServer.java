import java.io.*;
import java.net.*;

public class TCPServer  {
    public static void main(String argv[]) throws Exception {
        String clientSentence;
        String capitalizedSentence;
        
        // Δημιουργία socket για την υποδοχή συνδέσεων από πελάτες
        ServerSocket welcomeSocket = new ServerSocket(6789);
        System.out.println("TCP Server is ON");

        // Αναμονή για ροές από πελάτες
        while (true) {
            Socket connectionSocket = welcomeSocket.accept();

            // Δημιουργία αντικειμένου BufferedReader για την ανάγνωση δεδομένων από τον πελάτη
            BufferedReader inFromClient = new BufferedReader(new InputStreamReader(connectionSocket.getInputStream()));

            // Δημιουργία αντικειμένου DataOutputStream για την αποστολή δεδομένων στον πελάτη
            DataOutputStream outToClient = new DataOutputStream(connectionSocket.getOutputStream());

            // Διαβάζουμε τα δεδομένα που αποστέλλονται από τον πελάτη
            clientSentence = inFromClient.readLine();

            // Μετατροπή των δεδομένων σε κεφαλαίους χαρακτήρες
            capitalizedSentence = clientSentence.toUpperCase() + '\n';

            // Εκτυπώνουμε τα δεδομένα που αποστέλλονται στον πελάτη
            System.out.println(capitalizedSentence);

            // Αποστέλλουμε τα δεδομένα στον πελάτη
            outToClient.writeBytes(capitalizedSentence);

            // Εκτυπώνουμε μήνυμα αναμονής για τον επόμενο πελάτη
            System.out.println("Waiting next client");
        }
    }
}