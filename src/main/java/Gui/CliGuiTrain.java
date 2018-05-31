package Gui;

import Client.Client;
import Game.Train;
import Game.Wagon;
import Utils.ResourceAmount;
import Utils.WagonStats;

import javax.swing.*;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;
import java.awt.*;
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

    private Train train;
    private Wagon selectedWagon;

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
    }

    private void init() {
        infoNamePanel.setLayout(new GridLayout(0, 1));
        infoValuePanel.setLayout(new GridLayout(0, 1));
        wagonsPanel.setLayout(new GridLayout(0, 2));
        infoNamePanel.add(new JLabel("size : "));
        infoNamePanel.add(new JLabel("max cargo : "));
        infoNamePanel.add(new JLabel("max parallel crafts : "));
    }

    public void update() {
        updateInfoPanel();
        updateWagonsPanel();
        updateUpgradeCostPanel();
        updateWagonsList();
    }

    public void updateExceptList() {
        updateInfoPanel();
        updateWagonsPanel();
        updateUpgradeCostPanel();
    }

    public void updateInfoPanel() {
        infoValuePanel.removeAll();
        infoValuePanel.add(new JLabel("" + train.getSize()));
        infoValuePanel.add(new JLabel("" + WagonStats.getMaxCapacity(train)));
        infoValuePanel.add(new JLabel("" + WagonStats.getMaxParallelCraft(train)));
    }

    public void updateWagonsPanel() {
        wagonsPanel.removeAll();
        for(Wagon w : train.getWagons()) {
            wagonsPanel.add(new JLabel("" + WagonStats.getName(w.getType()) + " "));
            wagonsPanel.add(new JLabel("" + w.getLevel()));
        }
    }

    public void updateWagonsList() {
        upgradeList.removeAllItems();
        for(Wagon w : train.getWagons()) upgradeList.addItem(w);
    }

    public void updateUpgradeCostPanel() {
        if(selectedWagon == null) return;
        ArrayList<ResourceAmount> costs = WagonStats.getUpgradeCost(selectedWagon);
        upgradeCostPanel.removeAll();
        upgradeCostPanel.setLayout(new GridLayout(0, 1));
        for(ResourceAmount cost : costs) upgradeCostPanel.add(new JLabel(cost.toString()));
        upgradeCostPanel.revalidate();
    }
}
