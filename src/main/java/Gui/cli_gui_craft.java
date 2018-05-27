package Gui;

import Utils.Recipe;
import Utils.ResourceAmount;

import javax.swing.*;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;
import java.awt.*;

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
        recipeDropdown.addPopupMenuListener(new PopupMenuListener() {
            @Override
            public void popupMenuWillBecomeVisible(PopupMenuEvent e) {}

            @Override
            public void popupMenuWillBecomeInvisible(PopupMenuEvent e) {
                selectedRecipe = (Recipe) recipeDropdown.getSelectedItem();
                update();
            }

            @Override
            public void popupMenuCanceled(PopupMenuEvent e) {}
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
    }

    public void updateAvailableCrafts(){
        availableCrafts.removeAll();
        availableCrafts.setLayout(new GridLayout(0, 1));
        for(Recipe r : Recipe.getAllRecipes()) availableCrafts.add(new JLabel(r.toString()));
    }

    public void update() {
        updateAvailableCrafts();
        updateRecipeDropdown();
        updateCraftCost();
    }
}
