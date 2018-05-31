package Server;

import Game.*;
import Utils.OTrainProtocol;
import Utils.ResourceAmount;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.logging.Logger;

public class
ClientHandler implements Runnable {
    private static final Logger LOG = Logger.getLogger(ClientHandler.class.getName());

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

            String line = readLine();
            while (running && line != null) {
                //work...

                if(line.equals(OTrainProtocol.GET_RESSOURCES)) {
                    //int r[] = db.getPlayerResources(username);
                    int r[] = db.getPlayerResourcesViaObjects(username);// temp solution
                    Resources resources = new Resources(r);
                    writer.println(resources.toJson());
                    writer.flush();
                } else if(line.equals(OTrainProtocol.GET_TRAIN_STATUS)) {
                    writer.println(db.getTrain(username).toJson());
                    writer.flush();
                } else if(line.equals(OTrainProtocol.GET_OBJECTS)) {
                    writer.println(ResourceAmount.listToJson(db.getPlayerObjects(username)));
                    writer.flush();
                } else if(line.equals(OTrainProtocol.GET_TRAINS_AT)) {
                    int stationId = Integer.valueOf(readLine());
                    writer.println(Train.listToJson(db.getAllTrainsAtStation(stationId)));
                    writer.flush();
                } else if(line.equals(OTrainProtocol.MINE_INFO)) {
                    writer.println(WagonMining.listToJson(Server.getInstance().getMineController().getPlayerWagonMining(username)));
                    writer.flush();
                } else if(line.equals(OTrainProtocol.GET_GARES)) {
                    ArrayList<TrainStation> trainStations = db.getAllTrainStations();
                    writer.println(TrainStation.listToJson(trainStations));
                    writer.flush();
                } else if(line.equals(OTrainProtocol.GO_TO)) {
                    String newTsLine = readLine();
                    if(Server.getInstance().getTravelController().ctrlChangeStation(username, newTsLine)) {
                        writer.println(OTrainProtocol.SUCCESS);
                    } else {
                        writer.println(OTrainProtocol.FAILURE);
                    }
                    writer.flush();
                } else if(line.equals(OTrainProtocol.MINE)) {
                    String wagonLine = readLine();
                    String mineLine = readLine();
                    if(Server.getInstance().getMineController().tryMine(username, wagonLine, mineLine)) {
                        writer.println(OTrainProtocol.SUCCESS);
                    } else {
                        writer.println(OTrainProtocol.FAILURE);
                    }
                    writer.flush();
                } else if(line.equals(OTrainProtocol.STOP_MINE)) {
                    String wagonLine = readLine();
                    if(Server.getInstance().getMineController().removeWagon(wagonLine)) {
                        writer.println(OTrainProtocol.SUCCESS);
                    } else {
                        writer.println(OTrainProtocol.FAILURE);
                    }
                    writer.flush();
                } else if(line.equals(OTrainProtocol.CRAFT)) {
                    String recipeLine = readLine();
                    if(Server.getInstance().getCraftController().tryCraft(username, recipeLine)) {
                        writer.println(OTrainProtocol.SUCCESS);
                    } else {
                        writer.println(OTrainProtocol.FAILURE);
                    }
                    writer.flush();
                } else if(line.equals(OTrainProtocol.GET_PROD_QUEUE)) {
                    writer.println(Craft.listToJson(Server.getInstance().getCraftController().getPlayerCrafts(username)));
                    writer.flush();
                } else if(line.equals(OTrainProtocol.UPGRADE)) {
                    String wagonLine = readLine();
                    if(Server.getInstance().getUpgradeController().tryUpgrade(username, wagonLine)) {
                        writer.println(OTrainProtocol.SUCCESS);
                    } else {
                        writer.println(OTrainProtocol.FAILURE);
                    }
                    writer.flush();
                } else if(line.equals(OTrainProtocol.GET_UPGRADE_QUEUE)) {
                    writer.println(UpgradeWagon.listToJson(Server.getInstance().getUpgradeController().getPlayerUpgrades(username)));
                    writer.flush();
                } else if(line.equals(OTrainProtocol.CREATION)) {
                    String wagonRecipeLine = readLine();
                    if(Server.getInstance().getCreateController().tryCreateWagon(username, wagonRecipeLine)) {
                        writer.println(OTrainProtocol.SUCCESS);
                    } else {
                        writer.println(OTrainProtocol.FAILURE);
                    }
                    writer.flush();
                } else if(line.equals(OTrainProtocol.GET_CREATION_QUEUE)) {
                    writer.println(CreateWagon.listToJson(Server.getInstance().getCreateController().getPlayerCreateWagons(username)));
                    writer.flush();
                }

                line = readLine();
            }
        } catch (IOException e) {
            e.printStackTrace();

            Server.getInstance().removeHandler(this);
        }
    }

    private String readLine() throws IOException {
        String line = reader.readLine();
        LOG.info("Client (" + username + ") : " + line);
        return line;
    }

    private void waitForAuthentication() {
        try {
            boolean logged = false;
            boolean signedUp;
            while(!logged) {
                String request = readLine();
                while(!request.equals(OTrainProtocol.CONNECT) && !request.equals(OTrainProtocol.SIGN_UP)) request = readLine();
                username = readLine();
                String password = readLine();
                if(request.equals(OTrainProtocol.CONNECT)) {
                    logged = db.checkLogin(username, password);
                    writer.println(logged ? OTrainProtocol.SUCCESS : OTrainProtocol.FAILURE);
                    writer.flush();
                } else if(request.equals(OTrainProtocol.SIGN_UP)) {
                    if(username == null || password == null || username.equals("")) {
                        signedUp = false;
                    } else {
                        signedUp = db.insertPlayer(username, password);
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
