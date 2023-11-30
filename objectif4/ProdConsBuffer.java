package projetpc.objectif4;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import projetpc.objectif3.Message;

public class ProdConsBuffer implements IProdConsBuffer {

    int size; // array size and number of free slots
    Message msg_array[];
    final Condition notFull; 
    final Condition notEmpty; 
    final Lock lock; 

    // tracking variables for put and get
    int num_stored = 0; // same as currently stored messages

    // les indexes in et out
    int num_free = 0;
    int index_in = 0;
    int index_out = 0;

    // number of putted messages
    int total_putted = 0;

    public ProdConsBuffer(int size) {
        this.size = size;
        msg_array = new Message[size];
        this.num_free = size;
        this.lock = new ReentrantLock();
        this.notFull  = lock.newCondition(); 
        this.notEmpty = lock.newCondition(); 
    }

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

    // Adds a message to the buffer if there is space
    // otherwise waits until there is space
    @Override
    public void put(Message m) throws InterruptedException {
        lock.lock();
        try {
            while(num_free==0){
                notFull.await();
            }
            this.msg_array[getInIndex()] = m;
            this.num_free--;
            this.num_stored++;
            this.total_putted++;
            notEmpty.signal();
        } finally {
            lock.unlock();
        }
    }

    /**
     * Retrieve a message from the buffer,
     * following a FIFO order (if M1 was put before M2, M1
     * is retrieved before M2)
     **/
    @Override
    public Message get() throws InterruptedException{
        lock.lock();
        try {
            while (num_stored==0) {
                notEmpty.await();
            }
            Message m = this.msg_array[getOutIndex()];
            if (getOutIndex() == this.size){this.index_out = 0;} // reset index_out (FIFO)
            this.num_free++;
            this.num_stored--;
            notFull.signal();
            return m;
        } finally {
            lock.unlock();
        }
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


