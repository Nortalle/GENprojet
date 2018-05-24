package Server;

import Server.Controller.MineRegeneration;
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
    private DataBase dataBase;
    private Travel travelController;
    private MineRegeneration regenerationController;

    private Server(){}

    public static Server getInstance() {
        if(instance == null) instance = new Server();
        return instance;
    }

    public void init() {
        dataBase = new DataBase();
        dataBase.insertTrainStation(0, 0, 30, 30);// make sure the starting station exist
        travelController = new Travel();
        new Thread(travelController).start();
        regenerationController = new MineRegeneration();
        new Thread(regenerationController).start();
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
                            ClientHandler clientHandler = new ClientHandler(clientSocket);
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

    public MineRegeneration getRegenerationController() {
        return regenerationController;
    }

    public DataBase getDataBase() {
        return dataBase;
    }

    public void removeHandler(ClientHandler handler) {
        clientHandlers.remove(handler);
    }

    public static void main(String ... args) {
        Server.getInstance().startServer();
    }
}
