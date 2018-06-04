package Gui;

import Client.Client;
import Game.TrainStation;
import Utils.ResourceAmount;
import Utils.Ressource;

import javax.swing.*;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;
import java.util.ArrayList;

public class AdminGuiPlayer {
    private JPanel panel_player;
    private JComboBox select_player;
    private JComboBox ressource_select;
    private JTextField textField1;
    private JButton addButton;
    private JLabel inventoryLabel;

    private String selectedPlayer;

    public AdminGuiPlayer() {
        update();
        select_player.addPopupMenuListener(new PopupMenuListener() {
            @Override
            public void popupMenuWillBecomeVisible(PopupMenuEvent e) {}

            @Override
            public void popupMenuWillBecomeInvisible(PopupMenuEvent e) {
                selectedPlayer = (String) select_player.getSelectedItem();
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
        select_player.removeAllItems();
        for(String player : Client.getInstance().getAdminPlayers()) select_player.addItem(player);
        if(selectedPlayer == null) selectedPlayer = (String) select_player.getSelectedItem();
        select_player.setSelectedItem(selectedPlayer);
    }

    public void updateTextFields() {
        ressource_select.removeAllItems();
        for(Ressource.Type type : Ressource.Type.values()) ressource_select.addItem(type);
        if(selectedPlayer == null) {
            // clear ???
        } else {
            Client.getInstance().updateAdminCargo(selectedPlayer);
            String playerCargo = Client.getInstance().getAdminCargo();
            inventoryLabel.setText(playerCargo);
        }
    }
}
