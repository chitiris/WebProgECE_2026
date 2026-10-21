import socket

# Ορισμός σταθερών
HEADER = 64  # Μέγεθος του μηνύματος μήκους που θα σταλεί στον διακομιστή
PORT = 5050  # Θύρα σύνδεσης
FORMAT = 'utf-8'  # Μορφή κωδικοποίησης (UTF-8 στην περίπτωση αυτή)
DISCONNECT_MESSAGE = "!DISCONNECT"  # Μήνυμα αποσύνδεσης
SERVER = "192.168.44.164"  # Ορισμός της διεύθυνσης IP του διακομιστή (θα πρέπει να ορίσετε τη δική σας διεύθυνση IP)
ADDR = (SERVER, PORT)

# Δημιουργία socket για τη σύνδεση στον διακομιστή
client = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
client.connect(ADDR)  # Σύνδεση στον διακομιστή

# Συνάρτηση για την αποστολή μηνυμάτων στον διακομιστή
def send(msg):
    message = msg.encode(FORMAT)  # Κωδικοποίηση του μηνύματος σε bytes
    msg_length = len(message)  # Υπολογισμός του μήκους του μηνύματος
    send_length = str(msg_length).encode(FORMAT)  # Κωδικοποίηση του μήκους του μηνύματος σε bytes
    send_length += b' ' * (HEADER - len(send_length)) # pad spaces at the end of the send_length string to finally hecontain Header characters
    # Αποστολή του μήκους του μηνύματος προς τον διακομιστή
    client.send(send_length)
    # Αποστολή του πραγματικού μηνύματος προς τον διακομιστή
    client.send(message)
    # Εκτύπωση της απάντησης από τον διακομιστή (εφόσον υπάρχει)
    print(client.recv(2048).decode(FORMAT))

# Κώδικας για την είσοδο μηνυμάτων από τον χρήστη και αποστολή τους στον διακομιστή

send("Hello World!") #change 1st Message
input() #Waiting to Press Enter
send("Hello Everyone!") #change 2nd Message
input()
send("Hello tHMMY!") #change 3rd Message
print('Type Something and Press Enter')
#Type Something and press enter
send(input())

# Αποστολή μηνύματος αποσύνδεσης στον διακομιστή
send(DISCONNECT_MESSAGE)