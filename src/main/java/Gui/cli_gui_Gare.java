package Gui;

import Client.Client;
import Game.Mine;
import Game.SimpleStation;
import Game.Train;
import Game.TrainStation;
import Utils.GuiUtility;
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

    private SimpleStation viewingStation;
    private int viewingStationIndex = 0;

    public cli_gui_Gare() {

        localUpdate();
        viewingStation = new SimpleStation(Client.getInstance().getTrain().getTrainStation());// maybe useless
        Client.viewingStation = viewingStation.getId();

        button_travel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                viewingStation = (SimpleStation) select_station.getSelectedItem();
                Client.viewingStation = viewingStation.getId();
                String line = Client.getInstance().changeStation(viewingStation.getId());
                Client.getInstance().updateAll();// MANUAL UPDATE
                if(line.equals(OTrainProtocol.SUCCESS)) localUpdate();

            }
        });
        button_view.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                viewingStation = (SimpleStation) select_station.getSelectedItem();
                Client.viewingStation = viewingStation.getId();
                viewingStationIndex = select_station.getSelectedIndex();
                Client.getInstance().updateAll();// MANUAL UPDATE
                localUpdate();
            }
        });
        button_currentStation.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                viewingStation = new SimpleStation(Client.getInstance().getTrain().getTrainStation());
                Client.viewingStation = viewingStation.getId();
                Client.getInstance().updateAll();// MANUAL UPDATE
                localUpdate();
            }
        });
        select_station.addPopupMenuListener(new PopupMenuListener() {
            public void popupMenuWillBecomeVisible(PopupMenuEvent e) {}

            public void popupMenuWillBecomeInvisible(PopupMenuEvent e) {
                // maybe localUpdate so we can remove view button
            }

            public void popupMenuCanceled(PopupMenuEvent e) {}
        });
    }

    public void localUpdate(){
        frequentLocalUpdate();
        updateStationList();
        updateMines();
        updateTrainsAtStation();
        updateStationInfo();
    }

    public void frequentLocalUpdate(){
        updateEtaBar();
    }

    public void updateStationInfo() {
        if(viewingStation == null) {
            viewingStation = new SimpleStation(Client.getInstance().getTrain().getTrainStation());
            Client.viewingStation = viewingStation.getId();
        }
        label_stationName.setText(viewingStation.toString());
        label_stationCoords.setText(Client.getInstance().getTrain().getTrainStation().getPosX() + ";" + Client.getInstance().getTrain().getTrainStation().getPosY());
        stationInfosLabel.setText(viewingStation.getInfos());
    }

    public void updateEtaBar() {
        Train train = Client.getInstance().getTrain();
        int totalTime = train.getTrainStationTotalETA();
        int eta = train.getTrainStationETA();
        progressBar1.setMaximum(totalTime);
        progressBar1.setValue(totalTime - eta);
        progressBar1.setString((eta == 0) ? "At Station" : "On the move");
    }

    public void updateTrainsAtStation() {
        if (viewingStation == null) return;
        GuiUtility.listInPanel(panel_liste_joueurs, Client.getInstance().getTrainsAtStation(), train -> new JLabel(train.toString()));
    }

    public void updateMines() {
        if (viewingStation == null) return;
        GuiUtility.listInPanel(panel_liste_mines, viewingStation.getMines(), mine -> new JLabel(mine.toString()));
    }

    public void updateStationList() {
        select_station.removeAllItems();
        for(SimpleStation ts : Client.getInstance().getSimpleStations()) select_station.addItem(ts);
        if(select_station != null && select_station.getItemCount() > 0 && viewingStationIndex < select_station.getItemCount()) select_station.setSelectedIndex(viewingStationIndex);
    }
}
