package domain_model;

import controller.Controller;
import data_access.DBConnection;
import manager.Activity;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import manager.LoginManager;
import javax.mail.MessagingException;
import javax.naming.directory.InvalidAttributesException;
import javax.naming.ldap.Control;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertFalse;

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

        //Cancella l'utente inserito
        String deleteUserSql = "DELETE FROM Utente WHERE Nome = ?";
        PreparedStatement deleteUserStatement = conn.prepareStatement(deleteUserSql);
        deleteUserStatement.setString(1, "TestNome");
        deleteUserStatement.executeUpdate();
        deleteUserStatement.setString(1, "TestNome1");
        deleteUserStatement.executeUpdate();
        deleteUserStatement.setString(1, "TestNome2");
        deleteUserStatement.executeUpdate();
        deleteUserStatement.setString(1, "TestNome3");
        deleteUserStatement.executeUpdate();
        deleteUserStatement.close();

        if (conn != null) {
            conn = DBConnection.disconnect();
        }
    }

    @Test
    public void testAddProfessor() throws SQLException {
        Professor professor = new Professor("12345", "TestNome1", "TestCognome1");

        Controller professorController = new Controller(professor);

        // Verifica che lo professore sia stato effettivamente aggiunto al database
        String sql = "SELECT * FROM Docente WHERE Matricola = ?";
        PreparedStatement statement = conn.prepareStatement(sql);
        statement.setString(1, "12345");
        ResultSet result = statement.executeQuery();

        assertTrue(result.next());
        assertEquals("TestNome1", result.getString("Nome"));
        assertEquals("TestCognome1", result.getString("Cognome"));
        assertEquals("12345", result.getString("Matricola"));

        statement.close();
    }

    @Test
    public void testAverageStudent() throws SQLException, MessagingException {
        Student student = new Student("12346", "TestNome", "TestCognome");
        Professor professor = new Professor("12345", "TestNome1", "TestCognome1");
        Professor professor2 = new Professor("12346", "TestNome2", "TestCognome2");

        Controller professorController = new Controller(professor);
        Controller professorController2 = new Controller(professor2);
        Controller studentController = new Controller(student);


        Course courseTest1 = new Course("TestCorso1", 6, professor, ExamType.WRITTEN_AND_ORAL_TEST);
        Course courseTest2 = new Course("TestCorso2", 6, professor2, ExamType.WRITTEN_AND_ORAL_TEST);

        List<Course> courseList = new ArrayList<>();
        courseList.add(courseTest1);
        courseList.add(courseTest2);
        studentController.getStudentGateway().linkStudentToCourse(courseList, student);

        professorController.setGrade(student,25, "TestData", false);
        professorController2.setGrade(student,24, "TestData", false);

        float average = ( 25 * 6 + 24 * 6 ) / 12f;

        assertEquals(average, professorController.getAverage(student), 0.0001f);

        conn = DBConnection.connect("../database/unicoachdb.db");

        //Elimino i corsi in IscrizioneCorso
        String deleteCourseSql = "DELETE FROM IscrizioneCorso WHERE IdCorso = ?";
        PreparedStatement deleteCourseStatement = conn.prepareStatement(deleteCourseSql);
        deleteCourseStatement.setString(1, courseTest1.getId());
        deleteCourseStatement.executeUpdate();
        deleteCourseStatement.setString(1, courseTest2.getId());
        deleteCourseStatement.executeUpdate();
        deleteCourseStatement.close();
    }

    @Test
    public void testAverageCourse() throws SQLException, MessagingException {
        Student student1 = new Student("12345", "TestNome1", "TestCognome");
        Student student2 = new Student("12346", "TestNome2", "TestCognome");
        Student student3 = new Student("12347", "TestNome3", "TestCognome");

        Professor professor = new Professor("12345", "TestNome", "TestCognome");
        Course courseTest1 = new Course("TestCorso1", 6, professor, ExamType.WRITTEN_AND_ORAL_TEST);

        Controller professorController = new Controller(professor);
        Controller studentController1 = new Controller(student1);
        Controller studentController2 = new Controller(student2);
        Controller studentController3 = new Controller(student3);


        List<Course> courseList = new ArrayList<>();
        courseList.add(courseTest1);
        studentController1.getStudentGateway().linkStudentToCourse(courseList, student1);
        studentController2.getStudentGateway().linkStudentToCourse(courseList, student2);
        studentController3.getStudentGateway().linkStudentToCourse(courseList, student3);

        professorController.setGrade(student1,22 , "dataTest", false);
        professorController.setGrade(student2, 28 , "dataTest", false);
        professorController.setGrade(student3,21, "dataTast", false);

        float average = ( 22 * 6 + 28 * 6 + 21 * 6 ) / 18f;

        assertEquals(average, professorController.getAverage(), 0.0001f);

        conn = DBConnection.connect("../database/unicoachdb.db");

        //Elimino i corsi in IscrizioneCorso
        String deleteCourseSql = "DELETE FROM IscrizioneCorso WHERE IdCorso = ?";
        PreparedStatement deleteCourseStatement = conn.prepareStatement(deleteCourseSql);
        deleteCourseStatement.setString(1, courseTest1.getId());
        deleteCourseStatement.executeUpdate();
        deleteCourseStatement.close();
    }

    @Test
    public void testSetGetGrade() throws SQLException, MessagingException {
        Professor professorTest = new Professor("12345", "TestNome1", "TestCognome1", "professor.unicoach@gmail.com");
        Student studentTest = new Student("12345", "TestNome2", "TestCognome2", "unicoach2023@gmail.com");
        Course courseTest = new Course("TestCorso", 6, professorTest, ExamType.WRITTEN_AND_ORAL_TEST);

        Controller professorController = new Controller(professorTest);
        Controller studentController = new Controller(studentTest);

        LoginManager loginManager = new LoginManager("../database/unicoachdb.db");

        // Simuliamo l'inserimento della password dell'utente
        String input = "knpjdyxlkuzjetfx\n";
        InputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        loginManager.addUser(professorTest);

        //Colleghiamo il corso allo studente
        List<Course> courseList = new ArrayList<>();
        courseList.add(courseTest);
        studentController.getStudentGateway().linkStudentToCourse(courseList, studentTest);

        professorController.setGrade(studentTest, 22, "dataTest", true);
        int grade = professorController.getGrade(studentTest);

        conn = DBConnection.connect("../database/unicoachdb.db");

        // Verifica che l'esame venga inserito correttamente
        String sql = "SELECT Voto FROM Esame WHERE Corso = ?";

        PreparedStatement statement = conn.prepareStatement(sql);
        statement.setString(1, courseTest.getId());

        ResultSet result = statement.executeQuery();

        assertTrue(result.next());
        assertEquals(grade, result.getInt("Voto"));

        statement.close();

        //Elimino i corsi in IscrizioneCorso
        String deleteCourseSql = "DELETE FROM IscrizioneCorso WHERE IdCorso = ?";
        PreparedStatement deleteCourseStatement = conn.prepareStatement(deleteCourseSql);
        deleteCourseStatement.setString(1, courseTest.getId());
        deleteCourseStatement.executeUpdate();
        deleteCourseStatement.close();
    }

    @Test
    public void testAddActivity() throws SQLException, MessagingException {
        Professor professorTest = new Professor("12345", "TestNome1", "TestCognome1", "professor.unicoach@gmail.com");
        Student studentTest1 = new Student("12345", "TestNome2", "TestCognome2", "unicoach2023@gmail.com");
        Student studentTest2 = new Student("12346", "TestNome3", "TestCognome3", "unicoach2023@gmail.com");
        Course courseTest = new Course("TestCorso", 6, professorTest, ExamType.WRITTEN_AND_ORAL_TEST);

        Controller professorController = new Controller(professorTest);
        Controller studentController = new Controller(studentTest1);
        Controller studentController2 = new Controller(studentTest2);

        //Eseguo registrazione professore per la mandare la mail
        LoginManager loginManager = new LoginManager("../database/unicoachdb.db");

        //Prepara la password simulata
        String simulatedInput1 = "knpjdyxlkuzjetfx\n";
        InputStream inputStream1 = new ByteArrayInputStream(simulatedInput1.getBytes());
        System.setIn(inputStream1);

        loginManager.addUser(professorTest);

        //Iscrivo gli studenti al corso del professore
        studentTest1.attach(courseTest);
        studentTest2.attach(courseTest);

        //Il professore aggiunge la nuova data dell'esame (e quindi richiama il notifyObservers)
        Activity activity = professorController.addExamDate("24/05/2023", 8, 10);

        professorController.displayActivities(professorTest);

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

    @Test
    public void testScheduleLesson() throws SQLException, InvalidAttributesException, MessagingException {
        Professor professorTest = new Professor("12345", "TestNome1", "TestCognome1", "professor.unicoach@gmail.com");
        Student studentTest1 = new Student("12345", "TestNome2", "TestCognome2", "unicoach2023@gmail.com");
        Student studentTest2 = new Student("12346", "TestNome3", "TestCognome", "unicoach2023@gmail.com");
        Course courseTest = new Course("TestCorso", 6, professorTest, ExamType.WRITTEN_AND_ORAL_TEST);

        Controller professorController = new Controller(professorTest);

        //Eseguo registrazione professore per la mandare la mail
        LoginManager loginManager = new LoginManager("../database/unicoachdb.db");

        //Prepara la password simulata
        String simulatedInput1 = "knpjdyxlkuzjetfx\n";
        InputStream inputStream1 = new ByteArrayInputStream(simulatedInput1.getBytes());
        System.setIn(inputStream1);

        loginManager.addUser(professorTest);

        //Iscrivo gli studenti al corso del professore
        studentTest1.attach(courseTest);
        studentTest2.attach(courseTest);

        //Ritorna tutte le lezioni a partire dalla data inserita (una lezione a settimana)
        List<Activity> activityList = professorController.scheduleLessons(12, 4, 2023, 10, 12);

        conn = DBConnection.connect("../database/unicoachdb.db");

        //Verifico che tutte le lezioni siano correttamente inserite
        String sql = "SELECT Attività, Data, OraInizio, OraFine, Matricola FROM CalendarioDocenti WHERE Id = ?";

        PreparedStatement statement = conn.prepareStatement(sql);
        for (Activity activity : activityList) {
            statement.setString(1, activity.getId());
            ResultSet result = statement.executeQuery();

            assertTrue(result.next());
            assertEquals(activity.getName(), result.getString("Attività"));
            assertEquals(activity.getDate(), result.getString("Data"));
            assertEquals(activity.getStartTime(), result.getInt("OraInizio"));
            assertEquals(activity.getEndTime(), result.getInt("OraFine"));
            assertEquals(professorTest.getId(), result.getString("Matricola"));
        }

        statement.close();

        //Elimino le lezioni inserite
        String deleteSql = "DELETE FROM CalendarioDocenti WHERE Id = ?";

        PreparedStatement deleteStatement = conn.prepareStatement(deleteSql);
        for (Activity activity : activityList) {
            deleteStatement.setString(1, activity.getId());
            deleteStatement.executeUpdate();
        }

        deleteSql = "DELETE FROM CalendarioStudenti WHERE Attività = ?";

        deleteStatement = conn.prepareStatement(deleteSql);
        for (Activity activity : activityList) {
            deleteStatement.setString(1, activity.getName());
            deleteStatement.executeUpdate();
        }

        deleteStatement.close();
    }

    @Test
    public void testRemoveLesson() throws SQLException, MessagingException, InvalidAttributesException {
        Professor professorTest = new Professor("12345", "TestNome1", "TestCognome1", "professor.unicoach@gmail.com");
        Student studentTest1 = new Student("12345", "TestNome2", "TestCognome2", "unicoach2023@gmail.com");
        Student studentTest2 = new Student("12346", "TestNome3", "TestCognome3", "unicoach2023@gmail.com");
        Course courseTest = new Course("TestCorso", 6, professorTest, ExamType.WRITTEN_AND_ORAL_TEST);

        Controller professorController = new Controller(professorTest);
        Controller studentContoller1 = new Controller(studentTest1);
        Controller studentContoller2 = new Controller(studentTest2);

        //Eseguo registrazione professore per la mandare la mail
        LoginManager loginManager = new LoginManager("../database/unicoachdb.db");

        //Prepara la password simulata
        String simulatedInput1 = "knpjdyxlkuzjetfx\n";
        InputStream inputStream1 = new ByteArrayInputStream(simulatedInput1.getBytes());
        System.setIn(inputStream1);

        loginManager.addUser(professorTest);

        //Iscrivo gli studenti al corso del professore
        studentTest1.attach(courseTest);
        studentTest2.attach(courseTest);

        //Ritorna tutte le lezioni a partire dalla data inserita (una lezione a settimana)
        List<Activity> activityList = professorController.scheduleLessons(12, 4, 2023, 10, 12);

        //Rimuovo una lezione
        professorController.removeLesson(3, 5, 2023);

        conn = DBConnection.connect("../database/unicoachdb.db");

        //Verifico che la lezione sia rimossa da entrambi i calendari
        String sql = "SELECT * FROM CalendarioDocenti WHERE Data = ? AND Matricola = ?";

        PreparedStatement statement = conn.prepareStatement(sql);
        statement.setString(1, "3/5/2023");
        statement.setString(2, professorTest.getId());

        ResultSet result = statement.executeQuery();
        assertFalse(result.next());

        sql = "SELECT * FROM CalendarioStudenti WHERE Data = ? AND Matricola = ?";
        statement = conn.prepareStatement(sql);
        statement.setString(1, "3/5/2023");
        statement.setString(2, studentTest1.getId());

        result = statement.executeQuery();
        assertFalse(result.next());

        statement = conn.prepareStatement(sql);
        statement.setString(1, "3/5/2023");
        statement.setString(2, studentTest2.getId());

        result = statement.executeQuery();
        assertFalse(result.next());

        statement.close();

        //Elimino le lezioni inserite
        String deleteSql = "DELETE FROM CalendarioDocenti WHERE Id = ?";

        PreparedStatement deleteStatement = conn.prepareStatement(deleteSql);
        for (Activity activity : activityList) {
            deleteStatement.setString(1, activity.getId());
            deleteStatement.executeUpdate();
        }

        deleteSql = "DELETE FROM CalendarioStudenti WHERE Attività = ?";

        deleteStatement = conn.prepareStatement(deleteSql);
        for (Activity activity : activityList) {
            deleteStatement.setString(1, activity.getName());
            deleteStatement.executeUpdate();
        }

        deleteStatement.close();
    }

    private Connection conn;
}