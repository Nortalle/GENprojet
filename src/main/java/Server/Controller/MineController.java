package Server.Controller;

import Game.*;
import Server.Server;
import Server.DataBase;
import Utils.WagonStats;

import java.util.ArrayList;

public class MineController implements Runnable {

    private ArrayList<WagonMining> wagonMining = new ArrayList<WagonMining>();
    private ArrayList<Long> ETMs = new ArrayList<Long>();

    private final int INTERVAL_MS = 1000;

    public void run() {

        long start;
        long diff;
        while (true) {
            start = System.currentTimeMillis();
            do {
                diff = System.currentTimeMillis() - start;
            } while (diff < INTERVAL_MS);

            for(int i = 0; i < wagonMining.size(); ++i) {
                WagonMining wagon = wagonMining.get(i);
                String username = Server.getInstance().getDataBase().getUsernameByWagonId(wagon.getWagon().getId());
                long ETM = ETMs.get(i);
                ETMs.set(i, --ETM);
                if(ETM == 0) {
                    ETMs.set(i, (long)WagonStats.getMiningTime(wagon.getWagon()));
                    Resources r = new Resources(Server.getInstance().getDataBase().getPlayerResources(username));
                    Server.getInstance().getDataBase().setPlayerResources(username, r.toArray());
                }
            }

            //Minage par secondes
            for(WagonMining wagon : wagonMining){
                Mine mine = wagon.getCurrentMine();



            }

            //Minage par niveau de wagon, avec des interval différents
            for(int i = 0; i < wagonMining.size(); ++i){
                WagonMining wagon = wagonMining.get(i);
                long ETM = ETMs.get(i);
            }
        }
    }

    // TODO
    public boolean tryMine(String username, String wagonLine, String mineLine) {
        DataBase db = Server.getInstance().getDataBase();
        Wagon wagon = db.getWagon(Integer.valueOf(wagonLine));
        Mine mine = db.getMine(Integer.valueOf(mineLine));
        Train train = db.getTrain(username);
        if(wagon == null || mine == null) return false;//if wagon and mine exist
        if(train.getTrainStationETA() > 0) return false;//if train is arrived
        if(train.getTrainStation().getId() != mine.getPlace()) return false;//if mine is at curr station of train
        addWagon(new WagonMining(wagon, mine));
        return true;
    }

    public void addWagon(WagonMining wagonMining){
        this.wagonMining.add(wagonMining);
        ETMs.add((long)WagonStats.getMiningTime(wagonMining.getWagon()));
    }

    public ArrayList<WagonMining> getPlayerWagonMining(String username) {
        ArrayList<WagonMining> result = new ArrayList<>();
        for(WagonMining wm : wagonMining) {
            if(Server.getInstance().getDataBase().getUsernameByWagonId(wm.getWagon().getId()).equals(username)) {
                result.add(wm);
            }
        }
        return result;
    }
}
