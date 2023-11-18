package ProjetPC;

public class TestProdCons {
    static final int BUFFER_SIZE = 5;
    static final int NB_OPERATIONS = 100;

    public static void main(String[] args) {
        // Create an instance of the shared buffer
        IProdConsBuffer buffer = new ProdConsBuffer(BUFFER_SIZE); 

        // Create a producer and a consumer
        Producer producteur = new Producer(buffer, NB_OPERATIONS);
        Consumer consommateur = new Consumer(buffer, NB_OPERATIONS);

        // Wait for both producer and consumer to finish
        try {
            producteur.join();
            consommateur.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("Le test est terminé.");
    }
}
