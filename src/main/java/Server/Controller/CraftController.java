package Server.Controller;

import Game.Craft;
import Server.Server;
import Server.DataBase;
import Utils.Recipe;
import Utils.ResourceAmount;
import Utils.WagonStats;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.TimerTask;

public class CraftController {
    private ArrayList<Craft> crafts = new ArrayList<>();

    private final int INTERVAL_MS = 1000;

    public CraftController() {
        new java.util.Timer().scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                DataBase db = Server.getInstance().getDataBase();
                ArrayList<Craft> toRemove = new ArrayList<>();
                HashMap<String, Integer> nbrCrafts = new HashMap<>();
                synchronized (crafts) {
                    for (Craft c : crafts) {
                        if (nbrCrafts.merge(c.getUsername(), 1, (a, b) -> a + b) > WagonStats.getMaxParallelCraft(db.getTrain(c.getUsername())))
                            continue;
                        c.decreaseRemainingTime();
                        if (c.getRemainingTime() <= 0) {
                            // insert in DB
                            ResourceAmount finalProduct = Recipe.getAllRecipes().get(c.getRecipeIndex()).getFinalProduct();
                            int finalAmount = finalProduct.getQuantity();
                            finalAmount = db.canUpdatePlayerObjectsOnReservedCargo(c.getUsername(), finalAmount, finalAmount);
                            db.updatePlayerObjects(c.getUsername(), finalProduct.getRessource().ordinal(), finalAmount);
                            Server.getInstance().getReserveCargoController().removeReservedCargo(c.getUsername(), finalProduct.getQuantity());
                            toRemove.add(c);
                        }
                    }

                    for (Craft c : toRemove) crafts.remove(c);
                }
            }
        }, INTERVAL_MS, INTERVAL_MS);
    }

    public void addCraft(Craft craft) {
        synchronized (crafts) {
            crafts.add(craft);
        }
    }

    public boolean tryCraft(String username, String recipeLine) {
        int recipeIndex = Integer.valueOf(recipeLine);
        // need more tests
        Recipe recipe = Recipe.getAllRecipes().get(recipeIndex);
        for(ResourceAmount ra : recipe.getCost()) {
            ResourceAmount playerObject = Server.getInstance().getDataBase().getPlayerObjectOfType(username, ra.getRessource().ordinal());
            if(playerObject == null) return false;
            if(playerObject.getQuantity() < ra.getQuantity()) return false;
        }
        Server.getInstance().getReserveCargoController().addReservedCargo(username, recipe.getFinalProduct().getQuantity());
        for(ResourceAmount ra : recipe.getCost()) Server.getInstance().getDataBase().updatePlayerObjects(username, ra.getRessource().ordinal(), -ra.getQuantity());

        addCraft(new Craft(username, recipeIndex, Recipe.getAllRecipes().get(recipeIndex).getProductionTime()));
        return true;
    }

    public ArrayList<Craft> getPlayerCrafts(String username) {
        ArrayList<Craft> result = new ArrayList<>();
        synchronized (crafts) {
            for (Craft c : crafts) {
                if (c.getUsername().equals(username)) {
                    result.add(c);
                }
            }
        }
        return result;
    }
}
