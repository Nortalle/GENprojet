package Gui;

import Client.Client;
import Utils.OTrainProtocol;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LoginForm {
    private JPasswordField input_password;
    private JTextField input_username;
    private JButton button_sign_up;
    private JButton button_login;
    private JPanel panel_main;
    private JPanel panel_buttons;
    private JLabel label_info;

    private Client client;

    public LoginForm() {
        client = Client.getInstance();
        //client.connectServer();

        button_sign_up.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String answer = client.signUp(input_username.getText(), String.valueOf(input_password.getPassword()));

                if(answer.equals(OTrainProtocol.SUCCESS)) {
                    label_info.setForeground(Color.GREEN);
                    label_info.setText("New account created");
                }
                else if(answer.equals(OTrainProtocol.FAILURE)) {
                    label_info.setForeground(Color.RED);
                    label_info.setText("Fail to create new account");
                }
                else {
                    label_info.setForeground(Color.RED);
                    label_info.setText("ERROR");
                }
            }
        });
        button_login.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String answer = client.sendLogin(input_username.getText(), String.valueOf(input_password.getPassword()));

                if(answer.equals(OTrainProtocol.SUCCESS)) {
                    label_info.setForeground(Color.GREEN);
                    label_info.setText("You are logged");
                    //
                    client.setFrameContent(new ClientForm().getPanel_main(), new Dimension(800, 500));
                }
                else if(answer.equals(OTrainProtocol.FAILURE)) {
                    label_info.setForeground(Color.RED);
                    label_info.setText("Wrong username/password");
                }
                else {
                    label_info.setForeground(Color.RED);
                    label_info.setText("ERROR");
                }
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
