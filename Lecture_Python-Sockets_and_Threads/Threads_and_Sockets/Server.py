# =============================================================================
# Server.py — Πολυνηματικός (multithreaded) διακομιστής TCP σε Python.
#
# Τι κάνει:
#   Ακούει στη θύρα 5050, στη διεύθυνση IP του υπολογιστή. Για κάθε νέο πελάτη ξεκινά ένα νέο
#   νήμα (threading.Thread), ώστε να εξυπηρετεί πολλούς πελάτες ταυτόχρονα.
#   Χρησιμοποιεί ένα απλό πρωτόκολλο εφαρμογής (βλ. και client.py):
#     1. ο πελάτης στέλνει πρώτα μια κεφαλίδα (header) σταθερού μήκους 64 bytes, που περιέχει
#        το μήκος του μηνύματος ως κείμενο, συμπληρωμένο με κενά (π.χ. "12" + 62 κενά),
#     2. και μετά το ίδιο το μήνυμα.
#   Το TCP είναι ροή bytes χωρίς όρια μηνυμάτων, οπότε η κεφαλίδα λέει στον server πόσα bytes
#   να διαβάσει (πλαισίωση μηνυμάτων, message framing).
#   Το μήνυμα "!DISCONNECT" τερματίζει τη σύνδεση.
#
# Εκτέλεση: python Server.py  και μετά ένας ή περισσότεροι  python client.py
#   Η διεύθυνση IP του server τυπώνεται στο μήνυμα [LISTENING] και πρέπει να γραφτεί
#   στη μεταβλητή SERVER του client.py.
#
# Βασικές έννοιες: threading.Thread, threading.active_count(), message framing, UTF-8.
# =============================================================================
import socket
import threading

# Σταθερές του πρωτοκόλλου (πρέπει να είναι ίδιες στον server και στον client)
HEADER = 64  # Μήκος σε bytes της κεφαλίδας που περιέχει το μήκος του μηνύματος
PORT = 5050  # Θύρα στην οποία ακούει ο server
# Η διεύθυνση IP του υπολογιστή: η gethostname() δίνει το όνομα του υπολογιστή και η
# gethostbyname() το μετατρέπει σε διεύθυνση IPv4 (σε κάποια συστήματα μπορεί να δώσει 127.x.x.x)
SERVER = socket.gethostbyname(socket.gethostname())
ADDR = (SERVER, PORT)  # Πλειάδα (IP, θύρα) για το bind()
FORMAT = 'utf-8'  # Κωδικοποίηση για τη μετατροπή κειμένου σε bytes και αντίστροφα
DISCONNECT_MESSAGE = "!DISCONNECT"  # Ειδικό μήνυμα με το οποίο ο πελάτης ζητά αποσύνδεση

# Δημιουργία του socket του διακομιστή (IPv4, TCP) και δέσμευσή του στη διεύθυνση και στη θύρα
server = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
server.bind(ADDR)

# Συνάρτηση που εξυπηρετεί έναν πελάτη. Εκτελείται σε ξεχωριστό νήμα για κάθε πελάτη.
#   conn : το socket της σύνδεσης με τον πελάτη
#   addr : η διεύθυνση του πελάτη, ως πλειάδα (IP, θύρα)
def handle_client(conn, addr):
    print(f"[NEW CONNECTION] {addr} connected.")

    connected = True
    while connected:
        # Βήμα 1: λήψη της κεφαλίδας (HEADER bytes), για να μάθουμε πόσα bytes έχει το μήνυμα
        try:
            msg_length = conn.recv(HEADER).decode(FORMAT)
        except ConnectionResetError:
            # Ο πελάτης έκλεισε απότομα (π.χ. Ctrl+C)· στα Windows αυτό εμφανίζεται ως
            # ConnectionResetError. Βγαίνουμε από τον βρόχο για να κλείσει η σύνδεση κανονικά.
            break
        # Αν η κεφαλίδα δεν είναι κενή, ήρθε νέο μήνυμα.
        # Κενή απάντηση από τη recv() σημαίνει ότι ο πελάτης έκλεισε τη σύνδεση (βλ. else παρακάτω).
        if msg_length:
            # Μετατροπή του κειμένου σε ακέραιο· η int() αγνοεί τα κενά του padding
            msg_length = int(msg_length)
            # Βήμα 2: λήψη του πραγματικού μηνύματος, μήκους msg_length bytes.
            # (Για μικρά μηνύματα, όπως εδώ, φτάνει μία recv(). Για μεγάλα μηνύματα η recv() μπορεί
            # να επιστρέψει λιγότερα bytes, οπότε θα χρειαζόταν βρόχος μέχρι να διαβαστούν όλα.)
            msg = conn.recv(msg_length).decode(FORMAT)
            # Αν ο πελάτης ζήτησε αποσύνδεση, ο βρόχος θα τελειώσει μετά από αυτή την επανάληψη
            if msg == DISCONNECT_MESSAGE:
                connected = False

            # Εκτύπωση του μηνύματος και του μήκους του, μαζί με τη διεύθυνση του πελάτη
            print(f"[{addr}] {msg}")
            print(f"[{addr}] {msg_length}")
            conn.send("Msg received ".encode(FORMAT)) # Απάντηση (επιβεβαίωση λήψης) στον πελάτη
        else:
            # Ο πελάτης έκλεισε τη σύνδεση χωρίς "!DISCONNECT". Τερματίζουμε τον βρόχο,
            # αλλιώς η recv() θα επέστρεφε συνεχώς κενό και ο βρόχος θα γύριζε ασταμάτητα (100% CPU).
            connected = False

    # Βοηθητικό μήνυμα: ο πελάτης αποσυνδέθηκε
    print(f"[DISCONNECTED] {addr} disconnected.")
    # Κλείσιμο της σύνδεσης με τον πελάτη· μετά το νήμα τερματίζει
    conn.close()

# Συνάρτηση που ξεκινά τον διακομιστή και περιμένει συνδέσεις
def start():
    # Το socket αρχίζει να ακούει για εισερχόμενες συνδέσεις
    server.listen()
    print(f"[LISTENING] Server is listening on {SERVER}")
    # Βοηθητικά μηνύματα: η θύρα, τι πρέπει να ρυθμιστεί στο client.py και πώς σταματά ο server
    print(f"[INFO] Port {PORT}. In client.py set SERVER = \"{SERVER}\"")
    print("[INFO] Waiting for clients... (press Ctrl+C to stop)")
    while True:
        # Η accept() περιμένει μέχρι να συνδεθεί πελάτης και επιστρέφει νέο socket και διεύθυνση
        conn, addr = server.accept()
        # Δημιουργία νέου νήματος που θα εκτελέσει την handle_client για αυτόν τον πελάτη.
        # Έτσι το κύριο νήμα επιστρέφει αμέσως στην accept() για τον επόμενο πελάτη.
        thread = threading.Thread(target=handle_client, args=(conn, addr))
        thread.start()
        # Πλήθος ενεργών συνδέσεων: όλα τα ενεργά νήματα μείον 1 (το κύριο νήμα)
        print(f"[ACTIVE CONNECTIONS] {threading.active_count() - 1}")

# Εκκίνηση του διακομιστή
print("[STARTING] server is starting...")
start()