package manager;

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

    //TODO tastare lato studente, altrimenti togliere il secondo corso
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

        professor.setGrade(student, 25, "testData", false);
        professor2.setGrade(student, 27, "testData", false);

        // Simuliamo l'input del primo corso
        String input1 = courseTest1.getName() + "\n" + "Lezione\n" + String.valueOf(1) + "\n" + "Progetto\n" + String.valueOf(2) + "\n" + "Ripasso\n" + String.valueOf(1) + "\n0\n";

        // Simuliamo un nuovo giorno, dove si studia lo stesso corso in modo da sommare le nuove ore
        String input2 = courseTest1.getName() + "\n" + "Lezione\n" + String.valueOf(3) + "\n" + "Progetto\n" + String.valueOf(1) + "\n" + "Ripasso\n" + String.valueOf(1) + "\n0\n";

        // Simuliamo l'input del secondo corso
        String input3 = courseTest2.getName() + "\n" + "Lezione\n" + String.valueOf(2) + "\n" + "Progetto\n" + String.valueOf(2) + "\n" + "Ripasso\n" + String.valueOf(1) + "\n0\n";

        // Simuliamo un nuovo giorno, dove si studia lo stesso corso in modo da sommare le nuove ore
        String input4 = courseTest2.getName() + "\n" + "Lezione\n" + String.valueOf(3) + "\n" + "Progetto\n" + String.valueOf(2) + "\n" + "Ripasso\n" + String.valueOf(3) + "\n" + "Studio per esame\n" + String.valueOf(2) + "\n0\n";

        // Concatena gli input in un unico stream da passare alla funzione
        InputStream combinedInput = new SequenceInputStream(
                new ByteArrayInputStream((input1 + input2).getBytes()),
                new ByteArrayInputStream((input3 + input4).getBytes())

        );

        System.setIn(combinedInput);
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        //FIXME: si dovrebbe testare il metodo setDailyStudyTime() e non compileForm()!
        //StudyTimeManager.setDailyStudyTime(true);
        StudyTimeManager.compileForm(student);

        //Il professore visualizza le informazioni sullo studyTime del suo corso
        professor.getCourseStudyInfo(courseTest1);

        //Lo studente visualizza le proprie informazioni voti esame/study type
        student.getStudyInfo();

        conn = DBConnection.connect("../database/unicoachdb.db");

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
