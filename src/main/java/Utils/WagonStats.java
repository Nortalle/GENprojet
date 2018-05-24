package Utils;

import Game.Wagon;

public class WagonStats {

    // --- LOCO --- //
    public static final int LOCO_ID = 1;
    public static final String LOCO_NAME = "Loco";

    // -- mining levels -- //
    public static final int MINING_TIME[][] = {{10,9,8,7,6}, {5,4,3,2,1}, {20,19,18,17,16}};//DRILL; SAW; PUMP

    // --- DRILL WAGON --- //
    public static final int DRILL_ID = 2;
    public static final String DRILL_NAME = "Drill wagon";
    public static final String[] DRILL_CAN_MINE = {"Charbon", "Fer", "Cuivre", "Acier", "Or"};// change to ID ?
    // --- SAW WAGON --- //
    public static final int SAW_ID = 3;
    public static final String SAW_NAME = "Saw wagon";
    public static final String[] SAW_CAN_MINE = {"Wood"};// change to ID ?

    // --- PUMP WAGON --- //
    public static final int PUMP_ID = 4;
    public static final String PUMP_NAME = "Pump wagon";
    public static final String[] PUMP_CAN_MINE = {"Eau", "Pertrol"};// change to ID ?

    public static String getName(int id) {
        switch (id) {
            case LOCO_ID: return LOCO_NAME;
            case DRILL_ID: return DRILL_NAME;
            case SAW_ID: return SAW_NAME;
            case PUMP_ID: return PUMP_NAME;
        }
        return "unknown";
    }

    public static int getMiningTime(int type, int level) {
        return MINING_TIME[type - 2][level - 1];
    }

    public static int getMiningTime(Wagon wagon) {
        return MINING_TIME[wagon.getTypeID() - 2][wagon.getLevel() - 1];
    }
}
