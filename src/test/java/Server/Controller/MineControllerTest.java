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
    private static Server server;
    private static Client client;
    private static String user = "a";
    private static int stationId;
    private static int mineId;
    private static int wagonId;

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
        server.getDataBase().changeStationOfTrain(user, stationId);
        mineId = server.getDataBase().addMine(stationId, 500, 1000, 10, Ressource.Type.IRON_ORE.ordinal());
        server.getDataBase().addMine(stationId, 500, 1000, 10, Ressource.Type.WOOD_LOG.ordinal());
        server.getDataBase().addMine(stationId, 0, 1000, 10, Ressource.Type.WATER.ordinal());
        server.getDataBase().addMine(stationId, 500, 1000, 10, Ressource.Type.OIL.ordinal());
        server.getDataBase().addMine(stationId, 500, 1000, 10, Ressource.Type.GOLD_ORE.ordinal());
        wagonId = server.getDataBase().addWagon(user, 2000, 1, WagonStats.WagonType.DRILL.ordinal());
        server.getDataBase().addWagon(user, 2000, 1, WagonStats.WagonType.SAW.ordinal());
        server.getDataBase().addWagon(user, 2000, 1, WagonStats.WagonType.PUMP.ordinal());

        for(Wagon w : server.getDataBase().getTrain(user).getWagons()) server.getDataBase().updateWagon(user, w.getWeight(), 5, w.getType().ordinal(), w.getId());

        client = Client.getInstance();
        client.connectServer();
        client.sendLogin(user, user);
        client.readLine();
    }

    @Test
    public void testAddRemoveToMine(){
        // add
        assertEquals(OTrainProtocol.SUCCESS, client.startMining(wagonId, mineId));
        client.updateWagonMining();
        assertEquals(1, client.getWagonMining().size());
        // remove
        assertEquals(OTrainProtocol.SUCCESS, client.stopMining(wagonId));
        client.updateWagonMining();
        assertEquals(0, client.getWagonMining().size());
    }


}