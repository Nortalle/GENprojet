package Utils;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.util.ArrayList;

import Client.Client;

public class GuiUtility {

    /**
     *
     * @param panel panel where to add components
     * @param list list of object to place in panel
     * @param lambda function to apply to object to get the component to add
     * @param <T> type of object
     */
    public static <T> void listInPanel(JPanel panel, ArrayList<T> list, ListToPanel<T> lambda) {
        listInPanel(panel, list, lambda, GridBagConstraints.NORTHWEST);
    }

    /**
     *
     * @param panel panel where to add components
     * @param list list of object to place in panel
     * @param lambda function to apply to object to get the component to add
     * @param anchor Alignment, default is GridBagConstraints.NORTHWEST
     * @param <T> type of object
     */
    public static <T> void listInPanel(JPanel panel, ArrayList<T> list, ListToPanel<T> lambda, int anchor) {
        panel.removeAll();
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = anchor;
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
     * @param lambdaCurrent how to calculate the current value
     * @param lambdaMax how to calculate the max value
     * @param <T> type of object
     */
    public static <T> void listProgressBar(JPanel panel, ArrayList<T> list, ToProgressBar<T> lambdaCurrent, ToProgressBar<T> lambdaMax) {
        listInPanel(panel, list, t -> {
            JPanel p = new JPanel();
            p.setLayout(new BorderLayout());
            p.add(new JLabel(t.toString() + " "), BorderLayout.WEST);
            p.add(GuiUtility.getProgressBar(t, lambdaCurrent, lambdaMax), BorderLayout.EAST);
            return p;
        }, GridBagConstraints.NORTHEAST);
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

    /**
     *
     * @param t object to create the bar for
     * @param lambdaCurrent how to calculate the current value
     * @param lambdaMax how to calculate the max value
     * @param <T> type of object
     * @return the progress bar
     */
    public static <T> JProgressBar getProgressBar(T t, ToProgressBar<T> lambdaCurrent, ToProgressBar<T> lambdaMax) {
        JProgressBar bar = new JProgressBar();
        int current = lambdaCurrent.getValue(t);
        int max = lambdaMax.getValue(t);
        bar.setMaximum(max);
        bar.setValue(max - current);
        return bar;
    }

    public static void addChangeListener(JTextField textField) {
        textField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                textField.setBackground(Color.WHITE);
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                textField.setBackground(Color.WHITE);
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                textField.setBackground(Color.WHITE);
            }
        });
    }

    public static int getValueFromTextField(JTextField textField) {
        int value;
        String text = textField.getText();
        try {
            value = Integer.valueOf(text);
        } catch (NumberFormatException e) {
            textField.setBackground(Color.RED);
            JOptionPane.showConfirmDialog(null, "\"" + text + "\"" + " is not a number", "WRONG NUMBER FORMAT", JOptionPane.DEFAULT_OPTION, JOptionPane.ERROR_MESSAGE, null);
            throw e;
        }
        return value;
    }
}
