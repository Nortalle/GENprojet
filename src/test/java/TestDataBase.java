import Game.Mine;
import Game.Resources;
import Game.TrainStation;
import Server.DataBase;
import Server.Server;
import Utils.Ressource;
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
    public void insertNewUserAlsoInsertNewResourcesPerUser(){
        dataBase.insertPlayer(username, password);
        assertTrue(dataBase.getPlayerResources(username)[0] > -1);
    }

    @Test
    public void deleteUserAlsoDeleteResourcesPerUser() {
        dataBase.insertPlayer(username, password);
        dataBase.deleteUser(username);
        assertTrue(dataBase.getPlayerResources(username)[0] == -1);
    }

    @Test
    public void addMineToStation() {
        int x = -10;
        int y = -10;
        dataBase.deleteTrainStation(dataBase.getTrainStationIdByPos(y, y));
        dataBase.insertTrainStation(x, y, 10, 10);
        int stationId = dataBase.getTrainStationIdByPos(x, y);
        TrainStation tsPrev = dataBase.getTrainStation(stationId);
        dataBase.addMine(stationId, 400, Ressource.Type.IRON_ORE.ordinal());
        dataBase.addMine(stationId, 0, Ressource.Type.GOLD_ORE.ordinal());
        TrainStation tsNext = dataBase.getTrainStation(stationId);
        assertEquals(tsPrev.getMines().size() + 1, tsNext.getMines().size());
    }


    @Test
    public void setMineAmount(){
        /*
        ArrayList<Mine> mines = dataBase.getAllMines();
        int id = mines.get(0).getId();

        dataBase.setMineAmount(id,467);

        assertEquals(467, dataBase.getMine(id).getAmount());
        */
    }
}
