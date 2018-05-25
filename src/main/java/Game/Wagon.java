package Game;

import Utils.JsonUtility;
import Utils.WagonStats;
import com.google.gson.*;

import java.util.ArrayList;

public class Wagon {
    private int id;
    private int weight;
    private int level;
    private int typeID;

    public Wagon() {}

    public Wagon(JsonObject json) {
        fromJson(json);
    }

    public Wagon(String json) {
        fromJson((JsonObject) JsonUtility.fromJson(json));
    }

    public Wagon(int id , int weight, int level, int typeID) {
        this.id = id;
        this.weight = weight;
        this.level = level;
        this.typeID = typeID;
    }

    public JsonObject toJson() {
        JsonObject wagon = new JsonObject();
        wagon.add("id", new JsonPrimitive(id));
        wagon.add("weight", new JsonPrimitive(weight));
        wagon.add("level", new JsonPrimitive(level));
        wagon.add("typeID", new JsonPrimitive(typeID));

        return wagon;
    }

    public void fromJson(JsonObject from) {
        id = from.get("id").getAsInt();
        weight = from.get("weight").getAsInt();
        level = from.get("level").getAsInt();
        typeID = from.get("typeID").getAsInt();
    }

    public static JsonArray listToJson(ArrayList<Wagon> wagons) {
        JsonArray list = new JsonArray();
        for(Wagon w : wagons) list.add(w.toJson());

        return list;
    }

    public static ArrayList<Wagon> listFromJson(JsonArray from) {
        ArrayList<Wagon> wagons = new ArrayList<>();
        for(JsonElement j : from) wagons.add(new Wagon((JsonObject) j));

        return wagons;
    }

    @Override
    public String toString() {
        return WagonStats.getName(typeID) + "-" + id;
    }

    public int getId() {
        return id;
    }

    public int getTypeID() {
        return typeID;
    }

    public int getLevel() {
        return level;
    }
}
