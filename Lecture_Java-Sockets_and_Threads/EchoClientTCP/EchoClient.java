/*
 * EchoClient.java — Πελάτης (client) για τον πολυνηματικό Echo Server μέσω TCP.
 *
 * Τι κάνει:
 *   Συνδέεται στον EchoServer (θύρα 8205) και επαναλαμβάνει συνεχώς τα εξής:
 *     1. διαβάζει μια γραμμή από το πληκτρολόγιο,
 *     2. τη στέλνει στον server μέσω του TCP socket,
 *     3. διαβάζει την απάντηση του server ("Received: <κείμενο>") και την τυπώνει.
 *   Αν σταλεί η λέξη "quit", ο server τερματίζει τη συνεδρία και ο client τελειώνει κι αυτός.
 *   Ο client τελειώνει επίσης αν ο server κλείσει τη σύνδεση.
 *
 * Εκτέλεση (πρώτα ο server, μετά ο client, σε ξεχωριστά τερματικά):
 *   javac *.java
 *   java EchoServer
 *   java EchoClient
 *   Ανοίξτε πολλούς EchoClient ταυτόχρονα για να δείτε ότι ο server εξυπηρετεί
 *   κάθε πελάτη σε δικό του νήμα.
 *
 * Βασικές έννοιες: Socket, ροές εισόδου/εξόδου (streams) πάνω από TCP, try/catch/finally.
 */
import java.io.*;   // Κλάσεις εισόδου/εξόδου: DataInputStream, DataOutputStream, PrintStream, IOException
import java.net.*;  // Κλάσεις δικτύου: Socket

public class EchoClient {
    /*
     * Η μέθοδος echoclient υλοποιεί τον κύριο βρόχο επικοινωνίας με τον server.
     *   sin  : ροή εισόδου από το socket (ό,τι στέλνει ο server)
     *   sout : ροή εξόδου προς το socket (ό,τι στέλνουμε στον server)
     */
    public static void echoclient(DataInputStream sin,
            DataOutputStream sout) throws IOException
{
    // Ροή εισόδου από το πληκτρολόγιο (System.in)
    DataInputStream in = new DataInputStream(System.in);
    // Τυλίγουμε τη ροή εξόδου του socket σε PrintStream, ώστε να μπορούμε να
    // στέλνουμε ολόκληρες γραμμές κειμένου με println() (προσθέτει αυτόματα '\n')
    PrintStream out = new PrintStream(sout);
    // Μεταβλητή για την τρέχουσα γραμμή κειμένου
    String line;
    // Βρόχος επικοινωνίας: συνεχίζει μέχρι ο χρήστης να στείλει "quit"
    // ή μέχρι ο server να κλείσει τη σύνδεση (βλ. τους ελέγχους με return παρακάτω)
    while(true) {
        line = "";
        // Βήμα 1: Διαβάζουμε μια γραμμή από το πληκτρολόγιο και τη στέλνουμε στο TCP socket.
        // Το '\n' που προσθέτει η println() είναι σημαντικό: ο server διαβάζει
        // με readLine(), δηλαδή περιμένει μέχρι να λάβει τέλος γραμμής.
        try {
            line = in.readLine();
            // Αν η είσοδος του πληκτρολογίου τελείωσε (Ctrl+Z στα Windows, Ctrl+D σε Linux/macOS),
            // η readLine() επιστρέφει null και σταματάμε
            if (line == null)
                return;
            out.println(line);
        } catch(IOException e) {
            System.out.println(e.getMessage());
        }
        // Θυμόμαστε αν ο χρήστης έστειλε "quit", για να τερματίσουμε αφού διαβάσουμε την τελευταία απάντηση
        boolean quit = "quit".equals(line);
        // Βήμα 2: Διαβάζουμε την απάντηση του server από το TCP socket και την τυπώνουμε στο τερματικό.
        // Η readLine() μπλοκάρει (περιμένει) μέχρι να φτάσει ολόκληρη γραμμή από τον server.
        // Σημείωση: η DataInputStream.readLine() είναι deprecated (παρωχημένη) στη Java·
        // στον σύγχρονο κώδικα προτιμάται η BufferedReader.readLine().
        try {
            line = sin.readLine();
            // Αν ο server έκλεισε τη σύνδεση, η readLine() επιστρέφει null (τέλος της ροής).
            // Χωρίς αυτόν τον έλεγχο ο βρόχος θα τύπωνε "null" ασταμάτητα.
            if (line == null) {
                System.out.println("Server closed the connection.");
                return;
            }
            System.out.println(line);
        } catch(IOException e) {
            System.out.println(e.getMessage());
        }
        // Αν στάλθηκε "quit", ο server τερματίζει τη συνεδρία, οπότε τελειώνουμε κι εμείς.
        // Η return επιστρέφει στη main, όπου το finally κλείνει το socket.
        if (quit) {
            System.out.println("Session ended.");
            return;
        }
    }
}    // Τέλος της μεθόδου echoclient()
public static void main(String[] args)
{
    // Το socket δηλώνεται έξω από το try, ώστε να είναι ορατό και στο finally για να το κλείσουμε
    Socket s = null;
    try {
        // Δημιουργούμε το socket για επικοινωνία με τον "echo" server
        // στον συγκεκριμένο υπολογιστή (host). Η διεύθυνση δίνεται προαιρετικά από τη γραμμή
        // εντολών (args[0]), π.χ. java EchoClient 192.168.1.10 — αν δεν δοθεί, χρησιμοποιείται
        // το 127.0.0.1 (localhost), δηλαδή ο server τρέχει στον ίδιο υπολογιστή με τον client.
        // Ο κατασκευαστής new Socket(host, port) κάνει αμέσως τη σύνδεση TCP
        // (three-way handshake)· αν ο server δεν τρέχει, πετάει εξαίρεση (Connection refused).
        String hostname = (args.length > 0) ? args[0] : "127.0.0.1";
        s = new Socket(hostname, 8205);
        // Δημιουργούμε ροές για ανάγνωση και εγγραφή γραμμών κειμένου
        // από και προς αυτό το socket
        DataInputStream sin =
            new DataInputStream(s.getInputStream());
        DataOutputStream sout =
            new DataOutputStream(s.getOutputStream());
        // Ενημερώνουμε τον χρήστη ότι συνδεθήκαμε, τυπώνοντας τη διεύθυνση IP και τη θύρα του server
        System.out.println("Connected to " +
            s.getInetAddress() + ":" + s.getPort());
        // Βοηθητικό μήνυμα: οδηγίες χρήσης για τον χρήστη
        System.out.println("Type a line and press Enter. Type quit to end the session.");
        // Ξεκινάμε την επικοινωνία με τον echo server
        echoclient(sin, sout);
    } catch(IOException e) {
        System.out.println(e);
    }
    // Το μπλοκ finally εκτελείται πάντα (με ή χωρίς εξαίρεση),
    // οπότε εδώ φροντίζουμε να κλείσουμε το socket και να ελευθερώσουμε τους πόρους
    finally {
        try {
            if (s != null)
                s.close();
        } catch(IOException e) {
            System.out.println("Closing socket...");
        }
    }
}         // Τέλος της main()
}        // Τέλος της κλάσης EchoClient
