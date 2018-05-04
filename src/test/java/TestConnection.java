import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;
import Server.Server;
import Client.Client;
import org.junit.jupiter.api.Test;

public class TestConnection {

    private static Server server;
    private static Client client;

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

    }

    @Test
    public void LoginCorrect(){
        String username = "vincent";
        String line = client.readLineFromServer();
        System.out.println("Server : " + line);
        client.sendLogin(username, username);
        line = client.readLineFromServer();
        System.out.println("Server : " + line);
        assertEquals("YOU ARE LOGGED AS : " + username, line);
    }

    @Test
    public void LoginWrong(){
        String username = "vincent";
        String username2 = "other";
        String line = client.readLineFromServer();
        System.out.println("Server : " + line);
        client.sendLogin(username, username2);
        line = client.readLineFromServer();
        System.out.println("Server : " + line);
        assertEquals("SEND LOGGIN", line);
        client.sendLogin(username, username);
        line = client.readLineFromServer();
        System.out.println("Server : " + line);
    }


}
