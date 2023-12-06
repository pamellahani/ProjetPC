package projetpc.objectif3;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

public class Message {
    private String chaineAleatoire;
    private int longueur = 10;
    private int id;

    private static final AtomicInteger messageCount = new AtomicInteger(1);

    public Message() {
        this.chaineAleatoire = genererChaineAleatoire();
        this.id = messageCount.getAndIncrement();
    }

    public String getChaineAleatoire() {
        return chaineAleatoire;
    }

    private String genererChaineAleatoire() {
        StringBuilder chaineAleatoireBuilder = new StringBuilder();
        String caracteres = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        Random random = new Random();

        for (int i = 0; i < longueur; i++) {
            int index = random.nextInt(caracteres.length());
            chaineAleatoireBuilder.append(caracteres.charAt(index));
        }

        return chaineAleatoireBuilder.toString();
    }

    public int getMessageID() {
        return id;
    }
}
