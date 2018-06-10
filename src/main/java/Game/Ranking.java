package Game;

import Utils.JsonUtility;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

public class Ranking {
    private String playerName;
    private int global;
    private int drill;
    private int saw;
    private int pump;
    private int items;

    public Ranking() {}

    public Ranking(String playerName, int global, int drill, int saw, int pump, int items) {
        this.playerName = playerName;
        this.global = global;
        this.drill = drill;
        this.saw = saw;
        this.pump = pump;
        this.items = items;
    }

    public Ranking(JsonObject json) {
        fromJson(json);
    }

    public Ranking(String json) {
        fromJson((JsonObject) JsonUtility.fromJson(json));
    }

    public JsonObject toJson() {
        JsonObject ranking = new JsonObject();
        ranking.add("playerName", new JsonPrimitive(playerName));
        ranking.add("global", new JsonPrimitive(global));
        ranking.add("drill", new JsonPrimitive(drill));
        ranking.add("saw", new JsonPrimitive(saw));
        ranking.add("pump", new JsonPrimitive(pump));
        ranking.add("items", new JsonPrimitive(items));

        return ranking;
    }

    public void fromJson(JsonObject from) {
        playerName = from.get("trainStationETA").getAsString();
        global = from.get("global").getAsInt();
        drill = from.get("drill").getAsInt();
        saw = from.get("saw").getAsInt();
        pump = from.get("pump").getAsInt();
        items = from.get("items").getAsInt();
    }

    public String getPlayerName() {
        return playerName;
    }

    public int getGlobal() {
        return global;
    }

    public int getDrill() {
        return drill;
    }

    public int getSaw() {
        return saw;
    }

    public int getPump() {
        return pump;
    }

    public int getItems() {
        return items;
    }
}
