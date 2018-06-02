package Utils;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class GuiUtility {

    /**
     * @param panel panel where to place labels
     * @param list list of object to place in panel
     * @param lambda function to apply to object to get the string to display
     * @param <T> type of object
     */
    public static <T> void listInPanel(JPanel panel, ArrayList<T> list, ListToPanel<T> lambda) {
        panel.removeAll();
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.NORTHWEST;
        for(T item : list) panel.add(new JLabel(lambda.toDisplay(item)), gbc);
        gbc.weighty = 1.0;
        gbc.weightx = 1.0;
        panel.add(Box.createVerticalGlue(), gbc);
        panel.revalidate();
    }
}
