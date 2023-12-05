package projetpc.objectif5;


public class Consumers extends Thread {

    IProdConsBuffer buffer;
    int nombreThreads = 5;
    int dureeSommeil = 10;

    private volatile boolean running = true;

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

    public void stopConsumers() {
        running = false;
    }

    @Override
    public void run() {
        Thread[] consumers = new Thread[nombreThreads];
        for (int i = 0; i < nombreThreads; i++) {
            consumers[i] = new Thread(() -> {
                while (true) {
                    dormir();
                    try {
                        Message[] message = (Message[]) this.buffer.get(4);
                        for (int j = 0; j < message.length; j++) {
                            System.out.println("Message consommer num " + message[j].getMessageID() + " --> " + message[j].getChaineAleatoire());
                        }
                       // System.out.println("Message consommer num " + message.getMessageID() + " --> " + message.getChaineAleatoire()); objectif1
                        // Process the message as needed
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
