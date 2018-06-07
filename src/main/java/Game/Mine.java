package Game;

import Utils.JsonUtility;
import Utils.Ressource;
import com.google.gson.*;

import java.util.ArrayList;

public class Mine {
    private int id;
    private int resource = 0;// change to ID ?
    private int amount;
    private int max;
    private int regen;
    private int place;

    public Mine() {}

    public Mine(JsonObject json) {
        fromJson(json);
    }

    public Mine(String json) {
        fromJson((JsonObject) JsonUtility.fromJson(json));
    }

    public Mine(int id, int resource, int amout, int max, int regen, int place) {
        this.id = id;
        this.resource = resource;
        this.amount = amout;
        this.max = max;
        this.regen = regen;
        this.place = place;
    }

    public int getResource() {
        return resource;
    }

    public JsonObject toJson() {
        JsonObject mine = new JsonObject();
        mine.add("id", new JsonPrimitive(id));
        mine.add("resource", new JsonPrimitive(resource));
        mine.add("amount", new JsonPrimitive(amount));
        mine.add("max", new JsonPrimitive(max));
        mine.add("regen", new JsonPrimitive(regen));
        mine.add("place", new JsonPrimitive(place));

        return mine;
    }

    public void fromJson(JsonObject from) {
        id = from.get("id").getAsInt();
        resource = from.get("resource").getAsInt();
        amount = from.get("amount").getAsInt();
        max = from.get("max").getAsInt();
        regen = from.get("regen").getAsInt();
        place = from.get("place").getAsInt();
    }

    public int getId() {
        return id;
    }

    public int getAmount() {
        return amount;
    }

    public int getMax() {
        return max;
    }

    public int getRegen() {
        return regen;
    }

    public int getPlace() {
        return place;
    }

    public void setAmount(int new_amount) {
        this.amount = new_amount;
    }

    public Mine reduceAmount(int i) {
        amount -= i;
        return this;
    }

    @Override
    public String toString() {
        return id + " " + Ressource.RessourceToString(Ressource.Type.values()[resource]) + " : " + amount;
    }
}
