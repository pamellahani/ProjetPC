package projetpc.objectif1;
import java.util.Random;

public class Message {
    static int messageCount = 1;

    private String chaineAleatoire;
    private int longueur = 10;
    private int id = messageCount++;

    public Message() {
        this.chaineAleatoire = genererChaineAleatoire();
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

    int getMessageID() {
        return id;
    }
}