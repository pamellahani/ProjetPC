package prodcons.v6;

import java.util.concurrent.Semaphore;

/* 
 * Objectif 6 :  Chaque message est encapsulé dans une classe "MessageData" avec des sémaphores "onPut" et "onGet". 
 * Lorsqu'un producteur place un message dans le buffer, 
 * il attend que tous les exemplaires du message soient consommés par les consommateurs via "onPut". 
 * De même, lorsqu'un consommateur récupère un message, 
 * il utilise "onGet" pour s'assurer que le message est consommé avant d'en obtenir un autre.
 * 
 */

public class ProdConsBuffer implements IProdConsBuffer {
    MessageData[] msg_array;

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
        this.index_out = (index_out + 1) % this.size;
        return i;
    }

    public ProdConsBuffer(int size) {
        this.size = size;
        msg_array = new MessageData[size];
        this.num_free = size;
    }

    /**
     * Put the message m in the buffer
     **/
    @Override
    public void put(Message m, int n) throws InterruptedException {
        MessageData mydata;
        synchronized (this) {
            while (num_free < n) {
                wait();
            }
            mydata = new MessageData(m);
            for (int i = 0; i < n; i++) {
                this.msg_array[getInIndex()] = mydata;
                this.num_free--; // free space in array
                this.num_stored++;
                this.total_putted++;
            }
            notifyAll();
        }
        mydata.attenteDansPut(n);
    }

    /**
     * Retrieve a message from the buffer,
     * following a FIFO order (if M1 was put before M2, M1
     * is retrieved before M2)
     **/
    @Override
    public Message get() throws InterruptedException {
        MessageData mydata;
        Message m;
        synchronized (this) {
            while (num_stored == 0) {
                wait();
            }
            mydata = this.msg_array[getOutIndex()];
            m = mydata.getMessage();
            mydata.removeExemplaire();
            this.num_stored--;
            this.num_free++;
            notifyAll();
        }
        mydata.attenteDansGet();
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

}

class MessageData {
    Message msg;
    Semaphore onPut = new Semaphore(0);
    Semaphore onGet = new Semaphore(0);

    public MessageData(Message msg) {
        this.msg = msg;
    }

    public Message getMessage() {
        return this.msg;
    }

    public void removeExemplaire() {
        this.onPut.release();
    }

    public void attenteDansPut(int num_messages) {
        try {
            this.onPut.acquire(num_messages);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        this.onGet.release(num_messages);
    }

    public void attenteDansGet() {
        try {
            this.onGet.acquire();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
