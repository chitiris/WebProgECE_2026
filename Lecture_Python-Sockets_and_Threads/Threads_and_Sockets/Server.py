import socket
import threading

# Ορισμός των σταθερών που χρησιμοποιούνται στον κώδικα
HEADER = 64
PORT = 5050
SERVER = socket.gethostbyname(socket.gethostname())
ADDR = (SERVER, PORT)
FORMAT = 'utf-8'
DISCONNECT_MESSAGE = "!DISCONNECT"

# Δημιουργία του socket του διακομιστή και δέσμευση της διεύθυνσης και της θύρας
server = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
server.bind(ADDR)

# Συνάρτηση που χειρίζεται τη σύνδεση ενός πελάτη
def handle_client(conn, addr):
    print(f"[NEW CONNECTION] {addr} connected.")

    connected = True
    while connected:
        # Λήψη του μήνυματος μήκους HEADER για να γνωρίζουμε πόσα δεδομένα περιμένουμε
        msg_length = conn.recv(HEADER).decode(FORMAT)
        if msg_length:
            msg_length = int(msg_length)
            # Λήψη του πραγματικού μηνύματος
            msg = conn.recv(msg_length).decode(FORMAT)
            if msg == DISCONNECT_MESSAGE:
                connected = False

            print(f"[{addr}] {msg}")
            print(f"[{addr}] {msg_length}")
            conn.send("Msg received ".encode(FORMAT)) #response

    conn.close()

# Συνάρτηση που εκκινεί τον διακομιστή και αναμένει για συνδέσεις
def start():
    server.listen()
    print(f"[LISTENING] Server is listening on {SERVER}")
    while True:
        conn, addr = server.accept()
        # Δημιουργία νέου thread για να χειρίζεται τη σύνδεση του πελάτη
        thread = threading.Thread(target=handle_client, args=(conn, addr))
        thread.start()
        print(f"[ACTIVE CONNECTIONS] {threading.active_count() - 1}")

# Ξεκίνησε τον διακομιστή
print("[STARTING] server is starting...")
start()