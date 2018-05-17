package Gui;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class cli_gui_Gare {
    private JPanel panel_main;
    private JLabel label_stationName;
    private JLabel label_stationCoords;
    private JButton button_view;
    private JButton button_travel;
    private JPanel panel_info;
    private JPanel panel_move;
    private JPanel panel_validate;
    private JComboBox select_station;
    private JPanel panel_progress;
    private JProgressBar progressBar1;
    private JButton button_currentStation;

    public cli_gui_Gare() {

        Update();

        button_travel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // envoyer la requête de changement de gare
            }
        });
        button_view.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // peupler les panel
                    // info station
                    // players at station
                    // mines at station
            }
        });
        button_currentStation.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {

            }
        });
    }

    public void Update(){
        // peuple les
    }

    public JPanel getPanel_main() {
        return panel_main;
    }
}
