package Gui;

import Client.Client;
import Game.Train;
import Game.UpgradeWagon;
import Game.Wagon;
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
    private JPanel wagonsPanel;
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
        wagonsPanel.removeAll();
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.NORTHWEST;
        for(Wagon w : train.getWagons()) {
            gbc.gridx = 1;
            wagonsPanel.add(new JLabel("" + WagonStats.getName(w.getType()) + " "), gbc);
            gbc.gridx = 2;
            wagonsPanel.add(new JLabel("" + w.getLevel()), gbc);
        }
        gbc.weighty = 1.0;
        gbc.weightx = 1.0;
        wagonsPanel.add(Box.createVerticalGlue(), gbc);
    }

    public void updateWagonsList() {
        upgradeList.removeAllItems();
        for(Wagon w : train.getWagons()) upgradeList.addItem(w);
    }

    public void updateUpgradeCostPanel() {
        if(selectedWagon == null) return;
        ArrayList<ResourceAmount> costs = WagonStats.getUpgradeCost(selectedWagon);
        upgradeCostPanel.removeAll();
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.NORTHWEST;
        for(ResourceAmount cost : costs) upgradeCostPanel.add(new JLabel(cost.toString()), gbc);

        gbc.weighty = 1.0;
        gbc.weightx = 1.0;
        upgradeCostPanel.add(Box.createVerticalGlue(), gbc);
        upgradeCostPanel.revalidate();
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
        createCostPanel.removeAll();
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.NORTHWEST;
        for(ResourceAmount cost : costs) createCostPanel.add(new JLabel(cost.toString()), gbc);

        gbc.weighty = 1.0;
        gbc.weightx = 1.0;
        createCostPanel.add(Box.createVerticalGlue(), gbc);
        createCostPanel.revalidate();
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
