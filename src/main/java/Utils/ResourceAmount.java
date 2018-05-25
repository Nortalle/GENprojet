package Utils;

public class ResourceAmount {
    private Ressource.Type ressource;
    private int quantity;

    ResourceAmount(Ressource.Type r, int q){
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
