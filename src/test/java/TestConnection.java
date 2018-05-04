import Server.DataBase;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;
import Server.Server;
import Server.DataBase;
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
        DataBase dataBase = new DataBase("jdbc:mysql://localhost:3306/GEN_otrain?user=root&password=root");
        dataBase.insertUser(username, username);
    }

    @Test
    public void LoginCorrect(){
        String line = client.readLineFromServer();
        System.out.println("Server : " + line);
        client.sendLogin(username, username);
        line = client.readLineFromServer();
        System.out.println("Server : " + line);
        assertEquals("YOU ARE LOGGED AS : " + username, line);
    }

    @Test
    public void LoginWrong(){
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
