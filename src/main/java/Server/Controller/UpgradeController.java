package Server.Controller;

import Game.UpgradeWagon;
import Game.Wagon;
import Server.DataBase;
import Server.Server;
import Utils.ResourceAmount;
import Utils.WagonStats;

import java.util.ArrayList;
import java.util.TimerTask;

public class UpgradeController {

    private ArrayList<UpgradeWagon> upgrades = new ArrayList<>();
    private final int INTERVAL_MS = 1000;
    private DataBase dataBase;

    public UpgradeController() {
        dataBase = Server.getInstance().getDataBase();
        new java.util.Timer().scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {

                ArrayList<UpgradeWagon> toRemove = new ArrayList<>();

                for(UpgradeWagon upgrade : upgrades ){
                    upgrade.decreaseRemainingTime();

                    if(upgrade.getRemainingTime() <= 0){
                        Wagon current = upgrade.getWagon_to_upgrade();

                        current.levelUp();

                        dataBase.updateWagonLevel(current.getId(), current.getLevel());

                        toRemove.add(upgrade);

                    }
                }

                for(UpgradeWagon upgrade : toRemove) upgrades.remove(upgrade);
            }
        }, INTERVAL_MS, INTERVAL_MS);
    }

    public boolean tryUpgrade(String username, String wagonId) {
        Wagon wagonToUpgrade = dataBase.getWagon(Integer.valueOf(wagonId));

        if(wagonToUpgrade.getLevel() > WagonStats.LEVEL_MAX){
            return false;
        }

        ArrayList<ResourceAmount> resourceAmounts = WagonStats.getUpgradeCost(wagonToUpgrade);

        if (resourceAmounts.equals(null)) {
            return false;
        }
        for (ResourceAmount ra : resourceAmounts) {
            ResourceAmount playerObject = dataBase.getPlayerObjectOfType(username, ra.getRessource().ordinal());
            if (playerObject == null) return false;
            if (playerObject.getQuantity() < ra.getQuantity()) return false;
        }

        for (ResourceAmount ra : resourceAmounts) dataBase.updatePlayerObjects(username, ra.getRessource().ordinal(), -ra.getQuantity());

        int time = WagonStats.getUpgradeTime(wagonToUpgrade.getLevel());// TODO
        addUpgrade(new UpgradeWagon(username, wagonToUpgrade, time));

        return true;
    }

    public void addUpgrade(UpgradeWagon upgrade) {
        upgrades.add(upgrade);
    }

    public ArrayList<UpgradeWagon> getPlayerUpgrades(String username) {
        ArrayList<UpgradeWagon> result = new ArrayList<>();
        for(UpgradeWagon uw : upgrades) {
            if(uw.getUsername().equals(username)) {
                result.add(uw);
            }
        }
        return result;
    }
}
