package Utils;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

import Client.Client;
import Utils.ResourceAmount;

public class GuiUtility {

    /**
     * @param panel panel where to add components
     * @param list list of object to place in panel
     * @param lambda function to apply to object to get the component to add
     * @param <T> type of object
     */
    public static <T> void listInPanel(JPanel panel, ArrayList<T> list, ListToPanel<T> lambda) {
        panel.removeAll();
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.NORTHWEST;
        for(T item : list) panel.add(lambda.toAdd(item), gbc);
        gbc.weighty = 1.0;
        gbc.weightx = 1.0;
        panel.add(Box.createVerticalGlue(), gbc);
        panel.revalidate();
    }

    /**
     *
     * @param panel panel where to add components
     * @param list list of object to place in panel
     */
    public static void displayCost(JPanel panel, ArrayList<ResourceAmount> list){
        GuiUtility.listInPanel(panel, list, cost -> {
            int actualAmount = Client.getInstance().getSpecificResource(cost.getRessource());
            JLabel label = new JLabel(actualAmount + "/" + cost.toString());
            label.setForeground(actualAmount < cost.getQuantity() ? Color.RED : Color.GREEN);
            return label;
        });
    }
}
