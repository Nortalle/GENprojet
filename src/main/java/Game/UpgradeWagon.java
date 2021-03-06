package Game;

import Utils.JsonUtility;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

import java.util.ArrayList;

public class UpgradeWagon {
    private String username;
    private Wagon wagon_to_upgrade;
    private int remainingTime;

    public UpgradeWagon(JsonObject json){
        fromJson(json);
    }

    public UpgradeWagon(String json) {
        fromJson((JsonObject) JsonUtility.fromJson(json));
    }

    public UpgradeWagon(String username, Wagon wagon, int remainingTime) {
        this.username = username;
        this.wagon_to_upgrade = wagon;
        this.remainingTime = remainingTime;
    }

    public JsonObject toJson() {
        JsonObject craft = new JsonObject();
        craft.add("u", new JsonPrimitive(username));
        craft.add("w", wagon_to_upgrade.toJson());
        craft.add("t", new JsonPrimitive(remainingTime));

        return craft;
    }

    public void fromJson(JsonObject from) {
        username = from.get("u").getAsString();
        if(wagon_to_upgrade == null) wagon_to_upgrade = new Wagon();
        wagon_to_upgrade.fromJson(from.get("w").getAsJsonObject());
        remainingTime = from.get("t").getAsInt();
    }

    public void decreaseRemainingTime() {
        remainingTime--;
    }

    public String getUsername() {
        return username;
    }

    public Wagon getWagon_to_upgrade() {
        return wagon_to_upgrade;
    }

    public int getRemainingTime() {
        return remainingTime;
    }

    @Override
    public String toString() {
        return wagon_to_upgrade.toString() + " " + remainingTime;
    }
}
