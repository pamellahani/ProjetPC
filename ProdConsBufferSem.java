package ProjetPC;

import java.util.concurrent.Semaphore;

/* 
 * Objectif 3 :
 * Solution avec des semaphores
 */

public class ProdConsBufferSem implements IProdConsBuffer {
    private Message[] msgArray;

    // taille du buffer
    private int size;

    // le nombre total des messages mit dedans
    private int totalPutted = 0;

    // les semaphores
    private Semaphore notFull;
    private Semaphore notEmpty;
    private Semaphore mutex;

    // les indexes in et out
    int index_in = 0;
    int index_out = 0;

    public ProdConsBufferSem(int size) {
        this.size = size;
        msgArray = new Message[size];
        notFull = new Semaphore(size);
        notEmpty = new Semaphore(0);
        mutex = new Semaphore(1);
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
        notFull.acquire(); // On attend qu'un place soit disponible pour put
        mutex.acquire(); // entrer dans la section critique

        msgArray[getInIndex()] = m;
        totalPutted++;

        mutex.release(); // sortie de la section critique
        notEmpty.release(); // on indique qu'un message est disponible
    }

    @Override
    public Message get() throws InterruptedException {
        notEmpty.acquire(); // on attend qu'un message soit disponible pour retirer
        mutex.acquire(); // entrer dans la section critique

        Message m = msgArray[getOutIndex()];

        mutex.release(); // sortie de la section critique
        notFull.release(); // on indique qu'une place est disponible

        return m;
    }

    @Override
    public int nmsg() {
        return notEmpty.availablePermits();
    }

    @Override
    public int totmsg() {
        return totalPutted;
    }
}
