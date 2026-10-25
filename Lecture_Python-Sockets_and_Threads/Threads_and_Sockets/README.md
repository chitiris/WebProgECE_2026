# Threads και Sockets (Python) — Πολυνηματικός TCP server

Το **`Server.py`** ακούει στη θύρα `5050`, στη διεύθυνση IP του υπολογιστή, και για κάθε πελάτη ξεκινά ένα
νέο νήμα (`threading.Thread`). Κάθε μήνυμα στέλνεται με μια κεφαλίδα 64 bytes που δηλώνει το μήκος του
(message framing). Το μήνυμα `!DISCONNECT` τερματίζει τη σύνδεση.

| Αρχείο | Ρόλος |
|---|---|
| `Server.py` | Server: `accept()` σε βρόχο, ένα νήμα `handle_client` ανά πελάτη |
| `client.py` | Client: στέλνει 3 έτοιμα μηνύματα, ένα μήνυμα του χρήστη και `!DISCONNECT` |

## Εκτέλεση

1. `python Server.py` (Τερματικό 1) και σημειώστε την IP από τη γραμμή `[LISTENING]`.
2. Στο `client.py` βάλτε αυτή την IP στη μεταβλητή `SERVER`.
3. `python client.py` (Τερματικό 2, και 3, 4, ... για πολλούς πελάτες).

## Αναμενόμενη έξοδος

**Client** (ο χρήστης πατά Enter δύο φορές και μετά γράφει `my own message`):
```
[CONNECTED] Connected to server 192.168.1.10:5050
Msg received
Press Enter to send the next message

Msg received
Press Enter to send the next message

Msg received
Type Something and Press Enter
my own message
Msg received
Msg received
[DISCONNECTED] Disconnected from server
```

**Server:**
```
[STARTING] server is starting...
[LISTENING] Server is listening on 192.168.1.10
[INFO] Port 5050. In client.py set SERVER = "192.168.1.10"
[INFO] Waiting for clients... (press Ctrl+C to stop)
[NEW CONNECTION] ('192.168.1.10', 51728) connected.
[ACTIVE CONNECTIONS] 1
[('192.168.1.10', 51728)] Hello World!
[('192.168.1.10', 51728)] 12
[('192.168.1.10', 51728)] Hello Everyone!
[('192.168.1.10', 51728)] 15
[('192.168.1.10', 51728)] Hello tHMMY!
[('192.168.1.10', 51728)] 12
[('192.168.1.10', 51728)] my own message
[('192.168.1.10', 51728)] 14
[('192.168.1.10', 51728)] !DISCONNECT
[('192.168.1.10', 51728)] 11
[DISCONNECTED] ('192.168.1.10', 51728) disconnected.
```

> Η IP `192.168.1.10` είναι παράδειγμα: θα δείτε την IP του δικού σας υπολογιστή. Κάτω από κάθε μήνυμα ο server
> τυπώνει το μήκος του σε bytes. Οι γραμμές `[NEW CONNECTION]` και `[ACTIVE CONNECTIONS]` μπορεί να εμφανιστούν
> με αντίστροφη σειρά, γιατί τις τυπώνουν διαφορετικά νήματα.

## Δοκιμάστε
- Ανοίξτε **πολλούς** `client.py` ταυτόχρονα και δείτε τον μετρητή `[ACTIVE CONNECTIONS]` να αυξάνεται.
- Στείλτε ένα μήνυμα με ελληνικούς χαρακτήρες: γιατί το μήκος σε bytes είναι μεγαλύτερο από το πλήθος των γραμμάτων;
