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
    private int remainingTime_S;

    public UpgradeWagon(JsonObject json){ fromJson(json);}

    public UpgradeWagon(String json) {
        fromJson((JsonObject) JsonUtility.fromJson(json));
    }

    public UpgradeWagon(String username, Wagon wagon, int remainingTime_S) {
        this.username = username;
        this.wagon_to_upgrade = wagon;
        this.remainingTime_S = remainingTime_S;
    }

    public JsonObject toJson() {
        JsonObject craft = new JsonObject();
        craft.add("username", new JsonPrimitive(username));
        craft.add("wagon_to_upgrade", wagon_to_upgrade.toJson());
        craft.add("remainingTime", new JsonPrimitive(remainingTime_S));

        return craft;
    }

    public void fromJson(JsonObject from) {
        username = from.get("username").getAsString();
        wagon_to_upgrade.fromJson(from.get("wagon_to_upgrade").getAsJsonObject());
        remainingTime_S = from.get("remainingTime").getAsInt();
    }

    public static JsonArray listToJson(ArrayList<UpgradeWagon> upgrades) {
        JsonArray list = new JsonArray();
        for(UpgradeWagon upgrade : upgrades) list.add(upgrade.toJson());

        return list;
    }

    public static ArrayList<UpgradeWagon> listFromJson(JsonArray from) {
        ArrayList<UpgradeWagon> upgrades = new ArrayList<>();
        for(JsonElement j : from) upgrades.add(new UpgradeWagon((JsonObject) j));

        return upgrades;
    }

    public void decreaseRemainingTime() {
        remainingTime_S--;
    }

    public String getUsername() {
        return username;
    }

    public Wagon getWagon_to_upgrade() {
        return wagon_to_upgrade;
    }

    public int getRemaintingTime() {
        return remainingTime_S;
    }
}
