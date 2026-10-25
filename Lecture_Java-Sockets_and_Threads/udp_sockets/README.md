# UDP Sockets (Java) — Μετατροπή κειμένου σε κεφαλαία

Η ίδια λειτουργία με το παράδειγμα `tcp_sockets`, αλλά με **UDP** (χωρίς σύνδεση).
Ο **`UDPServer`** ακούει στη θύρα `9876`. Ο **`UDPClient`** στέλνει ένα datagram με κείμενο
και λαμβάνει πίσω το ίδιο κείμενο με κεφαλαία.

| Αρχείο | Ρόλος |
|---|---|
| `UDPServer.java` | Server: `DatagramSocket(9876)`, `receive()`, απάντηση στη διεύθυνση του αποστολέα |
| `UDPClient.java` | Client: `DatagramPacket` με τη διεύθυνση του server, `send()` / `receive()` |

## Εκτέλεση

```bash
javac *.java          # μία φορά (σε Windows, αν χρειαστεί: javac -encoding UTF-8 *.java)
java UDPServer        # Τερματικό 1
java UDPClient        # Τερματικό 2
```

## Αναμενόμενη έξοδος

**Client** (ο χρήστης πληκτρολογεί `hello world`):
```
Type a sentence in lowercase and press Enter:
hello world
Sent to UDP server localhost:9876. Waiting for reply...
FROM SERVER:HELLO WORLD
```

**Server:**
```
UDP Server is ON
Listening on port 9876. Waiting for packets... (press Ctrl+C to stop)
Packet received from /127.0.0.1:48998
HELLO WORLD
```

> Η θύρα του client (εδώ `48998`) αλλάζει σε κάθε εκτέλεση. Ο server σταματά με **Ctrl+C**.

## Δοκιμάστε
- Τρέξτε τον client χωρίς να τρέχει ο server: σε αντίθεση με το TCP, δεν εμφανίζεται σφάλμα σύνδεσης·
  ο client στέλνει το πακέτο και περιμένει για πάντα απάντηση. Γιατί;
