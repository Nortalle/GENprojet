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
        label_resources.setText(answer);
    }

    public JPanel getPanel_main() {
        return panel_main;
    }

    private void createUIComponents() {
        // TODO: place custom component creation code here
    }
}
