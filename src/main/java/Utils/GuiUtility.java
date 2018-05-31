package Utils;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class GuiUtility {

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
