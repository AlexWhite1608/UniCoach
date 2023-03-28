package data_access;

import domain_model.Course;
import domain_model.Exam;
import domain_model.Professor;
import domain_model.Student;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ProfessorGateway implements Gateway{

    public ProfessorGateway(Professor professor) throws SQLException {
        connection = DBConnection.connect();

        getCourse(professor);
    }

    private void getCourse(Professor professor) throws SQLException {
        //SQL per ottenere il corso del professore
        String docSql = "SELECT codice FROM Corso WHERE docente = ?";
        PreparedStatement docStatement = connection.prepareStatement(docSql);
        docStatement.setString(1, professor.getId());
        ResultSet docRs = docStatement.executeQuery();

        this.courseID = docRs.getString("Codice");

        docRs.close();
        docStatement.close();
    }

    public void addProfessor(Professor professor) throws SQLException {
        String sql = "INSERT OR IGNORE INTO Docente (Matricola, Nome, Cognome, Email) VALUES (?, ?, ?, ?)";

        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setString(1, professor.getId());
        statement.setString(2, professor.getName());
        statement.setString(3, professor.getSurname());
        statement.setString(4, professor.getEmail());

        statement.executeUpdate();
        statement.close();
    }

    public int getGrade(Student student) throws SQLException {

        int grade;

        //SQL per ottenere il voto dello studente fornito
        String gradeSQL = """
            SELECT Esame.Voto
            FROM Esame
            JOIN Libretto ON Esame.Codice = Libretto.Esame
            JOIN Studente ON Libretto.Codice = Studente.Libretto
            WHERE Esame.Corso = ? AND Studente.Matricola = ?""";

        PreparedStatement gradeStatement = connection.prepareStatement(gradeSQL);
        gradeStatement.setString(1, this.courseID);
        gradeStatement.setString(2, student.getId());
        ResultSet gradeRs = gradeStatement.executeQuery();

        if (gradeRs.next()) {
            grade = gradeRs.getInt("Voto");

            System.out.println("Lo studente " + student.getId() + " ha preso " + grade + " all'esame con codice " + this.courseID);
        } else {
            grade = -1; //valore di default per indicare che non c'è un voto per questo studente
            System.out.println("Lo studente " + student.getId() + " non ha un voto per l'esame con codice " + this.courseID);
        }

        gradeRs.close();
        gradeStatement.close();

        return grade;
    }

    public void setExamDate(Exam exam, String date){}

    public void getAverage(Student student){}

    public void getAverage(List<Student> students){}

    public void getAverage(Course course){}

    private Connection connection = null;
    private String courseID;

}
