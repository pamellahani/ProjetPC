package ProjetPC;

import java.util.Random;

public class Consumer extends Thread {
    static final int GET_PER_THREAD = 10;

    IProdConsBuffer buffer;
    int nombreConsommations = 50;
    int nombreThreads = 5;

    public Consumer(IProdConsBuffer buffer, int nombreConsommations) {
        this.buffer = buffer;
        this.nombreConsommations = nombreConsommations;
        this.nombreThreads = nombreConsommations / GET_PER_THREAD;
        this.start();
    }

    public static void dormirAleatoirement() {
        Random random = new Random();
        int dureeMaxSommeil = 700; // durée maximale du sommeil en millisecondes
        int dureeSommeil = random.nextInt(dureeMaxSommeil + 1);
        try {
            Thread.sleep(dureeSommeil);
        } catch (InterruptedException e) {
            System.out.println("Le sommeil a été interrompu !");
        }
    }

    @Override
    public void run() {
        Thread[] threads = new Thread[nombreThreads];
        for (int i = 0; i < nombreThreads; i++) {
            threads[i] = new Thread(() -> {
                for (int j = 0; j < GET_PER_THREAD; j++) {
                    dormirAleatoirement();
                    try {
                        Message message = (Message) this.buffer.get();
                        System.out.println("Message generer num " + message.getMessageID() + " --> " + message.getChaineAleatoire());
                        // Process the message as needed
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            });
            threads[i].start();
        }

        for (int i = 0; i < nombreThreads; i++) {
            try {
                threads[i].join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
