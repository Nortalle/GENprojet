package Utils;

import java.util.LinkedList;



public class Recipe {

    private static LinkedList<Recipe> allRecipes = new LinkedList<>();

    public static LinkedList<Recipe> getAllRecipes(){
        return allRecipes;
    }

    private LinkedList<ResourceAmount> cost;
    private String name;
    private ResourceAmount finalProduct;
    private int productionTime;

    Recipe(String n, ResourceAmount fp, int time){
        name = n;
        cost = new LinkedList<>();
        finalProduct = fp;
        productionTime = time;
        allRecipes.add(this);
    }

    public LinkedList<ResourceAmount> getCost() {
        return cost;
    }

    public String getName(){
        return name;
    }

    public int getProductionTime() {
        return productionTime;
    }

    public void addCost(ResourceAmount c){
        cost.add(c);
    }

    public ResourceAmount getFinalProduct(){
        return finalProduct;
    }

    // Définition des recettes existantes

    static Recipe IRON_INGOT_RCPT;
    static {
        IRON_INGOT_RCPT = new Recipe("Iron Ingot", new ResourceAmount(Ressource.Type.IRON_INGOT, 1), 10);
        IRON_INGOT_RCPT.addCost(new ResourceAmount(Ressource.Type.IRON_ORE, 10));
        IRON_INGOT_RCPT.addCost(new ResourceAmount(Ressource.Type.CHARCOAL, 10));
    }

    static Recipe CHARCOAL_RCPT;
    static {
        CHARCOAL_RCPT = new Recipe("Charcoal", new ResourceAmount(Ressource.Type.CHARCOAL, 1), 3);
        CHARCOAL_RCPT.addCost(new ResourceAmount(Ressource.Type.WOOD_LOG, 1));
    }

    static Recipe COPPER_INGOT_RCPT;
    static {
        COPPER_INGOT_RCPT = new Recipe("Copper Ingot", new ResourceAmount(Ressource.Type.COPPER_INGOT, 1), 10);
        COPPER_INGOT_RCPT.addCost(new ResourceAmount(Ressource.Type.COPPER_ORE, 10));
        COPPER_INGOT_RCPT.addCost(new ResourceAmount(Ressource.Type.CHARCOAL, 1));
    }

    static Recipe IRON_PLATE_RCPT;
    static {
        IRON_PLATE_RCPT = new Recipe("Iron Plate", new ResourceAmount(Ressource.Type.IRON_PLATE,1), 15);
        IRON_PLATE_RCPT.addCost(new ResourceAmount(Ressource.Type.IRON_INGOT,1));
    }

    static Recipe IRON_WHEEL_RCPT;
    static {
        IRON_WHEEL_RCPT = new Recipe("Iron Wheel", new ResourceAmount(Ressource.Type.IRON_WHEEL, 1), 20);
        IRON_WHEEL_RCPT.addCost(new ResourceAmount(Ressource.Type.IRON_INGOT, 2));
    }
    static Recipe COPPER_GEAR_RCPT;
    static {
        COPPER_GEAR_RCPT = new Recipe("Copper Gear", new ResourceAmount(Ressource.Type.COPPER_GEAR, 1), 20);
        COPPER_GEAR_RCPT.addCost(new ResourceAmount(Ressource.Type.COPPER_INGOT, 2));
    }


    static Recipe COPPER_TUBE_RCPT;
    static {
        COPPER_TUBE_RCPT = new Recipe("Copper Tube", new ResourceAmount(Ressource.Type.COPPER_TUBE,1), 15);
        COPPER_TUBE_RCPT.addCost(new ResourceAmount(Ressource.Type.COPPER_INGOT,1));
    }

    static Recipe DRILL_T1_RCPT;
    static {
        DRILL_T1_RCPT = new Recipe("Drill T1", new ResourceAmount(Ressource.Type.DRILL_T1,1), 120);
        DRILL_T1_RCPT.addCost(new ResourceAmount(Ressource.Type.IRON_PLATE, 100));
        DRILL_T1_RCPT.addCost(new ResourceAmount(Ressource.Type.COPPER_GEAR, 30));
    }
}

