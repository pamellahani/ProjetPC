package prodcons.v2;

/* 
 * Objectif 2 : on assure la terminaison du programme
 * Description detaillee dans le fichier TestProdCons.java
 */

public class ProdConsBuffer implements IProdConsBuffer {
    Message msg_array[];

    // array size
    int size = 0;

    // number of putted messages
    int total_putted = 0;


    // tracking variables for put and get
    int num_free = 0;
    int num_stored = 0; // same as currently stored messages

    // les indexes in et out
    int index_in = 0;
    int index_out = 0;

    private int getInIndex(){
        int i = index_in;
        this.index_in = (index_in + 1 ) % this.size;
        return i;
    }

    private int getOutIndex(){
        int i = index_out;
        this.index_out = (index_out + 1) % this.size;
        return i;
    }

    public ProdConsBuffer(int size) {
        this.size = size;
        msg_array = new Message[size];
        this.num_free = size;
    }

    /**
     * Put the message m in the buffer
     **/
    @Override
    public synchronized void put(Message m) throws InterruptedException {
        while(num_free==0){
            wait();
        }
        this.msg_array[getInIndex()] = m;
        this.num_free--;
        this.num_stored++;
        this.total_putted++;
        notifyAll();
    }

    /**
     * Retrieve a message from the buffer,
     * following a FIFO order (if M1 was put before M2, M1
     * is retrieved before M2)
     **/
    @Override
    public synchronized Message get() throws InterruptedException {
        while (num_stored==0) {
            wait();
        }
        Message m = this.msg_array[getOutIndex()];
        this.num_free++;
        this.num_stored--;
        notifyAll();
        return m;
    }

    /**
     * Returns the number of messages currently available in
     * the buffer
     **/
    @Override
    public int nmsg() {
        return num_stored;
    }

    /**
     * Returns the total number of messages that have
     * been put in the buffer since its creation
     **/
    @Override
    public int totmsg() {
        return total_putted;
    }

    /*
    Objectif 1 :
    
    Tableau du gardes_actions :

    +----------------------+------------+---------------+---------------------------+
    | Methode              | Pre-Action | Garde         | Post-Action               |
    +----------------------+------------+---------------+---------------------------+
    | Produce(Message msg) | ---------  | num_free==0   | num_free-- ; num_stored++ |
    +----------------------+------------+---------------+---------------------------+
    | Message Consume()    | ---------  | num_stored==0 | num_free++ ; num_stored-- |
    +----------------------+------------+---------------+---------------------------+


    */



}
