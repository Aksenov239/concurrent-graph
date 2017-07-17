package test.workers;

/**
 * Created by vaksenov on 17.07.2017.
 */
public abstract class Worker implements Runnable {
    public volatile int numAdd;
    public volatile int numRemove;
    public volatile int numConnected;

    public abstract void stop();
}
