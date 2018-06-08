package Gui;

import Client.Client;
import Utils.GuiUtility;
import Utils.OTrainProtocol;
import Utils.Ressource;

import javax.swing.*;
import java.awt.*;
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
    private JPanel offersPanel;

    public cli_gui_trade() {
        localUpdate();
        GuiUtility.addChangeListener(quantity_offer);
        GuiUtility.addChangeListener(quantity_price);

        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Client.getInstance().updateOffers(getType(dropdown_ressource_offer).orElse(-1), getType(dropdown_ressource_price).orElse(-1));
                localUpdate();
            }
        });
        placeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Optional<Integer> offerType = getType(dropdown_ressource_offer);
                Optional<Integer> priceType = getType(dropdown_ressource_price);
                try {
                    if(!offerType.isPresent()) noResourceTypeError();
                    if(!priceType.isPresent()) noResourceTypeError();

                    String line = Client.getInstance().placeOffer(offerType.get(), GuiUtility.getValueFromTextField(quantity_offer), priceType.get(), GuiUtility.getValueFromTextField(quantity_price));

                    Client.getInstance().updateAll();
                    if(line.equals(OTrainProtocol.SUCCESS)) localUpdate();

                } catch (NumberFormatException | NoResourceTypeError ex) {
                    ex.printStackTrace();
                }
            }
        });
    }

    private Optional<Integer> getType(JComboBox list) {
        if(!(list.getSelectedItem() instanceof Ressource.Type)) return Optional.empty();
        else return Optional.of(((Ressource.Type) list.getSelectedItem()).ordinal());
    }

    private void noResourceTypeError() throws NoResourceTypeError {
        JOptionPane.showConfirmDialog(null, "You must select a type of resource", "NO RESOURCE TYPE", JOptionPane.DEFAULT_OPTION, JOptionPane.ERROR_MESSAGE, null);
        throw new NoResourceTypeError();
    }

    private static class NoResourceTypeError extends Exception {}

    public void localUpdate() {
        frequentLocalUpdate();
        updateTypeLists();
        updateOffers();
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

    public void updateOffers() {
        GuiUtility.listInPanel(offersPanel, Client.getInstance().getOffers(), offer -> {
            JPanel panel = new JPanel();
            JLabel label = new JLabel(offer.toString());
            JButton button = new JButton();

            // own offer -> cancel
            if(Client.getInstance().getUsername().equals(offer.getPlayerName())) {
                button.setText("CANCEL");
                button.addActionListener(e -> Client.getInstance().cancelOffer(offer.getId()));
            // someone else offer -> by
            } else {
                button.setText("BUY");
                button.addActionListener(e -> Client.getInstance().buyOffer(offer.getId()));
            }
            button.setPreferredSize(new Dimension(button.getPreferredSize().width, 20));

            panel.add(label);
            panel.add(button);
            return panel;
        });
    }
}
