/*
 * TimeServer.java — Διακομιστής (server) υπηρεσίας ώρας μέσω UDP.
 *
 * Τι κάνει:
 *   Ακούει στη θύρα 8505. Όταν λάβει οποιοδήποτε datagram, απαντά στον αποστολέα με την
 *   τρέχουσα ημερομηνία και ώρα του server, σε μορφή  ημέρα/μήνας/έτος-ώρα:λεπτά:δευτερόλεπτα.
 *   Επειδή το UDP δεν έχει συνδέσεις, ο server μαθαίνει πού να απαντήσει (IP και θύρα του
 *   πελάτη) από το ίδιο το πακέτο που λαμβάνει.
 *
 * Εκτέλεση: javac *.java  και μετά  java TimeServer  (ο πελάτης είναι ο TimeClient)
 *
 * Βασικές έννοιες: DatagramSocket, DatagramPacket, receive()/send(), Calendar, String σε byte[].
 */
import java.io.*;   // Γενικές κλάσεις εισόδου/εξόδου
import java.net.*;  // DatagramSocket, DatagramPacket, InetAddress
import java.util.*; // Για τις συναρτήσεις ημερομηνίας/ώρας (Calendar)

public class TimeServer {
    public static void main(String args[]) {
        // Το socket datagram (UDP) για είσοδο/έξοδο
        DatagramSocket s = null;
        // Το πακέτο που χρησιμοποιούμε για τη λήψη του αιτήματος και την αποστολή
        // της απάντησης
        DatagramPacket packet = null;
        // Δημιουργούμε έναν buffer λήψης 256 bytes
        byte[] buf = new byte[256];
        // Δημιουργούμε το πακέτο λήψης του αιτήματος. Για τη λήψη χρειάζεται μόνο
        // ο buffer και το μέγεθός του· η διεύθυνση του αποστολέα θα συμπληρωθεί από τη receive().
        packet = new DatagramPacket(buf, buf.length);
        // Δημιουργούμε το socket που ακούει στη θύρα 8505
        try {
            s = new DatagramSocket(8505);
            // Βοηθητικά μηνύματα: ο server ξεκίνησε, σε ποια θύρα ακούει και πώς σταματά
            System.out.println("Time Server is ON");
            System.out.println("Listening on port 8505. Waiting for time requests... (press Ctrl+C to stop)");
        } catch(Exception e) {
            // Π.χ. αν η θύρα 8505 χρησιμοποιείται ήδη από άλλο πρόγραμμα
            System.out.println("Error : " +
            e.toString());
        }
        // Ο server μένει σε έναν ατέρμονο βρόχο και απαντά στα
        // αιτήματα...
        while (true) {
            // Περιμένουμε ένα αίτημα: η receive() μπλοκάρει μέχρι να φτάσει πακέτο
            try {
                s.receive(packet);
            } catch (Exception e) {
                System.out.println("Error : " +
                e.toString());
            }
            // Παίρνουμε τα στοιχεία του πελάτη (διεύθυνση IP και θύρα) από το πακέτο που ήρθε.
            // Αυτά τα χρειαζόμαστε για να ξέρουμε πού θα στείλουμε την απάντηση.
            InetAddress cl = packet.getAddress();
            int port = packet.getPort();
            System.out.println("Client from " + cl.getHostAddress()
                + ":" + port + " requested the time.");
            // Κατασκευάζουμε την απάντηση και τη στέλνουμε πίσω...
            // Εναλλακτικά, η ώρα θα μπορούσε να πάρει τη μορφή της Date.toString():
            //String localtime = new Date().toString();
            // Η Calendar.getInstance() επιστρέφει την τρέχουσα ημερομηνία/ώρα του συστήματος
            Calendar rightNow = Calendar.getInstance();

            // Συνθέτουμε το κείμενο της απάντησης από τα πεδία του Calendar:
            //   DATE   : ημέρα του μήνα
            //   MONTH  : μήνας, μετρώντας από το 0 (Ιανουάριος = 0), γι' αυτό προσθέτουμε 1
            //   YEAR   : έτος
            //   HOUR   : ώρα σε 12ωρη μορφή (0-11)· για 24ωρη μορφή υπάρχει το HOUR_OF_DAY
            //   MINUTE, SECOND : λεπτά και δευτερόλεπτα (χωρίς μηδενικό μπροστά, π.χ. 5 αντί για 05)
            String localtime =""+rightNow.get(Calendar.DATE)+"/"+(rightNow.get(Calendar.MONTH)+1)+"/"+ rightNow.get(Calendar.YEAR)+ "-"+rightNow.get(Calendar.HOUR )+":"+rightNow.get(Calendar.MINUTE)+":"+ rightNow.get(Calendar.SECOND);

            // Άλλη εναλλακτική μορφή, με την πλήρη ημερομηνία από τη getTime():
            //String localtime ="The Current Date is:" + rightNow.getTime()

            // Μετατρέπουμε το κείμενο localtime σε πίνακα bytes,
            // γιατί ένα datagram μεταφέρει μόνο bytes
            buf = localtime.getBytes();
            // Παλαιότερος (deprecated) τρόπος αντιγραφής των χαρακτήρων στον buffer:
            //localtime.getBytes(0, localtime.length(), buf, 0);
            // Κατασκευάζουμε το πακέτο απάντησης: δεδομένα, μήκος, και ως προορισμό
            // τη διεύθυνση IP και τη θύρα του πελάτη που έστειλε το αίτημα
            try {
                packet = new DatagramPacket(buf,
                buf.length, cl, port);
                // Το στέλνουμε!
                s.send(packet);
            } catch (Exception e) {
                System.out.println("Error : " +
                e.toString());
            }
            System.out.println("Client served...");
        }
    }
}