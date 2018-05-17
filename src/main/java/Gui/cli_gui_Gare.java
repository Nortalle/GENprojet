package Gui;

import Client.Client;
import Game.Train;
import Game.TrainStation;
import Utils.OTrainProtocol;

import javax.swing.*;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;
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
        Client.getInstance().updateTrainStatus();
        TrainStation ts = Client.getInstance().getTrain().getTrainStation();
        setStationInfo(ts.toString(), ts.getPosX(), ts.getPosY());

        String line = Client.getInstance().getStations();
        for(TrainStation station : TrainStation.listFromJSON(line)) select_station.addItem(station);

        button_travel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                TrainStation ts = (TrainStation) select_station.getSelectedItem();
                String line = Client.getInstance().changeStation(ts.getId());
                if(line.equals(OTrainProtocol.SUCCESS)) setStationInfo(ts.toString(), ts.getPosX(), ts.getPosY());

            }
        });
        button_view.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // peupler les panel
                // info station
                // players at station
                // mines at station
                TrainStation ts = (TrainStation) select_station.getSelectedItem();
                setStationInfo(ts.toString(), ts.getPosX(), ts.getPosY());
            }
        });
        button_currentStation.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Client.getInstance().updateTrainStatus();
                TrainStation ts = Client.getInstance().getTrain().getTrainStation();
                setStationInfo(ts.toString(), ts.getPosX(), ts.getPosY());
            }
        });
        select_station.addPopupMenuListener(new PopupMenuListener() {
            public void popupMenuWillBecomeVisible(PopupMenuEvent e) {
                String line = Client.getInstance().getStations();
                select_station.removeAllItems();
                for(TrainStation ts : TrainStation.listFromJSON(line)) select_station.addItem(ts);
            }

            public void popupMenuWillBecomeInvisible(PopupMenuEvent e) {}

            public void popupMenuCanceled(PopupMenuEvent e) {}
        });
    }

    public void Update(){
        // peuple les
    }

    private void setStationInfo(String name, int x, int y) {
        label_stationName.setText(name);
        label_stationCoords.setText(x + ";" + y);

        // temp
        int totalTime = 20;// hard coded
        progressBar1.setMaximum(totalTime);
        Client.getInstance().updateTrainStatus();
        progressBar1.setValue(totalTime - Client.getInstance().getTrain().getTrainStationETA());
    }

    public JPanel getPanel_main() {
        return panel_main;
    }
}
