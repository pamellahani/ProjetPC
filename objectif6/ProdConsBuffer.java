package projetpc.objectif6;

import java.util.HashMap;
import java.util.concurrent.Semaphore;

/* 
 * Solution Directe : Objectif 1
 * Cette solution est implementer en utilisant 
 * que les Moniteurs
 * 
 */

public class ProdConsBuffer implements IProdConsBuffer {
    Pair[] msg_array;

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

    private int getInIndex() {
        int i = index_in;
        this.index_in = (index_in + 1) % this.size;
        return i;
    }

    private int getOutIndex() {
        int i = index_out;
        return i;
    }

    private void updateOutIndex() {
        this.index_out = (index_out + 1) % this.size;
    }

    public ProdConsBuffer(int size) {
        this.size = size;
        msg_array = new Pair[size];
        this.num_free = size;
    }

    /**
     * Put the message m in the buffer
     **/
    @Override
    public void put(Message m, int n) throws InterruptedException {
        Pair mypair;
        synchronized (this) {
            while (num_free < n) {
                wait();
            }
            mypair = new Pair(m, n);
            this.msg_array[getInIndex()] = mypair;
            this.num_free--; // free space in array
            this.num_stored += n;
            this.total_putted += n;
            notifyAll();
        }
        mypair.attenteConsommation(n);
    }

    /**
     * Retrieve a message from the buffer,
     * following a FIFO order (if M1 was put before M2, M1
     * is retrieved before M2)
     **/
    @Override
    public Message get() throws InterruptedException {
        Pair mypair;
        Message m;
        synchronized (this) {
            while (num_stored == 0) {
                wait();
            }
            mypair = this.msg_array[getOutIndex()];
            m = mypair.getMessage();
            mypair.removeExemplaire();
            this.num_stored--;

            if (mypair.isEmpty()) {
                this.msg_array[index_out] = null;
                this.num_free++;
                updateOutIndex();
            }
            notifyAll();
        }
        mypair.attenteGet();
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
     * Objectif 1 :
     * 
     * Tableau du gardes_actions :
     * 
     * +----------------------+------------+---------------+------------------------
     * ---+
     * | Methode | Pre-Action | Garde | Post-Action |
     * +----------------------+------------+---------------+------------------------
     * ---+
     * | Produce(Message msg) | --------- | num_free==0 | num_free-- ; num_stored++
     * |
     * +----------------------+------------+---------------+------------------------
     * ---+
     * | Message Consume() | --------- | num_stored==0 | num_free++ ; num_stored-- |
     * +----------------------+------------+---------------+------------------------
     * ---+
     * 
     * 
     */

}

class Pair {
    Message msg;
    int exemplaires;
    Semaphore onPut = new Semaphore(0);
    Semaphore onGet = new Semaphore(0);

    public Pair(Message msg, int exemplaires) {
        this.msg = msg;
        this.exemplaires = exemplaires;
    }

    public Message getMessage() {
        return this.msg;
    }

    public boolean isEmpty() {
        return this.exemplaires <= 0;
    }

    public void removeExemplaire() {
        this.exemplaires--;
        this.onPut.release();
    }

    public void attenteConsommation(int num_messages) {
        try {
            this.onPut.acquire(num_messages);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        this.onGet.release(num_messages);
    }

    public void attenteGet() {
        try {
            this.onGet.acquire();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
