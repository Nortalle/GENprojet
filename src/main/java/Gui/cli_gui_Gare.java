package Gui;

import Client.Client;
import Game.Mine;
import Game.Train;
import Game.TrainStation;
import Utils.JsonUtility;
import Utils.OTrainProtocol;
import com.google.gson.JsonArray;

import javax.swing.*;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;
import java.awt.*;
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
    private JPanel panel_liste_mines;
    private JPanel panel_liste_joueurs;
    private JPanel panel_infos;
    private JLabel stationInfosLabel;

    private TrainStation viewingStation;

    public cli_gui_Gare() {

        update();
        viewingStation = Client.getInstance().getTrain().getTrainStation();// maybe useless
        //setStationInfo();
        //String line = Client.getInstance().getStations();
        //for(TrainStation station : TrainStation.listFromJson((JsonArray) JsonUtility.fromJson(line))) select_station.addItem(station);

        button_travel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                viewingStation = (TrainStation) select_station.getSelectedItem();
                String line = Client.getInstance().changeStation(viewingStation.getId());
                if(line.equals(OTrainProtocol.SUCCESS)) setStationInfo();

            }
        });
        button_view.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                viewingStation = (TrainStation) select_station.getSelectedItem();
                setStationInfo();
            }
        });
        button_currentStation.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                viewingStation = Client.getInstance().getTrain().getTrainStation();
                setStationInfo();
            }
        });
        select_station.addPopupMenuListener(new PopupMenuListener() {
            public void popupMenuWillBecomeVisible(PopupMenuEvent e) {
                //select_station.removeAllItems();
                //for(TrainStation ts : Client.getInstance().getStations()) select_station.addItem(ts);
            }

            public void popupMenuWillBecomeInvisible(PopupMenuEvent e) {}

            public void popupMenuCanceled(PopupMenuEvent e) {}
        });
    }

    public void update(){
        setStationInfo();
    }

    private void setStationInfo() {
        if(viewingStation == null) viewingStation = Client.getInstance().getTrain().getTrainStation();

        label_stationName.setText(viewingStation.toString());
        label_stationCoords.setText(viewingStation.getPosX() + ";" + viewingStation.getPosY());

        // station info
        stationInfosLabel.setText(viewingStation.getInfos());

        // ETA bar
        Train train = Client.getInstance().getTrain();
        int totalTime = train.getTrainStationTotalETA();
        progressBar1.setMaximum(totalTime);
        progressBar1.setValue(totalTime - train.getTrainStationETA());
        progressBar1.setString((train.getTrainStationETA() == totalTime) ? "At Station" : "On the move");

        // trains at station
        panel_liste_joueurs.removeAll();
        panel_liste_joueurs.setLayout(new GridLayout(0, 1));
        for(Train t : Client.getInstance().getTrainsAtStation(viewingStation.getId())) panel_liste_joueurs.add(new JLabel(t.toString()));

        // mines
        if(viewingStation != null){// shouldn't be needed
            panel_liste_mines.removeAll();
            panel_liste_mines.setLayout(new GridLayout(0, 1));
            for(Mine m : viewingStation.getMines()) panel_liste_mines.add(new JLabel(m.toString()));
        }

        // update drop down list
        select_station.removeAllItems();
        for(TrainStation ts : Client.getInstance().getStations()) select_station.addItem(ts);

    }

    public JPanel getPanel_main() {
        return panel_main;
    }
}
