package Server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;

public class Server {
    private ServerSocket serverSocket;
    private LinkedList<ClientHandler> clientHandlers;
    private boolean running;
    private DataBase db;

    public void startServer() {
        try {
            serverSocket = new ServerSocket(4444);//protocol
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
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
