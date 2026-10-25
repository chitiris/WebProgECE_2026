# Νήματα (Java) — Εισαγωγή στα threads

Η **`ThreadRunnable`** υλοποιεί το `Runnable` και τυπώνει έναν μετρητή από 1 έως 100.
Η **`MyThread`** δημιουργεί δύο νήματα (`Thread1`, `Thread2`) με το ίδιο αντικείμενο `Runnable` και τα ξεκινά.

| Αρχείο | Ρόλος |
|---|---|
| `MyThread.java` | Πρόγραμμα με `main`: δημιουργεί και ξεκινά τα δύο νήματα |
| `ThreadRunnable.java` | Ο κώδικας (`run()`) που εκτελεί κάθε νήμα |

## Εκτέλεση

**Προαπαιτούμενα:** JDK (`javac -version`). Σε Ubuntu / WSL: `bash ../requirements.sh`, που εγκαθιστά το OpenJDK 25.

```bash
javac *.java          # σε Windows, αν χρειαστεί: javac -encoding UTF-8 *.java
java MyThread
```

> ⚠️ Το πρόγραμμα ξεκινά **πάντα από την `MyThread`**, που έχει τη μέθοδο `main`. Η `ThreadRunnable` περιέχει
> μόνο τον κώδικα των νημάτων και δεν τρέχει μόνη της: το `java ThreadRunnable.java` δίνει
> `error: can't find main(String[]) method in class: ThreadRunnable`.
>
> 💡 Η σύντομη μορφή `java MyThread.java` (εκτέλεση χωρίς `javac`) δουλεύει μόνο από τη **Java 22** και μετά,
> γιατί βρίσκει μόνη της και το `ThreadRunnable.java` του ίδιου φακέλου. Σε παλαιότερες εκδόσεις βγάζει
> `cannot find symbol`. Οι εντολές `javac *.java` και `java MyThread` δουλεύουν σε κάθε έκδοση.

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
