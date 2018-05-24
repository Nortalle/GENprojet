package Utils;

import java.util.LinkedList;



public class Reciept {

    LinkedList<Cost> costs;
    String name;
    Cost finalProduct;

    Reciept(String n, Cost fp){
        name = n;
        costs = new LinkedList<Cost>();
        finalProduct = fp;
    }

    public LinkedList<Cost> getCosts() {
        return costs;
    }

    public String getName(){
        return name;
    }

    public void addCost(Cost c){
        costs.add(c);
    }

    static Reciept IRON_INGOT_RCPT;
    static {
        IRON_INGOT_RCPT = new Reciept("Iron Ingot", new Cost(Ressource.IRON_INGOT, 1));
        IRON_INGOT_RCPT.addCost(new Cost(Ressource.IRON_ORE, 10));
        IRON_INGOT_RCPT.addCost(new Cost(Ressource.CHARCOAL, 10));
    }

    static Reciept CHARCOAL_RCPT;
    static {
        CHARCOAL_RCPT = new Reciept("Charcoal", new Cost(Ressource.CHARCOAL, 1));
        CHARCOAL_RCPT.addCost(new Cost(Ressource.WOOD_LOG, 1));
    }

    static Reciept COPPER_INGOT_RCPT;
    static {
        COPPER_INGOT_RCPT = new Reciept("Copper Ingot", new Cost(Ressource.COPPER_INGOT, 1));
        COPPER_INGOT_RCPT.addCost(new Cost(Ressource.COPPER_ORE, 10));
        COPPER_INGOT_RCPT.addCost(new Cost(Ressource.CHARCOAL, 1));
    }


}

