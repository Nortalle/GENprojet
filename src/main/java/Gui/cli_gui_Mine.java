package Gui;

import Client.*;
import Game.Mine;
import Game.Train;
import Game.Wagon;
import Game.WagonMining;
import Utils.OTrainProtocol;
import Utils.WagonStats;

import javax.swing.*;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class cli_gui_Mine implements Updatable{
    private JComboBox select_mine;
    private JButton startMiningButton;
    private JPanel panel1;
    private JPanel availableMinesPanel;
    private JComboBox select_wagon;
    private JPanel currently_mining_panel;

    public cli_gui_Mine() {

        Update();

        select_mine.addPopupMenuListener(new PopupMenuListener() {
            public void popupMenuWillBecomeVisible(PopupMenuEvent e) {
                Update();
            }

            public void popupMenuWillBecomeInvisible(PopupMenuEvent e) {}

            public void popupMenuCanceled(PopupMenuEvent e) {}
        });

        startMiningButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // TODO
                Train train = Client.getInstance().getTrain();
                System.out.println("Train state :" + train.getTrainStationETA());
                Mine mine = (Mine) select_mine.getSelectedItem();
                System.out.println("mine sélectionnée : " + mine);
                Wagon wagon = (Wagon) select_wagon.getSelectedItem();
                System.out.println("Wagon utilisé : " + wagon);

                String line = Client.getInstance().startMining(wagon.getId(), mine.getId());
                System.out.println(line);
                if(line.equals(OTrainProtocol.SUCCESS)) {
                    
                }
            }
        });
    }

    @Override
    public void Update(){

        // réupération et maj de la liste des mines
        Train train = Client.getInstance().getTrain();
        availableMinesPanel.setLayout(new GridLayout(0,1));
        availableMinesPanel.removeAll();
        select_mine.removeAllItems();
        for(Mine m : train.getTrainStation().getMines()) {
            JLabel label = new JLabel(m.toString());
            availableMinesPanel.add(label);
            select_mine.addItem(m);
        }

        // récupération de la liste des wagons
        select_wagon.removeAllItems();
        for(Wagon w : train.getWagons()){
            if(w.getTypeID() == WagonStats.DRILL_ID){
                select_wagon.addItem(w);
            }
        }

        // récupération de la liste des wagons qui sont entrain de miner
        currently_mining_panel.setLayout(new GridLayout(0,1));
        currently_mining_panel.removeAll();
        Client.getInstance().updateWagonMinig();
        for( WagonMining wm : Client.getInstance().getWagonMining()) {
            JLabel label = new JLabel(wm.getWagon() + " -> " + wm.getCurrentMine());
            currently_mining_panel.add(label);
        }
    }
}
