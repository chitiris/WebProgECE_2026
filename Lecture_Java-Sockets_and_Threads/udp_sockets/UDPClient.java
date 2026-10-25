/*
 * UDPClient.java — Απλός πελάτης (client) UDP.
 *
 * Τι κάνει:
 *   Διαβάζει μία γραμμή κειμένου από τον χρήστη, τη στέλνει σε ένα datagram στον UDPServer
 *   (localhost, θύρα 9876) και τυπώνει την απάντηση (το ίδιο κείμενο με κεφαλαία γράμματα).
 *   Σε αντίθεση με το TCP, δεν γίνεται σύνδεση: κάθε πακέτο στέλνεται αυτόνομα, με τη
 *   διεύθυνση και τη θύρα του παραλήπτη γραμμένες μέσα του.
 *
 * Εκτέλεση (πρώτα ο server, μετά ο client, σε ξεχωριστά τερματικά):
 *   javac *.java
 *   java UDPServer
 *   java UDPClient
 *
 * Βασισμένο στο παράδειγμα του βιβλίου Kurose & Ross, "Computer Networking: A Top-Down Approach".
 * Βασικές έννοιες: DatagramSocket, DatagramPacket, InetAddress, send()/receive(), byte[].
 */
import java.io.*;   // BufferedReader, InputStreamReader
import java.net.*;  // DatagramSocket, DatagramPacket, InetAddress

public class UDPClient {
    // Η main δηλώνει "throws Exception" για απλότητα: οι εξαιρέσεις δεν χειρίζονται εδώ
    public static void main(String args[]) throws Exception {
        // Δημιουργούμε αντικείμενο BufferedReader για ανάγνωση εισόδου από τον χρήστη (πληκτρολόγιο)
        BufferedReader inFromUser = new BufferedReader(new InputStreamReader(System.in));

        // Δημιουργούμε DatagramSocket για επικοινωνία μέσω UDP. Δεν δίνουμε θύρα, οπότε το
        // λειτουργικό σύστημα επιλέγει αυτόματα μια ελεύθερη θύρα για τον client.
        // Σε αντίθεση με το TCP, η δημιουργία του socket ΔΕΝ κάνει σύνδεση με τον server.
        DatagramSocket clientSocket = new DatagramSocket();

        // Μετατρέπουμε το όνομα του διακομιστή (εδώ "localhost") σε διεύθυνση IP (αντικείμενο InetAddress).
        // Για server σε άλλο υπολογιστή θα γράφαμε εδώ το όνομα ή την IP του.
        InetAddress IPAddress = InetAddress.getByName("localhost");

        // Δημιουργούμε πίνακες bytes για την αποστολή και τη λήψη δεδομένων (1024 bytes ο καθένας).
        // Το UDP μεταφέρει μόνο bytes, οπότε το κείμενο πρέπει να μετατραπεί σε byte[].
        byte[] sendData = new byte[1024];
        byte[] receiveData = new byte[1024];



        // Βοηθητικό μήνυμα: λέει στον χρήστη ότι το πρόγραμμα περιμένει να πληκτρολογήσει κάτι
        System.out.println("Type a sentence in lowercase and press Enter:");
        // Διαβάζουμε μία γραμμή κειμένου από τον χρήστη
        String sentence = inFromUser.readLine();

        // Μετατρέπουμε το κείμενο σε bytes. Η getBytes() επιστρέφει νέο πίνακα με ακριβώς
        // όσα bytes χρειάζεται το κείμενο, ο οποίος αντικαθιστά τον αρχικό πίνακα των 1024 bytes.
        sendData = sentence.getBytes();

        // Δημιουργούμε DatagramPacket για την αποστολή: περιέχει τα δεδομένα, το μήκος τους,
        // και τη διεύθυνση IP και τη θύρα (9876) του διακομιστή στον οποίο θα σταλεί
        DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, 9876);

        // Στέλνουμε το πακέτο στον διακομιστή. Το UDP δεν εγγυάται ότι θα φτάσει.
        clientSocket.send(sendPacket);
        // Βοηθητικό μήνυμα: το πακέτο στάλθηκε και ο client περιμένει απάντηση
        System.out.println("Sent to UDP server localhost:9876. Waiting for reply...");

        // Δημιουργούμε DatagramPacket για τη λήψη της απάντησης από τον διακομιστή
        DatagramPacket receivePacket1 = new DatagramPacket(receiveData, receiveData.length);

        // Περιμένουμε να λάβουμε ένα πακέτο από τον διακομιστή. Η receive() μπλοκάρει μέχρι
        // να φτάσει πακέτο (αν χαθεί η απάντηση, ο client θα περιμένει για πάντα).
        clientSocket.receive(receivePacket1);

        // Μετατρέπουμε τα δεδομένα που λάβαμε σε String.
        // Η getData() επιστρέφει ΟΛΟ τον buffer των 1024 bytes, ενώ η getLength() δίνει πόσα bytes
        // ήρθαν πραγματικά. Μετατρέπουμε μόνο αυτά, ώστε να μη μπουν στο String τα αχρησιμοποίητα
        // μηδενικά bytes μετά το κείμενο.
        String modifiedSentence = new String(receivePacket1.getData(), 0, receivePacket1.getLength());

        // Τυπώνουμε την απάντηση του διακομιστή
        System.out.println("FROM SERVER:" + modifiedSentence);

        // Κλείνουμε το DatagramSocket και ελευθερώνουμε τη θύρα
        clientSocket.close();
}
}