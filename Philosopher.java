/**
 * Philosopher.java
 *
 * This class represents each philosopher thread.
 * Philosophers alternate between eating and thinking.
 *
 */


public class Philosopher implements Runnable
 {
    private int num;
    DiningServerImpl diningControls;
    long time;
 
 
    @Override
    public void run() {
        time = System.currentTimeMillis();
        while (true){
            //eat
            diningControls.takeForks(num);
 
            //for time
            try {
                time = diningControls.threadSleep(num);
                System.out.println("Philosopher " + num + " took " + time + "ms eating");
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
 
            //thinking time
            diningControls.returnForks(num);
        }
    }
    public void setNum(int num) {
        this.num = num;
    }
    public void setDiningControls(DiningServerImpl diningControls) {
        this.diningControls = diningControls;
    }
 
}