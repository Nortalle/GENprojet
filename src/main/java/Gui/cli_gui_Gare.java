package Gui;

import Client.Client;
import Client.Updatable;
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

public class cli_gui_Gare implements Updatable{
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

    private TrainStation viewingStation;

    public cli_gui_Gare() {

        Update();
        viewingStation = Client.getInstance().getTrain().getTrainStation();
        setStationInfo();

        String line = Client.getInstance().getStations();
        for(TrainStation station : TrainStation.listFromJson((JsonArray) JsonUtility.fromJson(line))) select_station.addItem(station);

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
                String line = Client.getInstance().getStations();
                select_station.removeAllItems();
                for(TrainStation ts : TrainStation.listFromJson((JsonArray) JsonUtility.fromJson(line))) select_station.addItem(ts);
            }

            public void popupMenuWillBecomeInvisible(PopupMenuEvent e) {}

            public void popupMenuCanceled(PopupMenuEvent e) {}
        });
    }

    public void Update(){
        //TrainStation ts = (TrainStation) select_station.getSelectedItem();
        viewingStation = (TrainStation) select_station.getSelectedItem();
        setStationInfo();
        if(viewingStation != null){
            panel_liste_mines.removeAll();
            panel_liste_mines.setLayout(new GridLayout(0, 1));
            for(Mine m : viewingStation.getMines()) {
                JLabel label = new JLabel(m.toString());
                panel_liste_mines.add(label);
            }
        }

    }

    private void setStationInfo() {
        if(viewingStation == null) viewingStation = Client.getInstance().getTrain().getTrainStation();

        label_stationName.setText(viewingStation.toString());
        label_stationCoords.setText(viewingStation.getPosX() + ";" + viewingStation.getPosY());

        Train train = Client.getInstance().getTrain();
        int totalTime = train.getTrainStationTotalETA();
        progressBar1.setMaximum(totalTime);
        progressBar1.setValue(totalTime - train.getTrainStationETA());
        progressBar1.setString((train.getTrainStationETA() == totalTime) ? "At Station" : "On the move");

    }

    public JPanel getPanel_main() {
        return panel_main;
    }
}
