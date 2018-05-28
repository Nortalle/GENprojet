package Gui;

import Client.Client;
import Utils.ResourceAmount;
import Utils.Ressource;

import javax.swing.*;
import java.awt.*;

public class CliGuiInventory {
    private JPanel mainPanel;
    private JPanel objectsPanel;

    public CliGuiInventory() {
        update();
    }

    public void update() {
        updateObjectsList();
    }

    public void updateObjectsList() {
        objectsPanel.removeAll();
        //objectsPanel.setLayout(new GridLayout(0, 2));
        GridBagConstraints gbc = new GridBagConstraints();
        for(ResourceAmount ra : Client.getInstance().getAllObjects()) {
            gbc.gridx = 0;
            gbc.anchor = GridBagConstraints.EAST;
            objectsPanel.add(new JLabel(ra.getQuantity() + " x "), gbc);
            gbc.gridx = 1;
            gbc.anchor = GridBagConstraints.WEST;
            objectsPanel.add(new JLabel(Ressource.RessourceToString(ra.getRessource())), gbc);
        }
        objectsPanel.revalidate();
    }
}
