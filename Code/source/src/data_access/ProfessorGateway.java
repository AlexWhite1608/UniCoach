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

    public ProfessorGateway() {
        connection = DBConnection.connect();
    }

    public void getGrade(Student student, Professor professor) throws SQLException {

        //SQL per ottenere il corso del professore
        String courseName = "";

        String docSql = "SELECT Corso.Nome FROM Corso JOIN Docente ON Corso.Docente = Docente.Matricola WHERE Docente.Matricola = ?";
        PreparedStatement docStatement = connection.prepareStatement(docSql);
        docStatement.setInt(1, professor.getId());
        ResultSet docRs = docStatement.executeQuery();


        courseName = docRs.getString("Nome");


        docRs.close();
        docStatement.close();

        int grade = 0;

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

        System.out.println("Lo studente " + student.getId() + " ha preso " + grade + " all'esame di " + courseName);

        //FIXME: deve ritornare un oggetto di tipo Exam?
    }

    void getGrade(List<Student> students){}

    void getGrade(Course course){}

    void setExamDate(Exam exam, String date){}

    void getAverage(Student student){}

    void getAverage(List<Student> students){}

    void getAverage(Course course){}

    private Connection connection = null;

}
