package Gui;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.LinkedList;

import Client.*;

public class RessourcesForm {

    private Client client;
    private JPanel panel_main;
    private JButton deconnecterButton;
    private JButton actualiserButton;
    private JPanel RessourcesPanel;
    private JPanel ObjectsPanel;
    private JPanel ActionPanel;
    private LinkedList<JLabel> ressources = new LinkedList<JLabel>();
    private LinkedList<JLabel> objects = new LinkedList<JLabel>();


    public RessourcesForm(Client c) {
        this.client = c;
        updateRessources();

        actualiserButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                updateRessources();
            }
        });
        deconnecterButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                client.disconnect();
            }
        });
    }

    private void updateRessources(){

        //ressources = client.getAllClientRessrouces;
        for(JLabel r : ressources){
            RessourcesPanel.add(r);
        }

        //objects = client.getAllClientObjects;
        for(JLabel o : objects){
            ObjectsPanel.add(o);
        }
    }

    public JPanel getPanel_main() {
        return panel_main;
    }

    private void createUIComponents() {
        // TODO: place custom component creation code here
    }
}
