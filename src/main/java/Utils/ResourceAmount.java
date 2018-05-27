package Utils;

public class ResourceAmount {
    private Ressource.Type ressource;
    private int quantity;

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
}
