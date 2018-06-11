import Client.Client;
import Server.Server;
import Utils.OTrainProtocol;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Random;

public class StabilityTest {
    private static Client client;
    private static String players[] = {"1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20"};

    @BeforeAll
    public static void setUpBeforeAll() {
        Server.getInstance().startServer();
        client = Client.getInstance();
        client.connectServer();
        for(String p : players) {
            Server.getInstance().getDataBase().deleteUser(p);
            client.signUp(p, p, "DRILL");
        }
    }

    @BeforeEach
    public  void setUpBeforeEach(){
        System.out.println("---");
    }

    @Test
    public void testChangeStation30Times(){
        Random random = new Random();
        int nbrOfChange = 0;
        for(int i = 0; i < 100 && nbrOfChange < 30; i++) {
            for (String p : players) {
                client.connectServer();
                client.sendLogin(p, p);
                client.readLine();
                client.updateTrainStations();
                client.getTrainStations().size();
                int nbrOfStations = client.getTrainStations().size();
                if(nbrOfStations == 0) {
                    client.disconnect();
                    System.out.println("failed");
                    System.out.println(nbrOfChange + " : " + i);
                    continue;
                }
                int newStationId = client.getTrainStations().get(random.nextInt(nbrOfStations)).getId();
                String line = client.changeStation(newStationId);
                if(line.equals(OTrainProtocol.SUCCESS)) nbrOfChange++;
                System.out.println(line);
                System.out.println(nbrOfChange + " : " + i);
                client.disconnect();
            }
        }
    }
}
