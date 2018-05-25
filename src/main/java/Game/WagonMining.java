package Game;

import com.google.gson.*;

import java.util.ArrayList;

public class WagonMining  {
    private Wagon wagon;
    private Mine currentMine;

    public WagonMining() {}

    public WagonMining(String json) {
        fromJSON(json);
    }

    public WagonMining(Wagon wagon, Mine currentMine) {
        this.wagon = wagon;
        this.currentMine = currentMine;
    }

    public Mine getCurrentMine() {
        return currentMine;
    }

    public void setCurrentMine(Mine currentMine) {
        this.currentMine = currentMine;
    }

    public boolean isMining() {
        return currentMine != null;
    }

    public String toJSON() {
        Gson jsonEngine = new GsonBuilder().create();

        JsonObject miningWagon = new JsonObject();
        if(currentMine == null) wagon = new Wagon();
        miningWagon.add("wagon", new JsonPrimitive(wagon.toJSON()));
        if(currentMine == null) currentMine = new Mine();
        miningWagon.add("currentMine", new JsonPrimitive(currentMine.toJSON()));

        return jsonEngine.toJson(miningWagon);
    }

    public void fromJSON(String from) {
        Gson jsonEngine = new GsonBuilder().create();

        JsonObject miningWagon = jsonEngine.fromJson(from, JsonObject.class);
        if(wagon == null) wagon = new Wagon();// can we do better ?
        wagon.fromJSON(miningWagon.get("wagon").getAsString());
        if(currentMine == null) currentMine = new Mine();// can we do better ?
        currentMine.fromJSON(miningWagon.get("currentMine").getAsString());
    }

    public static String listToJSON(ArrayList<WagonMining> wagonMining) {
        Gson jsonEngine = new GsonBuilder().create();

        JsonArray list = new JsonArray();
        for(WagonMining wm : wagonMining) list.add(new JsonPrimitive(wm.toJSON()));

        return jsonEngine.toJson(list);
    }

    public static ArrayList<WagonMining> listFromJSON(String from) {
        ArrayList<WagonMining> trainStations = new ArrayList<>();
        Gson jsonEngine = new GsonBuilder().create();

        ArrayList<String> jTrainsStation = jsonEngine.fromJson(from, ArrayList.class);
        for(String s : jTrainsStation) trainStations.add(new WagonMining(s));

        return trainStations;
    }

    public Mine getMine(){
        return this.currentMine;
    }

    public Wagon getWagon() {
        return wagon;
    }

    public boolean linkWagonToTrain(Train train) {
        for(Wagon w : train.getWagons()) {
            if(wagon.getId() == w.getId()) {
                wagon = w;
                return true;
            }
        }
        return false;
    }
}
