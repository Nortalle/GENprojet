package Gui;

import Client.Client;
import Game.Resources;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class RessourcesForm {

    private Client client;
    private JPanel panel_main;
    private JButton disconnectButton;
    private JButton actualiserButton;
    private JPanel RessourcesPanel;
    private JPanel ObjectsPanel;
    private JPanel ActionPanel;
    private JLabel scrum_i;
    private JLabel eau_i;
    private JLabel bois_i;
    private JLabel coal_i;
    private JLabel oil_i;
    private JLabel iron_ore_i;
    private JLabel copper_ore_i;
    private JLabel steel_i;
    private JLabel gold_ore_i;
    private JLabel label_resources;


    public RessourcesForm(Client c) {
        this.client = c;
        updateResources();

        actualiserButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                updateResources();
            }
        });
        disconnectButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                client.disconnect();
            }
        });
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

    public JComponent getPanel_main() {
        return panel_main;
    }

    private void createUIComponents() {
        // TODO: place custom component creation code here
    }
}
