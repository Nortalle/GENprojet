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
import java.awt.*;
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

    private Recipe selectedRecipe;

    public cli_gui_craft() {
        update();
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
                Client.getInstance().updateCrafts();// MANUAL UPDATE
                if(line.equals(OTrainProtocol.SUCCESS)) update();
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
        orderQueuePanel.removeAll();
        orderQueuePanel.setLayout(new GridLayout(0, 2));
        for(Craft c : crafts) {
            orderQueuePanel.add(new JLabel(c.toString()));
            JProgressBar bar = new JProgressBar();
            int max = Recipe.getAllRecipes().get(c.getRecipeIndex()).getProductionTime();
            bar.setMaximum(max);
            bar.setValue(max - c.getRemainingTime());
            orderQueuePanel.add(bar);
        }
    }

    public void updateAvailableCrafts(){
        ArrayList<ResourceAmount> playerObjects = Client.getInstance().getResourceAmounts();
        ArrayList<Recipe> availableRecipes = new ArrayList<>();
        for(Recipe r : Recipe.getAllRecipes()) {
            if(canCraft(r, playerObjects)) {
                availableRecipes.add(r);
            }
        }

        GuiUtility.listInPanel(availableCrafts, availableRecipes, recipe -> new JLabel(recipe.toString()));
    }

    public boolean canCraft(Recipe recipe, ArrayList<ResourceAmount> resourceAmounts) {
        for(ResourceAmount cost : recipe.getCost()) {
            if(!hasEnough(cost, resourceAmounts)) return false;
        }
        return true;
    }

    public boolean hasEnough(ResourceAmount cost, ArrayList<ResourceAmount> resourceAmounts) {
        for(ResourceAmount ra : resourceAmounts) {
            if(ra.getRessource() == cost.getRessource()) {
                return ra.getQuantity() >= cost.getQuantity();
            }
        }
        return false;
    }

    public void update() {
        updateAvailableCrafts();
        updateRecipeDropdown();
        updateCraftCost();
        updateOrderQueue();
    }
}
