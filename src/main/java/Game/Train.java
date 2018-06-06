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

    public JsonObject toJson() {
        JsonObject train = new JsonObject();
        train.add("wagons", JsonUtility.listToJson(wagons, Wagon::toJson));
        train.add("trainStation", trainStation.toJson());
        train.add("trainStationETA", new JsonPrimitive(trainStationETA));
        train.add("trainStationTotalETA", new JsonPrimitive(trainStationTotalETA));

        return train;
    }

    public void fromJson(JsonObject from) {
        wagons = JsonUtility.listFromJson((JsonArray) from.get("wagons"), Wagon::new);
        if(trainStation == null) trainStation = new TrainStation();// to change
        trainStation.fromJson((JsonObject) from.get("trainStation"));
        trainStationETA = from.get("trainStationETA").getAsInt();
        trainStationTotalETA = from.get("trainStationTotalETA").getAsInt();
    }

    @Override
    public String toString() {
        String result = "I'm Tom the train";
        //for(Wagon w : wagons) result += WagonStats.getName(w.getType()) + " " + w.getLevel() + " | ";
        return result;
    }
}
