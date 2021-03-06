package Game;

import Utils.JsonUtility;
import Utils.ResourceAmount;
import Utils.Ressource;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

public class Offer {
    private int id;
    private String playerName;
    private ResourceAmount offer;
    private ResourceAmount price;

    public Offer() {}

    public Offer(int id, String playerName, ResourceAmount offer, ResourceAmount price) {
        this.id = id;
        this.playerName = playerName;
        this.offer = offer;
        this.price = price;
    }

    public Offer(int id, String playerName, int offerType, int offerAmount, int priceType, int priceAmount) {
        this.id = id;
        this.playerName = playerName;
        this.offer = new ResourceAmount(Ressource.Type.values()[offerType], offerAmount);
        this.price = new ResourceAmount(Ressource.Type.values()[priceType], priceAmount);
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

    public ResourceAmount getOffer() {
        return offer;
    }

    public ResourceAmount getPrice() {
        return price;
    }

    public JsonObject toJson() {
        JsonObject train = new JsonObject();
        train.add("i", new JsonPrimitive(id));
        train.add("n", new JsonPrimitive(playerName));
        train.add("o", offer.toJson());
        train.add("p", price.toJson());

        return train;
    }

    public void fromJson(JsonObject from) {
        id = from.get("i").getAsInt();
        playerName = from.get("n").getAsString();
        if(offer == null) offer = new ResourceAmount((JsonObject) from.get("o"));
        else offer.fromJson((JsonObject) from.get("o"));
        if(price == null) price = new ResourceAmount((JsonObject) from.get("p"));
        else price.fromJson((JsonObject) from.get("p"));
    }

    @Override
    public String toString() {
        return playerName
                + " offers : "
                + offer.getQuantity()
                + "x"
                + Ressource.RessourceToString(offer.getRessource().ordinal())
                + " for : "
                + price.getQuantity()
                + "x"
                + Ressource.RessourceToString(price.getRessource().ordinal());
    }

    public double getRatio() {
        return ((double) price.getQuantity()) / ((double) offer.getQuantity());
    }
}
