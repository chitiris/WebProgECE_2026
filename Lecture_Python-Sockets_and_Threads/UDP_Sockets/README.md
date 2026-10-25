# UDP Sockets (Python) — Μετατροπή κειμένου σε κεφαλαία

Η ίδια λειτουργία με το παράδειγμα `TCP_Sockets`, αλλά με **UDP** (χωρίς σύνδεση).
Το **`UDPServer.py`** ακούει στο `127.0.0.1`, θύρα `12000`. Το **`UDPClient.py`** στέλνει ένα πακέτο
με `sendto()` και λαμβάνει την απάντηση με `recvfrom()`.

| Αρχείο | Ρόλος |
|---|---|
| `UDPServer.py` | Server: `bind()`, `recvfrom()`, απάντηση με `sendto()` στη διεύθυνση του αποστολέα |
| `UDPClient.py` | Client: `sendto()` με τη διεύθυνση του server, `recvfrom()` |

## Εκτέλεση

```bash
python UDPServer.py   # Τερματικό 1
python UDPClient.py   # Τερματικό 2
```

## Αναμενόμενη έξοδος

**Client** (ο χρήστης πληκτρολογεί `hello world`):
```
Input lowercase sentence:hello world
Sent to UDP server 127.0.0.1 port 12000 - waiting for reply...
HELLO WORLD
```

**Server:**
```
The server is ready to receive
Listening on 127.0.0.1 port 12000 - waiting for packets... (press Ctrl+C to stop)
Packet received from ('127.0.0.1', 36544)
hello world
```

> Η θύρα του client (εδώ `36544`) αλλάζει σε κάθε εκτέλεση. Ο server σταματά με **Ctrl+C**.
> Τα `TCP_Sockets` και `UDP_Sockets` χρησιμοποιούν την ίδια θύρα `12000`, αλλά μπορούν να τρέχουν μαζί,
> γιατί οι θύρες TCP και UDP είναι ανεξάρτητες.

## Δοκιμάστε
- Τρέξτε τον client χωρίς να τρέχει ο server: τι συμβαίνει; Συγκρίνετε με το TCP.
