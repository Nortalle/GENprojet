package Gui;

import Client.Client;
import Game.Train;
import Game.Wagon;
import Utils.WagonStats;

import javax.swing.*;
import java.awt.*;

public class CliGuiTrain {
    private JPanel mainPanel;
    private JPanel trainInfoPanel;
    private JPanel wagonsPanel;
    private JComboBox upgradeList;
    private JPanel upgradeInfoPanel;
    private JPanel upgradePanel;
    private JPanel infoNamePanel;
    private JPanel infoValuePanel;

    private Train train;

    public CliGuiTrain() {
        train = Client.getInstance().getTrain();
        init();
        update();
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
        updateWagonsList();
    }

    public void updateExceptList() {
        updateInfoPanel();
        updateWagonsPanel();
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
            wagonsPanel.add(new JLabel("" + WagonStats.getName(w.getTypeID()) + " "));
            wagonsPanel.add(new JLabel("" + w.getLevel()));
        }
    }

    public void updateWagonsList() {
        upgradeList.removeAllItems();
        for(Wagon w : train.getWagons()) upgradeList.addItem(w);
    }
}
