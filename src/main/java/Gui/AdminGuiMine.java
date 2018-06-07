package Gui;

import Client.Client;
import Game.Mine;
import Game.TrainStation;
import Utils.GuiUtility;
import Utils.Ressource;

import javax.swing.*;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AdminGuiMine {
    private JComboBox gare_select;
    private JComboBox mine_select;
    private JComboBox resource_select;
    private JTextField max_ressource;
    private JTextField regen_rate;
    private JTextField current_amount;
    private JButton updateButton;
    private JButton deleteButton;
    private JPanel panel_mine;

    private TrainStation selectedStation;
    private Mine selectedMine;

    public AdminGuiMine() {
        update();
        GuiUtility.addChangeListener(max_ressource);
        GuiUtility.addChangeListener(regen_rate);
        GuiUtility.addChangeListener(current_amount);

        gare_select.addPopupMenuListener(new PopupMenuListener() {
            @Override
            public void popupMenuWillBecomeVisible(PopupMenuEvent e) {}

            @Override
            public void popupMenuWillBecomeInvisible(PopupMenuEvent e) {
                selectedStation = (TrainStation) gare_select.getSelectedItem();
                selectedMine = null;
                update();
            }

            @Override
            public void popupMenuCanceled(PopupMenuEvent e) {}
        });
        mine_select.addPopupMenuListener(new PopupMenuListener() {
            @Override
            public void popupMenuWillBecomeVisible(PopupMenuEvent e) {}

            @Override
            public void popupMenuWillBecomeInvisible(PopupMenuEvent e) {
                Object selectedObject = mine_select.getSelectedItem();
                if(selectedObject == null) return;
                if(!(selectedObject instanceof Mine)) selectedMine = null;
                else selectedMine = (Mine) selectedObject;
                update();
            }

            @Override
            public void popupMenuCanceled(PopupMenuEvent e) {}
        });
        updateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(selectedMine == null) {
                    Client.getInstance().sendNewMine(getMineFromInputs(-1));
                } else {
                    Client.getInstance().sendChangeMine(getMineFromInputs(selectedMine.getId()));
                }
            }
        });
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(selectedMine == null) {
                    return;
                } else {
                    Client.getInstance().sendDeleteMine(selectedMine.getId());
                }
            }
        });
    }

    public Mine getMineFromInputs(int id) {
        Mine mine = null;
        Ressource.Type type = (Ressource.Type) resource_select.getSelectedItem();
        int max;
        int regen;
        int amount;
        try {
            max = GuiUtility.getValueFromTextField(max_ressource);
            regen = GuiUtility.getValueFromTextField(regen_rate);
            amount = GuiUtility.getValueFromTextField(current_amount);
            mine = new Mine(id, type.ordinal(), amount, max, regen, selectedStation.getId());
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        return mine;
    }

    public void update() {
        updateStationList();
        updateMineList();
        updateTextFields();
    }

    public void updateStationList() {
        gare_select.removeAllItems();
        for(TrainStation station : Client.getInstance().getAdminTrainStations()) gare_select.addItem(station);
        if(selectedStation == null) selectedStation = (TrainStation) gare_select.getSelectedItem();
        if(selectedStation != null) gare_select.setSelectedItem(selectedStation);
    }

    public void updateMineList() {
        mine_select.removeAllItems();
        if(selectedStation == null) return;
        mine_select.addItem("NEW MINE");
        for(Mine mine : selectedStation.getMines()) mine_select.addItem(mine);
        if(selectedMine != null) mine_select.setSelectedItem(selectedMine);
    }

    public void updateTextFields() {
        resource_select.removeAllItems();
        for(int id : Ressource.getBaseResourcesId()) {
            if(id != Ressource.Type.SCRUM.ordinal()) {
                resource_select.addItem(Ressource.Type.values()[id]);
            }
        }
        if(selectedMine == null) {
            max_ressource.setText("");
            regen_rate.setText("");
            current_amount.setText("");
        } else {
            resource_select.setSelectedItem(Ressource.Type.values()[selectedMine.getResource()]);
            max_ressource.setText("1000"/* + selectedMine.getMax()*/);
            regen_rate.setText("10"/* + selectedMine.getRegen()*/);
            current_amount.setText("" + selectedMine.getAmount());
        }
    }
}
