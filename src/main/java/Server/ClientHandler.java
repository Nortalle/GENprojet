package Server;

import Game.*;
import Utils.*;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;
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

    public void sendBooleanResult(boolean b) {
        writer.println(b ? OTrainProtocol.SUCCESS : OTrainProtocol.FAILURE);
        writer.flush();
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
                    sendBooleanResult(logged);

                } else if(request.equals(OTrainProtocol.SIGN_UP)) {
                    // check if input are correct for sign up
                    if(username == null || password == null || username.equals("")) signedUp = false;
                    else signedUp = db.insertPlayer(username, password);
                    sendBooleanResult(signedUp);
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

                if(line.equals(OTrainProtocol.GET_TRAIN_STATUS)) {
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
                    //TODO query plus propre pour récupérer la portée de la loco et ses coordonnées a partir du username (voir de get carrément les gares)
                    Train t = db.getTrain(username);
                    writer.println(JsonUtility.listToJson(db.getAllTrainStationsWithinRange(25, t.getTrainStation().getPosX(),t.getTrainStation().getPosY()), TrainStation::toJson));
                    writer.flush();
                } else if(line.equals(OTrainProtocol.GO_TO)) {
                    String newTsLine = readLine();
                    sendBooleanResult(Server.getInstance().getTravelController().ctrlChangeStation(username, newTsLine));
                } else if(line.equals(OTrainProtocol.MINE)) {
                    String wagonLine = readLine();
                    String mineLine = readLine();
                    sendBooleanResult(Server.getInstance().getMineController().tryMine(username, wagonLine, mineLine));
                } else if(line.equals(OTrainProtocol.STOP_MINE)) {
                    String wagonLine = readLine();
                    sendBooleanResult(Server.getInstance().getMineController().removeWagon(wagonLine));
                } else if(line.equals(OTrainProtocol.CRAFT)) {
                    String recipeLine = readLine();
                    String amountLine = readLine();
                    sendBooleanResult(Server.getInstance().getCraftController().tryCraft(username, recipeLine, amountLine));
                } else if(line.equals(OTrainProtocol.GET_PROD_QUEUE)) {
                    writer.println(JsonUtility.listToJson(Server.getInstance().getCraftController().getPlayerCrafts(username), Craft::toJson));
                    writer.flush();
                } else if(line.equals(OTrainProtocol.UPGRADE)) {
                    String wagonLine = readLine();
                    sendBooleanResult(Server.getInstance().getUpgradeController().tryUpgrade(username, wagonLine));
                } else if(line.equals(OTrainProtocol.GET_UPGRADE_QUEUE)) {
                    writer.println(JsonUtility.listToJson(Server.getInstance().getUpgradeController().getPlayerUpgrades(username), UpgradeWagon::toJson));
                    writer.flush();
                } else if(line.equals(OTrainProtocol.CREATION)) {
                    String wagonRecipeLine = readLine();
                    sendBooleanResult(Server.getInstance().getCreateController().tryCreateWagon(username, wagonRecipeLine));
                } else if(line.equals(OTrainProtocol.GET_CREATION_QUEUE)) {
                    writer.println(JsonUtility.listToJson(Server.getInstance().getCreateController().getPlayerCreateWagons(username), CreateWagon::toJson));
                    writer.flush();
                } else if(line.equals(OTrainProtocol.GET_OFFERS)) {
                    int offerTypeLine = Integer.valueOf(readLine());
                    int priceTypeLine = Integer.valueOf(readLine());
                    writer.println(JsonUtility.listToJson(db.getOffers(offerTypeLine, priceTypeLine), Offer::toJson));
                    writer.flush();
                } else if(line.equals(OTrainProtocol.SET_OFFER)) {
                    int offerTypeLine = Integer.valueOf(readLine());
                    int offerAmountLine = Integer.valueOf(readLine());
                    int priceTypeLine = Integer.valueOf(readLine());
                    int priceAmountLine = Integer.valueOf(readLine());
                    sendBooleanResult(db.addOffer(username, offerTypeLine, offerAmountLine, priceTypeLine, priceAmountLine));
                } else if(line.equals(OTrainProtocol.BUY_OFFER)) {
                    int idLine = Integer.valueOf(readLine());
                    sendBooleanResult(db.optionalBuyOffer(username, idLine));
                } else if(line.equals(OTrainProtocol.CANCEL_OFFER)) {
                    int idLine = Integer.valueOf(readLine());
                    sendBooleanResult(db.optionalCancelOffer(idLine));
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

                if(line.equals(OTrainProtocol.GET_ALL_PLAYER)) {
                    writer.println(JsonUtility.listToJson(db.getAllPlayers(), string -> {
                        JsonObject jsonObject = new JsonObject();
                        jsonObject.add("p", new JsonPrimitive(string));
                        return jsonObject;
                    }));
                    writer.flush();
                } else if(line.equals(OTrainProtocol.GET_GARES)) {
                    writer.println(JsonUtility.listToJson(db.getAllTrainStations(), TrainStation::toJson));
                    writer.flush();
                } else if(line.equals(OTrainProtocol.GET_PLAYER_CARGO)) {
                    String playerName = readLine();
                    int currentCargo = 0;
                    for(ResourceAmount ra : db.getPlayerObjects(playerName)) currentCargo += ra.getQuantity();
                    int reservedCargo = Server.getInstance().getReserveCargoController().getReservedCargo(playerName);
                    int maxCargo = WagonStats.getMaxCapacity(db.getTrain(playerName));

                    writer.println(currentCargo + "(" + reservedCargo + ")/" + maxCargo);
                    writer.flush();
                } else if(line.equals(OTrainProtocol.GET_PLAYER_OBJECT)) {
                    String playerName = readLine();
                    String objectLine = readLine();
                    int objectId = Integer.valueOf(objectLine);
                    ResourceAmount ra = db.getPlayerObjectOfType(playerName, objectId).orElse(new ResourceAmount(Ressource.Type.values()[objectId], 0));

                    writer.println(ra.toJson());
                    writer.flush();
                } else if(line.equals(OTrainProtocol.NEW_STATION)) {
                    TrainStation station = new TrainStation(readLine());
                    sendBooleanResult(db.insertTrainStation(station.getPosX(), station.getPosY(), station.getNbOfPlatforms(), station.getSizeOfPlatforms()));
                } else if(line.equals(OTrainProtocol.CHANGE_STATION)) {
                    TrainStation station = new TrainStation(readLine());
                    sendBooleanResult(db.updateTrainStation(station.getId(), station.getPosX(), station.getPosY(), station.getNbOfPlatforms(), station.getSizeOfPlatforms()));
                } else if(line.equals(OTrainProtocol.DELETE_STATION)) {
                    int id = Integer.valueOf(readLine());
                    sendBooleanResult(db.deleteTrainStation(id));
                } else if(line.equals(OTrainProtocol.NEW_MINE)) {
                    Mine mine = new Mine(readLine());
                    sendBooleanResult(db.addMine(mine.getPlace(), mine.getAmount(), mine.getMax(), mine.getRegen(), mine.getResource()) != -1);
                } else if(line.equals(OTrainProtocol.CHANGE_MINE)) {
                    Mine mine = new Mine(readLine());
                    sendBooleanResult(db.updateMine(mine.getId(), mine.getResource(), mine.getAmount(), mine.getMax(), mine.getRegen(), mine.getPlace()));
                } else if(line.equals(OTrainProtocol.DELETE_MINE)) {
                    int id = Integer.valueOf(readLine());
                    sendBooleanResult(db.deleteMine(id));
                } else if(line.equals(OTrainProtocol.CHANGE_PLAYER_OBJECT)) {
                    String playerName = readLine();
                    int type = Integer.valueOf(readLine());
                    AtomicInteger amount = new AtomicInteger(Integer.valueOf(readLine()));//AtomicInteger as a wrapper for modification in lambda
                    db.getPlayerObjectOfType(playerName, type).ifPresent(ra -> amount.set(amount.intValue() - ra.getQuantity()));
                    sendBooleanResult(db.updatePlayerObjects(playerName, type, amount.intValue()));
                } else if(line.equals(OTrainProtocol.DELETE_PLAYER)) {
                    String playerName = readLine();
                    sendBooleanResult(db.deleteUser(playerName));
                }

                line = readLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
            Server.getInstance().removeHandler(this);
        }
    }
}
