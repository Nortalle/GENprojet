package Gui;

import Client.Client;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LoginForm {
    private JTextArea OTrainTextArea;
    private JPasswordField input_password;
    private JTextField input_username;
    private JButton button_sign_up;
    private JButton button_login;
    private JPanel panel_main;
    private JPanel panel_buttons;
    private JTextArea label_password;
    private JTextArea label_username;

    private Client client;

    public LoginForm(Client c) {
        client = c;
        client.connectServer();

        button_sign_up.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {

            }
        });
        button_login.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                client.sendLogin(input_username.getText(), String.valueOf(input_password.getPassword()));
            }
        });
    }

    public JPanel getPanel_main() {
        return panel_main;
    }

    private void createUIComponents() {
        // TODO: place custom component creation code here
    }
}
