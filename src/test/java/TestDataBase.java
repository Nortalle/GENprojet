import Server.DataBase;
import Server.Server;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class TestDataBase {
    private static DataBase dataBase;
    private static String username = "user1";
    private static String password = "pass1";

    @BeforeAll
    public static void setUpBeforeAll() {
        dataBase = new DataBase();
    }

    @BeforeEach
    public  void setUpBeforeEach(){
        System.out.println("---");
        dataBase.deleteUser(username);
        Server.getInstance().init();
    }

    @Test
    public void insertNewUser(){
        assertTrue(dataBase.insertUser(username, password));
    }

    @Test
    public void numberOfUsersAfterInsert(){
        int nbUsers = dataBase.getAllUsers().size();
        if(dataBase.insertUser(username, password)) assertEquals(nbUsers + 1, dataBase.getAllUsers().size());
        else assertEquals(nbUsers, dataBase.getAllUsers().size());
    }

    @Test
    public void failInsertAlreadyExitingUser(){
        dataBase.insertUser(username, password);
        assertFalse(dataBase.insertUser(username, password));
    }

    @Test
    public void insertNewUserAlsoInsertNewResourcesPerUser(){
        dataBase.insertUser(username, password);
        assertTrue(dataBase.getPlayerResources(username)[0] > -1);
    }

    @Test
    public void deleteUserAlsoDeleteResourcesPerUser(){
        dataBase.insertUser(username, password);
        dataBase.deleteUser(username);
        assertTrue(dataBase.getPlayerResources(username)[0] == -1);
    }
}
