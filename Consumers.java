package ProjetPC;


public class Consumers extends Thread {

    IProdConsBuffer buffer;
    int nombreThreads = 5;
    int dureeSommeil = 10;

    public Consumers(IProdConsBuffer buffer, int nombreConsumers, int dureeSommeil) {
        this.buffer = buffer;
        this.nombreThreads = nombreConsumers;
        this.dureeSommeil = dureeSommeil;
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
                for (int j = 0; j < nombreThreads; j++) {
                    dormir();
                    try {
                        Message message = (Message) this.buffer.get();
                        System.out.println("Message generer num " + message.getMessageID() + " --> " + message.getChaineAleatoire());
                        // Process the message as needed
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            });
            consumers[i].start();
        }

        for (int i = 0; i < nombreThreads; i++) {
            try {
                consumers[i].join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
