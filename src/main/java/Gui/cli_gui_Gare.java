package Gui;

import Client.Client;
import Game.Mine;
import Game.Train;
import Game.TrainStation;
import Utils.OTrainProtocol;

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

        button_travel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                viewingStation = (TrainStation) select_station.getSelectedItem();
                String line = Client.getInstance().changeStation(viewingStation.getId());
                if(line.equals(OTrainProtocol.SUCCESS)) updateExceptList();

            }
        });
        button_view.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                viewingStation = (TrainStation) select_station.getSelectedItem();
                updateExceptList();
            }
        });
        button_currentStation.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                viewingStation = Client.getInstance().getTrain().getTrainStation();
                updateExceptList();
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
        updateStationInfo();
        updateEtaBar();
        updateTrainsAtStation();
        updateMines();
        updateStationList();
    }

    public void updateExceptList(){
        updateStationInfo();
        updateEtaBar();
        updateTrainsAtStation();
        updateMines();
    }

    public void updateStationInfo() {
        if(viewingStation == null) viewingStation = Client.getInstance().getTrain().getTrainStation();
        label_stationName.setText(viewingStation.toString());
        label_stationCoords.setText(viewingStation.getPosX() + ";" + viewingStation.getPosY());
        stationInfosLabel.setText(viewingStation.getInfos());
    }

    public void updateEtaBar() {
        Train train = Client.getInstance().getTrain();
        int totalTime = train.getTrainStationTotalETA();
        int eta = train.getTrainStationETA();
        progressBar1.setMaximum(totalTime);
        progressBar1.setValue(totalTime - eta);
        progressBar1.setString((eta == totalTime) ? "At Station" : "On the move");
    }

    public void updateTrainsAtStation() {
        if(viewingStation != null) {
            panel_liste_joueurs.removeAll();
            panel_liste_joueurs.setLayout(new GridLayout(0, 1));
            for(Train t : Client.getInstance().getTrainsAtStation(viewingStation.getId())) panel_liste_joueurs.add(new JLabel(t.toString()));
        }
    }

    public void updateMines() {
        if(viewingStation != null) {
            panel_liste_mines.removeAll();
            panel_liste_mines.setLayout(new GridLayout(0, 1));
            for(Mine m : viewingStation.getMines()) panel_liste_mines.add(new JLabel(m.toString()));
        }
    }

    public void updateStationList() {
        select_station.removeAllItems();
        for(TrainStation ts : Client.getInstance().getStations()) select_station.addItem(ts);
    }
}
