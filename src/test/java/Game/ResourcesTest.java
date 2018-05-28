package Game;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author Vincent Guidoux
 */
class ResourcesTest {
    static Resources ressources;


    @BeforeAll
    public static void setUpBeforeAll() {
        ressources = new Resources(0);

    }

    @Test
    void json() {
        Resources res = new Resources(10);
        Resources newRes = new Resources(res.toJson());
        assertEquals(res.getBois(), newRes.getBois());
    }
}