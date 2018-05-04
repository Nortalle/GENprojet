import Server.DataBase;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class TestDataBase {
    private static DataBase dataBase;

    @BeforeAll
    public static void setUpBeforeAll() {
        dataBase = new DataBase();
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
        assertEquals(username, dataBase.getAllUsers().get(0));
    }

    @Test
    public void insertNumberOfUsers(){
        String username = "user1";
        String password = "pass1";
        int nbUsers = dataBase.getAllUsers().size();
        if(dataBase.insertUser(username, password)) assertEquals(nbUsers + 1, dataBase.getAllUsers().size());
        else assertEquals(nbUsers, dataBase.getAllUsers().size());
    }

    @Test
    public void failInsertAlreadyExitingUser(){
        String username = "user1";
        String password = "pass1";
        dataBase.insertUser(username, password);
        assertFalse(dataBase.insertUser(username, password));
    }

    @Test
    public void insertNewUserAlsoInsertNewResourcesPerUser(){
        String username = "user1";
        String password = "pass1";
        dataBase.insertUser(username, password);
        assertTrue(dataBase.getPlayerResources(username)[0] > -1);
    }

    @Test
    public void deleteUserAlsoDeleteResourcesPerUser(){
        String username = "user1";
        String password = "pass1";
        dataBase.insertUser(username, password);
        dataBase.deleteUser(username);
        assertTrue(dataBase.getPlayerResources(username)[0] == -1);
    }
}
