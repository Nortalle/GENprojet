package Server.Controller;

import Game.*;
import Server.Server;
import Server.DataBase;
import Utils.ResourceAmount;
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
                DataBase db = Server.getInstance().getDataBase();
                for(int i = 0; i < wagonMining.size(); ++i) {
                    WagonMining wm = wagonMining.get(i);
                    String username = db.getUsernameByWagonId(wm.getWagon().getId());
                    long ETM = ETMs.get(i);
                    ETMs.set(i, --ETM);
                    if(ETM == 0) {
                        // TODO TEST IF TRAIN STILL AT STATION WHERE MINE IS
                        ETMs.set(i, (long)WagonStats.getMiningTime(wm.getWagon()));
                        int currentCargoUsed = 0;
                        for(ResourceAmount ra : db.getPlayerObjects(username)) currentCargoUsed += ra.getQuantity();
                        if(currentCargoUsed < WagonStats.getMaxCapacity(db.getTrain(db.getUsernameByWagonId(wm.getWagon().getId())))) {// because one mine 1 by 1
                            if (db.changeMineAmount(wm.getCurrentMine().getId(), -1)) {
                                Resources r = new Resources(db.getPlayerResources(username));
                                int newResources[] = r.toArray();
                                newResources[wm.getCurrentMine().getResource()]++;
                                db.setPlayerResources(username, newResources);
                                // in ObjetsParJoueur
                                db.addPlayerObjects(username, wm.getCurrentMine().getResource(), 1);
                            }
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
        if(!WagonStats.canMine(wagon, mine)) return false;//if wagon can mine
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

    public boolean removeWagon(String wagonLine) {
        return removeWagon(Integer.valueOf(wagonLine));
    }

    public boolean removeWagon(int id) {// need tests
        for(int i = 0; i < wagonMining.size(); i++) {
            if(wagonMining.get(i).getWagon().getId() == id) {
                wagonMining.remove(i);
                ETMs.remove(i);
                return true;
            }
        }
        return false;
    }

    public ArrayList<WagonMining> getPlayerWagonMining(String username) {
        ArrayList<WagonMining> result = new ArrayList<>();
        for(WagonMining wm : wagonMining) {
            if(Server.getInstance().getDataBase().getUsernameByWagonId(wm.getWagon().getId()).equals(username)) {
                wm.setCurrentMine(Server.getInstance().getDataBase().getMine(wm.getCurrentMine().getId()));
                result.add(wm);
            }
        }
        return result;
    }
}
