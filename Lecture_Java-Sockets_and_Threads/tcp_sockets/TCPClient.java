/*
 * TCPClient.java — Απλός πελάτης (client) TCP.
 *
 * Τι κάνει:
 *   Συνδέεται στον TCPServer (localhost, θύρα 6789), διαβάζει μία γραμμή κειμένου από τον χρήστη,
 *   τη στέλνει στον server και τυπώνει την απάντηση (το ίδιο κείμενο με κεφαλαία γράμματα).
 *
 * Εκτέλεση (πρώτα ο server, μετά ο client, σε ξεχωριστά τερματικά):
 *   javac *.java
 *   java TCPServer
 *   java TCPClient
 *
 * Βασισμένο στο παράδειγμα του βιβλίου Kurose & Ross, "Computer Networking: A Top-Down Approach".
 * Βασικές έννοιες: Socket, connect, ροές εισόδου/εξόδου, BufferedReader, DataOutputStream.
 */
import java.io.*;   // BufferedReader, InputStreamReader, DataOutputStream
import java.net.*;  // Socket

public class TCPClient  {
    // Η main δηλώνει "throws Exception" για απλότητα: δεν χειριζόμαστε εδώ τις εξαιρέσεις,
    // οπότε αν π.χ. ο server δεν τρέχει, το πρόγραμμα τερματίζει με μήνυμα σφάλματος.
    public static void main(String argv[]) throws Exception {
        // Δηλώνουμε τις μεταβλητές που θα χρησιμοποιηθούν
        String sentence; // Η πρόταση που πληκτρολογεί ο χρήστης
        String modifiedSentence; // Η απάντηση από τον διακομιστή (η πρόταση με κεφαλαία)

        // Δημιουργούμε ένα αντικείμενο BufferedReader για την είσοδο από τον χρήστη (πληκτρολόγιο).
        // Η InputStreamReader μετατρέπει τα bytes του System.in σε χαρακτήρες και η BufferedReader
        // μας δίνει τη μέθοδο readLine() για ανάγνωση ολόκληρης γραμμής.
        BufferedReader inFromUser = new BufferedReader(
            new InputStreamReader(System.in));

        // Δημιουργούμε ένα socket και συνδεόμαστε στον διακομιστή (server).
        // Ο κατασκευαστής εκτελεί αμέσως τη σύνδεση TCP (three-way handshake) με τον
        // υπολογιστή "localhost" (127.0.0.1) στη θύρα 6789, όπου ακούει ο TCPServer.
        Socket clientSocket = new Socket("localhost", 6789);

        // Δημιουργούμε ένα αντικείμενο DataOutputStream για την αποστολή δεδομένων προς τον διακομιστή
        // (ροή εξόδου που "γράφει" μέσα στο socket)
        DataOutputStream outToServer = new DataOutputStream(
            clientSocket.getOutputStream());

        // Δημιουργούμε ένα αντικείμενο BufferedReader για την ανάγνωση της απάντησης από τον διακομιστή
        // (ροή εισόδου που "διαβάζει" από το socket)
        BufferedReader inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

        // Διαβάζουμε μία γραμμή από τον χρήστη (περιμένει μέχρι να πατηθεί Enter)
        sentence = inFromUser.readLine();

        // Στέλνουμε την πρόταση στον διακομιστή. Προσθέτουμε '\n' στο τέλος, γιατί ο server
        // διαβάζει με readLine(): χωρίς τέλος γραμμής θα περίμενε για πάντα.
        // Το TCP είναι ροή (stream) bytes χωρίς όρια μηνυμάτων, οπότε το '\n' λειτουργεί ως
        // "διαχωριστικό" που δείχνει στον server πού τελειώνει το μήνυμα.
        outToServer.writeBytes(sentence + '\n');

        // Διαβάζουμε την απάντηση από τον διακομιστή (μπλοκάρει μέχρι να φτάσει ολόκληρη γραμμή)
        modifiedSentence = inFromServer.readLine();

        // Εκτυπώνουμε την απάντηση από τον διακομιστή
        System.out.println("FROM SERVER: " + modifiedSentence);

        // Κλείνουμε το socket, τερματίζοντας τη σύνδεση TCP με τον διακομιστή
        clientSocket.close();
    }
}