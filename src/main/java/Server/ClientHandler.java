package Server;

import Game.Resources;
import Game.Train;
import Game.TrainStation;
import Utils.OTrainProtocol;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

public class
ClientHandler implements Runnable {
    private Socket socket;
    private DataBase db;
    private boolean running;
    private BufferedReader reader;
    private PrintWriter writer;
    private String username;

    public ClientHandler(Socket socket) throws IOException {
        this.socket = socket;
        db = Server.getInstance().getDataBase();
        running = true;
        reader = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
        writer = new PrintWriter(this.socket.getOutputStream());
        username = null;
    }

    public void run() {
        try {
            waitForAuthentication();

            String line = reader.readLine();
            while (running && line != null) {
                //work...
                System.out.println("Client (" + username + ") : " + line);
                if(line.equals(OTrainProtocol.GET_RESSOURCES)) {
                    int r[] = db.getPlayerResources(username);
                    Resources resources = new Resources(r);
                    writer.println(resources.toJSON());
                    writer.flush();
                } else if(line.equals(OTrainProtocol.GET_TRAIN_STATUS)) {
                    writer.println(db.getTrain(username).toJSON());
                    writer.flush();
                } else if(line.equals(OTrainProtocol.GET_GARES)) {
                    ArrayList<TrainStation> trainStations = db.getAllTrainStations();
                    writer.println(TrainStation.listToJSON(trainStations));
                    writer.flush();
                } else if(line.equals(OTrainProtocol.GO_TO)) {
                    String newTsLine = reader.readLine();
                    System.out.println(newTsLine);
                    if(Server.getInstance().getTravelController().ctrlChangeStation(username, newTsLine)) {
                        writer.println(OTrainProtocol.SUCCESS);
                    } else {
                        writer.println(OTrainProtocol.FAILURE);
                    }
                    writer.flush();

                } else if(line.equals(OTrainProtocol.MINE)) {

                } else if(line.equals(OTrainProtocol.STOP_MINE)) {

                }
                /*writer.println("You sent me that : " + line);
                writer.flush();*/

                line = reader.readLine();
            }
        } catch (IOException e) {
            e.printStackTrace();

            Server.getInstance().removeHandler(this);
        }
    }

    private void waitForAuthentication() {
        try {
            boolean logged = false;
            boolean signedUp;
            while(!logged) {
                String request = reader.readLine();
                while(!request.equals(OTrainProtocol.CONNECT) && !request.equals(OTrainProtocol.SIGN_UP)) request = reader.readLine();
                username = reader.readLine();
                String password = reader.readLine();
                if(request.equals(OTrainProtocol.CONNECT)) {
                    logged = db.checkLoggin(username, password);
                    writer.println(logged ? OTrainProtocol.SUCCESS : OTrainProtocol.FAILURE);
                    writer.flush();
                } else if(request.equals(OTrainProtocol.SIGN_UP)) {
                    if(username == null || password == null || username.equals("")) {
                        signedUp = false;
                    } else {
                        signedUp = db.insertUser(username, password);
                    }
                    writer.println(signedUp ? OTrainProtocol.SUCCESS : OTrainProtocol.FAILURE);
                    writer.flush();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
