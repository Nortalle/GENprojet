package Game;

import Utils.JsonUtility;
import Utils.Ressource;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

import java.util.ArrayList;
import java.util.stream.Collectors;

public class SimpleMine {
    private int id;
    private int resource;

    public SimpleMine() {}

    public SimpleMine(JsonObject json) {
        fromJson(json);
    }

    public SimpleMine(String json) {
        fromJson((JsonObject) JsonUtility.fromJson(json));
    }

    public SimpleMine(Mine mine) {
        this.id = mine.getId();
        this.resource = mine.getResource();
    }

    public SimpleMine(int id, int resource) {
        this.id = id;
        this.resource = resource;
    }

    public JsonObject toJson() {
        JsonObject mine = new JsonObject();
        mine.add("i", new JsonPrimitive(id));
        mine.add("r", new JsonPrimitive(resource));

        return mine;
    }

    public void fromJson(JsonObject from) {
        id = from.get("i").getAsInt();
        resource = from.get("r").getAsInt();
    }

    @Override
    public String toString() {
        return Ressource.RessourceToString(resource) + " Mine " + id;
    }

    public static ArrayList<SimpleMine> listToSimple(ArrayList<Mine> mines) {
        return  mines.stream()
                .map(SimpleMine::new)
                .collect(Collectors.toCollection(ArrayList::new));
    }
}
