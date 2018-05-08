package Gui;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.LinkedList;

import Client.*;

public class RessourcesForm {

    private Client client;
    private JPanel panel_main;
    private JButton disconnectButton;
    private JButton actualiserButton;
    private JPanel RessourcesPanel;
    private JPanel ObjectsPanel;
    private JPanel ActionPanel;
    private JLabel scrum_i;
    private JLabel water_i;
    private JLabel wood_i;
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

        // maj ressources

        //label_resources.setText(answer);
    }

    public JPanel getPanel_main() {
        return panel_main;
    }

    private void createUIComponents() {
        // TODO: place custom component creation code here
    }
}
