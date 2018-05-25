package Server.Controller;

import Game.*;
import Server.Server;
import Server.DataBase;
import Utils.WagonStats;

import java.util.ArrayList;
import java.util.TimerTask;

public class MineController {

    private ArrayList<WagonMining> wagonMining = new ArrayList<>();
    private ArrayList<Long> ETMs = new ArrayList<>();

    private final int INTERVAL_MS = 1000;


    public MineController() {
        new java.util.Timer().scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                for(int i = 0; i < wagonMining.size(); ++i) {
                    WagonMining wm = wagonMining.get(i);
                    String username = Server.getInstance().getDataBase().getUsernameByWagonId(wm.getWagon().getId());
                    long ETM = ETMs.get(i);
                    ETMs.set(i, --ETM);
                    if(ETM == 0) {
                        ETMs.set(i, (long)WagonStats.getMiningTime(wm.getWagon()));
                        if(Server.getInstance().getDataBase().changeMineAmount(wm.getCurrentMine().getId(), -1)) {
                            Resources r = new Resources(Server.getInstance().getDataBase().getPlayerResources(username));
                            int newResources[] = r.toArray();
                            newResources[wm.getCurrentMine().getResource()]++;
                            Server.getInstance().getDataBase().setPlayerResources(username, newResources);
                        }
                    }
                }
            }
        }, INTERVAL_MS, INTERVAL_MS);
    }

    public boolean tryMine(String username, String wagonLine, String mineLine) {
        DataBase db = Server.getInstance().getDataBase();
        Wagon wagon = db.getWagon(Integer.valueOf(wagonLine));
        Mine mine = db.getMine(Integer.valueOf(mineLine));
        Train train = db.getTrain(username);
        if(wagon == null || mine == null) return false;//if wagon and mine exist
        if(wagon.getTypeID() != WagonStats.DRILL_ID) return false;//if wagon can mine // TODO
        if(train.getTrainStationETA() > 0) return false;//if train is arrived
        if(train.getTrainStation().getId() != mine.getPlace()) return false;//if mine is at curr station of train
        addWagon(new WagonMining(wagon, mine));
        return true;
    }

    public void addWagon(WagonMining wm){
        boolean found = false;
        int i;
        for(i = 0; i < wagonMining.size(); i++) {
            if(wagonMining.get(i).getWagon().getId() == wm.getWagon().getId()) {
                found = true;
                break;
            }
        }

        if(found) {
            wagonMining.set(i,wm);
            ETMs.set(i, (long)WagonStats.getMiningTime(wm.getWagon()));
        } else {
            wagonMining.add(wm);
            ETMs.add((long)WagonStats.getMiningTime(wm.getWagon()));
        }

    }

    public ArrayList<WagonMining> getPlayerWagonMining(String username) {
        ArrayList<WagonMining> result = new ArrayList<>();
        //TODO UPDATER LA MINE ET LE WAGON AVANT DE L'ENVOYER
        for(WagonMining wm : wagonMining) {

            if(Server.getInstance().getDataBase().getUsernameByWagonId(wm.getWagon().getId()).equals(username)) {
                result.add(wm);
            }
        }
        return result;
    }
}
