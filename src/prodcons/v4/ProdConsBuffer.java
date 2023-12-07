package prodcons.v4;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;


/*
 * Objectif 4 : 
 * Dans cette partie , on utilise une methode equivalent au Moniteur 
 * Ce sont les verrous , pour cela, on a besoin de 2 condition , une condition 
 * notFull qui attend qu'un place soit disponible pour faire le put (le signal sera recu par un get)
 * et une autre condition notEmpty qui attend qu'au moins un message soit mit dans le buffer pour faire
 * un get (le signal sera recu par un put).
 * */

public class ProdConsBuffer implements IProdConsBuffer {
    // Verrou pour assurer la synchronisation
    private final Lock lock = new ReentrantLock();

    // Conditions pour gérer l'attente et la notification
    private final Condition notFull = lock.newCondition();
    private final Condition notEmpty = lock.newCondition();

    private Message msg_array[];

    // array size
    private int size;

    // number of putted messages
    private int total_putted = 0;


    // variables du tracage
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
        lock.lock();
        try {
            // Tant que le buffer est plein, attendre qu'il ne soit pas plein
            while (num_free == 0) {
                notFull.await();
            }
            // Placer le message dans le buffer
            this.msg_array[getInIndex()] = m;
            this.num_free--;
            this.num_stored++;
            this.total_putted++;
            // Signaler que le buffer n'est pas vide
            notEmpty.signal();
        } finally {
            lock.unlock();
        }
    }

    @Override
    public Message get() throws InterruptedException {
        lock.lock();
        try {
            // Tant que le buffer est vide, attendre qu'il ne soit pas vide
            while (num_stored == 0) {
                notEmpty.await();
            }
            // Récupérer un message du buffer
            Message m = this.msg_array[getOutIndex()];
            this.num_free++;
            this.num_stored--;
            // Signaler que le buffer n'est pas plein
            notFull.signal();
            return m;
        } finally {
            lock.unlock();
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
