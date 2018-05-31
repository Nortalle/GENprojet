package Game;

import Utils.JsonUtility;
import com.google.gson.JsonArray;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TrainStationTest {
    @BeforeAll
    public static void setUpBeforeAll() {

    }

    @BeforeEach
    public  void setUpBeforeEach(){
        System.out.println("---");
    }

    @Test
    void fromJSON() {
        int id = 0;
        int posX = 0;
        int posY = 0;
        int nbOfPlatforms = 1;
        int sizeOfPlatforms = 1;
        ArrayList<Mine> mines = new ArrayList<>();
        mines.add(new Mine());
        mines.add(new Mine());
        mines.add(new Mine());
        TrainStation ts = new TrainStation(id, posX, posY, nbOfPlatforms, sizeOfPlatforms, mines);
        String json = JsonUtility.toJson(ts.toJson());
        TrainStation newTs = new TrainStation(json);
        assertEquals(ts.getMines().size(), newTs.getMines().size());
    }

    @Test
    void listFromJSON() {
        ArrayList<TrainStation> stations = new ArrayList<>();
        stations.add(new TrainStation());
        stations.add(new TrainStation());
        stations.add(new TrainStation());
        String json = JsonUtility.toJson(JsonUtility.listToJson(stations, station -> station.toJson()));
        System.out.println(stations.size());
        assertEquals(stations.size(), JsonUtility.listFromJson((JsonArray) JsonUtility.fromJson(json), station -> new TrainStation(station)).size());
    }
}
