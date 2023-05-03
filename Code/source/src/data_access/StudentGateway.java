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
        connection = DBConnection.connect("../database/unicoachdb.db");
    }

    public void addStudent(Student student) throws SQLException {
        String sql = "INSERT INTO Studente (Matricola, Nome, Cognome, Email) VALUES (?, ?, ?, ?)";

        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setString(1, student.getId());
        statement.setString(2, student.getName());
        statement.setString(3, student.getSurname());
        statement.setString(4, student.getEmail());

        statement.executeUpdate();
        statement.close();
    }

    // Ritorna il voto dello studente all'esame del corso fornito
    void getGrade(Course course){

    }

    // Ritorna la media dello studente
    void getAverage(){}

    private Connection connection = null;
}
