package Server;

import Game.*;
import Utils.WagonStats;

import java.sql.*;
import java.util.ArrayList;

public class DataBase {
    private Connection connection;
    private final static String URL = "jdbc:mysql://localhost:3306/GEN_otrain?user=root&password=root";

    public DataBase() {
        this(URL);
    }

    public DataBase(String url) {
        try {
            connection = DriverManager.getConnection(url);
        } catch (SQLException e) {
            e.printStackTrace();
        }
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

            ps = connection.prepareStatement("INSERT INTO RessourcesParJoueur VALUES(?,default,default,default,default,default,default,default,default,default);", Statement.RETURN_GENERATED_KEYS);
            ps.setObject(1, username);
            status = ps.executeUpdate();
            if(status == 0) return false;

            ps = connection.prepareStatement("INSERT INTO Train VALUES(?,'Tom',?,0);", Statement.RETURN_GENERATED_KEYS);
            ps.setObject(1, username);
            ps.setObject(2, getStartingStationId());
            status = ps.executeUpdate();
            if(status == 0) return false;

            //DEFAULT LOCO + WAGONS//
            ps = connection.prepareStatement("INSERT INTO Wagon VALUES(default,?,2000,1,?);", Statement.RETURN_GENERATED_KEYS);
            ps.setObject(1, username);
            ps.setObject(2, WagonStats.LOCO_ID);
            status = ps.executeUpdate();
            if(status == 0) return false;

            ps = connection.prepareStatement("INSERT INTO Wagon VALUES(default,?,2000,1,?);", Statement.RETURN_GENERATED_KEYS);
            ps.setObject(1, username);
            ps.setObject(2, WagonStats.DRILL_ID);
            status = ps.executeUpdate();
            if(status == 0) return false;
            // //

            return true;
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
            if(status == 0) return true;
            else return false;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * @return list of all username
     */
    public ArrayList<String> getAllUsers() {
        ArrayList<String> result = new ArrayList<String>();
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
            else return false;
        }catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * @param username player
     * @return tab of resources (Scrum, Eau, Bois, Charbon, Petrol, Fer, Cuivre, Acier, Or); -1 means unknown
     */
    public int[] getPlayerResources(String username) {
        int resources[] = {-1, -1, -1, -1, -1, -1, -1, -1, -1};
        try {
            ResultSet resultSet;
            PreparedStatement ps = connection.prepareStatement("SELECT * FROM RessourcesParJoueur WHERE nomJoueur=?", Statement.RETURN_GENERATED_KEYS);
            ps.setObject(1, username);
            resultSet = ps.executeQuery();
            if(resultSet.next()) {
                for(int i = 1; i < 10; i++) {
                    resources[i-1] = resultSet.getInt(i+1);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return resources;
    }

    public String getUsernameByWagonId(int wagonId) {
        String result = "";
        try {
            ResultSet resultSet;
            PreparedStatement ps = connection.prepareStatement("SELECT proprietaire FROM Wagon WHERE id=?", Statement.RETURN_GENERATED_KEYS);
            ps.setObject(1, wagonId);
            resultSet = ps.executeQuery();
            if(resultSet.next()) {
                result = resultSet.getString(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    public boolean setPlayerResources(String username, int resources[]){
        try {
            PreparedStatement ps = connection.prepareStatement("UPDATE RessourcesParJoueur SET qteScrum=?, qteEau=?, qteBois=?, qteCharbon=?, qtePetrol=?, qteFer=?, qteCuivre=?, qteAcier=?, qteOr=? WHERE `nomJoueur`=?", Statement.RETURN_GENERATED_KEYS);
            for(int i = 0; i < resources.length; i++) {
                ps.setObject(i + 1, resources[i]);
            }
            ps.setObject(resources.length + 1, username);
            ps.executeUpdate();
            return true;
        }catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // TRAIN REQUESTS

    /**
     *
     * @param username player owner of the train
     * @param trainName name of the train
     * @return true if the train has been created, else false
     */
    public boolean createTrain(String username, String trainName){
        try {
            PreparedStatement ps = connection.prepareStatement("INSERT INTO Train VALUES(?,?);", Statement.RETURN_GENERATED_KEYS);
            ps.setObject(1, username);
            ps.setObject(2, trainName);
            int status = ps.executeUpdate();
            if(status != 0){
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * @param username player owner of the train
     * @return the player's train
     */
    public Train getTrain(String username){
        Train train = null;
        try {
            ResultSet resultSet;
            PreparedStatement ps = connection.prepareStatement("SELECT * FROM Train WHERE `proprietaire`=?;", Statement.RETURN_GENERATED_KEYS);
            ps.setObject(1, username);
            resultSet = ps.executeQuery();
            if(!resultSet.next()){return null;}
            String name = resultSet.getString("nom");
            int currentTs = resultSet.getInt("gareActuelle");
            //int eta = resultSet.getInt("tempsArriveeEstime");

            int eta = Server.getInstance().getTravelController().getETA(username);
            train = new Train(getAllWagons(username), getTrainStation(currentTs), eta);

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return train;
    }

    // WAGON REQUESTS

    /**
     * @param username the player owner of the train
     * @param weight the weight of the wagon
     * @param level the level of the wagon
     * @param type the type of the wagon
     * @return true if the wagon has been created, else false
     */
    public boolean addWagon(String username, int weight, int level, String type){
        try {
            PreparedStatement ps = connection.prepareStatement("INSERT INTO Utilisateur VALUES(?,?,?,?);", Statement.RETURN_GENERATED_KEYS);
            ps.setObject(1, username);
            ps.setObject(2, weight);
            ps.setObject(3, level);
            ps.setObject(4, type);
            int status = ps.executeUpdate();
            if(status != 0){
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     *
     * @param username the player owner of the train
     * @param type type of the wagons that want to be get
     * @return a list containing all the owner's wagons of this type
     */
    public ArrayList<Wagon> getWagonsOfType(String username, String type){
        ArrayList<Wagon> result = new ArrayList<Wagon>();
        try {
            ResultSet resultSet;
            PreparedStatement ps = connection.prepareStatement("SELECT * FROM Wagon WHERE `proprietaire`=? AND `type`=?;", Statement.RETURN_GENERATED_KEYS);
            ps.setObject(1, username);
            ps.setObject(2, type);
            resultSet = ps.executeQuery();
            while(resultSet.next()) {
                int id = resultSet.getInt("id");
                String owner = resultSet.getString("proprietaire");
                String _type = resultSet.getString("type");
                int weight = resultSet.getInt("poids");
                int level = resultSet.getInt("niveau");
                /*
                Wagon wagon = new Wagon(id, owner, _type, weight, level);
                result.add(wagon);
                */

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     *
     * @param username player owner of the train
     * @return the liste of all the wagons of the train
     */
    public ArrayList<Wagon> getAllWagons(String username){
        ArrayList<Wagon> result = new ArrayList<Wagon>();
        try {
            ResultSet resultSet;
            PreparedStatement ps = connection.prepareStatement("SELECT * FROM Wagon WHERE `proprietaire`=?;", Statement.RETURN_GENERATED_KEYS);
            ps.setObject(1, username);
            resultSet = ps.executeQuery();
            while(resultSet.next()) {
                int id = resultSet.getInt("id");
                int weight = resultSet.getInt("poids");
                int level = resultSet.getInt("niveau");
                int typeID = resultSet.getInt("typeID");

                Wagon wagon = new Wagon(id, weight, level, typeID);
                result.add(wagon);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    public Wagon getWagon(int id){
        Wagon wagon = null;
        try {
            ResultSet resultSet;
            PreparedStatement ps = connection.prepareStatement("SELECT * FROM Wagon WHERE `id`=?");
            ps.setObject(1, id);
            resultSet = ps.executeQuery();
            if(resultSet.next()) {
                int idWagon = resultSet.getInt("id");
                int weight = resultSet.getInt("poids");
                int level = resultSet.getInt("niveau");
                int typeID = resultSet.getInt("typeID");

                wagon = new Wagon(id, weight, level, typeID);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return wagon;
    }

    // STATION REQUESTS

    /**
     * @param posX position X of the station
     * @param posY position Y of the station
     * @param nbrPlatforms number of platforms in the station
     * @param platformSize size of the platforms in the station
     *
     * @return true if the station has been added, else falee
     */
    public boolean createStation(int posX, int posY, int nbrPlatforms, int platformSize){
        try {
            PreparedStatement ps = connection.prepareStatement("INSERT INTO TrainStation VALUES(?,?,?,?);", Statement.RETURN_GENERATED_KEYS);
            ps.setObject(1, posX);
            ps.setObject(2, posY);
            ps.setObject(3, nbrPlatforms);
            ps.setObject(4, platformSize);
            int status = ps.executeUpdate();
            if(status != 0){
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * @param tsId the id of the station
     * @return the station corresponding to the id
     */
    public TrainStation getTrainStation(int tsId){
        TrainStation trainStation = null;
        try {
            ResultSet resultSet;
            PreparedStatement ps = connection.prepareStatement("SELECT * FROM Gare WHERE `id`=?;", Statement.RETURN_GENERATED_KEYS);
            ps.setObject(1, tsId);
            resultSet = ps.executeQuery();
            if(!resultSet.next()){return null;}
            int id = resultSet.getInt("id");
            int posX = resultSet.getInt("posX");
            int posY = resultSet.getInt("posY");
            int nbOfPlatforms = resultSet.getInt("nbrQuai");
            int sizeOfPlatforms = resultSet.getInt("tailleQuai");
            ArrayList<Mine> mines = getAllMinesOfStation(tsId);
            // TODO
            if(mines == null) mines = new ArrayList<Mine>();

            trainStation = new TrainStation(id, posX, posY, nbOfPlatforms, sizeOfPlatforms, mines);

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return trainStation;
    }

    /**
     * @return a list containing all the stations of the database
     */
    public ArrayList<TrainStation> getAllTrainStations(){
        ArrayList<TrainStation> result = new ArrayList<TrainStation>();
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

                TrainStation trainStation = new TrainStation(id, posX, posY, nbOfPlatforms, sizeOfPlatforms, mines);
                result.add(trainStation);


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
            PreparedStatement ps = connection.prepareStatement("SELECT id FROM Gare WHERE posX = ? AND posY = ?;", Statement.RETURN_GENERATED_KEYS);
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
    public int getStartingStationId() {
        return getTrainStationIdByPos(0, 0);
    }

    public boolean insertTrainStation(int x, int y, int nbPlat, int sizePlat) {
        if(!canCreateStationAt(x, y)) return false;
        try {
            PreparedStatement ps = connection.prepareStatement("INSERT INTO Gare VALUES(default,?,?,?,?);", Statement.RETURN_GENERATED_KEYS);
            ps.setObject(1, x);
            ps.setObject(2, y);
            ps.setObject(3, nbPlat);
            ps.setObject(4, sizePlat);
            int status = ps.executeUpdate();
            if(status == 0) return false;
            else return true;
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
            if(status == 0) return true;
            else return false;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * @param x position x where we want to create a station
     * @param y position y where we want to create a station
     * @return
     */
    public boolean canCreateStationAt(int x, int y) {
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

    public int calculateTravelTime(int station1, int station2) {
        TrainStation ts1 = getTrainStation(station1);
        TrainStation ts2 = getTrainStation(station2);
        return Math.abs(ts2.getPosX() - ts1.getPosX()) + Math.abs(ts2.getPosY() - ts1.getPosY());
    }

    /**
     * @param username player who wants to move
     * @param newTsId id of the station where the player wants to move
     * @return true is the player has been able to move, else false
     */
    public boolean sendTrainToNewStation(String username, int newTsId){
        try {
            TrainStation ts = getTrainStation(newTsId);
            if(ts == null) return false;
            int currentTsId = getTrain(username).getTrainStation().getId();
            if(ts.getId() == currentTsId) return false;
            //A modifier pour donner le vrai temps de trajet initial
            int eta = calculateTravelTime(currentTsId, newTsId);
            PreparedStatement ps = connection.prepareStatement("UPDATE Train SET `gareActuelle`=?, `tempsArriveeEstime`=? WHERE `proprietaire`=?", Statement.RETURN_GENERATED_KEYS);
            ps.setObject(1, newTsId);
            ps.setObject(2, eta);
            ps.setObject(3, username);
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
     * @param qteResources quantity of ressources of the mine
     * @param type type of the mine
     * @return true if the mine has been added, else false
     */
    public boolean addMine(int emplacement, int qteResources, int type){
        try {
            PreparedStatement ps = connection.prepareStatement("INSERT INTO Mine VALUES(default,?,?,?);", Statement.RETURN_GENERATED_KEYS);
            ps.setObject(1, type);
            ps.setObject(2, qteResources);
            ps.setObject(3, emplacement);
            int status = ps.executeUpdate();
            if(status != 0){
                ResultSet resultSet = ps.getGeneratedKeys();
                resultSet.next();
                // need tests
                Server.getInstance().getRegenerationController().addMine(new Mine(resultSet.getInt(1), type, qteResources, emplacement));
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;

    }

    /**
     * @param trainStation id of the station
     * @return the list of all the mines at the station
     */
    public ArrayList<Mine> getAllMinesOfStation(int trainStation){
        ArrayList<Mine> result = new ArrayList<Mine>();
        try {
            ResultSet resultSet;
            PreparedStatement ps = connection.prepareStatement("SELECT * FROM Mine WHERE `emplacement` = " + trainStation + ";", Statement.RETURN_GENERATED_KEYS);
            resultSet = ps.executeQuery();
            while(resultSet.next()) {
                int id = resultSet.getInt("id");
                int type = resultSet.getInt("type");
                int qteRessources = resultSet.getInt("qteRessources");
                int emplacement = resultSet.getInt("emplacement");

                Mine mine = new Mine(id, type, qteRessources, emplacement);
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
        ArrayList<Mine> result = new ArrayList<Mine>();
        try {
            ResultSet resultSet;
            PreparedStatement ps = connection.prepareStatement("SELECT * FROM Mine");
            resultSet = ps.executeQuery();
            while(resultSet.next()) {
                int id = resultSet.getInt("id");
                int type = resultSet.getInt("type");
                int qteRessources = resultSet.getInt("qteRessources");
                int emplacement = resultSet.getInt("emplacement");

                Mine mine = new Mine(id, type, qteRessources, emplacement);
                result.add(mine);

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    public Mine getMine(int id){
        Mine mine = null;
        try {
            ResultSet resultSet;
            PreparedStatement ps = connection.prepareStatement("SELECT * FROM Mine WHERE `id`=?");
            ps.setObject(1, id);
            resultSet = ps.executeQuery();
            if(resultSet.next()) {
                int idMine = resultSet.getInt("id");
                int type = resultSet.getInt("type");
                int qteRessources = resultSet.getInt("qteRessources");
                int emplacement = resultSet.getInt("emplacement");

                mine = new Mine(id, type, qteRessources, emplacement);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return mine;
    }

    /**
     * Mets à jour la mine donnée avec la quantité donnée
     *
     * @param id        : mine à mettre à jour
     * @param amount    : quantité à mettre à jour
     */
    public void setMineAmount(int id, int amount){
        try {
            PreparedStatement ps = connection.prepareStatement("UPDATE Mine SET qteRessources=? WHERE `id`=?", Statement.RETURN_GENERATED_KEYS);

            ps.setObject(1, amount);
            ps.setObject(2, id);

            ps.executeUpdate();
        }catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
