package Gui;

import Client.Client;
import Client.Updater;
import Client.SyncClock;
import Game.Craft;
import Utils.Recipe;
import Utils.ResourceAmount;
import javafx.util.Pair;

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

    // pour l'update local
    ArrayList<Pair<JLabel,JProgressBar>> craftUI = new ArrayList<>();

    private Updater u;
    private int nbAssemblers = 0;
    // END pour l'update local

    public cli_gui_craft() {
        update();
        selectedRecipe = (Recipe) recipeDropdown.getSelectedItem();
        updateCraftCost();

        recipeDropdown.addPopupMenuListener(new PopupMenuListener() {
            @Override
            public void popupMenuWillBecomeVisible(PopupMenuEvent e) {
                SyncClock.getInstance().addUpdater(new Updater() {
                    @Override
                    public void sync() {
                        syncUpdateOrderQueue();
                    }

                    @Override
                    public void localUpdate() {
                        localUpdateOrderQueue();
                    }
                });
            }

            @Override
            public void popupMenuWillBecomeInvisible(PopupMenuEvent e) {
                selectedRecipe = (Recipe) recipeDropdown.getSelectedItem();
                updateCraftCost();
                if(u != null){
                    SyncClock.getInstance().removeUpdater(u);
                }
            }

            @Override
            public void popupMenuCanceled(PopupMenuEvent e) {}
        });

        placeOrderButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(selectedRecipe == null) return;
                Client.getInstance().startCraft(selectedRecipe.getRecipeIndex());

                update();
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
        costPanel.removeAll();
        costPanel.setLayout(new GridLayout(0, 1));
        for(ResourceAmount cost : selectedRecipe.getCost()) costPanel.add(new JLabel(cost.toString()));
        costPanel.revalidate();
    }

    public void localUpdateOrderQueue(){

        for(Pair<JLabel,JProgressBar> p : craftUI){
            p.getValue().setValue(p.getValue().getValue() + 1); // incrémente la barre
            if(p.getValue().getValue() >= p.getValue().getMaximum()){

                orderQueuePanel.remove(p.getKey());
                orderQueuePanel.remove(p.getValue());

                craftUI.remove(p);
            }
        }
    }

    public void syncUpdateOrderQueue() {
        ArrayList<Craft> crafts = Client.getInstance().getCrafts();
        orderQueuePanel.removeAll();
        orderQueuePanel.setLayout(new GridLayout(0, 2));

        //TODO updater le nombre d'assembleurs en vigeur

        // vide la liste des objets ui
        for(Object o : craftUI) {
            craftUI.remove(o);
        }

        for(Craft c : crafts) {
            JLabel lab = new JLabel(c.toString());
            JProgressBar bar = new JProgressBar();
            int max = Recipe.getAllRecipes().get(c.getRecipeIndex()).getProductionTime();
            bar.setMaximum(max);
            bar.setValue(max - c.getRemainingTime());

            orderQueuePanel.add(lab);
            orderQueuePanel.add(bar);

            craftUI.add(new Pair(lab, bar));
        }
    }

    public void updateAvailableCrafts(){
        ArrayList<ResourceAmount> playerObjects = Client.getInstance().getAllObjects();

        availableCrafts.removeAll();
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.NORTHWEST;
        gbc.gridx = 1;
        for(Recipe r : Recipe.getAllRecipes()) {
            if(canCraft(r, playerObjects)) {
                availableCrafts.add(new JLabel(r.toString()), gbc);
            }
        }
        gbc.weighty = 1.0;
        gbc.weightx = 1.0;
        availableCrafts.add(new JLabel(""), gbc);
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
        syncUpdateOrderQueue();
    }
}
