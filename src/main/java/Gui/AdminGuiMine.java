package Gui;

import Client.Client;
import Game.Mine;
import Game.Train;
import Game.TrainStation;
import Utils.ResourceAmount;
import Utils.Ressource;

import javax.swing.*;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;
import java.util.ArrayList;

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
    }

    public void update() {
        updateStationList();
        updateMineList();
        updateTextFields();
    }

    public void updateStationList() {
        gare_select.removeAllItems();
        for(TrainStation station : Client.getInstance().getAdminTrainStations()) gare_select.addItem(station);
    }

    public void updateMineList() {
        mine_select.removeAllItems();
        if(selectedStation == null) return;
        mine_select.addItem("NEW MINE");
        for(Mine mine : selectedStation.getMines()) mine_select.addItem(mine);
    }

    public void updateTextFields() {
        resource_select.removeAllItems();
        for(int id : Ressource.getBaseResourcesId()) resource_select.addItem(Ressource.Type.values()[id]);
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
