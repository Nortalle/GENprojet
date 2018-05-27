package Gui;

import Client.Client;
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
    private JTextField quantity_text_field;
    private JComboBox recipeDropdown;
    private JPanel costPanel;
    private JPanel availableCrafts;
    private JButton placeOrderButton;
    private JPanel OrderQueuePanel;

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
                Client.getInstance().startCraft(selectedRecipe.getRecipeIndex());
            }
        });
    }

    public void updateRecipeDropdown(){
        recipeDropdown.removeAllItems();
        for(Recipe r : Recipe.getAllRecipes()) recipeDropdown.addItem(r);
    }

    public void updateCraftCost(){
        if(selectedRecipe == null) return;
        costPanel.removeAll();
        costPanel.setLayout(new GridLayout(0, 1));
        for(ResourceAmount cost : selectedRecipe.getCost()) costPanel.add(new JLabel(cost.toString()));
        costPanel.revalidate();
    }

    public void updateAvailableCrafts(){
        availableCrafts.removeAll();
        availableCrafts.setLayout(new GridLayout(0, 1));
        for(Recipe r : Recipe.getAllRecipes()) availableCrafts.add(new JLabel(r.toString()));
        //
        ArrayList<ResourceAmount> playerObjects = Client.getInstance().getAllObjects();
        for(ResourceAmount ra : playerObjects) System.out.println(ra);

        availableCrafts.removeAll();
        availableCrafts.setLayout(new GridLayout(0, 1));
        for(Recipe r : Recipe.getAllRecipes()) {
            if(canCraft(r, playerObjects)) {
                availableCrafts.add(new JLabel(r.toString()));
            }
        }
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
    }
}
