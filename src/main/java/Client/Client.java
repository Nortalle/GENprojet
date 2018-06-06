package Client;

import Game.*;
import Gui.LoginForm;
import Server.ClientHandler;
import Utils.JsonUtility;
import Utils.OTrainProtocol;
import Utils.ResourceAmount;
import Utils.Ressource;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.logging.Logger;

public class Client {
    private static final java.util.logging.Logger LOG = Logger.getLogger(ClientHandler.class.getName());
    private static final ClientLog CLIENT_LOG = new ClientLog();
    static {LOG.addHandler(CLIENT_LOG);}

    private static Client instance;
    private String ipAddress = "localhost";
    private Socket socket;
    private BufferedReader reader;
    private PrintWriter writer;
    private String username;
    private Train train;
    private ArrayList<WagonMining> wagonMining = new ArrayList<>();
    private ArrayList<ResourceAmount> resourceAmounts = new ArrayList<>();
    private ArrayList<Craft> crafts = new ArrayList<>();
    private ArrayList<UpgradeWagon> upgradeWagons = new ArrayList<>();
    private ArrayList<CreateWagon> createWagons = new ArrayList<>();
    private ArrayList<Train> trainsAtStation = new ArrayList<>();
    private ArrayList<TrainStation> trainStations = new ArrayList<>();

    public static Client getInstance() {
        if(instance == null) instance = new Client();
        return instance;
    }

    public static void setClientLogComponent(JTextArea component) {
        CLIENT_LOG.setComponent(component);
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public boolean setIpAddress(String newIp) {
        ipAddress = newIp;
        return connectServer();
    }

    //GUI
    private JFrame frame;

    public boolean connectServer() {
        try {
            socket = new Socket(ipAddress, OTrainProtocol.PORT);
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            writer = new PrintWriter(socket.getOutputStream());
            train = new Train();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public void disconnect() {
        try {
            socket.close();
            reader.close();
            writer.close();
            setConnectionPanel();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void startingFrame() {
        frame = new JFrame("OTrain");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setConnectionPanel();
    }

    public void setConnectionPanel() {
        setFrameContent(new LoginForm().getPanel_main());
        frame.setVisible(true);
        connectServer();
    }

    public void setFrameContent(JPanel panel, Dimension d) {
        frame.setContentPane(panel);
        frame.setSize(d);
    }

    public void setFrameContent(JPanel panel) {
        frame.setContentPane(panel);
        frame.pack();
    }

    public int getSpecificResource(Ressource.Type type) {
        for(ResourceAmount ra : resourceAmounts) {
            if(ra.getRessource() == type) {
                return ra.getQuantity();
            }
        }
        return 0;
    }

    public String readLine() {
        String line = "ERROR";
        try {
            line = reader.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
        LOG.info(line);
        return line;
    }

    public void sendLine(String line) {
        writer.println(line);
        writer.flush();
    }

    public String sendLogin(String username, String password) {
        writer.println(OTrainProtocol.CONNECT);
        writer.println(username);
        writer.println(password);
        writer.flush();
        return readLine();
    }

    public String signUp(String username, String password) {
        writer.println(OTrainProtocol.SIGN_UP);
        writer.println(username);
        writer.println(password);
        writer.flush();
        return readLine();
    }

    public void updateResourceAmount() {
        writer.println(OTrainProtocol.GET_OBJECTS);
        writer.flush();
        String answer = readLine();
        resourceAmounts = JsonUtility.listFromJson((JsonArray) JsonUtility.fromJson(answer), ResourceAmount::new);
    }

    public ArrayList<ResourceAmount> getResourceAmounts() {
        return resourceAmounts;
    }

    public void updateCrafts() {
        writer.println(OTrainProtocol.GET_PROD_QUEUE);
        writer.flush();
        String answer = readLine();
        crafts =  JsonUtility.listFromJson((JsonArray) JsonUtility.fromJson(answer), Craft::new);
    }

    public ArrayList<Craft> getCrafts() {
        return crafts;
    }

    public void updateUpgradeWagons() {
        writer.println(OTrainProtocol.GET_UPGRADE_QUEUE);
        writer.flush();
        String answer = readLine();
        upgradeWagons = JsonUtility.listFromJson((JsonArray) JsonUtility.fromJson(answer), UpgradeWagon::new);
    }

    public ArrayList<UpgradeWagon> getUpgradeWagons() {
        return upgradeWagons;
    }

    public void updateCreateWagons() {
        writer.println(OTrainProtocol.GET_CREATION_QUEUE);
        writer.flush();
        String answer = readLine();
        createWagons = JsonUtility.listFromJson((JsonArray) JsonUtility.fromJson(answer), CreateWagon::new);
    }

    public ArrayList<CreateWagon> getCreateWagons() {
        return createWagons;
    }

    public void updateTrain() {
        writer.println(OTrainProtocol.GET_TRAIN_STATUS);
        writer.flush();
        String answer = readLine();
        train.fromJson((JsonObject) JsonUtility.fromJson(answer));
    }

    public Train getTrain() {
        updateTrain();
        return train;
    }

    public void updateWagonMining() {
        writer.println(OTrainProtocol.MINE_INFO);
        writer.flush();
        String answer = readLine();
        wagonMining = JsonUtility.listFromJson((JsonArray) JsonUtility.fromJson(answer), WagonMining::new);
    }

    public ArrayList<WagonMining> getWagonMining() {
        return wagonMining;
    }

    public void updateTrainsAtStation(int stationId) {
        writer.println(OTrainProtocol.GET_TRAINS_AT);
        writer.println(stationId);
        writer.flush();
        String answer = readLine();
        trainsAtStation = JsonUtility.listFromJson((JsonArray) JsonUtility.fromJson(answer), Train::new);
    }

    public ArrayList<Train> getTrainsAtStation(int stationId) {
        return trainsAtStation;
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

    public void updateAll() {
        updateTrain();
        updateResourceAmount();
        updateCrafts();
        updateUpgradeWagons();
        updateCreateWagons();
        updateWagonMining();
        updateTrainStations();
        updateTrainsAtStation(train.getTrainStation().getId());
    }

    public String changeStation(int stationId) {
        writer.println(OTrainProtocol.GO_TO);
        writer.println(stationId);
        writer.flush();
        return readLine();
    }

    public String startMining(int wagonId, int mineId) {
        writer.println(OTrainProtocol.MINE);
        writer.println(wagonId);
        writer.println(mineId);
        writer.flush();
        return readLine();
    }

    public String stopMining(int wagonId) {
        writer.println(OTrainProtocol.STOP_MINE);
        writer.println(wagonId);
        writer.flush();
        return readLine();
    }

    public String startCraft(int recipeIndex) {
        writer.println(OTrainProtocol.CRAFT);
        writer.println(recipeIndex);
        writer.flush();
        return readLine();
    }

    public String startUpgrade(int wagonId) {
        writer.println(OTrainProtocol.UPGRADE);
        writer.println(wagonId);
        writer.flush();
        return readLine();
    }

    public String startCreation(int wagonRecipeIndex) {
        writer.println(OTrainProtocol.CREATION);
        writer.println(wagonRecipeIndex);
        writer.flush();
        return readLine();
    }

    public static void main(String ... args) {
        Client.getInstance().startingFrame();
    }
}
