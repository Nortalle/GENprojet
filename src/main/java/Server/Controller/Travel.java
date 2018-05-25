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

    private Timer timer ;

    public Travel() {
        new java.util.Timer().scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                Iterator it = map.entrySet().iterator();
                while(it.hasNext()) {
                    Map.Entry pair = (Map.Entry)it.next();
                    //pair.setValue(Math.max((int)(((Integer) pair.getValue()) - ((System.currentTimeMillis() - start) / 1000)), 0));// only seconds
                    int newValue = (Integer) pair.getValue() - 1;
                    pair.setValue(Math.max(newValue, 0));// only seconds
                }
            }
        }, INTERVAL_MS, INTERVAL_MS);
    }

    public void addTrain(String username, int ETA) {
        map.put(username, ETA);
    }


    public void removeTrain(String username) {
        map.remove(username);
    }


    public int getETA(String username) {
        int eta = 0;
        Integer storedETA = map.get(username);
        if(storedETA != null) eta = storedETA;
        return eta;
    }

    public boolean ctrlChangeStation(String username, String newStation) {
        int newTsId = Integer.valueOf(newStation);
        DataBase db = Server.getInstance().getDataBase();
        Train train = db.getTrain(username);
        TrainStation newTrainStation = db.getTrainStation(newTsId);

        if(getETA(username) == 0) {
            if(newTrainStation.getSizeOfPlatforms() >= train.getSize()) {
                if(db.getNbUsedPlatforms(newTsId) < newTrainStation.getNbOfPlatforms()){
                    if(db.sendTrainToNewStation(username, newTrainStation.getId())) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
}
