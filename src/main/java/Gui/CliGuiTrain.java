package Gui;

import Client.Client;
import Game.Train;
import Game.UpgradeWagon;
import Game.Wagon;
import Utils.GuiUtility;
import Utils.ResourceAmount;
import Utils.WagonRecipe;
import Utils.WagonStats;

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
                updateExceptList();
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
                updateExceptList();
            }

            @Override
            public void popupMenuCanceled(PopupMenuEvent e) {}
        });
        upgradeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(selectedWagon == null) return;
                Client.getInstance().startUpgrade(selectedWagon.getId());

                updateExceptList();
            }
        });
    }

    private void init() {
        infoNamePanel.setLayout(new GridLayout(0, 1));
        infoValuePanel.setLayout(new GridLayout(0, 1));

        infoNamePanel.add(new JLabel("size : "));
        infoNamePanel.add(new JLabel("speed : "));
        infoNamePanel.add(new JLabel("max cargo : "));
        infoNamePanel.add(new JLabel("max parallel crafts : "));
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

    public void updateExceptList() {
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
        infoValuePanel.add(new JLabel("" + WagonStats.getMaxCapacity(train)));
        infoValuePanel.add(new JLabel("" + WagonStats.getMaxParallelCraft(train)));
    }

    public void updateWagonsPanel() {
        GuiUtility.listInPanel(wagonsNamePanel, train.getWagons(), w -> WagonStats.getName(w.getType()) + " ");
        GuiUtility.listInPanel(wagonsLevelPanel, train.getWagons(), w -> "" + w.getLevel());
    }

    public void updateWagonsList() {
        upgradeList.removeAllItems();
        for(Wagon w : train.getWagons()) upgradeList.addItem(w);
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
    }

    public void updateCreateCostPanel() {
        if(selectedWagonRecipe == null) return;
        ArrayList<ResourceAmount> costs = selectedWagonRecipe.getCost();
        GuiUtility.listInPanel(createCostPanel, costs, ra -> ra.toString());
    }

    public void updateCreateQueuePanel() {
        /*ArrayList<UpgradeWagon> upgrades = Client.getInstance().getUpgrades();
        upgradeQueuePanel.removeAll();
        upgradeQueuePanel.setLayout(new GridLayout(0, 2));
        for(UpgradeWagon uw : upgrades) {
            upgradeQueuePanel.add(new JLabel(uw.toString()));
            JProgressBar bar = new JProgressBar();
            int max = WagonStats.getUpgradeTime(uw.getWagon_to_upgrade().getLevel());
            bar.setMaximum(max);
            bar.setValue(max - uw.getRemainingTime());
            upgradeQueuePanel.add(bar);
        }*/
    }
}
