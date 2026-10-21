# Εισαγωγή του απαραίτητου module socket
from socket import *

# Ορισμός της διεύθυνσης IP του διακομιστή (θα πρέπει να ορίσετε τη δική σας διεύθυνση IP)
serverName = "127.0.0.1"

# Ορισμός της θύρας του διακομιστή
serverPort = 12000

# Δημιουργία ενός socket αντικειμένου χρησιμοποιώντας IPv4 διεύθυνση και πρωτόκολλο UDP
clientSocket = socket(AF_INET, SOCK_DGRAM)

# Εισαγωγή του μηνύματος από τον χρήστη με είσοδο από το πληκτρολόγιο
message = input('Input lowercase sentence:')

# Αποστολή πακέτου προς τον διακομιστή που περιέχει το μηνύμα  κωδικοποιημένο σε bytes, και τη διεύθυνση του διακομιστή (server IP and port)
clientSocket.sendto(message.encode(), (serverName, serverPort))

# Λήψη πακέτου από τον διακομιστή που περιέχει το τροποποιημένο μήνυμα και τη διεύθυνση του διακομιστή (server IP and port)
modifiedMessage, serverAddress = clientSocket.recvfrom(2048)

# Εμφάνιση του τροποποιημένου μηνύματος στην οθόνη
print(modifiedMessage.decode())

# Κλείσιμο του socket του πελάτη για να τερματιστεί η σύνδεση
clientSocket.close()
