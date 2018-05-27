package Utils;

import java.util.LinkedList;



public class Recipe {

    private static LinkedList<Recipe> allRecipes = new LinkedList<>();

    public static LinkedList<Recipe> getAllRecipes(){
        return allRecipes;
    }

    private LinkedList<ResourceAmount> resourceAmounts;
    private String name;
    private ResourceAmount finalProduct;

    Recipe(String n, ResourceAmount fp){
        name = n;
        resourceAmounts = new LinkedList<>();
        finalProduct = fp;
        allRecipes.add(this);
    }

    public LinkedList<ResourceAmount> getResourceAmounts() {
        return resourceAmounts;
    }

    public String getName(){
        return name;
    }

    public void addCost(ResourceAmount c){
        resourceAmounts.add(c);
    }

    public ResourceAmount getFinalProduct(){
        return finalProduct;
    }

    // Définition des recettes existantes

    static Recipe IRON_INGOT_RCPT;
    static {
        IRON_INGOT_RCPT = new Recipe("Iron Ingot", new ResourceAmount(Ressource.Type.IRON_INGOT, 1));
        IRON_INGOT_RCPT.addCost(new ResourceAmount(Ressource.Type.IRON_ORE, 10));
        IRON_INGOT_RCPT.addCost(new ResourceAmount(Ressource.Type.CHARCOAL, 10));
    }

    static Recipe CHARCOAL_RCPT;
    static {
        CHARCOAL_RCPT = new Recipe("Charcoal", new ResourceAmount(Ressource.Type.CHARCOAL, 1));
        CHARCOAL_RCPT.addCost(new ResourceAmount(Ressource.Type.WOOD_LOG, 1));
    }

    static Recipe COPPER_INGOT_RCPT;
    static {
        COPPER_INGOT_RCPT = new Recipe("Copper Ingot", new ResourceAmount(Ressource.Type.COPPER_INGOT, 1));
        COPPER_INGOT_RCPT.addCost(new ResourceAmount(Ressource.Type.COPPER_ORE, 10));
        COPPER_INGOT_RCPT.addCost(new ResourceAmount(Ressource.Type.CHARCOAL, 1));
    }

    static Recipe IRON_PLATE_RCPT;
    static {
        IRON_PLATE_RCPT = new Recipe("Iron Plate", new ResourceAmount(Ressource.Type.IRON_PLATE,1));
        IRON_PLATE_RCPT.addCost(new ResourceAmount(Ressource.Type.IRON_INGOT,1));
    }

    static Recipe IRON_WHEEL_RCPT;
    static {
        IRON_WHEEL_RCPT = new Recipe("Iron Wheel", new ResourceAmount(Ressource.Type.IRON_WHEEL, 1));
        IRON_WHEEL_RCPT.addCost(new ResourceAmount(Ressource.Type.IRON_INGOT, 2));
    }
    static Recipe COPPER_GEAR_RCPT;
    static {
        COPPER_GEAR_RCPT = new Recipe("Copper Gear", new ResourceAmount(Ressource.Type.COPPER_GEAR, 1));
        COPPER_GEAR_RCPT.addCost(new ResourceAmount(Ressource.Type.COPPER_INGOT, 2));
    }


    static Recipe COPPER_TUBE_RCPT;
    static {
        COPPER_TUBE_RCPT = new Recipe("Copper Tube", new ResourceAmount(Ressource.Type.COPPER_TUBE,1));
        COPPER_TUBE_RCPT.addCost(new ResourceAmount(Ressource.Type.COPPER_INGOT,1));
    }

    static Recipe DRILL_T1_RCPT;
    static {
        DRILL_T1_RCPT = new Recipe("Drill T1", new ResourceAmount(Ressource.Type.DRILL_T1,1));
        DRILL_T1_RCPT.addCost(new ResourceAmount(Ressource.Type.IRON_PLATE, 100));
        DRILL_T1_RCPT.addCost(new ResourceAmount(Ressource.Type.COPPER_GEAR, 30));
    }
}

