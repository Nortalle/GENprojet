package Server.Controller;

import Game.Mine;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class MineRegenerationTest {
    private static MineRegeneration regeneration;
    private static ArrayList<Mine> mines = new ArrayList<Mine>();

    @BeforeAll
    public static void setUpBeforeAll() {

        Mine mine1 = new Mine(1, "eau", 30, 1);
        Mine mine2 = new Mine(2, "fer", 1000, 1);
        Mine mine3 = new Mine(3, "cuir", 900, 1);
        Mine mine4 = new Mine(4, "or", 0, 1);

        mines.add(mine1);
        mines.add(mine2);
        mines.add(mine3);
        mines.add(mine4);

        regeneration = new MineRegeneration(mines);
        new Thread(regeneration).start();
    }

    @BeforeEach
    public  void setUpBeforeEach(){
        System.out.println("---");
    }

    @Test
    public void doesMinesRegenerate(){
        long start = System.currentTimeMillis();
        while(System.currentTimeMillis() - start < 1500){

        }


        assertEquals(31, mines.get(0).getAmount());
        assertEquals(1000, mines.get(1).getAmount());
        assertEquals(901, mines.get(2).getAmount());
        assertEquals(1, mines.get(3).getAmount());
    }

}