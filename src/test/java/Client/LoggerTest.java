package Client;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LoggerTest {


    @Test
    void getLog() {
        Logger.getInstance().log("Bonjour salut");

        System.out.println(Logger.getInstance().getLog());

        Logger.getInstance().log("Comment ça va ?");

        System.out.println(Logger.getInstance().getLog());
    }
}