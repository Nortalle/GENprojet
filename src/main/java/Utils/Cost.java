package Utils;

public class Cost{
    private Ressource ressource;
    private int quantity;

    Cost( Ressource r, int q){
        ressource = r;
        quantity = q;
    }

    public Ressource getRessource() {
        return ressource;
    }

    public int getQuantity() {
        return quantity;
    }
}
