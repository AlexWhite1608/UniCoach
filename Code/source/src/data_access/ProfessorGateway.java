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

        getCourse(professor);
    }

    private void getCourse(Professor professor) throws SQLException {
        //SQL per ottenere il corso del professore
        String docSql = "SELECT Corso.Nome FROM Corso JOIN Docente ON Corso.Docente = Docente.Matricola WHERE Docente.Matricola = ?";
        PreparedStatement docStatement = connection.prepareStatement(docSql);
        docStatement.setInt(1, professor.getId());
        ResultSet docRs = docStatement.executeQuery();

        this.courseName = docRs.getString("Nome");

        docRs.close();
        docStatement.close();
    }

    public void addProfessor(Professor professor) throws SQLException {
        String sql = "INSERT OR IGNORE INTO Docente (Matricola, Nome, Cognome, Email) VALUES (?, ?, ?, ?)";

        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setInt(1, professor.getId());
        statement.setString(2, professor.getName());
        statement.setString(3, professor.getSurname());
        statement.setString(4, professor.getEmail());

        statement.executeUpdate();
        statement.close();
    }

    public void getGrade(Student student) throws SQLException {

        int grade = 0;

        //SQL per ottenere il voto dello studente fornito
        String gradeSQL = """
            SELECT esame.voto\s
            FROM Studente\s
            JOIN Libretto ON Studente.matricola = Libretto.studente\s
            JOIN Esame ON Libretto.codice = Esame.codice\s
            WHERE Studente.nome = ?""";

        PreparedStatement gradeStatement = connection.prepareStatement(gradeSQL);
        gradeStatement.setString(1, student.getName());
        ResultSet gradeRs = gradeStatement.executeQuery();

        grade = gradeRs.getInt("voto");

        gradeRs.close();
        gradeStatement.close();

        System.out.println("Lo studente " + student.getId() + " ha preso " + grade + " all'esame di " + this.courseName);

        //FIXME: deve ritornare un oggetto di tipo Exam?
    }

    // Ritorna il voto di ciascuno studente fornito
    void getGrade(List<Student> students){

    }

    void getGrade(Course course){}

    void setExamDate(Exam exam, String date){}

    void getAverage(Student student){}

    void getAverage(List<Student> students){}

    void getAverage(Course course){}

    private Connection connection = null;
    private String courseName;

}
