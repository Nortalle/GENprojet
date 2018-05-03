package Client;

import Server.DataBase;

import java.io.*;
import java.net.Socket;

public class Client {

    private Socket socket;
    private BufferedReader reader;
    private PrintWriter writer;

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
}
