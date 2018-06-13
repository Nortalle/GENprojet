package Game;

import Utils.JsonUtility;
import com.google.gson.*;

import java.util.ArrayList;

public class TrainStation {
    private int id;
    private int posX;
    private int posY;
    private int nbOfPlatforms;
    private int sizeOfPlatforms;
    private ArrayList<Mine> mines = new ArrayList<>();

    public TrainStation() {}

    public TrainStation(JsonObject json) {
        fromJson(json);
    }

    public TrainStation(String json) {
        fromJson((JsonObject) JsonUtility.fromJson(json));
    }

    public TrainStation(int id, int x, int y, int nbPlat, int sizePlat, ArrayList<Mine> m) {
        this.id = id;
        posX = x;
        posY = y;
        nbOfPlatforms = nbPlat;
        sizeOfPlatforms = sizePlat;
        mines = m;
    }

    public JsonObject toJson() {
        JsonObject trainStation = new JsonObject();
        trainStation.add("id", new JsonPrimitive(id));
        trainStation.add("posX", new JsonPrimitive(posX));
        trainStation.add("posY", new JsonPrimitive(posY));
        trainStation.add("nbOfPlatforms", new JsonPrimitive(nbOfPlatforms));
        trainStation.add("sizeOfPlatforms", new JsonPrimitive(sizeOfPlatforms));
        if(mines == null) mines = new ArrayList<>();
        trainStation.add("mines", JsonUtility.listToJson(mines, Mine::toJson));

        return trainStation;
    }

    public void fromJson(JsonObject from) {
        id = from.get("id").getAsInt();
        posX = from.get("posX").getAsInt();
        posY = from.get("posY").getAsInt();
        nbOfPlatforms = from.get("nbOfPlatforms").getAsInt();
        sizeOfPlatforms = from.get("sizeOfPlatforms").getAsInt();
        mines = JsonUtility.listFromJson((JsonArray) from.get("mines"), Mine::new);
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

    public ArrayList<Mine> getMines() {
        return mines;
    }

    @Override
    public String toString() {
        return id + " : " + posX + ";" + posY;
    }

    public String getInfos() {
        return "Nbr of Platforms: " + nbOfPlatforms + " Size of Platforms: " + sizeOfPlatforms;
    }
}
