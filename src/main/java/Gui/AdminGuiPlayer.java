package Gui;

import Client.Client;
import Utils.GuiUtility;
import Utils.OTrainProtocol;
import Utils.Ressource;

import javax.swing.*;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AdminGuiPlayer {
    private JPanel panel_player;
    private JComboBox select_player;
    private JComboBox ressource_select;
    private JTextField amountTextField;
    private JButton updateButton;
    private JLabel inventoryLabel;
    private JButton deleteButton;

    private String selectedPlayer;
    private Ressource.Type selectedType;

    public AdminGuiPlayer() {
        update();
        GuiUtility.addChangeListener(amountTextField);

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
        ressource_select.addPopupMenuListener(new PopupMenuListener() {
            @Override
            public void popupMenuWillBecomeVisible(PopupMenuEvent e) {}

            @Override
            public void popupMenuWillBecomeInvisible(PopupMenuEvent e) {
                selectedType = (Ressource.Type) ressource_select.getSelectedItem();
                update();
            }

            @Override
            public void popupMenuCanceled(PopupMenuEvent e) {}
        });
        updateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int newAmount;
                try {
                    newAmount = GuiUtility.getValueFromTextField(amountTextField);
                } catch (NumberFormatException ex) {
                    ex.printStackTrace();
                    return;
                }
                String line = Client.getInstance().sendChangePlayerObject(selectedPlayer, selectedType.ordinal(), newAmount);
                if(line.equals(OTrainProtocol.SUCCESS)) {
                    Client.getInstance().updateAdminAll();
                    update();
                }
            }
        });
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String line = Client.getInstance().sendDeletePlayer(selectedPlayer);
                if(line.equals(OTrainProtocol.SUCCESS)) {
                    Client.getInstance().updateAdminAll();
                    update();
                }
            }
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
        // not exactly for the moment
        ressource_select.removeAllItems();
        for(Ressource.Type type : Ressource.Type.values()) ressource_select.addItem(type);
        if(selectedType == null) selectedType = (Ressource.Type) ressource_select.getSelectedItem();
        ressource_select.setSelectedItem(selectedType);
        if(selectedPlayer == null) {
            // clear ???
        } else {
            Client.getInstance().updateAdminCargo(selectedPlayer);
            String playerCargo = Client.getInstance().getAdminCargo();
            inventoryLabel.setText(playerCargo);

            Client.getInstance().updateAdminResourceAmount(selectedPlayer, selectedType.ordinal());
            amountTextField.setText("" + Client.getInstance().getAdminResourceAmount().getQuantity());
        }
    }
}
