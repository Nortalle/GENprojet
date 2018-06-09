package Server;

import Game.*;
import Utils.ResourceAmount;
import Utils.Ressource;
import Utils.WagonStats;

import java.sql.*;
import java.util.ArrayList;
import java.util.Random;
import java.util.Optional;

public class DataBase {
    private Connection connection;
    private final static String DEFAULT_URL = "jdbc:mysql://localhost:3306/GEN_otrain?user=root&password=root";

    public DataBase() {
        this(DEFAULT_URL);
    }

    public DataBase(String url) {
        try {
            connection = DriverManager.getConnection(url);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * @param username new admin
     * @param password password
     * @return if admin has been inserted
     */
    public boolean insertAdmin(String username, String password) {
        try {
            PreparedStatement ps = connection.prepareStatement("INSERT INTO Utilisateur VALUES(?,?);", Statement.RETURN_GENERATED_KEYS);
            ps.setObject(1, username);
            ps.setObject(2, password);
            int status = ps.executeUpdate();
            if (status == 0) return false;

            ps = connection.prepareStatement("INSERT INTO Admin VALUES(?);", Statement.RETURN_GENERATED_KEYS);
            ps.setObject(1, username);
            status = ps.executeUpdate();
            return status != 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * @param username new player
     * @param password password
     * @return if user has been inserted (false if username already is data base)
     */
    public boolean insertPlayer(String username, String password) {
        try {
            PreparedStatement ps = connection.prepareStatement("INSERT INTO Utilisateur VALUES(?,?);", Statement.RETURN_GENERATED_KEYS);
            ps.setObject(1, username);
            ps.setObject(2, password);
            int status = ps.executeUpdate();
            if(status == 0) return false;

            ps = connection.prepareStatement("INSERT INTO Joueur VALUES(?);", Statement.RETURN_GENERATED_KEYS);
            ps.setObject(1, username);
            status = ps.executeUpdate();
            if(status == 0) return false;

            ps = connection.prepareStatement("INSERT INTO Train VALUES(?,'Tom',?);", Statement.RETURN_GENERATED_KEYS);
            ps.setObject(1, username);
            ps.setObject(2, getStartingStationId());
            status = ps.executeUpdate();
            if(status == 0) return false;

            //DEFAULT LOCO + DRILL + CARGO//
            ps = connection.prepareStatement("INSERT INTO Wagon VALUES(default,?,2000,1,?);", Statement.RETURN_GENERATED_KEYS);
            ps.setObject(1, username);
            ps.setObject(2, WagonStats.WagonType.LOCO.ordinal());
            status = ps.executeUpdate();
            if(status == 0) return false;

            ps = connection.prepareStatement("INSERT INTO Wagon VALUES(default,?,2000,1,?);", Statement.RETURN_GENERATED_KEYS);
            ps.setObject(1, username);
            ps.setObject(2, WagonStats.WagonType.DRILL.ordinal());
            status = ps.executeUpdate();
            if(status == 0) return false;

            ps = connection.prepareStatement("INSERT INTO Wagon VALUES(default,?,2000,1,?);", Statement.RETURN_GENERATED_KEYS);
            ps.setObject(1, username);
            ps.setObject(2, WagonStats.WagonType.CARGO.ordinal());
            status = ps.executeUpdate();
            if(status == 0) return false;

            ps = connection.prepareStatement("INSERT INTO Wagon VALUES(default,?,2000,1,?);", Statement.RETURN_GENERATED_KEYS);
            ps.setObject(1, username);
            ps.setObject(2, WagonStats.WagonType.CRAFT.ordinal());
            status = ps.executeUpdate();
            return status != 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * @param username username
     * @return if user was delete
     */
    public boolean deleteUser(String username) {
        try {
            PreparedStatement ps = connection.prepareStatement("DELETE FROM Utilisateur WHERE nomUtilisateur=?;", Statement.RETURN_GENERATED_KEYS);
            ps.setObject(1, username);
            int status = ps.executeUpdate();
            return status == 1;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * @return list of all username
     */
    public ArrayList<String> getAllUsers() {
        ArrayList<String> result = new ArrayList<>();
        try {
            ResultSet resultSet;
            PreparedStatement ps = connection.prepareStatement("SELECT nomUtilisateur FROM Utilisateur;", Statement.RETURN_GENERATED_KEYS);
            resultSet = ps.executeQuery();
            while(resultSet.next()) {
                result.add(resultSet.getString(1));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * @return list of all players
     */
    public ArrayList<String> getAllPlayers() {
        ArrayList<String> result = new ArrayList<>();
        try {
            ResultSet resultSet;
            PreparedStatement ps = connection.prepareStatement("SELECT nomJoueur FROM Joueur;", Statement.RETURN_GENERATED_KEYS);
            resultSet = ps.executeQuery();
            while(resultSet.next()) {
                result.add(resultSet.getString(1));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * @param username player
     * @param password password
     * @return if login was successful
     */
    public boolean checkLogin(String username, String password) {
        try {
            ResultSet resultSet;
            PreparedStatement ps = connection.prepareStatement("SELECT motDePasse FROM Utilisateur WHERE nomUtilisateur=?", Statement.RETURN_GENERATED_KEYS);
            ps.setObject(1, username);
            resultSet = ps.executeQuery();
            if(resultSet.next()) return password.equals(resultSet.getString(1));
        }catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * @param wagonId DataBase wagon id
     * @return username
     */
    public Optional<String> getUsernameByWagonId(int wagonId) {
        try {
            ResultSet resultSet;
            PreparedStatement ps = connection.prepareStatement("SELECT proprietaire FROM Wagon WHERE id=?", Statement.RETURN_GENERATED_KEYS);
            ps.setObject(1, wagonId);
            resultSet = ps.executeQuery();
            if(resultSet.next()) {
                return Optional.of(resultSet.getString(1));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    /**
     * @param username player
     * @return how many resources the player's train is carrying + reserved cargo
     */
    private int getPlayerCurrentCargoAmount(String username) {
        int currentCargo = 0;
        for(ResourceAmount ra : getPlayerObjects(username)) currentCargo += ra.getQuantity();
        return currentCargo + Server.getInstance().getReserveCargoController().getReservedCargo(username);
    }

    /**
     * @param username player
     * @return all objects (resources) the player have
     */
    public ArrayList<ResourceAmount> getPlayerObjects(String username) {
        ArrayList<ResourceAmount> resourceAmounts = new ArrayList<>();
        try {
            ResultSet resultSet;
            PreparedStatement ps = connection.prepareStatement("SELECT * FROM ObjetsParJoueur WHERE nomJoueur=?", Statement.RETURN_GENERATED_KEYS);
            ps.setObject(1, username);
            resultSet = ps.executeQuery();
            while(resultSet.next()) {
                //int id = resultSet.getInt(1);
                //String user = resultSet.getString(2);
                int typeId = resultSet.getInt(3);
                int amount = resultSet.getInt(4);
                resourceAmounts.add(new ResourceAmount(Ressource.Type.values()[typeId], amount));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return resourceAmounts;
    }

    /**
     *
     * @param username player name
     * @param typeId id of the type of the object you want
     * @return Type, Amount
     */
    public Optional<ResourceAmount> getPlayerObjectOfType(String username, int typeId) {
        try {
            ResultSet resultSet;
            PreparedStatement ps = connection.prepareStatement("SELECT * FROM ObjetsParJoueur WHERE nomJoueur=? AND objetId=?", Statement.RETURN_GENERATED_KEYS);
            ps.setObject(1, username);
            ps.setObject(2, typeId);
            resultSet = ps.executeQuery();
            if(resultSet.next()) {
                int type = resultSet.getInt(3);
                int amount = resultSet.getInt(4);
                return Optional.of(new ResourceAmount(Ressource.Type.values()[type], amount));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    /**
     * @param username player
     * @param amount how much you want to add or remove
     * @return how much you actually can add or remove
     */
    public int canUpdatePlayerObjects(String username, int amount) {
        int maxChange = amount;

        Train train = getTrain(username).orElse(new Train());
        int MAX = WagonStats.getMaxCapacity(train);
        int MIN = 0;
        int currentAmount = getPlayerCurrentCargoAmount(username);
        if(currentAmount > MAX || currentAmount < MIN) return 0;
        int newAmount =  currentAmount + amount;
        if(newAmount > MAX) maxChange = MAX - currentAmount;
        if(newAmount < MIN) maxChange = MIN - currentAmount;

        return maxChange;
    }

    /**
     * if you have reserved slot sou should be able to add what you want
     * @param username player
     * @param amount how much you want to add or remove
     * @return how much you actually can add or remove
     */
    @Deprecated
    public int canUpdatePlayerObjectsOnReservedCargo(String username, int amount, int reservedCargo) {
        return Math.max(canUpdatePlayerObjects(username, amount), reservedCargo);
    }

    /**
     * @param username player
     * @param typeId id of resource
     * @param amount how much to change
     * @return if change has been apply
     */
    public boolean updatePlayerObjects(String username, int typeId, int amount) {
        try {
            PreparedStatement ps;
            Optional<ResourceAmount> existingEntry = getPlayerObjectOfType(username, typeId);// maybe we can do better
            if(!existingEntry.isPresent()) {
                ps = connection.prepareStatement("INSERT INTO ObjetsParJoueur VALUES(default,?,?,?)", Statement.RETURN_GENERATED_KEYS);
                ps.setObject(1, username);
                ps.setObject(2, typeId);
                ps.setObject(3, amount);
            } else if (existingEntry.get().getQuantity() + amount == 0) {
                ps = connection.prepareStatement("DELETE FROM ObjetsParJoueur WHERE nomJoueur=? AND objetId=?", Statement.RETURN_GENERATED_KEYS);
                ps.setObject(1, username);
                ps.setObject(2, typeId);
                ps.executeUpdate();
            } else {
                ps = connection.prepareStatement("UPDATE ObjetsParJoueur SET objetAmount=? WHERE nomJoueur=? AND objetId=?", Statement.RETURN_GENERATED_KEYS);
                ps.setObject(1, existingEntry.get().getQuantity() + amount);
                ps.setObject(2, username);
                ps.setObject(3, typeId);
            }

            int status = ps.executeUpdate();
            return status != 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // TRAIN REQUESTS

    /**
     * @param username player owner of the train
     * @return the player's train
     */
    public Optional<Train> getTrain(String username) {
        try {
            ResultSet resultSet;
            PreparedStatement ps = connection.prepareStatement("SELECT * FROM Train WHERE `proprietaire`=?;", Statement.RETURN_GENERATED_KEYS);
            ps.setObject(1, username);
            resultSet = ps.executeQuery();
            if(resultSet.next()){
                //String user = resultSet.getString("proprietaire");
                //String name = resultSet.getString("nom");
                int currentTs = resultSet.getInt("gareActuelle");

                int eta[] = Server.getInstance().getTravelController().getETA(username);
                return Optional.of(new Train(getAllWagons(username), getTrainStation(currentTs).orElse(new TrainStation()), eta[0], eta[1]));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    /**
     *
     * @param stationId id of the train station
     * @return a list of all the trains in the station
     */
    public ArrayList<Train> getAllTrainsAtStation(int stationId) {
        ArrayList<Train> trains = new ArrayList<>();
        try {
            ResultSet resultSet;
            PreparedStatement ps = connection.prepareStatement("SELECT * FROM Train WHERE `gareActuelle`=?;", Statement.RETURN_GENERATED_KEYS);
            ps.setObject(1, stationId);
            resultSet = ps.executeQuery();
            TrainStation currentTs = getTrainStation(stationId).orElse(new TrainStation());
            while(resultSet.next()){
                String user = resultSet.getString("proprietaire");
                //String name = resultSet.getString("nom");
                //int ts = resultSet.getInt("gareActuelle");

                int eta[] = Server.getInstance().getTravelController().getETA(user);

                trains.add(new Train(getAllWagons(user), currentTs, eta[0], eta[1]));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return trains;
    }

    /**
     *
     * @param stationId id of the train station
     * @return a list of all player name in the station
     */
    public ArrayList<String> getAllPlayersAtStation(int stationId) {
        ArrayList<String> players = new ArrayList<>();
        try {
            ResultSet resultSet;
            PreparedStatement ps = connection.prepareStatement("SELECT * FROM Train WHERE gareActuelle=?;", Statement.RETURN_GENERATED_KEYS);
            ps.setObject(1, stationId);
            resultSet = ps.executeQuery();
            while(resultSet.next()){
                String user = resultSet.getString("proprietaire");
                //String name = resultSet.getString("nom");
                //int ts = resultSet.getInt("gareActuelle");
                //int eta[] = Server.getInstance().getTravelController().getETA(user);

                players.add(user);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return players;
    }

    // WAGON REQUESTS

    /**
     * @param username the player owner of the train
     * @param weight the weight of the wagon
     * @param level the level of the wagon
     * @param type the type of the wagon
     * @return true if the wagon has been created, else false
     */
    public int addWagon(String username, int weight, int level, int type){
        int result = -1;
        try {
            PreparedStatement ps = connection.prepareStatement("INSERT INTO Wagon VALUES(default,?,?,?,?);", Statement.RETURN_GENERATED_KEYS);
            ps.setObject(1, username);
            ps.setObject(2, weight);
            ps.setObject(3, level);
            ps.setObject(4, type);
            int status = ps.executeUpdate();
            if(status != 0){
                ResultSet resultSet = ps.getGeneratedKeys();
                resultSet.next();
                result = resultSet.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * @param username new owner
     * @param weight new weight
     * @param level new level
     * @param type new type
     * @param id DataBase id of the wagon
     * @return if localUpdate has been applied
     */
    public boolean updateWagon(String username, int weight, int level, int type, int id){
        try {
            PreparedStatement ps = connection.prepareStatement("UPDATE Wagon SET proprietaire=?, poids=?, niveau=?, typeID=? WHERE id=?;", Statement.RETURN_GENERATED_KEYS);
            ps.setObject(1, username);
            ps.setObject(2, weight);
            ps.setObject(3, level);
            ps.setObject(4, type);
            ps.setObject(5, id);
            int status = ps.executeUpdate();
            return status != 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * @param id wagon DataBase id
     * @param level new level
     * @return if localUpdate has been applied
     */
    public boolean updateWagonLevel(int id, int level){
        try {
            PreparedStatement ps = connection.prepareStatement("UPDATE Wagon SET niveau=? WHERE id=?;", Statement.RETURN_GENERATED_KEYS);
            ps.setObject(1, level);
            ps.setObject(2, id);
            int status = ps.executeUpdate();
            return status != 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     *
     * @param username player owner of the train
     * @return the liste of all the wagons of the train
     */
    private ArrayList<Wagon> getAllWagons(String username){
        ArrayList<Wagon> result = new ArrayList<>();
        try {
            ResultSet resultSet;
            PreparedStatement ps = connection.prepareStatement("SELECT * FROM Wagon WHERE proprietaire=?;", Statement.RETURN_GENERATED_KEYS);
            ps.setObject(1, username);
            resultSet = ps.executeQuery();
            while(resultSet.next()) {
                int id = resultSet.getInt("id");
                int weight = resultSet.getInt("poids");
                int level = resultSet.getInt("niveau");
                int typeID = resultSet.getInt("typeID");

                Wagon wagon = new Wagon(id, weight, level, WagonStats.WagonType.values()[typeID]);
                result.add(wagon);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * @param id wagon DataBase id
     * @return the wagon of this id
     */
    public Optional<Wagon> getWagon(int id){
        try {
            ResultSet resultSet;
            PreparedStatement ps = connection.prepareStatement("SELECT * FROM Wagon WHERE id=?");
            ps.setObject(1, id);
            resultSet = ps.executeQuery();
            if(resultSet.next()) {
                int idWagon = resultSet.getInt("id");
                int weight = resultSet.getInt("poids");
                int level = resultSet.getInt("niveau");
                int typeID = resultSet.getInt("typeID");

                return Optional.of(new Wagon(idWagon, weight, level, WagonStats.WagonType.values()[typeID]));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    /**
     * @param username player
     * @return the player loco (null if not found)
     */
    private Optional<Wagon> getPlayerLoco(String username){
        try {
            ResultSet resultSet;
            PreparedStatement ps = connection.prepareStatement("SELECT * FROM Wagon WHERE `proprietaire`=? AND `typeID`=?");
            ps.setObject(1, username);
            ps.setObject(2, WagonStats.WagonType.LOCO);
            resultSet = ps.executeQuery();
            if(resultSet.next()) {
                int idWagon = resultSet.getInt("id");
                int weight = resultSet.getInt("poids");
                int level = resultSet.getInt("niveau");
                int typeID = resultSet.getInt("typeID");

                return Optional.of(new Wagon(idWagon, weight, level,  WagonStats.WagonType.values()[typeID]));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    // STATION REQUESTS

    /**
     * @param tsId the id of the station
     * @return the station corresponding to the id
     */
    public Optional<TrainStation> getTrainStation(int tsId){
        try {
            ResultSet resultSet;
            PreparedStatement ps = connection.prepareStatement("SELECT * FROM Gare WHERE `id`=?;", Statement.RETURN_GENERATED_KEYS);
            ps.setObject(1, tsId);
            resultSet = ps.executeQuery();
            if(resultSet.next()) {
                int id = resultSet.getInt("id");
                int posX = resultSet.getInt("posX");
                int posY = resultSet.getInt("posY");
                int nbOfPlatforms = resultSet.getInt("nbrQuai");
                int sizeOfPlatforms = resultSet.getInt("tailleQuai");
                ArrayList<Mine> mines = getAllMinesOfStation(tsId);

                return Optional.of(new TrainStation(id, posX, posY, nbOfPlatforms, sizeOfPlatforms, mines));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    /**
     * gets station from pos
     * @param x pos x
     * @param y pos y
     * @return the corresponding station
     */
    private TrainStation getTrainStationByPos(int x, int y) {
        TrainStation trainStation = null;// TODO OPTIONAL<T>
        try {
            ResultSet resultSet;
            PreparedStatement ps = connection.prepareStatement("SELECT * FROM Gare WHERE `posX`=? AND `posY`=?;", Statement.RETURN_GENERATED_KEYS);
            ps.setObject(1, x);
            ps.setObject(2, y);
            resultSet = ps.executeQuery();
            if(resultSet.next()) {
                int id = resultSet.getInt("id");
                int posX = resultSet.getInt("posX");
                int posY = resultSet.getInt("posY");
                int nbOfPlatforms = resultSet.getInt("nbrQuai");
                int sizeOfPlatforms = resultSet.getInt("tailleQuai");
                ArrayList<Mine> mines = getAllMinesOfStation(id);

                trainStation = new TrainStation(id, posX, posY, nbOfPlatforms, sizeOfPlatforms, mines);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return trainStation;
    }

    /**
     * all station at range of range from pos x,y
     * Creates them if they don't exist in database
     * @param range the search distance
     * @param x pos x
     * @param y pos y
     * @return all stations at range
     */
    public ArrayList<TrainStation> getAllTrainStationsWithinRange(int range, int x, int y){
        ArrayList<TrainStation> result = new ArrayList<>();
        try {
            ResultSet resultSet;
            PreparedStatement ps = connection.prepareStatement("SELECT * FROM Gare WHERE posX BETWEEN ? AND ? AND posY BETWEEN ? AND ?;", Statement.RETURN_GENERATED_KEYS);
            ps.setObject(1, x - range);
            ps.setObject(2, x + range);
            ps.setObject(3, y - range);
            ps.setObject(4, y + range);

            resultSet = ps.executeQuery();
            while(resultSet.next()) {
                int id = resultSet.getInt("id");
                int posX = resultSet.getInt("posX");
                int posY = resultSet.getInt("posY");
                int nbOfPlatforms = resultSet.getInt("nbrQuai");
                int sizeOfPlatforms = resultSet.getInt("tailleQuai");
                ArrayList<Mine> mines = getAllMinesOfStation(id);

                result.add(new TrainStation(id, posX, posY, nbOfPlatforms, sizeOfPlatforms, mines));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }


        Random r = new Random();
        // parcours de toutes les gares qui devraient s'y trouver et check si elles existent déjà
        for(int xi = x - range; xi < x + range; xi++ ){
            r.setSeed(xi);
            for(int yi = y -range; yi < y + range; yi++){
                r.setSeed(r.nextInt() + yi);
                boolean isStation = r.nextInt(1000) < 2;   // séquence déterministe pour savoir si il y a une gare a cet emplacement
                if(isStation){
                    //TODO remove le debug
                    System.out.println("New Station at (" + xi + ":" + yi + ")");
                    boolean found = false;
                    for(TrainStation ts : result){
                        if(ts.getPosX() == xi && ts.getPosY() == yi){
                            found = true;
                            break;
                        }
                    }
                    // si elle y est pas on doit la rajouter
                    if(!found){
                        int xiabs = Math.abs(xi);
                        int yiabs = Math.abs(yi);
                        // ajout de la gare
                        insertTrainStation(xi,
                                yi,
                                1 + r.nextInt(1 + 5 - Math.min(((xiabs + yiabs) / 30), 5)) ,
                                5 + ((xiabs + yiabs) / 100) + r.nextInt(5 + Math.min(((xiabs + yiabs) / 30), 50))
                                );
                        TrainStation station = getTrainStationByPos(xi, yi);
                        // ajout des mines
                        int nbMines = 1 + r.nextInt(1 + 8 - Math.min(((xiabs + yiabs) / 30), 8));
                        for(int i = 0 ; i < nbMines; i ++){
                            Ressource.Type t = yi < 0 ? Ressource.southOccurence() : Ressource.northOccurence();
                            int max = (int)(100 + r.nextInt(50 + xiabs + yiabs) * Ressource.amountMofifier(t));
                            addMine(station.getId(),
                                    max,
                                    max,
                                    1 + (int)((max / 120.0) * (1 + r.nextInt(20) / 5.0)),
                                    t.ordinal()
                                    );
                        }
                        station = getTrainStationByPos(xi, yi);
                        result.add(station);
                    }
                }
            }
        }

        return result;
    }

    /**
     * @return a list containing all the stations of the database
     */
    public ArrayList<TrainStation> getAllTrainStations(){
        ArrayList<TrainStation> result = new ArrayList<>();
        try {
            ResultSet resultSet;
            PreparedStatement ps = connection.prepareStatement("SELECT * FROM Gare;", Statement.RETURN_GENERATED_KEYS);
            resultSet = ps.executeQuery();
            while(resultSet.next()) {
                int id = resultSet.getInt("id");
                int posX = resultSet.getInt("posX");
                int posY = resultSet.getInt("posY");
                int nbOfPlatforms = resultSet.getInt("nbrQuai");
                int sizeOfPlatforms = resultSet.getInt("tailleQuai");
                ArrayList<Mine> mines = getAllMinesOfStation(id);

                result.add(new TrainStation(id, posX, posY, nbOfPlatforms, sizeOfPlatforms, mines));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * @param x position x of the station
     * @param y position y of the station
     * @return the station corresponding to those positions
     */
    public int getTrainStationIdByPos(int x, int y) {
        int result = -1;
        try {
            ResultSet resultSet;
            PreparedStatement ps = connection.prepareStatement("SELECT id FROM Gare WHERE posX=? AND posY=?;", Statement.RETURN_GENERATED_KEYS);
            ps.setObject(1, x);
            ps.setObject(2, y);
            resultSet = ps.executeQuery();
            if(resultSet.next()) {
                result = resultSet.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * @return the id of the starting station
     */
    private int getStartingStationId() {
        return getTrainStationIdByPos(0, 0);
    }

    /**
     * @param x x coordinate
     * @param y y coordinate
     * @param nbPlat number of platforms
     * @param sizePlat ize of platforms
     * @return if train station has been added to DataBase
     */
    public boolean insertTrainStation(int x, int y, int nbPlat, int sizePlat) {
        if(!canCreateStationAt(x, y)) return false;
        try {
            PreparedStatement ps = connection.prepareStatement("INSERT INTO Gare VALUES(default,?,?,?,?);", Statement.RETURN_GENERATED_KEYS);
            ps.setObject(1, x);
            ps.setObject(2, y);
            ps.setObject(3, nbPlat);
            ps.setObject(4, sizePlat);
            int status = ps.executeUpdate();
            return status != 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * @param id station id
     * @param x x coordinate
     * @param y y coordinate
     * @param nbPlat number of platforms
     * @param sizePlat ize of platforms
     * @return if train station has been changed in DataBase
     */
    public boolean updateTrainStation(int id, int x, int y, int nbPlat, int sizePlat) {
        try {
            PreparedStatement ps = connection.prepareStatement("UPDATE Gare SET posX=?, posY=?, nbrQuai=?, tailleQuai=? WHERE id=?;", Statement.RETURN_GENERATED_KEYS);
            ps.setObject(1, x);
            ps.setObject(2, y);
            ps.setObject(3, nbPlat);
            ps.setObject(4, sizePlat);
            ps.setObject(5, id);
            int status = ps.executeUpdate();
            return status != 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }


    /**
     * @param id id of station
     * @return if station was delete
     */
    public boolean deleteTrainStation(int id) {
        try {
            PreparedStatement ps = connection.prepareStatement("DELETE FROM Gare WHERE id=?;", Statement.RETURN_GENERATED_KEYS);
            ps.setObject(1, id);
            int status = ps.executeUpdate();
            return status == 1;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * @param x position x where we want to create a station
     * @param y position y where we want to create a station
     * @return if it's possible to create a station at given position
     */
    private boolean canCreateStationAt(int x, int y) {
        try {
            ResultSet resultSet;
            PreparedStatement ps = connection.prepareStatement("SELECT posX, posY FROM Gare;", Statement.RETURN_GENERATED_KEYS);
            resultSet = ps.executeQuery();
            while(resultSet.next()) {// bad need change
                if(resultSet.getInt(1) == x && resultSet.getInt(2) == y) return false;
            }
            return true;
        }catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * @param trainStation id of the station
     * @return number of train currently at the station
     */
    public int getNbUsedPlatforms(int trainStation){
        int nbUsedPlatforms = 0;
        try {
            ResultSet resultSet;
            PreparedStatement ps = connection.prepareStatement("SELECT COUNT(*) FROM Train WHERE `gareActuelle` =?;", Statement.RETURN_GENERATED_KEYS);
            ps.setObject(1, trainStation);
            resultSet = ps.executeQuery();
            if(resultSet.next()){
                nbUsedPlatforms = resultSet.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return nbUsedPlatforms;
    }

    /**
     * @param username player
     * @param newTsId new station id
     * @return if current station of player has been change
     */
    public boolean changeStationOfTrain(String username, int newTsId){
        try {
            // NOT SAFE !
            PreparedStatement ps = connection.prepareStatement("UPDATE Train SET gareActuelle=? WHERE proprietaire=?", Statement.RETURN_GENERATED_KEYS);
            ps.setObject(1, newTsId);
            ps.setObject(2, username);
            ps.executeUpdate();

            return true;
        }catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * @param username player who wants to move
     * @param newTsId id of the station where the player wants to move
     * @return true is the player has been able to move, else false
     */
    public boolean sendTrainToNewStation(String username, int newTsId){
        TrainStation ts1 = getTrainStation(getTrain(username).orElse(new Train()).getTrainStation().getId()).orElse(new TrainStation());
        TrainStation ts2 = getTrainStation(newTsId).orElse(new TrainStation());
        if(ts1.getId() == ts2.getId() || ts1.getId() == 0 || ts2.getId() == 0) return false;

        int eta = WagonStats.calculateTravelETA(getPlayerLoco(username).orElse(new Wagon()), ts1, ts2);

        try {
            PreparedStatement ps = connection.prepareStatement("UPDATE Train SET gareActuelle=? WHERE proprietaire=?", Statement.RETURN_GENERATED_KEYS);
            ps.setObject(1, newTsId);
            ps.setObject(2, username);
            ps.executeUpdate();

            Server.getInstance().getTravelController().addTrain(username, eta);
            return true;
        }catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }


    // MINE REQUESTS

    /**
     * @param emplacement station where the mine will be added
     * @param qteResources quantity of resources of the mine
     * @param type type of the mine
     * @return true if the mine has been added, else false
     */
    public int addMine(int emplacement, int qteResources, int max, int regen, int type){
        int result = -1;
        try {
            PreparedStatement ps = connection.prepareStatement("INSERT INTO Mine VALUES(default,?,?,?,?,?);", Statement.RETURN_GENERATED_KEYS);
            ps.setObject(1, type);
            ps.setObject(2, qteResources);
            ps.setObject(3, max);
            ps.setObject(4, regen);
            ps.setObject(5, emplacement);
            int status = ps.executeUpdate();
            if(status != 0){
                ResultSet resultSet = ps.getGeneratedKeys();
                resultSet.next();
                result = resultSet.getInt(1);
                // need tests
                Server.getInstance().getRegenerationController().addMine(new Mine(resultSet.getInt(1), type, qteResources, max, regen, emplacement));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * @param trainStation id of the station
     * @return the list of all the mines at the station
     */
    private ArrayList<Mine> getAllMinesOfStation(int trainStation){
        ArrayList<Mine> result = new ArrayList<>();
        try {
            ResultSet resultSet;
            PreparedStatement ps = connection.prepareStatement("SELECT * FROM Mine WHERE `emplacement` = " + trainStation + ";", Statement.RETURN_GENERATED_KEYS);
            resultSet = ps.executeQuery();
            while(resultSet.next()) {
                int id = resultSet.getInt("id");
                int type = resultSet.getInt("type");
                int qteRessources = resultSet.getInt("qteRessources");
                int max = resultSet.getInt("max");
                int regen = resultSet.getInt("regen");
                int emplacement = resultSet.getInt("emplacement");

                Mine mine = new Mine(id, type, qteRessources, max, regen, emplacement);
                result.add(mine);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }


    /**
     * @return the list of all the mines
     */
    public ArrayList<Mine> getAllMines(){
        ArrayList<Mine> result = new ArrayList<>();
        try {
            ResultSet resultSet;
            PreparedStatement ps = connection.prepareStatement("SELECT * FROM Mine");
            resultSet = ps.executeQuery();
            while(resultSet.next()) {
                int id = resultSet.getInt("id");
                int type = resultSet.getInt("type");
                int qteRessources = resultSet.getInt("qteRessources");
                int max = resultSet.getInt("max");
                int regen = resultSet.getInt("regen");
                int emplacement = resultSet.getInt("emplacement");

                Mine mine = new Mine(id, type, qteRessources, max, regen, emplacement);
                result.add(mine);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * @param id mine DataBase id
     * @return the mine
     */
    public Optional<Mine> getMine(int id){
        try {
            ResultSet resultSet;
            PreparedStatement ps = connection.prepareStatement("SELECT * FROM Mine WHERE id=?");
            ps.setObject(1, id);
            resultSet = ps.executeQuery();
            if(resultSet.next()) {
                int idMine = resultSet.getInt("id");
                int type = resultSet.getInt("type");
                int qteRessources = resultSet.getInt("qteRessources");
                int max = resultSet.getInt("max");
                int regen = resultSet.getInt("regen");
                int emplacement = resultSet.getInt("emplacement");

                return Optional.of(new Mine(idMine, type, qteRessources, max, regen, emplacement));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    /**
     * @param id mine id
     * @param resource new type of resource
     * @param amount new amount
     * @param place new station DataBase id
     */
    public boolean updateMine(int id, int resource, int amount, int max, int regen, int place){
        try {
            PreparedStatement ps = connection.prepareStatement("UPDATE Mine SET type=?, qteRessources=?, max=?, regen=?, emplacement=? WHERE id=?", Statement.RETURN_GENERATED_KEYS);
            ps.setObject(1, resource);
            ps.setObject(2, amount);
            ps.setObject(3, max);
            ps.setObject(4, regen);
            ps.setObject(5, place);
            ps.setObject(6, id);
            int status = ps.executeUpdate();
            return status != 0;
        }catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * @param id id of mine
     * @return if mine was delete
     */
    public boolean deleteMine(int id) {
        try {
            PreparedStatement ps = connection.prepareStatement("DELETE FROM Mine WHERE id=?;", Statement.RETURN_GENERATED_KEYS);
            ps.setObject(1, id);
            int status = ps.executeUpdate();
            return status == 1;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     *
     * @param id mine id
     * @param changeAmount how much you want to change
     * @return the max possible change
     */
    public int canChangeMineAmount(int id, int changeAmount) {
        int maxChange = changeAmount;

        Mine mine = getMine(id).orElse(new Mine());
        int MAX = mine.getMax();
        int MIN = 0;
        int currentAmount = mine.getAmount();
        if(currentAmount > MAX || currentAmount < MIN) return 0;
        int newAmount =  currentAmount + changeAmount;
        if(newAmount > MAX) maxChange = MAX - currentAmount;
        if(newAmount < MIN) maxChange = MIN - currentAmount;

        return maxChange;
    }

    /**
     * Mets à jour la mine donnée avec la quantité donnée
     *
     * @param id           : mine à mettre à jour
     * @param changeAmount : quantité à mettre à jour
     */
    public boolean changeMineAmount(int id, int changeAmount){
        try {
            PreparedStatement ps = connection.prepareStatement("UPDATE Mine SET qteRessources=? WHERE id=?", Statement.RETURN_GENERATED_KEYS);
            ps.setObject(1, getMine(id).orElse(new Mine()).getAmount() + changeAmount);
            ps.setObject(2, id);
            int status = ps.executeUpdate();
            return status != 0;
        }catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // OFFERS REQUESTS

    public ArrayList<Offer> getOffers(int offer, int price) {
        ArrayList<Offer> offers = new ArrayList<>();
        PreparedStatement ps;
        try {
            ResultSet resultSet;
            if(offer == -1 && price == -1) {
                ps = connection.prepareStatement("SELECT * FROM Offres");
            }
            else if(offer != -1 && price == -1) {
                ps = connection.prepareStatement("SELECT * FROM Offres WHERE offerType=?");
                ps.setObject(1, offer);
            }
            else if(offer == -1/* && price != -1*/) {
                ps = connection.prepareStatement("SELECT * FROM Offres WHERE priceType=?");
                ps.setObject(1, price);
            }
            else {
                ps = connection.prepareStatement("SELECT * FROM Offres WHERE offerType=? AND priceType=?");
                ps.setObject(1, offer);
                ps.setObject(2, price);
            }
            resultSet = ps.executeQuery();
            while(resultSet.next()) {
                int id = resultSet.getInt("id");
                String trader = resultSet.getString("trader");
                int offerType = resultSet.getInt("offerType");
                int offerAmount = resultSet.getInt("offerAmount");
                int priceType = resultSet.getInt("priceType");
                int priceAmount = resultSet.getInt("priceAmount");

                offers.add(new Offer(id, trader, offerType, offerAmount, priceType, priceAmount));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return offers;
    }

    public boolean addOffer(String playerName, int offerType, int offerAmount, int priceType, int priceAmount) {
        Optional<ResourceAmount> playerObject = getPlayerObjectOfType(playerName, offerType);
        if(!playerObject.filter(ra -> ra.getQuantity() >= offerAmount).isPresent()) return false;
        //if(ra == null) return false;
        //if(ra.getQuantity() < offerAmount) return false;
        updatePlayerObjects(playerName, offerType, -offerAmount);

        try {
            PreparedStatement ps = connection.prepareStatement("INSERT INTO Offres VALUES(default,?,?,?,?,?)", Statement.RETURN_GENERATED_KEYS);
            ps.setObject(1, playerName);
            ps.setObject(2, offerType);
            ps.setObject(3, offerAmount);
            ps.setObject(4, priceType);
            ps.setObject(5, priceAmount);
            int status = ps.executeUpdate();
            return status != 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean optionalBuyOffer(String buyer, int id) {
        return getOfferById(id).filter(offer -> buyOffer(buyer, offer)).isPresent();
    }

    public boolean optionalCancelOffer(int id) {
        return getOfferById(id).filter(this::cancelOffer).isPresent();
    }

    private boolean buyOffer(String buyer, Offer offer) {
        Optional<ResourceAmount> buyerObject = getPlayerObjectOfType(buyer, offer.getPrice().getRessource().ordinal());
        if(!buyerObject.filter(ra -> ra.getQuantity() >= offer.getPrice().getQuantity()).isPresent()) return false;
        updatePlayerObjects(buyer, offer.getPrice().getRessource().ordinal(), -offer.getPrice().getQuantity());

        try {
            PreparedStatement ps = connection.prepareStatement("DELETE FROM Offres WHERE id=?", Statement.RETURN_GENERATED_KEYS);
            ps.setObject(1, offer.getId());
            int status = ps.executeUpdate();
            if(status == 1) {
                updatePlayerObjects(buyer, offer.getOffer().getRessource().ordinal(), offer.getOffer().getQuantity());
                updatePlayerObjects(offer.getPlayerName(), offer.getPrice().getRessource().ordinal(), offer.getPrice().getQuantity());
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    private boolean cancelOffer(Offer offer) {
        try {
            PreparedStatement ps = connection.prepareStatement("DELETE FROM Offres WHERE id=?", Statement.RETURN_GENERATED_KEYS);
            ps.setObject(1, offer.getId());
            int status = ps.executeUpdate();
            if(status == 1) {
                updatePlayerObjects(offer.getPlayerName(), offer.getOffer().getRessource().ordinal(), offer.getOffer().getQuantity());
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    private Optional<Offer> getOfferById(int id) {
        try {
            ResultSet resultSet;
            PreparedStatement ps = connection.prepareStatement("SELECT * FROM Offres WHERE id=?");
            ps.setObject(1, id);
            resultSet = ps.executeQuery();
            if(resultSet.next()) {
                String trader = resultSet.getString("trader");
                int offerType = resultSet.getInt("offerType");
                int offerAmount = resultSet.getInt("offerAmount");
                int priceType = resultSet.getInt("priceType");
                int priceAmount = resultSet.getInt("priceAmount");

                return Optional.of(new Offer(id, trader, offerType, offerAmount, priceType, priceAmount));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return Optional.empty();
    }
}
