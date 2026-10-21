# Εισαγωγή του απαραίτητου module socket
from socket import *

# Ορισμός της θύρας του διακομιστή
serverPort = 12000

# Δημιουργία ενός socket αντικειμένου χρησιμοποιώντας IPv4 διεύθυνση και πρωτόκολλο UDP
serverSocket = socket(AF_INET, SOCK_DGRAM)

# Δέσμευση του socket στην καθορισμένη διεύθυνση IP και θύρα ("127.0.0.1" και 12000 σε αυτήν την περίπτωση)
serverSocket.bind(("127.0.0.1", serverPort))

# Εκτύπωση μηνύματος για να δείξετε ότι ο διακομιστής είναι έτοιμος να δεχτεί πακέτα
print("The server is ready to receive")

# Εισαγωγή σε μια ατέλειωτη βρόχο για την χειρισμό των πακέτων που έρχονται από τους πελάτες
while True:

 # Λήψη ενός πακέτου και της διεύθυνσης (client IP and port) του πελάτη που το έστειλε
 message, clientAddress = serverSocket.recvfrom(2048)
 print(message.decode())

 # Μετατροπή του μηνύματος που λήφθηκε σε μια συμβολοσειρά και μετατροπή της σε κεφαλαία γράμματα
 modifiedMessage = message.decode().upper()

 # Αποστολή στον πελάτη πακέτου που περιέχει το τροποποιημένο μήνυμα στη τη διεύθυνση (client IP and port) του πελάτη
 serverSocket.sendto(modifiedMessage.encode(), clientAddress)
