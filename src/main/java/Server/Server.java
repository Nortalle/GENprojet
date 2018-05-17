package Server;

import Utils.OTrainProtocol;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.SQLOutput;
import java.util.LinkedList;

public class Server {
    private static Server instance;
    private ServerSocket serverSocket;
    private LinkedList<ClientHandler> clientHandlers;
    private boolean running;
    private DataBase db;

    private Server(){}

    public static Server getInstance() {
        if(instance == null) instance = new Server();
        return instance;
    }

    public void startServer() {
        try {
            serverSocket = new ServerSocket(OTrainProtocol.PORT);
            clientHandlers = new LinkedList<ClientHandler>();
            running = true;
            db = new DataBase();

            Thread serverThread = new Thread(new Runnable() {
                public void run() {
                    while(running) {
                        try {
                            Socket clientSocket = serverSocket.accept();
                            ClientHandler clientHandler = new ClientHandler(clientSocket, db);
                            clientHandlers.add(clientHandler);
                            Thread clientThread = new Thread(clientHandler);
                            clientThread.start();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            });

            serverThread.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String ... args) {
        Server.getInstance().startServer();
    }
}
