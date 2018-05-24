package Game;

import Utils.WagonStats;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class MiningWagonTest {
    private static WagonMining wm;
    private static Wagon wagon;
    private static Mine mine;
    private static int wagonId;
    private static int mineId;

    @BeforeAll
    public static void setUpBeforeAll() {
        mineId = 1;
        wagonId = 1;
        mine = new Mine(mineId, 2, 1000, 1);
        wagon = new Wagon();
        wm = new WagonMining(wagon, mine);
    }

    @BeforeEach
    public  void setUpBeforeEach(){
        System.out.println("---");
    }

    @Test
    void jsonCurrentMine() {
        WagonMining newWagon = new WagonMining(wm.toJSON());
        assertEquals(mine.getResource(), newWagon.getCurrentMine().getResource());
    }
}
