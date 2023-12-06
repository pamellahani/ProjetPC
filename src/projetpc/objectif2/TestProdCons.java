package projetpc.objectif2;

import java.io.IOException;
import java.util.InvalidPropertiesFormatException;
import java.util.Properties;

public class TestProdCons {

    /* 
     * Pour assuer la terminaison du program apres la consommation de 
     * tout les Messages , on attend la terminaison to tout les threads producteur
     * puis on verifie dans une boucle infinie que le nombre de message dans le buffer
     * est != 0 , et comme les threads consommateurs son mit en Deamon , donc le programme
     * ce termine est les threads du consommation seront automatiquement termine
     */

    public static void main(String[] args) throws InvalidPropertiesFormatException, IOException {
        
        // Load properties from XML file
        Properties properties = new Properties();
        properties.loadFromXML(TestProdCons.class.getClassLoader().getResourceAsStream("options.xml"));
        
        // Access values from properties
        int nProd = Integer.parseInt(properties.getProperty("nProd"));
        int nCons = Integer.parseInt(properties.getProperty("nCons"));
        int bufSz = Integer.parseInt(properties.getProperty("bufSz"));
        int prodTime = Integer.parseInt(properties.getProperty("prodTime"));
        int consTime = Integer.parseInt(properties.getProperty("consTime"));
        int minProd = Integer.parseInt(properties.getProperty("minProd"));
        int maxProd = Integer.parseInt(properties.getProperty("maxProd"));
        
        // Create an instance of the shared buffer
        IProdConsBuffer buffer = new ProdConsBuffer(bufSz);
        System.out.println("Buffer size: " + bufSz);


        // Create a producer and a consumer
        Producers producteur = new Producers(buffer, nProd, minProd, maxProd, prodTime);
        Consumers consommateur = new Consumers(buffer, nCons, consTime);

        // Wait for both producer and consumer to finish
        try {
            producteur.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        while (buffer.nmsg()!=0) {
            //System.out.println("Il reste " + buffer.nmsg() + " messages dans le buffer");
            try {
                Thread.sleep(100); // on suspend le thread pour 100ms avant de reverifier
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        System.out.println("Le test est terminé.");
    }
}
