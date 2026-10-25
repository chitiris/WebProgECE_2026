# TCP Sockets (Python) — Μετατροπή κειμένου σε κεφαλαία

Το **`TCPServer.py`** ακούει στη θύρα `12000`. Το **`TCPClient.py`** συνδέεται, στέλνει μια πρόταση
και λαμβάνει πίσω την ίδια πρόταση με κεφαλαία. Μετά την απάντηση ο server κλείνει τη σύνδεση
και περιμένει τον επόμενο πελάτη.

| Αρχείο | Ρόλος |
|---|---|
| `TCPServer.py` | Server: `bind()`, `listen()`, `accept()`, απάντηση με κεφαλαία |
| `TCPClient.py` | Client: `connect()`, `send()`, `recv()` |

## Εκτέλεση

```bash
python TCPServer.py   # Τερματικό 1
python TCPClient.py   # Τερματικό 2
```

## Αναμενόμενη έξοδος

**Client** (ο χρήστης πληκτρολογεί `hello world`):
```
Connected to TCP server 127.0.0.1 port 12000
Input lowercase sentence:hello world
From Server:  HELLO WORLD
```

**Server:**
```
The server is ready to receive
Listening on port 12000 - waiting for a client... (press Ctrl+C to stop)
Client connected from ('127.0.0.1', 37550)
received sentence hello world
```

> Η θύρα του client (εδώ `37550`) αλλάζει σε κάθε εκτέλεση. Ο server σταματά με **Ctrl+C**
> (σε Windows ίσως χρειαστεί να συνδεθεί ακόμη ένας client για να σταματήσει).

## Δοκιμάστε
- Τρέξτε τον client χωρίς να τρέχει ο server: εμφανίζεται `ConnectionRefusedError`. Γιατί;
