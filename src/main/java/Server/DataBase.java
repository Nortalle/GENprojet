package Server;

import Game.*;

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
    public boolean insertUser(String username, String password) {
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
            ps = connection.prepareStatement("INSERT INTO Wagon VALUES(default,?,2000,1,'Loco');", Statement.RETURN_GENERATED_KEYS);
            ps.setObject(1, username);
            status = ps.executeUpdate();
            if(status == 0) return false;

            ps = connection.prepareStatement("INSERT INTO Wagon VALUES(default,?,2000,1,'Drill');", Statement.RETURN_GENERATED_KEYS);
            ps.setObject(1, username);
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
     * @return if all users were delete
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
     * @return if all users were delete
     */
    public boolean deleteAllUsers() {
        try {
            PreparedStatement ps = connection.prepareStatement("DELETE FROM Utilisateur;", Statement.RETURN_GENERATED_KEYS);
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
    public boolean checkLoggin(String username, String password) {
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
            int eta = resultSet.getInt("tempsArriveeEstime");

            Integer realETA = Server.getInstance().getTravelController().getETA(username);
            if(realETA != null) eta = realETA;
            train = new Train(new ArrayList<Wagon>(), getTrainStation(currentTs), eta);// TODO

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return train;
    }

    // WAGOON REQUESTS

    /**
     * @param username the player owner of the train
     * @param weight the weight of the wagoon
     * @param level the level of the wagoon
     * @param type the type of the wagoon
     * @return true if the wagoon has been created, else false
     */
    public boolean addWagoon(String username, int weight, int level, String type){
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
     * @param type type of the wagoons that want to be get
     * @return a list containing all the owner's wagoons of this type
     */
    public ArrayList<Wagon> getWagoonsOfType(String username, String type){
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
     * @return the liste of all the wagoons of the train
     */
    public ArrayList<Wagon> getAllWagoons(String username){
        ArrayList<Wagon> result = new ArrayList<Wagon>();
        try {
            ResultSet resultSet;
            PreparedStatement ps = connection.prepareStatement("SELECT * FROM Wagon WHERE `proprietaire`=?;", Statement.RETURN_GENERATED_KEYS);
            ps.setObject(1, username);
            resultSet = ps.executeQuery();
            while(resultSet.next()) {
                int id = resultSet.getInt("id");
                String owner = resultSet.getString("proprietaire");
                String type = resultSet.getString("type");
                int weight = resultSet.getInt("poids");
                int level = resultSet.getInt("niveau");
                /*
                Wagon wagon = new Wagon(id, owner, type, weight, level);
                result.add(wagon);
                */

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
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
     * @param newTsLine the id of the station
     * @return the station corresponding to the id
     */
    public TrainStation getTrainStation(int newTsLine){
        TrainStation trainStation = null;
        try {
            ResultSet resultSet;
            PreparedStatement ps = connection.prepareStatement("SELECT * FROM Gare WHERE `id`= " + newTsLine + ";", Statement.RETURN_GENERATED_KEYS);
            resultSet = ps.executeQuery();
            if(!resultSet.next()){return null;}
            int id = resultSet.getInt("id");
            int posX = resultSet.getInt("posX");
            int posY = resultSet.getInt("posY");
            int nbOfPlatforms = resultSet.getInt("nbrQuai");
            int sizeOfPlatforms = resultSet.getInt("tailleQuai");
            ArrayList<Mine> mines = getAllMinesOfStation(id);
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

        System.out.println(station1 + " ****" + station2);
        System.out.println(ts2.getPosX() + " " + ts1.getPosX() + " : " + ts2.getPosY() + " " + ts1.getPosY());
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
            //A modifier pour donner le vrai temps de trajet initial (pour le moment toujours à 100)
            int defaultETA = calculateTravelTime(currentTsId, newTsId);
            PreparedStatement ps = connection.prepareStatement("UPDATE Train SET `gareActuelle`=?, `tempsArriveeEstime`=? WHERE `proprietaire`=?", Statement.RETURN_GENERATED_KEYS);
            ps.setObject(1, newTsId);
            ps.setObject(2, defaultETA);
            ps.setObject(3, username);
            ps.executeUpdate();

            Server.getInstance().getTravelController().addTrain(username, defaultETA);
            return true;
        }catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // MINE REQUESTS

    /**
     * @param emplacement station where the mine will be added
     * @param qteRessources quantity of ressources of the mine
     * @param type type of the mine
     * @return true if the mine has been added, else false
     */
    public boolean addMine(int emplacement, int qteRessources, String type){
        try {
            PreparedStatement ps = connection.prepareStatement("INSERT INTO Mine VALUES(?,?,?);", Statement.RETURN_GENERATED_KEYS);
            ps.setObject(1, type);
            ps.setObject(2, qteRessources);
            ps.setObject(3, emplacement);
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
                String type = resultSet.getString("type");
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

}
