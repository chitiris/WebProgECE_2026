# Εισαγωγή του απαραίτητου module socket
from socket import *

# Ορισμός της θύρας που θα ακούσει ο διακομιστής
serverPort = 12000

# Δημιουργία socket χρησιμοποιώντας IPv4 διεύθυνση και πρωτόκολλο TCP
serverSocket = socket(AF_INET, SOCK_STREAM)

# Δέσμευση του socket στην καθορισμένη διεύθυνση IP ('' σημαίνει όλες οι διαθέσιμες διεπαφές) και στη θύρα που καθορίσατε
serverSocket.bind(('', serverPort))

# Ορισμός του  μέγιστου αριθμού αιτούμενων συνδέσεων που μπορούν να αναμένουν στην ουρά του socket
serverSocket.listen(1)

# Εκτύπωση μηνύματος για να δείξετε ότι ο διακομιστής είναι έτοιμος να δεχτεί συνδέσεις
print("The server is ready to receive")

# Εισαγωγή σε μια ατέλειωτη βρόχο για την χειρισμό των συνδέσεων των πελατών
while True:
    # Δέχεται μια σύνδεση από έναν πελάτη και δημιουργεί ένα νέο αντικείμενο socket για αυτήν τη σύνδεση
    connectionSocket, addr = serverSocket.accept()

    # Λήψη δεδομένων (σε αυτήν την περίπτωση, μια πρόταση κειμένου) από τον πελάτη, με ένα μέγιστο μέγεθος 1024 bytes, και αποκωδικοποίηση των δεδομένων από bytes σε συμβολοσειρά (string)
    sentence = connectionSocket.recv(1024).decode()
    print("received sentence", sentence)
    # Μετατροπή της ληφθείσας πρότασης σε κεφαλαία γράμματα
    capitalizedSentence = sentence.upper()

    # Αποστολή της πρότασης σε κεφαλαία γράμματα πίσω στον πελάτη μετά την κωδικοποίηση της σε bytes
    connectionSocket.send(capitalizedSentence.encode())

    # Κλείσιμο του socket σύνδεσης, τερματίζοντας αποτελεσματικά την επικοινωνία με αυτόν τον πελάτη
    connectionSocket.close()