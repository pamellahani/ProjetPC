package prodcons.v5;


public class Consumers extends Thread {

    IProdConsBuffer buffer;
    int nombreThreads = 5;
    int dureeSommeil = 10;

    public Consumers(IProdConsBuffer buffer, int nombreConsumers, int dureeSommeil) {
        this.buffer = buffer;
        this.nombreThreads = nombreConsumers;
        this.dureeSommeil = dureeSommeil;
        this.setDaemon(true);
        this.start();
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
        Thread[] consumers = new Thread[nombreThreads];
        for (int i = 0; i < nombreThreads; i++) {
            consumers[i] = new Thread(() -> {
                while (true) {
                    dormir();
                    try {
                        Message[] message = this.buffer.get(4);
                        for (int j = 0; j < message.length; j++) {
                        	System.out.println("Message consommer num " + message[j].getMessageID() + " (Thread " + Thread.currentThread().getId() + ") --> Message : " + message[j].getChaineAleatoire());
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            });
            consumers[i].setDaemon(true);
            consumers[i].start();
        }
    }
}
