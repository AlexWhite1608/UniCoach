package user_login;

import data_access.DBConnection;
import domain_model.Professor;
import domain_model.Student;
import junit.framework.TestCase;
import org.junit.After;
import org.junit.Before;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LoginManagerTest extends TestCase {

    @Before
    public void setUp(){
        conn = DBConnection.connect("../database/unicoachdb.db");
        loginManager = new LoginManager("../database/unicoachdb.db");
    }

    @After
    public void tearDown() throws SQLException {

        //Elimina il professore appena inserito
        String deleteProfessorSql = "DELETE FROM Docente WHERE Matricola = ?";
        PreparedStatement deleteProfessorStatement = conn.prepareStatement(deleteProfessorSql);
        deleteProfessorStatement.setString(1, "12121");

        deleteProfessorStatement.executeUpdate();
        deleteProfessorStatement.close();

        //Cancella l'utente inserito
        String deleteUserSql = "DELETE FROM Utente WHERE Nome = ?";
        PreparedStatement deleteUserStatement = conn.prepareStatement(deleteUserSql);
        deleteUserStatement.setString(1, "testNome");

        deleteUserStatement.executeUpdate();
        deleteUserStatement.close();

        conn = DBConnection.disconnect();
    }

    public void testAddUser() throws SQLException {
        Professor professor = new Professor("12121", "testNome", "testCognome");

        loginManager.addUser(professor);

        //Verifica che siano aggiunti i due utenti
        String sql = "SELECT * FROM Utente WHERE Email = ? and Password = ?";
        PreparedStatement statement = conn.prepareStatement(sql);
        statement.setString(1, professor.getEmail());
        statement.setString(2, "12345");    //FIXME: gestire la password!

        ResultSet result = statement.executeQuery();

        assertTrue(result.next());
        assertEquals("testNome", result.getString("Nome"));
        assertEquals("testCognome", result.getString("Cognome"));
        assertEquals(professor.getEmail(), result.getString("Email"));
        assertEquals("12345", result.getString("Password"));

        statement.close();
    }

    public void testLogin() {
    }

    public void testLogout() {
    }

    private LoginManager loginManager;
    private Connection conn;
}