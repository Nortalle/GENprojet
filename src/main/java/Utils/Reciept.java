package Utils;

import java.util.LinkedList;



public class Reciept {

    private static LinkedList<Reciept> allReciepts = new LinkedList<Reciept>();

    public static LinkedList<Reciept> getAllReciepts(){
        return allReciepts;
    }

    private LinkedList<Cost> costs;
    private String name;
    private Cost finalProduct;

    Reciept(String n, Cost fp){
        name = n;
        costs = new LinkedList<Cost>();
        finalProduct = fp;
        allReciepts.add(this);
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

    public Cost getFinalProduct(){
        return finalProduct;
    }

    // Définition des recettes existantes

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

    static Reciept IRON_PLATE_RCPT;
    static {
        IRON_PLATE_RCPT = new Reciept("Iron Plate", new Cost(Ressource.IRON_PLATE,1));
        IRON_PLATE_RCPT.addCost(new Cost(Ressource.IRON_INGOT,1));
    }

    static Reciept IRON_WHEEL_RCPT;
    static {
        IRON_WHEEL_RCPT = new Reciept("Iron Wheel", new Cost(Ressource.IRON_WHEEL, 1));
        IRON_WHEEL_RCPT.addCost(new Cost(Ressource.IRON_INGOT, 2));
    }
    static Reciept COPPER_GEAR_RCPT;
    static {
        COPPER_GEAR_RCPT = new Reciept("Copper Gear", new Cost(Ressource.COPPER_GEAR, 1));
        COPPER_GEAR_RCPT.addCost(new Cost(Ressource.COPPER_INGOT, 2));
    }


    static Reciept COPPER_TUBE_RCPT;
    static {
        COPPER_TUBE_RCPT = new Reciept("Copper Tube", new Cost(Ressource.COPPER_TUBE,1));
        COPPER_TUBE_RCPT.addCost(new Cost(Ressource.COPPER_INGOT,1));
    }

    static Reciept DRILL_T1_RCPT;
    static {
        DRILL_T1_RCPT = new Reciept("Drill T1", new Cost(Ressource.DRILL_T1,1));
        DRILL_T1_RCPT.addCost(new Cost(Ressource.IRON_PLATE, 100));
        DRILL_T1_RCPT.addCost(new Cost(Ressource.COPPER_GEAR, 30));
    }
}

