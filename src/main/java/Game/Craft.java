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
        craft.add("u", new JsonPrimitive(username));
        craft.add("r", new JsonPrimitive(recipeIndex));
        craft.add("t", new JsonPrimitive(remainingTime));

        return craft;
    }

    public void fromJson(JsonObject from) {
        username = from.get("u").getAsString();
        recipeIndex = from.get("r").getAsInt();
        remainingTime = from.get("t").getAsInt();
    }

    @Override
    public String toString() {
        return Recipe.getAllRecipes().get(recipeIndex).getName() + " : " + remainingTime;
    }
}
