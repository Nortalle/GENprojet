package Gui;

import Client.Updatable;
import Utils.Cost;
import Utils.Reciept;

import javax.swing.*;
import java.awt.*;

public class cli_gui_craft implements Updatable {
    private JPanel panel1;
    private JTextField quantity_text_field;
    private JComboBox recieptDropdown;
    private JPanel costPanel;
    private JPanel availableCrafts;
    private JButton placeOrderButton;
    private JPanel OrderQueuePanel;


    public void update(){

        // il faut remplir le panel des availableCrafts avec les recettes
        // availableCrafts.addContent (...);

        // il faut remplir le dropdown avec la liste des recette
        for(Reciept r : Reciept.getAllReciepts()){
            JPanel p = new JPanel();
            JLabel name = new JLabel();
            name.setText(r.getName());
            p.add(name);
            recieptDropdown.addItem(r.getName() + ": ");       // ajout au dropdown du nom
            for(Cost c : r.getCosts()){
                JLabel cost = new JLabel();
                cost.setText("\t" + c.getQuantity() + " " + c.getRessource());
                p.add(cost);
            }
            availableCrafts.add(p);                     // ajout au panel de la recette
        }
        //recieptDropdown.addItem(...);

        // set la quantité du dropdown à 1 de base
        quantity_text_field.setText("1");

        // update le coût en fonction de la recette sélectionée
        updateCraftCost();
    }

    public void updateCraftCost(){

    }


    @Override
    public void Update() {

    }
}
