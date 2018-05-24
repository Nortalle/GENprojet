package Gui;

import Client.*;
import Game.Mine;
import Game.Train;
import Game.TrainStation;
import Game.Wagon;
import Utils.OTrainProtocol;
import Utils.WagonStats;

import javax.swing.*;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class cli_gui_Mine {
    private JComboBox comboBox1;
    private JButton startMiningButton;
    private JPanel panel1;
    private JPanel availableMinesPanel;

    public cli_gui_Mine() {

        update();

        comboBox1.addPopupMenuListener(new PopupMenuListener() {
            public void popupMenuWillBecomeVisible(PopupMenuEvent e) {
                update();
            }

            public void popupMenuWillBecomeInvisible(PopupMenuEvent e) {}

            public void popupMenuCanceled(PopupMenuEvent e) {}
        });

        startMiningButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // TODO
                System.out.println("Clic clic");
                Train train = Client.getInstance().getTrain();
                System.out.println("Train state :" + train.getTrainStationETA());
                if(train.getTrainStationETA() > 0) return;
                Mine mine = (Mine) comboBox1.getSelectedItem();
                System.out.println("mine sélectionnée : " + mine);
                Wagon wagon = null;
                for(Wagon w : train.getWagons()) {
                    if(w.getTypeID() == WagonStats.DRILL_ID) {
                        wagon = w;
                        break;
                    }
                }
                System.out.println("Wagon utilisé : " + wagon);

                String line = Client.getInstance().startMining(wagon.getId(), mine.getId());
                System.out.println(line);
                if(line.equals(OTrainProtocol.SUCCESS)) {
                    
                }
            }
        });
    }

    public void update(){
        Train train = Client.getInstance().getTrain();
        //String listOfMines = "<html>";
        comboBox1.removeAllItems();
        availableMinesPanel.removeAll();
        availableMinesPanel.setLayout(new FlowLayout());
        for(Mine m : train.getTrainStation().getMines()) {

            JLabel label = new JLabel(m.toString());
            //availableMinesPanel.setLayout(new FlowLayout());
            availableMinesPanel.add(label);
            //listOfMines += m + "<br/>";
            comboBox1.addItem(m);
        }
        //availableMineLabel.setText(listOfMines + "</html>");
    }
}
