package Utils;

import Game.Wagon;

import java.util.ArrayList;

public class WagonRecipe {

    private static ArrayList<WagonRecipe> allRecipes = new ArrayList<>();

    public static ArrayList<WagonRecipe> getAllRecipes(){
        return allRecipes;
    }

    private ArrayList<ResourceAmount> cost;
    private String name;
    private Wagon finalProduct;
    private int productionTime;

    WagonRecipe(String n, Wagon fp, int time){
        name = n;
        cost = new ArrayList<>();
        finalProduct = fp;
        productionTime = time;
        allRecipes.add(this);
    }

    public ArrayList<ResourceAmount> getCost() {
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

    public Wagon getFinalProduct(){
        return finalProduct;
    }

    @Override
    public String toString() {
        return name;
    }

    public int getRecipeIndex() {
        return allRecipes.indexOf(this);
    }



    // Définition des recettes existantes

    static WagonRecipe WAGON_DRILL_RCPT;
    static {
        WAGON_DRILL_RCPT = new WagonRecipe("Drill Wagon", new Wagon(-1, 1000, 1, WagonStats.WagonType.DRILL), 120);
        WAGON_DRILL_RCPT.addCost(new ResourceAmount(Ressource.Type.IRON_PLATE, 10));
        WAGON_DRILL_RCPT.addCost(new ResourceAmount(Ressource.Type.IRON_WHEEL, 8));
        /*
        WAGON_DRILL_RCPT.addCost(new ResourceAmount(Ressource.Type.IRON_PLATE, 45));
        WAGON_DRILL_RCPT.addCost(new ResourceAmount(Ressource.Type.WOOD_LOG, 20));
        WAGON_DRILL_RCPT.addCost(new ResourceAmount(Ressource.Type.IRON_WHEEL, 10));
        WAGON_DRILL_RCPT.addCost(new ResourceAmount(Ressource.Type.COPPER_TUBE, 12));
        WAGON_DRILL_RCPT.addCost(new ResourceAmount(Ressource.Type.STEEL_INGOT, 12));
        WAGON_DRILL_RCPT.addCost(new ResourceAmount(Ressource.Type.COPPER_GEAR, 22));
        WAGON_DRILL_RCPT.addCost(new ResourceAmount(Ressource.Type.DRILL_T1, 4));
        */
    }

    static WagonRecipe WAGON_SAW_RCPT;
    static {
        WAGON_SAW_RCPT = new WagonRecipe("Saw Wagon", new Wagon(-1, 1000, 1,WagonStats.WagonType.SAW), 120);
        WAGON_SAW_RCPT.addCost(new ResourceAmount(Ressource.Type.IRON_PLATE, 35));
        WAGON_SAW_RCPT.addCost(new ResourceAmount(Ressource.Type.IRON_WHEEL, 8));
        WAGON_SAW_RCPT.addCost(new ResourceAmount(Ressource.Type.COPPER_GEAR, 22));
        WAGON_SAW_RCPT.addCost(new ResourceAmount(Ressource.Type.SAW_T1, 4));
    }

    static WagonRecipe WAGON_PUMP_RCPT;
    static {
        WAGON_PUMP_RCPT = new WagonRecipe("Pump Wagon", new Wagon(-1, 1000, 1,WagonStats.WagonType.PUMP), 120);
        WAGON_PUMP_RCPT.addCost(new ResourceAmount(Ressource.Type.IRON_PLATE, 45));
        WAGON_PUMP_RCPT.addCost(new ResourceAmount(Ressource.Type.COPPER_TUBE, 65));
        WAGON_PUMP_RCPT.addCost(new ResourceAmount(Ressource.Type.IRON_WHEEL, 8));
        WAGON_PUMP_RCPT.addCost(new ResourceAmount(Ressource.Type.COPPER_GEAR, 4));
        WAGON_PUMP_RCPT.addCost(new ResourceAmount(Ressource.Type.PUMP_T1, 4));
    }

    static WagonRecipe WAGON_CARGO_RCPT;
    static {
        WAGON_CARGO_RCPT = new WagonRecipe("Cargo Wagon", new Wagon(-1, 1000, 1,WagonStats.WagonType.CARGO), 120);
        WAGON_CARGO_RCPT.addCost(new ResourceAmount(Ressource.Type.IRON_PLATE, 45));
        WAGON_CARGO_RCPT.addCost(new ResourceAmount(Ressource.Type.COPPER_TUBE, 12));
        WAGON_CARGO_RCPT.addCost(new ResourceAmount(Ressource.Type.IRON_WHEEL, 16));
        WAGON_CARGO_RCPT.addCost(new ResourceAmount(Ressource.Type.COPPER_GEAR, 22));
        WAGON_CARGO_RCPT.addCost(new ResourceAmount(Ressource.Type.WOODEN_CRATE, 4));
    }

    static WagonRecipe WAGON_CRAFT_RCPT;
    static {
        WAGON_CRAFT_RCPT = new WagonRecipe("Craft Wagon", new Wagon(-1, 1000, 1,WagonStats.WagonType.CRAFT), 120);
        WAGON_CRAFT_RCPT.addCost(new ResourceAmount(Ressource.Type.IRON_PLATE, 145));
        WAGON_CRAFT_RCPT.addCost(new ResourceAmount(Ressource.Type.COPPER_TUBE, 48));
        WAGON_CRAFT_RCPT.addCost(new ResourceAmount(Ressource.Type.IRON_WHEEL, 12));
        WAGON_CRAFT_RCPT.addCost(new ResourceAmount(Ressource.Type.COPPER_GEAR, 22));
        WAGON_CRAFT_RCPT.addCost(new ResourceAmount(Ressource.Type.ROBOTIC_ARM_T1, 2));
        WAGON_CRAFT_RCPT.addCost(new ResourceAmount(Ressource.Type.PROCESSOR_T1, 1));
    }
}


