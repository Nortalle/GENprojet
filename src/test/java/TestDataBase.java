import Game.TrainStation;
import Game.Wagon;
import Server.DataBase;
import Server.Server;
import Utils.Ressource;
import Utils.WagonStats;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class TestDataBase {
    private static DataBase dataBase;
    private static String username = "user1";
    private static String password = "pass1";

    @BeforeAll
    public static void setUpBeforeAll() {
        dataBase = new DataBase();
    }

    @BeforeEach
    public  void setUpBeforeEach(){
        System.out.println("---");
        dataBase.deleteUser(username);
        Server.getInstance().init();
    }

    @Test
    public void insertNewUser(){
        assertTrue(dataBase.insertPlayer(username, password));
    }

    @Test
    public void numberOfUsersAfterInsert(){
        int nbUsers = dataBase.getAllUsers().size();
        if(dataBase.insertPlayer(username, password)) assertEquals(nbUsers + 1, dataBase.getAllUsers().size());
        else assertEquals(nbUsers, dataBase.getAllUsers().size());
    }

    @Test
    public void failInsertAlreadyExitingUser(){
        dataBase.insertPlayer(username, password);
        assertFalse(dataBase.insertPlayer(username, password));
    }

    @Test
    public void addMineToStation() {
        int x = -10;
        int y = -10;
        dataBase.deleteTrainStation(dataBase.getTrainStationIdByPos(y, y));
        dataBase.insertTrainStation(x, y, 10, 10);
        int stationId = dataBase.getTrainStationIdByPos(x, y);
        TrainStation tsPrev = dataBase.getTrainStation(stationId);
        dataBase.addMine(stationId, 400, 1000, 10, Ressource.Type.COPPER_ORE.ordinal());
        dataBase.addMine(stationId, 0, 1000, 10, Ressource.Type.CHARCOAL.ordinal());
        TrainStation tsNext = dataBase.getTrainStation(stationId);
        assertEquals(tsPrev.getMines().size() + 2, tsNext.getMines().size());
    }

    //TEST ON WAGON

    @Test
    public void addWagon(){
        dataBase.insertPlayer(username, password);
        int id = dataBase.addWagon(username, 1000, 1, WagonStats.WagonType.DRILL.ordinal());
        Wagon w = new Wagon(id, 1000, 1, WagonStats.WagonType.DRILL);
        Wagon w1 = dataBase.getWagon(id);
        String usernamefromDB = dataBase.getUsernameByWagonId(id);
        assertEquals(w.getId(), w1.getId());
        assertEquals(w.getType().ordinal(), w1.getType().ordinal());
        assertEquals(w.getLevel(), w1.getLevel());
        assertEquals(w.getWeight(), w1.getWeight());
        assertEquals(username, usernamefromDB);

    }


    @Test
    public void updateWagon(){
        dataBase.insertPlayer(username, password);
        int id = dataBase.addWagon(username, 1000, 1, 1);
        boolean changeWorked = dataBase.updateWagon(username, 1200, 2, 2, id);
        Wagon w = dataBase.getWagon(id);
        assertTrue(changeWorked);
        assertEquals(1200, w.getWeight());
        assertEquals(2, w.getLevel());
        assertEquals(2, w.getType().ordinal());
    }

    @Test
    public void updateWagonLevel(){
        dataBase.insertPlayer(username, password);
        int id = dataBase.addWagon(username, 1000, 1, 1);
        boolean changeWorked = dataBase.updateWagonLevel(id, 10);
        Wagon w = dataBase.getWagon(id);
        assertTrue(changeWorked);
        assertEquals(10, w.getLevel());
    }



}
