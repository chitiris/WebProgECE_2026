import threadsaxample.ThreadRunnable;

public class MyThread {
    public static void main(String[] args) {
        // Δημιουργία αντικειμένου της κλάσης ThreadRunnable
        ThreadRunnable threadObject = new ThreadRunnable();

        // Δημιουργία δύο νημάτων με το ίδιο threadObject ως πηγή λειτουργίας
        Thread thread1 = new Thread(threadObject, "Thread1");
        Thread thread2 = new Thread(threadObject, "Thread2");

        // Εκτύπωση μηνύματος ότι το Thread1 είναι έτοιμο να ξεκινήσει
        System.out.println("Thread 1 about to start...");
        // Έναρξη του Thread1
        thread1.start();

        // Εκτύπωση μηνύματος ότι το Thread2 είναι έτοιμο να ξεκινήσει
        System.out.println("Thread 2 about to start...");
        // Έναρξη του Thread2
        thread2.start();
    }
}