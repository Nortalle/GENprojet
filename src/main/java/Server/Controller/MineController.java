package Server.Controller;

import Game.Mine;
import Game.MiningWagon;
import Game.Resources;
import Server.Server;
import Utils.WagonStats;

import java.util.ArrayList;
import java.util.HashMap;

public class MineController implements Runnable {

    private ArrayList<MiningWagon> wagons = new ArrayList<MiningWagon>();
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
                MiningWagon wagon = wagons.get(i);
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
            for(MiningWagon wagon : wagons){
                Mine mine = wagon.getCurrentMine();



            }

            //Minage par niveau de wagon, avec des interval différents
            for(int i = 0; i < wagons.size(); ++i){
                MiningWagon wagon = wagons.get(i);
                long ETM = ETMs.get(i);
            }
        }
    }

    public void addWagon(MiningWagon wagon){
        wagons.add(wagon);
        ETMs.add((long)WagonStats.getMiningTime(wagon.getTypeID(), wagon.getLevel()));
    }
}
