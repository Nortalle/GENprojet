package Utils;

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
     * @return the basic resources of the game
     */
    public static int[] getBaseResources() {
        int baseResources[] = {Type.SCRUM.ordinal(), Type.WATER.ordinal(), Type.WOOD_LOG.ordinal(), Type.CHARCOAL.ordinal(),
                                Type.OIL.ordinal(), Type.IRON_ORE.ordinal(), Type.COPPER_ORE.ordinal(), Type.GOLD_ORE.ordinal()};
        return baseResources;
    }
}

