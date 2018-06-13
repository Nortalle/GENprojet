package Game;

import Utils.JsonUtility;
import Utils.WagonStats;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

import java.util.ArrayList;

public class Train {
    private ArrayList<Wagon> wagons = new ArrayList<Wagon>();
    private TrainStation trainStation;
    private int trainStationETA;// 0 = arrived
    private int trainStationTotalETA;

    public Train() {}

    public Train(ArrayList<Wagon> w, TrainStation ts, int eta, int totETA) {
        wagons = w;
        trainStation = ts;
        trainStationETA = eta;
        trainStationTotalETA = totETA;

    }

    public Train(JsonObject json) {
        fromJson(json);
    }

    public Train(String json) {
        fromJson((JsonObject) JsonUtility.fromJson(json));
    }

    public ArrayList<Wagon> getWagons() {
        return wagons;
    }

    public int getSize() {
        return wagons.size();
    }

    public TrainStation getTrainStation() {
        return trainStation;
    }

    public int getTrainStationETA() {
        return trainStationETA;
    }

    public int getTrainStationTotalETA() {
        return trainStationTotalETA;
    }

    public Train decreaseTrainStationETA(int i) {
        trainStationETA -= i;
        if(trainStationETA < 0){
            trainStationETA = 0;
        }
        return this;
    }

    public JsonObject toJson() {
        JsonObject train = new JsonObject();
        train.add("w", JsonUtility.listToJson(wagons, Wagon::toJson));
        train.add("s", trainStation.toJson());
        train.add("e", new JsonPrimitive(trainStationETA));
        train.add("t", new JsonPrimitive(trainStationTotalETA));

        return train;
    }

    public void fromJson(JsonObject from) {
        wagons = JsonUtility.listFromJson((JsonArray) from.get("w"), Wagon::new);
        //TODO change
        if(trainStation == null) trainStation = new TrainStation();// to change
        trainStation.fromJson((JsonObject) from.get("s"));
        trainStationETA = from.get("e").getAsInt();
        trainStationTotalETA = from.get("t").getAsInt();
    }

    @Override
    public String toString() {
        String result = "I'm Tom the train";
        //for(Wagon w : wagons) result += WagonStats.getName(w.getType()) + " " + w.getLevel() + " | ";
        return result;
    }
}
