package Utils;

import java.util.ArrayList;
import java.util.Random;

public class Ressource {
    /**
     * Enum qui représente les types des ressources du jeu. c'est l'ID unique qui représente une ressource dans la BDD
     */
    //scrum, eau, bois, charbon, petrol, fer, cuivre, acier, or
    public enum  Type {
        SCRUM,
        WATER,
        WOOD_LOG,
        CHARCOAL,
        OIL,
        IRON_ORE,
        COPPER_ORE,
        GOLD_ORE,
        IRON_INGOT,
        COPPER_INGOT,
        IRON_PLATE,
        IRON_WHEEL,
        COPPER_GEAR,
        COPPER_TUBE,// FROM HERE
        PLANK,
        NAILS,
        WOODEN_CRATE,
        STEEL_INGOT,
        RIVET,
        COPPER_CABLE,
        PETROL,
        PLASTIC_SHEET,
        GOLD_INGOT,
        GOLD_SHEET,
        PLASTIC,
        SAW_T1,
        PUMP_T1,
        ROBOTIC_ARM_T1,
        DRILL_T1,
        PROCESSOR_T1

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
                return 0.3;
            }
            case OIL: {
                return 0.2;
            }
            case IRON_ORE: {
                return 1;
            }
            case COPPER_ORE: {
                return 0.7;
            }
            case GOLD_ORE: {
                return 0.1;
            }
            default:
                return 0;
        }
    }
    public static Type southOccurence(){
        Random r = new Random();
        int randomNumber = r.nextInt(100);

        if(randomNumber < 30){
            return Type.IRON_ORE;       //30%
        }
        if(randomNumber < 50){
            return Type.WOOD_LOG;       //20%
        }
        if(randomNumber < 70) {
            return Type.CHARCOAL;       //20%
        }
        if(randomNumber < 90) {
            return Type.COPPER_ORE;     //20%
        }
        if(randomNumber < 95){
            return Type.OIL;            //5%
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
        if(randomNumber < 35){
            return Type.WOOD_LOG;       //5%
        }
        if(randomNumber < 60) {
            return Type.CHARCOAL;       //25%
        }
        if(randomNumber < 85) {
            return Type.COPPER_ORE;     //25%
        }
        if(randomNumber < 95){
            return Type.WATER;          //10%
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
            case OIL:
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
            case WOODEN_CRATE:
                return "Wooden Crate";
            case STEEL_INGOT:
                return "Steel Ingot";
            case RIVET:
                return "Rivet";
            case COPPER_CABLE:
                return "Copper Cable";
            case ROBOTIC_ARM_T1:
                return "Robotic Arm T1";
            case PETROL:
                return "Petrol Barrel";
            case PLASTIC_SHEET:
                return "plastic Sheet";
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
            default:
                return "Not a Resource";
        }
    }

    /**
     *
     * @return the basic resources id of the game
     */
    public static int[] getBaseResourcesId() {
        int baseResources[] = {Type.SCRUM.ordinal(), Type.WATER.ordinal(), Type.WOOD_LOG.ordinal(), Type.CHARCOAL.ordinal(),
                                Type.OIL.ordinal(), Type.IRON_ORE.ordinal(), Type.COPPER_ORE.ordinal(), Type.GOLD_ORE.ordinal()};
        return baseResources;
    }

    public static int[] getPlayerBaseResources(ArrayList<ResourceAmount> playerResources) {
        int baseResources[] = {0, 0, 0, 0, 0, 0, 0, 0};
        int resourcesFound = 0;
        for(ResourceAmount ra : playerResources) {
            if(resourcesFound >= getBaseResourcesId().length) break;// for optimisation
            for(int id : getBaseResourcesId()) {
                if(id == ra.getRessource().ordinal()) {
                    resourcesFound++;
                    baseResources[id] = ra.getQuantity();
                    break;
                }
            }
        }
        return baseResources;
    }

    public static void setPlayerBaseResources(ArrayList<ResourceAmount> playerResources, int[] newAmounts){

    }
}

