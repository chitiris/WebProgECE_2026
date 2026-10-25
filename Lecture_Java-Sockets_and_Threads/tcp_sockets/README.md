# TCP Sockets (Java) — Μετατροπή κειμένου σε κεφαλαία

Ο **`TCPServer`** ακούει στη θύρα `6789`. Ο **`TCPClient`** συνδέεται, στέλνει μία γραμμή κειμένου
και λαμβάνει πίσω το ίδιο κείμενο με κεφαλαία. Ο server εξυπηρετεί έναν πελάτη τη φορά (iterative server).

| Αρχείο | Ρόλος |
|---|---|
| `TCPServer.java` | Server: `ServerSocket`, `accept()`, απάντηση με κεφαλαία |
| `TCPClient.java` | Client: `Socket`, αποστολή γραμμής, εκτύπωση απάντησης |

## Εκτέλεση

```bash
javac *.java          # μία φορά (σε Windows, αν χρειαστεί: javac -encoding UTF-8 *.java)
java TCPServer        # Τερματικό 1
java TCPClient        # Τερματικό 2
```

## Αναμενόμενη έξοδος

**Client** (ο χρήστης πληκτρολογεί `hello world`):
```
Connected to TCP server localhost:6789
Type a sentence in lowercase and press Enter:
hello world
FROM SERVER: HELLO WORLD
```

**Server:**
```
TCP Server is ON
Listening on port 6789. Waiting for a client... (press Ctrl+C to stop)
Client connected from /127.0.0.1:39190
HELLO WORLD

Waiting next client
```

> Η θύρα του client (εδώ `39190`) την επιλέγει το λειτουργικό σύστημα, οπότε αλλάζει σε κάθε εκτέλεση.
> Ο server συνεχίζει να περιμένει νέους πελάτες· σταματά με **Ctrl+C**.

## Δοκιμάστε
- Τρέξτε τον client χωρίς να τρέχει ο server: τι σφάλμα εμφανίζεται και γιατί;
- Τρέξτε τον client πολλές φορές στη σειρά: ο server εξυπηρετεί τον έναν μετά τον άλλον.
