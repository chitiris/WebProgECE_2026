// Στόχευση της φόρμας
const form = document.getElementById('contactForm');

// Προσθήκη event listener για την υποβολή
form.addEventListener('submit', function (event) {
    // Αποτρέπουμε την προεπιλεγμένη υποβολή
    event.preventDefault();

    // Λήψη τιμών από τα πεδία
    const name = document.getElementById('name').value.trim();
    const email = document.getElementById('email').value.trim();
    const message = document.getElementById('message').value.trim();

    // Έλεγχος αν τα πεδία είναι συμπληρωμένα
    if (!name) {
        alert('Παρακαλώ εισάγετε το όνομά σας.');
        return;
    }

    if (!email) {
        alert('Παρακαλώ εισάγετε το email σας.');
        return;
    }

    // Έλεγχος αν το email είναι σε σωστή μορφή
    const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    // Η τακτική έκφραση: /^[^\s@]+@[^\s@]+\.[^\s@]+$/
    //
    // Αναλύεται ως εξής:
    //
    // 1. ^ και $: Εφαρμόζεται σε ολόκληρη τη συμβολοσειρά (αρχή και τέλος).
    // 2. [^\s@]+: Ένας ή περισσότεροι χαρακτήρες εκτός από κενά ή "@" (όνομα χρήστη).
    // 3. @: Ο χαρακτήρας "@" που απαιτείται.
    // 4. [^\s@]+: Ένας ή περισσότεροι χαρακτήρες εκτός από κενά ή "@" (domain).
    // 5. \.: Μια κυριολεκτική τελεία.
    // 6. [^\s@]+: Ένας ή περισσότεροι χαρακτήρες εκτός από κενά ή "@" (domain extension).
    //
    // Χρησιμοποιείται για να ελέγχει αν μια διεύθυνση email είναι σε σωστή μορφή.

    if (!emailRegex.test(email)) {
        alert('Παρακαλώ εισάγετε έγκυρο email.');
        return;
    }

    if (!message) {
        alert('Παρακαλώ γράψτε το μήνυμά σας.');
        return;
    }

    // Αν όλα είναι σωστά, εμφανίζουμε μήνυμα επιτυχίας
    alert('Η φόρμα υποβλήθηκε επιτυχώς!');
    form.reset(); // Καθαρισμός της φόρμας
});
