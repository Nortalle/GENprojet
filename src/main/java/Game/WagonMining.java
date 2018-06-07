package Game;

import Utils.JsonUtility;
import com.google.gson.*;

import java.util.ArrayList;

public class WagonMining  {
    private Wagon wagon;
    private Mine currentMine;

    public WagonMining() {}

    public WagonMining(JsonObject json) {
        fromJson(json);
    }

    public WagonMining(String json) {
        fromJson((JsonObject) JsonUtility.fromJson(json));
    }

    public WagonMining(Wagon wagon, Mine currentMine) {
        this.wagon = wagon;
        this.currentMine = currentMine;
    }

    public Mine getCurrentMine() {
        return currentMine;
    }

    public void setCurrentMine(Mine currentMine) {
        this.currentMine = currentMine;
    }

    public boolean isMining() {
        return currentMine != null;
    }

    public JsonObject toJson() {
        JsonObject miningWagon = new JsonObject();
        if(wagon == null) wagon = new Wagon();
        miningWagon.add("wagon", wagon.toJson());
        if(currentMine == null) currentMine = new Mine();
        miningWagon.add("currentMine", currentMine.toJson());

        return miningWagon;
    }

    public void fromJson(JsonObject from) {
        if(wagon == null) wagon = new Wagon();// can we do better ?
        wagon.fromJson((JsonObject) from.get("wagon"));
        if(currentMine == null) currentMine = new Mine();// can we do better ?
        currentMine.fromJson((JsonObject) from.get("currentMine"));
    }

    public Wagon getWagon() {
        return wagon;
    }

    /**
     * remplaces les référence courante du wagon par celle du train passé en paramètre
     * @param train
     * @return
     */
    public boolean linkWagonToTrain(Train train) {
        boolean hasBeenUpdate = false;
        // maj des références sur les wagons
        for(Wagon w : train.getWagons()) {
            if(wagon.getId() == w.getId()) {
                wagon = w;
                return true;
            }
        }
        return false;
    }

    /**
     * remplace la référence courante sur la mine par celles du train passé en paramètre
     * @param train
     * @return
     */
    public boolean linkMineToTrain(Train train){

        for(Mine m : train.getTrainStation().getMines()){
            if(currentMine.getId() == m.getId()){
                currentMine = m;
                return true;
            }
        }
        return false;
    }
}
