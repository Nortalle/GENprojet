package Gui;

import javax.swing.*;

public class cli_gui_craft {
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
        //recieptDropdown.addItem(...);

        // set la quantité du dropdown à 1 de base
        quantity_text_field.setText("1");

        // update le coût en fonction de la recette sélectionée
        updateCraftCost();
    }

    public void updateCraftCost(){

    }



}
