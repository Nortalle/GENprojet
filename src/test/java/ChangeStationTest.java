import Client.Client;
import Game.TrainStation;
import Server.Server;
import Server.DataBase;
import Utils.JsonUtility;
import Utils.OTrainProtocol;
import com.google.gson.JsonArray;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

public class ChangeStationTest {
    String username = "user1";
    String password = "pass1";
    Client client;
    static DataBase dataBase;
    static int x = 10;
    static int y = 10;

    @BeforeAll
    public static void setUpBeforeAll() {
        Server.getInstance().startServer();
        dataBase = new DataBase();
        dataBase.insertTrainStation(x, y, 10, 10);
    }

    @BeforeEach
    public  void setUpBeforeEach(){
        System.out.println("---");
        Server.getInstance().getTravelController().removeTrain(username);
        client = new Client();
        client.connectServer();
        dataBase.deleteUser(username);
        dataBase.insertPlayer(username, password);
        String line = client.sendLogin(username, password);
        System.out.println(line);
        client.readLine();
    }

    @Test
    public void getNumberOfAllTrainStations(){
        int nbStation = dataBase.getAllTrainStations().size();
        if(nbStation < 1) fail("No stations, insert one or more");
        client.updateTrainStations();
        assertEquals(nbStation, client.getTrainStations().size());

    }

    @Test
    public void changeStation() {
        int stationId = dataBase.getTrainStationIdByPos(x, y);
        String line = client.changeStation(stationId);
        System.out.println(line);
        assertEquals(dataBase.getTrainStation(stationId).toString(), client.getTrain().getTrainStation().toString());
    }

    @Test
    public void cantGoToFullStation() {
        String users[] = {"u1","u2","u3"};
        for(String u : users) {
            dataBase.deleteUser(u);
            dataBase.insertPlayer(u, u);
        }
        Client clients[] = {new Client(), new Client(), new Client()};
        for(int i = 0; i < clients.length; i++) {
            clients[i].connectServer();
            clients[i].sendLogin(users[i], users[i]);
            clients[i].readLine();
        }
        int posX = 20;
        int posY = 10;
        dataBase.deleteTrainStation(dataBase.getTrainStationIdByPos(posX, posY));
        dataBase.insertTrainStation(posX, posY, 2, 5);
        int stationId = dataBase.getTrainStationIdByPos(posX, posY);
        clients[0].changeStation(stationId);
        clients[1].changeStation(stationId);
        assertEquals(OTrainProtocol.FAILURE, clients[2].changeStation(stationId));
    }

    @Test
    public void changeStationTakeTime() {
        int stationId = dataBase.getTrainStationIdByPos(x, y);
        String line = client.changeStation(stationId);
        System.out.println(line);
        assertTrue(client.getTrain().getTrainStationETA() > 0);
    }
}
