package projetpc.objectif4;

import java.util.Random;

public class Producers extends Thread {

    IProdConsBuffer buffer;
    int numberOfThreads = 5;
    int dureeSommeil = 10;

    // max and min Production
    int minProd = 0;
    int maxProd = 500;

    public Producers(IProdConsBuffer buffer,int nProd,int minProd,  int maxProd, int dureeSommeil) {
        this.buffer = buffer;
        this.dureeSommeil = dureeSommeil;
        this.numberOfThreads = nProd;
        this.maxProd = maxProd;
        this.minProd = minProd;
        this.start();
    }

    public int generateRandomNumber() {
        Random random = new Random();
        return random.nextInt(maxProd - minProd + 1) + minProd;
    }

    public void dormir() {
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
                int numberOfProd = generateRandomNumber();
                for (int j = 0; j < numberOfProd; j++) {
                    dormir();
                    try {
                        Message msg = new Message();
                        this.buffer.put(msg);
                        //System.out.println("--> Message produit num " + msg.getMessageID() + " --> " + msg.getChaineAleatoire());
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
