package domain_model;

import data_access.DBConnection;
import manager_implementation.Activity;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import user_login.LoginManager;

import javax.mail.MessagingException;
import javax.naming.directory.InvalidAttributesException;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertFalse;

public class GradeManagerTest {
    @Before
    public void setUp() {
        conn = DBConnection.connect("../database/unicoachdb.db");
    }

    @After
    public void tearDown() throws SQLException {

        // Elimina lo studente appena inserito dal database
        String deleteStudentSql = "DELETE FROM Studente WHERE Matricola = ?";
        PreparedStatement deleteStudentStatement = conn.prepareStatement(deleteStudentSql);
        deleteStudentStatement.setString(1, "12345");
        deleteStudentStatement.executeUpdate();
        deleteStudentStatement.close();

        //Elimina i professori inseriti
        String deleteProfessorSql = "DELETE FROM Docente WHERE Matricola = ?";
        PreparedStatement deleteProfessorStatement = conn.prepareStatement(deleteProfessorSql);
        deleteProfessorStatement.setString(1, "12345");
        deleteProfessorStatement.executeUpdate();
        deleteProfessorStatement.setString(1, "12346");
        deleteProfessorStatement.executeUpdate();
        deleteProfessorStatement.setString(1, "12347");
        deleteProfessorStatement.executeUpdate();
        deleteProfessorStatement.setString(1, "12348");
        deleteProfessorStatement.executeUpdate();
        deleteProfessorStatement.close();

        //Elimina gli esami inseriti
        String deleteExamSql = "DELETE FROM Esame WHERE Nome = ?";
        PreparedStatement deleteExamStatement = conn.prepareStatement(deleteExamSql);
        deleteExamStatement.setString(1, "TestCorso");
        deleteExamStatement.executeUpdate();
        deleteExamStatement.setString(1, "TestCorso1");
        deleteExamStatement.executeUpdate();
        deleteExamStatement.setString(1, "TestCorso2");
        deleteExamStatement.executeUpdate();

        //Elimina i corsi inseriti
        String deleteCourseSql = "DELETE FROM Corso WHERE Nome = ?";
        PreparedStatement deleteCourseStatement = conn.prepareStatement(deleteCourseSql);
        deleteCourseStatement.setString(1, "TestCorso");
        deleteCourseStatement.executeUpdate();
        deleteCourseStatement.setString(1, "TestCorso1");
        deleteCourseStatement.executeUpdate();
        deleteCourseStatement.setString(1, "TestCorso2");
        deleteCourseStatement.executeUpdate();
        deleteCourseStatement.setString(1, "TestCorso3");
        deleteCourseStatement.executeUpdate();
        deleteCourseStatement.setString(1, "TestCorso4");
        deleteCourseStatement.executeUpdate();
        deleteCourseStatement.close();

        if (conn != null) {
            conn = DBConnection.disconnect();
        }
    }

    @Test
    public void testStudentGraph() throws SQLException {
        Student student = new Student("12345", "TestNome", "TestCognome");
        Professor professor = new Professor("12345", "TestNome", "TestCognome");
        Professor professor2 = new Professor("12346", "TestNome", "TestCognome");
        Professor professor3 = new Professor("12347", "TestNome", "TestCognome");
        Professor professor4 = new Professor("12348", "TestNome", "TestCognome");

        Course courseTest1 = new Course("TestCorso1", 6, professor, ExamType.WRITTEN_AND_ORAL_TEST);
        Course courseTest2 = new Course("TestCorso2", 6, professor2, ExamType.WRITTEN_AND_ORAL_TEST);
        Course courseTest3 = new Course("TestCorso3", 6, professor3, ExamType.WRITTEN_AND_ORAL_TEST);
        Course courseTest4 = new Course("TestCorso4", 6, professor4, ExamType.WRITTEN_AND_ORAL_TEST);

        //Imposta i corsi ai professori
        professor.getProfessorGateway().setCourseId(professor);
        professor2.getProfessorGateway().setCourseId(professor2);
        professor3.getProfessorGateway().setCourseId(professor3);
        professor4.getProfessorGateway().setCourseId(professor4);

        Exam examTest1 = new Exam(courseTest1, "testData1");
        Exam examTest2 = new Exam(courseTest2, "testData2");
        Exam examTest3 = new Exam(courseTest3, "testData3");
        Exam examTest4 = new Exam(courseTest4, "testData4");

        professor.setGrade(student, examTest1, 25);
        professor2.setGrade(student, examTest2, 24);
        professor3.setGrade(student, examTest3, 29);
        professor4.setGrade(student, examTest4, 27);

        student.displayExamsGraph();
    }

    private Connection conn;
}
