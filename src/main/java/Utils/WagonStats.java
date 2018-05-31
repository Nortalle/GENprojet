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

    public static final int LEVEL_MAX = 20;// min is 1

    // --- LOCO --- //
    //public static final int LOCO_ID = 1;
    public static final String LOCO_NAME = "Loco";
    public static final int LOCO_BASE_SPEED = 2;// units of distance / seconds
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
    // resources per seconds // TODO
    public static final int DRILL_BASE_MINING_TIME = 10;// DRILL
    public static final int SAW_BASE_MINING_TIME = 5;// SAW
    public static final int PUMP_BASE_MINING_TIME = 20;// PUMP
    public static final int MINING_TIME[][] = {{10,9,8,7,6}, {5,4,3,2,1}, {20,19,18,17,16}};// DRILL; SAW; PUMP
    // -- what can mine what -- //
    public static final int CAN_MINE[][] = {{CHARCOAL.ordinal(), IRON_ORE.ordinal(), COPPER_ORE.ordinal(), STEEL_INGOT.ordinal(), GOLD_ORE.ordinal()},
            {WOOD_LOG.ordinal()},
            {WATER.ordinal(), OIL.ordinal()}};

    // --- CARGO WAGON --- //
    //public static final int CARGO_ID = 5;
    public static final String CARGO_NAME = "Cargo wagon";
    public static final int BASE_CARGO_CAPACITY = 1000;
    public static final int CARGO_CAPACITY[] = {100, 200, 400, 600, 1000};

    // --- CRAFT WAGON --- //
    //public static final int CRAFT_ID = 6;
    public static final String CRAFT_NAME = "Craft wagon";
    public static final int BASE_CRAFT_PARALLEL = 1;
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

    public static int getLocoSpeed(Wagon loco) {
        if(loco.getType() != WagonType.LOCO) return 1;// if not a loco, not zero because distance/speed
        return linearValuePerLevel(loco.getLevel(), LOCO_BASE_SPEED);
    }

    public static int getMiningTime(Wagon wagon) {
        switch(wagon.getType()) {
            case DRILL:
                return linearReductionValuePerLevel(wagon.getLevel() / 2, DRILL_BASE_MINING_TIME);
            case SAW:
                return linearReductionValuePerLevel(wagon.getLevel() / 4, SAW_BASE_MINING_TIME);
            case PUMP:
                return linearReductionValuePerLevel(wagon.getLevel(), PUMP_BASE_MINING_TIME);
            default:
                return 1;// ERROR
        }

        //return MINING_TIME[wagon.getType().ordinal() - 1][wagon.getLevel() - 1];
    }

    public static boolean canMine(Wagon wagon, Mine mine) {// need tests
        for(int i : CAN_MINE[wagon.getType().ordinal() - 1]) {
            if(i == mine.getResource()) return true;
        }
        return false;
    }

    public static int getMaxCapacity(Train train) {
        int capacity = 0;
        for(Wagon w : train.getWagons()) if(w.getType() == WagonType.CARGO) capacity += linearValuePerLevel(w.getLevel(), BASE_CARGO_CAPACITY);//CARGO_CAPACITY[w.getLevel() - 1];
        return capacity;
    }

    public static int getMaxParallelCraft(Train train) {
        int parallelCraft = 0;
        for(Wagon w : train.getWagons()) if(w.getType() == WagonType.CRAFT) parallelCraft += linearValuePerLevel(w.getLevel(), BASE_CRAFT_PARALLEL);//CRAFT_PARALLEL[w.getLevel() - 1];
        return parallelCraft;
    }

    /**
     * Retourne le cout en ressource nécessaire pour améliorer le train de 1 niveau.
     * Retourne null si ce n'est pas possible
     * @param w le wagon a améliorer
     * @return
     */
    public static ArrayList<ResourceAmount> getUpgradeCost(Wagon w){

        if(w.getLevel() < 1 || w.getLevel() > LEVEL_MAX) {
            return null;
        }

        ArrayList<ResourceAmount> ra = new ArrayList<>();

        switch (w.getType()) {
            case LOCO:
                ra.add(new ResourceAmount(Ressource.Type.IRON_PLATE, costPerLevel(w.getLevel(), 10)));
                ra.add(new ResourceAmount(Ressource.Type.IRON_WHEEL, costPerLevel(w.getLevel(), 2)));
                ra.add(new ResourceAmount(Ressource.Type.COPPER_TUBE, costPerLevel(w.getLevel(), 5)));
                ra.add(new ResourceAmount(Ressource.Type.COPPER_GEAR, costPerLevel(w.getLevel(), 3)));
                ra.add(new ResourceAmount(Ressource.Type.STEEL_INGOT, costPerLevel(w.getLevel(), 7)));
                break;
            case DRILL:
                ra.add(new ResourceAmount(Ressource.Type.IRON_PLATE, costPerLevel(w.getLevel(), 10)));
                ra.add(new ResourceAmount(Ressource.Type.WOOD_LOG, costPerLevel(w.getLevel(), 3)));
                ra.add(new ResourceAmount(Ressource.Type.COPPER_TUBE, costPerLevel(w.getLevel(), 6)));
                ra.add(new ResourceAmount(Ressource.Type.STEEL_INGOT, costPerLevel(w.getLevel(), 5)));
                ra.add(new ResourceAmount(Ressource.Type.COPPER_GEAR, costPerLevel(w.getLevel(), 12)));
                ra.add(new ResourceAmount(Ressource.Type.DRILL_T1, costPerLevel(w.getLevel(), 1)));
                break;
            case SAW:
                ra.add(new ResourceAmount(Ressource.Type.IRON_PLATE, costPerLevel(w.getLevel(), 8)));
                ra.add(new ResourceAmount(Ressource.Type.WOOD_LOG, costPerLevel(w.getLevel(), 3)));
                ra.add(new ResourceAmount(Ressource.Type.COPPER_GEAR, costPerLevel(w.getLevel(), 15)));
                //ra.add(new ResourceAmount(Ressource.Type.SAW_T1_RCPT, costPerLevel(w.getLevel(), 4)));
                break;
            case PUMP:
                ra.add(new ResourceAmount(Ressource.Type.IRON_PLATE, costPerLevel(w.getLevel(), 9)));
                ra.add(new ResourceAmount(Ressource.Type.COPPER_TUBE, costPerLevel(w.getLevel(), 16)));
                ra.add(new ResourceAmount(Ressource.Type.COPPER_GEAR, costPerLevel(w.getLevel(), 4)));
                //ra.add(new ResourceAmount(Ressource.Type.PUMP_T1_RCPT, costPerLevel(w.getLevel(), 4)));
                break;
            case CARGO:
                ra.add(new ResourceAmount(Ressource.Type.IRON_PLATE, costPerLevel(w.getLevel(), 20)));
                ra.add(new ResourceAmount(Ressource.Type.COPPER_TUBE, costPerLevel(w.getLevel(), 2)));
                //ra.add(new ResourceAmount(Ressource.Type.CRATE, costPerLevel(w.getLevel(), 4)));
                break;
            case CRAFT:
                ra.add(new ResourceAmount(Ressource.Type.IRON_PLATE, costPerLevel(w.getLevel(), 18)));
                ra.add(new ResourceAmount(Ressource.Type.COPPER_TUBE, costPerLevel(w.getLevel(), 10)));
                ra.add(new ResourceAmount(Ressource.Type.COPPER_GEAR, costPerLevel(w.getLevel(), 24)));
                //ra.add(new ResourceAmount(Ressource.Type.ROBOTIC_ARM, costPerLevel(w.getLevel(), 2)));
                //ra.add(new ResourceAmount(Ressource.Type.PROCESSOR, costPerLevel(w.getLevel(), 1)));
                break;
        }

        return ra;
    }

    private static int costPerLevel(int level, int baseCost){
        return (int)(Math.pow(2, level-1) * baseCost);
    }

    private static int linearValuePerLevel(int level, int baseValue) {
        return level * baseValue;
    }

    private static int linearReductionValuePerLevel(int level, int baseValue) {
        return Math.max(baseValue - level, 1);
    }

    public static int getUpgradeTime(int level) {
        return costPerLevel(level, 5);// TODO
    }
}
