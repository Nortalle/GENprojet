package Gui;

import Client.*;
import Game.Mine;
import Game.Train;

import javax.swing.*;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;

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
