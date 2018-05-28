package Gui;

import Client.*;
import Game.Mine;
import Game.Train;
import Game.Wagon;
import Game.WagonMining;
import Utils.OTrainProtocol;
import Utils.WagonStats;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class cli_gui_Mine {
    private JComboBox select_mine;
    private JButton startMiningButton;
    private JPanel panel1;
    private JPanel availableMinesPanel;
    private JComboBox select_wagon;
    private JPanel currently_mining_panel;
    private JButton stopMiningButton;

    private Train train;

    public cli_gui_Mine() {
        train = Client.getInstance().getTrain();
        update();

        startMiningButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Mine mine = (Mine) select_mine.getSelectedItem();
                Wagon wagon = (Wagon) select_wagon.getSelectedItem();

                String line = Client.getInstance().startMining(wagon.getId(), mine.getId());
                if(line.equals(OTrainProtocol.SUCCESS)) {
                    update();
                }
            }
        });
        stopMiningButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String line = Client.getInstance().stopMining(((Wagon) select_wagon.getSelectedItem()).getId());
                if(line.equals(OTrainProtocol.SUCCESS)) {
                    update();
                }
            }
        });
    }

    public void update(){
        updateMinesPanel();
        updateCurrentlyMiningWagons();
        updateMinesList();
        updateWagonsList();
    }

    public void updateExceptList() {
        updateMinesPanel();
        updateCurrentlyMiningWagons();
    }

    public void updateMinesPanel() {
        availableMinesPanel.setLayout(new GridLayout(0,1));
        availableMinesPanel.removeAll();
        for(Mine m : train.getTrainStation().getMines()) {
            availableMinesPanel.add(new JLabel(m.toString()));
        }
    }

    public void updateCurrentlyMiningWagons() {
        currently_mining_panel.setLayout(new GridLayout(0,1));
        currently_mining_panel.removeAll();
        for( WagonMining wm : Client.getInstance().getWagonMining()) {
            JLabel label = new JLabel(wm.getWagon() + " -> " + wm.getCurrentMine());
            currently_mining_panel.add(label);
        }
    }

    public void updateMinesList() {
        select_mine.removeAllItems();
        for(Mine m : train.getTrainStation().getMines()) {
            select_mine.addItem(m);
        }
    }

    public void updateWagonsList() {
        select_wagon.removeAllItems();
        for(Wagon w : train.getWagons()){
            if(w.getTypeID() == WagonStats.DRILL_ID || w.getTypeID() == WagonStats.SAW_ID || w.getTypeID() == WagonStats.PUMP_ID){
                select_wagon.addItem(w);
            }
        }
    }
}
