package ProjetPC;

import java.util.Properties;
import java.util.function.Consumer;

/*vous prendrez aussi soin de garantir que les processus commutent souvent afin
d'avoir une réelle concurrence.
Vous mettrez en place des tests qui permettent de s’assurer des propriétés attendues du
programme. */
public class TestProdCons {
    public static void main(String[] args) {

        Producer prod= new Producer();
        Consumer cons= new Consumer();

        Properties properties = new Properties();
        properties.loadFromXML(
        TestProdCons.class.getClassLoader().getResourceAsStream ("config.xml"));
        int nProd = Integer.parseInt(properties.getProperty ("nProd"));
        int nCons = Integer.parseInt(properties.getProperty ("nCons"));

        



    }
}
