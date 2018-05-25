package Client;

import Game.Train;
import Game.TrainStation;
import Game.WagonMining;
import Gui.LoginForm;
import Utils.OTrainProtocol;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

public class Client {
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
    }

    public void setFrameContent(JPanel panel) {
        frame.setContentPane(panel);
        frame.pack();
    }

    public String readLineFromServer() {
        try {
            return reader.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "ERROR";
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
        String answer = "ERROR";
        try {
            answer = reader.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return answer;
    }

    public String signUp(String username, String password) {
        writer.println(OTrainProtocol.SIGN_UP);
        writer.println(username);
        writer.println(password);
        writer.flush();
        String answer = "ERROR";
        try {
            answer = reader.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return answer;
    }

    public String getResources() {
        writer.println(OTrainProtocol.GET_RESSOURCES);
        writer.println(username);
        writer.flush();
        String answer = "ERROR";
        try {
            answer = reader.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return answer;
    }

    public Train getTrain() {
        return train;
    }

    public ArrayList<WagonMining> getWagonMining() {
        return wagonMining;
    }

    public void updateTrainStatus() {
        writer.println(OTrainProtocol.GET_TRAIN_STATUS);
        writer.flush();
        String answer = "ERROR";
        try {
            answer = reader.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }

        train.fromJSON(answer);
    }

    public void updateWagonMinig() {
        writer.println(OTrainProtocol.MINE_INFO);
        writer.flush();
        String answer = "ERROR";
        try {
            answer = reader.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }

        wagonMining = WagonMining.listFromJSON(answer);
    }

    public String getStations() {
        writer.println(OTrainProtocol.GET_GARES);
        writer.flush();
        String answer = "ERROR";
        try {
            answer = reader.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return answer;
    }

    public String changeStation(int stationId) {
        writer.println(OTrainProtocol.GO_TO);
        writer.println(stationId);
        writer.flush();
        String answer = "ERROR";
        try {
            answer = reader.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return answer;
    }

    public String startMining(int wagonId, int mineId) {
        writer.println(OTrainProtocol.MINE);
        writer.println(wagonId);
        writer.println(mineId);
        writer.flush();
        String answer = "ERROR";
        try {
            answer = reader.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return answer;
    }

    public static void main(String ... args) {
        Client.getInstance().startingFrame();

        // Crée la fenêtre de login
        /*JFrame frame = new JFrame("OTrain");
        frame.setContentPane(new LoginForm(client).getPanel_main());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);*/

        //test server responses
        /*String line = client.readLineFromServer();
        while(true) {
            System.out.println("Server : " + line);
            line = client.readLineFromServer();
        }*/
    }
}
