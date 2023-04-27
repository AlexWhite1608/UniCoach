package data_access;

import domain_model.Course;
import domain_model.Exam;
import domain_model.Professor;
import domain_model.Student;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class ProfessorGateway implements Gateway{

    public ProfessorGateway(Professor professor) throws SQLException {
        connection = DBConnection.connect();

        setCourseId(professor);
    }

    private void setCourseId(Professor professor) throws SQLException {
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
                SELECT Voto
                FROM Esame
                WHERE Corso = ? AND Studente = ?""";

        PreparedStatement gradeStatement = connection.prepareStatement(gradeSQL);
        gradeStatement.setString(1, this.courseID);
        gradeStatement.setString(2, student.getId());
        ResultSet gradeRs = gradeStatement.executeQuery();

        if (gradeRs.next()) {
            grade = gradeRs.getInt("Voto");
        } else {
            grade = -1; //valore di default per indicare che non c'è un voto per questo studente
        }

        gradeRs.close();
        gradeStatement.close();

        return grade;
    }

    public void setGrade(Student student, Exam exam, int grade) throws SQLException{
        String sql = "INSERT INTO Esame (Codice, Nome, Studente, Data, CFU, Voto, Corso, TipoEsame) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setString(1, exam.getId());
        statement.setString(2, exam.getName());
        statement.setString(3, student.getId());
        statement.setString(4, exam.getDate());
        statement.setInt(5, exam.getCFU());
        statement.setInt(6, grade);
        statement.setString(7, exam.getCourse().getId());
        statement.setString(8, exam.getExamType());

        statement.executeUpdate();
        statement.close();
    }

    // Ritorna la media su tutti gli esami dati dallo studente
    public float getAverage(Student student) throws SQLException, Exception{
        String average = """
                SELECT AVG(Voto)
                FROM Esame
                WHERE Esame.Studente = ?""";

        PreparedStatement averageStatement = connection.prepareStatement(average);
        averageStatement.setString(1, student.getId());

        ResultSet averageRs = averageStatement.executeQuery();

        float finalAverage;

        if (averageRs.next()) {
            finalAverage = averageRs.getFloat(1);
        } else {
            throw new Exception("Non sono presenti esami su cui eseguire la media!");
        }

        averageRs.close();
        averageStatement.close();

        return finalAverage;
    }


    public void getAverage(List<Student> students) throws SQLException{

    }

    public void getAverage(Course course) throws SQLException{
        String average = """
                SELECT AVG(Esame.Voto)
                FROM Corso Join Esame ON Corso.id = Esame.Corso
                WHERE Corso.id = ?
                """;
        PreparedStatement statement = connection.prepareStatement(average);
        statement.setString(1, course.getId());
    }

    private Connection connection = null;
    private String courseID;

}
