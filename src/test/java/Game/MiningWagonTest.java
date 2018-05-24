package Game;

import Utils.WagonStats;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class MiningWagonTest {
    private static MiningWagon wagon;
    private static Mine mine;
    private static int wagonId;
    private static int mineId;

    @BeforeAll
    public static void setUpBeforeAll() {
        mineId = 1;
        wagonId = 1;
        mine = new Mine(mineId, "thing", 1000, 1);
        wagon = new MiningWagon(wagonId, 1000, 1, WagonStats.DRILL_ID, mine);
    }

    @BeforeEach
    public  void setUpBeforeEach(){
        System.out.println("---");
    }

    @Test
    void jsonSuperWagon() {
        MiningWagon newWagon = new MiningWagon(wagon.toJSON());
        assertEquals(wagonId, newWagon.getId());
    }

    @Test
    void jsonCurrentMine() {
        MiningWagon newWagon = new MiningWagon(wagon.toJSON());
        assertEquals(mine.getResource(), newWagon.getCurrentMine().getResource());
    }

    @Test
    void jsonList() {
        ArrayList<Wagon> wagons = new ArrayList<Wagon>();
        wagons.add(new Wagon());
        wagons.add(new MiningWagon());
        ArrayList<Wagon> newWagons = Wagon.listFromJSON(Wagon.listToJSON(wagons));
        System.out.println(newWagons.get(0));
        for(int i = 0; i < wagons.size(); i++) {
            //System.out.println(wagons.get(i).getClass() + " " + wagons.get(i) + " -- " + newWagons.get(i).getClass() + " " + newWagons.get(i));
        }
    }
}
