package Gui;

import Client.Client;
import Game.Ranking;
import Utils.GuiUtility;
import javafx.util.Pair;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Comparator;

public class cli_gui_Rank {
    private JPanel ranking_panel;
    private JPanel global_panel;
    private JPanel drill_panel;
    private JPanel pump_panel;
    private JPanel saw_panel;
    private JPanel items_panel;

    ArrayList<Ranking.Rank> globalRanking = new ArrayList<>();
    ArrayList<Ranking.Rank> drillRanking = new ArrayList<>();
    ArrayList<Ranking.Rank> sawRanking = new ArrayList<>();
    ArrayList<Ranking.Rank> pumpRanking = new ArrayList<>();
    ArrayList<Ranking.Rank> itemsRanking = new ArrayList<>();


    public void SyncAll(){
        Client.getInstance().updateRankings();
        ArrayList<Ranking> rankings = Client.getInstance().getRankings();

        for(Ranking r : rankings){
            globalRanking.add(r.getGlobalRank());
            drillRanking.add(r.getDrillRank());
            sawRanking.add(r.getSawRank());
            pumpRanking.add(r.getPumplRank());
            itemsRanking.add(r.getItemsRank());
        }

        globalRanking.sort(Comparator.comparing(Ranking.Rank::value).reversed());
        drillRanking.sort(Comparator.comparing(Ranking.Rank::value).reversed());
        sawRanking.sort(Comparator.comparing(Ranking.Rank::value).reversed());
        pumpRanking.sort(Comparator.comparing(Ranking.Rank::value).reversed());
        itemsRanking.sort(Comparator.comparing(Ranking.Rank::value).reversed());

        resetRankNo();
        GuiUtility.listInPanel(global_panel,globalRanking,a -> new JLabel(nextRank() + ") " + ((Ranking.Rank)a).name() + ": " + ((Ranking.Rank)a).value()));

        resetRankNo();
        GuiUtility.listInPanel(drill_panel,drillRanking,a -> new JLabel(nextRank() + ") " + ((Ranking.Rank)a).name() + ": " + ((Ranking.Rank)a).value()));

        resetRankNo();
        GuiUtility.listInPanel(pump_panel,sawRanking,a -> new JLabel(nextRank() + ") " + ((Ranking.Rank)a).name() + ": " + ((Ranking.Rank)a).value()));

        resetRankNo();
        GuiUtility.listInPanel(saw_panel,pumpRanking,a -> new JLabel(nextRank() + ") " + ((Ranking.Rank)a).name() + ": " + ((Ranking.Rank)a).value()));

        resetRankNo();
        GuiUtility.listInPanel(items_panel,itemsRanking,a -> new JLabel(nextRank() + ") " + ((Ranking.Rank)a).name() + ": " + ((Ranking.Rank)a).value()));

    }

    int rankNo = 0;

    int nextRank() {
        return ++rankNo;
    }

    void resetRankNo(){
        rankNo = 0;
    }
}
