package Admin;

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Admin {
    private static Admin instance;
    private Socket socket;
    private BufferedReader reader;
    private PrintWriter writer;

    public static Admin getInstance() {
        if(instance == null) instance = new Admin();
        return instance;
    }
}
