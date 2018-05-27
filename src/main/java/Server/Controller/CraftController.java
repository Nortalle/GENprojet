package Server.Controller;

import Game.Craft;
import Server.Server;
import Utils.Recipe;
import Utils.ResourceAmount;

import java.util.ArrayList;
import java.util.TimerTask;

public class CraftController {
    private ArrayList<Craft> crafts = new ArrayList<>();

    private final int INTERVAL_MS = 1000;

    public CraftController() {
        new java.util.Timer().scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                ArrayList<Craft> toRemove = new ArrayList<>();
                for(Craft c : crafts) {
                    c.decreceRemainingTime();
                    if(c.getRemainingTime() <= 0) {
                        // insert in DB
                        ResourceAmount finalProduct = Recipe.getAllRecipes().get(c.getRecipeIndex()).getFinalProduct();
                        Server.getInstance().getDataBase().updatePlayerObjects(c.getUsername(), finalProduct.getRessource().ordinal(), finalProduct.getQuantity());
                        toRemove.add(c);
                    }
                }

                for(Craft c : toRemove) crafts.remove(c);
            }
        }, INTERVAL_MS, INTERVAL_MS);
    }

    public void addCraft(Craft craft) {
        crafts.add(craft);
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

        for(ResourceAmount ra : recipe.getCost()) Server.getInstance().getDataBase().updatePlayerObjects(username, ra.getRessource().ordinal(), -ra.getQuantity());
        addCraft(new Craft(username, recipeIndex, Recipe.getAllRecipes().get(recipeIndex).getProductionTime()));
        return true;
    }

    public ArrayList<Craft> getPlayerCrafts(String username) {
        ArrayList<Craft> result = new ArrayList<>();
        for(Craft c : crafts) {
            if(c.getUsername().equals(username)) {
                result.add(c);
            }
        }
        return result;
    }
}
