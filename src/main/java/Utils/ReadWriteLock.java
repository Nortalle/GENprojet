package Utils;

public class ReadWriteLock {
    private int nbReader = 0;
    private int nbWriter = 0;
    private int nbWriterRequest = 0;
    private boolean debug = false;

    public ReadWriteLock() {}

    public ReadWriteLock(boolean debug) {
        this.debug = debug;
    }

    public synchronized void lockRead() throws InterruptedException {
        if(debug) System.out.println("lr:" + nbReader + "," + nbWriter + "," + nbWriterRequest);
        while (nbWriter > 0 || nbWriterRequest > 0) wait();
        nbReader++;
    }

    public synchronized void unlockRead() {
        if(debug) System.out.println("ur:" + nbReader + "," + nbWriter + "," + nbWriterRequest);
        nbReader--;
        notifyAll();
    }

    public synchronized void lockWrite() throws InterruptedException {
        if(debug) System.out.println("lw:" + nbReader + "," + nbWriter + "," + nbWriterRequest);
        nbWriterRequest++;
        while (nbWriter > 0 || nbReader > 0) wait();
        nbWriterRequest--;
        nbWriter++;
    }

    public synchronized void unlockWrite() {
        if(debug) System.out.println("uw:" + nbReader + "," + nbWriter + "," + nbWriterRequest);
        nbWriter--;
        notifyAll();
    }
}

