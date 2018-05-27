package Game;

import Utils.JsonUtility;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

import java.util.ArrayList;

public class Craft {
    private String username;
    private int recipeIndex;
    private int remainingTime;

    public Craft(JsonObject json) {
        fromJson(json);
    }

    public Craft(String json) {
        fromJson((JsonObject) JsonUtility.fromJson(json));
    }

    public Craft(String u, int i, int t) {
        username = u;
        recipeIndex = i;
        remainingTime = t;
    }

    public String getUsername() {
        return username;
    }

    public int getRecipeIndex() {
        return recipeIndex;
    }

    public int getRemainingTime() {
        return remainingTime;
    }

    /**
     * Don't forget to test if == 0
     */
    public void decreceRemainingTime() {
        remainingTime--;
    }

    public JsonObject toJson() {
        JsonObject craft = new JsonObject();
        craft.add("username", new JsonPrimitive(username));
        craft.add("recipeIndex", new JsonPrimitive(recipeIndex));
        craft.add("remainingTime", new JsonPrimitive(remainingTime));

        return craft;
    }

    public void fromJson(JsonObject from) {
        username = from.get("username").getAsString();
        recipeIndex = from.get("recipeIndex").getAsInt();
        remainingTime = from.get("remainingTime").getAsInt();
    }

    public static JsonArray listToJson(ArrayList<Craft> crafts) {
        JsonArray list = new JsonArray();
        for(Craft c : crafts) list.add(c.toJson());

        return list;
    }

    public static ArrayList<Craft> listFromJson(JsonArray from) {
        ArrayList<Craft> crafts = new ArrayList<>();
        for(JsonElement j : from) crafts.add(new Craft((JsonObject) j));

        return crafts;
    }
}
