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
    private JPanel createQueueNamePanel;
    private JPanel createQueueBarPanel;
    private JPanel upgradeQueueNamePanel;
    private JPanel upgradeQueueBarPanel;

    private Train train;
    private Wagon selectedWagon;
    private WagonRecipe selectedWagonRecipe;

    private int selectedWagonIndex = 0;

    public CliGuiTrain() {
        train = Client.getInstance().getTrain();
        init();
        update();
        //selectedWagon = (Wagon) upgradeList.getSelectedItem();
        //updateUpgradeCostPanel();

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
                String line = Client.getInstance().startUpgrade(selectedWagon.getId());
                Client.getInstance().updateUpgradeWagons();// MANUAL UPDATE
                if(line.equals(OTrainProtocol.SUCCESS)) update();
            }
        });
        createButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(selectedWagonRecipe == null) return;
                String line = Client.getInstance().startCreation(selectedWagonRecipe.getRecipeIndex());
                Client.getInstance().updateCreateWagons();// MANUAL UPDATE
                if(line.equals(OTrainProtocol.SUCCESS)) update();
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

    public void updateResources() {
        updateInfoPanel();
        updateWagonsPanel();
        updateUpgradeCostPanel();
        updateUpgradeQueuePanel();
        updateCreateCostPanel();
        updateCreateQueuePanel();
    }

    public void updateInfoPanel() {
        infoValuePanel.removeAll();
        infoValuePanel.add(new JLabel("" + train.getSize()));
        infoValuePanel.add(new JLabel("" + WagonStats.getLocoSpeed(train)));

        ArrayList<Craft> crafts = Client.getInstance().getCrafts();
        int reservedCargo = 0;
        for(Craft c : crafts) reservedCargo += Recipe.getAllRecipes().get(c.getRecipeIndex()).getFinalProduct().getQuantity();
        int totalCargo = 0;
        for(ResourceAmount ra : Client.getInstance().getResourceAmounts()) totalCargo += ra.getQuantity();
        infoValuePanel.add(new JLabel(totalCargo + "(" + reservedCargo + ")" + "/" + WagonStats.getMaxCapacity(train)));

        int totalCrafts = crafts.size();
        int maxCrafts = WagonStats.getMaxParallelCraft(train);
        int currentCrafts = Math.min(totalCrafts, maxCrafts);
        int waitingCrafts = totalCrafts - currentCrafts;
        infoValuePanel.add(new JLabel(currentCrafts + "(" + waitingCrafts + ")" + "/" + maxCrafts));
    }

    public void updateWagonsPanel() {
        GuiUtility.listInPanel(wagonsNamePanel, train.getWagons(), w -> new JLabel(WagonStats.getName(w.getType()) + " "));
        GuiUtility.listInPanel(wagonsLevelPanel, train.getWagons(), w -> new JLabel("" + w.getLevel()));
    }

    public void updateWagonsList() {
        upgradeList.removeAllItems();
        for(Wagon w : train.getWagons()) upgradeList.addItem(w);
        if(selectedWagon != null) upgradeList.setSelectedIndex(selectedWagonIndex);
    }

    public void updateUpgradeCostPanel() {
        if(selectedWagon == null) return;
        ArrayList<ResourceAmount> costs = WagonStats.getUpgradeCost(selectedWagon);
        GuiUtility.displayCost(upgradeCostPanel, costs);
    }

    public void updateUpgradeQueuePanel() {
        ArrayList<UpgradeWagon> upgrades = Client.getInstance().getUpgradeWagons();
        //GuiUtility.listInPanel(upgradeQueueNamePanel, upgrades, uw -> new JLabel(uw.toString()));
        //GuiUtility.listInPanel(upgradeQueueBarPanel, upgrades, uw -> GuiUtility.getProgressBar(uw, UpgradeWagon::getRemainingTime, u -> WagonStats.getUpgradeTime(u.getWagon_to_upgrade().getLevel())));
        GuiUtility.listProgressBar(upgradeQueuePanel, upgrades, UpgradeWagon::getRemainingTime, u -> WagonStats.getUpgradeTime(u.getWagon_to_upgrade().getLevel()));
    }

    public void updateCreateList() {
        createList.removeAllItems();
        for(WagonRecipe wr : WagonRecipe.getAllRecipes()) createList.addItem(wr);
        if(selectedWagonRecipe != null) createList.setSelectedItem(selectedWagonRecipe);
    }

    public void updateCreateCostPanel() {
        if(selectedWagonRecipe == null) return;
        ArrayList<ResourceAmount> costs = selectedWagonRecipe.getCost();
        GuiUtility.displayCost(createCostPanel, costs);
    }

    public void updateCreateQueuePanel() {
        ArrayList<CreateWagon> upgrades = Client.getInstance().getCreateWagons();
        //GuiUtility.listInPanel(createQueueNamePanel, upgrades, cw -> new JLabel(cw.toString()));
        //GuiUtility.listInPanel(createQueueBarPanel, upgrades, cw -> GuiUtility.getProgressBar(cw, CreateWagon::getRemainingTime, c -> WagonRecipe.getAllRecipes().get(c.getWagonRecipeIndex()).getProductionTime()), GridBagConstraints.NORTHEAST);
        GuiUtility.listProgressBar(createQueuePanel, upgrades, CreateWagon::getRemainingTime, c -> WagonRecipe.getAllRecipes().get(c.getWagonRecipeIndex()).getProductionTime());
    }
}
