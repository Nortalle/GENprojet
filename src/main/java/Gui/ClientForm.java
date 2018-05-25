package Gui;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import Client.*;
import Game.Resources;

import java.awt.event.*;

public class ClientForm implements Updatable{
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

    public ClientForm() {
        Client.setClientLogComponent(logTextArea);
        updateResources();

        tabs.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                //panCraft.update();
            }
        });
        updateButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Update();
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

    @Override
    public void Update() {
        //Client.getInstance().updateTrainStatus();
        Client.getInstance().getTrain();
        cli_gui_gare.Update();
        cli_gui_mine.Update();
        updateResources();
    }
}
