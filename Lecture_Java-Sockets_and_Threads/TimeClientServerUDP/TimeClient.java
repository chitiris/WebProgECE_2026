/*
 * TimeClient.java — Πελάτης (client) υπηρεσίας ώρας μέσω UDP.
 *
 * Τι κάνει:
 *   Στέλνει ένα datagram-αίτημα στον TimeServer (127.0.0.1, θύρα 8505), περιμένει την απάντηση
 *   και τυπώνει την ημερομηνία/ώρα του server. Το περιεχόμενο του αιτήματος δεν έχει σημασία:
 *   ο server απαντά σε οποιοδήποτε πακέτο λάβει.
 *
 * Εκτέλεση (πρώτα ο server, μετά ο client, σε ξεχωριστά τερματικά):
 *   javac *.java
 *   java TimeServer
 *   java TimeClient
 *
 * Βασικές έννοιες: DatagramSocket, DatagramPacket, request/response πάνω από UDP,
 * μετατροπή byte[] σε String.
 */
import java.io.*;   // Γενικές κλάσεις εισόδου/εξόδου
import java.net.*;  // DatagramSocket, DatagramPacket, InetAddress, εξαιρέσεις δικτύου

public class TimeClient {

  /*
   * Στέλνει το αίτημα και τυπώνει την απάντηση του server.
   *   serversoc   : το DatagramSocket μέσω του οποίου επικοινωνούμε (παρά το όνομα,
   *                 είναι το socket του client, όχι του server)
   *   hostaddress : η διεύθυνση IP του server
   *   port        : η θύρα του server
   */
  public static void timeclient(DatagramSocket serversoc, InetAddress hostaddress, int port) {
  // Δηλώνουμε ένα πακέτο datagram...
  DatagramPacket packet;
  // Δηλώνουμε έναν πίνακα bytes (256 θέσεις) που χρησιμοποιείται και για την αποστολή
  // του αιτήματος και για τη λήψη της απάντησης. Στη Java ένας νέος πίνακας byte[]
  // γεμίζει αρχικά με μηδενικά (0).
  byte[] message = new byte[256];
  try {
      // Κατασκευάζουμε το πακέτο με όλες τις πληροφορίες που χρειάζονται:
      // δεδομένα, μήκος, διεύθυνση IP και θύρα του παραλήπτη (server).
      // Στο UDP δεν υπάρχει σύνδεση, οπότε ο προορισμός γράφεται σε κάθε πακέτο.
      packet = new DatagramPacket(message, 256, hostaddress, port);
      System.out.println("Sending the request for the time...");
      // Το socket είναι ήδη ανοιχτό (δημιουργήθηκε στη main), οπότε στέλνουμε απευθείας το αίτημα
      serversoc.send(packet);
      // Περιμένουμε την απάντηση. Η receive() μπλοκάρει μέχρι να φτάσει πακέτο και
      // γράφει τα δεδομένα του στον πίνακα message, πάνω στο ίδιο αντικείμενο packet.
      // Επίσης ενημερώνει τη διεύθυνση και τη θύρα του packet με αυτές του αποστολέα.
      System.out.println("Waiting for reply...");
      serversoc.receive(packet);
     // Παλαιότερος τρόπος μετατροπής bytes σε String (ο κατασκευαστής αυτός είναι deprecated):
     // String mesg = new String(packet.getData(), 0);
      // Μετατρέπουμε τα bytes της απάντησης σε String με τη βοηθητική μέθοδο data() (βλ. παρακάτω)
      String mesg = data(packet.getData()).toString();

      // Τυπώνουμε την ώρα του server, μαζί με τη διεύθυνση IP και τη θύρα από τις οποίες ήρθε
      System.out.println("Time at server location "
                  + packet.getAddress() + ":"
                  + packet.getPort() + " is " + mesg );
  } catch(Exception exc) {
      System.out.println("Error : " +
      exc.toString());
  }
} // Τέλος της timeclient()

  /*
   * Βοηθητική μέθοδος: μετατρέπει έναν πίνακα bytes σε κείμενο (StringBuilder).
   * Διαβάζει έναν-έναν τους χαρακτήρες μέχρι να συναντήσει το πρώτο byte με τιμή 0,
   * το οποίο θεωρείται τέλος του κειμένου. Αυτό λειτουργεί επειδή η απάντηση του server
   * είναι μικρότερη από 256 bytes, άρα στον buffer μένουν μηδενικά μετά το κείμενο.
   * (Αν ο buffer ήταν γεμάτος χωρίς κανένα 0, ο βρόχος θα έβγαινε εκτός ορίων του πίνακα.)
   * Η μετατροπή (char) a[i] είναι σωστή μόνο για χαρακτήρες ASCII, όπως οι αριθμοί της ώρας.
   */
  public static StringBuilder data(byte[] a)
  {
      if (a == null)
          return null;
      StringBuilder ret = new StringBuilder();
      int i = 0;
      while (a[i] != 0)
      {
          ret.append((char) a[i]);
          i++;
      }
      return ret;
  }

  public static void main(String args[]) {
  // Η διεύθυνση και η θύρα του time server ορίζονται σταθερά παρακάτω
  // (θα μπορούσαν εναλλακτικά να δίνονται από τη γραμμή εντολών, μέσω του args)
  InetAddress hostAddress;
  int portnum;
  // Δηλώνουμε ένα socket datagram (UDP)
  DatagramSocket serversoc;

  // Αρχικοποίηση του δικτύου
  try {
      // Πληροφορίες δικτύου: η getByName() μετατρέπει ένα όνομα ή μια IP σε αντικείμενο InetAddress.
      // Το 127.0.0.1 (localhost) σημαίνει ότι ο server τρέχει στον ίδιο υπολογιστή.
      hostAddress = InetAddress.getByName("127.0.0.1");
      portnum = 8505;
      // Δημιουργούμε DatagramSocket χωρίς θύρα: το λειτουργικό σύστημα δίνει αυτόματα μια
      // ελεύθερη θύρα στον client. Ο server θα μάθει αυτή τη θύρα από το πακέτο που θα λάβει.
      serversoc = new DatagramSocket();
      timeclient(serversoc, hostAddress, portnum);
      // Μην ξεχνάτε να απελευθερώνετε τους πόρους
      // κλείνοντας το socket datagram.
      if (serversoc != null)
    	  serversoc.close();
  } catch (UnknownHostException uhe) {
      // Η διεύθυνση του server δεν μπόρεσε να βρεθεί
      System.out.println("Unknown host : " +    uhe.toString());
  } catch (SocketException exc) {
      // Το socket δεν μπόρεσε να δημιουργηθεί
      System.out.println("Error : " + exc);
  }
}         // Τέλος της main()



}