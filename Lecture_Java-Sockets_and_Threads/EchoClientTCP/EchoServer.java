import java.net.*;
import java.io.*;
import java.lang.*;
import java.util.*;

public class EchoServer {
    public static void main(String args[]) {
        // Αρχικοποίηση της δικτυακής σύνδεσης
        try {
            ServerSocket serversoc = new ServerSocket(8205);
            
            // Κάθεται σε έναν ατέλειωτο βρόχο και περιμένει αιτήσεις...
            while (true) {
                // Δέχεται το μήνυμα
                Socket incoming = serversoc.accept();
                
                // Δημιουργεί ένα παιδί νήμα για να εξυπηρετήσει το αίτημα
                EchoThread et = new EchoThread(incoming);
                et.start();
            }
        } catch (IOException e) {
            System.out.println("Error : " + e.getMessage());
        }
    }        // Τέλος της main
}        // Τέλος της κλάσης EchoServer

// Υποκλάση EchoThread για τη δημιουργία νημάτων
class EchoThread extends Thread {
    // Το socket προς το οποίο γράφουμε
    Socket s;
    
    // Ο κατασκευαστής μας
    EchoThread(Socket s) {
        this.s = s;
    }
    
    // Η μέθοδος run του νήματος...
    public void run() {
        boolean finished = false;
        
        try {
            // Λαμβάνουμε είσοδο από το socket
            DataInputStream in = new DataInputStream(s.getInputStream());
            PrintStream out = new PrintStream(s.getOutputStream());
            
            // Εκτυπώνουμε ένα μήνυμα:
            System.out.println("Client from : " + s.getInetAddress() + " port " + s.getPort());
            
            // Λαμβάνουμε την είσοδο από το socket...
            while (!finished) {
                String st = in.readLine();
                
                // Στείλτε την ίδια είσοδο πίσω στον πελάτη
                out.print("Received: ");
                out.println(st);
                
                // Εκτυπώνουμε το ίδιο στην οθόνη επίσης
                System.out.println("Received:");
                System.out.println(st);
                
                // Εάν η είσοδος ήταν "quit" τότε τερματίστε...
                if (st.equals("quit")) {
                    finished = true;
                    System.out.println("Thread exiting...");
                }
            }
        } catch (IOException e) {
            System.out.println("Error : " + e.getMessage());
        }
        
        // Βεβαιωθείτε πάντα ότι κλείνετε το socket...
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