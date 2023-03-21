package domain_model;

import data_access.DBConnection;
import data_access.StudentGateway;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertTrue;

public class StudentTest {
    private Connection conn;

    @Before
    public void setUp(){
        conn = DBConnection.connect("../database/unicoachdb.db");
    }

    @After
    public void tearDown() throws SQLException{
        // Elimina lo studente appena inserito dal database
        String deleteSql = "DELETE FROM Studente WHERE Matricola = ?";
        PreparedStatement deleteStatement = conn.prepareStatement(deleteSql);
        deleteStatement.setString(1, "12345");

        deleteStatement.executeUpdate();
        deleteStatement.close();

        conn = DBConnection.disconnect();
    }

    @Test
    public void testAddStudent() throws SQLException {
        Student student = new Student("12345", "Mario", "Rossi");

        // Verifica che lo studente sia stato effettivamente aggiunto al database
        String sql = "SELECT * FROM Studente WHERE Matricola = ?";
        PreparedStatement statement = conn.prepareStatement(sql);
        statement.setString(1, "12345");

        ResultSet result = statement.executeQuery();

        assertTrue(result.next());
        assertEquals("Mario", result.getString("Nome"));
        assertEquals("Rossi", result.getString("Cognome"));
        assertEquals("12345", result.getString("Matricola"));

        statement.close();
    }
}