package Game;

import Utils.JsonUtility;
import Utils.WagonRecipe;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

import java.util.ArrayList;

public class CreateWagon {
    private String username;
    private int wagonRecipeIndex;
    private int remainingTime;

    public CreateWagon(JsonObject json){
        fromJson(json);
    }

    public CreateWagon(String json) {
        fromJson((JsonObject) JsonUtility.fromJson(json));
    }

    public CreateWagon(String username, int wagonRecipeIndex, int remainingTime) {
        this.username = username;
        this.wagonRecipeIndex = wagonRecipeIndex;
        this.remainingTime = remainingTime;
    }

    public JsonObject toJson() {
        JsonObject craft = new JsonObject();
        craft.add("u", new JsonPrimitive(username));
        craft.add("r", new JsonPrimitive(wagonRecipeIndex));
        craft.add("t", new JsonPrimitive(remainingTime));

        return craft;
    }

    public void fromJson(JsonObject from) {
        username = from.get("u").getAsString();
        wagonRecipeIndex = from.get("r").getAsInt();
        remainingTime = from.get("t").getAsInt();
    }

    public void decreaseRemainingTime() {
        remainingTime--;
    }

    public String getUsername() {
        return username;
    }

    public int getWagonRecipeIndex() {
        return wagonRecipeIndex;
    }

    public int getRemainingTime() {
        return remainingTime;
    }

    @Override
    public String toString() {
        return WagonRecipe.getAllRecipes().get(wagonRecipeIndex).getName() + " : " + remainingTime;
    }
}
