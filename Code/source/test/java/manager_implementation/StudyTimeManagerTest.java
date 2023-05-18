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
import java.io.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertTrue;

public class StudyTimeManagerTest {

    @Before
    public void setUp() throws SQLException {
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
        deleteProfessorStatement.close();

        //Elimina gli esami inseriti
        String deleteExamSql = "DELETE FROM Esame WHERE Nome = ?";
        PreparedStatement deleteExamStatement = conn.prepareStatement(deleteExamSql);
        deleteExamStatement.setString(1, "TestCorso1");
        deleteExamStatement.executeUpdate();
        deleteExamStatement.setString(1, "TestCorso2");
        deleteExamStatement.executeUpdate();
        deleteExamStatement.close();


        //Elimina i corsi inseriti
        String deleteCourseSql = "DELETE FROM Corso WHERE Nome = ?";
        PreparedStatement deleteCourseStatement = conn.prepareStatement(deleteCourseSql);
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
    public void testSetDailyStudyTime() throws SQLException, InterruptedException, MessagingException {
        Student student = new Student("12345", "TestNome", "TestCognome");

        Professor professor = new Professor("12345", "TestNome", "TestCognome");
        Professor professor2 = new Professor("12346", "TestNome", "TestCognome");

        Course courseTest1 = new Course("TestCorso1", 6, professor, ExamType.WRITTEN_AND_ORAL_TEST);
        Course courseTest2 = new Course("TestCorso2", 6, professor2, ExamType.WRITTEN_AND_ORAL_TEST);

        //TODO Magari farlo anche in altri test invece di richiamare choose course
        List<Course> selectedCourses = new ArrayList<>();
        selectedCourses.add(courseTest1);
        selectedCourses.add(courseTest2);
        student.getStudentGateway().linkStudentToCourse(selectedCourses, student);

        // Simuliamo l'input del primo corso
        String input1 = courseTest1.getName() + "\n" + "Lezione\n" + String.valueOf(1) + "\n";

        // Simuliamo l'input del secondo corso
        String input2 = courseTest2.getName() + "\n" + "Progetto\n" + String.valueOf(2) + "\n";

        // Inseriamo gli altri study type per il primo corso
        String input3 = courseTest1.getName() + "\n" + "Progetto\n" + String.valueOf(2) + "\n";
        String input4 = courseTest1.getName() + "\n" + "Studio per esame\n" + String.valueOf(3) + "\n";
        String input5 = courseTest1.getName() + "\n" + "Ripasso\n" + String.valueOf(2) + "\n0";

        // Creazione degli InputStream separati per ogni input
        InputStream inputStream1 = new ByteArrayInputStream(input1.getBytes());
        InputStream inputStream2 = new ByteArrayInputStream(input2.getBytes());
        InputStream inputStream3 = new ByteArrayInputStream(input3.getBytes());
        InputStream inputStream4 = new ByteArrayInputStream(input4.getBytes());
        InputStream inputStream5 = new ByteArrayInputStream(input5.getBytes());

        // Creazione dell'elenco degli InputStream
        Arrays Arrays = null;
        List<InputStream> inputStreams = Arrays.asList(inputStream1, inputStream2, inputStream3, inputStream4, inputStream5);

        // Enumerazione degli InputStream
        Enumeration<InputStream> enumeration = Collections.enumeration(inputStreams);

        // Creazione del SequenceInputStream
        InputStream combinedInput = new SequenceInputStream(enumeration);

        System.setIn(combinedInput);
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        //FIXME: si dovrebbe testare il metodo setDailyStudyTime() e non compileForm()!
        //StudyTimeManager.setDailyStudyTime(true);
        StudyTimeManager.compileForm(student);

        //Il professore visualizza le informazioni sullo studyTime del suo corso
        professor.getCourseStudyInfo(courseTest1);

        conn = DBConnection.connect("../database/unicoachdb.db");

        //Verifico che le informazioni inserite siano corrette per un corso soltanto
        String sql = "SELECT * FROM OreStudio WHERE Codice = ?";
        PreparedStatement statement = conn.prepareStatement(sql);
        statement.setString(1, student.getUniTranscript().findExamByName(courseTest1.getName()).getId());
        ResultSet resultCourse1 = statement.executeQuery();

//        resultCourse1.next();
//        assertEquals("Progetto", resultCourse1.getString("TipoStudio"));
//        assertEquals(1, resultCourse1.getInt("Ore"));
//        resultCourse1.next();
//        assertEquals("lezione", resultCourse1.getString("TipoStudio"));
//        assertEquals(2, resultCourse1.getInt("Ore"));

        statement.close();

        //Elimino le informazioni dalla tabella OreStudio
        String deleteSql = "DELETE FROM OreStudio WHERE Codice = ?";
        PreparedStatement deleteStatement = conn.prepareStatement(deleteSql);
        deleteStatement.setString(1, student.getUniTranscript().findExamByName(courseTest1.getName()).getId());
        deleteStatement.executeUpdate();
        deleteStatement.setString(1, student.getUniTranscript().findExamByName(courseTest2.getName()).getId());
        deleteStatement.executeUpdate();
        deleteStatement.close();

        //Elimino le informazioni dalla tabella IscrizioneCorso
        deleteSql = "DELETE FROM IscrizioneCorso WHERE IdStudente = ?";
        deleteStatement = conn.prepareStatement(deleteSql);
        deleteStatement.setString(1, student.getId());
        deleteStatement.executeUpdate();
        deleteStatement.close();

    }

    private Connection conn;
}
