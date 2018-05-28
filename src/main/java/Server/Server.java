package Server;

import Server.Controller.CraftController;
import Server.Controller.MineController;
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
    private MineController mineController;
    private CraftController craftController;

    private Server(){}

    public static Server getInstance() {
        if(instance == null) instance = new Server();
        return instance;
    }

    public void init() {
        dataBase = new DataBase();
        dataBase.insertTrainStation(0, 0, 100, 100);// make sure the starting station exist
        travelController = new Travel();
        regenerationController = new MineRegeneration();
        mineController = new MineController();
        craftController = new CraftController();
    }

    public void startServer() {
        try {
            if(serverSocket != null && serverSocket.isBound()) return;
            serverSocket = new ServerSocket(OTrainProtocol.PORT);
            init();// init after binding
            clientHandlers = new LinkedList<>();
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

    public MineController getMineController() {
        return mineController;
    }

    public CraftController getCraftController() {
        return craftController;
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
