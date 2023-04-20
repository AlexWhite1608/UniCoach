package domain_model;

import data_access.DBConnection;
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
    @Before
    public void setUp(){
        conn = DBConnection.connect("../database/unicoachdb.db");
    }

    @After
    public void tearDown() throws SQLException{

        // Elimina lo studente appena inserito dal database
        String deleteStudentSql = "DELETE FROM Studente WHERE Matricola = ?";
        PreparedStatement deleteStudentStatement = conn.prepareStatement(deleteStudentSql);
        deleteStudentStatement.setString(1, "12345");

        deleteStudentStatement.executeUpdate();
        deleteStudentStatement.close();

        // Elimina il libretto appena inserito dal database
        String deleteTranscriptSql = "DELETE FROM Libretto WHERE Codice = ?";
        PreparedStatement deleteTranscriptStatement = conn.prepareStatement(deleteTranscriptSql);
        deleteTranscriptStatement.setString(1, student.getUniTranscript().getId());

        deleteTranscriptStatement.executeUpdate();
        deleteTranscriptStatement.close();

    }

    @Test
    public void testAddStudent() throws SQLException {
        student = new Student("12345", "TestNome", "TestCognome");

        // Verifica che lo studente sia stato effettivamente aggiunto al database
        String sql = "SELECT * FROM Studente WHERE Matricola = ?";
        PreparedStatement statement = conn.prepareStatement(sql);
        statement.setString(1, "12345");

        ResultSet result = statement.executeQuery();

        assertTrue(result.next());
        assertEquals("TestNome", result.getString("Nome"));
        assertEquals("TestCognome", result.getString("Cognome"));
        assertEquals("12345", result.getString("Matricola"));

        statement.close();
    }

    private Connection conn;
    private Student student;
}