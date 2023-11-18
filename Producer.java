package ProjetPC;

import java.util.Random;

public class Producer extends Thread {
    static final int PUT_PER_THREAD = 10;

    IProdConsBuffer buffer;
    int numberOfProductions = 50;
    int numberOfThreads = 5;

    public Producer(IProdConsBuffer buffer, int numberOfProductions) {
        this.buffer = buffer;
        this.numberOfProductions = numberOfProductions;
        this.numberOfThreads = numberOfProductions / PUT_PER_THREAD;
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
        super.run();
        Thread[] threads = new Thread[numberOfThreads];
        for (int i = 0; i < numberOfThreads; i++) {
            threads[i] = new Thread(() -> {
                for (int j = 0; j < PUT_PER_THREAD; j++) {
                    dormirAleatoirement();
                    try {
                        this.buffer.put(new Message());
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            });
            threads[i].start();
        }

        for (int i = 0; i < numberOfThreads; i++) {
            try {
                threads[i].join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
