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
    private Student student;

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

        //Elimina il libretto dello studente
        String deleteTranscriptSql = "DELETE FROM Libretto WHERE Codice = ?";
        PreparedStatement deleteTranscriptStatement = conn.prepareStatement(deleteTranscriptSql);
        deleteTranscriptStatement.setString(1, student.getUniTranscript().getId());

        deleteTranscriptStatement.executeUpdate();
        deleteTranscriptStatement.close();

        //Elimina il professore appena inserito
        String deleteProfessorSql = "DELETE FROM Docente WHERE Matricola = ?";
        PreparedStatement deleteProfessorStatement = conn.prepareStatement(deleteProfessorSql);
        deleteProfessorStatement.setString(1, "12345");

        deleteProfessorStatement.executeUpdate();
        deleteProfessorStatement.close();

        //Elimina l'esame appena inserito
        String deleteExamSql = "DELETE FROM Esame WHERE Nome = ?";
        PreparedStatement deleteExamStatement = conn.prepareStatement(deleteExamSql);
        deleteExamStatement.setString(1, "TestCorso");

        deleteExamStatement.executeUpdate();
        deleteExamStatement.close();

        //Elimina il corso appena inserito
        String deleteCourseSql = "DELETE FROM Corso WHERE Nome = ?";
        PreparedStatement deleteCourseStatement = conn.prepareStatement(deleteCourseSql);
        deleteCourseStatement.setString(1, "TestCorso");

        deleteCourseStatement.executeUpdate();
        deleteCourseStatement.close();


        conn = DBConnection.disconnect();
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

        //TODO: verifica anche che venga costruito il libretto!

        statement.close();
    }

    @Test
    public void testAddExam() throws SQLException{
        student = new Student("12345", "TestNome", "TestCognome");
        Professor testProfessor = new Professor("12345", "TestNome", "TestCognome");
        Course courseTest = new Course("TestCorso", 6, testProfessor, ExamType.WRITTEN_AND_ORAL_TEST);
        Exam examTest = new Exam(courseTest, "testData", 30);

        student.addExam(examTest);

        // Verifica che l'esame venga inserito correttamente
        String sql = "SELECT * FROM Esame WHERE Codice = ?";
        PreparedStatement statement = conn.prepareStatement(sql);
        statement.setString(1, examTest.getId());

        ResultSet result = statement.executeQuery();

        assertTrue(result.next());
        assertEquals("TestCorso", result.getString("Nome"));
        assertEquals("testData", result.getString("Data"));
        assertEquals(6, result.getInt("CFU"));
        assertEquals(30, result.getInt("Voto"));
        assertEquals(courseTest.getId(), result.getString("Corso"));
        assertEquals(courseTest.getExamType().getDisplayName(), result.getString("TipoEsame"));

        statement.close();
    }
}