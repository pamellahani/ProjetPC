public class Consumer extends Thread {
    
    public Consumer(){
        this.start();
    }

    public void run () {
        while (true) {
            try {
                Thread.sleep(1000);
                System.out.println("Consumer");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
