package Utils;

import java.util.ArrayList;

public class Recipe {

    private static ArrayList<Recipe> allRecipes = new ArrayList<>();

    public static ArrayList<Recipe> getAllRecipes(){
        return allRecipes;
    }

    public static Recipe getReciepAtIndex(int i) { return allRecipes.get(i); }

    private ArrayList<ResourceAmount> cost;
    private String name;
    private ResourceAmount finalProduct;
    private int productionTime;

    Recipe(String n, ResourceAmount fp, int time){
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

    public ResourceAmount getFinalProduct(){
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

    static Recipe IRON_INGOT_RCPT;
    static {
        IRON_INGOT_RCPT = new Recipe("Iron Ingot", new ResourceAmount(Ressource.Type.IRON_INGOT, 1), 5);
        IRON_INGOT_RCPT.addCost(new ResourceAmount(Ressource.Type.IRON_ORE, 10));
        IRON_INGOT_RCPT.addCost(new ResourceAmount(Ressource.Type.CHARCOAL, 1));
    }

    static Recipe CHARCOAL_RCPT;
    static {
        CHARCOAL_RCPT = new Recipe("Charcoal", new ResourceAmount(Ressource.Type.CHARCOAL, 1), 3);
        CHARCOAL_RCPT.addCost(new ResourceAmount(Ressource.Type.WOOD_LOG, 1));
    }

    static Recipe COPPER_INGOT_RCPT;
    static {
        COPPER_INGOT_RCPT = new Recipe("Copper Ingot", new ResourceAmount(Ressource.Type.COPPER_INGOT, 1), 5);
        COPPER_INGOT_RCPT.addCost(new ResourceAmount(Ressource.Type.COPPER_ORE, 10));
        COPPER_INGOT_RCPT.addCost(new ResourceAmount(Ressource.Type.CHARCOAL, 1));
    }

    static Recipe IRON_PLATE_RCPT;
    static {
        IRON_PLATE_RCPT = new Recipe("Iron Plate", new ResourceAmount(Ressource.Type.IRON_PLATE,1), 10);
        IRON_PLATE_RCPT.addCost(new ResourceAmount(Ressource.Type.IRON_INGOT,1));
    }

    static Recipe IRON_WHEEL_RCPT;
    static {
        IRON_WHEEL_RCPT = new Recipe("Iron Wheel", new ResourceAmount(Ressource.Type.IRON_WHEEL, 1), 15);
        IRON_WHEEL_RCPT.addCost(new ResourceAmount(Ressource.Type.IRON_INGOT, 2));
    }
    static Recipe COPPER_GEAR_RCPT;
    static {
        COPPER_GEAR_RCPT = new Recipe("Copper Gear", new ResourceAmount(Ressource.Type.COPPER_GEAR, 1), 15);
        COPPER_GEAR_RCPT.addCost(new ResourceAmount(Ressource.Type.COPPER_INGOT, 2));
    }


    static Recipe COPPER_TUBE_RCPT;
    static {
        COPPER_TUBE_RCPT = new Recipe("Copper Tube", new ResourceAmount(Ressource.Type.COPPER_TUBE,1), 10);
        COPPER_TUBE_RCPT.addCost(new ResourceAmount(Ressource.Type.COPPER_INGOT,1));
    }

    static Recipe DRILL_T1_RCPT;
    static {
        DRILL_T1_RCPT = new Recipe("Drill T1", new ResourceAmount(Ressource.Type.DRILL_T1,1), 120);
        DRILL_T1_RCPT.addCost(new ResourceAmount(Ressource.Type.IRON_PLATE, 100));
        DRILL_T1_RCPT.addCost(new ResourceAmount(Ressource.Type.COPPER_GEAR, 30));
    }

    static Recipe PLANK_RCPT;
    static {
        PLANK_RCPT = new Recipe("Plank", new ResourceAmount(Ressource.Type.PLANK,1), 2);
        PLANK_RCPT.addCost(new ResourceAmount(Ressource.Type.WOOD_LOG, 1));
    }

    static Recipe NAILS_RCPT;
    static {
        NAILS_RCPT = new Recipe("5x Nail Boxes", new ResourceAmount(Ressource.Type.NAILS,5), 5);
        NAILS_RCPT.addCost(new ResourceAmount(Ressource.Type.IRON_INGOT, 1));
    }

    static Recipe WOODEN_CRATE_RCPT;
    static {
        WOODEN_CRATE_RCPT = new Recipe("Wooden Crate", new ResourceAmount(Ressource.Type.WOODEN_CRATE,1), 15);
        WOODEN_CRATE_RCPT.addCost(new ResourceAmount(Ressource.Type.PLANK, 5));
        WOODEN_CRATE_RCPT.addCost(new ResourceAmount(Ressource.Type.NAILS, 10));
    }

    static Recipe STEEL_INGOT_RCPT;
    static {
        STEEL_INGOT_RCPT = new Recipe("Steel Ingot", new ResourceAmount(Ressource.Type.STEEL_INGOT,1), 15);
        STEEL_INGOT_RCPT.addCost(new ResourceAmount(Ressource.Type.CHARCOAL, 1));
        STEEL_INGOT_RCPT.addCost(new ResourceAmount(Ressource.Type.IRON_INGOT, 1));
    }

    static Recipe RIVET_RCPT;
    static {
        RIVET_RCPT = new Recipe("5x Rivet", new ResourceAmount(Ressource.Type.RIVET,5), 5);
        RIVET_RCPT.addCost(new ResourceAmount(Ressource.Type.COPPER_INGOT, 1));
    }

    static Recipe COPPER_CABLE_RCPT;
    static {
        COPPER_CABLE_RCPT = new Recipe("3x Copper Cable", new ResourceAmount(Ressource.Type.COPPER_CABLE,3), 5);
        COPPER_CABLE_RCPT.addCost(new ResourceAmount(Ressource.Type.COPPER_INGOT, 1));
    }

    static Recipe ROBOTIC_ARM_RCPT;
    static {
        ROBOTIC_ARM_RCPT = new Recipe("Robotic Arm", new ResourceAmount(Ressource.Type.ROBOTIC_ARM_T1,1), 120);
        ROBOTIC_ARM_RCPT.addCost(new ResourceAmount(Ressource.Type.STEEL_INGOT, 2));
        ROBOTIC_ARM_RCPT.addCost(new ResourceAmount(Ressource.Type.COPPER_GEAR, 12));
        ROBOTIC_ARM_RCPT.addCost(new ResourceAmount(Ressource.Type.RIVET, 1));
        ROBOTIC_ARM_RCPT.addCost(new ResourceAmount(Ressource.Type.COPPER_CABLE, 16));
    }

    static Recipe PETROL_RCPT;
    static {
        PETROL_RCPT = new Recipe("Petrol", new ResourceAmount(Ressource.Type.PETROL,1), 5);
        PETROL_RCPT.addCost(new ResourceAmount(Ressource.Type.OIL, 10));
        PETROL_RCPT.addCost(new ResourceAmount(Ressource.Type.WATER, 10));
    }

    static Recipe PLASTIC_RCPT;
    static {
        PLASTIC_RCPT = new Recipe("Plastic", new ResourceAmount(Ressource.Type.PLASTIC,1), 10);
        PLASTIC_RCPT.addCost(new ResourceAmount(Ressource.Type.PETROL, 1));
    }

    static Recipe PLASTIC_SHEET_RCPT;
    static {
        PLASTIC_SHEET_RCPT = new Recipe("3x Plastic Sheet", new ResourceAmount(Ressource.Type.PLASTIC_SHEET,3), 5);
        PLASTIC_SHEET_RCPT.addCost(new ResourceAmount(Ressource.Type.PLASTIC, 1));
    }

    static Recipe SAW_T1_RCPT;
    static {
        SAW_T1_RCPT = new Recipe("Saw T1", new ResourceAmount(Ressource.Type.SAW_T1,1), 60);
        SAW_T1_RCPT.addCost(new ResourceAmount(Ressource.Type.STEEL_INGOT, 5));
    }

    static Recipe PUMP_T1_RCPT;
    static {
        PUMP_T1_RCPT = new Recipe("Pump T1", new ResourceAmount(Ressource.Type.PUMP_T1,1), 120);
        PUMP_T1_RCPT.addCost(new ResourceAmount(Ressource.Type.IRON_PLATE, 3));
        PUMP_T1_RCPT.addCost(new ResourceAmount(Ressource.Type.IRON_WHEEL, 1));
        PUMP_T1_RCPT.addCost(new ResourceAmount(Ressource.Type.COPPER_TUBE, 6));
        PUMP_T1_RCPT.addCost(new ResourceAmount(Ressource.Type.COPPER_GEAR, 3));
        PUMP_T1_RCPT.addCost(new ResourceAmount(Ressource.Type.RIVET, 15));
    }

    static Recipe PROCESSOR_T1_RCPT;
    static {
        PROCESSOR_T1_RCPT = new Recipe("Processeur T1", new ResourceAmount(Ressource.Type.PROCESSOR_T1,1), 180);
        PROCESSOR_T1_RCPT.addCost(new ResourceAmount(Ressource.Type.COPPER_CABLE, 8));
        PROCESSOR_T1_RCPT.addCost(new ResourceAmount(Ressource.Type.PLASTIC_SHEET, 5));
        PROCESSOR_T1_RCPT.addCost(new ResourceAmount(Ressource.Type.GOLD_SHEET, 2));
    }

    static Recipe GOLD_INGOT_RCPT;
    static {
        GOLD_INGOT_RCPT = new Recipe("Gold Ingot", new ResourceAmount(Ressource.Type.GOLD_INGOT,1), 10);
        GOLD_INGOT_RCPT.addCost(new ResourceAmount(Ressource.Type.GOLD_ORE, 10));
        GOLD_INGOT_RCPT.addCost(new ResourceAmount(Ressource.Type.CHARCOAL, 1));
    }

    static Recipe GOLD_SHEET_RCPT;
    static {
        GOLD_SHEET_RCPT = new Recipe("2x Gold Sheet", new ResourceAmount(Ressource.Type.GOLD_SHEET,2), 10);
        GOLD_SHEET_RCPT.addCost(new ResourceAmount(Ressource.Type.GOLD_INGOT, 1));
    }

}

