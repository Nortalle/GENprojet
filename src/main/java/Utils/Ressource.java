package Utils;

import java.util.ArrayList;
import java.util.Random;

public class Ressource {
    /**
     * Enum qui représente les types des ressources du jeu. c'est l'ID unique qui représente une ressource dans la BDD
     */
    //scrum, eau, bois, charbon, petrol, fer, cuivre, acier, or
    public enum  Type {
        CHARCOAL,
        CRUDE_OIL,
        IRON_ORE,
        COPPER_ORE,
        GOLD_ORE,   // basic ressources
        CARDBOARD,
        CARDBOARD_BOX,
        COPPER_CABLE,
        COPPER_GEAR,
        COPPER_INGOT,
        COPPER_TUBE,// FROM HERE
        DRILL_T1,
        GOLD_INGOT,
        GOLD_SHEET,
        INSULATED_CABLE,
        IRON_INGOT,
        IRON_PLATE,
        IRON_WHEEL,
        NAILS,
        NYLON_FIBER,
        NYLON_ROPE,
        PAPER_PASTE,
        PLANK,
        PLASTIC,
        PLASTIC_SHEET,
        PROCESSOR_T1,
        PUMP_T1,
        REFINED_PETROL_BARREL,
        RIVET,
        ROBOTIC_ARM_T1,
        SAW_T1,
        SCRUM,
        STEEL_INGOT,
        WATER,
        WOOD_BARREL,
        WOOD_CHIPS,
        WOOD_CRATE,
        WOOD_LOG,
        WOOD_STRIP,


    }

    public static double amountMofifier(Type t){
        switch (t){
            case WATER: {
                return 1;
            }
            case WOOD_LOG: {
                return 1;
            }
            case CHARCOAL: {
                return 0.1;
            }
            case CRUDE_OIL: {
                return 0.5;
            }
            case IRON_ORE: {
                return 1.2;
            }
            case COPPER_ORE: {
                return 0.8;
            }
            case GOLD_ORE: {
                return 0.2;
            }
            default:
                return 0;
        }
    }
    public static Type southOccurence(){
        Random r = new Random();
        int randomNumber = r.nextInt(100);

        if(randomNumber < 30){
            return Type.IRON_ORE;       //35%
        }
        if(randomNumber < 60){
            return Type.WOOD_LOG;       //30%
        }
        if(randomNumber < 65) {
            return Type.CHARCOAL;       //5%
        }
        if(randomNumber < 85) {
            return Type.COPPER_ORE;     //20%
        }
        if(randomNumber < 95){
            return Type.CRUDE_OIL;      //10%
        }
        if(randomNumber < 100){
            return Type.WATER;          //5%
        }
        return Type.IRON_ORE;           // ????
    }

    public static Type northOccurence(){
        Random r = new Random();
        int randomNumber = r.nextInt(100);

        if(randomNumber < 30){
            return Type.IRON_ORE;       //30%
        }
        if(randomNumber < 45){
            return Type.WOOD_LOG;       //15%
        }
        if(randomNumber < 50) {
            return Type.CHARCOAL;       //5%
        }
        if(randomNumber < 80) {
            return Type.COPPER_ORE;     //20%
        }
        if(randomNumber < 95){
            return Type.WATER;          //15%
        }
        if(randomNumber < 100){
            return Type.GOLD_ORE;       //5%
        }
        return Type.IRON_ORE;           // ????
    }

    /**
     * Retourne le string proprement formatté correspondant au type d'une ressource
     * @param id l'id d'un type de ressource
     * @return le nom de la ressource tout joli
     */
    public static String RessourceToString(int id){
        return RessourceToString(Type.values()[id]);
    }

    /**
     * Retourne le string proprement formatté correspondant au type d'une ressource
     * @param t un type de ressource (id)
     * @return le nom de la ressource tout joli
     */
    public static String RessourceToString(Ressource.Type t){
        switch (t){
            case SCRUM:
                return "Scrum";
            case WATER:
                return "Water";
            case CRUDE_OIL:
                return "Oil";
            case GOLD_ORE:
                return "Gold Ore";
            case WOOD_LOG:
                return "Wood Log";
            case CHARCOAL:
                return "Charcoal";
            case IRON_ORE:
                return "Iron Ore";
            case COPPER_ORE:
                return "Copper Ore";
            case COPPER_INGOT:
                return "Copper Ingot";
            case IRON_INGOT:
                return "Iron Ingot";
            case IRON_PLATE:
                return "Iron Plate";
            case IRON_WHEEL:
                return "Iron Wheel";
            case COPPER_GEAR:
                return "Copper Gear";
            case COPPER_TUBE:
                return "Copper Tube";
            case DRILL_T1:
                return "Drill T1";
            case PLANK:
                return "Plank";
            case NAILS:
                return "Nail Box";
            case WOOD_CRATE:
                return "Wooden Crate";
            case STEEL_INGOT:
                return "Steel Ingot";
            case RIVET:
                return "Rivet";
            case COPPER_CABLE:
                return "Copper Cable";
            case ROBOTIC_ARM_T1:
                return "Robotic Arm T1";
            case REFINED_PETROL_BARREL:
                return "Petrol Barrel";
            case PLASTIC_SHEET:
                return "Plastic Sheet";
            case GOLD_INGOT:
                return "Gold Ingot";
            case GOLD_SHEET:
                return "Gold Sheet";
            case PROCESSOR_T1:
                return "Processor T1";
            case PLASTIC:
                return "Plastic";
            case SAW_T1:
                return "Saw T1";
            case PUMP_T1:
                return "Pump T1";
            case CARDBOARD:
                return "Cardboard";
            case WOOD_CHIPS:
                return "Woodchips";
            case WOOD_STRIP:
                return "Wood Strips";
            case PAPER_PASTE:
                return "Paper Paste";
            case WOOD_BARREL:
                return "Wooden Barrel";
            case CARDBOARD_BOX:
                return "Cardboard box";
            case INSULATED_CABLE:
                return "Insulated Cable";
            case NYLON_ROPE:
                return "Nylon Rope";
            case NYLON_FIBER:
                return  "Nylon Fiber";
            default:
                return "Not a Resource : " + t.ordinal();
        }
    }

    /**
     *
     * @return the basic resources id of the game
     */
    public static int[] getBaseResourcesId() {
        int baseResources[] = {Type.SCRUM.ordinal(), Type.WATER.ordinal(), Type.WOOD_LOG.ordinal(), Type.CHARCOAL.ordinal(),
                                Type.CRUDE_OIL.ordinal(), Type.IRON_ORE.ordinal(), Type.COPPER_ORE.ordinal(), Type.GOLD_ORE.ordinal()};
        return baseResources;
    }

    public static int[] getPlayerBaseResources(ArrayList<ResourceAmount> playerResources) {
        int baseResources[] = {0, 0, 0, 0, 0, 0, 0, 0};
        //int resourcesFound = 0;
        for(ResourceAmount ra : playerResources) {

            switch(ra.getRessource()){
                case SCRUM:
                    baseResources[0] = ra.getQuantity();
                    break;
                case WATER:
                    baseResources[1] = ra.getQuantity();
                    break;
                case WOOD_LOG:
                    baseResources[2] = ra.getQuantity();
                    break;
                case CHARCOAL:
                    baseResources[3] = ra.getQuantity();
                    break;
                case CRUDE_OIL:
                    baseResources[4] = ra.getQuantity();
                    break;
                case IRON_ORE:
                    baseResources[5] = ra.getQuantity();
                    break;
                case COPPER_ORE:
                    baseResources[6] = ra.getQuantity();
                    break;
                case GOLD_ORE:
                    baseResources[7] = ra.getQuantity();
                    break;
                default:
                    // donothing
            }
            /*   OLD WAY
            if(resourcesFound >= getBaseResourcesId().length) break;// for optimisation
            for(int id : getBaseResourcesId()) {
                if(id == ra.getRessource().ordinal()) {
                    resourcesFound++;
                    baseResources[id] = ra.getQuantity();
                    break;
                }
            }
            */
        }
        return baseResources;
    }

    public static void setPlayerBaseResources(ArrayList<ResourceAmount> playerResources, int[] newAmounts){

    }
}

