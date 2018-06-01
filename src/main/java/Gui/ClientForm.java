package Gui;

import javax.swing.*;

import Client.*;
import Game.Resources;
import Game.WagonMining;
import Utils.WagonStats;

public class ClientForm {
    private JPanel panel_main;

    //Ressources
    private JPanel RessouresGUI;

    //panelTabs
    private JPanel panelTabs;
    private JTabbedPane tabs;
    private JPanel TabGare;
    private JPanel Hangar;
    private JPanel Inventaire;
    private JPanel Mine;
    private JPanel Factory;

    //Logs
    private JPanel Logs;
    private JLabel eau_i;
    private JLabel bois_i;
    private JLabel coal_i;
    private JLabel oil_i;
    private JLabel iron_ore_i;
    private JLabel copper_ore_i;
    private JLabel gold_ore_i;
    private JLabel scrum_i;
    private JPanel RessourcesPanel;
    private JButton updateButton;
    private cli_gui_Mine cli_gui_mine;
    private cli_gui_Gare cli_gui_gare;
    private Gui.cli_gui_craft cli_gui_craft;
    private JTextArea logTextArea;
    private CliGuiInventory cliGuiInventory;
    private CliGuiTrain cliGuiTrain;

    public ClientForm() {
        Client.setClientLogComponent(logTextArea);

        SyncClock.getInstance().addUpdater(new Updater() {
            @Override
            public void sync() {
                syncResources();
            }

            @Override
            public void localUpdate() {
                localUpdateRessources();
            }
        });

        updateButton.addActionListener(e -> update());
        tabs.addChangeListener(e -> {
            update();
        });
    }

    /**
     * Calcule l'augmentetion en une seconde a partir du dernier taux d'incrémentation connu ne pas appeler plus d'une fois par seconde
     */
    private void localUpdateRessources(){


        localRessources[0] += ressourceGainRate[0];
        localRessources[1] += ressourceGainRate[1];
        localRessources[2] += ressourceGainRate[2];
        localRessources[3] += ressourceGainRate[3];
        localRessources[4] += ressourceGainRate[4];
        localRessources[5] += ressourceGainRate[5];
        localRessources[6] += ressourceGainRate[6];
        localRessources[7] += ressourceGainRate[7];

        scrum_i.setText(Integer.toString(localRessources[0]));
        eau_i.setText(Integer.toString(localRessources[1]));
        bois_i.setText(Integer.toString(localRessources[2]));
        coal_i.setText(Integer.toString(localRessources[3]));
        oil_i.setText(Integer.toString(localRessources[4]));
        iron_ore_i.setText(Integer.toString(localRessources[5]));
        copper_ore_i.setText(Integer.toString(localRessources[6]));
        gold_ore_i.setText(Integer.toString(localRessources[7]));
    }

    private void syncResources(){

        // Ajouté pour updater les gain_rate
        int j = 0;
        for(int i : ressourceGainRate ){
            ressourceGainRate[j++] = 0;
        }
        for( WagonMining wm : Client.getInstance().getWagonMining()) {
            ressourceGainRate[wm.getCurrentMine().getResource()] += WagonStats.getMiningAmount(wm.getWagon());
        }

        // END Ajouté pour updater les gain_rate

        String answer = Client.getInstance().getResources();
        Resources resources = new Resources(answer);

        localRessources[0] = resources.getScrum();
        localRessources[1] = resources.getEau();
        localRessources[2] = resources.getBois();
        localRessources[3] = resources.getCharbon();
        localRessources[4] = resources.getPetrol();
        localRessources[5] = resources.getFer();
        localRessources[6] = resources.getCuivre();
        localRessources[7] = resources.getOr();

        scrum_i.setText(Integer.toString(localRessources[0]));
        eau_i.setText(Integer.toString(localRessources[1]));
        bois_i.setText(Integer.toString(localRessources[2]));
        coal_i.setText(Integer.toString(localRessources[3]));
        oil_i.setText(Integer.toString(localRessources[4]));
        iron_ore_i.setText(Integer.toString(localRessources[5]));
        copper_ore_i.setText(Integer.toString(localRessources[6]));
        gold_ore_i.setText(Integer.toString(localRessources[7]));
    }

    public JPanel getPanel_main() {
        return panel_main;
    }

    public void update() {
        Client.getInstance().getTrainSync();
        syncResources();
        cli_gui_gare.update();
        cli_gui_mine.update();
        cli_gui_craft.update();
        cliGuiInventory.update();
        cliGuiTrain.update();
    }

    /* PARTIE POUR GERER L'AUTOUPDATE*/

    private Updater u;

    private int[] localRessources =  {0,0,0,0,0,0,0,0};
    private int[] ressourceGainRate =  {0,0,0,0,0,0,0,0};

}
