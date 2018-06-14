package Gui;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import Client.*;
import Utils.Ressource;

import java.awt.event.*;
import java.util.Timer;
import java.util.TimerTask;

public class ClientForm {
    private JPanel panel_main;

    //Ressources
    private JPanel RessouresGUI;

    //panelTabs
    private JPanel panelTabs;
    private JTabbedPane tabs;
    private JPanel TabGare;
    private JPanel Hangar;
    private JPanel Mine;
    private JPanel Factory;

    //Logs
    private JPanel Logs;
    private JLabel eau_i;
    private JLabel bois_i;
    private JLabel coal_i;
    private JLabel oil_i;
    private JLabel iron_ore_i;
    private JLabel copper_ore_i;
    private JLabel gold_ore_i;
    private JLabel scrum_i;
    private JPanel RessourcesPanel;
    private JButton updateButton;
    private cli_gui_Mine cli_gui_mine;
    private cli_gui_Gare cli_gui_gare;
    private Gui.cli_gui_craft cli_gui_craft;
    private JTextArea logTextArea;
    private CliGuiInventory cliGuiInventory;
    private CliGuiTrain cliGuiTrain;
    private JButton disconnectButton;
    private JPanel Inventory;
    private Gui.cli_gui_trade cli_gui_trade;
    private Gui.cli_gui_Rank cli_gui_Rank;

    public ClientForm() {
        Client.setClientLogComponent(logTextArea);
        sync();

        updateButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                sync();
                cli_gui_Rank.SyncAll();
            }
        });
        tabs.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                sync();
            }
        });
        disconnectButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Client.getInstance().disconnect();
                Client.getInstance().setConnectionPanel();
            }
        });

        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public synchronized void run() {
                if(Client.getInstance().isClientLogged())frequentLocalUpdate();
            }
        }, 0, 500);


    }

    private void updateResources(){
        int resources[] = Ressource.getPlayerBaseResources(Client.getInstance().getResourceAmounts());
        scrum_i.setText(Integer.toString(resources[0]));
        eau_i.setText(Integer.toString(resources[1]));
        bois_i.setText(Integer.toString(resources[2]));
        coal_i.setText(Integer.toString(resources[3]));
        oil_i.setText(Integer.toString(resources[4]));
        iron_ore_i.setText(Integer.toString(resources[5]));
        copper_ore_i.setText(Integer.toString(resources[6]));
        gold_ore_i.setText(Integer.toString(resources[7]));

    }

    public JPanel getPanel_main() {
        return panel_main;
    }

    public void sync(){
        Client.getInstance().updateAll();
        localUpdate();
    }

    /**
     * Maj de la GUI complête, appelée après un sync
     */
    public void localUpdate() {
        updateResources();
        cli_gui_gare.localUpdate();
        cli_gui_mine.localUpdate();
        cli_gui_craft.localUpdate();
        cliGuiInventory.localUpdate();
        cliGuiTrain.localUpdate();
        cli_gui_trade.localUpdate();
    }

    /**
     * Met a jour les parties de la GUI qui nécessitent une maj continue
     */
    public void frequentLocalUpdate() {
        updateResources();
        cli_gui_gare.frequentLocalUpdate();
        cli_gui_mine.frequentLocalUpdate();
        cli_gui_craft.frequentLocalUpdate();
        cliGuiInventory.frequentLocalUpdate();
        cliGuiTrain.frequentLocalUpdate();
        cli_gui_trade.frequentLocalUpdate();
    }

}
