# =============================================================================
# TCPClient.py — Απλός πελάτης (client) TCP σε Python.
#
# Τι κάνει:
#   Συνδέεται στον TCPServer.py (127.0.0.1, θύρα 12000), διαβάζει μια πρόταση από τον χρήστη,
#   τη στέλνει στον server και τυπώνει την απάντηση (την ίδια πρόταση με κεφαλαία γράμματα).
#
# Εκτέλεση (πρώτα ο server, μετά ο client, σε ξεχωριστά τερματικά):
#   python TCPServer.py
#   python TCPClient.py
#
# Βασισμένο στο παράδειγμα του βιβλίου Kurose & Ross, "Computer Networking: A Top-Down Approach".
# Βασικές έννοιες: socket(), connect(), send()/recv(), encode()/decode().
# =============================================================================

# Εισαγωγή όλων των ονομάτων του module socket (socket, AF_INET, SOCK_STREAM κ.λπ.),
# ώστε να τα χρησιμοποιούμε χωρίς το πρόθεμα "socket."
from socket import *

# Η διεύθυνση IP του διακομιστή στον οποίο θα συνδεθεί ο πελάτης.
# Το 127.0.0.1 (localhost) σημαίνει τον ίδιο υπολογιστή· αν ο server τρέχει σε άλλο
# υπολογιστή, βάλτε εδώ τη δική του διεύθυνση IP.
serverName = "127.0.0.1"

# Η θύρα στην οποία ακούει ο διακομιστής (πρέπει να είναι ίδια με του TCPServer.py)
serverPort = 12000

# Δημιουργία socket: AF_INET = διευθύνσεις IPv4, SOCK_STREAM = πρωτόκολλο TCP (ροή bytes)
clientSocket = socket(AF_INET, SOCK_STREAM)

# Σύνδεση με τον διακομιστή. Η connect() δέχεται μία πλειάδα (tuple) (IP, θύρα) και εκτελεί
# τη χειραψία τριών βημάτων (three-way handshake) του TCP. Αν ο server δεν τρέχει,
# προκύπτει σφάλμα ConnectionRefusedError.
clientSocket.connect((serverName, serverPort))

# Ζητάμε από τον χρήστη να πληκτρολογήσει μια πρόταση
sentence = input('Input lowercase sentence:')


# Αποστολή της πρότασης στον διακομιστή. Τα sockets μεταφέρουν bytes, όχι κείμενο,
# γι' αυτό η encode() μετατρέπει το string σε bytes (προεπιλογή: UTF-8).
clientSocket.send(sentence.encode())

# Λήψη της απάντησης από τον διακομιστή: η recv(1024) περιμένει και επιστρέφει έως 1024 bytes
modifiedSentence = clientSocket.recv(1024)

# Μετατροπή των bytes σε string με τη decode() και εκτύπωση της απάντησης
print("From Server: ", modifiedSentence.decode())

# Κλείσιμο του socket του πελάτη, που τερματίζει τη σύνδεση TCP
clientSocket.close()