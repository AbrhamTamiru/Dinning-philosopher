/**
 * DiningServer.java
 *
 * This class contains the methods called by the  philosophers.
 *
 */

import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class DiningServerImpl implements DiningServer 
{
    // Define the three states for a philosopher
    enum PhilosopherState { THINKING, HUNGRY, EATING };
    
    // Create arrays to store philosopher states and conditions
    PhilosopherState[] philosopherStatus = new PhilosopherState[5];
    Condition[] philosopherConditions = new Condition[5];
    Lock lock = new ReentrantLock();

    public DiningServerImpl() {
        // Initialize philosopher conditions and set them to thinking
        for (int i = 0; i < 5; i++) {
            philosopherConditions[i] = lock.newCondition();
            philosopherStatus[i] = PhilosopherState.THINKING;
        }

        // Create philosopher threads and start them
        for (int i = 0; i < 5; i++) {
            Philosopher philosopher = new Philosopher();
            philosopher.setNum(i);
            philosopher.setDiningControls(this);
            System.out.println("Philosopher " + i + " is thinking.");
            new Thread(philosopher).start();
        }
    }

    // Check if it's possible for the philosopher to eat
    public void evaluateEat(int i) {
        if (philosopherStatus[(i + 4) % 5] != PhilosopherState.EATING &&
            philosopherStatus[i] == PhilosopherState.HUNGRY &&
            philosopherStatus[(i + 1) % 5] != PhilosopherState.EATING) {
            
            System.out.println("Forks are now with Philosopher " + i);
            philosopherStatus[i] = PhilosopherState.EATING;
            philosopherConditions[i].signal();
        }
    }

    @Override
    public void takeForks(int philNumber) {
        lock.lock();

        philosopherStatus[philNumber] = PhilosopherState.HUNGRY;
        evaluateEat(philNumber);

        if (philosopherStatus[philNumber] != PhilosopherState.EATING) {
            try {
                philosopherConditions[philNumber].await();
            } catch (InterruptedException e) {
                System.out.println("Philosopher " + philNumber + " is waiting for forks.");
            }
        }

        lock.unlock();
    }

    @Override
    public void returnForks(int philNumber) {
        lock.lock();

        System.out.println("Philosopher " + philNumber + " thinking.");
        philosopherStatus[philNumber] = PhilosopherState.THINKING;

        evaluateEat((philNumber + 4) % 5);
        evaluateEat((philNumber + 1) % 5);

        lock.unlock();
    }

    // Simulate thinking time for the philosopher
    public int threadSleep(int philNumber) throws InterruptedException {
        int randomNum = ThreadLocalRandom.current().nextInt(1000, 3000 + 1);
        Thread.sleep(randomNum);
        return randomNum;
    }
}
