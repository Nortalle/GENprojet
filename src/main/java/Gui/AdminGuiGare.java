package Gui;

import Client.Client;
import Game.TrainStation;

import javax.swing.*;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;

public class AdminGuiGare {
    private JPanel panel_admin_gare;
    private JComboBox dropdown_gare;
    private JButton new_gare;
    private JButton delete_gare;
    private JTextField gare_name;
    private JTextField platformLengthTextField;
    private JTextField nbPlatformsTextField;
    private JTextField xTextField;
    private JTextField yTextField;

    private TrainStation selectedStation;

    public AdminGuiGare() {
        update();
        dropdown_gare.addPopupMenuListener(new PopupMenuListener() {
            @Override
            public void popupMenuWillBecomeVisible(PopupMenuEvent e) {}

            @Override
            public void popupMenuWillBecomeInvisible(PopupMenuEvent e) {
                Object selectedObject = dropdown_gare.getSelectedItem();
                if(selectedObject == null) return;
                if(!(selectedObject instanceof TrainStation)) selectedStation = null;
                else selectedStation = (TrainStation) selectedObject;
                update();
            }

            @Override
            public void popupMenuCanceled(PopupMenuEvent e) {}
        });
    }

    public void update() {
        updateStationList();
        updateTextFields();
    }

    public void updateStationList() {
        dropdown_gare.removeAllItems();
        dropdown_gare.addItem("NEW STATION");
        for(TrainStation station : Client.getInstance().getAdminTrainStations()) dropdown_gare.addItem(station);
    }

    public void updateTextFields() {
        if(selectedStation == null) {
            gare_name.setText("");
            platformLengthTextField.setText("");
            nbPlatformsTextField.setText("");
            xTextField.setText("");
            yTextField.setText("");
        } else {
            gare_name.setText(selectedStation.toString());
            platformLengthTextField.setText("" + selectedStation.getSizeOfPlatforms());
            nbPlatformsTextField.setText("" + selectedStation.getNbOfPlatforms());
            xTextField.setText("" + selectedStation.getPosX());
            yTextField.setText("" + selectedStation.getPosY());
        }
    }
}
