package manager;

import data_access.DBConnection;
import domain_model.Professor;
import domain_model.Student;
import domain_model.User;
import java.sql.*;
import java.util.Scanner;

public class LoginManager {
    private Connection connection;

    public LoginManager() {
        connection = DBConnection.connect("../database/unicoachdb.db");
    }

    public LoginManager(String testDbPath) {    //usato per i test
        connection = DBConnection.connect(testDbPath);
    }

    // Viene chiamato per la registrazione dell'utente
    public void addUser(User user) throws SQLException{

        // Registrazione utente -> inserimento password
        Scanner scanner = new Scanner(System.in);
        System.out.print("Benvenuto, registra la tua password: ");
        String userPassword = scanner.nextLine();
        user.setPassword(userPassword);

        String sql = "INSERT OR IGNORE INTO Utente (Id, Nome, Cognome, Email, Password, Tipologia) VALUES (?, ?, ?, ?, ?, ?)";
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setString(1, user.getId());
        statement.setString(2, user.getName());
        statement.setString(3, user.getSurname());
        statement.setString(4, user.getEmail());
        statement.setString(5, user.getPassword());
        if (user instanceof Student)
            statement.setString(6, "Studente");
        else if (user instanceof Professor)
            statement.setString(6, "Docente");

        statement.executeUpdate();
        statement.close();
    }

    public boolean login(User user) throws SQLException{

        // Login utente -> verifica della password
        Scanner scanner = new Scanner(System.in);
        System.out.print("Benvenuto " + user.getName() + ", inserisci la tua password: ");
        String userPassword = scanner.nextLine();

        String sql = "SELECT * FROM Utente WHERE Email = ? AND Password = ?;";
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setString(1, user.getEmail());
        statement.setString(2, userPassword);
        ResultSet rs = statement.executeQuery();
        boolean loggedIn = rs.next();

        System.out.println(user.getName() + " " + user.getSurname() + " logged");

        rs.close();
        statement.close();

        return loggedIn;
    }

    public void logout(User user) {
        //FIXME: da implementare meglio
        System.out.println(user.getName() + " " + user.getSurname() + " log out");
    }
}