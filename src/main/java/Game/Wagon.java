package Game;

import Utils.WagonStats;

public class Wagon {
    int id;
    int weight;
    int level;
    int typeID;

    public Wagon() {}

    public Wagon(int id , int weight, int level, int typeID) {
        this.id = id;
        this.weight = weight;
        this.level = level;
        this.typeID = typeID;
    }

    @Override
    public String toString() {
        return WagonStats.getName(typeID) + "-" + id;
    }
}
