# =============================================================================
# TCPServer.py — Απλός επαναληπτικός (iterative) διακομιστής (server) TCP σε Python.
#
# Τι κάνει:
#   Ακούει στη θύρα 12000. Για κάθε πελάτη που συνδέεται λαμβάνει μια πρόταση, την επιστρέφει
#   με κεφαλαία γράμματα και κλείνει τη σύνδεση. Εξυπηρετεί έναν πελάτη τη φορά
#   (για ταυτόχρονη εξυπηρέτηση με νήματα, δείτε το Threads_and_Sockets/Server.py).
#
# Εκτέλεση: python TCPServer.py  (ο πελάτης είναι το TCPClient.py)
#
# Βασισμένο στο παράδειγμα του βιβλίου Kurose & Ross, "Computer Networking: A Top-Down Approach".
# Βασικές έννοιες: socket(), bind(), listen(), accept(), recv()/send(), encode()/decode().
# =============================================================================

# Εισαγωγή όλων των ονομάτων του module socket (socket, AF_INET, SOCK_STREAM κ.λπ.)
from socket import *

# Η θύρα στην οποία θα ακούει ο διακομιστής
serverPort = 12000

# Δημιουργία socket: AF_INET = διευθύνσεις IPv4, SOCK_STREAM = πρωτόκολλο TCP
serverSocket = socket(AF_INET, SOCK_STREAM)

# Δέσμευση (bind) του socket σε διεύθυνση IP και θύρα. Η κενή συμβολοσειρά '' σημαίνει
# όλες τις διαθέσιμες διεπαφές δικτύου του υπολογιστή, οπότε ο server δέχεται συνδέσεις
# τόσο από το localhost όσο και από άλλους υπολογιστές του δικτύου.
serverSocket.bind(('', serverPort))

# Το socket γίνεται "socket υποδοχής": αρχίζει να ακούει για συνδέσεις.
# Η παράμετρος 1 ορίζει το μέγιστο πλήθος συνδέσεων που μπορούν να περιμένουν στην ουρά
# μέχρι να τις αποδεχτεί ο server με accept().
serverSocket.listen(1)

# Μήνυμα ότι ο διακομιστής είναι έτοιμος να δεχτεί συνδέσεις
print("The server is ready to receive")
# Βοηθητικό μήνυμα: σε ποια θύρα ακούει ο server και πώς σταματά
print("Listening on port", serverPort, "- waiting for a client... (press Ctrl+C to stop)")

# Ατέρμονος βρόχος για την εξυπηρέτηση των πελατών, του ενός μετά τον άλλο
while True:
    # Η accept() περιμένει (μπλοκάρει) μέχρι να συνδεθεί ένας πελάτης. Τότε επιστρέφει:
    #   connectionSocket : ένα ΝΕΟ socket, αφιερωμένο στην επικοινωνία με αυτόν τον πελάτη
    #   addr             : τη διεύθυνση του πελάτη, ως πλειάδα (IP, θύρα)
    connectionSocket, addr = serverSocket.accept()
    # Βοηθητικό μήνυμα: από ποια διεύθυνση (IP, θύρα) συνδέθηκε ο πελάτης
    print("Client connected from", addr)

    # Λήψη έως 1024 bytes από τον πελάτη και μετατροπή τους από bytes σε string με τη decode()
    sentence = connectionSocket.recv(1024).decode()
    print("received sentence", sentence)
    # Μετατροπή της πρότασης σε κεφαλαία γράμματα
    capitalizedSentence = sentence.upper()

    # Αποστολή της πρότασης με κεφαλαία πίσω στον πελάτη, αφού μετατραπεί σε bytes με την encode()
    connectionSocket.send(capitalizedSentence.encode())

    # Κλείσιμο του socket της σύνδεσης: τελειώνει η επικοινωνία με αυτόν τον πελάτη.
    # Το socket υποδοχής (serverSocket) μένει ανοιχτό για τον επόμενο πελάτη.
    connectionSocket.close()