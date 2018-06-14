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

    public class Rank{
        public final int value;
        public final String name;

        Rank(int value, String name){
            this.value = value;
            this.name = name;
        }

        public int value(){
            return value;
        }

        public String name(){
            return name;
        }

    }

    public Rank getGlobalRank(){
        return new Rank(global, playerName);
    }

    public Rank getDrillRank(){
        return new Rank(drill, playerName);
    }

    public Rank getPumplRank(){
        return new Rank(pump, playerName);
    }

    public Rank getSawRank(){
        return new Rank(saw, playerName);
    }

    public Rank getItemsRank(){
        return new Rank(items, playerName);
    }

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
        ranking.add("n", new JsonPrimitive(playerName));
        ranking.add("g", new JsonPrimitive(global));
        ranking.add("d", new JsonPrimitive(drill));
        ranking.add("s", new JsonPrimitive(saw));
        ranking.add("p", new JsonPrimitive(pump));
        ranking.add("i", new JsonPrimitive(items));

        return ranking;
    }

    public void fromJson(JsonObject from) {
        playerName = from.get("n").getAsString();
        global = from.get("g").getAsInt();
        drill = from.get("d").getAsInt();
        saw = from.get("s").getAsInt();
        pump = from.get("p").getAsInt();
        items = from.get("i").getAsInt();
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
