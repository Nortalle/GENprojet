package Game;

import Utils.WagonStats;
import com.google.gson.*;

import java.util.ArrayList;

public class Wagon {
    private int id;
    private int weight;
    private int level;
    private int typeID;

    public Wagon() {}

    public Wagon(String json) {
        fromJSON(json);
    }

    public Wagon(int id , int weight, int level, int typeID) {
        this.id = id;
        this.weight = weight;
        this.level = level;
        this.typeID = typeID;
    }

    public String toJSON() {
        Gson jsonEngine = new GsonBuilder().create();

        JsonObject wagon = new JsonObject();
        wagon.add("id", new JsonPrimitive(id));
        wagon.add("weight", new JsonPrimitive(weight));
        wagon.add("level", new JsonPrimitive(level));
        wagon.add("typeID", new JsonPrimitive(typeID));

        return jsonEngine.toJson(wagon);
    }

    public void fromJSON(String from) {
        Gson jsonEngine = new GsonBuilder().create();

        JsonObject wagon = jsonEngine.fromJson(from, JsonObject.class);
        id = wagon.get("id").getAsInt();
        weight = wagon.get("weight").getAsInt();
        level = wagon.get("level").getAsInt();
        typeID = wagon.get("typeID").getAsInt();
    }

    public static String listToJSON(ArrayList<Wagon> wagons) {
        Gson jsonEngine = new GsonBuilder().create();

        JsonArray list = new JsonArray();
        for(Wagon w : wagons) list.add(new JsonPrimitive(w.toJSON()));

        return jsonEngine.toJson(list);
    }

    public static ArrayList<Wagon> listFromJSON(String from) {
        ArrayList<Wagon> wagons = new ArrayList<Wagon>();
        Gson jsonEngine = new GsonBuilder().create();

        ArrayList<String> jWagons = jsonEngine.fromJson(from, ArrayList.class);
        for(String s : jWagons) wagons.add(new Wagon(s));// need to detect sub class

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
