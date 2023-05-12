package data_access;

import domain_model.*;
import manager_implementation.Activity;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

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

    public List<Exam> getExams(Student student) throws SQLException{

        String sql = """
                SELECT Codice
                FROM Esame
                WHERE Studente = ?""";

        connection = DBConnection.connect("../database/unicoachdb.db");

        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setString(1, student.getId());
        ResultSet gradeRs = statement.executeQuery();

        List<Exam> examList = new ArrayList<>();

        while(gradeRs.next()){
            String examId = gradeRs.getString("Codice");
            examList.add(student.getUniTranscript().findExam(examId));
        }

        return examList;

    }

    // Ritorna il voto dello studente all'esame del corso fornito
    public int getGrade(Course course, Student student) throws SQLException{
        int grade;

        String gradeSQL = """
                SELECT Voto
                FROM Esame
                WHERE Corso = ? AND Studente = ?
                """;

        connection = DBConnection.connect("../database/unicoachdb.db");

        PreparedStatement statement = connection.prepareStatement(gradeSQL);
        statement.setString(1, course.getId());
        statement.setString(2, student.getId());
        ResultSet gradeRs = statement.executeQuery();

        if (gradeRs.next()) {
            grade = gradeRs.getInt("Voto");
        } else {
            grade = -1; //valore di default per indicare che non c'è un voto per questo studente
        }

        gradeRs.close();
        statement.close();

        return grade;
    }


    //Ritorna la media dello studente
    public float getAverage(Student student) throws SQLException {
        String average = """
            SELECT CAST(SUM(CAST(Esame.Voto AS FLOAT) * CAST(Corso.CFU AS FLOAT)) / SUM(CAST(Corso.CFU AS FLOAT)) AS FLOAT)
            FROM Esame
            JOIN Corso ON Esame.Corso = Corso.Codice
            WHERE Esame.Studente = ?;""";

        connection = DBConnection.connect("../database/unicoachdb.db");

        PreparedStatement averageStatement = connection.prepareStatement(average);

        averageStatement.setString(1, student.getId());

        ResultSet averageRs = averageStatement.executeQuery();

        float finalAverage;

        if (averageRs.next()) {
            finalAverage = averageRs.getFloat(1);
        } else {
            throw new SQLException("Non sono presenti esami su cui eseguire la media!");
        }

        averageRs.close();
        averageStatement.close();

        return finalAverage;
    }

    @Override
    public void addActivity(Activity activity, User student) throws SQLException {
        String sql = """
                INSERT INTO CalendarioStudenti(Attività, Data, OraInizio, OraFine, Matricola) VALUES (?, ?, ?, ?, ?)""";

        connection = DBConnection.connect("../database/unicoachdb.db");

        PreparedStatement statement = connection.prepareStatement(sql);

        statement.setString(1, activity.getName());
        statement.setString(2, activity.getDate());
        statement.setInt(3, activity.getStartTime());
        statement.setInt(4, activity.getEndTime());
        statement.setString(5, student.getId());

        statement.executeUpdate();
        statement.close();

        String select = """
                SELECT Id FROM CalendarioDocenti ORDER BY Id DESC LIMIT 1""";   //FIXME: ho fatto che l'attività nella tabella CalendarioStudenti prende lo stesso id della stessa attività in CalendarioDocenti

        connection = DBConnection.connect("../database/unicoachdb.db");

        PreparedStatement selectStatement = connection.prepareStatement(select);
        ResultSet resultSet = selectStatement.executeQuery();

        if(resultSet.next()){
            String id = resultSet.getString(1);
            activity.setId(id);
        } else throw new SQLException("Non è riuscito a inserire l'evento nel database");

        resultSet.close();
        selectStatement.close();

    }

    @Override
    public void removeActivity(Activity activity, User user) throws SQLException {
        String deleteLesson = "DELETE FROM CalendarioStudenti WHERE Data = ? AND Attività = ? AND Matricola = ? ";
        connection = DBConnection.connect("../database/unicoachdb.db");

        PreparedStatement deleteStatement = connection.prepareStatement(deleteLesson);
        deleteStatement.setString(1, activity.getDate());
        deleteStatement.setString(2, activity.getName());
        deleteStatement.setString(3, user.getId());
        deleteStatement.executeUpdate();
    }

    public void displayActivities(Student student) throws SQLException {
        String sql = """
                SELECT Attività, Data, OraInizio, OraFine
                FROM CalendarioStudenti
                WHERE Matricola = ?
                ORDER BY CAST(Data as date) ASC""";

        connection = DBConnection.connect("../database/unicoachdb.db");

        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setString(1, student.getId());

        ResultSet resultSet = statement.executeQuery();

        if (resultSet.next()) {
            String activity = resultSet.getString(1);
            String date = resultSet.getDate(2).toString();
            int startTime = resultSet.getInt(3);
            int endTime = resultSet.getInt(4);

            System.out.println("\nProssime attività di " + student.getName() + " " + student.getSurname());
            System.out.println("Nome attività: " + activity);
            System.out.println("Data: " + date);
            System.out.println("Ora inizio: " + startTime);
            System.out.println("Ora fine: " + endTime);
            System.out.println("---------\n");
        } else {
            throw new SQLException("Non sono presenti attività da mostrare");
        }

        resultSet.close();
        statement.close();
    }

    public void displayCourses() throws SQLException{
        String sql = "SELECT * FROM Corso";

        connection = DBConnection.connect("../database/unicoachdb.db");

        PreparedStatement statement = connection.prepareStatement(sql);
        ResultSet resultSet = statement.executeQuery();

        while (resultSet.next()) {
            String codice = resultSet.getString("Codice");
            String nome = resultSet.getString("Nome");
            int cfu = resultSet.getInt("CFU");
            String docente= resultSet.getString("Docente");
            String tipoEsame = resultSet.getString("TipoEsame");

            System.out.println("\nCODICE CORSO: " + codice);
            System.out.println("NOME CORSO: " + nome);
            System.out.println("CFU: " + cfu);
            System.out.println("DOCENTE: " + docente);
            System.out.println("TIPO ESAME: " + tipoEsame +"\n");
        }

        resultSet.close();

    }

    public void linkStudentToCourse(List<Course> courses, Student studente) throws SQLException{

        connection = DBConnection.connect("../database/unicoachdb.db");

        String sqlInsert = "INSERT INTO IscrizioneCorso(IdStudente, IdCorso) VALUES (?, ?)";
        PreparedStatement statementInsert = connection.prepareStatement(sqlInsert);
        statementInsert.setString(1, studente.getId());

        for(Course course : courses){
            statementInsert.setString(2, course.getId());
            statementInsert.executeUpdate();
            studente.attach(course);
        }

        statementInsert.close();
    }



    public void displayTranscript(Student studente) throws SQLException {

        connection = DBConnection.connect("../database/unicoachdb.db");

        String sql = """
                SELECT Nome, Data, CFU, Voto, TipoEsame
                FROM Esame
                WHERE Studente = ?""";
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setString(1, studente.getId());
        ResultSet resultSet = statement.executeQuery();

        int somma = 0;


        System.out.println("Esami conseguiti");

        while (resultSet.next()) {
            String nome = resultSet.getString("Nome");
            String data = resultSet.getString("Data");
            int cfu = resultSet.getInt("CFU");
            int voto = resultSet.getInt("Voto");
            String tipoEsame = resultSet.getString("TipoEsame");

            System.out.println("\nNome esame: " + nome);
            System.out.println("Conseguito il: " + data);
            System.out.println("CFU: " + cfu);
            System.out.println("Voto preso: " + voto);
            System.out.println("Tipo di valutazione: " + tipoEsame + "\n");

            somma += cfu;

        }

        System.out.println("CFU convalidati " + somma + "/180");

        resultSet.close();

    }

    private Connection connection = null;
}
