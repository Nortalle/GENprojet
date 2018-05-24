package Utils;

public class Ressource {
    /**
     * Enum qui représente les types des ressources du jeu. c'est l'ID unique qui représente une ressource dans la BDD
     */
    public enum  Type {
        WOOD_LOG,
        CHARCOAL,
        IRON_ORE,
        COPPER_ORE,
        IRON_INGOT,
        COPPER_INGOT,
        IRON_PLATE,
        IRON_WHEEL,
        COPPER_GEAR,
        COPPER_TUBE,
        DRILL_T1
    }

    /**
     * Retourne le string proprement formatté correspondant au type d'une ressource
     * @param t un type de ressource (id)
     * @return le nom de la ressource tout joli
     */
    public static String RessourceToString(Ressource.Type t){
        switch (t){
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
            default:
                return "Not a Ressource";
        }
    }
}

