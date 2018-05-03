package Server;

public class DataBase {

    public boolean checkLoggin(String username, String password) {
        return username.equals(password);
    }
}
