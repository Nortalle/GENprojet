import Client.Client;
import Game.TrainStation;
import Server.Server;
import Server.DataBase;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

public class ChangeStationTest {
    String username = "user1";
    String password = "pass1";
    Client client;
    static DataBase dataBase;

    @BeforeAll
    public static void setUpBeforeAll() {
        Server.getInstance().startServer();
        dataBase = new DataBase();
    }

    @BeforeEach
    public  void setUpBeforeEach(){
        System.out.println("---");
        client = new Client();
        client.connectServer();
        dataBase.deleteUser(username);
        dataBase.insertUser(username, password);
    }

    @Test
    public void getNumberOfAllTrainStations(){
        String line = client.sendLogin(username, password);
        System.out.println("Server : " + line);
        // insert train stations
        line = client.getStations();
        int nbStation = dataBase.getAllTrainStations().size();
        if(nbStation < 1) fail("No stations, insert one or more");
        assertEquals(nbStation, TrainStation.listFromJSON(line).size());

    }
}
