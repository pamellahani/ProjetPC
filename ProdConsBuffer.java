package ProjetPC;

/*

+----------------+------------+------------------------+----------------+
| Methode        | Pre-action | Garde                  | Post-action    |  
+----------------+------------+------------------------+----------------+
| put(Message M) | -          | nfree > 0              | nfree --       |
|                |            |                        |                |
|                |            | num_stored == bufferSz | num_stored ++  |
+----------------+------------+------------------------+----------------+
| get()          | -          | num_stored = 0         | nfree ++       |
|                |            |                        |                |
|                |            |                        | num_stored --  |
+----------------+------------+------------------------+----------------+

 */

public class ProdConsBuffer implements IProdConsBuffer{
    private int bufferSz;
    private Message[] buffer;
    private int nfree; 
    private int nmsg;  //=num_stored
    private int totmsg ;; 



    public ProdConsBuffer(int size){
        bufferSz = size;
        buffer = new Message[size];
        nfree = size ; 
        nmsg = 0;
        totmsg = 0; 
    }

    //put a message into the buffer
    public void put(Message m) throws InterruptedException{
        while(nfree == 0){
            wait();
        }
        buffer[nfree] = m;
        nfree--;
        nmsg++;
        totmsg++;
        notifyAll();
    }

    //get a message from the buffer following a FIFO order 
    public Message get() throws InterruptedException{
        while(nmsg == 0){
            wait();
        }
        Message m = buffer[nfree];
        nfree++;
        nmsg--;
        notifyAll();
        return m;
    }

    public int nmsg(){
        return nmsg;
    }   

    public int totmsg(){
        return totmsg;
    }

}
