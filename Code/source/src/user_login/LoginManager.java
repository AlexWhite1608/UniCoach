package user_login;

import data_access.DBConnection;
import domain_model.Professor;
import domain_model.Student;
import domain_model.User;
import java.sql.*;

public class LoginManager {
    private Connection connection = null;

    // Viene chiamato tipo per la registrazione dell'utente
    public void addUser(User user) throws SQLException{
        connection = DBConnection.connect();

        //TODO: controlla che email non siano uguali per non avere utenti multipli
        String sql = "INSERT OR IGNORE INTO Utente (Nome, Cognome, Email, Password, Tipologia) VALUES (?, ?, ?, ?, ?)";
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setString(1, user.getName());
        statement.setString(2, user.getSurname());
        statement.setString(3, user.getEmail());
        statement.setString(4, "12345"); //FIXME: bisogna implementare le password
        if (user instanceof Student)
            statement.setString(5, "Studente");
        else if (user instanceof Professor)
            statement.setString(5, "Docente");

        statement.executeUpdate();
        statement.close();
    }

    public boolean login(User user) throws SQLException{
        connection = DBConnection.connect();

        String sql = "SELECT * FROM Utente WHERE Email = ? AND Password = ?;";
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setString(1, user.getEmail());
        statement.setString(2, "12345");
        ResultSet rs = statement.executeQuery();
        boolean loggedIn = rs.next();

        System.out.println(user.getName() + " " + user.getSurname() + " logged");

        rs.close();
        statement.close();

        return loggedIn;
    }

    public void getAllUsers() throws SQLException {
        String sql = "SELECT * FROM Utente";
        PreparedStatement statement = connection.prepareStatement(sql);
        ResultSet rs = statement.executeQuery();

        while (rs.next()){
            System.out.println("----- Utente -----");
            System.out.println("Nome: " + rs.getString("Nome"));
            System.out.println("Cognome: " + rs.getString("Cognome"));
            System.out.println("Email: " + rs.getString("Email"));
            System.out.println("Password: " + rs.getString("Password"));
            System.out.println("Tipologia: " + rs.getString("Tipologia"));
            System.out.println("---------");
        }

        rs.close();
        statement.close();
    }

    public void logout(User user) {
        //FIXME: da implementare meglio
        System.out.println(user.getName() + " " + user.getSurname() + " log out");

        connection = DBConnection.disconnect();
    }


}
