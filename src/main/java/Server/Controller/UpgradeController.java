package Server.Controller;

import Game.UpgradeWagon;
import Game.Wagon;
import Server.DataBase;
import Server.Server;
import Utils.ReadWriteLock;
import Utils.ResourceAmount;
import Utils.WagonStats;

import java.util.ArrayList;
import java.util.Optional;
import java.util.TimerTask;

public class UpgradeController {

    private ArrayList<UpgradeWagon> upgrades = new ArrayList<>();
    private final int INTERVAL_MS = 1000;
    private ReadWriteLock lock = new ReadWriteLock();

    private DataBase dataBase;

    public UpgradeController() {
        dataBase = Server.getInstance().getDataBase();
        new java.util.Timer().scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {

                ArrayList<UpgradeWagon> toRemove = new ArrayList<>();
                try {
                    lock.lockRead();
                    for (UpgradeWagon upgrade : upgrades) {
                        upgrade.decreaseRemainingTime();
                        if (upgrade.getRemainingTime() <= 0) {
                            Wagon current = upgrade.getWagon_to_upgrade();
                            current.levelUp();
                            dataBase.updateWagonLevel(current.getId(), current.getLevel());
                            toRemove.add(upgrade);
                        }
                    }
                    lock.unlockRead();
                    lock.lockWrite();
                    for (UpgradeWagon upgrade : toRemove) upgrades.remove(upgrade);
                    lock.unlockWrite();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }, INTERVAL_MS, INTERVAL_MS);
    }

    public boolean tryUpgrade(String username, String wagonId) {
        Optional<Wagon> optionalWagonToUpgrade = dataBase.getWagon(Integer.valueOf(wagonId));
        if(!optionalWagonToUpgrade.isPresent()) return false;
        Wagon wagonToUpgrade = optionalWagonToUpgrade.get();
        if(wagonToUpgrade.getLevel() > WagonStats.LEVEL_MAX) return false;
        ArrayList<ResourceAmount> resourceAmounts = WagonStats.getUpgradeCost(wagonToUpgrade);
        if (resourceAmounts.equals(null)) return false;
        for (ResourceAmount ra : resourceAmounts) {
            Optional<ResourceAmount> playerObject = dataBase.getPlayerObjectOfType(username, ra.getRessource().ordinal());
            if(!playerObject.filter(po -> po.getQuantity() >= ra.getQuantity()).isPresent()) return false;
            //if(playerObject == null) return false;
            //if(playerObject.getQuantity() < ra.getQuantity()) return false;
        }

        for (ResourceAmount ra : resourceAmounts) dataBase.updatePlayerObjects(username, ra.getRessource().ordinal(), -ra.getQuantity());

        int time = WagonStats.getUpgradeTime(wagonToUpgrade.getLevel());// TODO
        addUpgrade(new UpgradeWagon(username, wagonToUpgrade, time));

        return true;
    }

    public void addUpgrade(UpgradeWagon upgrade) {
        try {
            lock.lockWrite();
            upgrades.add(upgrade);
            lock.unlockWrite();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public ArrayList<UpgradeWagon> getPlayerUpgrades(String username) {
        ArrayList<UpgradeWagon> result = new ArrayList<>();
        try {
            lock.lockRead();
            for (UpgradeWagon uw : upgrades) {
                if (uw.getUsername().equals(username)) {
                    result.add(uw);
                }
            }
            lock.unlockRead();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return result;
    }
}
