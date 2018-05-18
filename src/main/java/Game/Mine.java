package Game;

import com.google.gson.*;

import java.util.ArrayList;

public class Mine {
    private int id;
    private String resource = "unknown";// change to ID ?
    private int amount;
    private int place;

    public Mine() {}

    public Mine(String json) {
        fromJSON(json);
    }

    public Mine(int i, String r, int a, int p) {
        id = i;
        resource = r;
        amount = a;
        place = p;
    }

    public String getResource() {
        return resource;
    }

    public String toJSON() {
        Gson jsonEngine = new GsonBuilder().create();

        JsonObject mine = new JsonObject();
        mine.add("id", new JsonPrimitive(id));
        mine.add("resource", new JsonPrimitive(resource));
        mine.add("amount", new JsonPrimitive(amount));
        mine.add("place", new JsonPrimitive(place));

        return jsonEngine.toJson(mine);
    }

    public void fromJSON(String from) {
        Gson jsonEngine = new GsonBuilder().create();

        JsonObject mine = jsonEngine.fromJson(from, JsonObject.class);
        id = mine.get("id").getAsInt();
        resource = mine.get("resource").getAsString();
        amount = mine.get("amount").getAsInt();
        place = mine.get("place").getAsInt();
    }

    public static String listToJSON(ArrayList<Mine> mines) {
        Gson jsonEngine = new GsonBuilder().create();

        JsonArray list = new JsonArray();
        for(Mine m : mines) list.add(new JsonPrimitive(m.toJSON()));

        return jsonEngine.toJson(list);
    }

    public static ArrayList<Mine> listFromJSON(String from) {
        ArrayList<Mine> mines = new ArrayList<Mine>();
        Gson jsonEngine = new GsonBuilder().create();

        ArrayList<String> jMines = jsonEngine.fromJson(from, ArrayList.class);
        for(String s : jMines) mines.add(new Mine(s));

        return mines;
    }

    public int getId() {
        return id;
    }

    public int getAmount() {
        return amount;
    }

    public int getPlace() {
        return place;
    }
}
