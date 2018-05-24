package Gui;

import Client.*;
import Game.Mine;
import Game.Train;
import Game.Wagon;
import Utils.OTrainProtocol;
import Utils.WagonStats;

import javax.swing.*;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class cli_gui_Mine {
    private JComboBox comboBox1;
    private JButton startMiningButton;
    private JPanel panel1;
    private JPanel availableMinesPanel;
    private JLabel availableMineLabel;

    public cli_gui_Mine() {

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
                Mine mine = (Mine) comboBox1.getSelectedItem();
                Wagon wagon = null;
                for(Wagon w : Client.getInstance().getTrain().getWagons()) {
                    if(w.getTypeID() == WagonStats.DRILL_ID) {
                        wagon = w;
                        break;
                    }
                }
                String line = Client.getInstance().startMining(wagon.getId(), mine.getId());
                System.out.println(line);
                if(line.equals(OTrainProtocol.SUCCESS));
            }
        });
    }

    public void update(){
        Train train = Client.getInstance().getTrain();
        String listOfMines = "<html>";
        comboBox1.removeAllItems();
        for(Mine m : train.getTrainStation().getMines()) {
            listOfMines += m + "<br/>";
            comboBox1.addItem(m);
        }
        availableMineLabel.setText(listOfMines + "</html>");
    }
}
