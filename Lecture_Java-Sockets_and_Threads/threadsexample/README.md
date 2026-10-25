# Νήματα (Java) — Εισαγωγή στα threads

Η **`ThreadRunnable`** υλοποιεί το `Runnable` και τυπώνει έναν μετρητή από 1 έως 100.
Η **`MyThread`** δημιουργεί δύο νήματα (`Thread1`, `Thread2`) με το ίδιο αντικείμενο `Runnable` και τα ξεκινά.

| Αρχείο | Ρόλος |
|---|---|
| `MyThread.java` | Πρόγραμμα με `main`: δημιουργεί και ξεκινά τα δύο νήματα |
| `ThreadRunnable.java` | Ο κώδικας (`run()`) που εκτελεί κάθε νήμα |

## Εκτέλεση

```bash
javac *.java          # σε Windows, αν χρειαστεί: javac -encoding UTF-8 *.java
java MyThread
```

## Αναμενόμενη έξοδος

Τυπώνονται συνολικά 200 γραμμές `Thread=...` (100 από κάθε νήμα). Για παράδειγμα:
```
Thread 1 about to start...
Thread 2 about to start...
Thread is under Running...
Thread is under Running...
Thread=Thread1  i=1
Thread=Thread1  i=2
Thread=Thread2  i=1
Thread=Thread1  i=3
Thread=Thread2  i=2
...
Thread=Thread2  i=100
```

> **Η σειρά των γραμμών αλλάζει από εκτέλεση σε εκτέλεση**: τα δύο νήματα τρέχουν ταυτόχρονα και τη σειρά
> την αποφασίζει ο χρονοπρογραμματιστής (scheduler). Σε γρήγορους υπολογιστές συχνά το ένα νήμα προλαβαίνει
> να τυπώσει πολλές γραμμές στη σειρά πριν εμφανιστεί το άλλο.

## Δοκιμάστε
- Αλλάξτε τα `start()` σε `run()`: τι αλλάζει στην έξοδο και γιατί;
- Αυξήστε τον μετρητή σε 1000 για να δείτε πιο καθαρά την παρεμβολή (interleaving) των νημάτων.
