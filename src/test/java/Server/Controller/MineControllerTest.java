package Server.Controller;

import Client.Client;
import Game.Wagon;
import Server.Server;
import Utils.OTrainProtocol;
import Utils.Ressource;
import Utils.WagonStats;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MineControllerTest {
    static Server server;
    static Client client;
    static String user = "a";
    static int stationId;
    static int mineId;
    static int wagonId;

    @BeforeAll
    public static void setUpBeforeAll() {
        server = Server.getInstance();
        server.startServer();
    }

    @BeforeEach
    public void setUpBeforeEach() {
        System.out.println("---");
        int x = -20;
        int y = -20;
        server.getDataBase().deleteUser(user);
        server.getDataBase().insertPlayer(user, user);
        server.getDataBase().deleteTrainStation(server.getDataBase().getTrainStationIdByPos(x, y));
        server.getDataBase().insertTrainStation(x, y, 10, 10);
        stationId = server.getDataBase().getTrainStationIdByPos(x, y);
        server.getDataBase().changeStaionOfTrain(user, stationId);
        mineId = server.getDataBase().addMine(stationId, 500, Ressource.Type.IRON_ORE.ordinal());
        server.getDataBase().addMine(stationId, 500, Ressource.Type.WOOD_LOG.ordinal());
        wagonId = server.getDataBase().addWagon(user, 2000, 1, WagonStats.DRILL_ID);

        for(Wagon w : server.getDataBase().getTrain(user).getWagons()) server.getDataBase().updateWagon(user, w.getWeight(), 5, w.getTypeID(), w.getId());

        client = Client.getInstance();
        client.connectServer();
        client.sendLogin(user, user);
    }

    @Test
    public void testAddRemoveToMine(){
        // add
        assertEquals(OTrainProtocol.SUCCESS, client.startMining(wagonId, mineId));
        assertEquals(1, client.getWagonMining().size());
        // remove
        assertEquals(OTrainProtocol.SUCCESS, client.stopMining(wagonId));
        assertEquals(0, client.getWagonMining().size());
    }


}