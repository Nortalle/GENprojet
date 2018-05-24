package Game;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

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
        currentMine.fromJSON(miningWagon.get("wagon").getAsString());
        if(currentMine == null) currentMine = new Mine();// can we do better ?
        currentMine.fromJSON(miningWagon.get("currentMine").getAsString());
    }

    public Wagon getWagon() {
        return wagon;
    }
}
