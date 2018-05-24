import Server.Controller.Travel;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TravelControllerTest {
    private static Travel travel;

    @BeforeAll
    public static void setUpBeforeAll() {
        travel = new Travel();
        //new Thread(travel).start(); C'est n'est plus un Thread
    }

    @BeforeEach
    public  void setUpBeforeEach(){
        System.out.println("---");
    }

    @Test
    public void getNotExistingKey(){
        assertEquals(null, travel.getETA("unknown"));
    }
}
