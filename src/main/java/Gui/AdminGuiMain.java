package Gui;

import Client.Client;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AdminGuiMain {
    private JPanel mainPanel;
    private JButton disconnectButton;
    private JTabbedPane tabs;
    private AdminGuiGare gareForm;
    private AdminGuiMine mineForm;
    private AdminGuiPlayer playerForm;

    public AdminGuiMain() {
        Client.getInstance().updateAdminAll();
        update();

        disconnectButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Client.getInstance().disconnect();
            }
        });
    }

    public void update() {
        gareForm.update();
        mineForm.update();
        playerForm.update();
    }


    public JPanel getMainPanel() {
        return mainPanel;
    }
}
