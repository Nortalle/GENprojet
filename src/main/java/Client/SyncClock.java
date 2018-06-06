package Client;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class SyncClock {


    private Timer timer;
    private int cycleTime = 1000;
    private int nbCyclesBetweenSync = 15;
    private long cyclesSinceLastSync = 0;
    private static SyncClock instance;
    private ArrayList<Updater> updaters;

    private SyncClock(){
        updaters = new ArrayList<>();
        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public synchronized void run() {
                if(cyclesSinceLastSync % nbCyclesBetweenSync == 0){
                    syncCycle();
                    cyclesSinceLastSync = 0;
                } else {
                    localCycle();
                }
                ++cyclesSinceLastSync;
            }
        }, cycleTime, cycleTime);
    }

    public static SyncClock getInstance() {
        if (instance == null) {
            instance = new SyncClock();
        }

        return instance;
    }


    public synchronized void addUpdater(Updater u){
        updaters.add(u);
        //u.sync();
    }

    public synchronized void removeUpdater(Updater u) {
        updaters.remove(u);
    }

    public synchronized void removeAllUpdaters() {
        updaters.clear();
    }

    private synchronized void localCycle(){
        for(Updater u : updaters){
            u.localUpdate();
        }
    }

    public synchronized void syncCycle(){
        for(Updater u : updaters){
            u.sync();
        }
    }
}
