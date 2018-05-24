package Server.Controller;

import Game.Mine;
import Server.Server;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;

public class MineRegeneration implements Runnable {



    //Contient toutes les mines du jeux
    private ArrayList<Mine> mines = new ArrayList<Mine>();

    private int MAX_AMOUNT = 1000;
    private int AMOUNT_TO_ADD = 1;
    private int INTERVAL_MS = 1000;

    public MineRegeneration() {
        this.mines = Server.getInstance().getDataBase().getAllMines();
    }

    public MineRegeneration(ArrayList<Mine> mines) {
        this.mines = mines;
    }

    public void run() {

        long start = System.currentTimeMillis();
        while(true) {
            start = System.currentTimeMillis();
            long diff;
            do {
                diff = System.currentTimeMillis() - start;
            }while(diff < INTERVAL_MS);
                for(Mine mine : mines){

                    int current_amount = mine.getAmount();

                    //Si la mine n'est pas pleine, on la régénère
                    if(current_amount < MAX_AMOUNT){
                        current_amount += AMOUNT_TO_ADD;

                        if(current_amount > MAX_AMOUNT){
                            current_amount = MAX_AMOUNT;
                        }

                        mine.setAmount(current_amount);
                    }
                }
            }

    }

    public void addMine(Mine mine_to_add){
        this.mines.add(mine_to_add);
    }

    public ArrayList<Mine> getMines() {
        return mines;
    }
}
