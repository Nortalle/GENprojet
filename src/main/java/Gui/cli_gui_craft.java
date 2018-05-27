package Gui;

import Utils.ResourceAmount;
import Utils.Recipe;

import javax.swing.*;

public class cli_gui_craft {
    private JPanel panel1;
    private JTextField quantity_text_field;
    private JComboBox recieptDropdown;
    private JPanel costPanel;
    private JPanel availableCrafts;
    private JButton placeOrderButton;
    private JPanel OrderQueuePanel;


    // TODO ???
    public void updateOld(){

        // il faut remplir le panel des availableCrafts avec les recettes
        // availableCrafts.addContent (...);

        // il faut remplir le dropdown avec la liste des recette
        for(Recipe r : Recipe.getAllRecipes()){
            JPanel p = new JPanel();
            JLabel name = new JLabel();
            name.setText(r.getName());
            p.add(name);
            recieptDropdown.addItem(r.getName() + ": ");       // ajout au dropdown du nom
            for(ResourceAmount c : r.getResourceAmounts()){
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

    public void update() {

    }
}
