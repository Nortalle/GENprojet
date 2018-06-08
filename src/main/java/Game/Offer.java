package Game;

public class Offer {
    int id;
    String palyerName;
    int offerType;
    int offerAmount;
    int priceType;
    int priceAmount;

    public Offer() {}

    public Offer(int id, String palyerName, int offerType, int offerAmount, int priceType, int priceAmount) {
        this.id = id;
        this.palyerName = palyerName;
        this.offerType = offerType;
        this.offerAmount = offerAmount;
        this.priceType = priceType;
        this.priceAmount = priceAmount;
    }
}
