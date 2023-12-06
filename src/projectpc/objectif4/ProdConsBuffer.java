package projetpc.objectif4;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ProdConsBuffer implements IProdConsBuffer {
    // Verrou pour assurer la synchronisation
    private final Lock verrou = new ReentrantLock();

    // Conditions pour gérer l'attente et la notification
    private final Condition nonPlein = verrou.newCondition();
    private final Condition nonVide = verrou.newCondition();

    private Message msg_array[];

    // array size
    private int size;

    // number of putted messages
    private int total_putted = 0;


    // tracking variables
    private int num_free;
    private int num_stored;

    // les indexes in et out
    private int index_in = 0;
    private int index_out = 0;

    
    public ProdConsBuffer(int size) {
        this.size = size;
        msg_array = new Message[size];
        this.num_free = size;
    }

    private int getInIndex() {
        int i = index_in;
        this.index_in = (index_in + 1) % this.size;
        return i;
    }

    private int getOutIndex() {
        int i = index_out;
        this.index_out = (index_out + 1) % this.size;
        return i;
    }

    @Override
    public void put(Message m) throws InterruptedException {
        verrou.lock();
        try {
            // Tant que le buffer est plein, attendre qu'il ne soit pas plein
            while (num_free == 0) {
                nonPlein.await();
            }
            // Placer le message dans le buffer
            this.msg_array[getInIndex()] = m;
            this.num_free--;
            this.num_stored++;
            this.total_putted++;
            // Signaler que le buffer n'est pas vide
            nonVide.signal();
        } finally {
            verrou.unlock();
        }
    }

    @Override
    public Message get() throws InterruptedException {
        verrou.lock();
        try {
            // Tant que le buffer est vide, attendre qu'il ne soit pas vide
            while (num_stored == 0) {
                nonVide.await();
            }
            // Récupérer un message du buffer
            Message m = this.msg_array[getOutIndex()];
            this.num_free++;
            this.num_stored--;
            // Signaler que le buffer n'est pas plein
            nonPlein.signal();
            return m;
        } finally {
            verrou.unlock();
        }
    }

    @Override
    public int nmsg() {
        return num_stored;
    }

    @Override
    public int totmsg() {
        return total_putted;
    }
}
