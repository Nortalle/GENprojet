package Gui;

import javax.swing.*;
import Client.*;

public class ClientForm {

    private JPanel ClientFromMainFrame;

    //Ressources
    private JPanel RessouresGUI;

    //Onglets
    private JPanel Onglets;
    private JTabbedPane Train;
    private JPanel TrainGUI;
    private JPanel Hangar;
    private JPanel Inventaire;
    private JPanel Mine;
    private JPanel Factory;

    //Logs
    private JPanel Logs;
    private JTextArea LoggerTextArea;

    private Client client;

    public ClientForm(Client client) {
        this.client = client;

        //RessouresGUI.setUI();

        RessouresGUI.setUI(new RessourcesForm(client).getPanel_main().getUI());

        LoggerTextArea.append(Logger.getInstance().getLog());
    }
}
