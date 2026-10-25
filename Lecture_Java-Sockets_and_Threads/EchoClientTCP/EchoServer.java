/*
 * EchoServer.java — Πολυνηματικός (multithreaded) Echo Server μέσω TCP.
 *
 * Τι κάνει:
 *   Ακούει στη θύρα 8205. Για ΚΑΘΕ νέα σύνδεση πελάτη δημιουργεί ένα νέο νήμα (EchoThread),
 *   ώστε να εξυπηρετεί πολλούς πελάτες ταυτόχρονα (concurrent server).
 *   Κάθε γραμμή που λαμβάνει την επιστρέφει στον πελάτη ως "Received: <κείμενο>".
 *   Όταν ο πελάτης στείλει "quit", το νήμα του τερματίζει και κλείνει τη σύνδεση.
 *
 * Σύγκριση με τον TCPServer (φάκελος tcp_sockets):
 *   Ο TCPServer είναι επαναληπτικός (iterative): εξυπηρετεί έναν πελάτη τη φορά.
 *   Εδώ το κύριο νήμα κάνει μόνο accept() και αναθέτει κάθε πελάτη σε ξεχωριστό νήμα,
 *   οπότε ένας αργός πελάτης δεν καθυστερεί τους υπόλοιπους.
 *
 * Εκτέλεση: javac *.java  και μετά  java EchoServer  (ο πελάτης είναι ο EchoClient)
 *
 * Βασικές έννοιες: ServerSocket, accept(), νήμα ανά πελάτη, extends Thread, try/finally.
 */
import java.net.*;   // ServerSocket, Socket
import java.io.*;    // DataInputStream, PrintStream, IOException
import java.lang.*;  // Εισάγεται πάντα αυτόματα στη Java, άρα αυτό το import δεν χρειάζεται
import java.util.*;  // Δεν χρησιμοποιείται κάποια κλάση του σε αυτό το αρχείο

public class EchoServer {
    public static void main(String args[]) {
        // Αρχικοποίηση της δικτυακής σύνδεσης
        try {
            // Δημιουργούμε το "socket υποδοχής" (ServerSocket) που ακούει στη θύρα 8205.
            // Αυτό δεν χρησιμοποιείται για ανταλλαγή δεδομένων, μόνο για την αποδοχή νέων συνδέσεων.
            ServerSocket serversoc = new ServerSocket(8205);

            // Ο server μένει σε έναν ατέρμονο βρόχο και περιμένει αιτήσεις σύνδεσης...
            while (true) {
                // Η accept() μπλοκάρει μέχρι να συνδεθεί ένας πελάτης. Τότε επιστρέφει
                // ένα ΝΕΟ Socket, αφιερωμένο αποκλειστικά στην επικοινωνία με αυτόν τον πελάτη.
                Socket incoming = serversoc.accept();

                // Δημιουργούμε ένα νέο νήμα (θυγατρικό) για να εξυπηρετήσει τον πελάτη
                // και το ξεκινάμε με start(). Η start() δημιουργεί νέο νήμα εκτέλεσης που
                // καλεί τη run()· το κύριο νήμα επιστρέφει αμέσως στην accept() για τον επόμενο πελάτη.
                EchoThread et = new EchoThread(incoming);
                et.start();
            }
        } catch (IOException e) {
            // Π.χ. αν η θύρα 8205 χρησιμοποιείται ήδη από άλλο πρόγραμμα
            System.out.println("Error : " + e.getMessage());
        }
    }        // Τέλος της main
}        // Τέλος της κλάσης EchoServer

// Η κλάση EchoThread επεκτείνει (extends) την Thread: κάθε αντικείμενό της είναι ένα νήμα
// που εξυπηρετεί έναν πελάτη. Ο κώδικας του νήματος βρίσκεται στη μέθοδο run().
class EchoThread extends Thread {
    // Το socket της σύνδεσης με τον συγκεκριμένο πελάτη (από εδώ διαβάζουμε και εδώ γράφουμε)
    Socket s;

    // Ο κατασκευαστής (constructor): αποθηκεύει το socket του πελάτη για να το χρησιμοποιήσει η run()
    EchoThread(Socket s) {
        this.s = s;
    }

    // Η μέθοδος run() περιέχει τον κώδικα που εκτελεί το νήμα όταν κληθεί η start()
    public void run() {
        // Σημαία (flag) που γίνεται true όταν ο πελάτης στείλει "quit"
        boolean finished = false;

        try {
            // Ροή εισόδου: ό,τι στέλνει ο πελάτης μέσω του socket
            DataInputStream in = new DataInputStream(s.getInputStream());
            // Ροή εξόδου: ό,τι στέλνουμε στον πελάτη (PrintStream για εύκολη αποστολή γραμμών)
            PrintStream out = new PrintStream(s.getOutputStream());

            // Τυπώνουμε στην οθόνη του server από ποια διεύθυνση IP και θύρα συνδέθηκε ο πελάτης
            System.out.println("Client from : " + s.getInetAddress() + " port " + s.getPort());

            // Επαναλαμβάνουμε μέχρι ο πελάτης να στείλει "quit"
            while (!finished) {
                // Διαβάζουμε μια γραμμή από τον πελάτη (μπλοκάρει μέχρι να φτάσει '\n').
                // Σημείωση: αν ο πελάτης κλείσει απότομα τη σύνδεση, η readLine() επιστρέφει null.
                String st = in.readLine();

                // Στέλνουμε την ίδια γραμμή πίσω στον πελάτη, με το πρόθεμα "Received: "
                out.print("Received: ");
                out.println(st);

                // Τυπώνουμε το ίδιο μήνυμα και στην οθόνη του server
                System.out.println("Received:");
                System.out.println(st);

                // Αν ο πελάτης έστειλε "quit", ορίζουμε finished = true ώστε να βγούμε από τον βρόχο
                if (st.equals("quit")) {
                    finished = true;
                    System.out.println("Thread exiting...");
                }
            }
        } catch (IOException e) {
            System.out.println("Error : " + e.getMessage());
        }

        // Το finally εκτελείται πάντα: κλείνουμε το socket του πελάτη για να ελευθερωθούν οι πόροι.
        // Μετά το τέλος της run() το νήμα τερματίζει.
        finally {
            try {
                if (s != null)
                    s.close();
            } catch (Exception e) {
                System.out.println("Error : " + e.getMessage());
            }
        }
    }        // Τέλος της run
}        // Τέλος της κλάσης EchoThread