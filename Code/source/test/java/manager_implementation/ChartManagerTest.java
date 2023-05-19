package manager_implementation;

import data_access.DBConnection;
import domain_model.Course;
import domain_model.ExamType;
import domain_model.Professor;
import domain_model.Student;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import javax.mail.MessagingException;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertFalse;

public class ChartManagerTest {
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
        deleteStudentStatement.setString(1, "12346");
        deleteStudentStatement.executeUpdate();
        deleteStudentStatement.setString(1, "12347");
        deleteStudentStatement.executeUpdate();
        deleteStudentStatement.setString(1, "12348");
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

    //TODO: si potrebbe usare anche negli altri test??
    private static void simulateUserInput(Student student, Course course) {
        String input = course.getId() + "\n0\n";
        InputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);

        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        student.chooseCourses();
    }

    //TODO: si potrebbe usare anche negli altri test??
    private static void simulateUserInput(Student student, List<Course> courseList) {
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        for (Course course : courseList) {
            String input = course.getId() + "\n0\n";
            InputStream in = new ByteArrayInputStream(input.getBytes());
            System.setIn(in);

            student.chooseCourses();
        }
    }

    @Test
    public void testStudentGraph() throws SQLException, MessagingException {
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

        professor.setGrade(student,25, "TestData", false);
        professor2.setGrade(student, 24, "TestData", false);
        professor3.setGrade(student, 29, "TestData", false);
        professor4.setGrade(student, 27, "TestData", false);


        student.displayExamsGraph();
        professor.displayExamsGraph(student);

        // Mostra i voti di tutti gli studenti iscritti al corso
        professor.displayExamsGraph(courseTest1);

        conn = DBConnection.connect("../database/unicoachdb.db");

        //Elimino i corsi in IscrizioneCorso
        String deleteCourseSql = "DELETE FROM IscrizioneCorso WHERE IdCorso = ?";
        PreparedStatement deleteCourseStatement = conn.prepareStatement(deleteCourseSql);
        deleteCourseStatement.setString(1, courseTest1.getId());
        deleteCourseStatement.executeUpdate();
        deleteCourseStatement.setString(1, courseTest2.getId());
        deleteCourseStatement.executeUpdate();
        deleteCourseStatement.setString(1, courseTest3.getId());
        deleteCourseStatement.executeUpdate();
        deleteCourseStatement.setString(1, courseTest4.getId());
        deleteCourseStatement.executeUpdate();
    }

    @Test
    public void testCourseGraph() throws SQLException, MessagingException {
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

        professor.setGrade(student1,25, "TestData", false);
        professor.setGrade(student2,21, "TestData", false);
        professor.setGrade(student3,29, "TestData", false);
        professor.setGrade(student4,30, "TestData", false);

        // Mostra i voti di tutti gli studenti iscritti al corso
        professor.displayExamsGraph(courseTest1);

        conn = DBConnection.connect("../database/unicoachdb.db");

        //Elimino i corsi in IscrizioneCorso
        String deleteCourseSql = "DELETE FROM IscrizioneCorso WHERE IdCorso = ?";
        PreparedStatement deleteCourseStatement = conn.prepareStatement(deleteCourseSql);
        deleteCourseStatement.setString(1, courseTest1.getId());
        deleteCourseStatement.executeUpdate();
    }

    @Test
    public void testAllCoursesAvg() throws SQLException, MessagingException {
        Student student1 = new Student("12345", "TestNome", "TestCognome");
        Student student2 = new Student("12346", "TestNome", "TestCognome");
        Student student3 = new Student("12347", "TestNome", "TestCognome");
        Student student4 = new Student("12348", "TestNome", "TestCognome");

        Professor professor = new Professor("12345", "TestNome", "TestCognome");
        Professor professor2 = new Professor("12346", "TestNome", "TestCognome");
        Professor professor3 = new Professor("12347", "TestNome", "TestCognome");
        Professor professor4 = new Professor("12348", "TestNome", "TestCognome");

        Course courseTest1 = new Course("TestCorso1", 6, professor, ExamType.WRITTEN_AND_ORAL_TEST);
        Course courseTest2 = new Course("TestCorso2", 6, professor2, ExamType.WRITTEN_AND_ORAL_TEST);
        Course courseTest3 = new Course("TestCorso3", 6, professor3, ExamType.WRITTEN_AND_ORAL_TEST);
        Course courseTest4 = new Course("TestCorso4", 6, professor4, ExamType.WRITTEN_AND_ORAL_TEST);

        List<Course> courseList = new ArrayList<>();
        courseList.add(courseTest1);
        courseList.add(courseTest2);
        courseList.add(courseTest3);
        courseList.add(courseTest4);

        // Si iscrivono gli studenti ai corsi
        simulateUserInput(student1, courseList);
        simulateUserInput(student2, courseList);
        simulateUserInput(student3, courseList);
        simulateUserInput(student4, courseList);

        // Si inseriscono i voti degli esami
        professor.setGrade(student1,25, "TestData", false);
        professor.setGrade(student2,21, "TestData", false);
        professor.setGrade(student3,29, "TestData", false);
        professor.setGrade(student4,30, "TestData", false);

        professor2.setGrade(student1,18, "TestData", false);
        professor2.setGrade(student2,26, "TestData", false);
        professor2.setGrade(student3,30, "TestData", false);
        professor2.setGrade(student4,30, "TestData", false);

        professor3.setGrade(student1,30, "TestData", false);
        professor3.setGrade(student2,20, "TestData", false);
        professor3.setGrade(student3,26, "TestData", false);
        professor3.setGrade(student4,19, "TestData", false);

        professor4.setGrade(student1,25, "TestData", false);
        professor4.setGrade(student2,22, "TestData", false);
        professor4.setGrade(student3,24, "TestData", false);
        professor4.setGrade(student4,20, "TestData", false);

        // Un qualsiasi professore può vedere la situazione di tutti i corsi
        professor.displayExamsGraph();

        conn = DBConnection.connect("../database/unicoachdb.db");

        //Elimino i corsi in IscrizioneCorso
        String deleteCourseSql = "DELETE FROM IscrizioneCorso WHERE IdCorso = ?";
        PreparedStatement deleteCourseStatement = conn.prepareStatement(deleteCourseSql);
        deleteCourseStatement.setString(1, courseTest1.getId());
        deleteCourseStatement.executeUpdate();
        deleteCourseStatement.setString(1, courseTest2.getId());
        deleteCourseStatement.executeUpdate();
        deleteCourseStatement.setString(1, courseTest3.getId());
        deleteCourseStatement.executeUpdate();
        deleteCourseStatement.setString(1, courseTest4.getId());
        deleteCourseStatement.executeUpdate();
    }
    private Connection conn;
}
