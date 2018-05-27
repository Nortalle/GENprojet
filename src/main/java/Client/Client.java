package Client;

import Game.Train;
import Game.TrainStation;
import Game.WagonMining;
import Gui.LoginForm;
import Server.ClientHandler;
import Utils.JsonUtility;
import Utils.OTrainProtocol;
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
    private Socket socket;
    private BufferedReader reader;
    private PrintWriter writer;
    private String username;
    private Train train;
    private ArrayList<WagonMining> wagonMining;

    public static Client getInstance() {
        if(instance == null) instance = new Client();
        return instance;
    }

    public static void setClientLogComponent(JTextArea component) {
        CLIENT_LOG.setComponent(component);
    }

    //GUI
    private JFrame frame;

    public void connectServer() {

        try {
            socket = new Socket("localhost", OTrainProtocol.PORT);
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            writer = new PrintWriter(socket.getOutputStream());
            train = new Train();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void disconnect() {
        try {
            socket.close();
            reader.close();
            writer.close();
            setFrameContent(new LoginForm().getPanel_main());
            connectServer();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void startingFrame() {
        frame = new JFrame("OTrain");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setFrameContent(new LoginForm().getPanel_main());
        frame.setVisible(true);

        connectServer();
    }

    public void setFrameContent(JPanel panel, Dimension d) {
        frame.setContentPane(panel);
        frame.setSize(d);
        frame.setPreferredSize(d);
    }

    public void setFrameContent(JPanel panel) {
        frame.setContentPane(panel);
        frame.pack();
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

    public String getResources() {
        writer.println(OTrainProtocol.GET_RESSOURCES);
        writer.flush();
        return readLine();
    }

    public Train getTrain() {
        updateTrainStatus();
        return train;
    }

    public ArrayList<WagonMining> getWagonMining() {
        updateWagonMining();
        return wagonMining;
    }

    private void updateTrainStatus() {
        writer.println(OTrainProtocol.GET_TRAIN_STATUS);
        writer.flush();
        String answer = readLine();
        train.fromJson((JsonObject) JsonUtility.fromJson(answer));
    }

    public ArrayList<Train> getTrainsAtStation(int stationId) {
        writer.println(OTrainProtocol.GET_TRAINS_AT);
        writer.println(stationId);
        writer.flush();
        String answer = readLine();
        return Train.listFromJson((JsonArray) JsonUtility.fromJson(answer));
    }

    private void updateWagonMining() {
        writer.println(OTrainProtocol.MINE_INFO);
        writer.flush();
        String answer = readLine();
        wagonMining = WagonMining.listFromJson((JsonArray) JsonUtility.fromJson(answer));
    }

    public ArrayList<TrainStation> getStations() {
        writer.println(OTrainProtocol.GET_GARES);
        writer.flush();
        String answer = readLine();
        return TrainStation.listFromJson((JsonArray) JsonUtility.fromJson(answer));
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

    public static void main(String ... args) {
        Client.getInstance().startingFrame();
    }
}
