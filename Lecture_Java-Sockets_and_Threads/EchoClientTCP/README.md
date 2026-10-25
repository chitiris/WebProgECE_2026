# Πολυνηματικός Echo Server (Java, TCP)

Ο **`EchoServer`** ακούει στη θύρα `8205` και για **κάθε** πελάτη δημιουργεί ένα νέο νήμα (`EchoThread`),
ώστε να εξυπηρετεί πολλούς πελάτες ταυτόχρονα. Κάθε γραμμή που λαμβάνει την επιστρέφει ως
`Received: <κείμενο>`. Η λέξη `quit` τερματίζει τη συνεδρία.

| Αρχείο | Ρόλος |
|---|---|
| `EchoServer.java` | Server: `accept()` σε βρόχο, ένα `EchoThread` ανά πελάτη |
| `EchoClient.java` | Client: στέλνει ό,τι πληκτρολογεί ο χρήστης και τυπώνει την απάντηση |

## Εκτέλεση

```bash
javac *.java          # μία φορά (σε Windows, αν χρειαστεί: javac -encoding UTF-8 *.java)
java EchoServer       # Τερματικό 1
java EchoClient       # Τερματικό 2 (και 3, 4, ... για πολλούς πελάτες)
```

## Αναμενόμενη έξοδος

**Client** (ο χρήστης πληκτρολογεί `hello`, `network programming` και `quit`):
```
Connected to /127.0.0.1:8205
Type a line and press Enter. Type quit to end the session.
hello
Received: hello
network programming
Received: network programming
quit
Received: quit
Session ended.
```

**Server:**
```
Echo Server is ON
Listening on port 8205. Waiting for clients... (press Ctrl+C to stop)
Client from : /127.0.0.1 port 43466
Received:
hello
Received:
network programming
Received:
quit
Thread exiting...
```

> Αν ένας client κλείσει με Ctrl+C χωρίς `quit`, ο server γράφει `Client disconnected without quit.`
> και συνεχίζει να εξυπηρετεί τους υπόλοιπους.

## Δοκιμάστε
- Ανοίξτε **δύο ή περισσότερους** `EchoClient` ταυτόχρονα: ο server τους εξυπηρετεί όλους, γιατί ο καθένας
  έχει το δικό του νήμα. Συγκρίνετε με τον `TCPServer` του φακέλου `tcp_sockets`.
