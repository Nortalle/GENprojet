package Server;

import Game.*;
import Utils.JsonUtility;
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
        waitForAuthentication();
        boolean isAdmin = false;
        for(String adminName : Server.ADMINS_USERNAME) {
            if(username.equals(adminName)) {
                isAdmin = true;
                break;
            }
        }

        if(isAdmin) {
            writer.println(OTrainProtocol.ADMIN);
            writer.flush();
            handleAdmin();
        } else {
            writer.println(OTrainProtocol.PLAYER);
            writer.flush();
            handleClient();
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
                // wait to get connection or sign up request
                while(!request.equals(OTrainProtocol.CONNECT) && !request.equals(OTrainProtocol.SIGN_UP)) request = readLine();
                // get username and password
                username = readLine();
                String password = readLine();

                if(request.equals(OTrainProtocol.CONNECT)) {
                    // check if input are correct for login
                    logged = db.checkLogin(username, password);
                    writer.println(logged ? OTrainProtocol.SUCCESS : OTrainProtocol.FAILURE);
                    writer.flush();

                } else if(request.equals(OTrainProtocol.SIGN_UP)) {
                    // check if input are correct for sign up
                    if(username == null || password == null || username.equals("")) signedUp = false;
                    else signedUp = db.insertPlayer(username, password);
                    writer.println(signedUp ? OTrainProtocol.SUCCESS : OTrainProtocol.FAILURE);
                    writer.flush();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            Server.getInstance().removeHandler(this);
        }
    }

    private void handleClient() {
        try {
            String line = readLine();
            while (running && line != null) {
                //work...

                if(line.equals(OTrainProtocol.GET_RESSOURCES)) {
                    int r[] = db.getPlayerResourcesViaObjects(username);// temp solution
                    Resources resources = new Resources(r);
                    writer.println(resources.toJson());
                    writer.flush();
                } else if(line.equals(OTrainProtocol.GET_TRAIN_STATUS)) {
                    writer.println(db.getTrain(username).toJson());
                    writer.flush();
                } else if(line.equals(OTrainProtocol.GET_OBJECTS)) {
                    writer.println(JsonUtility.listToJson(db.getPlayerObjects(username), ResourceAmount::toJson));
                    writer.flush();
                } else if(line.equals(OTrainProtocol.GET_TRAINS_AT)) {
                    int stationId = Integer.valueOf(readLine());
                    writer.println(JsonUtility.listToJson(db.getAllTrainsAtStation(stationId), Train::toJson));
                    writer.flush();
                } else if(line.equals(OTrainProtocol.MINE_INFO)) {
                    writer.println(JsonUtility.listToJson(Server.getInstance().getMineController().getPlayerWagonMining(username), WagonMining::toJson));
                    writer.flush();
                } else if(line.equals(OTrainProtocol.GET_GARES)) {
                    writer.println(JsonUtility.listToJson(db.getAllTrainStations(), TrainStation::toJson));
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
                    writer.println(JsonUtility.listToJson(Server.getInstance().getCraftController().getPlayerCrafts(username), Craft::toJson));
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
                    writer.println(JsonUtility.listToJson(Server.getInstance().getUpgradeController().getPlayerUpgrades(username), UpgradeWagon::toJson));
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
                    writer.println(JsonUtility.listToJson(Server.getInstance().getCreateController().getPlayerCreateWagons(username), CreateWagon::toJson));
                    writer.flush();
                }

                line = readLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
            Server.getInstance().removeHandler(this);
        }
    }

    private void handleAdmin() {
        try {
            String line = readLine();
            while (running && line != null) {
                //work...

                if(line.equals(OTrainProtocol.SUCCESS)) {
                    writer.println("if");
                    writer.flush();
                } else if(line.equals(OTrainProtocol.FAILURE)) {
                    writer.println("else if");
                    writer.flush();
                }

                line = readLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
            Server.getInstance().removeHandler(this);
        }
    }
}
