package Server.Controller;

import Game.Mine;
import Server.Server;
import Server.DataBase;
import Utils.ReadWriteLock;

import java.util.*;

public class MineRegeneration {

    //Contient toutes les mines du jeux
    private ArrayList<Mine> mines;

    private int INTERVAL_MS = 1000 * 60;
    private ReadWriteLock lock = new ReadWriteLock();


    public MineRegeneration() {
        this.mines = Server.getInstance().getDataBase().getAllMines();
        init();
    }

    public MineRegeneration(ArrayList<Mine> mines) {
        this.mines = mines;
        init();
    }

    /**
     * Fait en sorte que tous les INTERVAL_MS, les mines se régénère de AMOUNT_TO_ADD
     */
    private void init() {
        new Timer().scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                DataBase db = Server.getInstance().getDataBase();
                try {
                    lock.lockRead();
                    for (Mine mine : mines) {
                        int miningAmount = mine.getRegen();
                        miningAmount = db.canChangeMineAmount(mine.getId(), miningAmount);
                        if (db.changeMineAmount(mine.getId(), miningAmount)) {
                            // all good
                        } else {
                            System.out.println("mine : " + mine.getId() + " might not exist and must be removed from regen ctrl");
                        }
                    }
                    lock.unlockRead();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }, INTERVAL_MS, INTERVAL_MS);
    }

    public void addMine(Mine mine_to_add) {
        try {
            lock.lockWrite();
            this.mines.add(mine_to_add);
            lock.unlockWrite();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public ArrayList<Mine> getMines() {
        return mines;
    }
}
