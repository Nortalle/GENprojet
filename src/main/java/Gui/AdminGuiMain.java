package Gui;

import Client.Client;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AdminGuiMain {
    private JPanel mainPanel;
    private JButton disconnectButton;
    private JTabbedPane tabs;

    public AdminGuiMain() {
        disconnectButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Client.getInstance().disconnect();
            }
        });
    }


    public JPanel getMainPanel() {
        return mainPanel;
    }
}
