package Server.Controller;

import Game.Mine;
import Game.MiningWagon;

import java.util.ArrayList;
import java.util.HashMap;

public class MineController implements Runnable {

    private ArrayList<MiningWagon> wagons = new ArrayList<MiningWagon>();
    private ArrayList<Long> ETMs = new ArrayList<Long>();
    private int wagonNumber = 0;

    private final int INTERVAL_MS = 1000;

    public void run() {

        long start;
        long diff;
        while (true) {
            start = System.currentTimeMillis();
            do {
                diff = System.currentTimeMillis() - start;
            } while (diff < INTERVAL_MS);

            //Minage par secondes
            for(MiningWagon wagon : wagons){
                Mine mine = wagon.getCurrentMine();



            }

            //Minage par niveau de wagon, avec des interval différents
            for(int i = 0; i < wagonNumber; ++i){
                MiningWagon wagon = wagons.get(i);
                long ETM = ETMs.get(i);
            }
        }
    }

    public void addWagon(MiningWagon wagon, long mining_time){
        wagons.add(wagon);
        ETMs.add(mining_time);
        wagonNumber++;
    }
}
