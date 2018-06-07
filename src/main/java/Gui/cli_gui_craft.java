package Gui;

import Client.Client;
import Game.Craft;
import Utils.GuiUtility;
import Utils.OTrainProtocol;
import Utils.Recipe;
import Utils.ResourceAmount;

import javax.swing.*;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class cli_gui_craft {
    private JPanel panel1;
    private JComboBox recipeDropdown;
    private JPanel costPanel;
    private JPanel availableCrafts;
    private JButton placeOrderButton;
    private JPanel orderQueuePanel;
    private JPanel orderQueueNamePanel;
    private JPanel orderQueueBarPanel;
    private Recipe selectedRecipe;



    public cli_gui_craft() {
        localUpdate();
        selectedRecipe = (Recipe) recipeDropdown.getSelectedItem();
        updateCraftCost();

        recipeDropdown.addPopupMenuListener(new PopupMenuListener() {
            @Override
            public void popupMenuWillBecomeVisible(PopupMenuEvent e) {}

            @Override
            public void popupMenuWillBecomeInvisible(PopupMenuEvent e) {
                selectedRecipe = (Recipe) recipeDropdown.getSelectedItem();
                updateCraftCost();
            }

            @Override
            public void popupMenuCanceled(PopupMenuEvent e) {}
        });

        placeOrderButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(selectedRecipe == null) return;
                String line = Client.getInstance().startCraft(selectedRecipe.getRecipeIndex());
                Client.getInstance().updateAll();
                if(line.equals(OTrainProtocol.SUCCESS)) localUpdate();
            }
        });
    }

    public void updateRecipeDropdown(){
        recipeDropdown.removeAllItems();
        for(Recipe r : Recipe.getAllRecipes()) recipeDropdown.addItem(r);
        if(selectedRecipe != null) recipeDropdown.setSelectedItem(selectedRecipe);
    }

    public void updateCraftCost(){
        if(selectedRecipe == null) return;
        GuiUtility.displayCost(costPanel, selectedRecipe.getCost());
    }

    public void updateOrderQueue() {
        ArrayList<Craft> crafts = Client.getInstance().getCrafts();
        //GuiUtility.listInPanel(orderQueueNamePanel, crafts, craft -> new JLabel(craft.toString()));
        //GuiUtility.listInPanel(orderQueueBarPanel, crafts, craft -> GuiUtility.getProgressBar(craft, Craft::getRemainingTime, c -> Recipe.getAllRecipes().get(c.getRecipeIndex()).getProductionTime()));
        GuiUtility.listProgressBar(orderQueueBarPanel, crafts, Craft::getRemainingTime, c -> Recipe.getAllRecipes().get(c.getRecipeIndex()).getProductionTime());
    }

    public void updateAvailableCrafts(){
        ArrayList<Recipe> availableRecipes = new ArrayList<>();
        for(Recipe r : Recipe.getAllRecipes()) {
            if(canCraft(r)) {
                availableRecipes.add(r);
            }
        }

        GuiUtility.listInPanel(availableCrafts, availableRecipes, recipe -> new JLabel(recipe.toString()));
    }

    public boolean canCraft(Recipe recipe) {
        for(ResourceAmount cost : recipe.getCost()) {
            if(Client.getInstance().getSpecificResource(cost.getRessource()) < cost.getQuantity()) return false;
        }
        return true;
    }

    public void localUpdate() {
        updateAvailableCrafts();
        updateRecipeDropdown();
        updateCraftCost();
        updateOrderQueue();
    }

    public void frequentLocalUpdate() {
        updateAvailableCrafts();
        updateCraftCost();
        updateOrderQueue();
    }
}
