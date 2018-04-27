import static org.junit.jupiter.api.Assertions.*;

class ExempleTest {

    @org.junit.jupiter.api.BeforeEach
    void setUp() {
        System.out.println("Exécution de la méthode 'setUp'");
    }

    @org.junit.jupiter.api.AfterEach
    void tearDown() {
        System.out.println("Exécution de la méthode 'tearDown'");
    }

    @org.junit.jupiter.api.Test
    void incVal() {
        System.out.println("Exécution de la méthode 'incVal'");
    }

    @org.junit.jupiter.api.Test
    void getVal() {
    }

    @org.junit.jupiter.api.Test
    void getResult() {
    }

    @org.junit.jupiter.api.Test
    void getNom() {
    }

    @org.junit.jupiter.api.Test
    void getObject1() {
    }

    @org.junit.jupiter.api.Test
    void getObject2() {
    }

    @org.junit.jupiter.api.Test
    void factoriel() {
    }

    @org.junit.jupiter.api.Test
    void min() {
    }

    @org.junit.jupiter.api.Test
    void plusGrand() {
    }

    @org.junit.jupiter.api.Test
    void plusPetit() {
    }

    @org.junit.jupiter.api.Test
    void renvoiNull() {
    }
}