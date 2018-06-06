package Gui;

import Client.Client;
import Utils.GuiUtility;
import Utils.ResourceAmount;
import Utils.Ressource;

import javax.swing.*;
import java.awt.*;

public class CliGuiInventory {
    private JPanel mainPanel;
    private JPanel objectsPanel;
    private JPanel amountPanel;
    private JPanel namePanel;

    public CliGuiInventory() {
        update();
    }

    public void update() {
        updateObjectsList();
    }

    public void updateResources() {
        updateObjectsList();
    }

    public void updateObjectsList() {
        GuiUtility.listInPanel(amountPanel, Client.getInstance().getResourceAmounts(), ra -> new JLabel(ra.getQuantity() + " x"),GridBagConstraints.NORTHEAST);
        GuiUtility.listInPanel(namePanel, Client.getInstance().getResourceAmounts(), ra -> new JLabel(Ressource.RessourceToString(ra.getRessource())));
    }
}
