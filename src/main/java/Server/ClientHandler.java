package Server;

import java.io.*;
import java.net.Socket;

public class ClientHandler implements Runnable {
    private Socket socket;
    private DataBase db;
    private boolean running;
    private BufferedReader reader;
    private PrintStream writer;
    private String username;

    public ClientHandler(Socket socket, DataBase db) throws IOException {
        this.socket = socket;
        this.db = db;
        running = true;
        reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        writer = new PrintStream(new ObjectOutputStream(socket.getOutputStream()));
    }

    public void run() {
        try {
            //try to connect
            String password;
            do {
                writer.println("SEND LOGGIN");//protocol
                writer.flush();
                username = reader.readLine();
                password = reader.readLine();
            } while(!db.checkLoggin(username, password));

            writer.println("YOU ARE LOGGED AS : " + username);//protocol
            writer.flush();
            String line;
            while (running && (line = reader.readLine()) != null) {
                //work...
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
