package Server;

import java.sql.*;
import java.util.ArrayList;

public class DataBase {
    Connection connection;

    //jdbc:mysql://localhost:3306/GEN_otrain?user=root&password=root
    public DataBase(String url) {
        try {
            connection = DriverManager.getConnection(url);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean insertUser(String username, String password) {
        try {
            PreparedStatement ps = connection.prepareStatement("INSERT INTO Utilisateur VALUES(?,?);", Statement.RETURN_GENERATED_KEYS);
            ps.setObject(1, username);
            ps.setObject(2, password);
            int status = ps.executeUpdate();
            if(status == 0) return false;
            else return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean deleteAllUsers() {
        try {
            PreparedStatement ps = connection.prepareStatement("DELETE FROM Utilisateur;", Statement.RETURN_GENERATED_KEYS);
            int status = ps.executeUpdate();
            if(status == 0) return true;
            else return false;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public ArrayList<String> getAllusers() {
        ArrayList<String> result = new ArrayList<String>();
        try {
            ResultSet resultSet;
            PreparedStatement ps = connection.prepareStatement("SELECT nomUtilisateur FROM Utilisateur;", Statement.RETURN_GENERATED_KEYS);
            resultSet = ps.executeQuery();
            while(resultSet.next()) {
                result.add(resultSet.getString(1));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    public boolean checkLoggin(String username, String password) {
        try {
            ResultSet resultSet;
            PreparedStatement ps = connection.prepareStatement("SELECT motDePasse FROM Utilisateur WHERE nomUtilisateur=?", Statement.RETURN_GENERATED_KEYS);
            ps.setObject(1, username);
            resultSet = ps.executeQuery();
            if(resultSet.next()) return password.equals(resultSet.getString(1));
            else return false;
        }catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
