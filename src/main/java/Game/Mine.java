package Game;

import Utils.JsonUtility;
import Utils.Ressource;
import com.google.gson.*;

import java.util.ArrayList;

public class Mine {
    private int id;
    private int resource = 0;// change to ID ?
    private int amount;
    private int place;

    public Mine() {}

    public Mine(JsonObject json) {
        fromJson(json);
    }

    public Mine(String json) {
        fromJson((JsonObject) JsonUtility.fromJson(json));
    }

    public Mine(int i, int r, int a, int p) {
        id = i;
        resource = r;
        amount = a;
        place = p;
    }

    public int getResource() {
        return resource;
    }

    public JsonObject toJson() {
        JsonObject mine = new JsonObject();
        mine.add("id", new JsonPrimitive(id));
        mine.add("resource", new JsonPrimitive(resource));
        mine.add("amount", new JsonPrimitive(amount));
        mine.add("place", new JsonPrimitive(place));

        return mine;
    }

    public void fromJson(JsonObject from) {
        id = from.get("id").getAsInt();
        resource = from.get("resource").getAsInt();
        amount = from.get("amount").getAsInt();
        place = from.get("place").getAsInt();
    }

    public static JsonArray listToJson(ArrayList<Mine> mines) {
        JsonArray list = new JsonArray();
        for(Mine m : mines) list.add(m.toJson());

        return list;
    }

    public static ArrayList<Mine> listFromJson(JsonArray from) {
        ArrayList<Mine> mines = new ArrayList<>();
        for(JsonElement j : from) mines.add(new Mine((JsonObject) j));

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

    public void setAmount(int new_amount) {
        this.amount = new_amount;
    }

    @Override
    public String toString() {
        return id + " " + Ressource.RessourceToString(Ressource.Type.values()[resource]) + " : " + amount;
    }
}
