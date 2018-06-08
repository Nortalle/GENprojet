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
    private static DataBase dataBase;

    @BeforeAll
    public static void setUpBeforeAll() {
        Server.getInstance().startServer();
        dataBase = new DataBase();
    }

    @BeforeEach
    public  void setUpBeforeEach(){
        System.out.println("---");
        client = Client.getInstance();
        client.connectServer();
        dataBase.deleteUser(username);
        dataBase.insertPlayer(username, username);
    }

    @Test
    public void loginCorrect(){
        String line = client.sendLogin(username, username);
        System.out.println("Server : " + line);
        assertEquals(OTrainProtocol.SUCCESS, line);
        client.readLine();
    }

    @Test
    public void loginWrong(){
        String username2 = "other";
        String line = client.sendLogin(username, username2);
        System.out.println("Server : " + line);
        assertEquals(OTrainProtocol.FAILURE, line);
        line = client.sendLogin(username, username);
        System.out.println("Server : " + line);
        client.readLine();
    }

    @Test
    public void createNewUser(){
        String username2 = "other";
        dataBase.deleteUser(username2);
        assertEquals(OTrainProtocol.SUCCESS, client.signUp(username2, username2));
    }

    @Test
    public void cantCreateAlreadyUsedUsername(){
        assertEquals(OTrainProtocol.FAILURE, client.signUp(username, username));
    }
}
