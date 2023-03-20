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

    public void getGrade(Student student) throws SQLException {

        int grade = 0;

        //SQL per ottenere il voto dello studente fornito
        String gradeSQL = """
                SELECT esame.voto\s
                FROM Esame\s
                WHERE Studente = ? AND Corso = ?""";

        PreparedStatement gradeStatement = connection.prepareStatement(gradeSQL);
        gradeStatement.setString(1, student.getId());
        gradeStatement.setString(2, this.courseID);
        ResultSet gradeRs = gradeStatement.executeQuery();

        grade = gradeRs.getInt("voto");

        gradeRs.close();
        gradeStatement.close();

        System.out.println("Lo studente " + student.getId() + " ha preso " + grade + " all'esame con codice " + this.courseID);

        //FIXME: deve ritornare un oggetto di tipo Exam?
    }

    // Ritorna il voto di ciascuno studente fornito
    //FIXME: non è giusto il codice per ricavare tutte le matricole!
    public void getGrade(ArrayList<Student> students) throws SQLException {

        int grade = 0;

        //SQL per ottenere il voto dello studente fornito
        String gradeSQL = """
                SELECT esame.voto\s
                FROM Esame\s
                WHERE Corso = ? AND Studente IN (?)""";

        PreparedStatement gradeStatement = connection.prepareStatement(gradeSQL);

        // Otteniamo la matricola di tutti gli studenti forniti in input
        List<String> studentsId = new ArrayList<>(students.size());

        for (Student s: students ) {
            studentsId.add(s.getId());
        }

        gradeStatement.setString(1, this.courseID);
        gradeStatement.setString(2, String.join(", ", studentsId));
        ResultSet gradeRs = gradeStatement.executeQuery();

        int voto = gradeRs.getInt("voto");
        System.out.println(voto);

        gradeRs.close();
        gradeStatement.close();

        //FIXME: deve ritornare una lista di Exam?
    }

    // Ritorna il voto di tutti gli studenti del corso del professore
    public void getGrade(Course course){}

    public void setExamDate(Exam exam, String date){}

    public void getAverage(Student student){}

    public void getAverage(List<Student> students){}

    public void getAverage(Course course){}

    private Connection connection = null;
    private String courseID;

}
