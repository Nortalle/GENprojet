package Client;

import Gui.LoginForm;

import javax.swing.*;
import java.io.*;
import java.net.Socket;

public class Client {
    private Socket socket;
    private BufferedReader reader;
    private PrintWriter writer;
    private String username;

    public void connectServer() {

        try {
            socket = new Socket("localhost", 44444);
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            writer = new PrintWriter(socket.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
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

    public void sendLogin(String username, String password) {
        writer.println(username);
        writer.println(password);
        writer.flush();
    }

    //temp
    public static void main(String ... args) {
        Client client = new Client();

        // Crée la fenêtre de login
        JFrame frame = new JFrame("OTrain");
        frame.setContentPane(new LoginForm(client).getPanel_main());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);

        //client.connectServer();
        String line = client.readLineFromServer();
        //client.sendLogin(args[0], args[1]);
        while(true) {
            System.out.println("Server : " + line);
            line = client.readLineFromServer();
        }
    }
}
