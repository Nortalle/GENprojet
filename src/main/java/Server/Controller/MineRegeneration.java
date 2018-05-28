package Server.Controller;

import Game.Mine;
import Server.Server;
import Server.DataBase;

import java.util.*;

public class MineRegeneration {

    //Contient toutes les mines du jeux
    private ArrayList<Mine> mines;

    private int MAX_AMOUNT = 1000;
    private int AMOUNT_TO_ADD = 10;
    private int INTERVAL_MS = 1000 * 60;


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
                for (Mine mine : mines) {
                    int miningAmount = AMOUNT_TO_ADD;
                    miningAmount = db.canChangeMineAmount(mine.getId(), miningAmount);
                    if(db.changeMineAmount(mine.getId(), miningAmount)) {
                        // all good
                    } else {
                        System.out.println("mine : " + mine.getId() + " might not exist and must be removed from regen ctrl");
                    }
                }
            }
        }, INTERVAL_MS, INTERVAL_MS);
    }

    public void addMine(Mine mine_to_add) {
        this.mines.add(mine_to_add);
    }

    public ArrayList<Mine> getMines() {
        return mines;
    }
}
