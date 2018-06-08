package Gui;

import Client.Client;
import Utils.GuiUtility;
import Utils.Ressource;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Optional;

public class cli_gui_trade {
    private JComboBox dropdown_ressource_offer;
    private JComboBox dropdown_ressource_price;
    private JTextField quantity_offer;
    private JTextField quantity_price;
    private JButton searchButton;
    private JButton placeButton;
    private JPanel trading_panel;

    public cli_gui_trade() {
        localUpdate();
        GuiUtility.addChangeListener(quantity_offer);
        GuiUtility.addChangeListener(quantity_price);

        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Optional<Integer> offerType;
                Optional<Integer> priceType;
            }
        });
        placeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });
    }

    public void localUpdate() {
        frequentLocalUpdate();
        updateTypeLists();
    }

    public void frequentLocalUpdate() {

    }

    public void updateTypeLists() {
        dropdown_ressource_offer.addItem("ANY");
        dropdown_ressource_price.addItem("ANY");
        for(Ressource.Type t : Ressource.Type.values()) {
            dropdown_ressource_offer.addItem(t);
            dropdown_ressource_price.addItem(t);
        }
    }
}
