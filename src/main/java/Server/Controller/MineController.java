package Server.Controller;

import Game.Mine;
import Game.WagonMining;
import Game.Resources;
import Server.Server;
import Utils.WagonStats;

import java.util.ArrayList;

public class MineController implements Runnable {

    private ArrayList<WagonMining> wagons = new ArrayList<WagonMining>();
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

            for(int i = 0; i < wagons.size(); ++i) {
                WagonMining wagon = wagons.get(i);
                String username = Server.getInstance().getDataBase().getUsernameByWagonId(wagon.getId());
                long ETM = ETMs.get(i);
                ETMs.set(i, --ETM);
                if(ETM == 0) {
                    ETMs.set(i, (long)WagonStats.getMiningTime(wagon.getTypeID(), wagon.getLevel()));
                    Resources r = new Resources(Server.getInstance().getDataBase().getPlayerResources(username));
                    Server.getInstance().getDataBase().setPlayerResources(username, r.toArray());
                }
            }

            //Minage par secondes
            for(WagonMining wagon : wagons){
                Mine mine = wagon.getCurrentMine();



            }

            //Minage par niveau de wagon, avec des interval différents
            for(int i = 0; i < wagons.size(); ++i){
                WagonMining wagon = wagons.get(i);
                long ETM = ETMs.get(i);
            }
        }
    }

    // TODO
    public boolean tryMine(String username, String wagonLine, String mineLine) {
        /*DataBase db = Server.getInstance().getDataBase();
        WagonMining wagon = db.getWagon(Integer.valueOf(wagonLine));
        Mine mine = db.getMine(Integer.valueOf(mineLine));
        Train train = db.getTrain(username);
        if(train.getTrainStationETA() > 0) return false;//if arrived
        if(train.getTrainStation().getId() != mine.getPlace()) return false;//if mine is at curr station of train
        wagon.setCurrentMine(mine);*/
        return true;
    }

    public void addWagon(String username, WagonMining wagon){
        wagons.add(wagon);
        ETMs.add((long)WagonStats.getMiningTime(wagon.getTypeID(), wagon.getLevel()));
    }
}
