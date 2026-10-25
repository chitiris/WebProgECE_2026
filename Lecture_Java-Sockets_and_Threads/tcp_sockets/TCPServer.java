/*
 * TCPServer.java — Απλός επαναληπτικός (iterative) διακομιστής (server) TCP.
 *
 * Τι κάνει:
 *   Ακούει στη θύρα 6789. Για κάθε πελάτη που συνδέεται διαβάζει μία γραμμή κειμένου,
 *   τη μετατρέπει σε κεφαλαία γράμματα και την στέλνει πίσω. Μετά περιμένει τον επόμενο πελάτη.
 *   "Επαναληπτικός" σημαίνει ότι εξυπηρετεί έναν πελάτη τη φορά (για ταυτόχρονη εξυπηρέτηση
 *   πολλών πελατών με νήματα, δείτε τον EchoServer στον φάκελο EchoClientTCP).
 *
 * Εκτέλεση: javac *.java  και μετά  java TCPServer  (ο πελάτης είναι ο TCPClient)
 *
 * Βασισμένο στο παράδειγμα του βιβλίου Kurose & Ross, "Computer Networking: A Top-Down Approach".
 * Βασικές έννοιες: ServerSocket, accept(), Socket σύνδεσης, BufferedReader, DataOutputStream.
 */
import java.io.*;   // BufferedReader, InputStreamReader, DataOutputStream
import java.net.*;  // ServerSocket, Socket

public class TCPServer  {
    public static void main(String argv[]) throws Exception {
        String clientSentence; // Η πρόταση που λαμβάνουμε από τον πελάτη
        String capitalizedSentence; // Η ίδια πρόταση με κεφαλαία, που θα στείλουμε πίσω

        // Δημιουργούμε το "socket υποδοχής" (welcome socket) στη θύρα 6789.
        // Αυτό δεν ανταλλάσσει δεδομένα· χρησιμοποιείται μόνο για να δέχεται νέες συνδέσεις πελατών.
        ServerSocket welcomeSocket = new ServerSocket(6789);
        System.out.println("TCP Server is ON");
        // Βοηθητικά μηνύματα: σε ποια θύρα ακούει ο server και πώς σταματά
        System.out.println("Listening on port 6789. Waiting for a client... (press Ctrl+C to stop)");

        // Ατέρμονος βρόχος: ο server εξυπηρετεί τον έναν πελάτη μετά τον άλλον
        while (true) {
            // Η accept() μπλοκάρει μέχρι να συνδεθεί ένας πελάτης. Τότε επιστρέφει ένα ΝΕΟ socket
            // (connection socket), αφιερωμένο στην επικοινωνία με αυτόν τον συγκεκριμένο πελάτη.
            Socket connectionSocket = welcomeSocket.accept();
            // Βοηθητικό μήνυμα: από ποια διεύθυνση IP και θύρα συνδέθηκε ο πελάτης
            System.out.println("Client connected from " + connectionSocket.getInetAddress() + ":" + connectionSocket.getPort());

            // Δημιουργούμε αντικείμενο BufferedReader για την ανάγνωση δεδομένων από τον πελάτη
            BufferedReader inFromClient = new BufferedReader(new InputStreamReader(connectionSocket.getInputStream()));

            // Δημιουργούμε αντικείμενο DataOutputStream για την αποστολή δεδομένων στον πελάτη
            DataOutputStream outToClient = new DataOutputStream(connectionSocket.getOutputStream());

            // Διαβάζουμε μία γραμμή από τον πελάτη (μπλοκάρει μέχρι να φτάσει '\n')
            clientSentence = inFromClient.readLine();

            // Μετατρέπουμε την πρόταση σε κεφαλαίους χαρακτήρες. Η readLine() αφαιρεί το '\n',
            // οπότε το προσθέτουμε ξανά για να μπορέσει ο πελάτης να διαβάσει την απάντηση με readLine().
            capitalizedSentence = clientSentence.toUpperCase() + '\n';

            // Τυπώνουμε στην οθόνη του server την πρόταση που θα στείλουμε στον πελάτη
            System.out.println(capitalizedSentence);

            // Στέλνουμε την πρόταση με κεφαλαία στον πελάτη
            outToClient.writeBytes(capitalizedSentence);

            // Ενημερώνουμε ότι ο server περιμένει τον επόμενο πελάτη (επιστροφή στην accept())
            System.out.println("Waiting next client");
        }
    }
}