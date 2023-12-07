package prodcons.v1;

import java.io.IOException;
import java.util.InvalidPropertiesFormatException;
import java.util.Properties;

public class TestProdCons {

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


        System.out.println("--> Thread Initial est terminee");
    }
}
