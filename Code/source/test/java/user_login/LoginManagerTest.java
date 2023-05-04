package user_login;

import data_access.DBConnection;
import domain_model.Professor;
import domain_model.Student;
import junit.framework.TestCase;
import org.junit.After;
import org.junit.Before;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
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

        if (conn != null) {
            conn = DBConnection.disconnect();
        }
    }

    public void testAddUser() throws SQLException {
        Professor professor = new Professor("12121", "testNome", "testCognome");

        // Prepara la password simulata
        String simulatedInput = "password123\n";
        InputStream inputStream = new ByteArrayInputStream(simulatedInput.getBytes());
        System.setIn(inputStream);

        loginManager.addUser(professor);

        //Verifica che siano aggiunti i due utenti
        String sql = "SELECT * FROM Utente WHERE Email = ? and Password = ?";
        PreparedStatement statement = conn.prepareStatement(sql);
        statement.setString(1, professor.getEmail());
        statement.setString(2, professor.getPassword());

        ResultSet result = statement.executeQuery();

        assertTrue(result.next());
        assertEquals("testNome", result.getString("Nome"));
        assertEquals("testCognome", result.getString("Cognome"));
        assertEquals(professor.getEmail(), result.getString("Email"));
        assertEquals(professor.getPassword(), result.getString("Password"));

        statement.close();
    }

    public void testLogin() throws SQLException  {
        Student student = new Student("101010", "TestNome", "TestCognome");

        // Prepara la password simulata per la registrazione
        String simulatedInput1 = "password123\n";
        InputStream inputStream1 = new ByteArrayInputStream(simulatedInput1.getBytes());
        System.setIn(inputStream1);

        loginManager.addUser(student);

        // Prepara la password simulata per il login
        String simulatedInput2 = "password123\n";
        InputStream inputStream2 = new ByteArrayInputStream(simulatedInput2.getBytes());
        System.setIn(inputStream2);

        assertTrue(loginManager.login(student));

        //Cancella l'utente inserito
        String deleteUserSql = "DELETE FROM Utente WHERE Nome = ?";
        PreparedStatement deleteUserStatement = conn.prepareStatement(deleteUserSql);
        deleteUserStatement.setString(1, "TestNome");

        deleteUserStatement.executeUpdate();
        deleteUserStatement.close();

        //Elimina lo studente appena inserito
        String deleteStudentSql = "DELETE FROM Studente WHERE Matricola = ?";
        PreparedStatement deleteStudentStatement = conn.prepareStatement(deleteStudentSql);
        deleteStudentStatement.setString(1, "101010");

        deleteStudentStatement.executeUpdate();
        deleteStudentStatement.close();
    }

    //TODO: da implementare quando si è fatto per bene il codice per il logout
    public void testLogout(){
    }

    private LoginManager loginManager;
    private Connection conn;
}