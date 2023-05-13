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
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;
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

        conn = DBConnection.connect("../database/unicoachdb.db");

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
        deleteExamStatement.setString(1, "TestCorso3");
        deleteExamStatement.executeUpdate();
        deleteExamStatement.setString(1, "TestCorso4");
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

    // Crea un metodo per simulare l'input dell'utente e catturare l'output
    private static void simulateUserInput(Student student, Course course) {
        String input = course.getId() + "\n0\n";
        InputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);

        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        student.chooseCourses();
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


        // Simuliamo l'input utente con tutti i courseTest.getId()
        String input = courseTest1.getId() + "\n" + courseTest2.getId() + "\n" + courseTest3.getId() + "\n" + courseTest4+"\n0\n";
        InputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);

        // Catturiamo l'output su console tramite ByteArrayOutputStream e PrintStream
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        student.chooseCourses();

        professor.setGrade(student,25, "TestData");
        professor2.setGrade(student, 24, "TestData");
        professor3.setGrade(student, 29, "TestData");
        professor4.setGrade(student, 27, "TestData");


        student.displayExamsGraph();
        professor.displayExamsGraph(student);

        // Mostra i voti di tutti gli studenti iscritti al corso
        professor.displayExamsGraph(courseTest1);
    }

    @Test
    public void testCourseGraph() throws SQLException {
        Student student1 = new Student("12345", "TestNome", "TestCognome");
        Student student2 = new Student("12346", "TestNome", "TestCognome");
        Student student3 = new Student("12347", "TestNome", "TestCognome");
        Student student4 = new Student("12348", "TestNome", "TestCognome");
        Professor professor = new Professor("12345", "TestNome", "TestCognome");

        Course courseTest1 = new Course("TestCorso1", 6, professor, ExamType.WRITTEN_AND_ORAL_TEST);

        // Simula l'input per tutti gli studenti
        simulateUserInput(student1, courseTest1);
        simulateUserInput(student2, courseTest1);
        simulateUserInput(student3, courseTest1);
        simulateUserInput(student4, courseTest1);

        professor.setGrade(student1,25, "TestData");
        professor.setGrade(student2,21, "TestData");
        professor.setGrade(student3,29, "TestData");
        professor.setGrade(student4,30, "TestData");

        // Mostra i voti di tutti gli studenti iscritti al corso
        professor.displayExamsGraph(courseTest1);
    }

    private Connection conn;
}
