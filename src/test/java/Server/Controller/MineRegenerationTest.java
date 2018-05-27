package Server.Controller;

import Game.Mine;
import Server.Server;
import Utils.Ressource;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.SQLOutput;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class MineRegenerationTest {
    private static ArrayList<Mine> mines = new ArrayList<>();

    @BeforeAll
    public static void setUpBeforeAll() {

        Mine mine1 = new Mine(1, 1, 30, 1);
        Mine mine2 = new Mine(2, 2, 1000, 1);
        Mine mine3 = new Mine(3, 3, 900, 1);
        Mine mine4 = new Mine(4, 4, 0, 1);

        mines.add(mine1);
        mines.add(mine2);
        mines.add(mine3);
        mines.add(mine4);
    }

    @BeforeEach
    public  void setUpBeforeEach(){
        System.out.println("---");
    }

    @Test
    void addMines() {
        Server.getInstance().init();
        int nbrMines = Server.getInstance().getRegenerationController().getMines().size();
        Server.getInstance().getRegenerationController().addMine(new Mine(-1, Ressource.Type.WOOD_LOG.ordinal(), 500, 1));
        ArrayList<Mine> updatedMines = Server.getInstance().getRegenerationController().getMines();
        assertEquals(nbrMines + 1, updatedMines.size());
    }
}