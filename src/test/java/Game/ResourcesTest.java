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
    void toJSON() {
        ressources.init();
        assertEquals("{\"scrum\":0,\"eau\":0,\"bois\":0,\"chardon\":0,\"petrol\":0,\"fer\":0,\"cuivre\":0,\"acier\":0,\"or\":0}", ressources.toJSON());
    }

    @Test
    void fromJSON() {
        String json = "{\"scrum\":40,\"eau\":0,\"bois\":30,\"chardon\":0,\"petrol\":40,\"fer\":0,\"cuivre\":0,\"acier\":1000,\"or\":0}";
        ressources.fromJSON(json);

        assertEquals(40, ressources.getScrum());
        assertEquals(0, ressources.getEau());
        assertEquals(30, ressources.getBois());
        assertEquals(0, ressources.getCharbon());
        assertEquals(40, ressources.getPetrol());
        assertEquals(0, ressources.getFer());
        assertEquals(0, ressources.getCuivre());
        assertEquals(1000, ressources.getAcier());
        assertEquals(0, ressources.getOr());
    }
}