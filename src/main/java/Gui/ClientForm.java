package Gui;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import Client.*;
import Game.Resources;
import Utils.Ressource;

import java.awt.event.*;
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
    private JPanel Inventaire;
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

    public ClientForm() {
        Client.setClientLogComponent(logTextArea);
        update();

        updateButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                update();
            }
        });
        tabs.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                update();
            }
        });
        disconnectButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Client.getInstance().disconnect();
            }
        });
    }

    private void updateResources(){
        //String answer = Client.getInstance().getResources();
        //Resources resources = new Resources(answer);
        //Resources resources = Client.getInstance().getResources();
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

    public void update() {
        //Client.getInstance().getTrain();
        Client.getInstance().updateAll();
        updateResources();
        cli_gui_gare.update();
        cli_gui_mine.update();
        cli_gui_craft.update();
        cliGuiInventory.update();
        cliGuiTrain.update();
    }
}
