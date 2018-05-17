package Client;

import Game.TrainStation;
import Gui.LoginForm;
import Utils.OTrainProtocol;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

import javax.swing.*;
import java.io.*;
import java.net.Socket;

public class Client {
    private Socket socket;
    private BufferedReader reader;
    private PrintWriter writer;
    private String username;

    //GUI
    private JFrame frame;

    public void connectServer() {

        try {
            socket = new Socket("localhost", OTrainProtocol.PORT);
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            writer = new PrintWriter(socket.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void disconnect() {
        try {
            socket.close();
            reader.close();
            writer.close();
            setFrameContent(new LoginForm(this).getPanel_main());
            connectServer();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void startingFrame() {
        frame = new JFrame("OTrain");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setFrameContent(new LoginForm(this).getPanel_main());
        frame.setVisible(true);

        connectServer();
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

    public String getStations() {
        writer.println(OTrainProtocol.GET_GARES);
        //writer.println(username);
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
        Client client = new Client();
        client.startingFrame();

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
