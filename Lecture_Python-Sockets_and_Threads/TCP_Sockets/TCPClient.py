# Εισαγωγή του απαραίτητου module socket
from socket import *

# Ορισμός της διεύθυνσης IP του διακομιστή που θα συνδεθεί ο πελάτης (Θα πρέπει να ορίσετε τη δική σας διεύθυνση IP)
serverName = "127.0.0.1"

# Ορισμός της θύρας που χρησιμοποιεί ο διακομιστής
serverPort = 12000

# Δημιουργία ενός socket αντικειμένου χρησιμοποιώντας IPv4 διεύθυνση και πρωτόκολλο TCP
clientSocket = socket(AF_INET, SOCK_STREAM)

# Σύνδεση του πελάτη στον διακομιστή χρησιμοποιώντας τη διεύθυνση IP και τη θύρα του διακομιστή
clientSocket.connect((serverName, serverPort))

# Λήψη μιας πρότασης από τον χρήστη με είσοδο από το πληκτρολόγιο
sentence = input('Input lowercase sentence:')


# Αποστολή της πρότασης προς τον διακομιστή, κωδικοποιημένη σε bytes
clientSocket.send(sentence.encode())

# Λήψη τροποποιημένης πρότασης από τον διακομιστή με ένα μέγιστο μέγεθος 1024 bytes
modifiedSentence = clientSocket.recv(1024)

# Εκτύπωση της τροποποιημένης πρότασης που λήφθηκε από τον διακομιστή
print("From Server: ", modifiedSentence.decode())

# Κλείσιμο του socket του πελάτη για να τερματιστεί η σύνδεση
clientSocket.close()