package Game;

import Utils.JsonUtility;
import com.google.gson.JsonArray;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class MinesTest {
    @BeforeAll
    public static void setUpBeforeAll() {

    }

    @BeforeEach
    public  void setUpBeforeEach(){
        System.out.println("---");
    }

    @Test
    void listFromJSON() {
        int id = 0;
        int amount = 0;
        int placeId = 0;
        int s1 = 1;
        int s2 = 2;
        int s3 = 3;
        ArrayList<Mine> mines = new ArrayList<>();
        mines.add(new Mine(id, s1, amount, placeId));
        mines.add(new Mine(id, s2, amount, placeId));
        mines.add(new Mine(id, s3, amount, placeId));
        String json = JsonUtility.toJson(JsonUtility.listToJson(mines, mine -> mine.toJson()));
        ArrayList<Mine> newMines = JsonUtility.listFromJson((JsonArray) JsonUtility.fromJson(json), mine -> new Mine(mine));
        for(Mine m : newMines) System.out.println("- " + m.getResource());
        assertEquals(mines.get(1).getResource(), newMines.get(1).getResource());
    }
}
