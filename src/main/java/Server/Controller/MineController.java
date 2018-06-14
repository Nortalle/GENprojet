package Server.Controller;

import Game.*;
import Server.Server;
import Server.DataBase;
import Utils.ReadWriteLock;
import Utils.ResourceAmount;
import Utils.WagonStats;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Optional;
import java.util.TimerTask;
import java.util.stream.Collectors;

public class MineController {

    private ArrayList<WagonMining> wagonMining = new ArrayList<>();

    private final int INTERVAL_MS = 1000;
    private ReadWriteLock lock = new ReadWriteLock();


    public MineController() {
        new java.util.Timer().scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                DataBase db = Server.getInstance().getDataBase();
                try {
                    lock.lockRead();
                    for (WagonMining wm : wagonMining) {
                        String username = db.getUsernameByWagonId(wm.getWagon().getId()).orElse("");
                        // TODO TEST IF TRAIN IS STILL AT STATION WHERE MINE IS
                        int miningAmount = WagonStats.getMiningAmount(wm.getWagon());
                        miningAmount = -db.canChangeMineAmount(wm.getCurrentMine().getId(), -miningAmount);
                        miningAmount = db.canUpdatePlayerObjects(username, miningAmount);
                        if (miningAmount != 0) {
                            if (db.changeMineAmount(wm.getCurrentMine().getId(), -miningAmount)) {
                                if (db.updatePlayerObjects(username, wm.getCurrentMine().getResource(), miningAmount)) {
                                    // SUCCESS
                                }
                            }
                        }
                    }
                    lock.unlockRead();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }, INTERVAL_MS, INTERVAL_MS);
    }

    public boolean tryMine(String username, String wagonLine, String mineLine) {
        DataBase db = Server.getInstance().getDataBase();
        Train train = db.getTrain(username).orElse(new Train());
        Optional<Wagon> optionalWagon = db.getWagon(Integer.valueOf(wagonLine));
        Optional<Mine> optionalMine = db.getMine(Integer.valueOf(mineLine));
        if(!optionalWagon.isPresent() || !optionalMine.isPresent()) return false;//if wagon and mine exist
        Wagon wagon = optionalWagon.get();
        Mine mine = optionalMine.get();
        if(!WagonStats.canMine(wagon, mine)) return false;//if wagon can mine
        if(train.getTrainStationETA() > 0) return false;//if train is arrived
        if(train.getTrainStation().getId() != mine.getPlace()) return false;//if mine is at curr station of train
        addWagon(new WagonMining(wagon, mine));
        return true;
    }

    public void addWagon(WagonMining wm){
        boolean found = false;
        int i;
        try {
            lock.lockRead();
            for (i = 0; i < wagonMining.size(); i++) {
                if (wagonMining.get(i).getWagon().getId() == wm.getWagon().getId()) {
                    found = true;
                    break;
                }
            }
            lock.unlockRead();
            lock.lockWrite();
            if (found) {
                wagonMining.set(i, wm);
            } else {
                wagonMining.add(wm);
            }
            lock.unlockWrite();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    public boolean removeWagon(String wagonLine) {
        return removeWagon(Integer.valueOf(wagonLine));
    }

    public boolean removeWagon(int id) {// need tests
        try {
            lock.lockWrite();
            for (int i = 0; i < wagonMining.size(); i++) {
                if (wagonMining.get(i).getWagon().getId() == id) {
                    wagonMining.remove(i);
                    lock.unlockWrite();
                    return true;
                }
            }
            lock.unlockWrite();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return false;
    }

    public ArrayList<WagonMining> getPlayerWagonMining(String username) {
        ArrayList<WagonMining> result = new ArrayList<>();
        try {
            lock.lockRead();
            for (WagonMining wm : wagonMining) {
                if (Server.getInstance().getDataBase().getUsernameByWagonId(wm.getWagon().getId()).orElse("").equals(username)) {
                    Server.getInstance().getDataBase().getMine(wm.getCurrentMine().getId()).ifPresent(wm::setCurrentMine);
                    result.add(wm);
                }
            }
            lock.unlockRead();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return result;
        // TODO TEST AND USE STREAM
        /*return wagonMining.stream()
                .filter(Comparator.comparing(wm -> Server.getInstance().getDataBase().getUsernameByWagonId(wm).orElse("").equals(username)))
                .collect(Collectors.toCollection(ArrayList::new));*/
    }
}
