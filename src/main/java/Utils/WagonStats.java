package Utils;

import Game.Mine;
import Game.Train;
import Game.TrainStation;
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
    public static final String LOCO_NAME = "Loco";
    public static final int LOCO_BASE_SPEED = 2;// units of distance / seconds

    // --- DRILL WAGON --- //
    public static final String DRILL_NAME = "Drill wagon";
    public static final int DRILL_BASE_MINING_AMOUNT = 2;
    public static final int DRILL_CAN_MINE[] = {CHARCOAL.ordinal(), IRON_ORE.ordinal(), COPPER_ORE.ordinal(), GOLD_ORE.ordinal()};

    // --- SAW WAGON --- //
    public static final String SAW_NAME = "Saw wagon";
    public static final int SAW_BASE_MINING_AMOUNT = 4;
    public static final int SAW_CAN_MINE[] = {WOOD_LOG.ordinal()};

    // --- PUMP WAGON --- //
    public static final String PUMP_NAME = "Pump wagon";
    public static final int PUMP_CAN_MINE[] = {WATER.ordinal(), CRUDE_OIL.ordinal()};
    public static final int PUMP_BASE_MINING_AMOUNT = 1;

    // --- CARGO WAGON --- //
    public static final String CARGO_NAME = "Cargo wagon";
    public static final int BASE_CARGO_CAPACITY = 500;

    // --- CRAFT WAGON --- //
    public static final String CRAFT_NAME = "Craft wagon";
    public static final int BASE_CRAFT_PARALLEL = 1;


    /**
     * @param t type of wagon
     * @return name of wagon
     */
    public static String getName(WagonType t) {
        switch (t) {
            case LOCO: return LOCO_NAME;
            case DRILL: return DRILL_NAME;
            case SAW: return SAW_NAME;
            case PUMP: return PUMP_NAME;
            case CARGO: return CARGO_NAME;
            case CRAFT: return CRAFT_NAME;
            default: return "unknown";
        }
    }

    /**
     * @param train train
     * @return the train speed (min 1)
     */
    public static int getLocoSpeed(Train train) {
        Wagon loco = null;
        for(Wagon w : train.getWagons()) {
            if(w.getType() == WagonType.LOCO) {
                loco = w;
                break;
            }
        }
        if(loco == null) return 1;// if no loco, not zero because distance/speed

        return getLocoSpeed(loco);
    }

    /**
     * @param loco loco
     * @return the speed of the loco (min 1)
     */
    public static int getLocoSpeed(Wagon loco) {
        if(loco.getType() != WagonType.LOCO) return 1;// if not a loco, not zero because distance/speed
        return linearValuePerLevel(loco.getLevel(), LOCO_BASE_SPEED);
    }

    /**
     * @param ts1 station 1
     * @param ts2 station 2
     * @return the distance between the two station
     */
    public static int calculateTravelDistance(TrainStation ts1, TrainStation ts2) {
        return Math.abs(ts2.getPosX() - ts1.getPosX()) + Math.abs(ts2.getPosY() - ts1.getPosY());
    }

    /**
     * @param loco the loco
     * @param ts1 station 1
     * @param ts2 station 2
     * @return the ETA to get to station 2 from station 1
     */
    public static int calculateTravelETA(Wagon loco, TrainStation ts1, TrainStation ts2) {
        if(loco.getType() != WagonType.LOCO) return 1;// error
        return Math.max(1, calculateTravelDistance(ts1, ts2) / WagonStats.getLocoSpeed(loco));
    }

    /**
     * @param wagon mining wagon
     * @return how much the wagon mine every seconds
     */
    public static int getMiningAmount(Wagon wagon) {
        switch(wagon.getType()) {
            case DRILL:
                return linearValuePerLevel(wagon.getLevel(), DRILL_BASE_MINING_AMOUNT);
            case SAW:
                return linearValuePerLevel(wagon.getLevel(), SAW_BASE_MINING_AMOUNT);
            case PUMP:
                return linearValuePerLevel(wagon.getLevel(), PUMP_BASE_MINING_AMOUNT);
            default:
                return 0;// ERROR
        }
    }

    /**
     * @param wagon mining wagon
     * @return what can be mine by the wagon
     */
    public static int[] getWhatCanBeMine(Wagon wagon) {
        switch(wagon.getType()) {
            case DRILL:
                return DRILL_CAN_MINE;
            case SAW:
                return SAW_CAN_MINE;
            case PUMP:
                return PUMP_CAN_MINE;
            default:
                int[] empty = {};
                return empty;// ERROR
        }
    }

    /**
     * @param wagon mining wagon
     * @param mine mine
     * @return if the wagon can mine the mine
     */
    public static boolean canMine(Wagon wagon, Mine mine) {// need tests
        for(int i : getWhatCanBeMine(wagon)) if(i == mine.getResource()) return true;
        return false;
    }

    /**
     * @param train train
     * @return max cargo capacity of the train
     */
    public static int getMaxCapacity(Train train) {
        if(train == null) return 0;
        int capacity = 0;
        for(Wagon w : train.getWagons()) if(w.getType() == WagonType.CARGO) capacity += linearValuePerLevel(w.getLevel(), BASE_CARGO_CAPACITY);//CARGO_CAPACITY[w.getLevel() - 1];
        return capacity;
    }

    /**
     * @param train train
     * @return max crafts the train can do at the same time
     */
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
                ra.add(new ResourceAmount(Ressource.Type.STEEL_INGOT, costPerLevel(w.getLevel(), 2)));
                ra.add(new ResourceAmount(Ressource.Type.COPPER_GEAR, costPerLevel(w.getLevel(), 12)));
                ra.add(new ResourceAmount(Ressource.Type.DRILL_T1, costPerLevel(w.getLevel(), 1)));
                break;
            case SAW:
                ra.add(new ResourceAmount(Ressource.Type.IRON_PLATE, costPerLevel(w.getLevel(), 8)));
                ra.add(new ResourceAmount(Ressource.Type.IRON_WHEEL, costPerLevel(w.getLevel(), 2)));
                ra.add(new ResourceAmount(Ressource.Type.WOOD_LOG, costPerLevel(w.getLevel(), 3)));
                ra.add(new ResourceAmount(Ressource.Type.COPPER_GEAR, costPerLevel(w.getLevel(), 15)));
                ra.add(new ResourceAmount(Ressource.Type.SAW_T1, costPerLevel(w.getLevel(), 6)));
                break;
            case PUMP:
                ra.add(new ResourceAmount(Ressource.Type.IRON_PLATE, costPerLevel(w.getLevel(), 9)));
                ra.add(new ResourceAmount(Ressource.Type.COPPER_TUBE, costPerLevel(w.getLevel(), 16)));
                ra.add(new ResourceAmount(Ressource.Type.COPPER_GEAR, costPerLevel(w.getLevel(), 4)));
                ra.add(new ResourceAmount(Ressource.Type.PUMP_T1, costPerLevel(w.getLevel(), 2)));
                break;
            case CARGO:
                ra.add(new ResourceAmount(Ressource.Type.IRON_PLATE, costPerLevel(w.getLevel(), 10)));
                ra.add(new ResourceAmount(Ressource.Type.COPPER_TUBE, costPerLevel(w.getLevel(), 10)));
                ra.add(new ResourceAmount(Ressource.Type.WOOD_CRATE, costPerLevel(w.getLevel(), 2)));
                ra.add(new ResourceAmount(Ressource.Type.WOOD_BARREL, costPerLevel(w.getLevel(), 2)));
                break;
            case CRAFT:
                ra.add(new ResourceAmount(Ressource.Type.IRON_PLATE, costPerLevel(w.getLevel(), 18)));
                ra.add(new ResourceAmount(Ressource.Type.COPPER_TUBE, costPerLevel(w.getLevel(), 10)));
                ra.add(new ResourceAmount(Ressource.Type.COPPER_GEAR, costPerLevel(w.getLevel(), 24)));
                ra.add(new ResourceAmount(Ressource.Type.ROBOTIC_ARM_T1, costPerLevel(w.getLevel(), 2)));
                ra.add(new ResourceAmount(Ressource.Type.PROCESSOR_T1, costPerLevel(w.getLevel(), 1)));
                break;
        }

        return ra;
    }

    /**
     * @param level level of wagon
     * @param baseCost initial cost
     * @return actual cost
     */
    private static int costPerLevel(int level, int baseCost){
        return (int)(Math.pow(2, level-1) * baseCost);
    }

    /**
     * @param level level of wagon
     * @param baseValue initial value
     * @return actual value
     */
    private static int linearValuePerLevel(int level, int baseValue) {
        return level * baseValue;
    }

    /**
     * @param level level of wagon
     * @return time it takes to upgrade
     */
    public static int getUpgradeTime(int level) {
        return costPerLevel(level, 5);// TODO
    }
}
