package Gui;

import Client.Client;
import Game.*;
import Utils.*;

import javax.swing.*;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class CliGuiTrain {
    private JPanel mainPanel;
    private JPanel trainInfoPanel;
    private JComboBox upgradeList;
    private JPanel upgradeCostPanel;
    private JPanel upgradePanel;
    private JPanel infoNamePanel;
    private JPanel infoValuePanel;
    private JButton upgradeButton;
    private JPanel upgradeQueuePanel;
    private JComboBox createList;
    private JPanel createCostPanel;
    private JButton createButton;
    private JPanel createQueuePanel;
    private JPanel createPanel;
    private JPanel wagonsPanel;
    private JPanel wagonsNamePanel;
    private JPanel wagonsLevelPanel;

    private Train train;
    private Wagon selectedWagon;
    private WagonRecipe selectedWagonRecipe;

    private int selectedWagonIndex = 0;

    public CliGuiTrain() {
        train = Client.getInstance().getTrain();
        init();
        update();
        selectedWagon = (Wagon) upgradeList.getSelectedItem();
        updateUpgradeCostPanel();

        upgradeList.addPopupMenuListener(new PopupMenuListener() {
            @Override
            public void popupMenuWillBecomeVisible(PopupMenuEvent e) {}

            @Override
            public void popupMenuWillBecomeInvisible(PopupMenuEvent e) {
                selectedWagon = (Wagon) upgradeList.getSelectedItem();
                selectedWagonIndex = upgradeList.getSelectedIndex();
                update();
            }

            @Override
            public void popupMenuCanceled(PopupMenuEvent e) {}
        });
        createList.addPopupMenuListener(new PopupMenuListener() {
            @Override
            public void popupMenuWillBecomeVisible(PopupMenuEvent e) {}

            @Override
            public void popupMenuWillBecomeInvisible(PopupMenuEvent e) {
                selectedWagonRecipe = (WagonRecipe) createList.getSelectedItem();
                update();
            }

            @Override
            public void popupMenuCanceled(PopupMenuEvent e) {}
        });
        upgradeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(selectedWagon == null) return;
                Client.getInstance().startUpgrade(selectedWagon.getId());

                update();
            }
        });
        createButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(selectedWagonRecipe == null) return;
                Client.getInstance().startCreation(selectedWagonRecipe.getRecipeIndex());

                update();
            }
        });
    }

    private void init() {
        infoNamePanel.setLayout(new GridLayout(0, 1));
        infoValuePanel.setLayout(new GridLayout(0, 1));

        infoNamePanel.add(new JLabel("size : "));
        infoNamePanel.add(new JLabel("speed : "));
        infoNamePanel.add(new JLabel("cargo : "));
        infoNamePanel.add(new JLabel("crafts : "));
    }

    public void update() {
        updateInfoPanel();
        updateWagonsPanel();
        updateUpgradeCostPanel();
        updateUpgradeQueuePanel();
        updateCreateCostPanel();
        updateCreateQueuePanel();
        updateWagonsList();
        updateCreateList();
    }

    public void updateInfoPanel() {
        infoValuePanel.removeAll();
        infoValuePanel.add(new JLabel("" + train.getSize()));
        infoValuePanel.add(new JLabel("" + WagonStats.getLocoSpeed(train)));

        ArrayList<Craft> crafts = Client.getInstance().getCrafts();
        int reservedCargo = 0;
        for(Craft c : crafts) reservedCargo += Recipe.getAllRecipes().get(c.getRecipeIndex()).getFinalProduct().getQuantity();
        int totalCargo = 0;
        for(ResourceAmount ra : Client.getInstance().getAllObjects()) totalCargo += ra.getQuantity();
        infoValuePanel.add(new JLabel(totalCargo + "(" + reservedCargo + ")" + "/" + WagonStats.getMaxCapacity(train)));

        int totalCrafts = crafts.size();
        int maxCrafts = WagonStats.getMaxParallelCraft(train);
        int currentCrafts = Math.min(totalCrafts, maxCrafts);
        int waitingCrafts = totalCrafts - currentCrafts;
        infoValuePanel.add(new JLabel(currentCrafts + "(" + waitingCrafts + ")" + "/" + maxCrafts));
    }

    public void updateWagonsPanel() {
        GuiUtility.listInPanel(wagonsNamePanel, train.getWagons(), w -> WagonStats.getName(w.getType()) + " ");
        GuiUtility.listInPanel(wagonsLevelPanel, train.getWagons(), w -> "" + w.getLevel());
    }

    public void updateWagonsList() {
        upgradeList.removeAllItems();
        for(Wagon w : train.getWagons()) upgradeList.addItem(w);
        if(selectedWagon != null) upgradeList.setSelectedIndex(selectedWagonIndex);
    }

    public void updateUpgradeCostPanel() {
        if(selectedWagon == null) return;
        ArrayList<ResourceAmount> costs = WagonStats.getUpgradeCost(selectedWagon);
        GuiUtility.listInPanel(upgradeCostPanel, costs, ra -> ra.toString());
    }

    public void updateUpgradeQueuePanel() {
        ArrayList<UpgradeWagon> upgrades = Client.getInstance().getUpgrades();
        upgradeQueuePanel.removeAll();
        upgradeQueuePanel.setLayout(new GridLayout(0, 2));
        for(UpgradeWagon uw : upgrades) {
            upgradeQueuePanel.add(new JLabel(uw.toString()));
            JProgressBar bar = new JProgressBar();
            int max = WagonStats.getUpgradeTime(uw.getWagon_to_upgrade().getLevel());
            bar.setMaximum(max);
            bar.setValue(max - uw.getRemainingTime());
            upgradeQueuePanel.add(bar);
        }
    }

    public void updateCreateList() {
        createList.removeAllItems();
        for(WagonRecipe wr : WagonRecipe.getAllRecipes()) createList.addItem(wr);
        if(selectedWagonRecipe != null) createList.setSelectedItem(selectedWagonRecipe);
    }

    public void updateCreateCostPanel() {
        if(selectedWagonRecipe == null) return;
        ArrayList<ResourceAmount> costs = selectedWagonRecipe.getCost();
        GuiUtility.listInPanel(createCostPanel, costs, ra -> ra.toString());
    }

    public void updateCreateQueuePanel() {
        ArrayList<CreateWagon> upgrades = Client.getInstance().getCreations();
        createQueuePanel.removeAll();
        createQueuePanel.setLayout(new GridLayout(0, 2));
        for(CreateWagon cw : upgrades) {
            createQueuePanel.add(new JLabel(cw.toString()));
            JProgressBar bar = new JProgressBar();
            int max = WagonRecipe.getAllRecipes().get(cw.getWagonRecipeIndex()).getProductionTime();
            bar.setMaximum(max);
            bar.setValue(max - cw.getRemainingTime());
            createQueuePanel.add(bar);
        }
    }
}
