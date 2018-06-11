
import Game.TrainStation;
import Server.Server;
import Utils.JsonUtility;
import Utils.OTrainProtocol;
import com.google.gson.JsonArray;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.*;

public class StabilityTest {
    private static String players[] = {"1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20"};
    private static TestClient clients[] = new TestClient[players.length];

    private static class TestClient {
        private String name;
        private boolean finished;
        private String ipAddress = "localhost";
        private Socket socket;
        private BufferedReader reader;
        private PrintWriter writer;
        private ArrayList<TrainStation> trainStations = new ArrayList<>();

        public TestClient(String name) {
            this.name = name;
        }

        public boolean connectServer() {
            try {
                socket = new Socket(ipAddress, OTrainProtocol.PORT);
                reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                writer = new PrintWriter(socket.getOutputStream());
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
            return true;
        }

        public String signUp(Object selectedValue) {
            writer.println(OTrainProtocol.SIGN_UP);
            writer.println(name);
            writer.println(name);
            writer.println(selectedValue);
            writer.flush();
            return readLine();
        }

        public String readLine() {
            String line = "ERROR";
            try {
                line = reader.readLine();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return line;
        }

        public String sendLogin() {
            writer.println(OTrainProtocol.CONNECT);
            writer.println(name);
            writer.println(name);
            writer.flush();
            readLine();
            return readLine();
        }

        public void updateTrainStations() {
            writer.println(OTrainProtocol.GET_GARES);
            writer.flush();
            String answer = readLine();
            trainStations = JsonUtility.listFromJson((JsonArray) JsonUtility.fromJson(answer), TrainStation::new);
        }

        public ArrayList<TrainStation> getTrainStations() {
            return trainStations;
        }

        public void disconnect() {
            try {
                socket.close();
                reader.close();
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        public String changeStation(int stationId) {
            writer.println(OTrainProtocol.GO_TO);
            writer.println(stationId);
            writer.flush();
            return readLine();
        }

        public String getName() {
            return name;
        }

        public boolean isFinished() {
            return finished;
        }

        public void setFinished(boolean finished) {
            this.finished = finished;
        }
    }

    @BeforeAll
    public static void setUpBeforeAll() {
        Server.getInstance().startServer();
        for (int i = 0; i < players.length; i++) {
            Server.getInstance().getDataBase().deleteUser(players[i]);
            clients[i] = new TestClient(players[i]);
            clients[i].connectServer();
            clients[i].signUp("DRILL");
            clients[i].sendLogin();
        }
    }

    @BeforeEach
    public void setUpBeforeEach() {
        System.out.println("---");
    }

    public static int nbrOfChange = 0;
    @Test
    public void testChangeStation30Times() {
        Random random = new Random();
        long start = System.currentTimeMillis();
        for (TestClient c : clients) {
            new Timer().scheduleAtFixedRate(new TimerTask() {
                @Override
                public void run() {
                    c.setFinished(false);
                    while(nbrOfChange < 30) {
                        c.updateTrainStations();
                        int nbrOfStations = c.getTrainStations().size();
                        if (nbrOfStations == 0) {
                            System.out.println("failed" + c.getName());
                            continue;
                        }
                        int newStationId = c.getTrainStations().get(random.nextInt(nbrOfStations)).getId();
                        String line = c.changeStation(newStationId);
                        if (line.equals(OTrainProtocol.SUCCESS)) nbrOfChange++;
                        System.out.println(line);
                        System.out.println("nbrOfChange : " + nbrOfChange);
                    }
                    c.setFinished(true);
                }
            }, 1, 1);
        }

        while((System.currentTimeMillis() - start < 1000*20) && (Arrays.stream(clients).filter(TestClient::isFinished).count() < clients.length) && nbrOfChange < 30) {}
    }
}
