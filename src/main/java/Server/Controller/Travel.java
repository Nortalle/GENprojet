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
    private HashMap<String, int[]> map = new HashMap<>();// username, {time remaining, total time}
    private final int INTERVAL_MS = 1000;

    public Travel() {
        new java.util.Timer().scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                ArrayList<String> toRemove = new ArrayList<>();
                Iterator it = map.entrySet().iterator();
                while(it.hasNext()) {
                    Map.Entry pair = (Map.Entry)it.next();
                    int newValue[] = (int[]) pair.getValue();
                    if(--newValue[0] == 0) toRemove.add((String) pair.getKey());
                    pair.setValue(newValue);
                }
                for(String s : toRemove) map.remove(s);
            }
        }, INTERVAL_MS, INTERVAL_MS);
    }

    public void addTrain(String username, int ETA) {
        int[] eta = {ETA, ETA};
        map.put(username, eta);
    }


    public void removeTrain(String username) {
        map.remove(username);
    }


    public int[] getETA(String username) {
        int[] eta = {0, 0};
        if(map.containsKey(username)) eta = map.get(username);
        return eta;
    }

    public boolean ctrlChangeStation(String username, String newStation) {
        int newTsId = Integer.valueOf(newStation);
        DataBase db = Server.getInstance().getDataBase();
        Train train = db.getTrain(username);
        TrainStation newTrainStation = db.getTrainStation(newTsId);

        if(getETA(username)[0] == 0) {
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
