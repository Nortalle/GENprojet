package Utils;

import Game.Craft;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

import java.util.ArrayList;

public class ResourceAmount {
    private Ressource.Type ressource;
    private int quantity;

    public ResourceAmount(JsonObject json) {
        fromJson(json);
    }

    public ResourceAmount(String json) {
        fromJson((JsonObject) JsonUtility.fromJson(json));
    }

    public ResourceAmount(Ressource.Type r, int q){
        ressource = r;
        quantity = q;
    }

    public Ressource.Type getRessource() {
        return ressource;
    }

    public int getQuantity() {
        return quantity;
    }

    @Override
    public String toString() {
        return quantity + "x" + Ressource.RessourceToString(ressource);
    }

    public JsonObject toJson() {
        JsonObject resourceAmount = new JsonObject();
        resourceAmount.add("ressource", new JsonPrimitive(ressource.ordinal()));
        resourceAmount.add("quantity", new JsonPrimitive(quantity));

        return resourceAmount;
    }

    public void fromJson(JsonObject from) {
        ressource = Ressource.Type.values()[from.get("ressource").getAsInt()];
        quantity = from.get("quantity").getAsInt();
    }

    public static JsonArray listToJson(ArrayList<ResourceAmount> resourceAmounts) {
        JsonArray list = new JsonArray();
        for(ResourceAmount ra : resourceAmounts) list.add(ra.toJson());

        return list;
    }

    public static ArrayList<ResourceAmount> listFromJson(JsonArray from) {
        ArrayList<ResourceAmount> resourceAmounts = new ArrayList<>();
        for(JsonElement j : from) resourceAmounts.add(new ResourceAmount((JsonObject) j));

        return resourceAmounts;
    }
}
