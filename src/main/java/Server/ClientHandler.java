package Server;

import java.io.*;
import java.net.Socket;

public class
ClientHandler implements Runnable {
    private Socket socket;
    private DataBase db;
    private boolean running;
    private BufferedReader reader;
    private PrintWriter writer;
    private String username;

    public ClientHandler(Socket socket, DataBase db) throws IOException {
        this.socket = socket;
        this.db = db;
        running = true;
        reader = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
        writer = new PrintWriter(this.socket.getOutputStream());
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

                System.out.println("user : " + username + " pass : " + password);
            } while(!db.checkLoggin(username, password));

            writer.println("YOU ARE LOGGED AS : " + username);//protocol
            writer.flush();
            String line = reader.readLine();
            while (running && line != null) {
                //work...
                System.out.println("Client (" + username + ") : " + line);
                writer.println("You sent me that : " + line);
                writer.flush();

                line = reader.readLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
