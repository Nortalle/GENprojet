package Game;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

import java.util.ArrayList;

public class Train {
    private Loco loco;
    private ArrayList<Wagon> wagons = new ArrayList<Wagon>();
    private TrainStation trainStation;
    private int trainStationETA;// 0 = arrived

    public Train() {

    }

    public Train(Loco l, ArrayList<Wagon> w, TrainStation ts, int eta) {
        loco = l;
        wagons = w;
        trainStation = ts;
        trainStationETA = eta;

    }

    public int getSize() {
        return wagons.size() + 1;
    }

    public TrainStation getTrainStation() {
        return trainStation;
    }

    public int getTrainStationETA() {
        return trainStationETA;
    }

    public String toJSON() {
        Gson gson = new GsonBuilder().create();

        JsonObject train = new JsonObject();
        train.add("loco", new JsonPrimitive("loco"));// TODO
        train.add("wagons", new JsonPrimitive("wagons"));// TODO
        train.add("trainStation", new JsonPrimitive(trainStation.toJSON()));
        train.add("trainStationETA", new JsonPrimitive(trainStationETA));

        return gson.toJson(train);
    }

    public void fromJSON(String from) {
        Gson gson = new GsonBuilder().create();

        JsonObject train = gson.fromJson(from, JsonObject.class);
        //loco = train.get("loco");
        //wagons = train.get("wagons");
        if(trainStation == null) trainStation = new TrainStation();// to change
        trainStation.fromJSON(train.get("trainStation").getAsString());
        trainStationETA = train.get("trainStationETA").getAsInt();
    }
}
