package Gui;

import javax.swing.*;
import Client.*;
import Game.Resources;

import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class ClientForm {

    private Logger LOG = Logger.getInstance();

    private JPanel panel_main;

    //Ressources
    private JPanel RessouresGUI;

    //Onglets
    private JPanel Onglets;
    private JTabbedPane Train;
    private JPanel TrainGUI;
    private JPanel Hangar;
    private JPanel Inventaire;
    private JPanel Mine;
    private JPanel Factory;

    //Logs
    private JPanel Logs;
    private JTextPane LoggerTestPane;
    private JPanel RessourcesPanel;
    private JLabel eau_i;
    private JLabel bois_i;
    private JLabel coal_i;
    private JLabel oil_i;
    private JLabel iron_ore_i;
    private JLabel copper_ore_i;
    private JLabel steel_i;
    private JLabel gold_ore_i;
    private JLabel scrum_i;

    private Client client;

    public ClientForm(Client client) {
        this.client = client;
        updateResources();

        LOG.log("Ouverture de ce que le client va voir");


    }

    private void updateResources(){
        String answer = client.getResources();
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
}
