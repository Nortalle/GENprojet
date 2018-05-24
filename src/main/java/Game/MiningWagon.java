package Game;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

public class MiningWagon extends Wagon {
    private Mine currentMine;

    public MiningWagon() {}

    public MiningWagon(String json) {
        fromJSON(json);
    }

    public MiningWagon(int id , int weight, int level, int typeID, Mine currentMine) {
        super(id, weight, level, typeID);
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

    @Override
    public String toJSON() {
        Gson jsonEngine = new GsonBuilder().create();

        JsonObject miningWagon = new JsonObject();
        miningWagon.add("wagon", new JsonPrimitive(super.toJSON()));
        miningWagon.add("currentMine", new JsonPrimitive(currentMine.toJSON()));

        return jsonEngine.toJson(miningWagon);
    }

    @Override
    public void fromJSON(String from) {
        Gson jsonEngine = new GsonBuilder().create();

        JsonObject miningWagon = jsonEngine.fromJson(from, JsonObject.class);
        super.fromJSON(miningWagon.get("wagon").getAsString());
        if(currentMine == null) currentMine = new Mine();// can we do better ?
        currentMine.fromJSON(miningWagon.get("currentMine").getAsString());
    }
}
