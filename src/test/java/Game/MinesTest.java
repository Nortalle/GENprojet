package Game;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

public class MinesTest {
    @BeforeAll
    public static void setUpBeforeAll() {
        System.out.println("--- BeforeAll ---");
    }

    @Test
    void listFromJSON() {
        int id = 0;
        int amount = 0;
        int placeId = 0;
        String s1 = "s1";
        String s2 = "s2";
        String s3 = "s3";
        ArrayList<Mine> mines = new ArrayList<Mine>();
        mines.add(new Mine(id, s1, amount, placeId));
        mines.add(new Mine(id, s2, amount, placeId));
        mines.add(new Mine(id, s3, amount, placeId));
        String json = Mine.listToJSON(mines);
        ArrayList<Mine> newMines = Mine.listFromJSON(json);
        for(Mine m : newMines) System.out.println("- " + m.getResource());

    }
}
