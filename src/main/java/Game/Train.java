package Game;

import java.util.ArrayList;

public class Train {
    private Loco loco;
    private ArrayList<Wagon> wagons = new ArrayList<Wagon>();
    private TrainStation trainStation;
    private long trainStationETA;// 0 = arrived

    public Train() {

    }

    public int getSize() {
        return wagons.size() + 1;
    }
}
