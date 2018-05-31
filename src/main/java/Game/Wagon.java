package Game;

import Utils.JsonUtility;
import Utils.WagonStats;
import com.google.gson.*;

import java.util.ArrayList;

public class Wagon {
    private int id;
    private int weight;
    private int level;
    private WagonStats.WagonType type;

    public Wagon() {}

    public Wagon(JsonObject json) {
        fromJson(json);
    }

    public Wagon(String json) {
        fromJson((JsonObject) JsonUtility.fromJson(json));
    }

    public Wagon(int id , int weight, int level, WagonStats.WagonType type) {
        this.id = id;
        this.weight = weight;
        this.level = level;
        this.type = type;
    }

    public JsonObject toJson() {
        JsonObject wagon = new JsonObject();
        wagon.add("id", new JsonPrimitive(id));
        wagon.add("weight", new JsonPrimitive(weight));
        wagon.add("level", new JsonPrimitive(level));
        if(type == null){
            type = WagonStats.WagonType.LOCO;
        }
        wagon.add("typeID", new JsonPrimitive(type.ordinal()));

        return wagon;
    }

    public void fromJson(JsonObject from) {
        id = from.get("id").getAsInt();
        weight = from.get("weight").getAsInt();
        level = from.get("level").getAsInt();
        type = WagonStats.WagonType.values()[from.get("typeID").getAsInt()];
    }

    public void levelUp(){
        if(level < WagonStats.LEVEL_MAX){
            level++;
        }
    }

    @Override
    public String toString() {
        return WagonStats.getName(type) + "-" + id;
    }

    public int getId() {
        return id;
    }

    public WagonStats.WagonType getType() {
        return type;
    }

    public int getLevel() {
        return level;
    }

    public int getWeight() {
        return weight;
    }
}
