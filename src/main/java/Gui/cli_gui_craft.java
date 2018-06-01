package Gui;

import Client.Client;
import Client.Updater;
import Client.SyncClock;
import Game.Craft;
import Utils.Recipe;
import Utils.ResourceAmount;
import Utils.WagonStats;
import javafx.util.Pair;

import javax.swing.*;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Iterator;

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
    ArrayList<Craft> crafts = new ArrayList<>();
    // END pour l'update local

    public cli_gui_craft() {
        update();
        selectedRecipe = (Recipe) recipeDropdown.getSelectedItem();
        updateCraftCost();

        recipeDropdown.addPopupMenuListener(new PopupMenuListener() {
            @Override
            public void popupMenuWillBecomeVisible(PopupMenuEvent e) {

            }

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

                update();
            }
        });

        // ajout du auto updater
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
        //System.out.println("COUCOU");
        orderQueuePanel.removeAll();
        orderQueuePanel.setLayout(new GridLayout(0, 2));

        int cntAssemblyCount = 0;

        for(Craft c : crafts) {
            if(c.getRemainingTime() > 0){
                if(WagonStats.getMaxParallelCraft(Client.getInstance().getTrainLocal()) > cntAssemblyCount){
                    cntAssemblyCount++;
                    c.decreaseRemainingTime();
                }

                JLabel lab = new JLabel(c.toString());
                JProgressBar bar = new JProgressBar();
                int max = Recipe.getAllRecipes().get(c.getRecipeIndex()).getProductionTime();
                bar.setMaximum(max);
                bar.setValue(max - c.getRemainingTime());

                orderQueuePanel.add(lab);
                orderQueuePanel.add(bar);
            } else {
                // pas sensé l'afficher
            }
        }
        orderQueuePanel.updateUI();
    }

    public synchronized void syncUpdateOrderQueue() {
        crafts = Client.getInstance().getCraftsSync();

        orderQueuePanel.removeAll();
        orderQueuePanel.setLayout(new GridLayout(0, 2));

        // vide la liste des objets ui
        Iterator<Pair<JLabel,JProgressBar>> iter = craftUI.iterator();
        while(iter.hasNext()) {
            iter.next();
            iter.remove();
        }

        for(Craft c : crafts) {
            JLabel lab = new JLabel(c.toString());
            JProgressBar bar = new JProgressBar();
            int max = Recipe.getAllRecipes().get(c.getRecipeIndex()).getProductionTime();
            bar.setMaximum(max);
            bar.setValue(max - c.getRemainingTime());

            orderQueuePanel.add(lab);
            orderQueuePanel.add(bar);
        }
    }

    public synchronized void updateAvailableCrafts(){
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
