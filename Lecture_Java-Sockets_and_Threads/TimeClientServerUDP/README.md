# UDP Time Server (Java) — Υπηρεσία ώρας

Ο **`TimeServer`** ακούει στη θύρα `8505`. Όταν λάβει οποιοδήποτε datagram, απαντά με την τρέχουσα
ημερομηνία και ώρα του server. Ο **`TimeClient`** στέλνει ένα αίτημα και τυπώνει την ώρα που επέστρεψε ο server.

| Αρχείο | Ρόλος |
|---|---|
| `TimeServer.java` | Server: `receive()`, σύνθεση ώρας με `Calendar`, απάντηση στον αποστολέα |
| `TimeClient.java` | Client: αποστολή αιτήματος, λήψη και εκτύπωση της ώρας |

## Εκτέλεση

```bash
javac *.java          # μία φορά (σε Windows, αν χρειαστεί: javac -encoding UTF-8 *.java)
java TimeServer       # Τερματικό 1
java TimeClient       # Τερματικό 2
```

Για σύνδεση σε server που τρέχει σε **άλλον υπολογιστή**, δώστε την IP του ως όρισμα:

```bash
java TimeClient 192.168.1.10
```

## Αναμενόμενη έξοδος

**Client:**
```
Sending the request for the time...
Waiting for reply...
Time at server location /127.0.0.1:8505 is 24/10/2026-21:31:11
```

**Server:**
```
Time Server is ON
Listening on port 8505. Waiting for time requests... (press Ctrl+C to stop)
Client from 127.0.0.1:59642 requested the time.
Client served...
```

> Η ώρα έχει μορφή `ημέρα/μήνας/έτος-ώρα:λεπτά:δευτερόλεπτα` (24ωρη). Η θύρα του client αλλάζει σε κάθε εκτέλεση.

## Δοκιμάστε
- Τρέξτε τον server σε έναν υπολογιστή και τον client σε άλλον του ίδιου δικτύου, δίνοντας την IP
  του server ως όρισμα: `java TimeClient <IP του server>`.
