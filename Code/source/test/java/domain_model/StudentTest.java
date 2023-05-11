package domain_model;

import data_access.DBConnection;
import manager_implementation.CoursesManager;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import java.io.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.io.ByteArrayInputStream;
import static junit.framework.TestCase.*;

import java.util.ArrayList;
import java.util.List;

public class StudentTest {
    @Before
    public void setUp(){
        conn = DBConnection.connect("../database/unicoachdb.db");
    }

    @After
    public void tearDown() throws SQLException{

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
        deleteExamStatement.close();

        //Elimina i corsi inseriti
        String deleteCourseSql = "DELETE FROM Corso WHERE Nome = ?";
        PreparedStatement deleteCourseStatement = conn.prepareStatement(deleteCourseSql);
        deleteCourseStatement.setString(1, "TestCorso");
        deleteCourseStatement.executeUpdate();
        deleteCourseStatement.setString(1, "TestCorso1");
        deleteCourseStatement.executeUpdate();
        deleteCourseStatement.setString(1, "TestCorso2");
        deleteCourseStatement.executeUpdate();
        deleteCourseStatement.close();

        if (conn != null) {
            conn = DBConnection.disconnect();
        }

    }

    @Test
    public void testAddStudent() throws SQLException {
        Student student = new Student("12345", "TestNome", "TestCognome");

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


    @Test
    public void testGetGrade() throws SQLException {
        Professor professorTest = new Professor("12345", "TestNome", "TestCognome");
        Student studentTest = new Student("12345", "TestNome", "TestCognome");
        Course courseTest = new Course("TestCorso", 6, professorTest, ExamType.WRITTEN_AND_ORAL_TEST);
        Exam examTest = new Exam(courseTest, "testData");

        professorTest.getProfessorGateway().setCourseId(professorTest);

        professorTest.setGrade(studentTest, examTest, 22);
        int grade = studentTest.getGrade(courseTest, studentTest);

        conn = DBConnection.connect("../database/unicoachdb.db");

        // Verifica che l'esame venga inserito correttamente
        String sql = "SELECT Voto FROM Esame WHERE Corso = ?";

        PreparedStatement statement = conn.prepareStatement(sql);
        statement.setString(1, courseTest.getId());

        ResultSet result = statement.executeQuery();

        assertTrue(result.next());
        assertEquals(grade, result.getInt("Voto"));

        statement.close();
    }

    @Test
    public void testAverage() throws SQLException {
        Student student = new Student("12345", "TestNome", "TestCognome");
        Professor professor = new Professor("12345", "TestNome", "TestCognome");
        Professor professor2 = new Professor("12346", "TestNome", "TestCognome");

        Course courseTest1 = new Course("TestCorso1", 6, professor, ExamType.WRITTEN_AND_ORAL_TEST);
        Course courseTest2 = new Course("TestCorso2", 6, professor2, ExamType.WRITTEN_AND_ORAL_TEST);

        Exam examTest1 = new Exam(courseTest1, "testData1");
        Exam examTest2 = new Exam(courseTest2, "testData2");

        professor.setGrade(student, examTest1, 25);
        professor2.setGrade(student, examTest2, 24);

        float average = ( 25 * 6 + 24 * 6 ) / 12f;

        assertEquals(average, student.getAverage(student), 0.0001f);

    }

    //FIXME: decidere se togliere o meno questo test
//    @Test
//    public void testUpdate() throws SQLException {
//        Student studentTest = new Student("12345", "TestName", "TestSurname");
//        Activity activity = new Activity("TestName", "TestDate", 13, 14);
//
//
//        studentTest.update(activity);
//
//        conn = DBConnection.connect("../database/unicoachdb.db");
//
//        //Verifico che l'attività sia stata correttamente inserita nel calendario dello studente
//        String sql = "SELECT * FROM CalendarioStudenti WHERE Id = ?";
//
//        PreparedStatement statement = conn.prepareStatement(sql);
//        statement.setString(1, activity.getId());
//
//        ResultSet result = statement.executeQuery();
//
//        assertTrue(result.next());
//        assertEquals(activity.getName(), result.getString("Attività"));
//        assertEquals(activity.getDate(), result.getString("Data"));
//        assertEquals(activity.getStartTime(), result.getInt("OraInizio"));
//        assertEquals(activity.getEndTime(), result.getInt("OraFine"));
//        assertEquals(studentTest.getId(), result.getString("Matricola"));
//
//        statement.close();
//
//        //Elimino l'attività inserita nel database
//        String deleteActivitySql = "DELETE FROM CalendarioDocenti WHERE Id = ?";
//        PreparedStatement deleteActivityStatement = conn.prepareStatement(deleteActivitySql);
//        deleteActivityStatement.setString(1, activity.getId());
//        deleteActivityStatement.executeUpdate();
//        deleteActivityStatement.close();
//
//    }

@Test
public void TestChooseCourses() throws SQLException {
    Student student = new Student("12345", "TestNome", "TestCognome");
    Professor professor = new Professor("12345", "TestNome", "TestCognome");
    Professor professor2 = new Professor("12346", "TestNome", "TestCognome");
    Professor professor3 = new Professor("12347", "TestNome", "TestCognome");
    Professor professor4 = new Professor("12348", "TestNome", "TestCognome");

    Course courseTest1 = new Course("TestCorso1", 6, professor, ExamType.WRITTEN_AND_ORAL_TEST);
    Course courseTest2 = new Course("TestCorso2", 6, professor2, ExamType.WRITTEN_AND_ORAL_TEST);
    Course courseTest3 = new Course("TestCorso3", 6, professor3, ExamType.WRITTEN_AND_ORAL_TEST);
    Course courseTest4 = new Course("TestCorso4", 6, professor4, ExamType.WRITTEN_AND_ORAL_TEST);

    professor.getProfessorGateway().setCourseId(professor);
    professor2.getProfessorGateway().setCourseId(professor2);
    professor3.getProfessorGateway().setCourseId(professor3);
    professor4.getProfessorGateway().setCourseId(professor4);

    // Simuliamo l'input utente con tutti i courseTest.getId()
    String input = courseTest1.getId() + "\n" + courseTest2.getId() + "\n" + courseTest3.getId() + "\n0\n";
    InputStream in = new ByteArrayInputStream(input.getBytes());
    System.setIn(in);

    // Catturiamo l'output su console tramite ByteArrayOutputStream e PrintStream
    ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    System.setOut(new PrintStream(outContent));

    student.chooseCourses();

    conn = DBConnection.connect("../database/unicoachdb.db");

    assertTrue(professor.getObservers().contains(student));
    assertTrue(professor2.getObservers().contains(student));
    assertTrue(professor3.getObservers().contains(student));

    //Verifichiamo che nel corso non scelto dallo studente quest'ultimo non sia iscritto
    assertFalse(professor4.getObservers().contains(student));

    //Verifica che siano inserite le righe nella tabella IscrizioneCorso
    String sql = "SELECT * FROM IscrizioneCorso WHERE IdStudente = ?";

    PreparedStatement statement = conn.prepareStatement(sql);
    statement.setString(1, student.getId());

    ResultSet result = statement.executeQuery();

    List<String> courseIdList = new ArrayList<>(); // Lista per memorizzare gli ID dei corsi

    while (result.next()) {
        courseIdList.add(result.getString("IdCorso")); // Aggiungi l'ID del corso alla lista
    }

    assertEquals(3, courseIdList.size()); // Verifica che siano presenti i corsi inseriti

    // Verifica che i corsi siano corretti confrontandoli con i corrispettivi ID
    assertTrue(courseIdList.contains(courseTest1.getId()));
    assertTrue(courseIdList.contains(courseTest2.getId()));
    assertTrue(courseIdList.contains(courseTest3.getId()));

    // Verifica che non ci siano altri ID di corsi nella lista
    assertFalse(courseIdList.contains(courseTest4.getId()));

    statement.close();

    //TODO: ELIMINA TUTTA LA ROBA
}

    private Connection conn;
}