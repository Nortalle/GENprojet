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
        mines.add(new Mine(id, s1, amount, 1000, 10, placeId));
        mines.add(new Mine(id, s2, amount, 1000, 10, placeId));
        mines.add(new Mine(id, s3, amount, 1000, 10, placeId));
        String json = JsonUtility.toJson(JsonUtility.listToJson(mines, Mine::toJson));
        ArrayList<Mine> newMines = JsonUtility.listFromJson((JsonArray) JsonUtility.fromJson(json), Mine::new);
        for(Mine m : newMines) System.out.println("- " + m.getResource());
        assertEquals(mines.get(1).getResource(), newMines.get(1).getResource());
    }

    @Test
    void testSimpleMine() {
        SimpleMine mine1 = new SimpleMine(153, 45);
        SimpleMine mine3 = new SimpleMine(453, 4);
        SimpleMine mine2 = new SimpleMine(1, 455);

        SimpleMine newMine1 = new SimpleMine();
        SimpleMine newMine3 = new SimpleMine();
        SimpleMine newMine2 = new SimpleMine();

        newMine1.fromSimpleFormat(mine1.toSimpleFormat());
        newMine2.fromSimpleFormat(mine2.toSimpleFormat());
        newMine3.fromSimpleFormat(mine3.toSimpleFormat());

        assertEquals(mine1.getId(), newMine1.getId());
        assertEquals(mine1.getResource(), newMine1.getResource());
        assertEquals(mine2.getId(), newMine2.getId());
        assertEquals(mine2.getResource(), newMine2.getResource());
        assertEquals(mine3.getId(), newMine3.getId());
        assertEquals(mine3.getResource(), newMine3.getResource());
    }
}
