package Server.Controller;

import Client.Client;
import Game.Mine;
import Game.Wagon;
import Game.WagonMining;
import Server.DataBase;
import Server.Server;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MineControllerTest {
    private static MineController mc;

    private static Mine mine1 = new Mine(1, 1, 30, 2);
    private static Mine mine2 = new Mine(2, 2, 1000, 2);
    private static Mine mine3 = new Mine(3, 3, 900, 2);

    @BeforeAll
    public static void setUpBeforeAll() {
        mc = new MineController();
        mine1 = new Mine(1, 1, 30, 2);
        mine2 = new Mine(2, 2, 1000, 2);
        mine3 = new Mine(3, 3, 900, 2);


    }

    @BeforeEach
    public void setUpBeforeEach() {
    }

    @Test
    public void testMinage(){

        Wagon wagon = new Wagon(1 , 1000, 1, 2);
        WagonMining wagonMining = new WagonMining(wagon, mine1);

        mc.addWagon(wagonMining);

        long start = System.currentTimeMillis();
        while(System.currentTimeMillis() - start < 1200){

        }


    }


}