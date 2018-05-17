package Server;

import Server.Controller.Travel;
import Utils.OTrainProtocol;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;

public class Server {
    private static Server instance;
    private ServerSocket serverSocket;
    private LinkedList<ClientHandler> clientHandlers;
    private boolean running;
    private DataBase db;
    private Travel travelController;

    private Server(){}

    public static Server getInstance() {
        if(instance == null) instance = new Server();
        return instance;
    }

    public void init() {
        db = new DataBase();
        db.insertTrainStation(0, 0, 30, 30);// make sure the starting station exist
        travelController = new Travel();
        new Thread(travelController).start();

    }

    public void startServer() {
        try {
            init();
            serverSocket = new ServerSocket(OTrainProtocol.PORT);
            clientHandlers = new LinkedList<ClientHandler>();
            running = true;

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

    public Travel getTravelController() {
        return travelController;
    }

    public void removeHandler(ClientHandler handler) {
        clientHandlers.remove(handler);
    }

    public static void main(String ... args) {
        Server.getInstance().startServer();
    }
}
