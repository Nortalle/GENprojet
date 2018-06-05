package Gui;

import Client.Client;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AdminGuiMain {
    private JPanel mainPanel;
    private JButton disconnectButton;
    private JTabbedPane tabs;
    private AdminGuiGare gareForm;
    private AdminGuiMine mineForm;
    private AdminGuiPlayer playerForm;
    private JTextArea logTextArea;

    public AdminGuiMain() {
        Client.setClientLogComponent(logTextArea);
        Client.getInstance().updateAdminAll();
        update();

        disconnectButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Client.getInstance().disconnect();
            }
        });
        tabs.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                Client.getInstance().updateAdminAll();
                update();
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
