package domain_model;

import data_access.DBConnection;
import manager_implementation.Activity;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import user_login.LoginManager;

import javax.mail.MessagingException;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertTrue;

public class ProfessorTest {

    @Before
    public void setUp(){
        conn = DBConnection.connect("../database/unicoachdb.db");
    }

    @After
    public void tearDown() throws SQLException{

        conn = DBConnection.connect("../database/unicoachdb.db");

        // Elimina gli studenti inseriti
        String deleteStudentSql = "DELETE FROM Studente WHERE Matricola = ?";
        PreparedStatement deleteStudentStatement = conn.prepareStatement(deleteStudentSql);
        deleteStudentStatement.setString(1, "12345");
        deleteStudentStatement.executeUpdate();
        deleteStudentStatement.setString(1, "12346");
        deleteStudentStatement.executeUpdate();
        deleteStudentStatement.setString(1, "12347");
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
    public void testAddProfessor() throws SQLException {
        Professor professor = new Professor("12345", "TestNome", "TestCognome");

        // Verifica che lo professore sia stato effettivamente aggiunto al database
        String sql = "SELECT * FROM Docente WHERE Matricola = ?";
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
    public void testAverageStudent() throws SQLException {
        Student student = new Student("12346", "TestNome", "TestCognome");
        Professor professor = new Professor("12345", "TestNome", "TestCognome");
        Professor professor2 = new Professor("12346", "TestNome", "TestCognome");

        Course courseTest1 = new Course("TestCorso1", 6, professor, ExamType.WRITTEN_AND_ORAL_TEST);
        Course courseTest2 = new Course("TestCorso2", 6, professor2, ExamType.WRITTEN_AND_ORAL_TEST);

        Exam examTest1 = new Exam(courseTest1, "testData1");
        Exam examTest2 = new Exam(courseTest2, "testData2");

        professor.setGrade(student, examTest1, 25);
        professor2.setGrade(student, examTest2, 24);

        float average = ( 25 * 6 + 24 * 6 ) / 12f;

        assertEquals(average, professor.getAverage(student), 0.0001f);

    }

    @Test
    public void testAverageCourse() throws SQLException {
        Student student1 = new Student("12345", "TestNome", "TestCognome");
        Student student2 = new Student("12346", "TestNome", "TestCognome");
        Student student3 = new Student("12347", "TestNome", "TestCognome");

        Professor professor = new Professor("12345", "TestNome", "TestCognome");
        Course courseTest1 = new Course("TestCorso1", 6, professor, ExamType.WRITTEN_AND_ORAL_TEST);

        professor.getProfessorGateway().setCourseId(professor);

        Exam examTest1 = new Exam(courseTest1, "testData");
        Exam examTest2 = new Exam(courseTest1, "testData1");
        Exam examTest3 = new Exam(courseTest1, "testData2");

        professor.setGrade(student1, examTest1, 22);
        professor.setGrade(student2, examTest2, 28);
        professor.setGrade(student3, examTest3, 21);

        float average = ( 22 * 6 + 28 * 6 + 21 * 6 ) / 18f;

        assertEquals(average, professor.getAverage(), 0.0001f);
    }

    @Test
    public void testAddExam() throws SQLException {
        Student student = new Student("12345", "TestNome", "TestCognome");
        Professor testProfessor = new Professor("12345", "TestNome", "TestCognome");
        Course courseTest = new Course("TestCorso", 6, testProfessor, ExamType.WRITTEN_AND_ORAL_TEST);
        Exam examTest = new Exam(courseTest, "testData");

        testProfessor.setGrade(student, examTest, 30);

        conn = DBConnection.connect("../database/unicoachdb.db");

        // Verifica che l'esame venga inserito correttamente
        String sql = "SELECT * FROM Esame WHERE Codice = ?";
        PreparedStatement statement = conn.prepareStatement(sql);
        statement.setString(1, examTest.getId());

        ResultSet result = statement.executeQuery();

        assertTrue(result.next());
        assertEquals(examTest.getId(), result.getString("Codice"));
        assertEquals("TestCorso", result.getString("Nome"));
        assertEquals("testData", result.getString("Data"));
        assertEquals(6, result.getInt("CFU"));
        assertEquals(30, result.getInt("Voto"));
        assertEquals(courseTest.getId(), result.getString("Corso"));
        assertEquals(courseTest.getExamType().getDisplayName(), result.getString("TipoEsame"));

        //Si verifica anche che l'esame sia stato inserito nel libretto:
        assertEquals(student.getUniTranscript().getExam(examTest.getId()), examTest);

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
        int grade = professorTest.getGrade(studentTest);

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

//    @Test
//    public void testSendEmail() throws SQLException, MessagingException {
//        Professor professorTest = new Professor("12345", "testNome", "testCognome", "riccardo.becciolini00@gmail.com");
//        Student studentTest = new Student("12346", "Alessandro", "Bianco", "alessandro.bianco1608@gmail.com");
//        Course courseTest = new Course("TestCorso", 6, professorTest, ExamType.WRITTEN_AND_ORAL_TEST);
//
//        //Collega il corso al professore
//        professorTest.getProfessorGateway().setCourseId(professorTest);
//
//        LoginManager loginManager = new LoginManager("../database/unicoachdb.db");
//
//        // Prepara la password simulata
//        String simulatedInput1 = "fldiejclqrzckthd\n";
//        InputStream inputStream1 = new ByteArrayInputStream(simulatedInput1.getBytes());
//        System.setIn(inputStream1);
//
//        loginManager.addUser(professorTest);
//
//        studentTest.attach(courseTest);
//
//        professorTest.notifyObservers("Test Messaggio", "Test Oggetto", null);  //FIXME: passa attività!
//
//        conn = DBConnection.connect("../database/unicoachdb.db");
//
//        //Cancella l'utente inserito
//        String deleteUserSql = "DELETE FROM Utente WHERE Nome = ?";
//        PreparedStatement deleteUserStatement = conn.prepareStatement(deleteUserSql);
//        deleteUserStatement.setString(1, "testNome");
//        deleteUserStatement.executeUpdate();
//        deleteUserStatement.close();
//
//    }

    @Test
    public void testAddActivity() throws SQLException, MessagingException {
        Professor professorTest = new Professor("12345", "TestNome", "TestCognome", "riccardo.becciolini00@gmail.com");
        Student studentTest1 = new Student("12345", "TestNome", "TestCognome", "nibbiojr@gmail.com");
        Student studentTest2 = new Student("12346", "TestNome", "TestCognome", "alessandro.bianco1608@gmail.com");
        Course courseTest = new Course("TestCorso", 6, professorTest, ExamType.WRITTEN_AND_ORAL_TEST);

        //Eseguo registrazione professore per la mandare la mail
        LoginManager loginManager = new LoginManager("../database/unicoachdb.db");

        //Prepara la password simulata
        String simulatedInput1 = "fldiejclqrzckthd\n";
        InputStream inputStream1 = new ByteArrayInputStream(simulatedInput1.getBytes());
        System.setIn(inputStream1);

        loginManager.addUser(professorTest);

        //Iscrivo gli studenti al corso del professore
        studentTest1.attach(courseTest);
        studentTest2.attach(courseTest);

        //Il professore aggiunge la nuova data dell'esame (e quindi richiama il notifyObservers)
        Activity activity = professorTest.addExamDate("24/05/2023", 8, 10);

        professorTest.displayActivities();

        conn = DBConnection.connect("../database/unicoachdb.db");

        //Verifico che l'attività sia stata correttamente inserita nel calendario del professore
        String sql = "SELECT Attività, Data, OraInizio, OraFine, Matricola FROM CalendarioDocenti WHERE Id = ?";

        PreparedStatement statement = conn.prepareStatement(sql);
        statement.setString(1, activity.getId());

        ResultSet result = statement.executeQuery();

        assertTrue(result.next());
        assertEquals(activity.getName(), result.getString("Attività"));
        assertEquals(activity.getDate(), result.getString("Data"));
        assertEquals(activity.getStartTime(), result.getInt("OraInizio"));
        assertEquals(activity.getEndTime(), result.getInt("OraFine"));
        assertEquals(professorTest.getId(), result.getString("Matricola"));

        statement.close();

        //Cancella l'utente inserito
        String deleteUserSql = "DELETE FROM Utente WHERE Nome = ?";
        PreparedStatement deleteUserStatement = conn.prepareStatement(deleteUserSql);
        deleteUserStatement.setString(1, "TestNome");
        deleteUserStatement.executeUpdate();
        deleteUserStatement.close();

        //Elimino l'attività inserita per docente e studente
        String deleteActivitySql = "DELETE FROM CalendarioDocenti WHERE Id = ?";
        PreparedStatement deleteActivityProfessorStatement = conn.prepareStatement(deleteActivitySql);
        deleteActivityProfessorStatement.setString(1, activity.getId());
        deleteActivityProfessorStatement.executeUpdate();
        deleteActivityProfessorStatement.close();

        deleteActivitySql = "DELETE FROM CalendarioStudenti WHERE Attività = ?";
        PreparedStatement deleteActivityStatement = conn.prepareStatement(deleteActivitySql);
        deleteActivityStatement.setString(1, activity.getName());
        deleteActivityStatement.executeUpdate();
        deleteActivityStatement.close();

    }


    private Connection conn;
}