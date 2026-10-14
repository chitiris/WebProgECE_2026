import java.io.*;
import java.net.*;

public class EchoClient {
    public static void echoclient(DataInputStream sin, 
            DataOutputStream sout) throws IOException
{
    DataInputStream in = new DataInputStream(System.in);
    PrintStream out = new PrintStream(sout);
    String line;
    while(true) {
        line = "";
        // read keyboard input and write to TCP socket
        try {
            line = in.readLine();
            out.println(line);
        } catch(IOException e) {
            System.out.println(e.getMessage());
        }
        // read TCP socket and write to terminal...
        try {
            line = sin.readLine();
            System.out.println(line);
        } catch(IOException e) {
            System.out.println(e.getMessage());
        }
    }
}    // End of echoclient() function...
public static void main(String[] args) 
{
    Socket s = null;
    try {
        // Create the socket to communicate with "echo"
        // on the specified host
        String hostname = "127.0.0.1";
        s = new Socket(hostname, 8205);
        // Create streams for reading and writing lines
        // of text from and to this socket
        DataInputStream sin = 
            new DataInputStream(s.getInputStream());
        DataOutputStream sout = 
            new DataOutputStream(s.getOutputStream());
        // Tell the user that we"ve connected
        System.out.println("Connected to " + 
            s.getInetAddress() + ":" + s.getPort());
        // Use the echo feature...
        echoclient(sin, sout);
    } catch(IOException e) {
        System.out.println(e);
    }
    // Always be sure to close the socket...
    finally {
        try {
            if (s != null) 
                s.close();
        } catch(IOException e) {
            System.out.println("Closing socket...");
        }
    }
}         // End of main()
}        // End of class


/*
 * Κάντε δεξί κλικ στο αρχείο που περιέχει τον κώδικά σας (στην περίπτωσή σας, το αρχείο EchoClient.java) 
 * στο παράθυρο project Explorer ή Package Explorer.
 * Επιλέξτε "Run As" (Εκτέλεση ως) και μετά "Java Application" (Εφαρμογή Java).
 * Ανοίγει ένα παράθυρο που σάς ζητά να επιλέξετε τον κύριο (main) κλάση που θα εκτελεστεί. 
 * Επιλέξτε την κύριο κλάση του προγράμματός σας (στην περίπτωσή σας, η κύρια κλάση είναι η EchoClient).
 * Κάντε κλικ στο "Run" για να ξεκινήσετε το πρόγραμμα.
 * Όταν το πρόγραμμά σας εκτελείται, προσθέστε τα απαιτούμενα εκτελέσιμα όρισματα στο παράθυρο 
 * "Run Configuration" (Διαμόρφωση Εκτέλεσης) στο πεδίο "Program arguments" (Ορίσματα προγράμματος). 
 * Εδώ μπορείτε να προσθέσετε τη διεύθυνση που θέλετε να περάσετε στο πρόγραμμά σας.
 * Κάντε κλικ στο "Run" για να εκτελέσετε το πρόγραμμά σας με τα συγκεκριμένα εκτελέσιμα όρισματα. */