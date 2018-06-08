package Game;

import Utils.JsonUtility;
import Utils.Ressource;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

public class Offer {
    private int id;
    private String playerName;
    private int offerType;
    private int offerAmount;
    private int priceType;
    private int priceAmount;

    public Offer() {}

    public Offer(int id, String playerName, int offerType, int offerAmount, int priceType, int priceAmount) {
        this.id = id;
        this.playerName = playerName;
        this.offerType = offerType;
        this.offerAmount = offerAmount;
        this.priceType = priceType;
        this.priceAmount = priceAmount;
    }

    public Offer(JsonObject json) {
        fromJson(json);
    }

    public Offer(String json) {
        fromJson((JsonObject) JsonUtility.fromJson(json));
    }

    public int getId() {
        return id;
    }

    public String getPlayerName() {
        return playerName;
    }

    public int getOfferType() {
        return offerType;
    }

    public int getOfferAmount() {
        return offerAmount;
    }

    public int getPriceType() {
        return priceType;
    }

    public int getPriceAmount() {
        return priceAmount;
    }

    public JsonObject toJson() {
        JsonObject train = new JsonObject();
        train.add("id", new JsonPrimitive(id));
        train.add("playerName", new JsonPrimitive(playerName));
        train.add("offerType", new JsonPrimitive(offerType));
        train.add("offerAmount", new JsonPrimitive(offerAmount));
        train.add("priceType", new JsonPrimitive(priceType));
        train.add("priceAmount", new JsonPrimitive(priceAmount));

        return train;
    }

    public void fromJson(JsonObject from) {
        id = from.get("id").getAsInt();
        playerName = from.get("playerName").getAsString();
        offerType = from.get("offerType").getAsInt();
        offerAmount = from.get("offerAmount").getAsInt();
        priceType = from.get("priceType").getAsInt();
        priceAmount = from.get("priceAmount").getAsInt();
    }

    @Override
    public String toString() {
        return playerName + " offers : " + offerAmount + Ressource.RessourceToString(offerType) + " for : " + priceAmount + Ressource.RessourceToString(priceType);
    }
}
