package Game;

import com.google.gson.*;

import java.util.ArrayList;

public class TrainStation {
    private int id;
    private int posX;
    private int posY;
    private int nbOfPlatforms;
    private int sizeOfPlatforms;
    private ArrayList<Mine> mines = new ArrayList<Mine>();

    public TrainStation() {}

    public TrainStation(int id, int x, int y, int nbPlat, int sizePlat, ArrayList<Mine> m) {
        this.id = id;
        posX = x;
        posY = y;
        nbOfPlatforms = nbPlat;
        sizeOfPlatforms = sizePlat;
        mines = m;
    }

    public String toJSON() {
        Gson jsonEngine = new GsonBuilder().create();

        JsonObject trainStation = new JsonObject();
        trainStation.add("id", new JsonPrimitive(id));
        trainStation.add("posX", new JsonPrimitive(posX));
        trainStation.add("posY", new JsonPrimitive(posY));
        trainStation.add("nbOfPlatforms", new JsonPrimitive(nbOfPlatforms));
        trainStation.add("sizeOfPlatforms", new JsonPrimitive(sizeOfPlatforms));
        //JsonArray jMines = new JsonArray();
        //for(Mine m : mines) jMines.add(new JsonPrimitive(m.getResource()));
        trainStation.add("mines", new JsonPrimitive(Mine.listToJSON(mines)));

        return jsonEngine.toJson(trainStation);
    }

    public void fromJSON(String from) {
        Gson jsonEngine = new GsonBuilder().create();

        JsonObject trainStation = jsonEngine.fromJson(from, JsonObject.class);
        id = trainStation.get("id").getAsInt();
        posX = trainStation.get("posX").getAsInt();
        posY = trainStation.get("posY").getAsInt();
        nbOfPlatforms = trainStation.get("nbOfPlatforms").getAsInt();
        sizeOfPlatforms = trainStation.get("sizeOfPlatforms").getAsInt();

        // mines ???
    }

    public static String listToJSON(ArrayList<TrainStation> trainStations) {
        Gson jsonEngine = new GsonBuilder().create();

        JsonArray list = new JsonArray();
        for(TrainStation ts : trainStations) list.add(new JsonPrimitive(ts.toJSON()));

        return jsonEngine.toJson(list);
    }

    public static ArrayList<TrainStation> listFromJSON(String from) {
        ArrayList<TrainStation> trainStations = new ArrayList<TrainStation>();
        Gson jsonEngine = new GsonBuilder().create();

        ArrayList<String> jTrainsStation = jsonEngine.fromJson(from, ArrayList.class);
        for(String s : jTrainsStation) {
            TrainStation ts = new TrainStation();//bad ?
            ts.fromJSON(s);
            trainStations.add(ts);
        }

        return trainStations;
    }

    public int getId() {
        return id;
    }

    public int getPosX() {
        return posX;
    }

    public int getPosY() {
        return posY;
    }

    public int getNbOfPlatforms() {
        return nbOfPlatforms;
    }

    public int getSizeOfPlatforms() {
        return sizeOfPlatforms;
    }

    @Override
    public String toString() {
        return id + " : " + posX + ";" + posY;
    }
}
