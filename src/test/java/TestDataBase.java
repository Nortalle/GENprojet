import Server.DataBase;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class TestDataBase {
    private static DataBase dataBase;

    @BeforeAll
    public static void setUpBeforeAll() {
        dataBase = new DataBase("jdbc:mysql://localhost:3306/GEN_otrain?user=root&password=root");
    }

    @BeforeEach
    public  void setUpBeforeEach(){
        System.out.println("---");
        dataBase.deleteAllUsers();
    }

    @Test
    public void insertNewUser(){
        String username = "user1";
        String password = "pass1";
        assertTrue(dataBase.insertUser(username, password));
    }

    @Test
    public void insertGetAllUsers(){
        String username = "user1";
        String password = "pass1";
        dataBase.insertUser(username, password);
        assertEquals(username, dataBase.getAllusers().get(0));
    }

    @Test
    public void insertNumberOfUsers(){
        String username = "user1";
        String password = "pass1";
        dataBase.insertUser(username, password);
        assertEquals(1, dataBase.getAllusers().size());
    }
}
