package Client;

import Game.*;
import Gui.LoginForm;
import Server.ClientHandler;
import Utils.*;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class Client {
    private static final java.util.logging.Logger LOG = Logger.getLogger(ClientHandler.class.getName());
    private static final ClientLog CLIENT_LOG = new ClientLog();
    static {LOG.addHandler(CLIENT_LOG);}

    private static Client instance;
    private String ipAddress = "localhost";
    private Socket socket;
    private BufferedReader reader;
    private PrintWriter writer;
    private String username;
    private boolean clientLogged;
    private boolean adminLogged;
    private Train train;
    private ArrayList<WagonMining> wagonMining = new ArrayList<>();
    private HashMap<Ressource.Type,ResourceAmount> resourceAmounts = new HashMap<>();
    private ArrayList<Craft> crafts = new ArrayList<>();
    private ArrayList<UpgradeWagon> upgradeWagons = new ArrayList<>();
    private ArrayList<CreateWagon> createWagons = new ArrayList<>();
    //private ArrayList<Train> trainsAtStation = new ArrayList<>();
    private ArrayList<String> trainsAtStation = new ArrayList<>();
    private ArrayList<TrainStation> trainStations = new ArrayList<>();
    private ArrayList<Offer> offers = new ArrayList<>();
    private int baseResources[] = new int[8];

    // start admin
    private ArrayList<TrainStation> adminTrainStations = new ArrayList<>();
    private ArrayList<String> adminPlayers = new ArrayList<>();
    private String adminCargo;
    private ResourceAmount adminResourceAmount;


    private Client(){}

    private void addLocalUpdater() {
        SyncClock.getInstance().addUpdater(new Updater() {
            @Override
            public void sync() {
                updateAll();
            }

            @Override
            public void localUpdate() {

                // 1) localUpdate des crafts en cours //
                ArrayList<Craft> craftToRemove = new ArrayList<>();
                for(int i = 0; i < crafts.size() && i < WagonStats.getMaxParallelCraft(train); i++) {
                    crafts.get(i).decreaseRemainingTime();
                    if(crafts.get(i).getRemainingTime() <= 0){
                        craftToRemove.add(crafts.get(i));
                        // localUpdate de la liste locale de l'inventaire
                        ResourceAmount r = Recipe.getReciepAtIndex(crafts.get(i).getRecipeIndex()).getFinalProduct();
                        resourceAmounts.put(r.getRessource(), resourceAmounts.getOrDefault(r.getRessource(), new ResourceAmount(r.getRessource(), 0)).addQuantity(r.getQuantity()));
                    }
                }
                for(Craft c : craftToRemove) crafts.remove(c);

                // 2) localUpdate des ressources en cours de minage //
                for(WagonMining w : wagonMining) {
                    if(w.isMining()){
                        // la quantité maximale que va miner le wagon
                        int amount = WagonStats.getMiningAmount(w.getWagon());
                        // on déduit de la mine
                        w.getCurrentMine().reduceAmount(amount);

                        // si la mine passe en négatif on corrige
                        if(w.getCurrentMine().getAmount() < 0){
                            amount += w.getCurrentMine().getAmount();
                            w.getCurrentMine().setAmount(0);
                        }

                        // maj des ressources disponibles du joueur
                        Ressource.Type type = Ressource.Type.values()[w.getCurrentMine().getResource()];
                        resourceAmounts.put(type, resourceAmounts.getOrDefault(type, new ResourceAmount(type, 0)).addQuantity(amount));
                    }
                }

                // 3) localUpdate des déplacements de train //
                train.decreaseTrainStationETA(1);

                // 4) localUpdate des améliorations de wagons //
                ArrayList<UpgradeWagon> upgradeWagonToRemove = new ArrayList<>();
                for(UpgradeWagon u : upgradeWagons) {
                    u.decreaseRemainingTime();
                    if(u.getRemainingTime() <= 0) {
                        upgradeWagonToRemove.add(u);
                        u.getWagon_to_upgrade().levelUp();
                    }
                }
                for(UpgradeWagon u : upgradeWagonToRemove) upgradeWagons.remove(u);

                // 4) localUpdate des améliorations de wagons //
                ArrayList<CreateWagon> createWagonToRemove = new ArrayList<>();
                for(CreateWagon c : createWagons) {
                    c.decreaseRemainingTime();
                    if(c.getRemainingTime() <= 0) {
                        createWagonToRemove.add(c);
                        // this line may bug because final product id is -1
                        //train.getWagons().add(WagonRecipe.getAllRecipes().get(c.getWagonRecipeIndex()).getFinalProduct());
                    }
                }
                for(CreateWagon c : createWagonToRemove) createWagons.remove(c);
            }
        });
    }

    public void updateAdminTrainStations() {
        writer.println(OTrainProtocol.GET_GARES);
        writer.flush();
        String answer = readLine();
        adminTrainStations = JsonUtility.listFromJson((JsonArray) JsonUtility.fromJson(answer), TrainStation::new);
    }

    public ArrayList<TrainStation> getAdminTrainStations() {
        return adminTrainStations;
    }

    public void updateAdminPlayers() {
        writer.println(OTrainProtocol.GET_ALL_PLAYER);
        writer.flush();
        String answer = readLine();
        adminPlayers = JsonUtility.listFromJson((JsonArray) JsonUtility.fromJson(answer), p -> p.get("p").getAsString());
    }

    public ArrayList<String> getAdminPlayers() {
        return adminPlayers;
    }

    public void updateAdminCargo(String playerName) {
        writer.println(OTrainProtocol.GET_PLAYER_CARGO);
        writer.println(playerName);
        writer.flush();
        String answer = readLine();
        //adminCargo = JsonUtility.listFromJson((JsonArray) JsonUtility.fromJson(answer), ResourceAmount::new);
        adminCargo = answer;
    }

    public String getAdminCargo() {
        return adminCargo;
    }

    public void updateAdminResourceAmount(String playerName, int type) {
        writer.println(OTrainProtocol.GET_PLAYER_OBJECT);
        writer.println(playerName);
        writer.println(type);
        writer.flush();
        String answer = readLine();
        adminResourceAmount = new ResourceAmount(answer);
    }

    public ResourceAmount getAdminResourceAmount() {
        return adminResourceAmount;
    }

    public void updateAdminAll() {
        updateAdminTrainStations();
        updateAdminPlayers();
    }

    public boolean isClientLogged() {
        return clientLogged;
    }

    public void setClientLogged(boolean clientLogged) {
        if(clientLogged) addLocalUpdater();
        this.clientLogged = clientLogged;
    }

    public boolean isAdminLogged() {
        return adminLogged;
    }

    public void setAdminLogged(boolean adminLogged) {
        this.adminLogged = adminLogged;
    }

    public String sendNewStation(TrainStation station) {
        writer.println(OTrainProtocol.NEW_STATION);
        writer.println(station.toJson());
        writer.flush();
        return readLine();
    }

    public String sendChangeStation(TrainStation station) {
        writer.println(OTrainProtocol.CHANGE_STATION);
        writer.println(station.toJson());
        writer.flush();
        return readLine();
    }

    public String sendDeleteStation(int id) {
        writer.println(OTrainProtocol.DELETE_STATION);
        writer.println(id);
        writer.flush();
        return readLine();
    }

    public String sendNewMine(Mine mine) {
        writer.println(OTrainProtocol.NEW_MINE);
        writer.println(mine.toJson());
        writer.flush();
        return readLine();
    }

    public String sendChangeMine(Mine mine) {
        writer.println(OTrainProtocol.CHANGE_MINE);
        writer.println(mine.toJson());
        writer.flush();
        return readLine();
    }

    public String sendDeleteMine(int id) {
        writer.println(OTrainProtocol.DELETE_MINE);
        writer.println(id);
        writer.flush();
        return readLine();
    }

    public String sendChangePlayerObject(String playerName, int type, int amount) {
        writer.println(OTrainProtocol.CHANGE_PLAYER_OBJECT);
        writer.println(playerName);
        writer.println(type);
        writer.println(amount);
        writer.flush();
        return readLine();
    }

    public String sendDeletePlayer(String playerName) {
        writer.println(OTrainProtocol.DELETE_PLAYER);
        writer.println(playerName);
        writer.flush();
        return readLine();
    }

    // end admin

    public static Client getInstance() {
        if(instance == null) instance = new Client();
        return instance;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public static void setClientLogComponent(JTextArea component) {
        CLIENT_LOG.setComponent(component);
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public boolean setIpAddress(String newIp) {
        ipAddress = newIp;
        return connectServer();
    }

    //GUI
    private JFrame frame;

    public boolean connectServer() {
        try {
            socket = new Socket(ipAddress, OTrainProtocol.PORT);
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            writer = new PrintWriter(socket.getOutputStream());
            train = new Train();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public void disconnect() {
        try {
            clientLogged = false;
            adminLogged = false;
            SyncClock.getInstance().removeAllUpdaters();
            socket.close();
            reader.close();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void startingFrame() {
        frame = new JFrame("OTrain");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setConnectionPanel();
    }

    public void setConnectionPanel() {
        setFrameContent(new LoginForm().getPanel_main());
        frame.setVisible(true);
        connectServer();
    }

    public void setFrameContent(JPanel panel, Dimension d) {
        frame.setContentPane(panel);
        frame.setSize(d);
    }

    public void setFrameContent(JPanel panel) {
        frame.setContentPane(panel);
        frame.pack();
    }

    public int getSpecificResource(Ressource.Type type) {
        if(resourceAmounts.containsKey(type)){
            return resourceAmounts.get(type).getQuantity();
        }
        return 0;
    }

    public String readLine() {
        String line = "ERROR";
        try {
            line = reader.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
        LOG.info(line);
        return line;
    }

    public void sendLine(String line) {
        writer.println(line);
        writer.flush();
    }

    public String sendLogin(String username, String password) {
        writer.println(OTrainProtocol.CONNECT);
        writer.println(username);
        writer.println(password);
        writer.flush();
        return readLine();
    }

    public String signUp(String username, String password) {
        writer.println(OTrainProtocol.SIGN_UP);
        writer.println(username);
        writer.println(password);
        writer.flush();
        return readLine();
    }

    public void updateResourceAmount() {
        writer.println(OTrainProtocol.GET_OBJECTS);
        writer.flush();
        String answer = readLine();
        ArrayList<ResourceAmount> list = JsonUtility.listFromJson((JsonArray) JsonUtility.fromJson(answer), ResourceAmount::new);
        resourceAmounts.clear();

        list.forEach(r -> resourceAmounts.put(r.getRessource(), r));
    }

    public ArrayList<ResourceAmount> getResourceAmounts() {
        //ArrayList<ResourceAmount> list = new ArrayList<>();
        //resourceAmounts.forEach((k,v) -> list.add(v));
        //return list;

        return resourceAmounts.entrySet().stream()
                .map(Map.Entry::getValue)
                .sorted(Comparator.comparing(r -> r.getRessource().ordinal()))
                .collect(Collectors.toCollection(ArrayList::new));

    }

    public void updateCrafts() {
        writer.println(OTrainProtocol.GET_PROD_QUEUE);
        writer.flush();
        String answer = readLine();
        crafts =  JsonUtility.listFromJson((JsonArray) JsonUtility.fromJson(answer), Craft::new);
    }

    public ArrayList<Craft> getCrafts() {
        return crafts;
    }

    public void updateUpgradeWagons() {
        writer.println(OTrainProtocol.GET_UPGRADE_QUEUE);
        writer.flush();
        String answer = readLine();
        upgradeWagons = JsonUtility.listFromJson((JsonArray) JsonUtility.fromJson(answer), UpgradeWagon::new);
    }

    public ArrayList<UpgradeWagon> getUpgradeWagons() {
        return upgradeWagons;
    }

    public void updateCreateWagons() {
        writer.println(OTrainProtocol.GET_CREATION_QUEUE);
        writer.flush();
        String answer = readLine();
        createWagons = JsonUtility.listFromJson((JsonArray) JsonUtility.fromJson(answer), CreateWagon::new);
    }

    public ArrayList<CreateWagon> getCreateWagons() {
        return createWagons;
    }

    public void updateTrain() {
        writer.println(OTrainProtocol.GET_TRAIN_STATUS);
        writer.flush();
        String answer = readLine();
        train.fromJson((JsonObject) JsonUtility.fromJson(answer));

        for(WagonMining w : wagonMining){
            w.linkWagonToTrain(train);
            w.linkMineToTrain(train);
        }
    }

    public Train getTrain() {
        return train;
    }

    public void updateWagonMining() {
        writer.println(OTrainProtocol.MINE_INFO);
        writer.flush();
        String answer = readLine();
        wagonMining = JsonUtility.listFromJson((JsonArray) JsonUtility.fromJson(answer), WagonMining::new);
    }

    public ArrayList<WagonMining> getWagonMining() {
        return wagonMining;
    }

    public void updateTrainsAtStation(int stationId) {
        writer.println(OTrainProtocol.GET_TRAINS_AT);
        writer.println(stationId);
        writer.flush();
        String answer = readLine();
        trainsAtStation = JsonUtility.listFromJson((JsonArray) JsonUtility.fromJson(answer), p -> p.get("p").getAsString());
    }

    public ArrayList<String> getTrainsAtStation(int stationId) {
        return trainsAtStation;
    }

    public void updateTrainStations() {
        writer.println(OTrainProtocol.GET_GARES);
        writer.flush();
        String answer = readLine();
        trainStations = JsonUtility.listFromJson((JsonArray) JsonUtility.fromJson(answer), TrainStation::new);
    }

    public ArrayList<TrainStation> getTrainStations() {
        return trainStations;
    }


    public synchronized void updateAll() {
        if(!clientLogged) return;
        updateWagonMining();
        updateTrain();          // doit se faire après updateWagonMining;
        updateResourceAmount();
        updateCrafts();
        updateUpgradeWagons();
        updateCreateWagons();
        updateTrainStations();
        updateTrainsAtStation(viewingStation);
    }

    // not very good but should be working
    public static int viewingStation = 0;
    //

    public void updateOffers(int offerType, int priceType) {
        writer.println(OTrainProtocol.GET_OFFERS);
        writer.println(offerType);
        writer.println(priceType);
        writer.flush();
        String answer = readLine();
        offers = JsonUtility.listFromJson((JsonArray) JsonUtility.fromJson(answer), Offer::new);
    }

    public ArrayList<Offer> getOffers() {
        return offers;
    }

    public String placeOffer(int offerType, int offerAmount, int priceType, int priceAmount) {
        writer.println(OTrainProtocol.SET_OFFER);
        writer.println(offerType);
        writer.println(offerAmount);
        writer.println(priceType);
        writer.println(priceAmount);
        writer.flush();
        return readLine();
    }

    public String buyOffer(int id) {
        writer.println(OTrainProtocol.BUY_OFFER);
        writer.println(id);
        writer.flush();
        return readLine();
    }

    public String cancelOffer(int id) {
        writer.println(OTrainProtocol.CANCEL_OFFER);
        writer.println(id);
        writer.flush();
        return readLine();
    }

    public String changeStation(int stationId) {
        writer.println(OTrainProtocol.GO_TO);
        writer.println(stationId);
        writer.flush();
        return readLine();
    }

    public String startMining(int wagonId, int mineId) {
        writer.println(OTrainProtocol.MINE);
        writer.println(wagonId);
        writer.println(mineId);
        writer.flush();
        return readLine();
    }

    public String stopMining(int wagonId) {
        writer.println(OTrainProtocol.STOP_MINE);
        writer.println(wagonId);
        writer.flush();
        return readLine();
    }

    public String startCraft(int recipeIndex, int amount) {
        writer.println(OTrainProtocol.CRAFT);
        writer.println(recipeIndex);
        writer.println(amount);
        writer.flush();
        return readLine();
    }

    public String startUpgrade(int wagonId) {
        writer.println(OTrainProtocol.UPGRADE);
        writer.println(wagonId);
        writer.flush();
        return readLine();
    }

    public String startCreation(int wagonRecipeIndex) {
        writer.println(OTrainProtocol.CREATION);
        writer.println(wagonRecipeIndex);
        writer.flush();
        return readLine();
    }

    public static void main(String ... args) {
        Client.getInstance().startingFrame();
    }
}
