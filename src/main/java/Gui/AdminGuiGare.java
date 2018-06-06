package Gui;

import Client.Client;
import Game.TrainStation;
import Utils.GuiUtility;

import javax.swing.*;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

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
        GuiUtility.addChangeListener(platformLengthTextField);
        GuiUtility.addChangeListener(nbPlatformsTextField);
        GuiUtility.addChangeListener(xTextField);
        GuiUtility.addChangeListener(yTextField);

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
        new_gare.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(selectedStation == null) {
                    Client.getInstance().sendNewStation(getStationFromInputs(-1));
                } else {
                    Client.getInstance().sendChangeStation(getStationFromInputs(selectedStation.getId()));
                }
            }
        });
        delete_gare.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(selectedStation == null) {
                    return;
                } else {
                    Client.getInstance().sendDeleteStation(selectedStation.getId());
                }
            }
        });
    }

    public TrainStation getStationFromInputs(int id) {
        TrainStation station = null;
        String name = gare_name.getText();
        int platformSize;
        int nbPlatform;
        int x;
        int y;
        try {
            platformSize = GuiUtility.getValueFromTextField(platformLengthTextField);
            nbPlatform = GuiUtility.getValueFromTextField(nbPlatformsTextField);
            x = GuiUtility.getValueFromTextField(xTextField);
            y = GuiUtility.getValueFromTextField(yTextField);
            station = new TrainStation(id, x, y, nbPlatform, platformSize, null);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        return station;
    }

    public void update() {
        updateStationList();
        updateTextFields();
    }

    public void updateStationList() {
        dropdown_gare.removeAllItems();
        dropdown_gare.addItem("NEW STATION");
        for(TrainStation station : Client.getInstance().getAdminTrainStations()) dropdown_gare.addItem(station);
        if(selectedStation != null) dropdown_gare.setSelectedItem(selectedStation);
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
