package Game;

import Utils.JsonUtility;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

import java.util.ArrayList;
import java.util.stream.Collectors;

public class SimpleStation {
    private int id;
    private int posX;
    private int posY;
    private int nbOfPlatforms;
    private int sizeOfPlatforms;
    private ArrayList<SimpleMine> mines = new ArrayList<>();

    public SimpleStation() {}

    public SimpleStation(JsonObject json) {
        fromJson(json);
    }

    public SimpleStation(String json) {
        fromJson((JsonObject) JsonUtility.fromJson(json));
    }

    public SimpleStation(TrainStation ts) {
        this.id = ts.getId();
        posX = ts.getPosX();
        posY = ts.getPosY();
        nbOfPlatforms = ts.getNbOfPlatforms();
        sizeOfPlatforms = ts.getSizeOfPlatforms();
        mines = SimpleMine.listToSimple(ts.getMines());
    }

    public SimpleStation(int id, int x, int y, int nbPlat, int sizePlat, ArrayList<SimpleMine> m) {
        this.id = id;
        posX = x;
        posY = y;
        nbOfPlatforms = nbPlat;
        sizeOfPlatforms = sizePlat;
        mines = m;
    }

    public JsonObject toJson() {
        JsonObject trainStation = new JsonObject();
        trainStation.add("i", new JsonPrimitive(id));
        trainStation.add("x", new JsonPrimitive(posX));
        trainStation.add("y", new JsonPrimitive(posY));
        trainStation.add("n", new JsonPrimitive(nbOfPlatforms));
        trainStation.add("s", new JsonPrimitive(sizeOfPlatforms));
        if(mines == null) mines = new ArrayList<>();

        JsonArray list = new JsonArray();
        for(SimpleMine m : mines) list.add(new JsonPrimitive(m.toSimpleFormat()));
        trainStation.add("m", list);

        return trainStation;
    }

    public void fromJson(JsonObject from) {
        id = from.get("i").getAsInt();
        posX = from.get("x").getAsInt();
        posY = from.get("y").getAsInt();
        nbOfPlatforms = from.get("n").getAsInt();
        sizeOfPlatforms = from.get("s").getAsInt();

        mines = new ArrayList<>();
        for(JsonElement j : (JsonArray)from.get("m")) {
            SimpleMine mine = new SimpleMine();
            mine.fromSimpleFormat(j.getAsString());
            mines.add(mine);
        }
        //mines = JsonUtility.listFromJson((JsonArray) from.get("m"), SimpleMine::new);
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

    public ArrayList<SimpleMine> getMines() {
        return mines;
    }

    @Override
    public String toString() {
        return id + " : " + posX + ";" + posY;
    }

    public String getInfos() {
        return "Nbr of Platforms: " + nbOfPlatforms + " Size of Platforms: " + sizeOfPlatforms;
    }

    public static ArrayList<SimpleStation> listToSimple(ArrayList<TrainStation> stations) {
        return  stations.stream()
                .map(SimpleStation::new)
                .collect(Collectors.toCollection(ArrayList::new));
    }
}
