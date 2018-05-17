package Game;

import com.google.gson.*;

import java.util.ArrayList;

public class Mine {
    private String resource;

    public Mine(String r) {
        resource = r;
    }

    public String getResource() {
        return resource;
    }

    public String toJSON() {
        Gson jsonEngine = new GsonBuilder().create();

        JsonObject mine = new JsonObject();
        mine.add("resource", new JsonPrimitive(resource));

        return jsonEngine.toJson(mine);
    }

    public void fromJSON(String from) {
        Gson jsonEngine = new GsonBuilder().create();

        JsonObject trainStation = jsonEngine.fromJson(from, JsonObject.class);
        resource = trainStation.get("resource").getAsString();

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
        for(String s : jMines) {
            Mine m = new Mine("");//bad
            m.fromJSON(s);
            mines.add(m);
        }

        return mines;
    }
}
