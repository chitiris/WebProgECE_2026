# =============================================================================
# UDPServer.py — Απλός διακομιστής (server) UDP σε Python.
#
# Τι κάνει:
#   Ακούει στη διεύθυνση 127.0.0.1, θύρα 12000. Για κάθε πακέτο που λαμβάνει, μετατρέπει το
#   μήνυμα σε κεφαλαία γράμματα και το στέλνει πίσω στη διεύθυνση του αποστολέα.
#   Στο UDP δεν υπάρχουν listen()/accept() ούτε ξεχωριστό socket ανά πελάτη: ένα μόνο socket
#   δέχεται πακέτα από όλους τους πελάτες.
#
# Εκτέλεση: python UDPServer.py  (ο πελάτης είναι το UDPClient.py)
#
# Βασισμένο στο παράδειγμα του βιβλίου Kurose & Ross, "Computer Networking: A Top-Down Approach".
# Βασικές έννοιες: SOCK_DGRAM, bind(), recvfrom()/sendto(), διεύθυνση πελάτη (IP, θύρα).
# =============================================================================

# Εισαγωγή όλων των ονομάτων του module socket (socket, AF_INET, SOCK_DGRAM κ.λπ.)
from socket import *

# Η θύρα στην οποία θα ακούει ο διακομιστής
serverPort = 12000

# Δημιουργία socket: AF_INET = διευθύνσεις IPv4, SOCK_DGRAM = πρωτόκολλο UDP
serverSocket = socket(AF_INET, SOCK_DGRAM)

# Δέσμευση (bind) του socket στη διεύθυνση "127.0.0.1" και στη θύρα 12000.
# Με το 127.0.0.1 ο server δέχεται πακέτα μόνο από τον ίδιο υπολογιστή· για πακέτα και από
# άλλους υπολογιστές θα χρησιμοποιούσαμε την κενή συμβολοσειρά '' (όλες οι διεπαφές).
serverSocket.bind(("127.0.0.1", serverPort))

# Μήνυμα ότι ο διακομιστής είναι έτοιμος να δεχτεί πακέτα
print("The server is ready to receive")
# Βοηθητικό μήνυμα: σε ποια διεύθυνση/θύρα ακούει ο server και πώς σταματά
print("Listening on 127.0.0.1 port", serverPort, "- waiting for packets... (press Ctrl+C to stop)")

# Ατέρμονος βρόχος για την εξυπηρέτηση των πακέτων που έρχονται από τους πελάτες
while True:

 # Η recvfrom(2048) περιμένει ένα πακέτο (έως 2048 bytes) και επιστρέφει:
 #   message       : τα δεδομένα του πακέτου (bytes)
 #   clientAddress : τη διεύθυνση του πελάτη που το έστειλε, ως πλειάδα (IP, θύρα)
 message, clientAddress = serverSocket.recvfrom(2048)
 # Βοηθητικό μήνυμα: από ποια διεύθυνση (IP, θύρα) ήρθε το πακέτο
 print("Packet received from", clientAddress)
 print(message.decode())

 # Μετατροπή του μηνύματος από bytes σε string (decode) και στη συνέχεια σε κεφαλαία γράμματα
 modifiedMessage = message.decode().upper()

 # Αποστολή του τροποποιημένου μηνύματος (ως bytes, με encode) πίσω στη διεύθυνση (IP, θύρα)
 # του πελάτη. Η διεύθυνση αυτή είναι γνωστή μόνο από το πακέτο που λάβαμε.
 serverSocket.sendto(modifiedMessage.encode(), clientAddress)
