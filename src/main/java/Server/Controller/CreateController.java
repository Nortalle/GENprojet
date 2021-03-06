package Server.Controller;

import Game.CreateWagon;
import Game.Wagon;
import Server.Server;
import Server.DataBase;
import Utils.ReadWriteLock;
import Utils.ResourceAmount;
import Utils.WagonRecipe;

import java.util.ArrayList;
import java.util.Optional;
import java.util.TimerTask;

public class CreateController {
    private ArrayList<CreateWagon> createWagons = new ArrayList<>();
    private final int INTERVAL_MS = 1000;
    private ReadWriteLock lock = new ReadWriteLock();

    public CreateController() {
        new java.util.Timer().scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                DataBase db = Server.getInstance().getDataBase();
                ArrayList<CreateWagon> toRemove = new ArrayList<>();
                try {
                    lock.lockRead();
                    for (CreateWagon cw : createWagons) {
                        cw.decreaseRemainingTime();
                        if (cw.getRemainingTime() <= 0) {
                            // insert in DB
                            Wagon finalProduct = WagonRecipe.getAllRecipes().get(cw.getWagonRecipeIndex()).getFinalProduct();
                            db.addWagon(cw.getUsername(), finalProduct.getWeight(), finalProduct.getLevel(), finalProduct.getType().ordinal());
                            toRemove.add(cw);
                        }
                    }
                    lock.unlockRead();
                    lock.lockWrite();
                    for (CreateWagon c : toRemove) createWagons.remove(c);
                    lock.unlockWrite();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }, INTERVAL_MS, INTERVAL_MS);
    }

    public void addCraft(CreateWagon createWagon) {
        try {
            lock.lockWrite();
            createWagons.add(createWagon);
            lock.unlockWrite();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public boolean tryCreateWagon(String username, String wagonRecipeLine) {
        int wagonRecipeIndex = Integer.valueOf(wagonRecipeLine);
        // need more tests
        WagonRecipe wagonRecipe = WagonRecipe.getAllRecipes().get(wagonRecipeIndex);
        for(ResourceAmount ra : wagonRecipe.getCost()) {
            Optional<ResourceAmount> playerObject = Server.getInstance().getDataBase().getPlayerObjectOfType(username, ra.getRessource().ordinal());
            if(!playerObject.filter(po -> po.getQuantity() >= ra.getQuantity()).isPresent()) return false;
            //if(playerObject == null) return false;
            //if(playerObject.getQuantity() < ra.getQuantity()) return false;
        }

        for(ResourceAmount ra : wagonRecipe.getCost()) Server.getInstance().getDataBase().updatePlayerObjects(username, ra.getRessource().ordinal(), -ra.getQuantity());
        addCraft(new CreateWagon(username, wagonRecipeIndex, WagonRecipe.getAllRecipes().get(wagonRecipeIndex).getProductionTime()));
        return true;
    }

    public ArrayList<CreateWagon> getPlayerCreateWagons(String username) {
        ArrayList<CreateWagon> result = new ArrayList<>();
        try {
            lock.lockRead();
            for (CreateWagon c : createWagons) {
                if (c.getUsername().equals(username)) {
                    result.add(c);
                }
            }
            lock.unlockRead();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return result;
    }
}
