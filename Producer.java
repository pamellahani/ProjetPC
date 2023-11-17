public class Producer extends Thread{

    public Producer(){
        this.start();
    }

    public void run () {
        while (true) {
            try {
                Thread.sleep(1000);
                System.out.println("Producer");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
