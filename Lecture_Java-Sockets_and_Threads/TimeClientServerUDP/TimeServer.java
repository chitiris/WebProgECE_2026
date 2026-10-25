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
            // Π.χ. αν η θύρα 8505 χρησιμοποιείται ήδη από άλλο πρόγραμμα.
            // Χωρίς socket ο server δεν μπορεί να λειτουργήσει, οπότε τερματίζουμε με return
            // (αλλιώς ο βρόχος παρακάτω θα γέμιζε την οθόνη με σφάλματα NullPointerException).
            System.out.println("Error : " +
            e.toString());
            return;
        }
        // Ο server μένει σε έναν ατέρμονο βρόχο και απαντά στα
        // αιτήματα...
        while (true) {
            // Περιμένουμε ένα αίτημα: η receive() μπλοκάρει μέχρι να φτάσει πακέτο
            try {
                // Η receive() αλλάζει το μήκος του packet στο μήκος του πακέτου που ήρθε.
                // Το επαναφέρουμε στο πλήρες μέγεθος του buffer πριν από κάθε νέα λήψη.
                packet.setLength(buf.length);
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
            //   DATE        : ημέρα του μήνα
            //   MONTH       : μήνας, μετρώντας από το 0 (Ιανουάριος = 0), γι' αυτό προσθέτουμε 1
            //   YEAR        : έτος
            //   HOUR_OF_DAY : ώρα σε 24ωρη μορφή (0-23)· το HOUR θα έδινε 12ωρη μορφή χωρίς ΠΜ/ΜΜ
            //   MINUTE, SECOND : λεπτά και δευτερόλεπτα
            // Η String.format() συνθέτει το κείμενο: το %d τυπώνει έναν ακέραιο και το %02d έναν
            // ακέραιο με τουλάχιστον 2 ψηφία, βάζοντας 0 μπροστά (π.χ. 5 → 05).
            // Αποτέλεσμα, π.χ.: 24/10/2026-20:05:09
            String localtime = String.format("%d/%d/%d-%02d:%02d:%02d",
                    rightNow.get(Calendar.DATE), rightNow.get(Calendar.MONTH) + 1, rightNow.get(Calendar.YEAR),
                    rightNow.get(Calendar.HOUR_OF_DAY), rightNow.get(Calendar.MINUTE), rightNow.get(Calendar.SECOND));

            // Άλλη εναλλακτική μορφή, με την πλήρη ημερομηνία από τη getTime():
            //String localtime ="The Current Date is:" + rightNow.getTime()

            // Μετατρέπουμε το κείμενο localtime σε πίνακα bytes, γιατί ένα datagram μεταφέρει
            // μόνο bytes. Χρησιμοποιούμε ξεχωριστό πίνακα (sendBuf), ώστε ο buffer λήψης buf
            // να μείνει άθικτος για το επόμενο αίτημα.
            byte[] sendBuf = localtime.getBytes();
            // Παλαιότερος (deprecated) τρόπος αντιγραφής των χαρακτήρων στον buffer:
            //localtime.getBytes(0, localtime.length(), buf, 0);
            // Κατασκευάζουμε ξεχωριστό πακέτο απάντησης (reply): δεδομένα, μήκος, και ως προορισμό
            // τη διεύθυνση IP και τη θύρα του πελάτη που έστειλε το αίτημα.
            // Το packet της λήψης μένει ίδιο και ξαναχρησιμοποιείται στην επόμενη επανάληψη.
            try {
                DatagramPacket reply = new DatagramPacket(sendBuf,
                sendBuf.length, cl, port);
                // Το στέλνουμε!
                s.send(reply);
            } catch (Exception e) {
                System.out.println("Error : " +
                e.toString());
            }
            System.out.println("Client served...");
        }
    }
}