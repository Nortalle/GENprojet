package Server.Controller;

import Game.Mine;
import Server.Server;

import java.util.*;

public class MineRegeneration {

    //Contient toutes les mines du jeux
    private ArrayList<Mine> mines = new ArrayList<Mine>();

    private int MAX_AMOUNT = 1000;
    private int AMOUNT_TO_ADD = 1;
    private int INTERVAL_MS = 1000;


    public MineRegeneration() {
        this.mines = Server.getInstance().getDataBase().getAllMines();
        init();
    }

    public MineRegeneration(ArrayList<Mine> mines) {
        this.mines = mines;
        init();
    }

    private void init() {
        new Timer().scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                for (Mine mine : mines) {

                    int current_amount = mine.getAmount();

                    //Si la mine n'est pas pleine, on la régénère
                    if (current_amount < MAX_AMOUNT) {
                        current_amount += AMOUNT_TO_ADD;

                        //Si la mine est trop pleine, on la met au MAX_AMOUNT
                        if (current_amount > MAX_AMOUNT) {
                            current_amount = MAX_AMOUNT;
                        }

                        mine.setAmount(current_amount);

                        //met à jour la base de donnée en même temps, ce n'est pas ce qu'on veut
                        //Server.getInstance().getDataBase().setMineAmount(mine.getId(), current_amount);
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
