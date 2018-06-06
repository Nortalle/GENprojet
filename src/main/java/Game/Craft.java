package Game;

import Utils.JsonUtility;
import Utils.Recipe;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

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
    public void decreaseRemainingTime() {
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

    @Override
    public String toString() {
        return Recipe.getAllRecipes().get(recipeIndex).getName() + " : " + remainingTime;
    }
}
