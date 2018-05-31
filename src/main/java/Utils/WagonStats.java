package Utils;

import Game.Mine;
import Game.Train;
import Game.Wagon;

import java.util.ArrayList;

import static Utils.Ressource.Type.*;

public class WagonStats {

    public enum WagonType {
        LOCO,
        DRILL,
        SAW,
        PUMP,
        CARGO,
        CRAFT
    }

    // --- LOCO --- //
    //public static final int LOCO_ID = 1;
    public static final String LOCO_NAME = "Loco";
    public static final int LOCO_SPEED[] = {4, 5, 7, 10, 15};// units of distance / seconds

    // --- DRILL WAGON --- //
    //public static final int DRILL_ID = 2;
    public static final String DRILL_NAME = "Drill wagon";
    // --- SAW WAGON --- //
    //public static final int SAW_ID = 3;
    public static final String SAW_NAME = "Saw wagon";
    // --- PUMP WAGON --- //
    //public static final int PUMP_ID = 4;
    public static final String PUMP_NAME = "Pump wagon";
    // -- mining levels -- //
    public static final int MINING_TIME[][] = {{10,9,8,7,6}, {5,4,3,2,1}, {20,19,18,17,16}};// DRILL; SAW; PUMP
    // -- what can mine what -- //
    public static final int CAN_MINE[][] = {{CHARCOAL.ordinal(), IRON_ORE.ordinal(), COPPER_ORE.ordinal(), STEEL.ordinal(), GOLD_ORE.ordinal()},
            {WOOD_LOG.ordinal()},
            {WATER.ordinal(), OIL.ordinal()}};

    // --- CARGO WAGON --- //
    //public static final int CARGO_ID = 5;
    public static final String CARGO_NAME = "Cargo wagon";
    public static final int CARGO_CAPACITY[] = {100, 200, 400, 600, 1000};

    // --- CRAFT WAGON --- //
    //public static final int CRAFT_ID = 6;
    public static final String CRAFT_NAME = "Craft wagon";
    public static final int CRAFT_PARALLEL[] = {1, 2, 3, 4, 5};




    public static String getName(WagonType t) {
        switch (t) {
            case LOCO: return LOCO_NAME;
            case DRILL: return DRILL_NAME;
            case SAW: return SAW_NAME;
            case PUMP: return PUMP_NAME;
            case CARGO: return CARGO_NAME;
            case CRAFT: return CRAFT_NAME;
        }
        return "unknown";
    }

    public static int getMiningTime(int type, int level) {
        return MINING_TIME[type - 2][level - 1];
    }


    public static int getMiningTime(Wagon wagon) {
        return MINING_TIME[wagon.getType().ordinal() - 1][wagon.getLevel() - 1];
    }

    public static boolean canMine(Wagon wagon, Mine mine) {// need tests
        for(int i : CAN_MINE[wagon.getType().ordinal() - 1]) {
            if(i == mine.getResource()) return true;
        }
        return false;
    }

    public static int getMaxCapacity(Train train) {
        int capacity = 0;
        for(Wagon w : train.getWagons()) if(w.getType() == WagonType.CARGO) capacity += CARGO_CAPACITY[w.getLevel() - 1];
        return capacity;
    }

    public static int getMaxParallelCraft(Train train) {
        int parallelCraft = 0;
        for(Wagon w : train.getWagons()) if(w.getType() == WagonType.CRAFT) parallelCraft += CRAFT_PARALLEL[w.getLevel() - 1];
        return parallelCraft;
    }

    /**
     * Retourne le cout en ressource nécessaire pour améliorer le train de 1 niveau.
     * Retourne null si ce n'est pas possible
     * @param w le wagon a améliorer
     * @return
     */
    public static ArrayList<ResourceAmount> getUpgradeCost(Wagon w){

        if(w.getLevel() < 1 || w.getLevel() > 100) {
            return null;
        }

        ArrayList<ResourceAmount> ra = new ArrayList<>();

        switch (w.getType()) {
            case LOCO:
                ra.add(new ResourceAmount(Ressource.Type.IRON_PLATE, costPerLevel(w.getLevel(), 185)));
                ra.add(new ResourceAmount(Ressource.Type.IRON_WHEEL, costPerLevel(w.getLevel(), 24)));
                ra.add(new ResourceAmount(Ressource.Type.COPPER_TUBE, costPerLevel(w.getLevel(), 55)));
                ra.add(new ResourceAmount(Ressource.Type.COPPER_GEAR, costPerLevel(w.getLevel(), 36)));
                ra.add(new ResourceAmount(Ressource.Type.STEEL, costPerLevel(w.getLevel(), 98)));
                break;
            case DRILL:
                ra.add(new ResourceAmount(Ressource.Type.IRON_PLATE, costPerLevel(w.getLevel(), 45)));
                ra.add(new ResourceAmount(Ressource.Type.WOOD_LOG, costPerLevel(w.getLevel(), 20)));
                ra.add(new ResourceAmount(Ressource.Type.IRON_WHEEL, costPerLevel(w.getLevel(), 10)));
                ra.add(new ResourceAmount(Ressource.Type.COPPER_TUBE, costPerLevel(w.getLevel(), 12)));
                ra.add(new ResourceAmount(Ressource.Type.STEEL, costPerLevel(w.getLevel(), 12)));
                ra.add(new ResourceAmount(Ressource.Type.COPPER_GEAR, costPerLevel(w.getLevel(), 22)));
                ra.add(new ResourceAmount(Ressource.Type.DRILL_T1, costPerLevel(w.getLevel(), 4)));
                break;
            case SAW:
                ra.add(new ResourceAmount(Ressource.Type.IRON_PLATE, costPerLevel(w.getLevel(), 35)));
                ra.add(new ResourceAmount(Ressource.Type.WOOD_LOG, costPerLevel(w.getLevel(), 20)));
                ra.add(new ResourceAmount(Ressource.Type.IRON_WHEEL, costPerLevel(w.getLevel(), 8)));
                ra.add(new ResourceAmount(Ressource.Type.COPPER_GEAR, costPerLevel(w.getLevel(), 22)));
                //ra.add(new ResourceAmount(Ressource.Type.SAW_T1, costPerLevel(w.getLevel(), 4)));
                break;
            case PUMP:
                ra.add(new ResourceAmount(Ressource.Type.IRON_PLATE, costPerLevel(w.getLevel(), 45)));
                ra.add(new ResourceAmount(Ressource.Type.COPPER_TUBE, costPerLevel(w.getLevel(), 65)));
                ra.add(new ResourceAmount(Ressource.Type.IRON_WHEEL, costPerLevel(w.getLevel(), 8)));
                ra.add(new ResourceAmount(Ressource.Type.COPPER_GEAR, costPerLevel(w.getLevel(), 4)));
                //ra.add(new ResourceAmount(Ressource.Type.PUMP_T1, costPerLevel(w.getLevel(), 4)));
                break;
            case CARGO:
                ra.add(new ResourceAmount(Ressource.Type.IRON_PLATE, costPerLevel(w.getLevel(), 45)));
                ra.add(new ResourceAmount(Ressource.Type.COPPER_TUBE, costPerLevel(w.getLevel(), 12)));
                ra.add(new ResourceAmount(Ressource.Type.IRON_WHEEL, costPerLevel(w.getLevel(), 16)));
                ra.add(new ResourceAmount(Ressource.Type.COPPER_GEAR, costPerLevel(w.getLevel(), 22)));
                //ra.add(new ResourceAmount(Ressource.Type.CRATE, costPerLevel(w.getLevel(), 4)));
                break;
            case CRAFT:
                ra.add(new ResourceAmount(Ressource.Type.IRON_PLATE, costPerLevel(w.getLevel(), 145)));
                ra.add(new ResourceAmount(Ressource.Type.COPPER_TUBE, costPerLevel(w.getLevel(), 48)));
                ra.add(new ResourceAmount(Ressource.Type.IRON_WHEEL, costPerLevel(w.getLevel(), 12)));
                ra.add(new ResourceAmount(Ressource.Type.COPPER_GEAR, costPerLevel(w.getLevel(), 22)));
                //ra.add(new ResourceAmount(Ressource.Type.ROBOTIC_ARM, costPerLevel(w.getLevel(), 2)));
                //ra.add(new ResourceAmount(Ressource.Type.PROCESSOR, costPerLevel(w.getLevel(), 1)));
                break;
        }

        return ra;
    }

    private static int costPerLevel(int level, int baseCost){
        return    (int)(Math.pow(2, level-1) * baseCost);
    }
}
