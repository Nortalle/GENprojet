import Server.DataBase;
import Utils.OTrainProtocol;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;
import Server.Server;
import Client.Client;
import org.junit.jupiter.api.Test;

public class TestConnection {
    private static Client client;
    private static String username = "vincent";

    @BeforeAll
    public static void setUpBeforeAll() {
        Server.getInstance().startServer();
    }

    @BeforeEach
    public  void setUpBeforeEach(){
        System.out.println("---");
        client = new Client();
        client.connectServer();
        DataBase dataBase = new DataBase();
        dataBase.insertUser(username, username);
    }

    @Test
    public void loginCorrect(){
        String line = client.sendLogin(username, username);
        System.out.println("Server : " + line);
        assertEquals(OTrainProtocol.SUCCESS, line);
    }

    @Test
    public void loginWrong(){
        String username2 = "other";
        String line = client.sendLogin(username, username2);
        System.out.println("Server : " + line);
        assertEquals(OTrainProtocol.FAILURE, line);
        line = client.sendLogin(username, username);
        System.out.println("Server : " + line);
    }

    @Test
    public void createNewUser(){
        String username2 = "other";
        assertEquals(OTrainProtocol.SUCCESS, client.signUp(username2, username2));
    }

    @Test
    public void cantCreateAlreadyUsedUsername(){
        assertEquals(OTrainProtocol.FAILURE, client.signUp(username, username));
    }
}
