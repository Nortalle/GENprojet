import com.sun.scenario.effect.impl.sw.sse.SSEBlend_SRC_OUTPeer;
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
        System.out.println("Server : " + client.readLineFromServer());
        client.sendLogin(username, username);
        assertEquals("YOU ARE LOGGED AS : " + username, client.readLineFromServer());
    }

    @Test
    public void LoginWrong(){
        String username = "vincent";
        String username2 = "other";
        System.out.println("Server : " + client.readLineFromServer());
        client.sendLogin(username, username2);
        assertEquals("SEND LOGGIN", client.readLineFromServer());
        client.sendLogin(username, username);
    }


}
