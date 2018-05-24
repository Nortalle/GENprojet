package Utils;

public class Cost{
    private Ressource.Type ressource;
    private int quantity;

    Cost( Ressource.Type r, int q){
        ressource = r;
        quantity = q;
    }

    public Ressource.Type getRessource() {
        return ressource;
    }

    public int getQuantity() {
        return quantity;
    }
}
