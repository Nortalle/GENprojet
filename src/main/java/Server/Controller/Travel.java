package Server.Controller;

import Game.Train;
import Game.TrainStation;
import Server.Server;
import Server.DataBase;

import javax.swing.*;
import javax.swing.Timer;
import java.awt.event.ActionListener;
import java.util.*;

public class Travel {
    private HashMap<String, Integer> map = new HashMap<String, Integer>();// username, time remaining
    private final int INTERVAL_MS = 1000;
    private long start;

    private Timer timer ;

    public Travel() {


        start = System.currentTimeMillis();

        new java.util.Timer().scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                Iterator it = map.entrySet().iterator();
                while(it.hasNext()) {
                    Map.Entry pair = (Map.Entry)it.next();
                    pair.setValue(Math.max((int)(((Integer) pair.getValue()) - ((System.currentTimeMillis() - start) / 1000)), 0));// only seconds

                    start = System.currentTimeMillis();
                }
            }
        }, INTERVAL_MS, INTERVAL_MS);
    }

    public void addTrain(String username, int ETA) {
        map.put(username, ETA);
    }

    public Integer getETA(String username) {
        return map.get(username);
    }

    public boolean ctrlChangeStation(String username, String newStation) {
        int newTsId = Integer.valueOf(newStation);
        DataBase db = Server.getInstance().getDataBase();
        Train train = db.getTrain(username);
        TrainStation newTrainStation = db.getTrainStation(newTsId);

        int eta = 0;
        Integer realETA = Server.getInstance().getTravelController().getETA(username);
        if(realETA != null) eta = realETA;
        if(eta == 0) {
            if(newTrainStation.getSizeOfPlatforms() >= train.getSize()) {
                if(db.getNbUsedPlatforms(newTsId) < newTrainStation.getNbOfPlatforms())
                    if(db.sendTrainToNewStation(username, newTrainStation.getId())) {
                        return true;
                    }
            }
        }
        return false;
    }
}
