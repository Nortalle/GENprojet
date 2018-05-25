package Game;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

import java.util.ArrayList;

public class Train {
    private ArrayList<Wagon> wagons = new ArrayList<Wagon>();
    private TrainStation trainStation;
    private int trainStationETA;// 0 = arrived

    public Train() {

    }

    public Train(ArrayList<Wagon> w, TrainStation ts, int eta) {
        wagons = w;
        trainStation = ts;
        trainStationETA = eta;

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

    public JsonObject toJson() {
        JsonObject train = new JsonObject();
        train.add("wagons", Wagon.listToJson(wagons));
        train.add("trainStation", trainStation.toJson());
        train.add("trainStationETA", new JsonPrimitive(trainStationETA));

        return train;
    }

    public void fromJson(JsonObject from) {
        wagons = Wagon.listFromJson((JsonArray) from.get("wagons"));
        if(trainStation == null) trainStation = new TrainStation();// to change
        trainStation.fromJson((JsonObject) from.get("trainStation"));
        trainStationETA = from.get("trainStationETA").getAsInt();
    }
}
