package Server;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.SQLOutput;
import java.util.LinkedList;

public class Server {
    private ServerSocket serverSocket;
    private LinkedList<ClientHandler> clientHandlers;
    private boolean running;
    private DataBase db;

    public void startServer() {
        try {
            serverSocket = new ServerSocket(44444);//protocol
            clientHandlers = new LinkedList<ClientHandler>();
            running = true;
            db = new DataBase();

            Thread serverThread = new Thread(new Runnable() {
                public void run() {
                    while(running) {
                        try {
                            Socket clientSocket = serverSocket.accept();
                            //
                            PrintWriter pw = new PrintWriter(clientSocket.getOutputStream());
                            pw.println("println");
                            pw.flush();
                            //
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
}
