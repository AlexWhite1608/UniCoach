package data_access;

import domain_model.Course;
import domain_model.Professor;
import domain_model.Student;
import domain_model.Exam;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class StudentGateway implements Gateway {

    public StudentGateway() {
        connection = DBConnection.connect();
    }

    public void addStudent(Student student) throws SQLException {
        String sql = "INSERT OR IGNORE INTO Studente (Matricola, Nome, Cognome, Email, Libretto) VALUES (?, ?, ?, ?, ?)";

        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setString(1, student.getId());
        statement.setString(2, student.getName());
        statement.setString(3, student.getSurname());
        statement.setString(4, student.getEmail());
        statement.setString(5, student.getUniTranscript().getId());

        statement.executeUpdate();
        statement.close();

        // Si inserisce anche il corrispettivo libretto
        String transcriptSql = "INSERT OR IGNORE INTO Libretto (Codice) VALUES (?)";

        PreparedStatement transcriptStatement = connection.prepareStatement(transcriptSql);
        transcriptStatement.setString(1, student.getUniTranscript().getId());

        transcriptStatement.executeUpdate();
        transcriptStatement.close();
    }

    public void addExam(Student student, Exam exam) throws SQLException {
        String sql = "INSERT OR IGNORE INTO Esame (Codice, Nome, Data, CFU, Voto, Corso, TipoEsame) VALUES (?, ?, ?, ?, ?, ?, ?)";

        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setString(1, exam.getId());
        statement.setString(2, exam.getName());
        statement.setString(3, exam.getDate());
        statement.setInt(4, exam.getCFU());
        statement.setInt(5, exam.getGrade());
        statement.setString(6, exam.getCourse().getId());
        statement.setString(7, exam.getExamType());

        statement.executeUpdate();
        statement.close();

        //Aggiungo il corso relativo all'esame
        String courseSql = "INSERT OR IGNORE INTO Corso (Codice, Nome, CFU, Docente , TipoEsame) VALUES (?, ?, ?, ?, ?)";

        PreparedStatement courseStatement = connection.prepareStatement(courseSql);
        courseStatement.setString(1, exam.getCourse().getId());
        courseStatement.setString(2, exam.getCourse().getName());
        courseStatement.setInt(3, exam.getCFU());
        courseStatement.setString(4, exam.getCourse().getProfessor().getId());
        courseStatement.setString(5, exam.getCourse().getExamType().getDisplayName());

        courseStatement.executeUpdate();
        courseStatement.close();

        //Aggiungo l'esame al libretto dello studente
        String transcriptSql = """
                UPDATE Libretto
                SET Esame = ?
                WHERE Codice = ?""";

        PreparedStatement transcriptStatement = connection.prepareStatement(transcriptSql);
        transcriptStatement.setString(1, exam.getId());
        transcriptStatement.setString(2, student.getUniTranscript().getId());

        transcriptStatement.executeUpdate();
        transcriptStatement.close();
    }

    void getGrade(Course course){}

    void  getAverage(){}

    private Connection connection = null;
}
