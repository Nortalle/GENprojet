import Server.DataBase;
import Utils.OTrainProtocol;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;
import Server.Server;
import Client.Client;
import org.junit.jupiter.api.Test;

public class TestConnection {

    private static Server server;
    private static Client client;
    private static String username = "vincent";

    @BeforeAll
    public static void setUpBeforeAll() {
        server = new Server();
        server.startServer();
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
    public void LoginCorrect(){
        client.sendLogin(username, username);
        String line = client.readLineFromServer();
        System.out.println("Server : " + line);
        assertEquals(OTrainProtocol.SUCCESS, line);
    }

    @Test
    public void LoginWrong(){
        String username2 = "other";
        client.sendLogin(username, username2);
        String line = client.readLineFromServer();
        System.out.println("Server : " + line);
        assertEquals(OTrainProtocol.FAILURE, line);
        client.sendLogin(username, username);
        line = client.readLineFromServer();
        System.out.println("Server : " + line);
    }


}
