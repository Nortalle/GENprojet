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
        client = new Client();

        client.connectServer();

    }

    @Test
    public void LoginCorrect(){
/*
        String username = "vincent";

        System.out.println("Server : " + client.readLineFromServer());
        System.out.println("Server : " + client.readLineFromServer());
        client.sendLogin(username, username);

        System.out.println("Server : " + client.readLineFromServer());

        assertEquals("YOU ARE LOGGED AS : " + username, client.readLineFromServer());*/
    }

    @Test
    public void testtest(){


        for (int i = 0; i < 8; ++i){
            System.out.println(client.readLineFromServer());
            client.sendLine("client " + i);
        }
    }


}
