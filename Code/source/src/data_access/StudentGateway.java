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
        String sql = "INSERT INTO Studente (Matricola, Nome, Cognome, Email, Libretto) VALUES (?, ?, ?, ?, ?)";

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

    void getGrade(Course course){}

    void  getAverage(){}

    private Connection connection = null;
}
