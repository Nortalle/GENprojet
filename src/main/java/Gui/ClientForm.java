package Gui;

import javax.swing.*;

import Client.*;
import Game.Resources;

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
    private JLabel steel_i;
    private JLabel gold_ore_i;
    private JLabel scrum_i;
    private JPanel RessourcesPanel;
    private JButton updateButton;
    private cli_gui_Mine cli_gui_mine;
    private cli_gui_Gare cli_gui_gare;
    private Gui.cli_gui_craft cli_gui_craft;
    private JTextArea logTextArea;
    private CliGuiInventory cliGuiInventory;

    public ClientForm() {
        Client.setClientLogComponent(logTextArea);
        updateResources();

        updateButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                update();
            }
        });
    }

    private void updateResources(){
        String answer = Client.getInstance().getResources();
        Resources resources = new Resources(answer);
        scrum_i.setText(Integer.toString(resources.getScrum()));
        eau_i.setText(Integer.toString(resources.getEau()));
        bois_i.setText(Integer.toString(resources.getBois()));
        coal_i.setText(Integer.toString(resources.getCharbon()));
        oil_i.setText(Integer.toString(resources.getPetrol()));
        iron_ore_i.setText(Integer.toString(resources.getFer()));
        copper_ore_i.setText(Integer.toString(resources.getCuivre()));
        steel_i.setText(Integer.toString(resources.getAcier()));
        gold_ore_i.setText(Integer.toString(resources.getOr()));

    }

    public JPanel getPanel_main() {
        return panel_main;
    }

    public void update() {
        Client.getInstance().getTrain();
        updateResources();
        cli_gui_gare.update();
        cli_gui_mine.update();
        cli_gui_craft.update();
        cliGuiInventory.update();
    }

    public void updateExceptList() {
        Client.getInstance().getTrain();
        updateResources();
        cli_gui_gare.updateExceptList();
        cli_gui_mine.updateExceptList();
        cli_gui_craft.updateExceptList();
        cliGuiInventory.update();// no list for the moment
    }
}
