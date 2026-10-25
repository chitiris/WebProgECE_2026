/*
 * UDPServer.java — Απλός διακομιστής (server) UDP.
 *
 * Τι κάνει:
 *   Ακούει στη θύρα 9876. Για κάθε datagram που λαμβάνει, μετατρέπει το κείμενο σε κεφαλαία
 *   γράμματα και το στέλνει πίσω στη διεύθυνση IP και στη θύρα του αποστολέα.
 *   Στο UDP δεν υπάρχει accept() ούτε ξεχωριστό socket ανά πελάτη: ένα μόνο DatagramSocket
 *   δέχεται πακέτα από όλους τους πελάτες.
 *
 * Εκτέλεση: javac *.java  και μετά  java UDPServer  (ο πελάτης είναι ο UDPClient)
 *
 * Βασισμένο στο παράδειγμα του βιβλίου Kurose & Ross, "Computer Networking: A Top-Down Approach".
 * Βασικές έννοιες: DatagramSocket, DatagramPacket, receive()/send(), διεύθυνση αποστολέα.
 */
import java.io.*;   // Γενικές κλάσεις εισόδου/εξόδου
import java.net.*;  // DatagramSocket, DatagramPacket, InetAddress

public class UDPServer {
    public static void main(String args[]) throws Exception {
        // Δημιουργούμε DatagramSocket που ακούει σε μια συγκεκριμένη θύρα (9876).
        // Ο server χρειάζεται σταθερή, γνωστή θύρα, ώστε να ξέρουν οι πελάτες πού να στέλνουν.
        DatagramSocket serverSocket = new DatagramSocket(9876);

        // Μήνυμα που δείχνει ότι ο UDP server είναι ενεργός
        System.out.println("UDP Server is ON");

        // Ατέρμονος βρόχος: ο server εξυπηρετεί το ένα πακέτο μετά το άλλο
        while (true) {
            // Δημιουργούμε νέους πίνακες bytes για λήψη και αποστολή δεδομένων σε κάθε επανάληψη,
            // ώστε να μη μείνουν υπολείμματα από προηγούμενο, μεγαλύτερο μήνυμα
            byte[] receiveData = new byte[1024];
            byte[] sendData = new byte[1024];

            // Δημιουργούμε DatagramPacket για τη λήψη δεδομένων από τους πελάτες
            DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);

            // Περιμένουμε να λάβουμε ένα πακέτο από κάποιον πελάτη (η receive() μπλοκάρει)
            serverSocket.receive(receivePacket);

            // Μετατρέπουμε τα δεδομένα που λάβαμε σε συμβολοσειρά (String).
            // Η getData() επιστρέφει όλο τον buffer των 1024 bytes, άρα το String περιέχει και τα
            // μηδενικά bytes μετά το κείμενο (το πραγματικό μήκος δίνει η receivePacket.getLength()).
            String sentence = new String(receivePacket.getData());
            // Από το ίδιο το πακέτο μαθαίνουμε τη διεύθυνση IP και τη θύρα του πελάτη,
            // για να ξέρουμε πού να στείλουμε την απάντηση
            InetAddress IPAddress = receivePacket.getAddress();
            int port = receivePacket.getPort();

            // Μετατρέπουμε τη συμβολοσειρά σε κεφαλαία γράμματα και την τυπώνουμε
            String capitalizedSentence = sentence.toUpperCase();
            System.out.println(capitalizedSentence);

            // Μετατρέπουμε τη συμβολοσειρά με τα κεφαλαία σε bytes για αποστολή
            sendData = capitalizedSentence.getBytes();

            // Δημιουργούμε DatagramPacket για την αποστολή: δεδομένα, μήκος, και ως προορισμό
            // τη διεύθυνση IP και τη θύρα του πελάτη που έστειλε το αίτημα
            DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, port);

            // Στέλνουμε το πακέτο στον πελάτη
            serverSocket.send(sendPacket);
        }
    }
}