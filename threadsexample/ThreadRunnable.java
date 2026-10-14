//δεν εκτελείται ο κώδικας τον καλούμε στο My_Thread
public class ThreadRunnable implements Runnable {
	    // Η μέθοδος run υλοποιεί τον κώδικα που θα εκτελεστεί από το νήμα
	    public void run() {
	        // Εκτύπωση μηνύματος όταν το νήμα ξεκινά την εκτέλεσή του
	        System.out.println("Thread is under Running...");

	        // Βρόγχος που εκτελείται 100 φορές
	        for (int i = 1; i <= 100; i++) {
	            // Εκτύπωση τρέχοντος νήματος, της τιμής του i και της τρέχουσας επανάληψης
	            System.out.println("Thread=" + Thread.currentThread().getName() + "  i=" + i);
	        }
	    }

}