package Game;

import Utils.JsonUtility;
import Utils.Ressource;
import com.google.gson.*;

import java.util.ArrayList;

public class Mine {
    private int id;
    private int resource;
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

    public Mine(int id, int resource, int amount, int max, int regen, int place) {
        this.id = id;
        this.resource = resource;
        this.amount = amount;
        this.max = max;
        this.regen = regen;
        this.place = place;
    }

    public int getResource() {
        return resource;
    }

    public JsonObject toJson() {
        JsonObject mine = new JsonObject();
        mine.add("i", new JsonPrimitive(id));
        mine.add("r", new JsonPrimitive(resource));
        mine.add("a", new JsonPrimitive(amount));
        mine.add("m", new JsonPrimitive(max));
        mine.add("g", new JsonPrimitive(regen));
        mine.add("p", new JsonPrimitive(place));

        return mine;
    }

    public void fromJson(JsonObject from) {
        id = from.get("i").getAsInt();
        resource = from.get("r").getAsInt();
        amount = from.get("a").getAsInt();
        max = from.get("m").getAsInt();
        regen = from.get("g").getAsInt();
        place = from.get("p").getAsInt();
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
        return Ressource.RessourceToString(resource) + " Mine " + id;
    }

    public String printInfo() {
        return toString() + " [" + amount + "] left";
    }
}
